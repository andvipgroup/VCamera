package virtual.camera.app.app

import android.content.Context
import android.content.SharedPreferences

object AppManager {
    @JvmStatic
    val mRemarkSharedPreferences: SharedPreferences by lazy {
        App.getContext().getSharedPreferences("UserRemark",Context.MODE_PRIVATE)
    }
}
