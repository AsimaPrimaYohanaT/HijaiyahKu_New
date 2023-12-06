package com.example.hijaiyahku_new

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import com.example.hijaiyahku_new.data.Soal
import com.example.hijaiyahku_new.databinding.ActivityDetailQuestBinding
import com.example.hijaiyahku_new.fragment.ErrorFragment
import com.example.hijaiyahku_new.fragment.HintFragment
import com.example.hijaiyahku_new.fragment.SuccessFragment
import com.example.hijaiyahku_new.ml.ModelSambung
import com.example.hijaiyahku_new.ml.ModelPisah
import com.example.hijaiyahku_new.ml.Pisah1
import org.tensorflow.lite.support.image.TensorImage
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

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
    private var jenis =""
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
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }



        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding.btnBack.setOnClickListener {
            val back = Intent(this@DetailQuest, DaftarSoal::class.java)
            startActivity(back)
        }
//        if(isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
//            Thread {
//                intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
//                stopService(intent)
//
//
//            }.start()
//        }

        val soalId = intent.getIntExtra("SOAL", 0)
        jenis = intent.getStringExtra("jenis").toString()
        val arrId = intent.extras?.getIntegerArrayList("arrId")

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailQuestViewModel::class.java)

        if(soalId !=null && arrId != null){
            viewModel.soalId = soalId
            viewModel.arrId = arrId
        }
        var nextId : Int? = null
        Log.d("cekkk",arrId.toString())
        if(arrId != null){
            Log.d("cekkk","masuk sini")
            for (i in 0 until arrId.size){
                if(soalId == arrId[i] && i != arrId.size -1){
                    nextId = arrId[i + 1]
                }
            }
        }

        Log.d("cekkk",soalId.toString())
       Log.d("cekkk",nextId.toString())
        val savedSoalId = viewModel.soalId
        val savedArrId = viewModel.arrId


        if(nextId != null){
            successDialog = SuccessFragment.newInstance(nextId,savedArrId!!)

            binding.info.setOnClickListener {
                hintDialog.show(supportFragmentManager, "CustomDialog")
            }
        }else{
            if (savedArrId != null) {
                successDialog = SuccessFragment.newInstance(null, savedArrId)
            } else {
                // Handle kasus ketika arrId null
                Log.e("YourActivity", "arrId is null in the else block")
            }
        }

        viewModel.start(savedSoalId)

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
                            val desiredWidth = 640
                            val desiredHeight = 640
                            val grayscaleBitmap = convertToGrayscale(bitmapFile!!, desiredWidth, desiredHeight)
                            val resizedBitmap = Bitmap.createScaledBitmap(grayscaleBitmap, desiredWidth, desiredHeight, true)
                            var copyBitmap = resizedBitmap!!.copy(Bitmap.Config.ARGB_8888, true);
                            val image = TensorImage.fromBitmap(copyBitmap)
                            val model = ModelSambung.newInstance(applicationContext)
                            val outputs = model.process(image)
                            val detectionResult = outputs.detectionResultList[0]
                            val score = detectionResult.categoryAsString

                            Log.d("sina",score)
                            Log.d("sina",answer!!)
                            if (score == answer) {
                                val player = MediaPlayer.create(applicationContext,R.raw.berhasil)

                            player.setVolume(200f, 200f);
                            player.start()
                            if(nextId != null){
                                viewModel.update(nextId,true)
                            }


                            successDialog.show(supportFragmentManager, "CustomDialog")
                        } else {
                                if(isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
                                    Thread {
                                        intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
                                        stopService(intent)


                                    }.start()
                                }
                            val player1 = MediaPlayer.create(applicationContext,R.raw.gagal)
                            player1.setVolume(200f, 200f);
                            player1.start()

                            errorDialog.show(supportFragmentManager, "CustomDialog")
                                Thread {
                                    intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
                                    startService(intent)


                                }.start()
                        }
                        model.close()
                    }else{


                        val paint = Paint().apply {
                            color = Color.RED
                            style = Paint.Style.STROKE
                            strokeWidth = 4.0f
                            textSize = 90f
                        }
                        val desiredWidth = 640
                        val desiredHeight = 640
                        val grayscaleBitmap = convertToGrayscale(bitmapFile!!, desiredWidth, desiredHeight)
                        val resizedBitmap = Bitmap.createScaledBitmap(grayscaleBitmap, desiredWidth, desiredHeight, true)
                        var copyBitmap = resizedBitmap!!.copy(Bitmap.Config.ARGB_8888, true);
                        val canvas = Canvas(copyBitmap)
                            Log.d("lol","mulai")

                        val image = TensorImage.fromBitmap(copyBitmap)
                        val model = ModelPisah.newInstance(applicationContext)
                        val outputs = model.process(image)
                        var size = 0;
                        for (i in 0..(outputs.detectionResultList.size - 1)) {
                            val detectionResult = outputs.detectionResultList.get(i)
                            Log.d("lol",outputs.detectionResultList[i].categoryAsString.toString())
                            Log.d("lol",outputs.detectionResultList[i].scoreAsFloat.toString())

                            if (detectionResult.scoreAsFloat > 0.5f) {

                                if (i != 0) {
                                    size = i
                                }
                            }
                        }
                        size++

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

                        val pairs = xarr1.zip(labelArr)

                        val sortedPairs = pairs.sortedByDescending { it.first }
                        val sortedA = sortedPairs.map { it.first }.toTypedArray()
                        val sortedB = sortedPairs.map { it.second }.toTypedArray()

                        Log.d("detect1", sortedB.contentToString())

                        for (i in 0..(xarr1.size -1)) {
                            canvas?.drawRect(xarr1.get(i), yarr1.get(i), xarr2.get(i), yarr2.get(i), paint)
                            canvas?.drawText("${labelArr.get(i)} ", xarr1.get(i) , yarr1.get(i) -  10, paint)
                        }

//                        binding.imageView2.setImageBitmap(copyBitmap)


                        var string = ""
                        for (element in sortedB) {
                            string += element
                        }
                            Log.d("cekk",string)
                            Log.d("cekk",answer!!)
                        if(answer == string){
                            if(isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
                                Thread {
                                    intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
                                    stopService(intent)


                                }.start()
                            }
                            val player = MediaPlayer.create(applicationContext,R.raw.berhasil)

                            player.setVolume(200f, 200f);
                            player.start()
                            if(nextId != null){
                                viewModel.update(nextId,true)
                            }


                            successDialog.show(supportFragmentManager, "FCustomDialog")
                            Thread {
                                intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
                                startService(intent)


                            }.start()
                        }else{
                            if(isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
                                Thread {
                                    intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
                                    stopService(intent)


                                }.start()
                            }
                            val player1 = MediaPlayer.create(applicationContext,R.raw.gagal)
                            player1.setVolume(200f, 200f);
                            player1.start()

                            errorDialog.show(supportFragmentManager, "CustomDialog")
                            Thread {
                                intent = Intent(this@DetailQuest, BackgroundSoundService::class.java)
                                stopService(intent)


                            }.start()
                        }
                    }


                }
            }
        }
    }

    override fun onBackPressed() {
        val back = Intent(this@DetailQuest, DaftarSoal::class.java)
        back.putExtra("jenis",jenis)
        startActivity(back)

    }

