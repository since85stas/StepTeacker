package stas.batura.stepteacker.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
//import stas.batura.stepteacker.data.room.Step
import stas.batura.stepteacker.databinding.PressureItemBinding

//class PressureAdapter ():
//    ListAdapter<Step, PressureAdapter.ViewHolder> (TrackDiffCalback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class ViewHolder (val binding: PressureItemBinding) : RecyclerView.ViewHolder (binding.root) {
//
//        fun bind (step: Step) {
//            binding.pressure = step
////            binding.viewHolder = this
//            binding.executePendingBindings()
//        }
//
//        fun onItemClicked () {
//
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = PressureItemBinding.inflate(layoutInflater,
//                        parent,
//                        false)
//                return ViewHolder(binding)
//            }
//        }
//    }
//
//
//    class TrackDiffCalback : DiffUtil.ItemCallback<Step> (){
//
//        override fun areItemsTheSame(
//            oldItem: Step,
//            newItem: Step
//        ): Boolean {
//            return oldItem.equals(newItem)
//        }
//
//        override fun areContentsTheSame(
//            oldItem: Step,
//            newItem: Step
//        ): Boolean {
//            return  oldItem == newItem
//        }
//    }
//
//
//
//}