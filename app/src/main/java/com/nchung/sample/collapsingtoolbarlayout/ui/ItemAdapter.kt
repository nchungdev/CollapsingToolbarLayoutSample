package com.nchung.sample.collapsingtoolbarlayout.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nchung.sample.collapsingtoolbarlayout.R
import com.nchung.sample.collapsingtoolbarlayout.model.Item

class ItemAdapter(private val list: List<Item>, private val isHorizontal: Boolean = true) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (isHorizontal) R.layout.item_item_horizontal else R.layout.item_item, parent, false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ViewHolder) {
            p0.bindData(list[p1])
        }
    }

    fun updateItemsAlphaValue(alpha: Float) {
        list.map { it.alpha = alpha }
        notifyDataSetChanged()
        Log.e("Scroll", "alpha $alpha")
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val margin16 = itemView.resources.getDimensionPixelSize(R.dimen.margin16)
        private val margin4 = itemView.resources.getDimensionPixelSize(R.dimen.margin4)
        private val textView by lazy { itemView.findViewById<TextView>(R.id.text_title) }
        private val textInside by lazy { itemView.findViewById<TextView>(R.id.text_inside) }

        fun bindData(item: Item) {
            textView?.text = item.title
            textInside?.text = item.title
            if (!isHorizontal) return
            val marginStart = if (adapterPosition == 0) margin16 else margin4
            val marginEnd = if (adapterPosition == itemCount - 1) 0 else margin4
            val layoutParams = itemView.layoutParams as RecyclerView.LayoutParams
            layoutParams.marginStart = marginStart
            layoutParams.marginEnd = marginEnd
            itemView.layoutParams = layoutParams
            textInside.alpha = item.alpha
            textView.alpha = 1 - item.alpha
        }
    }
}