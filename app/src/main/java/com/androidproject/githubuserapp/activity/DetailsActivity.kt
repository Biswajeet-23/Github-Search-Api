package com.androidproject.githubuserapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.androidproject.githubuserapp.MainActivity
import com.androidproject.githubuserapp.databinding.ActivityDetailsBinding
import com.androidproject.githubuserapp.roomDB.AppDatabase
import com.androidproject.githubuserapp.roomDB.FavRepoDao
import com.androidproject.githubuserapp.roomDB.FavRepoModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)


        //creating the reference for image view
        val avatarImage: ImageView = binding.avatarImg
        //Extracting values from intent
        val repoName = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        val description = intent.getStringExtra("description")
        val repoId: Int? = intent.getStringExtra("id")?.toInt()
        val avatar = intent.getStringExtra("avatar")
        val starCount: Int? = intent.getStringExtra("stargazers")?.toInt()
        val watchCount: Int? = intent.getStringExtra("watchers")?.toInt()
        val forkCount: Int? = intent.getStringExtra("forks")?.toInt()
        val subscriberCount: Int? = intent.getStringExtra("subscribers")?.toInt()
        val ownerName = intent.getStringExtra("owner")
//        val issueCount: Int? = intent.getStringExtra("open_issues")?.toInt()
//        val networkCount: Int? = intent.getStringExtra("network")?.toInt()

        //initialising Texts in the UI
        binding.tvTitle.text = "$repoName"
        binding.tvDescription.text = "$description"
        binding.tvURL.text = url
//        binding.tvId.text = "Id: $repoId"
        binding.tvFork.text = "$forkCount"
        binding.tvWatch.text = "$watchCount"
        binding.tvStar.text = "$starCount"
        binding.tvSubs.text = "$subscriberCount"
        binding.owner.text = "$ownerName"
        //adding the avatar image using glide
        Glide.with(this)
            .load(avatar)
            .circleCrop()
            .into(avatarImage)

        binding.tvURL.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            ContextCompat.startActivity(this, intent, null)
        }

        //For creating instance of database and setting the add button
        favouriteAction(repoId, repoName, url, description, starCount, watchCount, forkCount, subscriberCount, avatar, ownerName)

        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun favouriteAction(
        repoId: Int?,
        repoName: String?,
        url: String?,
        description: String?,
        starCount: Int?,
        watchCount: Int?,
        forkCount: Int?,
        subscriberCount: Int?,
        avatar: String?,
        ownerName: String?
    ) {
        val favDao = AppDatabase.getInstance(this).productDao()

        if(favDao.isExist(repoId)!=null){
            binding.addRepo.text = "Go To Repo"
        }else{
            binding.addRepo.text = "Add To Repo"
        }

        binding.addRepo.setOnClickListener  {
            if (favDao.isExist(repoId) != null) {
                openRepoList()
            } else {
                addToRepoList(favDao, repoId, repoName, description, url, starCount, watchCount, forkCount, subscriberCount, avatar, ownerName)
            }
        }
    }

    private fun addToRepoList(
        favDao: FavRepoDao,
        id: Int?,
        name: String?,
        description: String?,
        html_url: String?,
        stargazers_count: Int?,
        watchers_count: Int?,
        forks_count: Int?,
        subscribers_count: Int?,
        avatar_url: String?,
        owner: String?
    ) {
        val data = FavRepoModel(id, name, html_url, description,
                                stargazers_count, watchers_count,
                                forks_count, subscribers_count,
                                avatar_url, owner)
        lifecycleScope.launch(Dispatchers.IO){
            favDao.insertProduct(data)
            binding.addRepo.text = "Go To Repo"
        }
    }

    private fun openRepoList() {
        val preference = this.getSharedPreferences("Info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isFav", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }


}