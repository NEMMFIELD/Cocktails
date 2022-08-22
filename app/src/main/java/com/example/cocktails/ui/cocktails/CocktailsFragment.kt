package com.example.cocktails.ui.cocktails


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cocktails.R
import com.example.cocktails.databinding.CocktailsFragmentBinding
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.network.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CocktailsFragment : Fragment(R.layout.cocktails_fragment), CocktailsAdapter.clickListener,
    CocktailsAdapter.likeListener {
    companion object {
        const val FAST_SEARCH_KEY = "fsk"
        const val TIME_FOR_PAGING = 2900L // Время осуществления пагинации
    }

    private val viewBinding: CocktailsFragmentBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private lateinit var adapter: CocktailsAdapter
    private val viewModel: CocktailsViewModel by viewModels()
    private var searchingText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FAST_SEARCH_KEY, searchingText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        searchingText = savedInstanceState?.getString(FAST_SEARCH_KEY).toString()
    }

    override fun onStart() {
        super.onStart()
        with(viewBinding)
        {
            progressBar?.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        with(viewBinding)
        {
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when {
                        (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_DRAGGING) -> {
                            progressBar?.visibility = View.VISIBLE
                            viewModel.loadCocktails()
                            lifecycleScope.launch {
                                delay(TIME_FOR_PAGING)
                                progressBar?.visibility = View.GONE
                            }
                        }
                    }
                }
            })
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchItem: MenuItem = menu.findItem(R.id.action_search)
                val searchView: SearchView = searchItem.actionView as SearchView
                if (savedInstanceState == null) searchingText = ""

                searchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        viewModel.queryTextChangedJob?.cancel()
                        viewModel.queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main)
                        {
                            searchingText = newText
                            viewModel.searchInList(searchingText, viewModel.cocktails)
                        }
                        return false
                    }
                })
                searchView.requestFocus()
                searchView.setQuery(searchingText, false)
                viewModel.searchInList(searchingText, viewModel.cocktails)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun initRecyclerView() = with(viewBinding)
    {
        val spanCount = if (activity?.resources?.configuration?.orientation !=
            Configuration.ORIENTATION_PORTRAIT
        ) 3 else 2

        recycler.layoutManager = GridLayoutManager(context, spanCount)
        adapter = CocktailsAdapter(this@CocktailsFragment, this@CocktailsFragment)
        recycler.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.postStateFlow.collect { state ->
                    when (state) {
                        is ApiState.Success -> {
                            recycler.isVisible = true
                            adapter.submitList(state.data.toMutableList())
                        }
                        is ApiState.Failure -> {
                            recycler.isVisible = false
                            Log.d("TagError", "On Create ${state.message}")
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onLike(cocktail: CocktailModel, position: Int) {
        cocktail.isLiked = cocktail.isLiked == false
        viewModel.setLikeByViewModel(cocktail)
        adapter.notifyItemChanged(position, "Name")
    }

    override fun onItemClick(cocktail: CocktailModel) {
        val action = CocktailsFragmentDirections.actionCocktailsFragmentToCocktailDetailsFragment(
            cocktail.id ?: "0"
        )
        findNavController().navigate(action)
    }
}



