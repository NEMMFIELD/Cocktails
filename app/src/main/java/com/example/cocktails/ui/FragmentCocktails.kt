package com.example.cocktails.ui


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktails.R
import com.example.cocktails.adapter.CocktailsAdapter
import com.example.cocktails.databinding.FragmentCocktailsBinding
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.viewmodel.CocktailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val KEY = "key"
const val ID = "id"
const val FAST_SEARCH_KEY = "fsk"

@AndroidEntryPoint
class FragmentCocktails : Fragment(), CocktailsAdapter.clickListener {
    private var _binding: FragmentCocktailsBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CocktailsAdapter
    private val viewModel: CocktailsViewModel by viewModels()
    private val list: MutableList<CocktailModel> = ArrayList()

    private var searchingText: String = ""
    private var queryTextChangedJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCocktailsBinding.inflate(inflater, container, false)
        return binding?.root
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
        viewModel.loadCocktails()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        binding?.recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when {
                    (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_DRAGGING) -> {
                        viewModel.loadCocktails()
                    }
                }
            }
        })

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
                        queryTextChangedJob?.cancel()
                        queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main)
                        {
                            delay(500)
                            searchingText = newText
                            filter(searchingText)
                        }
                        return false
                    }
                })
                searchView.requestFocus()
                searchView.setQuery(searchingText, false)
                filter(searchingText)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun initRecyclerView() = with(binding)
    {
        val spanCount = if (activity?.resources?.configuration?.orientation !=
            Configuration.ORIENTATION_PORTRAIT
        ) 3 else 2
        this?.recycler?.layoutManager = GridLayoutManager(context, spanCount)
        adapter = CocktailsAdapter(this@FragmentCocktails)
        this?.recycler?.adapter = adapter
        viewModel.cocktails.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            list.addAll(viewModel.cocktails.value!!)
        })
    }

    private fun filter(text: String) {
        val filteredlist: ArrayList<CocktailModel> = ArrayList()
        for (item in list) {
            if (item.name!!.lowercase().contains(text.lowercase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Log.d("NoData", "No Data Found")
        } else {
            val set = filteredlist.toSet()
            val newList = set.toList()
            adapter.submitList(newList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(cocktail: CocktailModel, position: Int) {
        val args = Bundle()
        args.putString(ID, cocktail.id)
        args.putInt(KEY, position)
        findNavController().navigate(
            com.example.cocktails.R.id.action_fragmentCocktails_to_cocktailDetails,
            args
        )
    }
}

