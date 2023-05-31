package optandroid.plantsjournal.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import optandroid.plantsjournal.Adapter.PlantsAdapter
import optandroid.plantsjournal.Models.PlantViewModel
import optandroid.plantsjournal.R

private lateinit var viewModel : PlantViewModel
private lateinit var plantsRecyclerView: RecyclerView
lateinit var adapter : PlantsAdapter

class PlantsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plants_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantsRecyclerView = view.findViewById(R.id.recyclerView)
        plantsRecyclerView.layoutManager = LinearLayoutManager(context)
        plantsRecyclerView.setHasFixedSize(true)
        adapter = PlantsAdapter()
        plantsRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        viewModel.allPlants.observe(viewLifecycleOwner, Observer {
            adapter.updatePlantsList(it)
        })
    }
}