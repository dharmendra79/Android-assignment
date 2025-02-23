package com.apps.sportzinteractive.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.apps.sportzinteractive.databinding.FragmentTeamsBinding
import com.apps.sportzinteractive.screens.MainActivity
import com.apps.sportzinteractive.viewModel.MatchDetailViewModel

/**
 * TeamFragment is responsible for displaying match details, including team names,
 * scores, overs, and results. It also manages UI loading states using a shimmer effect
 * and allows users to navigate to TeamDetailFragment.
 *
 * @author [Your Name]
 */

class TeamFragment : Fragment() {
    private val TAG = "TeamFragment"
    private lateinit var binding: FragmentTeamsBinding
    private val viewModel: MatchDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.matchInfo.observe(viewLifecycleOwner) {
            binding.txtMatchInfo.text = it
        }

        viewModel.matchResult.observe(viewLifecycleOwner) {
            binding.txtMatchResult.text = it
        }

        viewModel.teamHomeName.observe(viewLifecycleOwner) {
            binding.txtTeamHome.text = it
        }

        viewModel.teamAwayName.observe(viewLifecycleOwner) {
            binding.txtTeamAway.text = it
        }

        viewModel.homeTeamScore.observe(viewLifecycleOwner) {
            binding.txtHomeTeamScore.text = it
        }

        viewModel.awayTeamScore.observe(viewLifecycleOwner) {
            binding.txtAwayTeamScore.text = it
        }

        viewModel.homeTeamOver.observe(viewLifecycleOwner) {
            binding.txtTeamHomeOver.text = it
        }

        viewModel.awayTeamOver.observe(viewLifecycleOwner) {
            binding.txtTeamAwayOver.text = it
        }

        viewModel.awayTeamScore.observe(viewLifecycleOwner) {
            binding.txtAwayTeamScore.text = it
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.llTeamDetail.visibility = View.GONE
            } else {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.llTeamDetail.visibility = View.VISIBLE
            }
        }
        viewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            viewModel.processApiResponse(response)
        }

        binding.cardView.setOnClickListener {
            (activity as? MainActivity)?.openNewFragment(TeamDetailFragment())
        }

        binding.fullTableButton.setOnClickListener {
            (activity as? MainActivity)?.openNewFragment(TeamDetailFragment())
        }
    }
}