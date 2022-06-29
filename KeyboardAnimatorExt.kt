package kim.uno.mock.extension

import android.animation.ValueAnimator
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.abs
import kotlin.math.max

fun View.applyKeyboardInsetsAnimator(offset: Int = bottomPadding) {
    val bottomNormalizer = object : (Int) -> Int {
        override fun invoke(bottom: Int): Int {
            val bottom = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val navigationBarHeight = context.navigationBarHeight.toInt()
                max(0, bottom - navigationBarHeight)
            } else {
                bottom
            }
            return offset + bottom
        }
    }

    var imeHeight = 0

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setWindowInsetsAnimationCallback(object :
            WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            override fun onProgress(
                insets: WindowInsets,
                runningAnimations: MutableList<WindowInsetsAnimation>
            ): WindowInsets {
                val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val bottomNormalized = bottomNormalizer(bottom)
                bottomPadding = bottomNormalized
                return insets
            }

            override fun onEnd(animation: WindowInsetsAnimation) {
                super.onEnd(animation)
                val bottomNormalized = bottomNormalizer(imeHeight)
                bottomPadding = bottomNormalized
            }
        })
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val bottomNormalized = bottomNormalizer(imeHeight)
            if (abs(bottomNormalized - paddingBottom) > 0) {
                ValueAnimator.ofInt(paddingBottom, imeHeight).apply {
                    addUpdateListener { bottomPadding = bottomNormalizer(it.animatedValue as Int) }
                    doOnEnd { bottomPadding = bottomNormalizer(imeHeight) }
                    duration = 200L
                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        }
        insets
    }
}
