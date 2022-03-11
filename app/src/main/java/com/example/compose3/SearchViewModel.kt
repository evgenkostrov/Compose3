package com.example.compose3

import androidx.lifecycle.ViewModel

class SearchViewModel:ViewModel() {

    fun getResultList(query: String): List<SearchResultModel> {
        return emptyList()
    }


    val searchHistoryList= listOf(
        SearchHistoryModel("nike"),
        SearchHistoryModel("nike"),
        SearchHistoryModel("nike"),
        SearchHistoryModel("nike"),
        SearchHistoryModel("nike"),
    )
}
data class SearchResultModel(
    val result:String
)
