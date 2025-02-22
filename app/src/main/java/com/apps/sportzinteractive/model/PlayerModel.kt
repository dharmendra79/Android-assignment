package com.apps.sportzinteractive.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PlayerModel(
    val Batting: Batting,
    val Bowling: Bowling,
    val Iscaptain: Boolean,
    val Iskeeper: Boolean,
    val Name_Full: String,
    val Position: String
) : Parcelable