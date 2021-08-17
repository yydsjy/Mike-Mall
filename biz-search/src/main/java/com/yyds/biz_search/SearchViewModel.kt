package com.yyds.biz_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikeyyds.common.http.ApiFactory
import com.mikeyyds.library.cache.MikeStorage
import com.mikeyyds.library.executor.MikeExecutor
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.yyds.pub_mod.model.GoodsModel

class SearchViewModel : ViewModel() {
    var keywords: java.util.ArrayList<Keyword>?=null
    var pageIndex = 1

    // TODO: 2021-08-16  
    var goodsSearchListliveData = MutableLiveData<List<GoodsModel>?>()

    companion object {
        const val PAGE_INIT_INDEX: Int = 1
        const val PAGE_SIZE = 10

        const val KEY_SEARCH_HISTORY = "search_history"
        const val MAX_HISTORY_SIZE = 10
    }

    fun quickSearch(key: String): MutableLiveData<List<Keyword>?> {
        val liveData = MutableLiveData<List<Keyword>?>()
        ApiFactory.create(SearchApi::class.java).quickSearch(key)
            .enqueue(object : MikeCallback<QuickSearchList> {
                override fun onSuccess(response: MikeResponse<QuickSearchList>) {
                    liveData.postValue(response.data?.list)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }

            })
        return liveData
    }

    fun goodsSearch(keyword: String, loadInit: Boolean) {
        if (loadInit) pageIndex = PAGE_INIT_INDEX
        ApiFactory.create(SearchApi::class.java).goodsSearch(keyword, pageIndex, PAGE_SIZE)
            .enqueue(object : MikeCallback<GoodsSearchList> {
                override fun onSuccess(response: MikeResponse<GoodsSearchList>) {
                    goodsSearchListliveData.postValue(response.data?.list)
                    pageIndex++
                }

                override fun onFailed(throwable: Throwable) {
                    goodsSearchListliveData.postValue(null)
                }

            })
    }

    fun saveHistory(keyword: Keyword) {
        if (keywords==null){
            keywords = ArrayList()
        }
        keywords?.apply {
            if (contains(keyword)){
                remove(keyword)
            }
            add(0,keyword)
            if (this.size> MAX_HISTORY_SIZE){
                dropLast(this.size- MAX_HISTORY_SIZE)
            }

            MikeExecutor.execute(runnable = Runnable {
                MikeStorage.saveCache(KEY_SEARCH_HISTORY,keywords)
            })
        }
    }

    fun queryLocalHistory():LiveData<ArrayList<Keyword>> {
        val liveData = MutableLiveData<ArrayList<Keyword>>()
        MikeExecutor.execute(runnable = Runnable {
            keywords = MikeStorage.getCache<ArrayList<Keyword>>(KEY_SEARCH_HISTORY)
            liveData.postValue(keywords)
        })
        return liveData
    }

    fun clearHistory(){
        MikeExecutor.execute(runnable = Runnable {
            MikeStorage.deleteCache(KEY_SEARCH_HISTORY)
        })
        keywords?.clear()
    }
}