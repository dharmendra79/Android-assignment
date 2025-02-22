package com.apps.sportzinteractive.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.sportzinteractive.model.Batting
import com.apps.sportzinteractive.model.Bowling
import com.apps.sportzinteractive.model.PlayerModel
import com.apps.sportzinteractive.retrofit.ApiRepository
import kotlinx.coroutines.launch

class MatchDetailViewModel : ViewModel() {
    private val TAG = "ApiViewModel"
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

    private val _homeTeamOver = MutableLiveData<String>()
    val homeTeamOver: LiveData<String> = _homeTeamOver
    private val _awayTeamOver = MutableLiveData<String>()
    val awayTeamOver: LiveData<String> = _awayTeamOver

    private val _awayTeamScore = MutableLiveData<String>()
    val awayTeamScore: LiveData<String> = _awayTeamScore

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _playersA = MutableLiveData<List<PlayerModel>>()
    val playersA: LiveData<List<PlayerModel>> get() = _playersA
    private val _playersB = MutableLiveData<List<PlayerModel>>()
    val playersB: LiveData<List<PlayerModel>> get() = _playersB

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
                teamInfo?.get(teamHome)?.let { (it as? Map<*, *>)?.get("Name_Full") }?.toString()
                    ?: "N/A"
            )
            _teamAwayName.postValue(
                teamInfo?.get(teamAway)?.let { (it as? Map<*, *>)?.get("Name_Full") }?.toString()
                    ?: "N/A"
            )

            // Extract team scores
            _homeTeamScore.postValue(extractInningsDetails(inningDetails?.getOrNull(1)))
            _awayTeamScore.postValue(extractInningsDetails(inningDetails?.getOrNull(0)))
            _homeTeamOver.postValue(extractInningsOver(inningDetails?.getOrNull(1)))
            _awayTeamOver.postValue(extractInningsOver(inningDetails?.getOrNull(0)))
            updatePlayersData(teamInfo, teamHome, teamAway)

            _loading.postValue(false)
        } ?: Log.e("API_RESPONSE", "No data received")
    }

    private fun extractInningsDetails(inning: Any?): String {
        val inningMap = inning as? Map<*, *> ?: return "N/A"
        val runs = inningMap["Total"] ?: "0"
        val wickets = inningMap["Wickets"] ?: "0"
        val overs = inningMap["Overs"] ?: "0.0"
        return "$runs-$wickets"
    }

    private fun extractInningsOver(inning: Any?): String {
        val inningMap = inning as? Map<*, *> ?: return "N/A"
        val overs = inningMap["Overs"] ?: "0.0"
        return "$overs"
    }

    fun fetchApiData(url: String) {
        viewModelScope.launch {
            val response = repository.fetchApiData(url)
            _apiResponse.postValue(response)
        }
    }

    private fun extractPlayers(teamDetails: Map<*, *>?): List<PlayerModel> {
        val playersMap = teamDetails?.get("Players") as? Map<*, *> ?: return emptyList()

        return playersMap.mapNotNull { (_, value) ->
            val playerData = value as? Map<*, *> ?: return@mapNotNull null

            fun extractBattingData(): Batting {
                val batting = playerData["Batting"] as? Map<*, *> ?: emptyMap<String, String>()
                return Batting(
                    batting["Average"] as? String ?: "N/A",
                    batting["Runs"] as? String ?: "N/A",
                    batting["Strikerate"] as? String ?: "N/A",
                    batting["Style"] as? String ?: "N/A"
                )
            }

            fun extractBowlingData(): Bowling {
                val bowling = playerData["Bowling"] as? Map<*, *> ?: emptyMap<String, String>()
                return Bowling(
                    bowling["Average"] as? String ?: "N/A",
                    bowling["Economyrate"] as? String ?: "N/A",
                    bowling["Style"] as? String ?: "N/A",
                    bowling["Wickets"] as? String ?: "N/A"
                )
            }

            val isCaptain: Boolean = playerData.contains("Iscaptain")
            val isKeeper: Boolean = playerData.contains("Iskeeper")
            PlayerModel(
                extractBattingData(),
                extractBowlingData(),
                isCaptain,
                isKeeper,
                playerData["Name_Full"] as? String ?: "Unknown",
                playerData["Position"] as? String ?: "N/A"
            )
        }
    }

    private fun updatePlayersData(teamInfo: Map<*, *>?, teamHome: String, teamAway: String) {
        val teamHomeDetail = teamInfo?.get(teamHome) as? Map<*, *>
        val teamAwayDetail = teamInfo?.get(teamAway) as? Map<*, *>

        _playersA.postValue(extractPlayers(teamHomeDetail))
        _playersB.postValue(extractPlayers(teamAwayDetail))
    }

}