package com.example.hijaiyahku_new
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hijaiyahku_new.databinding.ActivityChooseQuestBinding
import com.example.hijaiyahku_new.fragment.HintChooseQuest
class ChooseQuest : AppCompatActivity() {
    private lateinit var binding: ActivityChooseQuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        playAnimation()

        binding.btnPisah.setOnClickListener {
            val toHurufPisah = Intent(this@ChooseQuest, DaftarSoal::class.java)
            toHurufPisah.putExtra("jenis", "pisah")
            startActivity(toHurufPisah)
        }
        binding.btnSambung.setOnClickListener {
            val toHurufSambung = Intent(this@ChooseQuest, DaftarSoal::class.java)
            toHurufSambung.putExtra("jenis", "sambung")
            startActivity(toHurufSambung)
        }

        binding.logoTeamP.setOnClickListener {
            val hintFragment : HintChooseQuest = HintChooseQuest()
            hintFragment.show(supportFragmentManager,"HintQuest")
        }
    }
    fun playAnimation(){
        val animator = ObjectAnimator.ofFloat(findViewById(R.id.girl), "rotation", 0f, 20f, -20f)
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 2500L
        animator.start()
    }
}