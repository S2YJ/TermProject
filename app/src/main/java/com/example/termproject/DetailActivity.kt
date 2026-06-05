package com.example.termproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.termproject.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val no = intent.getIntExtra("TRAVEL_NO", -1)
        val place = intent.getStringExtra("TRAVEL_PLACE") ?: ""
        val date = intent.getStringExtra("TRAVEL_DATE") ?: ""
        val memo = intent.getStringExtra("TRAVEL_MEMO") ?: ""
        val photoUri = intent.getStringExtra("TRAVEL_PHOTO") ?: ""

        binding.tvDetailPlace.text = place
        binding.tvDetailDate.text = date
        binding.tvDetailMemo.text = memo

        if (photoUri.isNotEmpty()) {
            binding.ivDetailPhoto.setImageURI(Uri.parse(photoUri))
            binding.ivDetailPhoto.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        } else {
            binding.ivDetailPhoto.setImageResource(R.drawable.ic_image)
            binding.ivDetailPhoto.scaleType = android.widget.ImageView.ScaleType.CENTER
        }

        binding.btnGoEdit.setOnClickListener {
            val editIntent = Intent(this, AddEditActivity::class.java).apply {
                putExtra("TRAVEL_NO", no)
                putExtra("TRAVEL_PLACE", place)
                putExtra("TRAVEL_DATE", date)
                putExtra("TRAVEL_MEMO", memo)
                putExtra("TRAVEL_PHOTO", photoUri)
            }
            startActivity(editIntent)
            finish()
        }
    }
}