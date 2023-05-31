package optandroid.plantsjournal.Fragments

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import optandroid.plantsjournal.Models.PlantModel
import optandroid.plantsjournal.R
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddPlantFragment : Fragment(), View.OnClickListener {

    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_plant, container, false)

        val uid = FirebaseAuth.getInstance().uid ?: ""
        dbRef = FirebaseDatabase.getInstance().getReference("/users/$uid/plants")

        val takePictureAddPlant = view.findViewById<Button>(R.id.takePicture_addPlant)
        val uploadPhotoAddPlant = view.findViewById<Button>(R.id.uploadPhoto_addPlant)
        val uploadPlantAddPlant = view.findViewById<Button>(R.id.uploadPlant_addPlant)

        takePictureAddPlant.setOnClickListener(this)
        uploadPlantAddPlant.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.takePicture_addPlant -> {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, 100)
                Log.d("AddPlantActivity", "ACTIUNE BUTON")
            }
            R.id.uploadPlant_addPlant -> {
                val btn = view?.findViewById<Button>(R.id.uploadPlant_addPlant)
                ObjectAnimator.ofFloat(btn, View.TRANSLATION_X, 200f).let{
                    it.repeatCount = 1
                    it.repeatMode = ObjectAnimator.REVERSE
                    it.start()
                }
                uploadPlantImageToStorage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("AddPlantActivity", "FACE POZA")
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imgView = view?.findViewById<ImageView>(R.id.imageView3)
            imgView?.setImageBitmap(imageBitmap)
            selectedPhotoUri = getImageUri(requireContext(). applicationContext, imageBitmap)
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage,
            "Title", null
        )
        return Uri.parse(path)
    }

    private fun uploadPlantImageToStorage()
    {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$uid/plants/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnCompleteListener(){
                ref.downloadUrl.addOnSuccessListener {
                    savePlantToFirebase(it.toString())
                }
            }
    }

    private fun savePlantToFirebase(imageURL: String) {

        val name = view?.findViewById<EditText>(R.id.name_addPlant)?.text.toString()
        val rawPurchaseDate = view?.findViewById<DatePicker>(R.id.datePicker_addPlant)
        val purchaseDay = rawPurchaseDate?.dayOfMonth.toString()
        val purchaseMonth= (rawPurchaseDate!!.month+1).toString()
        val purchaseYear = rawPurchaseDate?.year.toString()
        val purchaseDate = "$purchaseDay/$purchaseMonth/$purchaseYear"
        val purchasePlace = view?.findViewById<EditText>(R.id.purchasePlace_addPlant)?.text.toString()
        val plantSize = view?.findViewById<EditText>(R.id.size_addPlant)?.text.toString()
        val description = view?.findViewById<EditText>(R.id.description_addPlant)?.text.toString()

        val plantId = dbRef.push().key!!
        val plant = PlantModel(description, plantId, imageURL, name, plantSize, purchaseDate, purchasePlace)

        dbRef.child(plantId).setValue(plant)
            .addOnCompleteListener{
                Log.d("AddPlantActivity", "savePlantToFirebase: SUCCESS")
            }
            .addOnFailureListener{
                Log.d("AddPlantActivity", "savePlantToFirebase: FAILURE")
            }
    }

}