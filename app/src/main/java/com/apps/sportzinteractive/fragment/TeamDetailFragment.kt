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

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     * UI should be attached to. The fragment should not add the view itself,
     * but rather return the view to the host.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Setting up the viewPager with TabLayout to display the team squad and a filter FloatinActionButton
     * to filter the data for All Team, Team A ana Team B
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager("All") // Default filter (All)

        binding.filterTeam.setOnClickListener {
            showFilterDialog()
        }
    }

    /**
     * Sets up the ViewPager with fragments based on the provided filter.
     *
     * This method dynamically creates and configures the ViewPager adapter,
     * populating it with either one or two fragments representing team squads,
     * depending on the filter applied. It also configures the TabLayout to
     * display the appropriate team names as tabs.
     *
     * @param filter The filter to apply, which determines which fragments and
     * team names are used. It can be "All", the name of the home team,
     * or the name of the away team.
     */

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
