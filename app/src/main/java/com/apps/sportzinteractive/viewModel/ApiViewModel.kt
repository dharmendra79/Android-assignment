package com.apps.sportzinteractive.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sportzinteractive.retrofit.ApiRepository
import kotlinx.coroutines.launch

class ApiViewModel : ViewModel() {

    private val repository = ApiRepository()

    private val _apiResponse = MutableLiveData<Map<String, Any>?>()
    val apiResponse: LiveData<Map<String, Any>?> get() = _apiResponse


    private val _matchInfo = MutableLiveData<String>()
    val matchInfo: LiveData<String> = _matchInfo

    private val _matchResult = MutableLiveData<String>()
    val matchResult: LiveData<String> = _matchResult

    private val _teamHomeName = MutableLiveData<String>()
    val teamHomeName: LiveData<String> = _teamHomeName

    private val _teamAwayName = MutableLiveData<String>()
    val teamAwayName: LiveData<String> = _teamAwayName

    private val _homeTeamScore = MutableLiveData<String>()
    val homeTeamScore: LiveData<String> = _homeTeamScore

    private val _awayTeamScore = MutableLiveData<String>()
    val awayTeamScore: LiveData<String> = _awayTeamScore

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun processApiResponse(response: Map<*, *>?) {
        response?.let {
            val matchDetails = it["Matchdetail"] as? Map<*, *>
            val inningDetails = it["Innings"] as? List<*>
            val teamInfo = it["Teams"] as? Map<*, *>

            // Extract home and away teams
            val teamHome = matchDetails?.get("Team_Home") as? String ?: ""
            val teamAway = matchDetails?.get("Team_Away") as? String ?: ""

            // Extract match info
            val match = matchDetails?.get("Match") as? Map<*, *>
            _matchInfo.postValue(
                listOfNotNull(
                    match?.get("Number") as? String,
                    match?.get("Date") as? String,
                    "@${match?.get("Time") as? String}"
                ).joinToString(", ")
            )

            // Match result
            _matchResult.postValue(matchDetails?.get("Result")?.toString() ?: "No result")

            // Extract team names
            _teamHomeName.postValue(
                teamInfo?.get(teamHome)?.let { (it as? Map<*, *>)?.get("Name_Full") }?.toString() ?: "N/A"
            )
            _teamAwayName.postValue(
                teamInfo?.get(teamAway)?.let { (it as? Map<*, *>)?.get("Name_Full") }?.toString() ?: "N/A"
            )

            // Extract team scores
            _homeTeamScore.postValue(extractInningsDetails(inningDetails?.getOrNull(1)))
            _awayTeamScore.postValue(extractInningsDetails(inningDetails?.getOrNull(0)))

            // Stop shimmer and show data
            _loading.postValue(false)
        } ?: Log.e("API_RESPONSE", "No data received")
    }

    private fun extractInningsDetails(inning: Any?): String {
        val inningMap = inning as? Map<*, *> ?: return "N/A"
        val runs = inningMap["Total"] ?: "0"
        val wickets = inningMap["Wickets"] ?: "0"
        val overs = inningMap["Overs"] ?: "0.0"
        return "$runs-$wickets ($overs)"
    }

    fun fetchApiData(url: String) {
        viewModelScope.launch {
            val response = repository.fetchApiData(url)
            _apiResponse.postValue(response)
        }
    }
}