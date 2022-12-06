package com.test.myplaylist.ui.screen.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.myplaylist.common.ItemClickable
import com.test.myplaylist.common.ItemClickableImpl
import com.test.myplaylist.databinding.ItemUsersListBinding
import com.test.myplaylist.domain.Music
import com.test.myplaylist.domain.model.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("NotifyDataSetChanged")
internal class UsersListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemClickable<UsersListAdapter.OnItemClick> by ItemClickableImpl() {
    var items = mutableListOf<Users?>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ViewHolderItem(ItemUsersListBinding.inflate(inflate, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UsersListAdapter.ViewHolderItem -> {
                holder.bind(items[position])
            }
        }
    }


    internal inner class ViewHolderItem(
        private val binding: ItemUsersListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Users?) {
            model?.let { data ->
                with(binding) {
                    Timber.d("values------- $data")
                    users = data
                    itemView.setOnClickListener {
                        CoroutineScope((Dispatchers.IO)).launch {
                            notifyItemClick(OnItemClick.OnItemClickUser(data))
                        }
                    }
                }
            }
        }

    }

    sealed class OnItemClick {
        data class OnItemClickUser(val data: Users) : OnItemClick()
    }
}