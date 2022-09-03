package com.side.project.messenger.ui.activity.launch

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.side.project.messenger.R
import com.side.project.messenger.databinding.ActivitySignUpBinding
import com.side.project.messenger.ui.activity.BaseActivity
import com.side.project.messenger.ui.vm.LaunchViewModel
import com.side.project.messenger.utils.helper.displayToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity() {
    private lateinit var activitySignUpBinding: ActivitySignUpBinding
    private val launchViewModel: LaunchViewModel by viewModel()

    private var selectImageFile: String? = null
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
                bundle = b
                viewModel = launchViewModel
                lifecycleOwner = mActivity
                launchViewModel.receiveInfoDetail(bundle)
            }
        }
    }

    private fun setListener() {
        activitySignUpBinding.run {
            imageProfile.setOnClickListener { displayToast(getString(R.string.hint_not_implemented)) }
            btnSignUp.setOnClickListener {
                if (!checkboxSignUp.isChecked)
                    displayToast(getString(R.string.hint_please_check_agree))
                else
                    TODO("註冊")
            }
        }
    }
}