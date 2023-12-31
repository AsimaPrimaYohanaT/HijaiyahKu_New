package com.example.hijaiyahku_new

import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hijaiyahku_new.databinding.ActivityDaftarSoalBinding
import com.example.hijaiyahku_new.fragment.DaftarSoalFragment
import com.example.hijaiyahku_new.fragment.HintFragment
import com.example.hijaiyahku_new.fragment.SuccessFragment
import com.example.hijaiyahku_new.utils.SoalSortType
class DaftarSoal : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var viewModel: DaftarSoalViewModel
    private lateinit var binding: ActivityDaftarSoalBinding
    private val hintDialog = HintFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarSoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        recycler = binding.rvSoal
        recycler.layoutManager = GridLayoutManager(this, 2)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DaftarSoalViewModel::class.java)

        val jenis = intent.getStringExtra("jenis")
        playAnimation()

        if (isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
            val backgroundService = Intent(this, BackgroundSoundService::class.java)
            backgroundService.putExtra("action", "setVolume")
            backgroundService.putExtra("volume", 0.5f)
            startService(backgroundService)
        }

        binding.info.setOnClickListener {
            hintDialog.show(supportFragmentManager, "CustomDialog")
        }
        binding.info.setOnClickListener {
            val daftarSoalFragment = DaftarSoalFragment()
            daftarSoalFragment.show(supportFragmentManager,"DaftarSoalFragment")
        }

        binding.back.setOnClickListener {
            val back = Intent(this@DaftarSoal, ChooseQuest::class.java)
            back.putExtra("jenis",jenis)
            startActivity(back)
        }
        if (jenis == "pisah") {
            viewModel.filter(SoalSortType.TYPE_1)
        } else if (jenis == "sambung") {
            viewModel.filter(SoalSortType.TYPE_2)
        }
        val adapter = DaftarSoalAdapter(viewModel) {  soal,arrId ->
//            if(soal.isComplete){
                val detailIntent = Intent(this@DaftarSoal, DetailQuest::class.java)
                val bundle = Bundle()
                bundle.putIntegerArrayList("arrId", ArrayList(arrId))
                detailIntent.putExtras(bundle)
                detailIntent.putExtra("jenis",jenis)
                detailIntent.putExtra("SOAL", soal.id)
                startActivity(detailIntent)
//            }
        }

        viewModel.soal.observe(this) { pagedList ->
            adapter.submitList(pagedList)
            recycler.adapter = adapter
        }
    }
    override fun onBackPressed() {
        val backIntent =        Intent(this@DaftarSoal,ChooseQuest::class.java)
        startActivity(backIntent)
        finish()


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
    private fun playAnimation(){

        val animator = ObjectAnimator.ofFloat(findViewById(R.id.boy), "rotation", 0f, 30f, -5f)
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 3500L
        animator.start()
    }

}