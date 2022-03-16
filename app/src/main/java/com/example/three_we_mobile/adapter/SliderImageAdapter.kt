package com.example.three_we_mobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.three_we_mobile.R
import com.example.three_we_mobile.databinding.MainSliderBinding
import com.example.three_we_mobile.model.SliderImage

class SliderImageAdapter(
    private var context: Context,
    private var imageList: List<SliderImage> = emptyList(),
    private val onItemClick: (SliderImage) -> Unit,
): RecyclerView.Adapter<SliderImageAdapter.ViewHolder>() {


    inner class ViewHolder(view: MainSliderBinding) : RecyclerView.ViewHolder(view.root) {
        val image : ImageView = view.sliderAdsImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MainSliderBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = imageList[position]
        Glide.with(holder.itemView)
            .load(item.image)
            .placeholder(R.drawable.sticky_header_bg)
            .into(holder.image)

        holder.image.setOnClickListener {
            onItemClick(imageList[position])
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}