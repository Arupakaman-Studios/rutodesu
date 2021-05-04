package com.arupakaman.rutodesu.uiModules.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arupakaman.rutodesu.databinding.ItemTestInfoLayoutBinding
import com.arupakaman.rutodesu.uiModules.base.BaseRecyclerViewAdapter
import com.arupakaman.rutodesu.utils.htmlText

class AdapterTestInfo : BaseRecyclerViewAdapter<AdapterTestInfo.MyViewHolder, Pair<String, String>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(ItemTestInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MyViewHolder(private val mBinding: ItemTestInfoLayoutBinding)
        : RecyclerView.ViewHolder(mBinding.root){

        fun bind(pos: Int) = with(mBinding.tvItemInfo){
            getItem(pos)?.run {
                htmlText = "<b>$first : </b> $second"
            }
        }

    }

}