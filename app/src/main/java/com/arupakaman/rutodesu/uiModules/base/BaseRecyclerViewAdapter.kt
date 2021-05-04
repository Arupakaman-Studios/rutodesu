package com.arupakaman.rutodesu.uiModules.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<VH : RecyclerView.ViewHolder, M : Any> : RecyclerView.Adapter<VH>() {

    var mItemsList: List<M> = emptyList()

    fun submit(list: List<M>){
        mItemsList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = mItemsList.size

    fun getItem(position: Int) = mItemsList.getOrNull(position)

}