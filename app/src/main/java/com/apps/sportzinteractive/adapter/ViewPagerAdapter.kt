package com.apps.sportzinteractive.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.apps.sportzinteractive.fragment.FragmentTeamAway
import com.apps.sportzinteractive.fragment.FragmentTeamHome

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentTeamHome()  // First Tab
            1 -> FragmentTeamAway() // Second Tab
            else -> FragmentTeamHome()
        }
    }
}
