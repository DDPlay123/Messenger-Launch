package com.side.project.messenger.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

fun logE(tag: String, message: String) =
    Log.e(tag, message)

fun logD(tag: String, message: String) =
    Log.d(tag, message)

fun verifyPhoneNumber(phone: String): Boolean {
    val pattern = Pattern.compile("09\\d{8}")
    val matcher = pattern.matcher(phone)
    return matcher.matches()
}

fun verifyEmail(email: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    val matcher = pattern.matcher(email)
    return matcher.matches()
}

// 數字
const val REG_NUMBER = ".*\\d+.*"
// 大寫字母
const val REG_UPPERCASE = ".*[A-Z]+.*"
// 小寫字母
const val REG_LOWERCASE = ".*[a-z]+.*"
// 特殊符號
const val REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*"

fun verifyPassword(password: String): Boolean {
    // 密碼爲空或者長度小於8位則返回false
    if (password.length < 8) return false
    var i = 0
    if (password.matches(Regex(REG_NUMBER))) i++
    if (password.matches(Regex(REG_LOWERCASE))) i++
    if (password.matches(Regex(REG_UPPERCASE))) i++
//    if (password.matches(Regex(REG_SYMBOL))) i++
    return i >= 2
}

fun showKeyBoard(activity: Activity, ed: EditText){
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(ed, 0)
}

fun hideKeyBoard(activity: Activity) {
    activity.currentFocus?.let {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken,0)
    }
}

fun hideKeyBoard(context: Context, view: View) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken,0)
}

// 解碼圖片
fun decodeImage(encodedImage: String?): Bitmap? {
    return if (encodedImage != null) {
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } else
        null
}

// 編碼圖片
fun encodeImage(bitmap: Bitmap): String? {
    val previewWidth = 150
    val previewHeight = bitmap.height * previewWidth / bitmap.height
    val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)

    val byteArrayOutputStream = ByteArrayOutputStream()
    previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val bytes = byteArrayOutputStream.toByteArray()

    return Base64.encodeToString(bytes, Base64.DEFAULT)
}