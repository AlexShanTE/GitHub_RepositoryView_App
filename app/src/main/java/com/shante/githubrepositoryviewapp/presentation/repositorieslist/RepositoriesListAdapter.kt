package com.shante.githubrepositoryviewapp.presentation.repositorieslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shante.githubrepositoryviewapp.R
import com.shante.githubrepositoryviewapp.databinding.RepositoryCardBinding
import com.shante.githubrepositoryviewapp.domain.models.Repo

class RepositoriesListAdapter(
    private val onClick: (repository: Repo) -> Unit
) : ListAdapter<Repo, RecyclerView.ViewHolder>(DiffCallBack) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Repo -> R.layout.repository_card
            else -> error("idk what it is item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.repository_card -> RepositoryCardViewHolder.from(parent, onClick)
            else -> error("View holder doesn't exist")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RepositoryCardViewHolder -> holder.bindRepositoryCard(getItem(position))
        }
    }

}

class RepositoryCardViewHolder(
    private val binding: RepositoryCardBinding,
    private val onClick: (repository: Repo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindRepositoryCard(repository: Repo) {
        binding.repositoryCard.setOnClickListener { onClick(repository) }
        binding.repositoryName.text = repository.name
        binding.repositoryDescription.text = repository.description
        binding.repositoryLanguage.text = repository.language
    }

    companion object {
        fun from(parent: ViewGroup, onClick: (repository: Repo) -> Unit): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RepositoryCardBinding.inflate(inflater, parent, false)
            return RepositoryCardViewHolder(binding, onClick)
        }
    }
}


private object DiffCallBack : DiffUtil.ItemCallback<Repo>() {

    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.name == newItem.name
    }
}