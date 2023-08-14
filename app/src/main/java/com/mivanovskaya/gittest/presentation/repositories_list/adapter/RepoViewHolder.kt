package com.mivanovskaya.gittest.presentation.repositories_list.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.databinding.RepositoriesViewHolderBinding
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.presentation.tools.Language

class RepoViewHolder(private val binding: RepositoriesViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Repo, onClick: (item: Repo) -> Unit) {

        with(binding) {
            repoCard.setOnClickListener { onClick(item) }

            title.text = item.name
            description.text = item.description
            language.text = item.language

            val color = when (item.language) {
                Language.Java.name -> R.color.yellow
                Language.Kotlin.name -> R.color.lilac
                Language.Python.name -> R.color.accent
                Language.JavaScript.name -> R.color.secondary
                else -> R.color.white_50_opacity
            }
            language.setTextColor(ContextCompat.getColor(repoCard.context, color))
        }
    }
}

