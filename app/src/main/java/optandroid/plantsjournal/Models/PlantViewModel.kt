package optandroid.plantsjournal.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import optandroid.plantsjournal.Repository.PlantsRepository

class PlantViewModel : ViewModel() {

    private val repository : PlantsRepository
    private val _allPlants = MutableLiveData<List<PlantModel>>()
    val allPlants : LiveData<List<PlantModel>> = _allPlants

    init {
        repository = PlantsRepository().getInstance()
        repository.loadPlants(_allPlants)
    }
}