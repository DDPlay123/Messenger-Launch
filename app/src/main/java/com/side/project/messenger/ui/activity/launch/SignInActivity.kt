package com.side.project.messenger.ui.activity.launch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.side.project.messenger.R
import com.side.project.messenger.data.local.UserAccount
import com.side.project.messenger.data.local.UserAccounts
import com.side.project.messenger.databinding.ActivitySignInBinding
import com.side.project.messenger.viewModels.LaunchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var activitySignInBinding: ActivitySignInBinding
    private val viewModel: LaunchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        activitySignInBinding.imageIcon.setOnClickListener {
            val compositeDisposable = CompositeDisposable()

            compositeDisposable.add(viewModel.getAccountByUserId("1")
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    activitySignInBinding.email = it.userAccount.userEmail
                    activitySignInBinding.password = it.userAccount.userPassword

                    compositeDisposable.dispose()
                })
        }

        activitySignInBinding.btnSignUp.setOnClickListener {
            val compositeDisposable = CompositeDisposable()

            val userAccount = UserAccounts(
                userId = "1",
                userAccount = UserAccount(
                    userImage =  "",
                    userFirstName =  "",
                    userLastName =  "",
                    userGender =  "",
                    userPhone =  "",
                    userEmail =  "Hello121",
                    userPassword =  "test",
                    userPhoneVerify =  0
                )
            )
            compositeDisposable.add(viewModel.insertUserAccount(userAccount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    compositeDisposable.dispose()
                })
        }
    }
}