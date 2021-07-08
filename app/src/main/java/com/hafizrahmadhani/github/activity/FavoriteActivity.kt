package com.hafizrahmadhani.github.activity

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafizrahmadhani.github.R
import com.hafizrahmadhani.github.adapter.FavoriteAdapter
import com.hafizrahmadhani.github.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.hafizrahmadhani.github.database.FavoriteHelper
import com.hafizrahmadhani.github.database.MappingHelper
import com.hafizrahmadhani.github.datamodel.DataModelFavUser
import com.loopj.android.http.AsyncHttpClient.log
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    private val listFavorite = ArrayList<DataModelFavUser>()
    private lateinit var favoriteHelper: FavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteHelper = FavoriteHelper(applicationContext)

        rv_userFav.setHasFixedSize(true)

        val handlerThread = HandlerThread("Observer")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val observer = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                recyclerviewFavorite()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI,true, observer)

        if(savedInstanceState== null){
            recyclerviewFavorite()
        }
    }

    private fun recyclerviewFavorite() {
        takeData()
        rv_userFav.layoutManager = LinearLayoutManager(this)

        val favoriteAdapter = FavoriteAdapter(listFavorite)
        rv_userFav.adapter = favoriteAdapter

        favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataModelFavUser) {
                val intentFavorite = Intent(this@FavoriteActivity, UserDetailsActivity::class.java)
                val user = data.login
                intentFavorite.putExtra("user", user)
                startActivity(intentFavorite)
            }
        })
        log.e(favoriteAdapter.toString(), "terjadi kesalahan")
    }

    private fun takeData(){
        val query = contentResolver.query(CONTENT_URI, null, null, null, null)
        val mappingHelper = MappingHelper.mapCursorToArrayList(query)

        if(query != null){
            if(query.count > 0){
                progress_bar.visibility = View.GONE

                listFavorite.addAll(mappingHelper)
            }
            else{
                if(query.count == 0){
                    progress_bar.visibility = View.GONE
                }
                callErrorMsg()
            }
        }
    }

    private fun callErrorMsg() {
        Toast.makeText(this, "Data Kosong", Toast.LENGTH_LONG).show()
    }
}