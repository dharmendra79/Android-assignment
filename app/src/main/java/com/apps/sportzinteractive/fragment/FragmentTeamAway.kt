package com.apps.sportzinteractive.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.sportzinteractive.PlayerAdapter
import com.apps.sportzinteractive.databinding.FragmentTeamAwayBinding
import com.apps.sportzinteractive.utils.showPlayerDialog
import com.apps.sportzinteractive.viewModel.MatchDetailViewModel

class FragmentTeamAway : Fragment() {
    lateinit var binding: FragmentTeamAwayBinding
    val viewModel: MatchDetailViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeamAwayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerTeamB.layoutManager = LinearLayoutManager(requireContext())
            viewModel.playersB.observe(viewLifecycleOwner) { players ->
                recyclerTeamB.adapter = PlayerAdapter(players) { player ->
                    showPlayerDialog(requireContext(), player)
                }
            }
        }
    }
}