@file:Suppress("DEPRECATION")

package com.hafizrahmadhani.github.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hafizrahmadhani.github.fragment.FollowersFragment
import com.hafizrahmadhani.github.fragment.FollowingFragment

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String? = null

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var frag: Fragment? = null
        val dummyBundle = Bundle()
        dummyBundle.putString("extra_name", username)
        when (position) {
            0 -> frag = FollowersFragment()
            1 -> frag = FollowingFragment()
        }
        frag?.arguments = dummyBundle
        return frag as Fragment
    }
}