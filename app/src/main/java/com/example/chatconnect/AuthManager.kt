package com.example.chatconnect

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

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
}

