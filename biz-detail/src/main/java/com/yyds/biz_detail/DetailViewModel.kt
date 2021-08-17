package com.yyds.biz_detail

import android.text.TextUtils
import androidx.lifecycle.*
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.alibaba.android.arouter.BuildConfig
import com.mikeyyds.common.http.ApiFactory


import com.yyds.biz_detail.api.DetailApi
import com.yyds.biz_detail.api.FavoriteApi
import com.yyds.biz_detail.model.DetailModel
import com.yyds.biz_detail.model.Favorite

class DetailViewModel(val goodsId: String?) : ViewModel() {
    // TODO: 2021-08-07  
    companion object {
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    val constructor = modelClass.getConstructor(String::class.java)
                    if (constructor != null) {
                        return constructor.newInstance(goodsId)
                    }
                } catch (e: Exception) {

                }
                return super.create(modelClass)
            }
        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                DetailViewModel::class.java
            )
        }
    }

    fun queryDetailData():LiveData<DetailModel?>{
        val pageData = MutableLiveData<DetailModel?>()
        if (!TextUtils.isEmpty(goodsId)){
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!).enqueue(object:MikeCallback<DetailModel>{
                override fun onSuccess(response: MikeResponse<DetailModel>) {
                    if (response.successful()&&response.data!=null){
                        pageData.postValue(response.data)
                    } else{
                        pageData.postValue(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    pageData.postValue(null)
                    if (BuildConfig.DEBUG){
                        throwable.printStackTrace()
                    }
                }

            })
        }
        return pageData
    }

    fun toggleFavorite():LiveData<Boolean?>{
        val toggleFavoriteData = MutableLiveData<Boolean?>()
        if (!TextUtils.isEmpty(goodsId)){
            ApiFactory.create(FavoriteApi::class.java).favorite(goodsId!!)
                .enqueue(object :MikeCallback<Favorite>{
                    override fun onSuccess(response: MikeResponse<Favorite>) {
                        toggleFavoriteData.postValue(response.data?.isFavorite)
                    }

                    override fun onFailed(throwable: Throwable) {
                        toggleFavoriteData.postValue(null)
                    }

                })
        }
        return toggleFavoriteData
    }
}