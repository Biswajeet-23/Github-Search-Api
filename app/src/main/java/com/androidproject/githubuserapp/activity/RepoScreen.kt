package com.androidproject.githubuserapp.activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.githubuserapp.adapter.SearchRepoAdapter
import com.androidproject.githubuserapp.databinding.ActivityRepoScreenBinding
import com.androidproject.githubuserapp.model.RepoGithub
import com.androidproject.githubuserapp.service.RepoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RepoScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRepoScreenBinding
    private lateinit var repoList: MutableList<RepoGithub>
    private lateinit var repoAdapter: SearchRepoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    var pageNumber: Int = 1
    private lateinit var queryKeyword: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //list to store repositories
        repoList = mutableListOf()

        //layout
        binding.repoScreenProgressBar.visibility = View.INVISIBLE
        repoAdapter = SearchRepoAdapter(this, repoList)
        recyclerView = binding.recyclerViewRepoScreen
        recyclerView.adapter = repoAdapter

        //Event Handling
        binding.searchBtn.setOnClickListener {

                repoList.clear()
                repoAdapter.notifyDataSetChanged()
                layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                queryKeyword = binding.repoName.text.toString()

                val prefs = getSharedPreferences("Share", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = prefs.edit()
                editor.putString("queryKw", queryKeyword)
                editor.putInt("pageNr", pageNumber)
                editor.apply()
                getRepositories()

        }

        setRecyclerViewScrollListener()
    }

    private fun getRepositories() {
        binding.repoScreenProgressBar.visibility = View.INVISIBLE

        //shared preferences
        val prefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val prefQueryKeyword = prefs.getString("queryKw", queryKeyword)
        val prefPageNumber = prefs.getInt("pageNr", pageNumber)

        // getting data from response and add to the list
        val response = RepoService().getRepository(prefQueryKeyword.toString(), prefPageNumber, 30)
        response.observe(this) { response ->
            response.items.let {
                for (item in it) {
                    repoList.add(item)
                }
            }
            //set and update dataset
            repoAdapter = SearchRepoAdapter(this, repoList)
            recyclerView.layoutManager = layoutManager
            repoAdapter.notifyDataSetChanged()

            if (repoAdapter.itemCount == 0) {
                Toast.makeText(
                    this,
                    "No repositories found for the entered Keyword",
                    Toast.LENGTH_SHORT
                ).show()
                binding.repoScreenProgressBar.visibility = View.GONE
            }
        }
        binding.repoName.text.clear()
    }

    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1)){
                    val prefs = getSharedPreferences("Share", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = prefs.edit()
                    val oldPageNumber = prefs.getInt("pageNr", pageNumber)
                    pageNumber = oldPageNumber + 1
                    editor.putInt("pageNr", pageNumber)
                    binding.repoScreenProgressBar.visibility = View.INVISIBLE
                    getRepositories()

                    //get State
                    val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

                    //Restore state
                    recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)

                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

}