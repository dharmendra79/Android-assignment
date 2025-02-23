package com.apps.sportzinteractive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.sportzinteractive.databinding.LayoutPlayerBinding
import com.apps.sportzinteractive.model.PlayerModel

class PlayerAdapter(private val players: List<PlayerModel>,
                    private val onItemClick: (PlayerModel) -> Unit) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(private val binding: LayoutPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(player: PlayerModel, onItemClick: (PlayerModel) -> Unit) {
            binding.player = player  // Set player data to binding
            binding.executePendingBindings()  // Ensure UI updates immediately

            binding.root.setOnClickListener {
                onItemClick(player)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutPlayerBinding.inflate(inflater, parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position], onItemClick)
    }

    override fun getItemCount(): Int = players.size
}
