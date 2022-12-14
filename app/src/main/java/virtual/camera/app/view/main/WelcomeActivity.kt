package virtual.camera.app.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import virtual.camera.app.R
import virtual.camera.app.util.HandlerUtil
import virtual.camera.app.util.InjectionUtil
import virtual.camera.app.view.list.ListViewModel

class WelcomeActivity : AppCompatActivity() {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        jump()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        previewInstalledAppList()
        HandlerUtil.runOnMain(
            {
                jump()
            },
            3500
        );
    }

    private fun jump() {
        MainActivity.start(this)
        finish()
    }

    private fun previewInstalledAppList() {
        val viewModel =
            ViewModelProvider(this, InjectionUtil.getListFactory()).get(ListViewModel::class.java)
        viewModel.previewInstalledList()
    }
}