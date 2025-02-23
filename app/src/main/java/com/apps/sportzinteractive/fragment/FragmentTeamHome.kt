package com.apps.sportzinteractive.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.sportzinteractive.PlayerAdapter
import com.apps.sportzinteractive.R
import com.apps.sportzinteractive.databinding.FragmentTeamHomeBinding
import com.apps.sportzinteractive.utils.showPlayerDialog
import com.apps.sportzinteractive.viewModel.MatchDetailViewModel

class FragmentTeamHome : Fragment() {
    lateinit var binding: FragmentTeamHomeBinding
    val viewModel: MatchDetailViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeamHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerTeamA.layoutManager = LinearLayoutManager(requireContext())
            viewModel.playersA.observe(viewLifecycleOwner) { players ->
                recyclerTeamA.adapter = PlayerAdapter(players) { player ->
                    showPlayerDialog(requireContext(), player)
                }
            }
        }
    }
}