package virtual.camera.app.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import virtual.camera.app.app.App;
import virtual.camera.app.settings.LogUtil;

public class AppUtil {
    public static void killAllApps() {
        try {
            int uid = App.getContext().getPackageManager().getApplicationInfo(App.getContext().getPackageName(), 0).uid;
            String str = App.getContext().getPackageName();
            for (ActivityManager.RunningAppProcessInfo processInfo : ((ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
                if (processInfo.uid != uid) {
                    continue;
                }
                if(processInfo.processName.startsWith(str) && !processInfo.processName.contains(":agent")){
                    continue;
                }
                LogUtil.log("kill processInfo:"+processInfo.processName);
                Process.killProcess(processInfo.pid);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
