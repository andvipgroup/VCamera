package check.env;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

import check.env.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("env");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        String path = getFilesDir().getAbsolutePath();
        StringBuffer sb = new StringBuffer();
        sb.append("=====Check Result=====" + "\n");
        sb.append("Files Dir path:" + path + "\n");
        sb.append("isPathExist By Java:" + new File(path).exists() + "\n");
        sb.append("isPathReallyExist By Native:" + (isPathReallyExist(path)) + "\n");
        tv.setText(sb.toString());
    }

    public native int isPathReallyExist(String path);
}