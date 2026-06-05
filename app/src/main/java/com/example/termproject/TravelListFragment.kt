package com.example.termproject

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.termproject.databinding.FragmentTravelListBinding

class TravelListFragment : Fragment() {
    private var _binding: FragmentTravelListBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBHelper
    private lateinit var travelAdapter: TravelAdapter

    private var isSortAsc = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())

        travelAdapter = TravelAdapter(
            travelList = emptyList(),
            onItemClick = { travel ->
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("TRAVEL_NO", travel.no)
                    putExtra("TRAVEL_PLACE", travel.place)
                    putExtra("TRAVEL_DATE", travel.visitDate)
                    putExtra("TRAVEL_MEMO", travel.memo)
                    putExtra("TRAVEL_PHOTO", travel.photoUri)
                }
                startActivity(intent)
            },
            onItemLongClick = { travel ->
                showDeleteDialog(travel)
            }
        )

        binding.rvTravelList.apply {
            adapter = travelAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddEditActivity::class.java)
            startActivity(intent)
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.option_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort -> {
                        isSortAsc = !isSortAsc
                        refreshList()
                        val msg = if (isSortAsc) "과거순으로 정렬했습니다." else "최신순으로 정렬했습니다."
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_delete_all -> {
                        showDeleteAllDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val latestList = dbHelper.getAllTravel(isSortAsc)
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

    private fun showDeleteAllDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("전체 삭제")
            .setMessage("모든 여행 기록을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.")
            .setPositiveButton("전체 삭제") { _, _ ->
                dbHelper.deleteAllTravel()
                Toast.makeText(requireContext(), "모든 기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                refreshList()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}