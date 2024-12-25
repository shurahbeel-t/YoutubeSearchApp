package com.observer.youtubesearchapp.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.observer.youtubesearchapp.R
import com.observer.youtubesearchapp.databinding.ActivityMainBinding
import com.observer.youtubesearchapp.viewmodel.ApiCallState
import com.observer.youtubesearchapp.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var displayMetrics: DisplayMetrics
    lateinit var searchFragment: Fragment
    val searchViewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        searchFragment = SearchResultsFragment()
        if (savedInstanceState == null) {
            // val fragmentBundle = bundleOf("viewModel" to searchViewModel)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.search_fragment_cv, searchFragment)
                // add(R.id.search_fragment_cv, (SearchResultsFragment::class.java), fragmentBundle)
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycleScope.launch(Dispatchers.Main) {
            // separating the child coroutine so the search method call does not block the collect method or vice-versa
            launch {
                val results = searchViewModel.searchYoutube(applicationContext, "Lofi Girl")
                if(results.isNotEmpty()){
                    // Do the magic
                }
            }
            searchViewModel.apiCallState.collect { state ->
                when (state) {
                    is ApiCallState.CallException -> {
                        Log.e("custom", "Request exception: ${state.e.message}", state.e)
                    }

                    is ApiCallState.Failed -> {
                        val errorBody = state.responseBody?.string()
                        Log.e("custom", "Api call Failed:\n${errorBody}")
                    }

                    ApiCallState.Pending -> Log.v("custom", "Api call Pending")
                    ApiCallState.Success -> Log.v("custom", "Api call Success")
                }
            }
        }
        // I AM SO PRO >:)
    }
}