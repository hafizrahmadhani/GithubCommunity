package com.hafizrahmadhani.consumerapp.activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafizrahmadhani.consumerapp.R
import com.hafizrahmadhani.consumerapp.adapter.FavoriteAdapter
import com.hafizrahmadhani.consumerapp.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.hafizrahmadhani.consumerapp.database.MappingHelper
import com.hafizrahmadhani.consumerapp.datamodel.DataModelFavUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listFavorite = ArrayList<DataModelFavUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    private fun recyclerviewFavorite(){
        takeData()
        rv_userFav.layoutManager = LinearLayoutManager(this)
        val favoriteAdapter = FavoriteAdapter(listFavorite)
        rv_userFav.adapter = favoriteAdapter
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