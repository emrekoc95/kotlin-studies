package com.kocemre.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kocemre.kotlincountries.databinding.FragmentFeedBinding
import com.kocemre.kotlincountries.databinding.ItemCountryBinding
import com.kocemre.kotlincountries.model.Country
import com.kocemre.kotlincountries.util.downloadUrl
import com.kocemre.kotlincountries.util.placeholderProgressBar
import com.kocemre.kotlincountries.view.FeedFragmentDirections

class CountryAdapter (val countryList: ArrayList<Country>) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder (val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.binding.name.text = countryList.get(position).countryName
        holder.binding.region.text = countryList.get(position).countryRegion

        holder.itemView.setOnClickListener(){
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.downloadUrl(countryList.get(position).imageUrl,
            placeholderProgressBar(holder.itemView.context))

    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountryList : List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}