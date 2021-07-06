package stas.batura.stepteacker.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stas.batura.stepteacker.databinding.RainItemBinding

//class RainAdapter ():
//        ListAdapter<Rain, RainAdapter.ViewHolder>(TrackDiffCalback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class ViewHolder (val binding: RainItemBinding) : RecyclerView.ViewHolder (binding.root) {
//
//        fun bind (rain: Rain) {
//            binding.rain = rain
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
//                val binding = RainItemBinding.inflate(layoutInflater,
//                        parent,
//                        false)
//                return ViewHolder(binding)
//            }
//        }
//    }
//
//
//    class TrackDiffCalback : DiffUtil.ItemCallback<Rain> (){
//
//        override fun areItemsTheSame(
//            oldItem: Rain,
//            newItem: Rain
//        ): Boolean {
//            return oldItem.equals(newItem)
//        }
//
//        override fun areContentsTheSame(
//            oldItem: Rain,
//            newItem: Rain
//        ): Boolean {
//            return  oldItem == newItem
//        }
//    }
//
//
//
//}