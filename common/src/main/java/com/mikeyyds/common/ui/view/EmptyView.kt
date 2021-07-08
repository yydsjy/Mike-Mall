package com.mikeyyds.common.ui.view

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.mikeyyds.common.R

class EmptyView : LinearLayout {
    private var icon:TextView?=null
    private var title:TextView? = null
    private var button:Button? = null
    constructor(context: Context) : this(context, null) {}

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0) {}

    constructor(context: Context, attributes: AttributeSet?, defStyle: Int) : super(
        context,
        attributes,
        defStyle
    ) {

        orientation= VERTICAL
        gravity= Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view,this,true);

        icon = findViewById(R.id.empty_icon)
        title = findViewById(R.id.empty_text)
        button = findViewById(R.id.empty_action)

        var typeface:Typeface = Typeface.createFromAsset(context.assets,"fonts/iconfont.ttf")
        icon!!.setTypeface(typeface)
    }

    fun setIcon(@StringRes iconRes:Int){
        icon!!.setText(iconRes)
    }

    fun setTitle(text:String){
        title!!.setText(text)
        title!!.visibility=if (TextUtils.isDigitsOnly(text)) View.GONE else View.VISIBLE
    }

    fun setButton(text: String,listener: OnClickListener){
        if  (TextUtils.isEmpty(text)){
            button!!.visibility = View.GONE
        } else{
            button!!.visibility = View.VISIBLE
            button!!.setText(text)
            button!!.setOnClickListener(listener)
        }
    }

}