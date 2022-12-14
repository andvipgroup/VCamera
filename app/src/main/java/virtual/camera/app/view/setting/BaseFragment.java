package virtual.camera.app.view.setting;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private Activity mActivity;

    public Activity getActivitySafe() {
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    // Convenience method to call getActivity().runOnUiThread()
    // without bothering about NPEs
    protected void runOnUiThread(Runnable task) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(task);
    }
}
