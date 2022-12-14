package virtual.camera.app.util;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {
    private static Handler sH = new Handler(Looper.getMainLooper());

    public static void runOnMain(Runnable runnable) {
        sH.post(runnable);
    }

    public static void runOnMain(Runnable runnable, long delay) {
        sH.postDelayed(runnable, delay);
    }
}
