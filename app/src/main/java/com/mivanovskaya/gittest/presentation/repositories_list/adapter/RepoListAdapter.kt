package com.mivanovskaya.gittest.presentation.repositories_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mivanovskaya.gittest.data.model.Repo
import com.mivanovskaya.gittest.databinding.RepositoriesViewHolderBinding

class RepoListAdapter(
    private val onClick: (Repo) -> Unit
) : ListAdapter<Repo, RepoViewHolder>(RepoDiff()) {

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item) { Repo ->
                onClick(Repo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RepoViewHolder(
        RepositoriesViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}