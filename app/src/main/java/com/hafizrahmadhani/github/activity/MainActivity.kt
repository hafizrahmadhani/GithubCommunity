package com.hafizrahmadhani.github.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hafizrahmadhani.github.*
import com.hafizrahmadhani.github.adapter.UserAdapter
import com.hafizrahmadhani.github.database.FavoriteHelper
import com.hafizrahmadhani.github.datamodel.DataModelRv
import com.hafizrahmadhani.github.viewmodel.ViewModelUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var viewModelUser : ViewModelUser
    private lateinit var favoriteHelper: FavoriteHelper

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_gitUser.setHasFixedSize(true)
        callProgressBar(true)

        viewModelUser = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModelUser::class.java)
        viewModelUser.callUser("hafizrahmadhani")

        viewModelUser.takeError().observe(this, {
            when(it){
                true -> callErrorMsg()
                else -> callUser()
            }
        })

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    private fun callProgressBar(status : Boolean){
        if(status){
            mainProgressBar.visibility = View.VISIBLE
        }
        else
            mainProgressBar.visibility = View.GONE
    }

    private fun callErrorMsg() {
        viewModelUser.takeMsgError().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun callUser() {
        viewModelUser.takeUser().observe(this, {
            recyclerView(it)
            callProgressBar(false)
        })
    }

    private fun recyclerView(param : ArrayList<DataModelRv>) {
        rv_gitUser.apply {
            Log.d(TAG, rv_gitUser.toString())
            val userAdapter = UserAdapter(param)
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: DataModelRv) {
                    val mainIntent = Intent(this@MainActivity, UserDetailsActivity::class.java)
                      mainIntent.putExtra("user", data.login)
                      startActivity(mainIntent)
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> {
                val intentSettings = Intent(this, SettingsActivity::class.java)
                startActivity(intentSettings)
            }

            R.id.favorite_user -> {
                val intentFavorite = Intent(this, FavoriteActivity::class.java)
                startActivity(intentFavorite)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModelUser.callUser(query)
                callProgressBar(true)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
}