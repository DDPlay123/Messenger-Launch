package com.side.project.messenger.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.side.project.messenger.R
import com.side.project.messenger.databinding.DialogLoadingBinding

class DialogManager(private val activity: Activity) {
    companion object {
        fun instance(activity: Activity): DialogManager = DialogManager(activity)
    }

    private var dialog: Dialog? = null
    private var loadingDialog: Dialog? = null
    private var bottomDialog: BottomSheetDialog? = null

    fun showLoading(cancelable: Boolean) {
        cancelLoadingDialog()
        val binding = DialogLoadingBinding.inflate(LayoutInflater.from(activity))

        loadingDialog = AlertDialog.Builder(activity).create()
        loadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog?.show()

        (binding.loadingHUD.background as AnimationDrawable).start()
        loadingDialog?.setContentView(binding.root)

        loadingDialog?.setCancelable(cancelable)
    }

    fun showCenterDialog(
        cancelable: Boolean,
        view: ViewDataBinding,
        keyboard: Boolean
    ): ViewDataBinding {
        cancelCenterDialog()
        dialog = AlertDialog.Builder(activity).create()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.show()

        if (keyboard) // 顯示鍵盤
            dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        dialog?.setContentView(view.root)
        dialog?.setCancelable(cancelable)

        return view
    }

    fun showBottomDialog(
        view: ViewDataBinding,
        isFullExpand: Boolean,
        bias: Int = 0
    ): ViewDataBinding {
        cancelBottomDialog()
        bottomDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        bottomDialog?.show()

        bottomDialog?.setContentView(view.root)
        expandBottomDialog(isFullExpand, bias)

        return view
    }

    private fun expandBottomDialog(isExpand: Boolean, bias: Int = 0) {
        if (!isExpand) return
        // 全展開
        val frameLayout: FrameLayout? = bottomDialog?.findViewById(
            com.google.android.material.R.id.design_bottom_sheet
        )
        if (frameLayout != null) {
            val bottomSheetBehavior: BottomSheetBehavior<View> =
                BottomSheetBehavior.from(frameLayout)
            (Resources.getSystem().displayMetrics.heightPixels - bias).let {
                bottomSheetBehavior.maxHeight = it
                bottomSheetBehavior.peekHeight = it
            }
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun cancelCenterDialog() = dialog?.dismiss()

    fun cancelLoadingDialog() = loadingDialog?.dismiss()

    fun cancelBottomDialog() = bottomDialog?.dismiss()

    fun cancelAllDialog() {
        dialog?.dismiss()
        loadingDialog?.dismiss()
        bottomDialog?.dismiss()
    }
}