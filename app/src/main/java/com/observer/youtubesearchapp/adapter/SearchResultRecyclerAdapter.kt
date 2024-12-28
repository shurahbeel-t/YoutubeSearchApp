package com.observer.youtubesearchapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.observer.youtubesearchapp.databinding.LayoutSearchResultEntryBinding
import com.observer.youtubesearchapp.model.SearchResultEntry
import com.observer.youtubesearchapp.model.VideoStatus

class SearchResultRecyclerAdapter(var searchResultsList: ArrayList<SearchResultEntry>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    fun updateSearchResults(newResultsList: List<SearchResultEntry>) {
        searchResultsList.clear()
        searchResultsList.addAll(newResultsList)
    }

//    private var searchResultsList: MutableList<SearchResultEntry> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val entryBinding = LayoutSearchResultEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(entryBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchResultViewHolder -> holder.bind(searchResultsList[position])
        }
    }

    override fun getItemCount(): Int = searchResultsList.size


    class SearchResultViewHolder(private val entryBinding: LayoutSearchResultEntryBinding) : RecyclerView.ViewHolder(entryBinding.root) {
        fun bind(searchResult: SearchResultEntry) {
            entryBinding.apply {
                tvTitle.text = searchResult.title
                when (searchResult.status) {
                    VideoStatus.Live -> {
                        tvTimeOrCount.text = "live"
                    }

                    VideoStatus.Uploaded -> {
                        tvTimeOrCount.text = searchResult.timeOrCount
                    }
                }
                tvDate.text = searchResult.date
                ivThumbnail.loadImage(searchResult.imgUrl)
            }
        }
    }
}