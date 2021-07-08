package com.hafizrahmadhani.github

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var viewModelUser : ViewModelUser

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}