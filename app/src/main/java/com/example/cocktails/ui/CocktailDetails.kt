package com.example.cocktails.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.cocktails.databinding.FragmentCocktailDetailsBinding
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.viewmodel.CocktailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CocktailDetails : Fragment() {
    private var id: String? = null
    private var count: Int? = null
    private var _binding: FragmentCocktailDetailsBinding? = null
    private val viewModel: CocktailsViewModel by viewModels()
    private val sharedList: MutableList<CocktailModel> = ArrayList()
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
        return binding?.root
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
            sharedList.add(it)
        })
        binding?.shareId?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.shareCocktail(requireContext(), sharedList.last())
            }
        }
    }
}
