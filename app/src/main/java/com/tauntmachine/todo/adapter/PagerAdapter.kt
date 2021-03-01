package com.tauntmachine.todo.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tauntmachine.todo.fragment.HomeFragment
import com.tauntmachine.todo.fragment.ListFragment


class PagerAdapter(
    private val myContext: Context,
    fm: FragmentManager?,
    var totalTabs: Int ) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }
            1 -> {
                ListFragment()
            }
            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

}