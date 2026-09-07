package com.example.chatconnect

import android.app.Activity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

object AuthManager {

    private val auth = FirebaseAuth.getInstance()

    fun signUpWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.sendEmailVerification()
                onResult(true, null)
            }
            .addOnFailureListener { onResult(false, friendlyMessage(it)) }
    }

    fun checkEmailVerified(onResult: (Boolean, String?) -> Unit ){
        val user = auth.currentUser
        if (user == null){
            onResult(false, "User is not logged in")
            return
        }
        user.reload()
            .addOnSuccessListener {
                if (auth.currentUser?.isEmailVerified == true){
                    onResult(true, null)
                }else{
                    onResult(false, "Email is not verified")
                }
            }
            .addOnFailureListener { onResult(false, friendlyMessage(it))}
    }

    fun resendVerification(onResult: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            onResult(false, "User is not logged in")
            return
        }
        user.sendEmailVerification()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, friendlyMessage(it)) }
    }

    fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, friendlyMessage(it)) }
    }
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        isResend: Boolean = false,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoVerified: () -> Unit,
        onError: (String) -> Unit
    ) {
        val codeSentCallback = onCodeSent

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneCredential(credential) { success, error ->
                    if (success) onAutoVerified() else onError(error ?: "Sign in failed")
                }
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                onError(friendlyMessage(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                resendToken = token
                codeSentCallback(verificationId)
            }
        }

        val builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)

        if (isResend) resendToken?.let { builder.setForceResendingToken(it) }

        PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

    fun verifyOtp(
        verificationId: String,
        code: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneCredential(credential, onResult)
    }

    private fun signInWithPhoneCredential(
        credential: PhoneAuthCredential,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, friendlyMessage(it)) }
    }
    fun signInWithGoogleIdToken(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, friendlyMessage(it)) }
    }


    fun currentUser() = auth.currentUser

    fun signOut() = auth.signOut()

    private fun friendlyMessage(e: Exception): String {
        if (e is FirebaseNetworkException) {
            return "No internet connection. Check your network and try again."
        }
        if (e is FirebaseTooManyRequestsException) {
            return "Too many attempts. Please wait a while before trying again."
        }

        val code = (e as? FirebaseAuthException)?.errorCode
        val raw = e.message.orEmpty()

        return when {
            code == "ERROR_INVALID_CREDENTIAL" ||
                code == "ERROR_WRONG_PASSWORD" ||
                code == "ERROR_USER_NOT_FOUND" ->
                "Email or password is incorrect."

            code == "ERROR_INVALID_EMAIL" -> "That email address doesn't look right."
            code == "ERROR_USER_DISABLED" -> "This account has been disabled."
            code == "ERROR_EMAIL_ALREADY_IN_USE" ->
                "An account with this email already exists. Try logging in instead."
            code == "ERROR_WEAK_PASSWORD" -> "Password is too weak - use at least 6 characters."
            code == "ERROR_INVALID_PHONE_NUMBER" ->
                "That phone number isn't valid. Include the country code, e.g. +91XXXXXXXXXX."
            code == "ERROR_INVALID_VERIFICATION_CODE" -> "That OTP is incorrect."
            code == "ERROR_SESSION_EXPIRED" -> "The OTP has expired. Request a new one."
            code == "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" ->
                "This email is already registered with a different sign-in method."

            raw.contains("region enabled by the app developer", ignoreCase = true) ->
                "OTP sign-in isn't available for this country yet. Please log in with email instead."

            raw.contains("BILLING_NOT_ENABLED", ignoreCase = true) ->
                "OTP sign-in isn't available right now. Please log in with email instead."

            code == "ERROR_OPERATION_NOT_ALLOWED" ->
                "This sign-in method is turned off for this app."

            raw.isBlank() -> "Something went wrong. Please try again."
            else -> raw.substringBefore(" [").trim()
        }
    }
}
