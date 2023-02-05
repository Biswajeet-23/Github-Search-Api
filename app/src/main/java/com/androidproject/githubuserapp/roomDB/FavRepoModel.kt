package com.androidproject.githubuserapp.roomDB

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidproject.githubuserapp.model.Owner

@Entity(tableName = "repos")
data class FavRepoModel(
    @PrimaryKey
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "html_url")
    val html_url: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "stargazers_count")
    val stargazers_count: Int?,

    @ColumnInfo(name = "watchers_count")
    val watchers_count: Int?,

    @ColumnInfo(name = "forks_count")
    val forks_count: Int?,

    @ColumnInfo(name = "subscribers_count")
    val subscribers_count: Int?,

    @ColumnInfo(name = "avatar_url")
    val avatar_url: String?,

    @ColumnInfo(name = "owner")
    val owner: String?
)

