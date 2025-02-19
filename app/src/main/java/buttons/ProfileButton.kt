package buttons

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.matchitmania.R

class ProfileButton @JvmOverloads constructor(
    context: Context, attr: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attr, defStyle) {

    private var isHovered = false
    private var contentScale = 0.7f

    private val backgroundView: FrameLayout = FrameLayout(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
            gravity = android.view.Gravity.CENTER
        }
    }

    private val contentImageView: ImageView = ImageView(context).apply {
        scaleType = ImageView.ScaleType.FIT_CENTER
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
            gravity = android.view.Gravity.CENTER
        }
    }

    private val frameImageView: ImageView = ImageView(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        scaleType = ImageView.ScaleType.FIT_XY
    }

    init {
        // Add views in order: background, frame, content
        addView(backgroundView)
        addView(frameImageView)
        addView(contentImageView)

        // Process attributes
        attr?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.ProfileButton
            )

            try {
                setFrameImage(typedArray.getDrawable(R.styleable.ProfileButton_frameImage))
                setContentImage(typedArray.getDrawable(R.styleable.ProfileButton_contentImage))
                val backgroundColor = typedArray.getColor(
                    R.styleable.ProfileButton_backgroundColor,
                    Color.BLACK  // Default to transparent if not specified
                )
                setBackgroundColor(backgroundColor)
                contentScale = typedArray.getFloat(R.styleable.ProfileButton_contentScale, 0.7f)
            } finally {
                typedArray.recycle()
            }
        }

        isClickable = true
        isFocusable = true
        isHovered = false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            updateContentLayout()
        }
    }

    private fun updateContentLayout() {
        // Scale both content image and background
        contentImageView.apply {
            scaleX = contentScale
            scaleY = contentScale
        }

        backgroundView.apply {
            scaleX = contentScale
            scaleY = contentScale
        }
    }

    override fun onHoverEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_HOVER_ENTER -> {
                if (!isHovered) {
                    isHovered = true
                    onMouseEnter()
                }
                return true
            }
            MotionEvent.ACTION_HOVER_EXIT -> {
                if (isHovered) {
                    isHovered = false
                    onMouseExit()
                }
                return true
            }
        }
        return super.onHoverEvent(event)
    }

    override fun performClick(): Boolean {
        onMouseClick()
        return super.performClick()
    }

    private fun onMouseEnter() {
        Log.d("ProfileButton", "Mouse entered")
        alpha = 0.8f
        invalidate()
    }

    private fun onMouseExit() {
        Log.d("ProfileButton", "Mouse exited")
        alpha = 1.0f
        invalidate()
    }

    private fun onMouseClick() {
        Log.d("ProfileButton", "Mouse clicked")
        animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    fun setContentImage(drawable: Drawable?) {
        contentImageView.setImageDrawable(drawable)
        contentImageView.visibility = if (drawable != null) VISIBLE else GONE
    }

    fun setFrameImage(drawable: Drawable?) {
        frameImageView.setImageDrawable(drawable)
        frameImageView.visibility = if (drawable != null) VISIBLE else GONE
    }

    fun setContentScale(scale: Float) {
        if (contentScale != scale) {
            contentScale = scale
            if (width > 0 && height > 0) {
                updateContentLayout()
            }
        }
    }

    override fun setBackgroundColor(color: Int) {
        backgroundView.setBackgroundColor(color)
    }
}