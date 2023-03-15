package com.kocemre.kotlincountries.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kocemre.kotlincountries.adapter.CountryAdapter
import com.kocemre.kotlincountries.databinding.FragmentFeedBinding
import com.kocemre.kotlincountries.viewmodel.FeedViewModel


class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : FeedViewModel
    private var countryAdapter = CountryAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData()


        binding.countryList.layoutManager = LinearLayoutManager(context)
        binding.countryList.adapter = countryAdapter

        observeLiveData()

    }

    private fun observeLiveData(){

        viewModel.countries.observe(viewLifecycleOwner, Observer { countries->
            countries?.let {
                countryAdapter.updateCountryList(it)
                binding.countryList.visibility = View.VISIBLE
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { countryError ->
            countryError?.let {
                if(it){
                    binding.countryError.visibility = View.VISIBLE
                    binding.countryList.visibility = View.GONE
                    binding.countryLoading.visibility = View.GONE
                }
                else{
                    binding.countryError.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { countryLoading->
            countryLoading?.let {
                if(it){
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.countryError.visibility = View.GONE
                    binding.countryList.visibility = View.GONE
                }
                else{
                    binding.countryLoading.visibility = View.GONE
                }
            }
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}