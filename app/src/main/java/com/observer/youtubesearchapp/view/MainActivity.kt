package com.observer.youtubesearchapp.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
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
import com.observer.youtubesearchapp.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            delay(1000L)

            binding.apply {
                val duration: Long = 750L
                title.animate().setDuration(duration).alpha(0f)
                searchBox.animate().setDuration(duration).alpha(0f)
                searchButton.animate().setDuration(duration).alpha(0f)
            }

            delay(750L)
            /*// val screenWidthPx = displayMetrics.widthPixels
            // val screenHeightPx = displayMetrics.heightPixels
            // binding.searchFragment.translationX = screenWidthPx * 1.0f*/
            displayMetrics = resources.displayMetrics

            binding.apply {
                title.visibility = View.GONE
                searchBox.visibility = View.GONE
                searchButton.visibility = View.GONE
            }

            binding.searchFragmentCv.apply {
                translationY = displayMetrics.heightPixels * 1.0f
                visibility = View.VISIBLE
                animate()
                    // .translationX(0f)
                    .translationY(0f)
                    .setDuration(1000L)
            }
        }
    }
}