package com.example.kotlinuploadpdf

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit


class RegisterActivity : AppCompatActivity() {
    lateinit var  mAuth: FirebaseAuth
    lateinit var verify: TextInputLayout
    lateinit var  phone: TextInputLayout
    lateinit var  btnSend: Button
    lateinit var  btnReceive: Button
    var res = false
    var codeSent: String? = null
    lateinit var  myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.kotlinuploadpdf.R.layout.activity_register)
        verify = findViewById(com.example.kotlinuploadpdf.R.id.verify)
        phone = findViewById(com.example.kotlinuploadpdf.R.id.phone)
        btnSend = findViewById(com.example.kotlinuploadpdf.R.id.btnSend)
        btnReceive = findViewById(com.example.kotlinuploadpdf.R.id.btnReceive)
        mAuth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference
        btnSend.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                sendVerificationCode()
            }
        })
        btnReceive.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                verifySignInCode()
            }
        })
    }

    private fun verifySignInCode() {
        val code = verify!!.editText!!.text.toString()
        val credential = PhoneAuthProvider.getCredential(codeSent!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        //here you can open new activity
                        Toast.makeText(
                            applicationContext,
                            "Login Successfull", Toast.LENGTH_LONG
                        ).show()
                        val a = Intent(this@RegisterActivity, MenuActivity::class.java)
                        startActivity(a)
                    } else {
                        if (task.getException() is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(
                                applicationContext,
                                "Incorrect Verification Code ", Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
    }

    private fun sendVerificationCode() {
        val phoneInput = phone!!.editText!!.text.toString().trim { it <= ' ' }
        if (phoneInput.isEmpty()) {
            phone!!.error = "Field can't be empty"
            return
        } else if (phoneInput.length != 10) {
            phone!!.error = "Invalid Number"
            return
        } else {
            phone!!.error = null
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$phoneInput",  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this,  // Activity (for callback binding)
            mCallbacks
        ) // OnVerificationStateChangedCallbacks
    }

    var mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //Toast.makeText(RegisterActivity.this, "Verifying code Automatically", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {}
            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                codeSent = s
            }
        }
}
