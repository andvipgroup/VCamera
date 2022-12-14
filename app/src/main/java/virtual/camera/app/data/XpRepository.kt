package virtual.camera.app.data

import androidx.lifecycle.MutableLiveData
import virtual.camera.app.bean.XpModuleInfo

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/5/2 20:55
 */
class XpRepository {
    fun getInstallModules(modulesLiveData: MutableLiveData<List<XpModuleInfo>>) {
    }

    fun installModule(source: String, resultLiveData: MutableLiveData<String>) {

    }

    fun unInstallModule(packageName: String, resultLiveData: MutableLiveData<String>) {

    }
}