package com.kocemre.retrofitkotlin.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kocemre.retrofitkotlin.databinding.LayoutRowBinding
import com.kocemre.retrofitkotlin.model.CryptoModel

class CryptoAdapter(val cryptoModels: ArrayList<CryptoModel>) : RecyclerView.Adapter<CryptoAdapter.RowHolder>() {
    val colors : ArrayList<String> = arrayListOf("#800000",	"#FF0000","#800080" ,"#FF00FF")
    class RowHolder(val binding: LayoutRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val binding = LayoutRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RowHolder(binding)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.binding.currencyText.text = cryptoModels.get(position).currency
        holder.binding.priceText.text = cryptoModels.get(position).price
        holder.itemView.setBackgroundColor(Color.parseColor(colors.get(position%4)))
    }

    override fun getItemCount(): Int {
        return cryptoModels.size
    }
}