package optandroid.plantsjournal.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import optandroid.plantsjournal.databinding.ActivityRegisterBinding
import java.util.UUID

class User (val uid: String, val username: String, val profileImageUrl: String)

class RegisterActivity : ComponentActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        auth = Firebase.auth

        //  Select user profile photo
        binding.insertPhotoButtonRegister.setOnClickListener{
            Log.d("RegisterActivity", "Try to insert photo!")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        //  Register the user
        binding.registerButtonRegister.setOnClickListener{
            performRegister()
        }

        //  Launch login activity
        binding.loginTextviewRegister.setOnClickListener{
            Log.d("RegisterActivity", "Switch to login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            binding.insertPhotoImageviewRegister.setImageBitmap(bitmap)
            binding.insertPhotoButtonRegister.alpha = 0f
        }
    }

    private fun performRegister() {

        val username = binding.usernameEdittextRegister.text.toString()
        val email = binding.emailEdittextRegister.text.toString()
        val password = binding.passwordEdittextRegister.text.toString()

        //  Check if the data was provided by the user
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Provide an email/password!", Toast.LENGTH_SHORT).show()
            Log.d("RegisterActivity", "registerPrecheck:FAILURE - EMPTY EMAIL OR PASSWORD")
            return
        }

        //  Register the user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    uploadImageToFirebase()
                    Log.d("RegisterActivity", "createUserWithEmailAndPassword:SUCCESS")
                } else {
                    Log.d("RegisterActivity", "createUserWithEmailAndPassword:FAILURE - CREATE")
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener(this) { task ->
                Log.d("RegisterActivity", "createUserWithEmailAndPassword:FAILURE - " + task.message)
                Toast.makeText(this, task.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebase() {
        if (selectedPhotoUri == null)
        {
            Log.d("RegisterActivity", "uploadImageToFirebase: Photo was not loaded!")
            return
        } else {
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$uid/profilePic/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "uploadImageToFirebase: Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity", "File Location: $it")
                        saveUserToFirebase(it.toString())
                    }
                }
                .addOnFailureListener {task ->
                    Log.d("RegisterActivity", "uploadImageToFirebase: - " + task.message)
                }
        }
    }

    private fun saveUserToFirebase(profileImageUrl: String) {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/profile")
        val username = binding.usernameEdittextRegister.text.toString()
        val user = User(uid, username, profileImageUrl)
        Log.d("RegisterActivity", "UID: ${user.uid} ${user.username} ${user.profileImageUrl}")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "saveUserToFirebase: SUCCESS")

                Toast.makeText(this, "User created successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to set value to database: ${it.message}")
            }
    }
}