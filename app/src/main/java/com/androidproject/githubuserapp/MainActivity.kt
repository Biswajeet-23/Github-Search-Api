package com.androidproject.githubuserapp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.githubuserapp.activity.RepoScreen
import com.androidproject.githubuserapp.adapter.FavRepoAdapter
import com.androidproject.githubuserapp.adapter.SearchRepoAdapter
import com.androidproject.githubuserapp.api.GithubApiInterface
import com.androidproject.githubuserapp.api.RetrofitInstance
import com.androidproject.githubuserapp.databinding.ActivityMainBinding
import com.androidproject.githubuserapp.model.GithubResponse
import com.androidproject.githubuserapp.model.RepoGithub
import com.androidproject.githubuserapp.roomDB.AppDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preference = getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isFav", false)
        editor.apply()

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView = binding.repoRecyclerView

        val dao = AppDatabase.getInstance(this).productDao()



        dao.getAllProducts().observe(this){

            recyclerView.adapter = FavRepoAdapter(this, it)
            recyclerView.layoutManager = layoutManager
        }

        //search repo
        val addRepo = binding.fab
        addRepo.setOnClickListener{
            val intent = Intent(this, RepoScreen::class.java)
            startActivity(intent)
        }

    }

}


