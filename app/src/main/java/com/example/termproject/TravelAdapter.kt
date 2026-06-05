package com.example.termproject

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.termproject.databinding.ItemTravelBinding

class TravelAdapter (
    private var travelList: List<Travel>,
    private val onItemClick: (Travel) -> Unit,
    private  val onItemLongClick: (Travel) -> Unit
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val binding = ItemTravelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        holder.bind(travelList[position])
    }

    override fun getItemCount(): Int = travelList.size

    fun updateData(newList: List<Travel>) {
        this.travelList = newList
        notifyDataSetChanged()
    }

    inner class TravelViewHolder(private val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(travel: Travel) {
            binding.tvItemPlace.text = travel.place
            binding.tvItemDate.text = travel.visitDate
            binding.tvItemMemo.text = travel.memo

            if (travel.photoUri.isNotEmpty()) {
                binding.ivItemPhoto.setImageURI(Uri.parse(travel.photoUri))
            } else {
                binding.ivItemPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.root.setOnClickListener {
                onItemClick(travel)
            }

            binding.root.setOnLongClickListener {
                onItemLongClick(travel)
                true
            }
        }
    }
}