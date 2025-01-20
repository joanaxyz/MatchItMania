package buttons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.matchitmania.R

class MButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF(0f, 0f, 0f, 0f)
    private val foregroundRect = Rect()
    private val paint = Paint().apply{
        isAntiAlias = true
    }
    private var backColor = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    private var cornerRadius = 20f
    private var shadowRadius = 8f
    private var shadowDx = 0f
    private var shadowDy = 4f
    private var shadowColor = 0x29000000
    private var pendingForeground: android.graphics.drawable.Drawable? = null
    private var text :String? = null
    private var fontSize: Float = 16f
    private var fontColor: Int = Color.BLACK
    private var fontWeight: Int = 0
    private var fontType: Int = 0
    init {
        isClickable = true
        isFocusable = true
        setLayerType(LAYER_TYPE_SOFTWARE, null)

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
                text = typedArray.getString(
                    R.styleable.MButton_text
                )
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
        framePaint.apply {
            color = backColor
            style = Paint.Style.FILL
        }

        shadowPaint.apply {
            color = shadowColor
            style = Paint.Style.FILL
            setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        }

        paint.apply{
            color = fontColor
            textSize = fontSize
            typeface = if(fontWeight == 1) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            textAlign = Paint.Align.CENTER
            if (fontType != 0) {
                try {
                    val typeface = ResourcesCompat.getFont(context, fontType)
                    setTypeface(typeface)
                } catch (e: Exception) {
                    e.printStackTrace() // Handle missing font gracefully
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = shadowRadius.toInt() * 2

        rect.set(
            padding.toFloat(),
            padding.toFloat(),
            (w - padding).toFloat(),
            (h - padding).toFloat()
        )

        pendingForeground?.let {
            super.setForeground(it)
            pendingForeground = null
        }

        updateForegroundBounds()
    }

    private fun updateForegroundBounds() {
        if (rect.width() <= 0 || rect.height() <= 0) return

        foreground?.let {
            // Center the foreground in the available space
            val frameWidth = rect.width()
            val frameHeight = rect.height()

            // Use the smaller dimension to maintain aspect ratio
            val size = frameWidth.coerceAtMost(frameHeight).toInt()

            val horizontalPadding = ((frameWidth - size) / 2).toInt()
            val verticalPadding = ((frameHeight - size) / 2).toInt()

            foregroundRect.set(
                rect.left.toInt() + horizontalPadding,
                rect.top.toInt() + verticalPadding,
                rect.left.toInt() + horizontalPadding + size,
                rect.top.toInt() + verticalPadding + size
            )

            it.bounds = foregroundRect
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, shadowPaint)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, framePaint)
        text?.let{
            var x = width/2f
            var y = height / 2f - (paint.descent() + paint.ascent()) /2f
            canvas.drawText(it, x, y, paint)
        }
    }

    override fun setForeground(foreground: android.graphics.drawable.Drawable?) {
        if (width == 0 || height == 0) {
            pendingForeground = foreground
        } else {
            super.setForeground(foreground)
            updateForegroundBounds()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                alpha = 0.8f
                animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .start()
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                alpha = 1.0f
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
                performClick()
                invalidate()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                alpha = 1.0f
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        Log.d("buttons.ProfileButton", "Button clicked")
        return super.performClick()
    }

    fun setbackColor(color: Int) {
        backColor = color
        framePaint.color = color
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Get the requested size mode and size value for width and height
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // Add padding for shadow
        val padding = (shadowRadius * 2).toInt()

        // Calculate the actual width and height based on the measure specs
        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize.coerceAtMost(heightSize)
            else -> suggestedMinimumWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> heightSize.coerceAtMost(widthSize)
            else -> suggestedMinimumHeight
        }

        setMeasuredDimension(width + padding * 2, height + padding * 2)
    }
}