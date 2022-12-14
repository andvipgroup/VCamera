package virtual.camera.app.bean

data class GmsBean(val userID:Int,val userName:String,var isInstalledGms:Boolean)


data class GmsInstallBean(val userID: Int,val success:Boolean,val msg:String)