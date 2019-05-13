package com.github.stormwyrm.activityrouter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.stormwyrm.router.Router
import com.github.stormwyrm.router.annotation.Autowired
import com.github.stormwyrm.router.annotation.Route
import kotlinx.android.synthetic.main.activity_autowired.*

@Route(path = "Autowired")
class AutowiredActivity : AppCompatActivity() {
    @Autowired
    @JvmField
    var age0: Byte? = 0

    @Autowired
    @JvmField
    var age1: Short? = 0

    @Autowired
    @JvmField
    var age2: Int? = 0

    @Autowired
    @JvmField
    var age3: Long? = 0

    @Autowired
    @JvmField
    var age4: Float? = 0.0f

    @Autowired
    @JvmField
    var age5: Double? = 0.0

    @Autowired
    @JvmField
    var age6: Char? = 'a'

    @Autowired(name = "Name")
    @JvmField
    var name: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autowired)
        Router.inject(this)
        val msg = "age0 = $age0;\nage1 = $age1;\nage2 = $age2;\nage3 = $age3;\nage4 = $age4;\nage5 = $age5;\nage6 = $age6;\nname = $name"
        Log.i("AutowiredActivity",msg)
        textView.text = msg
    }
}
