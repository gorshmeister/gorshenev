package com.example.tinkofffilms.utils

import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat


fun ComponentActivity.setUpEdgeToEdge() {
    val impl = if (Build.VERSION.SDK_INT >= 29) {
        EdgeToEdgeApi29()
    } else {
        EdgeToEdgeBase()
    }
    impl.setUp(window, findViewById(android.R.id.content), theme)
}

private interface EdgeToEdgeImpl {
    fun setUp(window: Window, view: View, theme: Resources.Theme)
}

private class EdgeToEdgeBase : EdgeToEdgeImpl {
    override fun setUp(window: Window, view: View, theme: Resources.Theme) {
    }
}

@RequiresApi(29)
private class EdgeToEdgeApi29: EdgeToEdgeImpl {

    override fun setUp(window: Window, view: View, theme: Resources.Theme) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val resources = view.resources
        val transparent = ResourcesCompat.getColor(resources, android.R.color.transparent, theme)
        window.statusBarColor = transparent
        window.navigationBarColor = transparent
        val controller = WindowInsetsControllerCompat(window, view)
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
    }
}