package com.cy.photoselector.basic

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.core.app.ActivityOptionsCompat
import com.cy.photoselector.R
import com.cy.photoselector.image.ImageLoader

object PhotoSelectorConfig {
    fun setImageLoader(loader: (ImageView, Uri) -> Unit) {
        ImageLoader.setImageLoader(loader);
    }

    @AnimRes
    private var endPageEnterAnim: Int? = null

    @AnimRes
    private var startPageExitAnim: Int? = null

    @AnimRes
    private var startPageEnterAnim: Int? = null

    @AnimRes
    private var endPageExitAnim: Int? = null
    fun setTransitionAnim(
        @AnimRes endPageEnterAnim: Int? = null,
        @AnimRes startPageExitAnim: Int? = null,
        @AnimRes startPageEnterAnim: Int? = null,
        @AnimRes endPageExitAnim: Int? = null,
    ) {
        this.endPageEnterAnim = endPageEnterAnim
        this.startPageExitAnim = startPageExitAnim
        this.startPageEnterAnim = startPageEnterAnim
        this.endPageExitAnim = endPageExitAnim
    }

    fun setSlideAnim() {
        setTransitionAnim(
            R.anim.slide_in_right, R.anim.stay_here,
            R.anim.stay_here, R.anim.slide_out_right,
        )
    }

    fun setUpDownAnim() {
        setTransitionAnim(
            R.anim.slide_in_up, R.anim.stay_here,
            R.anim.stay_here, R.anim.slide_out_down,
        )
    }

    fun getEnterAnimationOption(context: Context): ActivityOptionsCompat? {
        if (endPageEnterAnim == null || startPageExitAnim == null) {
            return null
        }
        return ActivityOptionsCompat.makeCustomAnimation(
            context, endPageEnterAnim!!, startPageExitAnim!!
        )
    }

    fun overridePendingTransition(activity: Activity) {
        if (startPageEnterAnim == null || endPageExitAnim == null) {
            return
        }
        activity.overridePendingTransition(
            startPageEnterAnim!!,
            endPageExitAnim!!
        )
    }
}

