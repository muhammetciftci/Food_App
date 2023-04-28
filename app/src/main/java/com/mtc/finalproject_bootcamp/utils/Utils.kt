package com.mtc.finalproject_bootcamp.utils

import android.app.Activity
import android.util.Patterns
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mtc.finalproject_bootcamp.R

object Utils {
    fun isValidEmail(text: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun areNullOrEmpty(vararg strings: String): Boolean {
        for (string in strings) {
            if (string.isNullOrEmpty()) {
                return false
            }
        }
        return true
    }

    fun bottomNavActive(activity: Activity) {
        val view: BottomNavigationView = activity.findViewById(R.id.bottomNavigationView)
        view.visible()
    }

    fun bottomNavInActive(activity: Activity) {
        val views: BottomNavigationView = activity.findViewById(R.id.bottomNavigationView)
        views.gone()
    }
}