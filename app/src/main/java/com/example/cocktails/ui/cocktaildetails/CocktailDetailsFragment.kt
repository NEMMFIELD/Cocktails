package com.example.cocktails.ui.cocktaildetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.cocktails.databinding.CocktailDetailsFragmentBinding
import com.example.cocktails.network.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CocktailDetailsFragment : Fragment() {
    private val binding: CocktailDetailsFragmentBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: SelectedCocktailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.cocktailDetails.collect { state ->
                    when (state) {
                        is ApiState.Success -> {
                            with(binding)
                            {
                                nameCocktailDetails.text = state.data.name
                                imageCocktailDetails.load(state.data.imgPath)
                                instructionCocktailDetails.text = state.data.recipe
                            }
                            binding.shareId.setOnClickListener {
                                lifecycleScope.launch {
                                    viewModel.shareCocktail(requireContext(), state.data)
                                }
                            }
                        }
                        is ApiState.Failure -> {
                            Log.d("TagError", "On Create ${state.message}")
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}