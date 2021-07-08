package com.example.github

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        detailUser()
    }

    private fun detailUser() {
        val call = intent.getParcelableExtra<DataModelUser>("Nama")
        call?.imgUser?.let { img_account_photo.setImageResource(it) }
        name.text = call?.name
        username.text = call?.userName
        followers.text = call?.followers
        following.text = call?.following
        repo.text = call?.repo
        location.text = call?.location
        company.text = call?.company
    }
}