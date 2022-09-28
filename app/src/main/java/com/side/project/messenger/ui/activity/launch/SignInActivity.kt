package com.side.project.messenger.ui.activity.launch

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.side.project.messenger.R
import com.side.project.messenger.data.local.UserAccount
import com.side.project.messenger.data.local.UserAccounts
import com.side.project.messenger.databinding.*
import com.side.project.messenger.ui.DialogManager
import com.side.project.messenger.ui.activity.BaseActivity
import com.side.project.messenger.ui.activity.MainActivity
import com.side.project.messenger.ui.viewModel.LaunchViewModel
import com.side.project.messenger.utils.*
import com.side.project.messenger.utils.helper.displayToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
            viewModel = launchViewModel
            lifecycleOwner = this@SignInActivity
            intent.extras?.let { b ->
                when (b.getString(PARSE_TYPE)) {
                    ActivityParseType.SIGN_UP.name,
                    ActivityParseType.SING_OUT.name ->
                        launchViewModel.receiveSignUpDetail(b)
                }
            }
        }
    }

    private fun setListener() {
        activitySignInBinding.run {
            // 登入
            btnSignIn.setOnClickListener {
                signIn()
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

    // 登入
    private fun signIn() {
        dialog.showLoading(false)
        activitySignInBinding.run {
            launchViewModel.signIn(inputAccount.text.toString().trim(), inputPassword.text.toString().trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result.isEmpty) {
                        val documentSnapshot = task.result.documents[0] // 取得 User 資料
                        // 判斷是否紀錄登入資訊
                        if (activitySignInBinding.checkboxSignIn.isChecked)
                            launchViewModel.setRecordAccount(documentSnapshot.id)

                        // 記錄到 Database
                        storageUsers(UserAccounts(
                            userId = documentSnapshot.id,
                            userAccount = UserAccount(
                                userImage = documentSnapshot.getString(KEY_USER_IMAGE).toString(),
                                userFirstName = documentSnapshot.getString(KEY_USER_FIRST_NAME).toString(),
                                userLastName = documentSnapshot.getString(KEY_USER_LAST_NAME).toString(),
                                userGender = documentSnapshot.getString(KEY_USER_GENDER).toString(),
                                userPhone = documentSnapshot.getString(KEY_USER_PHONE).toString(),
                                userEmail = documentSnapshot.getString(KEY_USER_EMAIL).toString(),
                                userPassword = documentSnapshot.getString(KEY_USER_PASSWORD).toString()
                            )
                        ))
                    } else {
                        dialog.cancelAllDialog()
                        displayToast(getString(R.string.hint_log_in_error_subtitle))
                    }
                }
        }
    }

    private fun storageUsers(userAccounts: UserAccounts) {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(launchViewModel.insertUserAccount(userAccounts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                dialog.cancelAllDialog()
                start(MainActivity::class.java, true)
                compositeDisposable.dispose()
            })
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
                            this.putString(PARSE_TYPE, ActivityParseType.SIGN_IN.name)
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