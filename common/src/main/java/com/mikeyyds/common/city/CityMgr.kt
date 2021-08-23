package com.mikeyyds.common.city

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikeyyds.common.http.ApiFactory
import com.mikeyyds.library.cache.MikeStorage
import com.mikeyyds.library.executor.MikeExecutor
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.ui.cityselector.*
import java.util.concurrent.atomic.AtomicBoolean

object CityMgr {
    private const val KEY_CITY_DATA_SET = "city_data_set"
    private val liveData = MutableLiveData<List<Province>?>()
    private val isFetching = AtomicBoolean(false)
    fun getCityData():LiveData<List<Province>?>{
        if (isFetching.compareAndSet(false,true)&& liveData.value==null){
            getCache(){cache->
                if (cache!=null){
                    liveData.postValue(cache)

                } else{
                    fetchRemote{remote->
                        liveData.postValue(remote)
                    }
                }
                isFetching.compareAndSet(true,false)
            }
        }
        return liveData
    }

    private fun fetchRemote(callback:(List<Province>?)->Unit) {
        ApiFactory.create(CityApi::class.java).listCities().enqueue(object :MikeCallback<CityModel>{
            override fun onSuccess(response: MikeResponse<CityModel>) {
                val list = response.data?.list
                if (response.successful()&&list?.isNullOrEmpty()==false){
                    groupByProvince(list){groupList->
                        saveGroupProvince(groupList)
                        callback(groupList)
                    }
                } else{
                    callback(null)
                }
            }

            override fun onFailed(throwable: Throwable) {

                callback(null)
            }

        })
    }

    private fun saveGroupProvince(groupList: List<Province>?) {
        if (groupList.isNullOrEmpty()) return
        MikeExecutor.execute(runnable = Runnable {
            MikeStorage.saveCache(KEY_CITY_DATA_SET,groupList)
        })
    }

    private fun groupByProvince(list: List<District>,callback: (List<Province>?) -> Unit){
        val proviceMaps = hashMapOf<String,Province>()
        val cityMaps = hashMapOf<String,City>()
        MikeExecutor.execute(runnable = Runnable {
            for (element in list){
                if (TextUtils.isEmpty(element.id)) continue
                when(element.type){
                    TYPE_COUNTRY->{}
                    TYPE_PROVINCE->{
                        val province = Province()
                        District.copyDistrict(element,province)
                        proviceMaps.put(element.id!!,province)

                    }
                    TYPE_CITY->{
                        val city = City()
                        District.copyDistrict(element,city)
                        val province = proviceMaps.get(element.pid)
                        province?.cities?.add(city)
                        cityMaps.put(element.id!!,city)
                    }
                    TYPE_DISTRICT->{
                        val city = cityMaps.get(element.pid)
                        city?.districts?.add(element)
                    }
                }
                callback(ArrayList(proviceMaps.values))
            }
        })
    }

    private fun getCache(callback:(List<Province>?)->Unit){
        MikeExecutor.execute(runnable = Runnable {
            val cache = MikeStorage.getCache<List<Province>?>(KEY_CITY_DATA_SET)
            callback(cache)
        })

    }
}