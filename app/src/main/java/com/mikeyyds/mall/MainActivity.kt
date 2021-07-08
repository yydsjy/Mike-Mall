package com.mikeyyds.mall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mikeyyds.mall.fragment.home.HomePageFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homePageFragment = HomePageFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container,homePageFragment)
            .commit()
    }
}