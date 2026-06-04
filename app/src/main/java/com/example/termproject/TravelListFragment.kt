package com.example.termproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.termproject.databinding.FragmentTravelListBinding

class TravelListFragment : Fragment() {
    private var _binding: FragmentTravelListBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBHelper
    private lateinit var travelAdapter: TravelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())

        travelAdapter = TravelAdapter(emptyList()) { travel ->
            showDeleteDialog(travel)
        }

        binding.rvTravelList.apply {
            adapter = travelAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddEditActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val latestList = dbHelper.getAllTravel()
        travelAdapter.updateData(latestList)
    }

    private fun showDeleteDialog(travel: Travel) {
        AlertDialog.Builder(requireContext())
            .setTitle("기록 삭제")
            .setMessage("[${travel.place}] 여행 기록을 정말 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                val result = dbHelper.deleteTravel(travel.no)
                if (result > 0) {
                    Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    refreshList()
                } else {
                    Toast.makeText(requireContext(), "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
                .setNegativeButton("취소", null)
                .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}