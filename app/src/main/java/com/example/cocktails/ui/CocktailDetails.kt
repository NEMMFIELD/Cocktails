package com.example.cocktails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.example.cocktails.databinding.FragmentCocktailDetailsBinding
import com.example.cocktails.viewmodel.CocktailsViewModel
import dagger.hilt.android.AndroidEntryPoint


//private const val KEY = "key"
//private const val ID = "id"
@AndroidEntryPoint
class CocktailDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var count: Int? = null
    private var _binding: FragmentCocktailDetailsBinding? = null
    private val viewModel: CocktailsViewModel by viewModels()
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)
            count = it.getInt(KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCocktailDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnBack?.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.loadSelectedCocktail(id)
        viewModel.cocktail.observe(viewLifecycleOwner, Observer {
            binding?.nameCocktailDetails?.text = it.name
            binding?.imageCocktailDetails?.load(it.imgPath)
            binding?.instructionCocktailDetails?.text = it.recipe
        })
    }
}