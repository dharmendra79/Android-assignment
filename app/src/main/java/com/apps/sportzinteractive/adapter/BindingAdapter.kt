package com.apps.sportzinteractive.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("isCaptain", "isKeeper", "playerName")
    fun setText(txt: TextView, isCaptain: Boolean, isKeeper: Boolean, playerName: String) {
        val name = playerName

        txt.text = when {
            isKeeper && isCaptain -> "$name (WK & C)"
            isKeeper -> "$name (WK)"
            isCaptain -> "$name (C)"
            else -> name
        }
    }
}