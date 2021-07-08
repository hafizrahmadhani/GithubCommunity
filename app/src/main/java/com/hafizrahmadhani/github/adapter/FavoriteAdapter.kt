package com.hafizrahmadhani.github.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hafizrahmadhani.github.R
import com.hafizrahmadhani.github.datamodel.DataModelFavUser
import kotlinx.android.synthetic.main.user_favorite.view.*

class FavoriteAdapter(private val favorite : ArrayList<DataModelFavUser>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataModelFavUser)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(favUser : DataModelFavUser){
            with(itemView){
                Glide.with(itemView.context)
                    .load(favUser.avatar_url)
                    .apply(RequestOptions().override(80,80))
                    .into(img_userfav)

                id_namefav.text = favUser.login

                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(favUser)}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorite[position])
    }

    override fun getItemCount(): Int = favorite.size

}