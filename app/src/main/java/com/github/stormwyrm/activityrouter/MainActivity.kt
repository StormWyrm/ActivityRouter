package com.github.stormwyrm.activityrouter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.github.stormwyrm.router.Router
import com.github.stormwyrm.router.data.NavigationCallback
import com.github.stormwyrm.router.data.PostCard

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
                    .navigation(object : NavigationCallback{
                        override fun onFound(postCard: PostCard?) {
                            Log.i("MainActivity","找到对应的路由配置：$postCard")
                        }

                        override fun onLost(postCard: PostCard?) {
                            Log.i("MainActivity","未找到对应的路由配置：$postCard")
                        }

                        override fun onArrival(postCard: PostCard?) {
                            Log.i("MainActivity","跳转到相应配置：$postCard")
                        }

                    })
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
