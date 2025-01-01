package com.observer.youtubesearchapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.observer.youtubesearchapp.R
import com.observer.youtubesearchapp.adapter.SearchResultRecyclerAdapter
import com.observer.youtubesearchapp.adapter.SearchResultRecyclerDecorator
import com.observer.youtubesearchapp.databinding.FragmentSearchResultsBinding
import com.observer.youtubesearchapp.model.SearchResultEntry
import com.observer.youtubesearchapp.viewmodel.ApiCallState
import com.observer.youtubesearchapp.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
    }

    private fun initSearchResults() {
        fragmentBinding.recyclerViewSearchResults.apply {
            layoutManager = LinearLayoutManager(this@SearchResultsFragment.context)
            addItemDecoration(SearchResultRecyclerDecorator(2))
            adapter = searchResultAdapter
        }
        setStateListener()
    }

    private fun setStateListener() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            searchViewModel.apiCallState.collect { state ->
                when (state) {
                    is ApiCallState.CallException -> {
                        Log.v("custom", "Fragment: CallException")
                    }

                    is ApiCallState.Failed -> {
                        Log.v("custom", "Fragment: Failed")
                    }

                    ApiCallState.Pending -> {
                        Log.v("custom", "Fragment: Pending")
                    }

                    is ApiCallState.Success -> {
                        Log.v("custom", "Fragment: Success")
                        updateSearchResults(state.resultList)
                    }
                }
            }
        }
    }


    fun updateSearchResults(resultList: List<SearchResultEntry>) {
        lifecycleScope.launch(Dispatchers.Main) {
            searchResultAdapter.updateSearchResults(resultList)
        }
    }
}
