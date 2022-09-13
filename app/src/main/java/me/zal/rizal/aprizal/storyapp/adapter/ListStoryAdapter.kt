package me.zal.rizal.aprizal.storyapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import me.zal.rizal.aprizal.storyapp.R
import me.zal.rizal.aprizal.storyapp.model.story.ListStoryItem

class ListStoryAdapter(private val listStoryItem: ArrayList<ListStoryItem>) :
    RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgCardStory: ImageView = itemView.findViewById(R.id.img_card_story)
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_description)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setStories(listStoryItem: List<ListStoryItem>) {
        this.listStoryItem.clear()
        this.listStoryItem.addAll(listStoryItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_stories, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyItem = listStoryItem[position]
        Glide.with(holder.itemView.context)
            .load(storyItem.photoUrl)
            .apply(RequestOptions().override(512, 512))
            .into(holder.imgCardStory)
        holder.tvName.text = storyItem.name
        holder.tvDescription.text = storyItem.description
    }

    override fun getItemCount(): Int {
        return listStoryItem.size
    }

}