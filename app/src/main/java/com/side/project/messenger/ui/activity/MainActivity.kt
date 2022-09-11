package com.side.project.messenger.ui.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.side.project.messenger.R
import com.side.project.messenger.databinding.ActivityMainBinding
import com.side.project.messenger.ui.DialogManager
import com.side.project.messenger.ui.activity.launch.SignInActivity
import com.side.project.messenger.ui.viewModel.LaunchViewModel
import com.side.project.messenger.utils.*
import com.side.project.messenger.utils.helper.displayToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val launchViewModel: LaunchViewModel by viewModel { parametersOf(this) }
    private val dialog = DialogManager.instance(this)

    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        bundle = Bundle()
//        val compositeDisposable = CompositeDisposable()
//
//        compositeDisposable.add(launchViewModel.getAccountByUserId(launchViewModel.getRecordAccount().toString())
//            .observeOn(Schedulers.computation())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                with(bundle) {
//                    this.putString(PARSE_TYPE, ActivityParseType.SING_OUT.name)
//                    this.putString(KEY_USER_PHONE, it.userAccount.userPhone)
//                    this.putString(KEY_USER_EMAIL, it.userAccount.userEmail)
//                    this.putString(KEY_USER_PASSWORD, it.userAccount.userPassword)
//                }
//
//                compositeDisposable.dispose()
//            })

        activityMainBinding.root.setOnClickListener {
//            launchViewModel.clearUserPrefs()
//            start(SignInActivity::class.java, bundle, true)
            displayToast("test")
            launchViewModel.getRecordAccount()
        }
    }
}