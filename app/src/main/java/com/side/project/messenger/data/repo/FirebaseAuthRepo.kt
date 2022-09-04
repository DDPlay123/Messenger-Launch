package com.side.project.messenger.data.repo

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

interface FirebaseAuthRepo {
    fun sendOTP(phone: String, callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    fun reSendOTP(phone: String, token: PhoneAuthProvider.ForceResendingToken,
                  callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    fun verifyOTP(verificationId: String, inputCode: String): Task<AuthResult>
}

class FirebaseAuthRepoImpl(activity: Activity): FirebaseAuthRepo, KoinComponent {
    private val _options: PhoneAuthOptions.Builder by inject { parametersOf(activity) }

    override fun sendOTP(phone: String, callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        val options = _options
            .setPhoneNumber("+886$phone")
            .setCallbacks(callback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun reSendOTP(phone: String, token: PhoneAuthProvider.ForceResendingToken,
                           callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        val options = _options
            .setPhoneNumber("+886$phone")
            .setForceResendingToken(token)
            .setCallbacks(callback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyOTP(verificationId: String, inputCode: String): Task<AuthResult> {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, inputCode)
        return FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
    }
}