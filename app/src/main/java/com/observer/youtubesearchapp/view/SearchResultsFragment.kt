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
import com.observer.youtubesearchapp.model.SearchResultEntry
import com.observer.youtubesearchapp.model.VideoStatus
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
        searchResultAdapter.updateSearchResults(searchViewModel.searchYoutube("searchQuery"))
    }

    fun getDummyData(): ArrayList<SearchResultEntry> = arrayListOf(
        SearchResultEntry(
            "Video01",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Hamilton_1.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video02",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Hamilton_2.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video03",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Hamilton_3.jpg",
            VideoStatus.LIVE,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video04",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Rosberg_1.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video05",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Rosberg_2.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video06",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Vettel_1.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video07",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Vettel_2.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video08",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/ign-0008.png",
            VideoStatus.LIVE,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video09",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Button_1.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        ),
        SearchResultEntry(
            "Video10",
            "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Button_2.jpg",
            VideoStatus.UPLOADED,
            "time",
            "date"
        )
    )
}
