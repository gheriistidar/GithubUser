package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UsersAdapter
    private lateinit var viewModel: FollowViewModel

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModel()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.adapter = adapter
        binding.rvUser.addItemDecoration(
            DividerItemDecoration(
                requireActivity(), DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[FollowViewModel::class.java]
    }

    private fun observeViewModel() {
        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME)

            if (username != null) {
                viewModel.getFollow(username, position)
            }
        }

        viewModel.follow.observe(viewLifecycleOwner) { follow ->
            adapter.submitList(follow)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
