package com.rojoxpress.gbmtest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rojoxpress.gbmtest.R
import com.rojoxpress.gbmtest.databinding.ItemTopBinding
import com.rojoxpress.gbmtest.model.Top

class TopTenAdapter(context: Context, private val data: List<Top>): RecyclerView.Adapter<TopTenAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_top, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTop(data[position])
    }

    override fun getItemCount() = data.size

    class ViewHolder(private val binding: ItemTopBinding): RecyclerView.ViewHolder(binding.root) {

        fun setTop(top: Top) {
            binding.top =top
        }
    }

}