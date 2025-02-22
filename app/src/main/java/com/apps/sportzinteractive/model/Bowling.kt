package com.apps.sportzinteractive.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bowling(
    val Average: String,
    val Economyrate: String,
    val Style: String,
    val Wickets: String
) : Parcelable