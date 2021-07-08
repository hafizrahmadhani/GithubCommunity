package com.hafizrahmadhani.github.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hafizrahmadhani.github.datamodel.DataModelRv
import com.hafizrahmadhani.github.R
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(private val listUser: ArrayList<DataModelRv>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: DataModelRv){
            with(itemView){
                Glide.with(itemView.context)
                    .load(item.avatar_url)
                    .apply(RequestOptions().override(105, 105))
                    .into(img_user)
                id_name.text = item.login

                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(item)}
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: DataModelRv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}