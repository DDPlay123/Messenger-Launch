package com.side.project.messenger.utils.helper

import android.view.View

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.dismiss() {
    this.visibility = View.INVISIBLE
}

fun View.cancel() {
    this.visibility = View.GONE
}