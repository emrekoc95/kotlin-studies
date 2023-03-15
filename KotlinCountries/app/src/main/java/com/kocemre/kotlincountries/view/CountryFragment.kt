package com.kocemre.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kocemre.kotlincountries.databinding.FragmentCountryBinding
import com.kocemre.kotlincountries.viewmodel.CountryViewModel


class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CountryViewModel

    private var countryUuid = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom()

        arguments?.let{
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
        }

        observeLiveData()

    }

    private fun observeLiveData(){

        viewModel.countryData.observe(viewLifecycleOwner, Observer { country->
            country?.let {
                binding.countryName.text = it.countryName
                binding.countryCapital.text = it.countryCapital
                binding.countryRegion.text = it.countryRegion
                binding.countryCurrency.text = it.countryCurrency
                binding.countryLanguage.text = it.countryLanguage
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}