package com.example.termproject

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.termproject.databinding.ActivityAddEditBinding
import java.util.Calendar

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var dbHelper: DBHelper

    private var selectedPhotoUri: String = ""
    private var travelNo: Int = -1

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedPhotoUri = uri.toString()
            binding.ivPhotoPreview.setImageURI(uri)

            binding.ivPhotoPreview.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        dbHelper = DBHelper(this)

        travelNo = intent.getIntExtra("TRAVEL_NO", -1)

        if (travelNo != -1) {
            binding.btnSave.text = "기록 수정 완료"
            binding.etPlace.setText(intent.getStringExtra("TRAVEL_PLACE"))
            binding.etDate.setText(intent.getStringExtra("TRAVEL_DATE"))
            binding.etMemo.setText(intent.getStringExtra("TRAVEL_MEMO"))

            selectedPhotoUri = intent.getStringExtra("TRAVEL_PHOTO") ?: ""
            if (selectedPhotoUri.isNotEmpty()) {
                binding.ivPhotoPreview.setImageURI(Uri.parse(selectedPhotoUri))

                binding.ivPhotoPreview.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            }
        }

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSelectPhoto.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.btnSave.setOnClickListener {
            saveOrUpdateTravelRecord()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.etDate.setText(formattedDate)
        }, year, month, day).show()
    }

    private fun saveOrUpdateTravelRecord() {
        val place = binding.etPlace.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val memo = binding.etMemo.text.toString().trim()

        if (place.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "여행지명과 날짜를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val isSuccess: Boolean
        if (travelNo == -1) {
            val result = dbHelper.insertTravel(place, date, memo, selectedPhotoUri)
            isSuccess = result != -1L
        } else {
            val result = dbHelper.updateTravel(travelNo, place, date, memo, selectedPhotoUri)
            isSuccess = result > 0
        }

        if (isSuccess) {
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "저장 실패!", Toast.LENGTH_SHORT).show()
        }
    }
}