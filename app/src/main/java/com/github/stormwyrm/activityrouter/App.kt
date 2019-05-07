package com.github.stormwyrm.activityrouter

import android.app.Application
import com.github.stormwyrm.router.Router

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Router.init()
    }
}