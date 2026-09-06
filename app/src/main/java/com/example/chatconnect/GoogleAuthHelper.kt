package com.example.chatconnect

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object GoogleAuthHelper {
    suspend fun signIn(
        activityContext: Context,
        onSuccess: (idToken: String) -> Unit,
        onError: (String?) -> Unit
    ) {
        val webClientId = webClientId(activityContext)
        if (webClientId.isBlank()) {
            onError(
                "Google Sign-In setup is incomplete: add the SHA-1 in the Firebase Console " +
                    "and add the new google-services.json file."
            )
            return
        }

        val option = GetSignInWithGoogleOption.Builder(webClientId).build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        try {
            val result = CredentialManager.create(activityContext)
                .getCredential(activityContext, request)

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                onSuccess(googleCredential.idToken)
            } else {
                onError("Google account token was not received")
            }
        } catch (e: GetCredentialCancellationException) {
            onError(null)
        } catch (e: GetCredentialException) {
            onError(e.message ?: "Google Sign-In failed")
        }
    }

    private fun webClientId(context: Context): String {
        val resId = context.resources.getIdentifier(
            "default_web_client_id", "string", context.packageName
        )
        return if (resId != 0) context.getString(resId) else ""
    }
}
