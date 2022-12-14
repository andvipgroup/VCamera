package virtual.camera.app.data

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.hack.opensdk.HackApi
import virtual.camera.app.R
import virtual.camera.app.app.App
import virtual.camera.app.app.AppManager
import virtual.camera.app.bean.AppInfo
import virtual.camera.app.bean.InstalledAppBean
import virtual.camera.app.util.AbiUtils
import virtual.camera.app.util.getString
import java.io.File


/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 23:05
 */

class AppsRepository {
    val TAG: String = "AppsRepository"
    private var mInstalledList = mutableListOf<AppInfo>()


    fun previewInstallList() {
        synchronized(mInstalledList) {
            val installedApplications: List<ApplicationInfo> =
                App.getContext().getPackageManager().getInstalledApplications(0)
            val installedList = mutableListOf<AppInfo>()

            for (installedApplication in installedApplications) {
                val file = File(installedApplication.sourceDir)

                if ((installedApplication.flags and ApplicationInfo.FLAG_SYSTEM) != 0) continue

                if (!AbiUtils.isSupport(file)) continue

                val isXpModule = false

                val info = AppInfo(
                    installedApplication.loadLabel(App.getContext().getPackageManager()).toString(),
                    installedApplication.loadIcon(App.getContext().getPackageManager()),
                    installedApplication.packageName,
                    installedApplication.sourceDir,
                    isXpModule
                )
                installedList.add(info)
            }
            this.mInstalledList.clear()
            this.mInstalledList.addAll(installedList)
        }


    }

    fun getInstalledAppList(
        userID: Int,
        loadingLiveData: MutableLiveData<Boolean>,
        appsLiveData: MutableLiveData<List<InstalledAppBean>>
    ) {
        loadingLiveData.postValue(true)
        synchronized(mInstalledList) {
            Log.d(TAG, mInstalledList.joinToString(","))
            val newInstalledList = mInstalledList.map {
                var isInstalled = HackApi.getPackageInfo(it.packageName,userID,0) != null
                InstalledAppBean(
                    it.name,
                    it.icon,
                    it.packageName,
                    it.sourceDir,
                    isInstalled
                )
            }
            appsLiveData.postValue(newInstalledList)
            loadingLiveData.postValue(false)
        }
    }

    fun getInstalledModuleList(
        loadingLiveData: MutableLiveData<Boolean>,
        appsLiveData: MutableLiveData<List<InstalledAppBean>>
    ) {

        loadingLiveData.postValue(true)
        synchronized(mInstalledList) {
            val moduleList = mInstalledList.filter {
                it.isXpModule
            }.map {
                InstalledAppBean(
                    it.name,
                    it.icon,
                    it.packageName,
                    it.sourceDir,
                    false
                )
            }
            appsLiveData.postValue(moduleList)
            loadingLiveData.postValue(false)
        }

    }


    fun getVmInstallList(userId: Int, appsLiveData: MutableLiveData<List<AppInfo>>) {
        val sortListData =
            AppManager.mRemarkSharedPreferences.getString("AppList$userId", "")
        val sortList = sortListData?.split(",")

        var installedPkgs = HackApi.getInstalledPackages(0, userId)
        if(installedPkgs != null && installedPkgs.size > 0){
            installedPkgs.remove("com.waxmoon.ma.gp");
        }

        var applicationList = mutableListOf<ApplicationInfo>()
        installedPkgs.forEach {
            var packageInfo = HackApi.getPackageInfo(it, userId, 0)
            applicationList.add(packageInfo.applicationInfo)
        }

        val appInfoList = mutableListOf<AppInfo>()
        applicationList.also {
            if (sortList.isNullOrEmpty()) {
                return@also
            }
            it.sortWith(AppsSortComparator(sortList))

        }.forEach {
            val info = AppInfo(
                it.loadLabel(App.getContext().getPackageManager()).toString(),
                it.loadIcon(App.getContext().getPackageManager()),
                it.packageName,
                it.sourceDir,
                isInstalledXpModule(it.packageName)
            )

            appInfoList.add(info)
        }


        appsLiveData.postValue(appInfoList)
    }

    private fun isInstalledXpModule(packageName: String): Boolean {
        return false
    }


    fun installApk(source: String, userId: Int, resultLiveData: MutableLiveData<String>) {
        var packageName:String = source;
        val installResult =  HackApi.installPackageFromHost(packageName,userId,false)
        Log.e("11111","source:"+source+",installResult:"+installResult)
        var INSTALL_SUCCEEDED:Int = 1
        if (installResult == INSTALL_SUCCEEDED) {
            updateAppSortList(userId, packageName, true)
            resultLiveData.postValue(getString(R.string.install_success))
        } else {
            resultLiveData.postValue(getString(R.string.install_fail, "failed code:"+installResult))
        }
        scanUser()
    }

    fun unInstall(packageName: String, userID: Int, resultLiveData: MutableLiveData<String>) {
        HackApi.uninstallPackage(packageName,userID)
        updateAppSortList(userID, packageName, false)
        scanUser()
        resultLiveData.postValue(getString(R.string.uninstall_success))
    }


    fun launchApk(packageName: String, userId: Int, launchLiveData: MutableLiveData<Boolean>) {
        val intent: Intent = HackApi.getLaunchIntentForPackage(packageName,userId)
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        var result = HackApi.startActivity(intent, 0) == 0
        launchLiveData.postValue(result)
    }


    fun clearApkData(packageName: String, userID: Int, resultLiveData: MutableLiveData<String>) {
        HackApi.deletePackageData(packageName,userID)
        resultLiveData.postValue(getString(R.string.clear_success))
    }

    /**
     * 倒序递归扫描用户，
     * 如果用户是空的，就删除用户，删除用户备注，删除应用排序列表
     */
    private fun scanUser() {
        if(1==1){
            return
        }
        val userList = HackApi.getAvailableUserSpace()

        if (userList.isEmpty()) {
            return
        }

        val id = userList.last()

        if (HackApi.getInstalledPackages(0, id).isEmpty()) {
            AppManager.mRemarkSharedPreferences.edit {
                remove("Remark$id")
                remove("AppList$id")
            }
            scanUser()
        }
    }


    /**
     * 更新排序列表
     * @param userID Int
     * @param pkg String
     * @param isAdd Boolean true是添加，false是移除
     */
    private fun updateAppSortList(userID: Int, pkg: String, isAdd: Boolean) {

        val savedSortList =
            AppManager.mRemarkSharedPreferences.getString("AppList$userID", "")

        val sortList = linkedSetOf<String>()
        if (savedSortList != null) {
            sortList.addAll(savedSortList.split(","))
        }

        if (isAdd) {
            sortList.add(pkg)
        } else {
            sortList.remove(pkg)
        }

        AppManager.mRemarkSharedPreferences.edit {
            putString("AppList$userID", sortList.joinToString(","))
        }

    }

    /**
     * 保存排序后的apk顺序
     */
    fun updateApkOrder(userID: Int, dataList: List<AppInfo>) {
        AppManager.mRemarkSharedPreferences.edit {
            putString("AppList$userID",
                dataList.joinToString(",") { it.packageName })
        }

    }

}
