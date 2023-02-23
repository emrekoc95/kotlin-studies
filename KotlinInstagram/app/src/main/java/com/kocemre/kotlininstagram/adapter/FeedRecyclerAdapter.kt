package com.kocemre.kotlininstagram.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kocemre.kotlininstagram.databinding.RecyclerRowBinding
import com.kocemre.kotlininstagram.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter (val postArrayList: ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.FeedHolder>() {
    class FeedHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FeedHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        val stringArray = postArrayList.get(position).userEmail.split('@')
        holder.binding.recyclerEmailText.text = stringArray[0].toString()
        Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.binding.recyclerImageView)
        holder.binding.recyclerCommentText.text = postArrayList.get(position).comment
    }

    override fun getItemCount(): Int {
        return postArrayList.size
    }
}