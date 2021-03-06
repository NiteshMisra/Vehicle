@file:Suppress("DEPRECATION")

package `in`.vehicle.activity

import `in`.vehicle.R
import `in`.vehicle.database.VehicleDatabase
import `in`.vehicle.database.entity.VehicleModel
import `in`.vehicle.databinding.ActivityVehicleDetailBinding
import `in`.vehicle.extras.PickImageDialog
import `in`.vehicle.extras.VehicleFactory
import `in`.vehicle.repository.VehicleRepository
import `in`.vehicle.viewmodel.VehicleViewModel
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class VehicleDetail : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleDetailBinding
    private var entryId : Int = 0
    private var imageToString: String? = null
    private var imageUri: Uri? = null
    private val cameraPickRequest: Int = 0
    private val galleryPickRequest: Int = 1
    private lateinit var viewModel: VehicleViewModel
    private val permission = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA
    )
    private val requestCode = 3
    private lateinit var oldVehicleModel: VehicleModel


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_detail)
        if (supportActionBar != null) {
            supportActionBar!!.title = ("Details")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        entryId = intent.getIntExtra("entryId",0)

        val database: VehicleDatabase = VehicleDatabase.getInstance(this)!!
        val repository = VehicleRepository(database.vehicleDao())
        val factory = VehicleFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(VehicleViewModel::class.java)


        viewModel.getVehicleDetail(entryId).observe(this, Observer { vehicleModel ->
            oldVehicleModel = vehicleModel
            binding.vehicleNo.setText(vehicleModel.vehicleNo)
            binding.model.setText(vehicleModel.model)
            binding.variant.setText(vehicleModel.variant)

            val stream = ByteArrayInputStream(Base64.decode(vehicleModel.image, Base64.DEFAULT))
            val bitmap = BitmapFactory.decodeStream(stream)
            binding.image.setImageBitmap(bitmap)
        })

        binding.editImage.setOnClickListener {
            checkPermission()
        }

        binding.update.setOnClickListener {

            if (binding.vehicleNo.text.toString().isEmpty()) {
                Toast.makeText(this, "enter vehicle no.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.model.text.toString().isEmpty()) {
                Toast.makeText(this, "enter model", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.variant.text.toString().isEmpty()) {
                Toast.makeText(this, "enter variant", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.vehicleNo.text.toString() == oldVehicleModel.vehicleNo &&
                binding.model.text.toString() == oldVehicleModel.model &&
                binding.variant.text.toString() == oldVehicleModel.variant &&
                imageToString == null
            ) {
                return@setOnClickListener
            }

            updateData()

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            checkCallingOrSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            val dialog = PickImageDialog(this)
            dialog.show(supportFragmentManager,"PickImageDialog2")

        } else {
            requestPermissions(permission, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                val dialog = PickImageDialog(this)
                dialog.show(supportFragmentManager,"PickImageDialog2")

            } else {
                Toast.makeText(this,"Allow Permissions",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateData() {

        val newVehicleModel: VehicleModel = if (imageToString == null) {
            VehicleModel(
                binding.vehicleNo.text.toString(),
                binding.model.text.toString(),
                binding.variant.text.toString(),
                oldVehicleModel.image
            )
        } else {
            VehicleModel(
                binding.vehicleNo.text.toString(),
                binding.model.text.toString(),
                binding.variant.text.toString(),
                imageToString!!
            )
        }
        newVehicleModel.entryId = oldVehicleModel.entryId

        viewModel.updateVehicle(newVehicleModel)
        Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()

    }

    fun cameraPicker() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "MyPicture")
        values.put(
            MediaStore.Images.Media.DESCRIPTION,
            "Photo taken on " + System.currentTimeMillis()
        )
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, cameraPickRequest)
    }

    fun galleryPicker() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(galleryIntent, "Select Picture"),
            galleryPickRequest
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == cameraPickRequest) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                binding.image.setImageBitmap(bitmap)
                val b = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, b)
                val byteArray = b.toByteArray()
                imageToString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
            if (requestCode == galleryPickRequest && data != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                binding.image.setImageBitmap(bitmap)
                val b = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, b)
                val byteArray = b.toByteArray()
                imageToString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }


}
