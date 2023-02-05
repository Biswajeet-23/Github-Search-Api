package com.androidproject.githubuserapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.githubuserapp.activity.DetailsActivity
import com.androidproject.githubuserapp.databinding.RepoCardViewBinding
import com.androidproject.githubuserapp.roomDB.AppDatabase
import com.androidproject.githubuserapp.roomDB.FavRepoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavRepoAdapter(val context : Context, val list: List<FavRepoModel>) :
    RecyclerView.Adapter<FavRepoAdapter.FavViewHolder>(){

    inner class FavViewHolder(val binding: RepoCardViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

        val shareBtn: ImageView = binding.share

        fun bindData(data: FavRepoModel) {
            binding.favRepoName.text = data.name
            binding.favRepoDesc.text = data.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = RepoCardViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {

        val data = list[position]

        holder.bindData(list[position])

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailsActivity::class.java)
                .apply {
                    putExtra("title", data.name)
                    putExtra("url", data.html_url)
                    putExtra("description", data.description)
                    putExtra("id", data.id.toString())
                    putExtra("avatar", data.avatar_url)
                    putExtra("stargazers", data.stargazers_count.toString())
                    putExtra("watchers", data.watchers_count.toString())
                    putExtra("forks", data.forks_count.toString())
                    putExtra("subscribers", data.subscribers_count.toString())
                    putExtra("owner", data.owner)
                }
            context.startActivity(intent)
        }

        //share
        holder.shareBtn.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Name: ${data.name} URL: ${data.html_url}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }

        val dao = AppDatabase.getInstance(context).productDao()
        holder.binding.delete.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(
                    FavRepoModel(
                        list[position].id,
                        list[position].name,
                        list[position].html_url,
                        list[position].description,
                        list[position].stargazers_count,
                        list[position].watchers_count,
                        list[position].forks_count,
                        list[position].subscribers_count,
                        list[position].avatar_url,
                        list[position].owner
                    )
                )
            }
        }
    }
}