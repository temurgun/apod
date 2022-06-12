package com.takorepanov.apod.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.takorepanov.apod.R
import com.takorepanov.apod.data.AstronomyPictureOfTheDayRepository
import com.takorepanov.apod.data.network.RetrofitBuilder
import com.takorepanov.apod.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AstronomyPictureOfTheDayRepository(RetrofitBuilder.apiService)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerAdapter = MainRecyclerAdapter()
        val recyclerView: RecyclerView = binding.mainRecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

        viewModel.pictures.observe(this) {
            recyclerAdapter.setData(it)
        }

        viewModel.isFindMore.observe(this) {
            if (it) {
                binding.mainSearchButton.text = getString(R.string.next)
            } else {
                binding.mainSearchButton.text = getString(R.string.search)
            }
        }

        viewModel.status.observe(this) { status ->
            when (status) {
                Status.LOADING -> {
                    recyclerView.visibility = View.GONE
                    with(binding) {
                        mainSearchField.visibility = View.GONE
                        mainSearchButton.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        mainTextView.visibility = View.GONE
                    }
                }
                Status.SUCCESS -> {
                    recyclerView.visibility = View.VISIBLE
                    with(binding) {
                        mainSearchField.visibility = View.VISIBLE
                        mainSearchButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        mainTextView.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    recyclerView.visibility = View.GONE
                    with(binding) {
                        mainSearchField.visibility = View.GONE
                        mainSearchButton.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        mainTextView.visibility = View.GONE
                    }
                    Snackbar.make(binding.root, "Нет интернет соединения", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }

        with(binding) {
            mainSearchButton.isEnabled = false
            mainSearchField.doAfterTextChanged {
                mainSearchButton.isEnabled = (it?.length ?: 0) >= 3
                viewModel.setQuery(it.toString())
            }
            mainSearchButton.setOnClickListener {
                val result = viewModel.findNext()
                if (result == -1) {
                    Snackbar.make(
                        binding.root,
                        "По запросу \"${viewModel.query}\" ничего не найдено",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    recyclerView.scrollToPosition(result)
                }
            }
        }

    }
}