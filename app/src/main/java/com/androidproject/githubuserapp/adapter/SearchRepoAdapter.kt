package com.androidproject.githubuserapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.githubuserapp.activity.DetailsActivity
import com.androidproject.githubuserapp.activity.RepoScreen
import com.androidproject.githubuserapp.databinding.ItemAddRepoCardViewBinding
import com.androidproject.githubuserapp.model.GithubResponse
import com.androidproject.githubuserapp.model.RepoGithub
import retrofit2.Callback

//Search Adapter

class SearchRepoAdapter(private val context: Context, private val list: MutableList<RepoGithub>) : RecyclerView.Adapter<SearchRepoAdapter.RepoViewHolder>()
{
    inner class RepoViewHolder(private val binding: ItemAddRepoCardViewBinding): RecyclerView.ViewHolder(binding.root) {

        val shareBtn:ImageView = binding.share

        //Binding the data with the UI
        fun bindData(data: RepoGithub) {
            binding.repositoryName.text = data.name
            binding.description.text = data.description
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemAddRepoCardViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val data = list[position]

        holder.bindData(list[position])

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
                .apply {
                    putExtra("title", data.name)
                    putExtra("url", data.html_url)
                    putExtra("description", data.description)
                    putExtra("id", data.id.toString())
                    putExtra("avatar", data.owner.avatar_url)
                    putExtra("stargazers", data.stargazers_count.toString())
                    putExtra("watchers", data.watchers_count.toString())
                    putExtra("forks", data.forks_count.toString())
                    putExtra("subscribers", data.subscribers_count.toString())
//                    putExtra("open_issues", data.open_issues_count.toString())
                    putExtra("owner", data.owner.login)
                }
            startActivity(holder.itemView.context, intent, null)
        }

        holder.shareBtn.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Name: ${data.name} URL: ${data.html_url}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}