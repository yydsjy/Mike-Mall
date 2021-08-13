package com.mikeyyds.mall.biz.notice

import android.graphics.Color
import android.os.Bundle

import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.library.restful.MikeCallback
import com.mikeyyds.library.restful.MikeResponse
import com.mikeyyds.library.util.MikeStatusBar
import com.mikeyyds.mall.R
import com.mikeyyds.mall.http.ApiFactory
import com.mikeyyds.mall.http.api.AccountApi
import com.mikeyyds.mall.model.CourseNotice
import com.mikeyyds.ui.item.core.MikeAdapter

@Route(path = "/notice/list")
class NotiveListActivity:MikeBaseActivity() {
    private lateinit var adapter: MikeAdapter
    private lateinit var courseNotice:CourseNotice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)
        MikeStatusBar.setStatusBar(this,true, Color.WHITE,false)
        findViewById<TextView>(R.id.action_back).setOnClickListener {
            onBackPressed()
        }
        initUI()
        querryCourseNotice()
    }

    private fun initUI() {
        val llm = LinearLayoutManager(this)
        adapter = MikeAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter
    }

    private fun querryCourseNotice(){
        ApiFactory.create(AccountApi::class.java).notice().enqueue(object :
            MikeCallback<CourseNotice> {
            override fun onSuccess(response: MikeResponse<CourseNotice>) {
                if (response.successful()&&response.data!=null){
                    bindData(response.data as CourseNotice)
                }
            }

            override fun onFailed(throwable: Throwable) {
            }
        })
    }

    private fun bindData(data: CourseNotice) {
        courseNotice = data
        data.list?.map {
            adapter.addItemAt(0,NoticeItem(it),true)
        }
    }
}