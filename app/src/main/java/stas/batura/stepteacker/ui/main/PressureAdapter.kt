package stas.batura.stepteacker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stas.batura.stepteacker.data.room.Pressure
import stas.batura.pressuretracker.databinding.PressureItemBinding

class PressureAdapter ():
    ListAdapter<Pressure, PressureAdapter.ViewHolder> (TrackDiffCalback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder (val binding: PressureItemBinding ) : RecyclerView.ViewHolder (binding.root) {

        fun bind (pressure: Pressure) {
            binding.pressure = pressure
//            binding.viewHolder = this
            binding.executePendingBindings()
        }

        fun onItemClicked () {

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PressureItemBinding.inflate(layoutInflater,
                        parent,
                        false)
                return ViewHolder(binding)
            }
        }
    }


    class TrackDiffCalback : DiffUtil.ItemCallback<Pressure> (){

        override fun areItemsTheSame(
            oldItem: Pressure,
            newItem: Pressure
        ): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(
            oldItem: Pressure,
            newItem: Pressure
        ): Boolean {
            return  oldItem == newItem
        }
    }



}