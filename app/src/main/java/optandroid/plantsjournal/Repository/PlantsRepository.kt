package optandroid.plantsjournal.Repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import optandroid.plantsjournal.Models.PlantModel

class PlantsRepository {
    private val uid = FirebaseAuth.getInstance().uid
    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("users/$uid/plants")

    @Volatile private var INSTANCE : PlantsRepository ?= null

    fun getInstance() : PlantsRepository {
        return INSTANCE ?: synchronized(this) {

            val instance = PlantsRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadPlants(plantsList : MutableLiveData<List<PlantModel>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _plantsList : List <PlantModel> = snapshot.children.map {dataSnapshot ->
                        dataSnapshot.getValue(PlantModel::class.java)!!
                    }

                plantsList.postValue(_plantsList)

                } catch (e : Exception) {

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

}