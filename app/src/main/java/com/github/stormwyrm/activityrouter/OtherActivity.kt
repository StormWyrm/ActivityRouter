package com.github.stormwyrm.activityrouter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.stormwyrm.router.annotation.Route

@Route(path = "Other")
class OtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
    }
}
