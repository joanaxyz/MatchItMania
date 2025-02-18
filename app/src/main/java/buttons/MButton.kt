package buttons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.matchitmania.R

class MButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageButton(context, attrs, defStyleAttr) {

    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF(0f, 0f, 0f, 0f)

    private var backColor = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    private var cornerRadius = 20f
    private var shadowRadius = 8f
    private var shadowDx = 0f
    private var shadowDy = 4f
    private var shadowColor = 0x29000000
    private var backHeightScale = 1f
    private var backWidthScale = 1f

    // Vertical and Horizontal offset for background position (between 0 and 1)
    private var backVerticalOffset: Float = 0f
    private var backHorizontalOffset: Float = 0f

    // Text properties
    private var text: String? = null
    private var fontSize: Float = 16f
    private var fontColor: Int = Color.BLACK
    private var fontWeight: Int = 0
    private var fontType: Int = 0

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        scaleType = ScaleType.FIT_CENTER // Maintain image aspect ratio

        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.MButton
            )
            try {
                backColor = typedArray.getColor(
                    R.styleable.MButton_backColor,
                    backColor
                )
                cornerRadius = typedArray.getDimension(
                    R.styleable.MButton_cornerRadius,
                    cornerRadius
                )
                shadowRadius = typedArray.getDimension(
                    R.styleable.MButton_shadowRadius,
                    shadowRadius
                )
                shadowDx = typedArray.getDimension(
                    R.styleable.MButton_shadowDx,
                    shadowDx
                )
                shadowDy = typedArray.getDimension(
                    R.styleable.MButton_shadowDy,
                    shadowDy
                )
                shadowColor = typedArray.getColor(
                    R.styleable.MButton_shadowColor,
                    shadowColor
                )
                backWidthScale = typedArray.getFloat(
                    R.styleable.MButton_backWidthScale,
                    backWidthScale
                )
                backHeightScale = typedArray.getFloat(
                    R.styleable.MButton_backHeightScale,
                    backHeightScale
                )
                backHorizontalOffset = typedArray.getFloat(
                    R.styleable.MButton_backHorizontalOffset,
                    backHorizontalOffset
                )
                backVerticalOffset = typedArray.getFloat(
                    R.styleable.MButton_backVerticalOffset,
                    backVerticalOffset
                )
                // Text attributes
                text = typedArray.getString(R.styleable.MButton_text)
                fontSize = typedArray.getDimension(R.styleable.MButton_fontSize, 16f)
                fontColor = typedArray.getColor(R.styleable.MButton_fontColor, Color.BLACK)
                fontWeight = typedArray.getInt(R.styleable.MButton_fontWeight, 0)
                fontType = typedArray.getResourceId(R.styleable.MButton_fontType, 0)
            } finally {
                typedArray.recycle()
            }
        }

        setupPaints()
    }

    private fun setupPaints() {
        backPaint.apply {
            color = backColor
            style = Paint.Style.FILL
        }

        shadowPaint.apply {
            color = shadowColor
            style = Paint.Style.FILL
            setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        }

        textPaint.apply {
            color = fontColor
            textSize = fontSize
            textAlign = Paint.Align.CENTER
            typeface = if(fontWeight == 1) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            if (fontType != 0) {
                try {
                    val typeface = ResourcesCompat.getFont(context, fontType)
                    setTypeface(typeface)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = shadowRadius.toInt() * 2

        val newWidth = backWidthScale * width.toFloat()
        val newHeight = backHeightScale * height.toFloat()

        // Apply vertical and horizontal offsets
        val verticalOffset = backVerticalOffset * (h - padding * 2).toFloat()
        val horizontalOffset = backHorizontalOffset * (w - padding * 2).toFloat()

        rect.set(
            padding.toFloat() + horizontalOffset,  // Horizontal offset applied here
            verticalOffset + padding.toFloat(),    // Vertical offset applied here
            newWidth - padding + horizontalOffset,
            newHeight + verticalOffset - padding
        )
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background with shadow and rounded corners
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, shadowPaint)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, backPaint)

        // Draw the image content
        super.onDraw(canvas)

        // Draw text overlay
        text?.let {
            val x = width / 2f
            val y = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
            canvas.drawText(it, x, y, textPaint)
        }
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                alpha = 0.8f
//                animate()
//                    .scaleX(0.95f)
//                    .scaleY(0.95f)
//                    .setDuration(100)
//                    .start()
//                invalidate()
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                alpha = 1.0f
//                animate()
//                    .scaleX(1f)
//                    .scaleY(1f)
//                    .setDuration(100)
//                    .start()
//                if (event.action == MotionEvent.ACTION_UP) {
//                    performClick()
//                }
//                invalidate()
//            }
//        }
//        return super.onTouchEvent(event)
//    }

    fun setbackColor(color: Int) {
        backColor = color
        backPaint.color = color
        invalidate()
    }

    fun setText(newText: String?) {
        text = newText
        invalidate()
    }

    fun setFontSize(size: Float) {
        fontSize = size
        textPaint.textSize = size
        invalidate()
    }

    fun setFontColor(color: Int) {
        fontColor = color
        textPaint.color = color
        invalidate()
    }

    // Setters for horizontal and vertical offsets
    fun setBackVerticalOffset(offset: Float) {
        backVerticalOffset = offset.coerceIn(0f, 1f)
        invalidate()
    }

    fun setBackHorizontalOffset(offset: Float) {
        backHorizontalOffset = offset.coerceIn(0f, 1f)
        invalidate()
    }
}
