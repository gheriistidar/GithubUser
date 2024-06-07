package com.dicoding.githubuser.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.database.FavoriteUser
import com.dicoding.githubuser.data.database.FavoriteUserDao

class FavoriteUserRepository(private val favoriteUserDao: FavoriteUserDao) {

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteUserDao.getFavoriteUserByUsername(username)
    }

    suspend fun insert(favoriteUser: FavoriteUser) {
        favoriteUserDao.insert(favoriteUser)
    }

    suspend fun delete(favoriteUser: FavoriteUser) {
        favoriteUserDao.delete(favoriteUser)
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getFavoriteUsers()
    }
}
