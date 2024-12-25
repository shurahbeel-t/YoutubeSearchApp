package com.observer.youtubesearchapp.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.observer.youtubesearchapp.R
import com.observer.youtubesearchapp.adapter.SearchResultRecyclerAdapter
import com.observer.youtubesearchapp.adapter.SearchResultRecyclerDecorator
import com.observer.youtubesearchapp.databinding.FragmentSearchResultsBinding
import com.observer.youtubesearchapp.viewmodel.SearchViewModel


class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {
    lateinit var fragmentBinding: FragmentSearchResultsBinding
    private val searchResultAdapter: SearchResultRecyclerAdapter = SearchResultRecyclerAdapter(arrayListOf())
    val searchViewModel: SearchViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // instead of this here
        // fragmentBinding = FragmentSearchResultsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // why this here?
        fragmentBinding = FragmentSearchResultsBinding.bind(view)
        initSearchResults()
        updateSearchResults()
    }

    private fun initSearchResults() {
        fragmentBinding.recyclerViewSearchResults.apply {
            layoutManager = LinearLayoutManager(this@SearchResultsFragment.context)
            addItemDecoration(SearchResultRecyclerDecorator(2))
            adapter = searchResultAdapter
        }
    }

    private fun updateSearchResults() {
        searchResultAdapter.updateSearchResults(searchViewModel.getDummyData("searchQuery"))
    }
}