//    override fun onBackPressed() {
//        val backIntent =        Intent(this@DetailQuest,DaftarSoal::class.java)
//        startActivity(backIntent)
//        finish()
//
//    }
    fun convertToGrayscale(inputBitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val width = inputBitmap.width
        val height = inputBitmap.height
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f) // Set saturation to 0 for grayscale
        val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorMatrixFilter
        canvas.drawBitmap(inputBitmap, 0f, 0f, paint)
        return grayscaleBitmap
    }
    fun getPermission() {
        var permissionList = mutableListOf<String>()

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) permissionList.add(
            android.Manifest.permission.CAMERA
        )


        if (permissionList.size > 0) {
            requestPermissions(permissionList.toTypedArray(), 101)
        }
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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




                Log.d("rotasi",orientation.toString())
                Log.d("rotasi", ExifInterface.ORIENTATION_ROTATE_90.toString())
                Log.d("rotasi", ExifInterface.ORIENTATION_ROTATE_180.toString())
                Log.d("rotasi", ExifInterface.ORIENTATION_ROTATE_270.toString())
                Log.d("rotasi", ExifInterface.ORIENTATION_NORMAL.toString())
                Log.d("rotasi", ExifInterface.ORIENTATION_UNDEFINED.toString())


                if (bitmapTemp !== null) {
                    if (orientation1 == "p") {
                        var bitmapp : Bitmap? = null
                        if(ExifInterface.ORIENTATION_ROTATE_90 == orientation){
                          bitmapp = rotateAndFlipBitmap(bitmapTemp)
                        }else{
                          bitmapp = bitmap
                        }

                        bitmapFile = bitmapp
                        binding.imageView2.setImageBitmap(bitmapp)


                    } else {

                        bitmapFile = bitmap
                        binding.imageView2.setImageBitmap(bitmapFile)

                    }
                }
            }
        }
    }
    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
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
                    val exif = ExifInterface(myFile.absolutePath) // path file gambar
                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    var bitmapFile2 :Bitmap? = null
                    if(orientation ==  ExifInterface.ORIENTATION_ROTATE_90){
                        bitmapFile2 = BitmapFactory.decodeFile(myFile.path)
                        val bitmapTemp = bitmapFile2
                        bitmapFile = rotateAndFlipBitmap(bitmapTemp)

                        // Pastikan untuk menggunakan objek yang dikembalikan oleh rotateAndFlipBitmap
                        binding.imageView2.setImageBitmap(bitmapFile)
                    }else{
                        bitmapFile2 =BitmapFactory.decodeFile(myFile.path)
                        bitmapFile =BitmapFactory.decodeFile(myFile.path)
                        binding.imageView2.setImageBitmap(bitmapFile2)
                    }



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