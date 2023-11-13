package com.example.hijaiyahku_new

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Intent
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.hijaiyahku_new.databinding.ActivityDetailQuestBinding
import com.example.hijaiyahku_new.fragment.HintFragment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.lifecycle.ViewModelProvider
import com.example.hijaiyahku_new.data.Soal
import com.example.hijaiyahku_new.fragment.ErrorFragment
import com.example.hijaiyahku_new.fragment.SuccessFragment
import com.example.hijaiyahku_new.ml.Soal20josMD
import com.example.hijaiyahku_new.ml.Pisah1
import org.tensorflow.lite.support.image.TensorImage
class DetailQuest : AppCompatActivity() {
    private val hintDialog = HintFragment()
    private lateinit  var successDialog:SuccessFragment
    private val errorDialog = ErrorFragment()
    private lateinit var viewModel: DetailQuestViewModel
    private lateinit var binding: ActivityDetailQuestBinding
    private var bitmapFile : Bitmap? = null
    private var getFile: File? = null
    private var answer: String? = null
    private var kindQuest: Int? = null
    private val FILENAME_FORMAT = "dd-MMM-yyyy"
    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())
    private lateinit var selectedSoal: Soal

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityDetailQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding.btnBack.setOnClickListener {
            val back = Intent(this@DetailQuest, DaftarSoal::class.java)
            startActivity(back)
        }
        if(isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
            Thread {
                intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
                stopService(intent)


            }.start()
        }
        val soalId = intent.getIntExtra("SOAL", 0)
        val arrId = intent.extras?.getIntegerArrayList("arrId")
        var nextId : Int? = null
        if(arrId != null){
            for (i in 0 until arrId.size){
                if(soalId == arrId[i] && i != arrId.size -1){
                    nextId = arrId[i + 1]
                }
            }
        }

        if(nextId != null){
            successDialog = SuccessFragment.newInstance(nextId,arrId!!)

        binding.info.setOnClickListener {
            hintDialog.show(supportFragmentManager, "CustomDialog")
        }
        }else{
            successDialog = SuccessFragment.newInstance(soalId,arrId!!)
        }


        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailQuestViewModel::class.java)

        viewModel.start(soalId)

        viewModel.soal.observe(this) { soal ->
            if (soal != null) {
                selectedSoal = soal
                binding.tvSoal.text = soal.soal
                answer = soal.jawaban1
                kindQuest = soal.jenis
            }
        }


        binding.apply {
            galery.setOnClickListener { startGallery() }
            btnKamera.setOnClickListener { startCameraX() }
            predict.setOnClickListener {
                    if (bitmapFile != null) {
                        if(kindQuest == 2){
                            val image = TensorImage.fromBitmap(bitmapFile)
                            val model = Soal20josMD.newInstance(applicationContext)
                            val outputs = model.process(image)
                            val detectionResult = outputs.detectionResultList[0]
                            val score = detectionResult.categoryAsString
                            if (score == answer) {
                                val player = MediaPlayer.create(applicationContext,R.raw.berhasil)

                                player.setVolume(200f, 200f);
                                player.start()
                                if(nextId != null){
                                    viewModel.update(nextId,true)
                                }


                                successDialog.show(supportFragmentManager, "CustomDialog")
                            } else {

                                val player1 = MediaPlayer.create(applicationContext,R.raw.gagal)
                                player1.setVolume(200f, 200f);
                                player1.start()

                                errorDialog.show(supportFragmentManager, "CustomDialog")

                            }
                            model.close()
                        }else{
                            val image = TensorImage.fromBitmap(bitmapFile)
                            val model = Pisah1.newInstance(applicationContext)
                            val outputs = model.process(image)
                            var size = 0;
                            for (i in 0..(outputs.detectionResultList.size - 1)) {
                                val detectionResult = outputs.detectionResultList.get(i)
                                if (detectionResult.scoreAsFloat > 0.2) {
                                    if (i != 0) {
                                        size = i
                                    }
                                }
                            }
                            var xarr1: FloatArray = Array(size) { 0f }.toFloatArray()
                            var xarr2: FloatArray = Array(size) { 0f }.toFloatArray()
                            var yarr1: FloatArray = Array(size) { 0f }.toFloatArray()
                            var yarr2: FloatArray = Array(size) { 0f }.toFloatArray()
                            var labelArr: Array<String> = Array(size) { "" }
                            var confArr: Array<String> = Array(size) { "" }
                            if (size != 0) {
                                for (i in 0..(size - 1)) {
                                    val detectionResult = outputs.detectionResultList.get(i)
                                    val conf = detectionResult.scoreAsFloat;
                                    val location = detectionResult.locationAsRectF;
                                    val score = detectionResult.categoryAsString;
                                    xarr1[i] = location.left
                                    yarr1[i] = location.top
                                    xarr2[i] = location.right
                                    yarr2[i] = location.bottom
                                    labelArr[i] = score
                                    confArr[i] = conf.toString()

                                }
                            }
                            labelArr.reverse()

                            var string = ""
                            for (element in labelArr) {
                                string += element
                            }
                            if(answer == string){
                                val player = MediaPlayer.create(applicationContext,R.raw.berhasil)

                                player.setVolume(200f, 200f);
                                player.start()
                                if(nextId != null){
                                    viewModel.update(nextId,true)
                                }


                                successDialog.show(supportFragmentManager, "CustomDialog")
                            }else{
                                val player1 = MediaPlayer.create(applicationContext,R.raw.gagal)
                                player1.setVolume(200f, 200f);
                                player1.start()

                                errorDialog.show(supportFragmentManager, "CustomDialog")
                            }
                        }


                    }
            }
        }
    }

    override fun onBackPressed() {
        val backIntent =        Intent(this@DetailQuest,DaftarSoal::class.java)
        startActivity(backIntent)
        finish()

    }


    private fun rotateAndFlipBitmap(bitmap: Bitmap): Bitmap {

        val matrix = Matrix()
        matrix.postRotate(90f)
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )

        val resultBitmap = Bitmap.createBitmap(
            rotatedBitmap,
            0,
            0,
            rotatedBitmap.width,
            rotatedBitmap.height
        )

        rotatedBitmap.recycle()
        bitmap.recycle()

        return resultBitmap
    }
    private fun rotateFile(file: File, orientation: Int) {
        val degree = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        val matrix = Matrix()
        matrix.postRotate(degree)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val orientation1 = it.data?.getStringExtra("orientation")

            myFile?.let { file ->
                val exif = ExifInterface(myFile.absolutePath) // path file gambar
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                rotateFile(file, orientation)
                getFile = file
                val bitmap = BitmapFactory.decodeFile(file.path)
                val bitmapTemp = bitmap
                if (bitmapTemp !== null) {
                    if (orientation1 == "p") {
                        val bitmap = rotateAndFlipBitmap(bitmapTemp)
                        bitmapFile = bitmap
                        binding.imageView2.setImageBitmap(bitmap)
                    } else {
                        binding.imageView2.setImageBitmap(bitmapFile)
                        bitmapFile = bitmap
                    }
                }
            }
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@DetailQuest)
                bitmapFile = uriToBitmap(applicationContext,selectedImg)
               binding.imageView2.setImageBitmap(bitmapFile)

            }
        }
    }

    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }
    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)

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
    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        var name:String? = null
    }
}