package com.example.hijaiyahku_new

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.hijaiyahku_new.data.MusicPreference
import com.example.hijaiyahku_new.data.Status
import com.example.hijaiyahku_new.databinding.ActivityMainBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var music = false

    override fun onDestroy() {
        super.onDestroy()
        stopBackgroundService()
    }


    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(MusicPreference.getInstance(dataStore), this)
        )[MainViewModel::class.java]

        mainViewModel.getStatus().observe(this, { status ->
            if (status.music) {
                if (!isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
                    startBackgroundService()
                    binding.mscIcnActive.visibility = View.VISIBLE
                    binding.mscIcnOff.visibility = View.GONE
                    music= true
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }else{
                stopBackgroundService()
                binding.mscIcnActive.visibility = View.GONE
                binding.mscIcnOff.visibility = View.VISIBLE
            }
        })
        playAnimation()
        binding.btnHome.setOnClickListener {
            val mulai = Intent(this@MainActivity, ChooseQuest::class.java)
            mulai.putExtra("music",music)
            startActivity(mulai)
        }
        binding.mscBtn.setOnClickListener {
            if (binding.mscIcnActive.visibility == View.GONE) {
                binding.mscIcnActive.visibility = View.VISIBLE
                binding.mscIcnOff.visibility = View.GONE
                mainViewModel.saveStatus(Status(true))
                startBackgroundService()
            } else {
                binding.mscIcnActive.visibility = View.GONE
                binding.mscIcnOff.visibility = View.VISIBLE
                mainViewModel.saveStatus(Status(false))
                stopBackgroundService()
            }
        }
    }
    private fun startBackgroundService() {
        if (!isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
            startService(Intent(this@MainActivity, BackgroundSoundService::class.java))
        }
    }

    private fun stopBackgroundService() {
        if (isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
            stopService(Intent(this@MainActivity, BackgroundSoundService::class.java))
        }
    }

    private fun isBackgroundServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun playAnimation() {
        val animator = ObjectAnimator.ofFloat(findViewById(R.id.btn_home), "scaleX", 1f, 1.1f)
        val animator1 = ObjectAnimator.ofFloat(findViewById(R.id.btn_home), "scaleY", 1f, 1.1f)
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 1000L
        animator.start()
        animator1.repeatCount = ObjectAnimator.INFINITE
        animator1.repeatMode = ObjectAnimator.REVERSE
        animator1.duration = 1000L
        animator1.start()
        ObjectAnimator.ofFloat(binding.asset51, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val buttonHome = ObjectAnimator.ofFloat(binding.btnHome, View.ALPHA, 1f).setDuration(100)
        val girl = ObjectAnimator.ofFloat(binding.asset33, View.ALPHA, 1f).setDuration(100)
        val boy = ObjectAnimator.ofFloat(binding.asset51, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                buttonHome,
                girl,
                boy
            )
            startDelay = 500
        }.start()

    }


}