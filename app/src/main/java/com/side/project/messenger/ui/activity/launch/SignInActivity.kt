package com.side.project.messenger.ui.activity.launch

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.side.project.messenger.R
import com.side.project.messenger.databinding.*
import com.side.project.messenger.ui.DialogManager
import com.side.project.messenger.ui.activity.BaseActivity
import com.side.project.messenger.ui.activity.MainActivity
import com.side.project.messenger.ui.viewModel.LaunchViewModel
import com.side.project.messenger.utils.*
import com.side.project.messenger.utils.helper.displayToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class SignInActivity : BaseActivity(), KoinComponent {
    private lateinit var activitySignInBinding: ActivitySignInBinding
    private val launchViewModel: LaunchViewModel by viewModel { parametersOf(this) }
    private val dialog = DialogManager.instance(this)

    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        doInitialization()
        setListener()
    }

    private fun doInitialization() {
        activitySignInBinding.run {
            intent.extras?.let { b ->
                viewModel = launchViewModel
                lifecycleOwner = this@SignInActivity
                launchViewModel.receiveInfoDetail(b)
            }
        }
    }

    private fun setListener() {
        activitySignInBinding.run {
            // 登入
            btnSignIn.setOnClickListener {
                launchViewModel.signIn(inputAccount.text.toString().trim(), inputPassword.text.toString().trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && !task.result.isEmpty) {
                            start(MainActivity::class.java, true)
                        }
                    }
            }
            // 註冊
            btnSignUp.setOnClickListener {
                showSignUpPrompt() // 跳出提示
            }
            // 忘記密碼
            tvForgetPassword.setOnClickListener {
                showForgetPasswordDialog() // 下方彈出
            }
        }
    }
    // 註冊
    private fun showSignUpPrompt() {
        val binding = DialogPromptBinding.inflate(layoutInflater)
        dialog.showCenterDialog(true, binding, false).let {
            binding.run {
                titleText = getString(R.string.hint_create_account_title)
                subTitleText = getString(R.string.hint_create_account_subtitle)
                tvCancel.setOnClickListener { dialog.cancelCenterDialog() }
                tvConfirm.setOnClickListener {
                    showSignUpDialog()
                    dialog.cancelCenterDialog()
                }
            }
        }
    }

    private fun showSignUpDialog() {
        val binding = DialogSignUpBinding.inflate(layoutInflater)
        dialog.showBottomDialog(binding, true, 50).let {
            binding.run {
                // 連接ViewModel
                viewModel = launchViewModel
                lifecycleOwner = this@SignInActivity

                tvCancelHeader.setOnClickListener { dialog.cancelBottomDialog() }
                imageRefreshHeader.setOnClickListener { refresh = true }
                btnCreateAccount.setOnClickListener {
                    // 驗證資料
                    if (launchViewModel.verifySignUpInfo(
                            inputFirstName, inputLastName,
                            inputPhoneNumber, inputEmail,
                            inputPassword, inputPasswordConfirm)) {
                        // 封裝
                        bundle = Bundle()
                        with(bundle) {
                            this.putString(KEY_USER_FIRST_NAME, inputFirstName.text.toString())
                            this.putString(KEY_USER_LAST_NAME, inputLastName.text.toString())
                            this.putString(
                                KEY_USER_GENDER, when (rgGender.checkedRadioButtonId) {
                                    R.id.rbMan -> getString(R.string.text_man)
                                    R.id.rbWoman -> getString(R.string.text_woman)
                                    R.id.rbSecrecy -> getString(R.string.text_secrecy)
                                    else -> getString(R.string.text_secrecy)
                                }
                            )
                            this.putString(KEY_USER_PHONE, inputPhoneNumber.text.toString())
                            this.putString(KEY_USER_EMAIL, inputEmail.text.toString())
                            this.putString(KEY_USER_PASSWORD, inputPassword.text.toString())
                        }
                        showVerifyPrompt(inputPhoneNumber.text.toString())
                    }
                }
            }
        }
    }

    // 驗證手機
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
                // 連接ViewModel
                viewModel = launchViewModel
                lifecycleOwner = this@SignInActivity
                launchViewModel.displayVerifyState(phone)

                tvCancel.setOnClickListener { dialog.cancelCenterDialog() }
                btnReSendOTP.setOnClickListener { reSendOTP(phone, token) }
                tvConfirm.setOnClickListener {
                    if (inputOTP.text.isNotEmpty()) {
                        dialog.showLoading(false)
                        launchViewModel.verifyOTP(code, inputOTP.text.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    dialog.cancelAllDialog()
                                    start(SignUpActivity::class.java, bundle, true)
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

    // 忘記密碼
    private fun showForgetPasswordDialog() {
        val binding = DialogForgetPasswordBinding.inflate(layoutInflater)
        dialog.showBottomDialog(binding, true, 200).let {
            binding.run {
                tvContinue.setOnClickListener { displayToast(getString(R.string.hint_not_implemented)) }
                tvReturn.setOnClickListener { dialog.cancelBottomDialog() }
            }
        }
    }

    // 發送驗證碼
    private fun sendOTP(phone: String) {
        dialog.showLoading(false)
        launchViewModel.sendOTP(phone, otpCallbacks)
    }

    // 重新發送驗證碼
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
                // 切換 Dialog
                dialog.cancelLoadingDialog()
                showVerifyDialog(bundle.getString("Phone") ?: "", forceResendingToken, verificationId)
            }

            override fun onCodeAutoRetrievalTimeOut(s: String) {
                super.onCodeAutoRetrievalTimeOut(s)
                logE("OTP Timeout", s)
                dialog.cancelLoadingDialog()
                dialog.cancelCenterDialog()
            }
        }
}