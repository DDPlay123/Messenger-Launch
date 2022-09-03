package com.side.project.messenger.utils

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.side.project.messenger.R

class BindingAdapter {
    companion object {
        @BindingAdapter("app:attachedDelete")
        @kotlin.jvm.JvmStatic
        fun attachedDelete(imageView: ImageView, editText: EditText) {
            try {
                // init
                imageView.setOnClickListener { editText.setText("") }
                // track
                editText.addTextChangedListener {
                    if (it?.isNotEmpty()!!)
                        imageView.visibility = View.VISIBLE
                    else
                        imageView.visibility = View.GONE
                }
            } catch (e: Exception) {
            }
        }

        @BindingAdapter("app:btnBindEdit1", "app:btnBindEdit2")
        @kotlin.jvm.JvmStatic
        fun attachedClickable(button: Button, editText1: EditText, editText2: EditText) {
            try {
                // init
                button.isClickable = false
                button.setTextColor(button.context.resources.getColorStateList(R.color.secondaryText, null))
                // track
                editText1.addTextChangedListener { ed1 ->
                    editText2.addTextChangedListener { ed2 ->
                        button.isClickable = ed1?.isNotEmpty()!! && ed2?.isNotEmpty()!!
                        if (ed1.isNotEmpty() && ed2?.isNotEmpty()!!)
                            button.setTextColor(getColor(button.context, R.color.white))
                        else
                            button.setTextColor(getColor(button.context, R.color.secondaryText))
                    }
                }
                editText2.addTextChangedListener { ed2 ->
                    editText1.addTextChangedListener { ed1 ->
                        button.isClickable = ed2?.isNotEmpty()!! && ed1?.isNotEmpty()!!
                        if (ed2.isNotEmpty() && ed1?.isNotEmpty()!!)
                            button.setTextColor(button.context.resources.getColorStateList(R.color.white, null))
                        else
                            button.setTextColor(button.context.resources.getColorStateList(R.color.secondaryText, null))
                    }
                }
            } catch (e: Exception) {
            }
        }

        @BindingAdapter("app:tvBindEdit")
        @kotlin.jvm.JvmStatic
        fun attachedTvClickable(textView: TextView, editText: EditText) {
            try {
                // init
                textView.isClickable = false
                textView.setTextColor(textView.context.resources.getColorStateList(R.color.secondaryText, null))
                // track
                editText.addTextChangedListener { ed ->
                    if (ed?.isNotEmpty() == true) {
                        textView.isClickable = true
                        textView.setTextColor(textView.context.resources.getColorStateList(R.color.blue_text_selector, null))
                    } else {
                        textView.isClickable = false
                        textView.setTextColor(textView.context.resources.getColorStateList(R.color.secondaryText, null))
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
}