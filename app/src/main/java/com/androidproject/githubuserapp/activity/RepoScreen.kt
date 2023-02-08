package com.androidproject.githubuserapp.activity

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.githubuserapp.adapter.SearchRepoAdapter
import com.androidproject.githubuserapp.api.RetrofitInstance
import com.androidproject.githubuserapp.databinding.ActivityRepoScreenBinding
import com.androidproject.githubuserapp.model.RepoGithub
import kotlinx.coroutines.*

class RepoScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRepoScreenBinding
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var repoAdapter: SearchRepoAdapter
    private lateinit var repoList: MutableList<RepoGithub>
    private var isLoading = false
    var pageNumber: Int = 1
    private lateinit var queryKeyword: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize
        recyclerView = binding.recyclerViewRepoScreen
        repoList = mutableListOf()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        queryKeyword = binding.repoName.text.toString()
        repoAdapter = SearchRepoAdapter(this, repoList)
        recyclerView.adapter = repoAdapter

        //layout
        binding.repoScreenProgressBar.visibility = View.INVISIBLE


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

            if (isNetworkConnected()) {
                retrieveRepositories()
            } else {
                AlertDialog.Builder(this).setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again")
                    .setPositiveButton(R.string.ok) { _, _ -> }
                    .setIcon(R.drawable.ic_dialog_alert).show()
            }

        }
        setRecyclerViewScrollListener()

    }
    private fun retrieveRepositories() {
        binding.repoScreenProgressBar.visibility = View.VISIBLE
        //Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //shared preferences
        val prefs = getSharedPreferences("SharePref", Context.MODE_PRIVATE)
        val prefQueryKeyword = prefs.getString("queryKw", queryKeyword)
        val prefPageNumber = prefs.getInt("pageNr", pageNumber)

        //Handle exceptions if any
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            AlertDialog.Builder(this).setTitle("Error")
                .setMessage(exception.message)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .setIcon(R.drawable.ic_dialog_alert).show()
        }

        //get data from response and  add to list
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            val response = RetrofitInstance().getRepositories(prefQueryKeyword.toString(), prefPageNumber, 30)
            response.observe(this@RepoScreen){response->
                response.items.let {
                    for(item in it) {
                        repoList.add(item)
                    }
                }

            }

            //set and update dataset
            repoAdapter = SearchRepoAdapter(this@RepoScreen, repoList)
            recyclerView.layoutManager = layoutManager
            repoAdapter.notifyDataSetChanged()
            if (repoAdapter.itemCount == 0) {
                Toast.makeText(
                    this@RepoScreen, "No repositories found for the entered Keyword",
                    Toast.LENGTH_SHORT
                ).show()
                binding.repoScreenProgressBar.visibility = View.GONE
            }
            binding.repoScreenProgressBar.visibility = View.GONE
        }
        binding.repoName.text.clear()
        isLoading = false

    }

    private fun isNetworkConnected(): Boolean {

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork

        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    val prefs = getSharedPreferences("Share", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = prefs.edit()
                    val oldPageNumber = prefs.getInt("pageNr", pageNumber)
                    pageNumber = oldPageNumber + 1
                    editor.putInt("pageNr", pageNumber)
                    binding.repoScreenProgressBar.visibility = View.VISIBLE
                    retrieveRepositories()
                    //get State
                    val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
                    // Restore state
                    recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)

                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)

    }

}