package com.apps.sportzinteractive.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.apps.sportzinteractive.R
import com.apps.sportzinteractive.databinding.ActivityMainBinding
import com.apps.sportzinteractive.fragment.TeamFragment
import com.apps.sportzinteractive.utils.SPORTZ_URL1
import com.apps.sportzinteractive.utils.SPORTZ_URL2
import com.apps.sportzinteractive.utils.isInternetAvailable
import com.apps.sportzinteractive.viewModel.MatchDetailViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MatchDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (isInternetAvailable(this)) {

            /**
             * Change URL to check the response for other API
             * */
            viewModel.fetchApiData(SPORTZ_URL1)

            openNewFragment(TeamFragment())
        } else {
            Toast.makeText(
                this@MainActivity,
                "Kindly check device internet connection",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openNewFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .addToBackStack(null)  // Adds to the back stack
            .commit()
    }
}