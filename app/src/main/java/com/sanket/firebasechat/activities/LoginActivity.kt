package com.sanket.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.sanket.firebasechat.databinding.ActivityLoginBinding
import com.sanket.firebasechat.utils.Constants
import com.sanket.firebasechat.utils.openActivity
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var verificationId: String
    private val usersDbRef by lazy {
        FirebaseDatabase.getInstance(Constants.Companion.Api.FIREBASE_DB_REFERENCE)
            .getReference(Constants.Companion.Api.USERS)
    }
    private val callbacks by lazy {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(this@LoginActivity, "On verification completed", Toast.LENGTH_SHORT)
                    .show()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@LoginActivity, "On verification failed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                this@LoginActivity.verificationId = verificationId
                binding.apply {
                    etOtp.isVisible = true
                    btnVerifyOtp.isVisible = true
                }
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                super.onCodeAutoRetrievalTimeOut(verificationId)
                this@LoginActivity.verificationId = verificationId
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (auth.currentUser != null) {
            usersDbRef.child(auth.uid!!).get().addOnSuccessListener {
                if (it.exists()) {
                    openActivity<UserListActivity>()
                } else {
                    openActivity<AddProfileActivity>()
                }
                finish()
            }
        }
        initViews()
        initClickListeners()
    }

    private fun initViews() {
        binding.apply {
            etPhoneNo.addTextChangedListener { editable ->
                editable?.let {
                    val phoneNo = it.toString()
                    btnSendOtp.isEnabled = phoneNo.startsWith("+91") && phoneNo.length == 13
                }
            }
            etOtp.addTextChangedListener { editable ->
                editable?.let {
                    btnVerifyOtp.isEnabled = it.toString().length == 6
                }
            }
        }
    }

    private fun initClickListeners() {
        binding.apply {
            btnSendOtp.setOnClickListener {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(binding.etPhoneNo.text.toString())
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this@LoginActivity)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
            btnVerifyOtp.setOnClickListener {
                signInWithPhoneAuthCredential(
                    PhoneAuthProvider.getCredential(
                        verificationId,
                        etOtp.text.toString()
                    )
                )
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failure ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}