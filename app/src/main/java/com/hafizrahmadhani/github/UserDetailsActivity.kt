package com.hafizrahmadhani.github

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hafizrahmadhani.github.databinding.ActivityUserDetailsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserDetailsBinding

    private lateinit var viewModelDetail: ViewModelDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabs = findViewById<TabLayout>(R.id.tabs)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val username = intent.getStringExtra("user")
        val tabTitles = intArrayOf(R.string.tab_1, R.string.tab_2)
        val pagerAdapter = PagerAdapter(this)
        pagerAdapter.username = username
        view_pager.adapter = pagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(tabTitles[position])
        }.attach()

        callProgressBar(true)

        binding.tvFollowers.text = resources.getString(R.string.followers)

        binding.tvFollowing.text = resources.getString(R.string.following)

        binding.tvRepo.text = resources.getString(R.string.repository)


        viewModelDetail = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ViewModelDetail::class.java)

        username.let {
            pagerAdapter.username = it
            it?.let { it1 -> viewModelDetail.callDetailUser(it1) }
        }

        viewModelDetail.takeError().observe(this, {
            when (it) {
                true -> callErrorMsg()
                false -> viewModelDetail.callDetailUser(username as String)
            }
        })

        viewModelDetail.takeDetailUser().observe(this, {
            detailUser(it)
            callProgressBar(false)
        })
    }

    private fun callErrorMsg() {
        viewModelDetail.takeMsgError().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun detailUser(userDetails: DataModelUser) {
        Glide.with(this@UserDetailsActivity)
            .load(userDetails.avatar_url)
            .apply(RequestOptions().override(105, 105)).into(img_account_photo)

        name.text = userDetails.name
        username.text = userDetails.login
        followers.text = userDetails.followers.toString()
        following.text = userDetails.following.toString()
        repo.text = userDetails.repo.toString()
    }

    private fun callProgressBar(status: Boolean) {
        if (status) {
            progress_bar.visibility = View.VISIBLE
        } else progress_bar.visibility = View.GONE
    }
}