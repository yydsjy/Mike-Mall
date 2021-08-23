package com.yyds.biz_order.address

import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikeyyds.common.http.ApiFactory
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.library.util.AppGlobals

class AddressViewModel : ViewModel() {

    var checkedAddressItem: AddressItem?= null
    var checkedPostion: Int = -1
    fun saveAddress(
        province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java)
            .addAddress(province, city, area, detail, receiver, phone).enqueue(
                object : MikeCallback<String> {
                    override fun onSuccess(response: MikeResponse<String>) {
                        if (response.successful()) {
                            val address =
                                Address(province, city, area, detail, receiver, phone, "", "")
                            liveData.postValue(address)
                        } else {
                            showToast(response.msg)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        showToast(throwable.message)
                    }

                })
        return liveData
    }

    fun updateAddress(
        id: String,
        province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java)
            .updateAddress(id, province, city, area, detail, receiver, phone).enqueue(
                object : MikeCallback<String> {
                    override fun onSuccess(response: MikeResponse<String>) {
                        if (response.successful()) {
                            val address =
                                Address(province, city, area, detail, receiver, phone, "", "")
                            liveData.postValue(address)
                        } else {
                            showToast(response.msg)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        showToast(throwable.message)
                    }

                })
        return liveData
    }

    fun queryAddressList():LiveData<List<Address>?>{
        val liveData = MutableLiveData<List<Address>?>()
        ApiFactory.create(AddressApi::class.java).querryAddress(1,10).enqueue(object :
            MikeCallback<AddressModel> {
            override fun onSuccess(response: MikeResponse<AddressModel>) {
                if (response.successful()){
                    liveData.postValue(response.data?.list)
                }
            }

            override fun onFailed(throwable: Throwable) {
                liveData.postValue(null)
            }
        })
        return liveData
    }

    private fun showToast(message: String?) {
        if (TextUtils.isEmpty(message)) return
        Toast.makeText(AppGlobals.get()!!, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCleared() {
        checkedAddressItem = null
        checkedPostion = -1
        super.onCleared()
    }

    fun deleteAddress(addressId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        ApiFactory.create(AddressApi::class.java).deleteAddress(addressId)
            .enqueue(object : MikeCallback<String> {
                override fun onSuccess(response: MikeResponse<String>) {
                    if (response.successful()) {
                        liveData.postValue(response.successful())
                    } else {
                        showToast("Delete Failed")
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast("Delete Failed: " + throwable.message)
                }
            })
        return liveData
    }
}