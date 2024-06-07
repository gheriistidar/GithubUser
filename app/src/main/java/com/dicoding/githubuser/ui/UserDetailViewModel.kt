package com.dicoding.githubuser.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.database.FavoriteUser
import com.dicoding.githubuser.data.database.FavoriteUserDatabase
import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.data.repository.FavoriteUserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<DetailUserResponse?>()
    val user: LiveData<DetailUserResponse?> get() = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val repository: FavoriteUserRepository

    companion object {
        private const val TAG = "UserDetailViewModel"
    }

    fun getUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }


            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    init {
        val favoriteUserDao = FavoriteUserDatabase.getDatabase(application).favoriteUserDao()
        repository = FavoriteUserRepository(favoriteUserDao)
    }

    fun insert(favoriteUser: FavoriteUser) = viewModelScope.launch {
        repository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) = viewModelScope.launch {
        repository.delete(favoriteUser)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return repository.getFavoriteUserByUsername(username)
    }
}
