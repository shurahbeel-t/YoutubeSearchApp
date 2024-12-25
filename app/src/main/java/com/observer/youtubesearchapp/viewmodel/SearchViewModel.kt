package com.observer.youtubesearchapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.observer.youtubesearchapp.api.service.YoutubeApi
import com.observer.youtubesearchapp.model.SearchResultEntry
import com.observer.youtubesearchapp.model.VideoStatus
import com.observer.youtubesearchapp.secrets.appSha1SignatureLowerCase
import com.observer.youtubesearchapp.secrets.getSHA1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ApiCallState {
    object Pending : ApiCallState
    object Success : ApiCallState
    data class Failed(val responseBody: okhttp3.ResponseBody?) : ApiCallState
    data class CallException(val e: Exception) : ApiCallState
}

class SearchViewModel : ViewModel() {

    private var _apiCallState = MutableStateFlow<ApiCallState>(ApiCallState.Pending)
    val apiCallState: StateFlow<ApiCallState> = _apiCallState

    fun getDummyData(query: String): List<SearchResultEntry> {
        return listOf(
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

    fun searchYoutube(appContext: Context, query: String): List<SearchResultEntry> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = YoutubeApi.retrofitService.getSearchResponseVideos(appContext.packageName, getSHA1(appContext), query, 10)
                if (response.isSuccessful) {
                    _apiCallState.value = ApiCallState.Success
                } else {
                    _apiCallState.value = ApiCallState.Failed(response.errorBody())
                }
            } catch (e: Exception) {
                _apiCallState.value = ApiCallState.CallException(e)
            }
        }
        return listOf()
    }
}
