package com.example.termproject

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.termproject.databinding.ActivityAddEditBinding
import android.net.Uri
import android.os.Bundle
import android.widget.Toast

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var dbHelper: DBHelper
    private var selectedPhotoUri: String = ""

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
            binding.ivPhotoPreview.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSelectPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveTravelRecord()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth +1, selectedDay)
            binding.etDate.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveTravelRecord() {
        val place = binding.etPlace.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val memo = binding.etMemo.text.toString().trim()

        if (place.isEmpty()) {
            Toast.makeText(this, "여행지명을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "방문 날짜를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val result = dbHelper.insertTravel(place, date, memo, selectedPhotoUri)

        if (result != -1L) {
            Toast.makeText(this, "여행 기록이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}