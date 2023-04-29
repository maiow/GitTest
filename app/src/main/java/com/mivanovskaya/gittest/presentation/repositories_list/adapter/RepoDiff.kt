package com.mivanovskaya.gittest.presentation.repositories_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mivanovskaya.gittest.data.model.Repo

class RepoDiff : DiffUtil.ItemCallback<Repo>() {

    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) =
        oldItem == newItem
}