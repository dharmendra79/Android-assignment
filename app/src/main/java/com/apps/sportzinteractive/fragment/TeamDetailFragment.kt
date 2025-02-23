package com.apps.sportzinteractive.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.apps.sportzinteractive.adapter.ViewPagerAdapter
import com.apps.sportzinteractive.databinding.FragmentTeamDetailBinding
import com.apps.sportzinteractive.viewModel.MatchDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class TeamDetailFragment : Fragment() {
    private val TAG = "TeamDetailFragment"
    private lateinit var binding: FragmentTeamDetailBinding
    private lateinit var adapter: ViewPagerAdapter
    private val viewModel: MatchDetailViewModel by activityViewModels()
    private var currentFilter: String = "All" // Stores the last applied filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager("All") // Default filter (All)

        binding.filterTeam.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun setupViewPager(filter: String) {
        val teamHome = viewModel.teamHomeName.value ?: "Team A"
        val teamAway = viewModel.teamAwayName.value ?: "Team B"

        val fragments = when (filter) {
            "All" -> listOf(FragmentTeamHome(), FragmentTeamAway())
            teamHome -> listOf(FragmentTeamHome())
            teamAway -> listOf(FragmentTeamAway())
            else -> listOf(FragmentTeamHome(), FragmentTeamAway())
        }

        val teamNames = when (filter) {
            "All" -> listOf(teamHome, teamAway)
            teamHome -> listOf(teamHome)
            teamAway -> listOf(teamAway)
            else -> listOf(teamHome, teamAway)
        }

        adapter = ViewPagerAdapter(requireActivity(), fragments, teamNames)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = teamNames.size > 1 // Enable swiping only if both tabs are present

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = teamNames[position]
        }.attach()
    }

    private fun showFilterDialog() {
        val options = arrayOf("All", viewModel.teamHomeName.value.toString(), viewModel.teamAwayName.value.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Filter Teams")
            .setItems(options) { _, which ->
                val selectedFilter = options[which]

                if (selectedFilter != currentFilter) { // Prevent redundant updates
                    currentFilter = selectedFilter
                    filterTeams(selectedFilter)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun filterTeams(filter: String) {
        Log.d(TAG, "Applying filter: $filter")
        setupViewPager(filter)
        binding.viewPager.currentItem = 0 // Always reset to the first tab
    }
}
