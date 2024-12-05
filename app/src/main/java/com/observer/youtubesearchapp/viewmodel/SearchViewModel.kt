package com.observer.youtubesearchapp.viewmodel

import androidx.lifecycle.ViewModel
import com.observer.youtubesearchapp.model.SearchResultEntry
import com.observer.youtubesearchapp.model.VideoStatus

class SearchViewModel : ViewModel() {


    fun getDummyData(): Array<SearchResultEntry> = arrayOf(
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
