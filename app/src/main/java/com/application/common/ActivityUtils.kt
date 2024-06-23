package com.application.common

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.application.rehabease.MenuActivity
import com.application.rehabease.R

/**
 * The Kotlin object to encapsulate movement between activities.
 */
object ActivityUtils {
    /**
     * The method to set up an action bar.
     * @param activity activity where the action bar is located.
     */
    fun actionBarSetup(activity: Activity) {
        val openMenu = activity.findViewById<ImageView>(R.id.image_bars)
        if (openMenu != null) {
            openMenu.setOnClickListener {
                val intent = Intent(activity, MenuActivity::class.java)
                activity.startActivity(intent)
            }
        } else {
            Log.e("ActivityUtils", "ImageView with id image_bars not found")
        }
    }

    /**
     * The method to set up a shorter way to code movement between activities.
     * @param T type of the view.
     * @param layoutResourceId the resource id of the view to set the click listener on.
     * @param activity the current activity.
     * @param targetActivity the target activity.
     */
    fun <T : View> changeActivity(layoutResourceId: Int, activity: Activity, targetActivity: Activity) {
        val element = activity.findViewById<T>(layoutResourceId)
        element.setOnClickListener {
            val intent = Intent(activity, targetActivity::class.java)
            activity.startActivity(intent)
        }
    }
}