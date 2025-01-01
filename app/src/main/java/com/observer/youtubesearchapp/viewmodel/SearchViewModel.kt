package com.observer.youtubesearchapp.viewmodel

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.observer.youtubesearchapp.api.model.SearchListResponse
import com.observer.youtubesearchapp.api.service.YoutubeApi
import com.observer.youtubesearchapp.model.SearchResultEntry
import com.observer.youtubesearchapp.model.VideoStatus
import com.observer.youtubesearchapp.secrets.getSHA1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

sealed interface ApiCallState {
    object Pending : ApiCallState
    data class Success(val resultList: List<SearchResultEntry>) : ApiCallState
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

    suspend fun searchYoutube(appContext: Context, query: String) {
        return withContext(Dispatchers.IO) {
            try {
                val response = YoutubeApi.retrofitService.getSearchResponseVideos(appContext.packageName, getSHA1(appContext) ?: "null", query, 10)
                if (response.isSuccessful) {
                    val resultList: List<SearchResultEntry> = transformToSearchResultEntries(appContext, response.body())
                    Log.v("custom", "got ${resultList.size} results")
                    _apiCallState.value = ApiCallState.Success(resultList)
                    launch {
                        delay(250)
                        _apiCallState.value = ApiCallState.Pending
                    }
                } else {
                    _apiCallState.value = ApiCallState.Failed(response.errorBody())
                }
            } catch (e: Exception) {
                _apiCallState.value = ApiCallState.CallException(e)
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
                    returnList.add(SearchResultEntry(title, imageUrl, videoStatus, "placeholder", convertIsoToCustomDateFormat(publishedAt)))
                }
            }
        }
        return returnList
    }

    fun convertIsoToCustomDateFormat(isoDateTime: String): String {
        /*return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            convertToCustomV26(isoDateTime)
        } else {
            convertToCustomV22(isoDateTime)
        }*/

        return convertToCustomV22(isoDateTime)
    }

    private fun convertToCustomV22(isoDateTime: String): String {
        return try {
            // Define the ISO 8601 date format
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC") // Ensure the correct timezone

            // Parse the ISO date string into a Date object
            val date = isoFormatter.parse(isoDateTime)

            // Define the target date format
            val customFormatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

            // Format the Date object into the desired string format
            customFormatter.format(date!!)
        } catch (e: Exception) {
            Log.e("custom", "Exception at V22 date formatter:\n${e.message}")
            "Invalid date format"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToCustomV26(isoDateTime: String): String {
        return try {
            // Parse the ISO 8601 string to a LocalDate
            val parsedDate = LocalDate.parse(isoDateTime)
            // Format the LocalDate to the desired format
            val customFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
            parsedDate.format(customFormatter)
        } catch (e: Exception) {
            Log.e("custom", "Exception at V26 date formatter:\n${e.message}")
            "Invalid date format"
        }
    }

    sealed interface ImageQuality {
        object Default : ImageQuality
        object Medium : ImageQuality
        object High : ImageQuality
    }

    private fun getScreenDpi(context: Context): Int = context.resources.displayMetrics.densityDpi
}
