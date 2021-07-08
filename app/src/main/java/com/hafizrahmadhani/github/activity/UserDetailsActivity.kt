package com.hafizrahmadhani.github.activity

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hafizrahmadhani.github.datamodel.DataModelUser
import com.hafizrahmadhani.github.R
import com.hafizrahmadhani.github.viewmodel.ViewModelDetail
import com.hafizrahmadhani.github.adapter.PagerAdapter
import com.hafizrahmadhani.github.database.DatabaseContract
import com.hafizrahmadhani.github.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.hafizrahmadhani.github.database.FavoriteHelper
import com.hafizrahmadhani.github.databinding.ActivityUserDetailsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.android.synthetic.main.user_item.*
import kotlinx.coroutines.InternalCoroutinesApi

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserDetailsBinding

    private lateinit var viewModelDetail: ViewModelDetail
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var favorite : FloatingActionButton
    private lateinit var contentValues: ContentValues
    private var query = false
    private var statusFavorite = false

    @InternalCoroutinesApi
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

        contentValues = ContentValues()

        callProgressBar(true)

        binding.tvFollowers.text = resources.getString(R.string.followers)

        binding.tvFollowing.text = resources.getString(R.string.following)

        binding.tvRepo.text = resources.getString(R.string.repository)

        favorite = binding.favButton


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

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        setStatusFavorite(statusFavorite, false)
        favorite.setOnClickListener{
            statusFavorite = !statusFavorite
            contentValues.put(DatabaseContract.FavColumns.FAV, statusFavorite)
            contentValues.put(DatabaseContract.FavColumns.LOGIN, username)

            when(query){
                true -> favoriteHelper.update(contentValues.getAsString(DatabaseContract.FavColumns.USER_NAME), contentValues)
                false -> contentResolver.insert(CONTENT_URI, contentValues)
            }

            setStatusFavorite(statusFavorite, true)
        }

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

        val data = favoriteHelper.queryById(userDetails.name)

        query = data.count > 0
        if(query){
            data.moveToFirst()
            statusFavorite = data.getInt(data.getColumnIndex(DatabaseContract.FavColumns.FAV)) == 1
            setStatusFavorite(statusFavorite, false)
        }
        contentValues.put(DatabaseContract.FavColumns.ID, userDetails.id)
        contentValues.put(DatabaseContract.FavColumns.USER_NAME, userDetails.name)
        contentValues.put(DatabaseContract.FavColumns.AVATAR, userDetails.avatar_url)

    }

    private fun callProgressBar(status: Boolean) {
        if (status) {
            progress_bar.visibility = View.VISIBLE
        } else progress_bar.visibility = View.GONE
    }

    private fun setStatusFavorite(statusFavorite: Boolean, withToast: Boolean) {
        if(statusFavorite) {
            if (withToast)Toast.makeText(this, "Dipilih menjadi Favorit", Toast.LENGTH_SHORT).show()
        }
        else {
            if (withToast)Toast.makeText(this, "Berhenti dijadikan Favorit", Toast.LENGTH_SHORT).show()
        }
    }
}