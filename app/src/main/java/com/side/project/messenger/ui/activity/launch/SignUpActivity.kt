package com.side.project.messenger.ui.activity.launch

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.side.project.messenger.R
import com.side.project.messenger.databinding.ActivitySignUpBinding
import com.side.project.messenger.databinding.DialogPromptBinding
import com.side.project.messenger.databinding.DialogVerifyPhoneBinding
import com.side.project.messenger.ui.DialogManager
import com.side.project.messenger.ui.activity.BaseActivity
import com.side.project.messenger.ui.viewModel.LaunchViewModel
import com.side.project.messenger.utils.*
import com.side.project.messenger.utils.helper.displayToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class SignUpActivity : BaseActivity(), KoinComponent {
    private lateinit var activitySignUpBinding: ActivitySignUpBinding
    private val launchViewModel: LaunchViewModel by viewModel { parametersOf(this) }
    private val dialog = DialogManager.instance(this)

    private var selectImageFile: String = ""
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        doInitialization()
        setListener()
    }

    private fun doInitialization() {
        activitySignUpBinding.run {
            intent.extras?.let { b ->
                if (b.getString(PARSE_TYPE).equals(ActivityParseType.SIGN_IN.name)) {
                    bundle = b
                    viewModel = launchViewModel
                    lifecycleOwner = this@SignUpActivity
                    launchViewModel.receiveSignInDetail(bundle)
                }
            }
        }
    }

    private fun setListener() {
        activitySignUpBinding.run {
            imageProfile.setOnClickListener { displayToast(getString(R.string.hint_not_implemented)) }
            btnSignUp.setOnClickListener {
                if (!checkboxSignUp.isChecked)
                    // ????????????
                    displayToast(getString(R.string.hint_please_check_agree))
                else if (inputPhone.text.toString().trim() != bundle.getString(KEY_USER_PHONE))
                    // ??????????????????
                    showVerifyPrompt(inputPhone.text.toString().trim())
                else
                    // ?????????????????????
                    checkUsersAuth()
            }
        }
    }

    private fun checkUsersAuth() {
        dialog.showLoading(false)
        activitySignUpBinding.run {
            // ?????????????????????
            launchViewModel.signIn(bundle.getString(KEY_USER_PHONE) ?: "", bundle.getString(KEY_USER_PASSWORD) ?: "")
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful && !task.result.isEmpty) {
                        displayToast(getString(R.string.hint_already_auth))
                        dialog.cancelLoadingDialog()
                    } else {
                        registerUsers()
                    }
                }
        }
    }

    private fun registerUsers() {
        activitySignUpBinding.run {
            launchViewModel.signUp(getUsersMap())
                .addOnSuccessListener {
                    dialog.cancelLoadingDialog()
                    bundle.remove(PARSE_TYPE)
                    bundle.putString(PARSE_TYPE, ActivityParseType.SIGN_UP.name)
                    start(SignInActivity::class.java, bundle, true)
                }
                .addOnFailureListener { e ->
                    dialog.cancelLoadingDialog()
                    displayToast(getString(R.string.hint_auth_error))
                    logE("Register Error", e.message.toString())
                }
        }
    }

    private fun getUsersMap(): HashMap<String, Any> =
        hashMapOf(
            KEY_USER_IMAGE to selectImageFile,
            KEY_USER_FIRST_NAME to bundle.getString(KEY_USER_FIRST_NAME).toString(),
            KEY_USER_LAST_NAME to bundle.getString(KEY_USER_LAST_NAME).toString(),
            KEY_USER_GENDER to bundle.getString(KEY_USER_GENDER).toString(),
            KEY_USER_PHONE to bundle.getString(KEY_USER_PHONE).toString(),
            KEY_USER_EMAIL to bundle.getString(KEY_USER_EMAIL).toString(),
            KEY_USER_PASSWORD to bundle.getString(KEY_USER_PASSWORD).toString(),
        )

    private fun showVerifyPrompt(phone: String) {
        val binding = DialogPromptBinding.inflate(layoutInflater)
        dialog.showCenterDialog(false, binding, false).let {
            binding.run {
                titleText = getString(R.string.hint_verify_phone_title)
                subTitleText = getString(R.string.hint_verify_phone_subtitle)
                tvCancel.setOnClickListener { dialog.cancelCenterDialog() }
                tvConfirm.setOnClickListener { sendOTP(phone) }
            }
        }
    }

    private fun showVerifyDialog(phone: String, token: PhoneAuthProvider.ForceResendingToken, code: String) {
        val binding = DialogVerifyPhoneBinding.inflate(layoutInflater)
        dialog.showCenterDialog(false, binding, true).let {
            binding.run {
                // ??????ViewModel
                viewModel = launchViewModel
                lifecycleOwner = this@SignUpActivity
                launchViewModel.displayVerifyState(phone)

                tvCancel.setOnClickListener { dialog.cancelCenterDialog() }
                btnReSendOTP.setOnClickListener { reSendOTP(phone, token) }
                tvConfirm.setOnClickListener {
                    if (inputOTP.text.isNotEmpty()) {
                        dialog.showLoading(false)
                        launchViewModel.verifyOTP(code, inputOTP.text.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    bundle.remove(KEY_USER_PHONE)
                                    bundle.putString(KEY_USER_PHONE, activitySignUpBinding.inputPhone.text.toString().trim())
                                    dialog.cancelAllDialog()
                                    displayToast(getString(R.string.hint_verify_success))
                                    checkUsersAuth()
                                } else {
                                    dialog.cancelLoadingDialog()
                                    displayToast(getString(R.string.hint_verify_error))
                                }
                            }
                    } else
                        displayToast(getString(R.string.hint_verify_error))
                }
            }
        }
    }

    // ???????????????
    private fun sendOTP(phone: String) {
        dialog.showLoading(false)
        launchViewModel.sendOTP(phone, otpCallbacks)
    }

    // ?????????????????????
    private fun reSendOTP(phone: String, token: PhoneAuthProvider.ForceResendingToken) {
        dialog.showLoading(false)
        launchViewModel.reSendOTP(phone, token, otpCallbacks)
    }

    private val otpCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                dialog.cancelLoadingDialog()
                dialog.cancelCenterDialog()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                dialog.cancelLoadingDialog()
                dialog.cancelCenterDialog()
                displayToast(getString(R.string.hint_occur_error))
            }

            override fun onCodeSent(
                verificationId: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                // ?????? Dialog
                dialog.cancelLoadingDialog()
                showVerifyDialog(activitySignUpBinding.inputPhone.text.toString().trim(), forceResendingToken, verificationId)
            }

            override fun onCodeAutoRetrievalTimeOut(s: String) {
                super.onCodeAutoRetrievalTimeOut(s)
                logE("OTP Timeout", s)
                dialog.cancelLoadingDialog()
                dialog.cancelCenterDialog()
            }
        }
}