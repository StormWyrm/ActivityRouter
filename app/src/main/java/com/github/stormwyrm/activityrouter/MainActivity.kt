package com.github.stormwyrm.activityrouter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.stormwyrm.router.Router

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        Router.startActivity(this,"Other")
    }
}
