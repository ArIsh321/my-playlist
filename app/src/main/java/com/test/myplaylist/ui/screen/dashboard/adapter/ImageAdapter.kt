package com.test.myplaylist.ui.screen.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.myplaylist.databinding.ItemCarouselViewBinding
import com.test.myplaylist.domain.model.ImagesList
import com.test.myplaylist.extension.loadImage

class ImageAdapter(
    private val imageModel: List<ImagesList>
) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemCarouselViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(dataModel: ImagesList) {
            binding.apply {
                imgView.loadImage(dataModel.imgUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        ItemCarouselViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ImageAdapter.MyViewHolder, position: Int) {
        holder.bindView(imageModel[position])
    }

    override fun getItemCount(): Int = imageModel.size
}

