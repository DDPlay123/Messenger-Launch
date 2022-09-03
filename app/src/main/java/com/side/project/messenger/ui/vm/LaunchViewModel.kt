package com.side.project.messenger.ui.vm

import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.side.project.messenger.R
import com.side.project.messenger.data.local.UserAccounts
import com.side.project.messenger.data.repo.UserAccountRepo
import com.side.project.messenger.utils.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class LaunchViewModel : ViewModel(), KoinComponent {
    private val userAccountRepo: UserAccountRepo by inject()

    // 1. 錯誤內容 2. true -> 錯誤 3. 錯誤 Index
    private var _verifyResult = MutableLiveData<Triple<String, Boolean, Int>>()
    val verifyResult: LiveData<Triple<String, Boolean, Int>>
        get() = _verifyResult

    // 1. 手機號碼 2. 倒數時間 3. 點擊許可
    private var _sendOTPState = MutableLiveData<Triple<String, String, Boolean>>()
    val sendOTPState: LiveData<Triple<String, String, Boolean>>
        get() = _sendOTPState

    // 1. 名稱 2. 手機 3. 電子郵件
    private var _receiveInfo = MutableLiveData<Triple<String, String, String>>()
    val receiveInfo: LiveData<Triple<String, String, String>>
        get() = _receiveInfo

    fun verifySignUpInfo(vararg ed: AppCompatEditText): Boolean {
        ed.forEachIndexed { index, edit ->
            val editName = edit.context.resources.getResourceName(edit.id)
            val editText = edit.text.toString().trim()
            if (editText.isEmpty()) {
                _verifyResult.postValue(
                    Triple(
                        edit.context.getString(R.string.hint_empty_error),
                        true,
                        index
                    )
                )
                return false
            } else if (editName.contains("Phone") && !verifyPhoneNumber(editText)) {
                _verifyResult.postValue(
                    Triple(
                        edit.context.getString(R.string.hint_phone_error),
                        true,
                        index
                    )
                )
                return false
            } else if (editName.contains("Email") && !verifyEmail(editText)) {
                _verifyResult.postValue(
                    Triple(
                        edit.context.getString(R.string.hint_email_error),
                        true,
                        index
                    )
                )
                return false
            } else if (editName.contains("Password") && !verifyPassword(editText)) {
                _verifyResult.postValue(
                    Triple(
                        edit.context.getString(R.string.hint_password_error),
                        true,
                        index
                    )
                )
                return false
            } else if (editName.contains("Confirm") && editText != ed[index - 1].text.toString()
                    .trim()
            ) {
                _verifyResult.postValue(
                    Triple(
                        edit.context.getString(R.string.hint_confirm_password_error),
                        true,
                        index
                    )
                )
                return false
            }
        }
        _verifyResult.postValue(Triple("", false, -1))
        return true
    }

    fun displayVerifyState(phone: String) {
        var counter = 30
        _sendOTPState.postValue(Triple(phone, counter.toString(), false))
        // 倒數計時
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .take(31)
            .map { counter-- }
            .subscribe {
                if (counter < 0)
                    _sendOTPState.postValue(Triple(phone, "", true))
                else
                    _sendOTPState.postValue(Triple(phone, "($counter)", false))
            }
    }

    fun receiveInfoDetail(bundle: Bundle) {
        bundle.let {
            _receiveInfo.postValue(Triple(
                it.getString(KEY_USER_FIRST_NAME) + it.getString(KEY_USER_LAST_NAME),
                "${it.getString(KEY_USER_PHONE)}",
                "${it.getString(KEY_USER_EMAIL)}")
            )
        }
    }

    fun getAllAccount(): Observable<List<UserAccounts>> =
        userAccountRepo.getAllAccount()

    fun getAccountByUserId(userId: String): Observable<UserAccounts> =
        userAccountRepo.getAccountByUserId(userId)

    fun insertUserAccount(userAccounts: UserAccounts): Completable =
        userAccountRepo.insertUserAccount(userAccounts)

    fun deleteUserAccount(userAccounts: UserAccounts): Completable =
        userAccountRepo.deleteUserAccount(userAccounts)
}