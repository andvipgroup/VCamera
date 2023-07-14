package virtual.camera.app.data

import androidx.lifecycle.MutableLiveData
import com.hack.opensdk.HackApi
import virtual.camera.app.R
import virtual.camera.app.app.AppManager
import virtual.camera.app.bean.GmsBean
import virtual.camera.app.bean.GmsInstallBean
import virtual.camera.app.util.getString

class GmsRepository {


    fun getGmsInstalledList(mInstalledLiveData: MutableLiveData<List<GmsBean>>) {

    }

    fun installGms(
        userID: Int,
        mUpdateInstalledLiveData: MutableLiveData<GmsInstallBean>
    ) {

    }

    fun uninstallGms(
        userID: Int,
        mUpdateInstalledLiveData: MutableLiveData<GmsInstallBean>
    ) {

    }
}