package com.apps.sportzinteractive

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apps.sportzinteractive.databinding.ActivityMainBinding
import com.apps.sportzinteractive.viewModel.ApiViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: ApiViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val url1 = "https://demo.sportz.io/nzin01312019187360.json"
        val url2 = "https://demo.sportz.io/sapk01222019186652.json"

        binding.shimmerLayout.startShimmer()
        // Fetch API Data
        viewModel.fetchApiData(url1)

//        // Observe API Response
//        viewModel.apiResponse.observe(this, Observer { response ->
//            response?.let {
//                val matchDetails = it["Matchdetail"] as? Map<*, *>
//                val inningDetails = it["Innings"] as? List<*>
//                val teamInfo = it["Teams"] as? Map<*, *>
//
//                // Extract home and away teams
//                val teamHome = matchDetails?.get("Team_Home") as? String ?: ""
//                val teamAway = matchDetails?.get("Team_Away") as? String ?: ""
//                val match = matchDetails?.get("Match") as? Map<*, *>
//                mainBinding.txtMatchInfo.text = listOfNotNull(
//                    match?.get("Number") as? String,
//                    match?.get("Date") as? String,
//                    "@${match?.get("Time") as? String}"
//                ).joinToString(", ")
//
//
//                // Set match result text
//                mainBinding.txtMatchResult.text =
//                    matchDetails?.get("Result")?.toString() ?: "No result"
//
//                // Extract and set team names
//                mainBinding.txtTeamHome.text =
//                    teamInfo?.get(teamHome)?.let { (it as? Map<*, *>)?.get("Name_Full") }
//                        ?.toString() ?: "N/A"
//                mainBinding.txtTeamAway.text =
//                    teamInfo?.get(teamAway)?.let { (it as? Map<*, *>)?.get("Name_Full") }
//                        ?.toString() ?: "N/A"
//
//                // Extract and set team scores
//                mainBinding.txtHomeTeamScore.text =
//                    extractInningsDetails(inningDetails?.getOrNull(1))
//                mainBinding.txtAwayTeamScore.text =
//                    extractInningsDetails(inningDetails?.getOrNull(0))
//                mainBinding.shimmerLayout.stopShimmer()
//                mainBinding.shimmerLayout.visibility = View.GONE
//                mainBinding.llDataLayout.visibility = View.VISIBLE
//            } ?: Log.e("API_RESPONSE", "No data received")
//        })
        viewModel.matchInfo.observe(this) {
            binding.txtMatchInfo.text = it
        }

        viewModel.matchResult.observe(this) {
            binding.txtMatchResult.text = it
        }

        viewModel.teamHomeName.observe(this) {
            binding.txtTeamHome.text = it
        }

        viewModel.teamAwayName.observe(this) {
            binding.txtTeamAway.text = it
        }

        viewModel.homeTeamScore.observe(this) {
            binding.txtHomeTeamScore.text = it
        }

        viewModel.awayTeamScore.observe(this) {
            binding.txtAwayTeamScore.text = it
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.llDataLayout.visibility = View.GONE
            } else {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.llDataLayout.visibility = View.VISIBLE
            }
        }

        // Simulate API call
//        viewModel.loading.postValue(true)
        viewModel.apiResponse.observe(this, Observer { response ->
            viewModel.processApiResponse(response)
        })
        binding.cardView.setOnClickListener {

        }
    }

    // Function to safely extract innings details
    private fun extractInningsDetails(inning: Any?): String {
        val inningMap = inning as? Map<*, *> ?: return "N/A"
        val runs = inningMap["Total"] ?: "0"
        val wickets = inningMap["Wickets"] ?: "0"
        val overs = inningMap["Overs"] ?: "0.0"
        return "$runs-$wickets ($overs)"
    }
}