package com.test.myplaylist.ui.screen.home

import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.myplaylist.common.ItemClickable
import com.test.myplaylist.common.ItemClickableImpl
import com.test.myplaylist.databinding.ItemAudioListBinding
import com.test.myplaylist.domain.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

@SuppressLint("NotifyDataSetChanged")
internal class MusicListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemClickable<MusicListAdapter.OnItemClick> by ItemClickableImpl() {
    var items = mutableListOf<Music?>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ViewHolderItem(ItemAudioListBinding.inflate(inflate, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MusicListAdapter.ViewHolderItem -> {
                holder.bind(items[position])
            }
        }
    }

    fun onPauseAllAudio(m: Music) {
        items.forEachIndexed { index, music ->
            if (m.name == music!!.name) return@forEachIndexed
            music.isPlaying = false
            notifyItemChanged(index)
        }
    }

    fun updateItem(m: Music) {
        items.forEachIndexed { index, music ->
            if (m.name == music!!.name) {
                items[index] = m
                notifyItemChanged(index)
            }
        }
    }


    internal inner class ViewHolderItem(
        private val binding: ItemAudioListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Music?) {
            model?.let { data ->
                with(binding) {
                    fileName = data.name
                    music = data
                    ivIcon.setOnClickListener {
                        CoroutineScope((Dispatchers.IO)).launch {
                            data.isPlaying = !data.isPlaying
                            music = data
                            notifyItemClick(OnItemClick.OnPlayAudio(data))
                        }
                    }
                }
            }
    }

}

sealed class OnItemClick {
    data class OnPlayAudio(val data: Music) : OnItemClick()
}
}