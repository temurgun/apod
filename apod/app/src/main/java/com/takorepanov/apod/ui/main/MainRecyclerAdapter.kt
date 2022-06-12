package com.takorepanov.apod.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.takorepanov.apod.R
import com.takorepanov.apod.data.model.AstronomyPictureOfTheDay
import com.squareup.picasso.Picasso
import java.util.*

class MainRecyclerAdapter() :
    RecyclerView.Adapter<MainViewHolder>() {

    private val pictures: MutableList<AstronomyPictureOfTheDay> = LinkedList()

    fun setData(newPictures: List<AstronomyPictureOfTheDay>) {
        pictures.clear()
        pictures.addAll(newPictures)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.title.text = pictures[position].title
        Picasso.with(holder.image.context).load(pictures[position].url)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.image)
        holder.description.text = pictures[position].explanation
        holder.date.text = pictures[position].date.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MainViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

}

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.recycler_item_title)
    val image: ImageView = itemView.findViewById(R.id.recycler_item_image)
    val description: TextView = itemView.findViewById(R.id.recycler_item_description)
    val date: TextView = itemView.findViewById(R.id.recycler_item_date)
}

private object AstronomyPictureOfTheDayDiffItemCallback :
    DiffUtil.ItemCallback<AstronomyPictureOfTheDay>() {

    override fun areItemsTheSame(
        oldItem: AstronomyPictureOfTheDay,
        newItem: AstronomyPictureOfTheDay
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: AstronomyPictureOfTheDay,
        newItem: AstronomyPictureOfTheDay
    ): Boolean {
        return oldItem.title == newItem.title && oldItem.url == newItem.url
    }
}