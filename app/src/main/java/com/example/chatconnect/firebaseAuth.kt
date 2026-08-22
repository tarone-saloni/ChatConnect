package com.example.chatconnect

import com.google.firebase.auth.FirebaseAuth

object AuthManager {
    val auth = FirebaseAuth.getInstance()

    fun signUpWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, task.exception?.message)
            }
    }

    fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, task.exception?.message)
            }
    }
    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser

}