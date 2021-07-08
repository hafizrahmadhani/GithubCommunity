package com.hafizrahmadhani.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hafizrahmadhani.consumerapp.R
import com.hafizrahmadhani.consumerapp.datamodel.DataModelFavUser
import kotlinx.android.synthetic.main.user_favorite.view.*

class FavoriteAdapter(private val fav : ArrayList<DataModelFavUser>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

   private var listFavorite = ArrayList<DataModelFavUser>()
       set(listFav) {
           if (listFav.size > 0) {
               this.listFavorite.clear()
           }
           this.listFavorite.addAll(listFav)
           notifyDataSetChanged()
       }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favUser : DataModelFavUser) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(favUser.avatar_url)
                    .apply(RequestOptions().override(80, 80))
                    .into(img_userfav)

                id_namefav.text = favUser.login
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(fav[position])
    }

    override fun getItemCount(): Int = fav.size

}