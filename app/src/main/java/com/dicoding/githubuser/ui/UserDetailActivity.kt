package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.database.FavoriteUser
import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel by viewModels<UserDetailViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: ""
        setupViewPager(username)
        observeViewModel(username)
    }

    private fun setupViewPager(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun observeViewModel(username: String) {
        if (viewModel.user.value == null) {
            showLoading(true)
            viewModel.getUser(username)
        }

        viewModel.user.observe(this) { user ->
            if (user != null) {
                updateUI(user)
                showLoading(false)
                val avatarUrl = user.avatarUrl

                viewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
                    if (favoriteUser != null) {
                        binding.fab.setImageResource(R.drawable.baseline_favorite_24)
                        binding.fab.setOnClickListener {
                            viewModel.delete(favoriteUser)
                            Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        binding.fab.setImageResource(R.drawable.baseline_favorite_border_24)
                        binding.fab.setOnClickListener {
                            val user = FavoriteUser(username = username, avatarUrl = avatarUrl)
                            viewModel.insert(user)
                            Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun updateUI(user: DetailUserResponse) {
        with(binding) {
            Glide.with(this@UserDetailActivity).load(user.avatarUrl).into(avatar)
            name.text = user.name
            username.text = user.login
            followers.text = getString(R.string.followers_count, user.followers)
            following.text = getString(R.string.following_count, user.following)

            share.setOnClickListener {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Coba cek Profil GitHub ini @${user.login} \n ${user.htmlUrl}")
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
