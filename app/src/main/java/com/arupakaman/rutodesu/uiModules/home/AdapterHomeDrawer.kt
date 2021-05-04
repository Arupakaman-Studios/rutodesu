package com.arupakaman.rutodesu.uiModules.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arupakaman.rutodesu.databinding.ItemHomeDrawerLayoutBinding
import com.arupakaman.rutodesu.uiModules.base.BaseRecyclerViewAdapter
import com.arupakaman.rutodesu.utils.*

class AdapterHomeDrawer(items: List<String>, private val onClick: (Int) -> Unit)
    : BaseRecyclerViewAdapter<AdapterHomeDrawer.MyViewHolder, String>() {

    init {
        mItemsList = items
    }

    var selItem = 0

    fun setSelected(pos: Int = 0){
        val prevPos = selItem
        selItem = pos
        notifyItemChanged(prevPos)
        notifyItemChanged(selItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MyViewHolder(ItemHomeDrawerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MyViewHolder(private val mBinding: ItemHomeDrawerLayoutBinding)
        : RecyclerView.ViewHolder(mBinding.root){

        init {
            mBinding{
                itemRootView.setSafeOnClickListener {
                    if (selItem != bindingAdapterPosition) {
                        onClick(bindingAdapterPosition)
                    }
                }
            }
        }

        fun bind(pos: Int) = with(mBinding.tvItemDrawer){
            text = getItem(pos)?:""
            isUnderlined = (selItem == bindingAdapterPosition)
        }

    }

}