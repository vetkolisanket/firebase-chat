package com.sanket.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.sanket.firebasechat.databinding.ActivityLoginBinding
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val callbacks by lazy { object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Toast.makeText(this@LoginActivity, "On verification completed", Toast.LENGTH_SHORT)
                .show()
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@LoginActivity, "On verification failed", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId, "999999"))
        }

        override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
            super.onCodeAutoRetrievalTimeOut(verificationId)
        }

    } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnSendOtp.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+919999999999")       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failure ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalide credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}