package com.github.stormwyrm.activityrouter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
                    .build("Autowired")
                    .withByte("age0", 1)
                    .withShort("age1", 2)
                    .withInt("age2", 3)
                    .withLong("age3", 4)
                    .withFloat("age4", 5.0f)
                    .withDouble("age5", 6.0)
                    .withChar("age6", '7')
                    .withBoolean("age7", true)
                    .withString("Name","liqingfeng")
                    .navigation()
        }
    }
}
