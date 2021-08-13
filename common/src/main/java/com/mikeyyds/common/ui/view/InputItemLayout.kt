package com.mikeyyds.common.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.mikeyyds.common.R

open class InputItemLayout : LinearLayout {

    private lateinit var titleView: TextView
    private lateinit var editText: EditText

    private var bottomLine: Line
    private var topLine: Line

    private var topPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        orientation = HORIZONTAL

        val array =
            context.obtainStyledAttributes(attributeSet, R.styleable.InputItemLayout)
        val titleStyleId = array.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        val title = array.getString(R.styleable.InputItemLayout_title)

        parseTitleStyle(titleStyleId, title)

        val inputStyleId = array.getResourceId(R.styleable.InputItemLayout_inputTextAppearance, 0)
        val hint = array.getString(R.styleable.InputItemLayout_hint)
        val inputType = array.getInteger(R.styleable.InputItemLayout_inputType, 0)

        parseInputStyle(inputStyleId, hint, inputType)


        val topLineStyleId = array.getResourceId(R.styleable.InputItemLayout_topLineAppearance, 0)
        val bottomLineStyleId =
            array.getResourceId(R.styleable.InputItemLayout_bottomLineAppearance, 0)

        topLine = parseLineStyle(topLineStyleId)
        bottomLine = parseLineStyle(bottomLineStyleId)

        if (topLine.enable) {
            topPaint.setColor(topLine.color)
            topPaint.style = Paint.Style.FILL_AND_STROKE
            topPaint.strokeWidth = topLine.height.toFloat()
        }

        if (bottomLine.enable) {
            bottomPaint.setColor(bottomLine.color)
            bottomPaint.style = Paint.Style.FILL_AND_STROKE
            bottomPaint.strokeWidth = bottomLine.height.toFloat()
        }

        array.recycle()
    }

    private fun parseLineStyle(resId: Int): Line {
        var line = Line()
        val array =
            context.obtainStyledAttributes(resId, R.styleable.lineAppearance)
        line.color =
            array.getColor(R.styleable.lineAppearance_color, resources.getColor(R.color.color_D1D))
        line.height = array.getDimensionPixelOffset(R.styleable.lineAppearance_height, 0)
        line.leftMargin = array.getDimensionPixelOffset(R.styleable.lineAppearance_leftMargin, 0)
        line.rightMargin = array.getDimensionPixelOffset(R.styleable.lineAppearance_rightMargin, 0)
        line.enable = array.getBoolean(R.styleable.lineAppearance_enable, false)
        array.recycle()

        return line
    }

    inner class Line {
        var color = 0
        var height = 0
        var leftMargin = 0
        var rightMargin = 0
        var enable = false
    }

    private fun parseInputStyle(resId: Int, hint: String?, inputType: Int) {
        val array =
            context.obtainStyledAttributes(resId, R.styleable.inputTextAppearance)

        val hintColor = array.getColor(
            R.styleable.inputTextAppearance_hintColor,
            resources.getColor(R.color.color_D1D)
        )

        val inputColor = array.getColor(
            R.styleable.inputTextAppearance_inputColor,
            resources.getColor(R.color.color_565)
        )
        val textSize = array.getDimensionPixelSize(
            R.styleable.inputTextAppearance_textSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f)
        )
        editText = EditText(context)
        editText.setPadding(0,0,0,0)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        editText.layoutParams = params
        editText.setHint(hint)
        editText.setHintTextColor(hintColor)
        editText.setTextColor(inputColor)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())

        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.gravity = Gravity.LEFT or Gravity.CENTER

        if (inputType == 0) {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputType == 1) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else if (inputType == 2) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        addView(editText)

        array.recycle()
    }

    private fun parseTitleStyle(resId: Int, title: String?) {
        val array =
            context.obtainStyledAttributes(resId, R.styleable.titleTextAppearance)
        val titleColor = array.getColor(
            R.styleable.titleTextAppearance_titleColor,
            resources.getColor(R.color.color_565)
        )
        val titleSize = array.getDimensionPixelSize(
            R.styleable.titleTextAppearance_titleSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f)
        )
        val minWidth =
            array.getDimensionPixelOffset(R.styleable.titleTextAppearance_minWidth, 0)

        titleView = TextView(context)

        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
        titleView.setTextColor(titleColor)
        titleView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        titleView.minWidth = minWidth
        titleView.gravity = Gravity.LEFT or Gravity.CENTER
        titleView.text = title

        addView(titleView)
        array.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (topLine.enable) {
            canvas!!.drawLine(
                topLine.leftMargin.toFloat(),
                0f,
                measuredWidth - topLine.rightMargin.toFloat(),
                0f,
                topPaint
            )
        }

        if (bottomLine.enable) {
            canvas!!.drawLine(
                bottomLine.leftMargin.toFloat(),
                height - bottomLine.height.toFloat(),
                measuredWidth - bottomLine.rightMargin.toFloat(),
                height - bottomLine.height.toFloat(),
                bottomPaint
            )
        }
    }

    private fun applyUnit(unit: Int, value: Float): Int {
        return TypedValue.applyDimension(unit, value, resources.displayMetrics).toInt()
    }

    fun getTitleView(): TextView {
        return titleView
    }

    fun getEditText(): EditText {
        return editText
    }
}