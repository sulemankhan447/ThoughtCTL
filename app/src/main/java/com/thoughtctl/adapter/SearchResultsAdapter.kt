package com.thoughtctl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thoughtctl.R
import com.thoughtctl.databinding.ItemGridBinding
import com.thoughtctl.databinding.ItemListBinding
import com.thoughtctl.model.ImgurModel

class SearchResultsAdapter(
    val context: Context,
    var results: List<ImgurModel>,
    var showListView: Boolean = true   //boolean flag to show grid or list ui
) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    companion object {
        const val LIST_VIEW = 1
        const val GRID_VIEW = 2
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var itemListView: ItemListBinding? = null
        var itemGridView: ItemGridBinding? = null

        constructor(binding: ItemListBinding) : super(binding.root) {
            itemListView = binding
        }

        constructor(binding: ItemGridBinding) : super(binding.root) {
            itemGridView = binding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (showListView) {
            return ViewHolder(
                DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_list,
                    parent,
                    false
                ) as ItemListBinding
            )
        } else {
            return ViewHolder(
                DataBindingUtil.inflate(
                    inflater, R.layout.item_grid, parent, false
                ) as ItemGridBinding
            )
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = results[position]
        if (holder.itemViewType == LIST_VIEW) {
            holder.itemListView?.tvTitle?.text = data.title
            holder.itemListView?.tvDatePosted?.text = data.images?.first()?.datetime.toString()
            holder.itemListView?.tvAdditionalImagesCount?.text =
                context.getString(R.string.more_images_available, data.images_count)
        } else {
            holder.itemGridView?.tvTitle?.text = data.title
            holder.itemGridView?.tvDatePosted?.text = data.images?.first()?.datetime.toString()
            holder.itemGridView?.tvAdditionalImagesCount?.text =
                context.getString(R.string.more_images_available, data.images_count)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (showListView) LIST_VIEW else GRID_VIEW
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun refreshData(results: List<ImgurModel>) {
        this.results = results
        notifyDataSetChanged()
    }
}