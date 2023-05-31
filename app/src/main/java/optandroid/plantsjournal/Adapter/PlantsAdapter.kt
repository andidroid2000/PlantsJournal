package optandroid.plantsjournal.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import optandroid.plantsjournal.Models.PlantModel
import optandroid.plantsjournal.R

//  Load plants into cards in Recyclerview

class PlantsAdapter : RecyclerView.Adapter<PlantsAdapter.MyViewHolder>() {

    private val plantsList = ArrayList<PlantModel>()

    //  Use card layout to extract plant info
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.plant_layout,
            parent,false
        )
        return MyViewHolder(itemView)

    }
    //  Extract the info from each plant
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = plantsList[position]

        holder.name.text = currentitem.name
        holder.purchaseDate.text = currentitem.purchaseDate
        holder.description.text = currentitem.description
        Picasso.get().load(currentitem.imageURL).into(holder.image)

    }

    override fun getItemCount(): Int {
        return plantsList.size
    }

    fun updatePlantsList(plantsList : List<PlantModel>){

        this.plantsList.clear()
        this.plantsList.addAll(plantsList)
        notifyDataSetChanged()

    }

    class  MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.plantName)
        val purchaseDate : TextView = itemView.findViewById(R.id.plantPurchaseDate)
        val description : TextView = itemView.findViewById(R.id.plantDescription)
        val image : ImageView = itemView.findViewById(R.id.plantImage)

    }

}