package com.example.hijaiyahku_new
import android.Manifest
import android.annotation.SuppressLint
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
import android.util.Log
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
import com.example.hijaiyahku_new.utils.MusicPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tensorflow.lite.support.image.TensorImage
class DetailQuest : AppCompatActivity() {
    private val hintDialog = HintFragment()
    lateinit  var successDialog:SuccessFragment
    private val errorDialog = ErrorFragment()
    private lateinit var viewModel: DetailQuestViewModel
    lateinit var binding: ActivityDetailQuestBinding
    private var bitmapFile : Bitmap? = null
    private var getFile: File? = null
    private var answer: String? = null
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        binding.btnBack.setOnClickListener {
            val back = Intent(this@DetailQuest, DaftarSoal::class.java)
            startActivity(back)
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
            }
        }
        hintDialog.show(supportFragmentManager, "CustomDialog")
        binding.apply {
            galery.setOnClickListener { startGallery() }
            btnKamera.setOnClickListener { startCameraX() }
            predict.setOnClickListener {
                    if (bitmapFile != null) {
                        val model = Soal20josMD.newInstance(applicationContext)
                        val image = TensorImage.fromBitmap(bitmapFile)
                        val outputs = model.process(image)
                        val detectionResult = outputs.detectionResultList[0]
                        val score = detectionResult.categoryAsString
                        if (score == answer) {
                            val player = MediaPlayer.create(applicationContext,R.raw.success)

                            player.setVolume(100f, 100f);
                            player.start()
                            if(nextId != null){
                                viewModel.update(nextId,true)
                            }


                            successDialog.show(supportFragmentManager, "CustomDialog")
                        } else {

                            val player1 = MediaPlayer.create(applicationContext,R.raw.fail)
                                player1.setVolume(100f, 100f);
                                player1.start()



                            errorDialog.show(supportFragmentManager, "CustomDialog")

                        }
                        model.close()
                    }


            }
        }
    }
    fun rotateAndFlipBitmap(bitmap: Bitmap): Bitmap {
        // Mengatur matriks rotasi 90 derajat
        val matrix = Matrix()
        matrix.postRotate(90f)
        // Memutar gambar sekitar 90 derajat
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )

        // Mengganti lebar dengan tinggi dan tinggi dengan lebar
        val resultBitmap = Bitmap.createBitmap(
            rotatedBitmap,
            0,
            0,
            rotatedBitmap.width,
            rotatedBitmap.height
        )

        // Bebaskan sumber daya gambar yang tidak digunakan
        rotatedBitmap.recycle()
        bitmap.recycle()

        return resultBitmap
    }
    fun rotateFile(file: File, orientation: Int) {
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
    fun uriToFile(selectedImg: Uri, context: Context): File {
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

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)

    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        var name:String? = null
    }
}