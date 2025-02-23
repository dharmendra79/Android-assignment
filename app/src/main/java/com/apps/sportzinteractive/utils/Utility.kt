package com.apps.sportzinteractive.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (connectivityManager != null) {
        val network = connectivityManager.activeNetwork ?: return false

        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    return false
}

fun showPlayerDialog(context: Context, playerName: String) {
    AlertDialog.Builder(context)
        .setTitle("Player Selected")
        .setMessage("You clicked on $playerName")
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .show()
}


