package com.tauntmachine.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.tauntmachine.todo.adapter.PagerAdapter


class MainActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var adapter_viewPager: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Grocery List"
        tabLayout = findViewById( R.id.tabLayout )
        viewPager = findViewById( R.id.viewPager )

        addTab()
        setAdapter()


        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun addTab() {
        tabLayout!!.addTab( this.tabLayout!!.newTab().setText( "Home" ) )
        tabLayout!!.addTab( this.tabLayout!!.newTab().setText( "All List" ) )
    }

    private fun setAdapter() {
        adapter_viewPager = PagerAdapter( this, supportFragmentManager, tabLayout!!.tabCount )
        viewPager!!.adapter = adapter_viewPager
        viewPager!!.addOnPageChangeListener( TabLayoutOnPageChangeListener( tabLayout ) )
    }
}