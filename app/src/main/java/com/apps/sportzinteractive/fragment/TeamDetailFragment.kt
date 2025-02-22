package com.apps.sportzinteractive.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.sportzinteractive.PlayerAdapter
import com.apps.sportzinteractive.databinding.FragmentTeamDetailBinding
import com.apps.sportzinteractive.viewModel.ApiViewModel

class TeamDetailFragment : Fragment() {
    lateinit var binding: FragmentTeamDetailBinding
    val viewModel: ApiViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTeamA.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTeamB.layoutManager = LinearLayoutManager(requireContext())

        viewModel.teamHomeName.observe(viewLifecycleOwner) { value ->
            binding.txtTeamAName.text = value
        }
        viewModel.teamAwayName.observe(viewLifecycleOwner) { value ->
            binding.txtTeamBName.text = value
        }
        viewModel.playersA.observe(viewLifecycleOwner) { players ->
            binding.recyclerTeamA.adapter = PlayerAdapter(players)
        }
        viewModel.playersB.observe(viewLifecycleOwner) { players ->
            binding.recyclerTeamB.adapter = PlayerAdapter(players)
        }
    }
}