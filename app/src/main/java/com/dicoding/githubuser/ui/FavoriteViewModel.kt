package com.dicoding.githubuser.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubuser.data.database.FavoriteUser
import com.dicoding.githubuser.data.database.FavoriteUserDatabase
import com.dicoding.githubuser.data.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteUserRepository
    private val favoriteUsers: LiveData<List<FavoriteUser>>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        val favoriteUserDao = FavoriteUserDatabase.getDatabase(application).favoriteUserDao()
        repository = FavoriteUserRepository(favoriteUserDao)
        favoriteUsers = repository.getFavoriteUsers()
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> {
        _isLoading.value = false
        return favoriteUsers
    }
}
