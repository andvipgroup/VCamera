package virtual.camera.app.util

import androidx.annotation.StringRes
import virtual.camera.app.app.App


fun getString(@StringRes id:Int,vararg arg:String):String{
    if(arg.isEmpty()){
        return App.getContext().getString(id)
    }
    return App.getContext().getString(id,*arg)
}

