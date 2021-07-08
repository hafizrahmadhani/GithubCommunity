package com.hafizrahmadhani.github

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.user_item.view.*

class FollowersAdapter (private var followers: ArrayList<DataModelRv>) : RecyclerView.Adapter<FollowersAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: DataModelRv){
            with(itemView){
                Glide.with(itemView.context)
                    .load(item.avatar_url)
                    .apply(RequestOptions().override(60, 60))
                    .into(img_user)
                id_name.text = item.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followers[position])
    }

    override fun getItemCount(): Int =followers.size

}