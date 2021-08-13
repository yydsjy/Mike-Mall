package com.mikeyyds.mall.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.library.restful.annotation.CacheStrategy
import com.mikeyyds.mall.http.ApiFactory
import com.mikeyyds.mall.http.api.HomeApi
import com.mikeyyds.mall.model.HomeModel
import com.mikeyyds.mall.model.TabCategory

class HomeViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    fun queryCategoryTabs(): LiveData<List<TabCategory>?> {
        val liveData = MutableLiveData<List<TabCategory>?>()
        val memCache = savedState.get<List<TabCategory>?>("categoryTabs")
        if (memCache != null) {
            liveData.postValue(memCache)
            return liveData
        }
        ApiFactory.create(HomeApi::class.java).querryTabList()
            .enqueue(object : MikeCallback<List<TabCategory>> {
                override fun onSuccess(response: MikeResponse<List<TabCategory>>) {
                    if (response.successful() && response.data != null) {
                        liveData.postValue(response.data)
                        savedState.set("categoryTabs", response.data)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                }

            })
        return liveData
    }

    fun queryTabCategoryList(
        categoryId: String?,
        pageIndex: Int,
        cacheStrategy: Int
    ): LiveData<HomeModel?> {
        val liveData = MutableLiveData<HomeModel?>()
        val memCache = savedState.get<HomeModel>("categoryList")
        if (memCache != null && pageIndex == 1 && cacheStrategy == CacheStrategy.CACHE_FIRST) {
            liveData.postValue(memCache)
            return liveData
        }

        ApiFactory.create(HomeApi::class.java)
            .querryTabCategoryList(cacheStrategy, categoryId!!, pageIndex, 10)
            .enqueue(object : MikeCallback<HomeModel> {
                override fun onSuccess(response: MikeResponse<HomeModel>) {
                    if (response.successful() && response.data != null) {
                        liveData.postValue(response.data)
                        savedState.set("categoryList", response.data)
                    } else {
                        liveData.postValue(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }

            })
        return liveData
    }
}