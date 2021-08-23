package com.yyds.biz_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikeyyds.common.http.ApiFactory
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.yyds.biz_order.address.Address
import com.yyds.biz_order.address.AddressApi
import com.yyds.biz_order.address.AddressModel

class OrderViewModel:ViewModel() {
    fun queryMainAddress():LiveData<Address?>{
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java).querryAddress(1,1).enqueue(object :
            MikeCallback<AddressModel> {
            override fun onSuccess(response: MikeResponse<AddressModel>) {
                val list = response.data?.list
                val firstElement = if (list?.isNotEmpty()==true) list[0] else null
                liveData.postValue(firstElement)
            }

            override fun onFailed(throwable: Throwable) {
                liveData.postValue(null)
            }

        })
        return liveData
    }
}