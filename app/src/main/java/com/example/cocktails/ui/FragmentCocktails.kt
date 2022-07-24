package com.example.cocktails.ui


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktails.R
import com.example.cocktails.adapter.CocktailsAdapter
import com.example.cocktails.databinding.FragmentCocktailsBinding
import com.example.cocktails.model.CocktailModel
import com.example.cocktails.viewmodel.CocktailsViewModel
import dagger.hilt.android.AndroidEntryPoint

const val KEY = "key"
const val ID = "id"

@AndroidEntryPoint
class FragmentCocktails : Fragment(), CocktailsAdapter.clickListener {
    private var _binding: FragmentCocktailsBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CocktailsAdapter
    private val viewModel: CocktailsViewModel by viewModels()
    private val list: MutableList<CocktailModel> = ArrayList()
    var c: Char = 'a'

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCocktailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putChar("Alphabet", c)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadFirst(c)
        c++
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            c = savedInstanceState.getChar("Alphabet")
        }
        initRecyclerView()
        binding?.recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when {
                    (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_DRAGGING) -> {
                        viewModel.loadFirst(c)
                        c++
                    }
                }
            }
        })
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchItem: MenuItem = menu.findItem(R.id.action_search)
                val searchView: SearchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        filter(newText)
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })
    }

    private fun initRecyclerView() = with(binding)
    {
        this?.recycler?.layoutManager = GridLayoutManager(context, 2)
        adapter = CocktailsAdapter(this@FragmentCocktails)
        this?.recycler?.adapter = adapter
        viewModel.cocktails.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            list.addAll(viewModel.cocktails.value!!)
        })
    }

    private fun filter(text: String) {
        val filteredlist: ArrayList<CocktailModel> = ArrayList()
        // running a for loop to compare elements.
        for (item in list) {
            if (item.name!!.lowercase().contains(text.lowercase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Log.d("NoData", "No Data Found")
        } else {
            adapter.submitList(filteredlist)
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

