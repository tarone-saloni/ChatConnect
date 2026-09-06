package com.example.chatconnect

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
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
            .addOnFailureListener { onResult(false, it.message) }
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
            .addOnFailureListener { onResult(false, it.message)}
    }

    fun resendVerification(onResult: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            onResult(false, "User is not logged in")
            return
        }
        user.sendEmailVerification()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.message) }
    }

    fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.message) }
    }

    // ---------------- Phone (OTP) ----------------

    /** Last resend token, so that "Resend OTP" works. */
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    /**
     * Sends an OTP to the given phone number.
     * @param phoneNumber with country code, e.g. "+919999999999"
     * @param onCodeSent gives back the verificationId, pass it to the OTP screen
     * @param onAutoVerified some phones read the OTP themselves -> sign in directly
     */
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        isResend: Boolean = false,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoVerified: () -> Unit,
        onError: (String) -> Unit
    ) {
        // local copy, otherwise the name clashes inside the override below
        val codeSentCallback = onCodeSent

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneCredential(credential) { success, error ->
                    if (success) onAutoVerified() else onError(error ?: "Sign in failed")
                }
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                onError(e.message ?: "Could not send the OTP")
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

    /** Verifies the 6 digit code the user typed and signs them in. */
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
            .addOnFailureListener { onResult(false, it.message) }
    }

    // ---------------- Google ----------------

    /** Signs in to Firebase with the idToken returned by Google. */
    fun signInWithGoogleIdToken(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.message) }
    }

    // ---------------- Common ----------------

    fun currentUser() = auth.currentUser

    fun signOut() = auth.signOut()
}
