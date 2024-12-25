package com.observer.youtubesearchapp.viewmodel

import android.content.Context
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModel
import com.observer.youtubesearchapp.api.model.SearchListResponse
import com.observer.youtubesearchapp.api.service.YoutubeApi
import com.observer.youtubesearchapp.model.SearchResultEntry
import com.observer.youtubesearchapp.model.VideoStatus
import com.observer.youtubesearchapp.secrets.getSHA1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

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
                VideoStatus.Uploaded,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video02",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Hamilton_2.jpg",
                VideoStatus.Uploaded,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video03",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Hamilton_3.jpg",
                VideoStatus.Live,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video04",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Rosberg_1.jpg",
                VideoStatus.Uploaded,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video05",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Rosberg_2.jpg",
                VideoStatus.Uploaded,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video06",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Vettel_1.jpg",
                VideoStatus.Uploaded,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video07",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Vettel_2.jpg",
                VideoStatus.Uploaded,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video08",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/ign-0008.png",
                VideoStatus.Live,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video09",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Button_1.jpg",
                VideoStatus.Uploaded,
                "time",
                "date"
            ),
            SearchResultEntry(
                "Video10",
                "https://raw.githubusercontent.com/linuxdotexe/nordic-wallpapers/master/wallpapers/FormulaOne_Button_2.jpg",
                VideoStatus.Uploaded,
                "time",
                "date"
            )
        )
    }

    suspend fun searchYoutube(appContext: Context, query: String): List<SearchResultEntry> {
        return withContext(Dispatchers.IO) {
            try {
                val response = YoutubeApi.retrofitService.getSearchResponseVideos(appContext.packageName, getSHA1(appContext) ?: "null", query, 10)
                if (response.isSuccessful) {
                    _apiCallState.value = ApiCallState.Success
                    transformToSearchResultEntries(appContext, response.body())
                } else {
                    _apiCallState.value = ApiCallState.Failed(response.errorBody())
                    listOf()
                }
            } catch (e: Exception) {
                _apiCallState.value = ApiCallState.CallException(e)
                listOf()
            }
        }
    }

    private fun transformToSearchResultEntries(appContext: Context, listResponse: SearchListResponse?): List<SearchResultEntry> {
        val returnList = mutableListOf<SearchResultEntry>()
        listResponse?.let { response ->
            val dpi = getScreenDpi(appContext)
            lateinit var quality: ImageQuality
            when {
                dpi <= DisplayMetrics.DENSITY_MEDIUM -> {
                    // Low to Medium DPI
                    quality = ImageQuality.Default
                }

                dpi in (DisplayMetrics.DENSITY_MEDIUM + 1)..DisplayMetrics.DENSITY_XHIGH -> {
                    // High DPI
                    quality = ImageQuality.Medium
                }

                else -> {
                    // Extra High DPI and above
                    quality = ImageQuality.High
                }
            }
            response.items.forEach { searchResult ->
                searchResult.snippet.apply {
                    val imageUrl: String = when (quality) {
                        ImageQuality.Default -> thumbnails.default.url
                        ImageQuality.High -> thumbnails.high.url
                        ImageQuality.Medium -> thumbnails.medium.url
                    }
                    val videoStatus: VideoStatus = when (liveBroadcastContent) {
                        "live" -> VideoStatus.Live
                        else -> VideoStatus.Uploaded
                    }
                    returnList.add(SearchResultEntry(title, imageUrl, videoStatus, "placeholder", publishedAt))
                }
            }
        }
        return returnList
    }

    sealed interface ImageQuality {
        object Default : ImageQuality
        object Medium : ImageQuality
        object High : ImageQuality
    }

    private fun getScreenDpi(context: Context): Int = context.resources.displayMetrics.densityDpi
}
