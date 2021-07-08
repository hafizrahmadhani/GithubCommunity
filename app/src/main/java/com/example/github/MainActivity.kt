package com.example.github

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView()
    }

    private fun recyclerView() {
        rv_gitUser.apply {
            val userAdapter = UserAdapter(UserData.listData)
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: DataModelUser) {
                    val intent = Intent(this@MainActivity, UserDetails::class.java)
                    intent.putExtra("Nama", data)
                    startActivity(intent)
                }
            })
        }
    }
}