package com.github.stormwyrm.activityrouter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import com.github.stormwyrm.router.Router

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnOther ->
                Router.getInstance()
                    .build("Other")
                    .navigation()
            R.id.btnAutowired ->
                Router.getInstance()
                    .build("Other")
                    .withByte("age0", 0)
                    .navigation()
        }
    }
}
