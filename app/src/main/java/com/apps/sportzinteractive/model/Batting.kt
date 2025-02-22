package com.apps.sportzinteractive.model


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Batting(
    val Average: String,
    val Runs: String,
    val Strikerate: String,
    val Style: String
) : Parcelable