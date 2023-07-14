package virtual.camera.app.util;

import android.os.Build;
import android.os.Process;

public class AbiCore {
    public static boolean is64Bit() {
        if (isM()) {
            return Process.is64Bit();
        } else {
            return Build.CPU_ABI.equals("arm64-v8a");
        }
    }

    public static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
