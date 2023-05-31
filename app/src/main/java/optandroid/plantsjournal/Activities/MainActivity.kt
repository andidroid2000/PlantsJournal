package optandroid.plantsjournal.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import optandroid.plantsjournal.R
import optandroid.plantsjournal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navCotroller : NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = FirebaseAuth.getInstance()

        verifyUserIsLoggedIn()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navCotroller = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        setupWithNavController(bottomNavigationView, navCotroller)

        binding.logOutButton.setOnClickListener{
            user.signOut()
            restartApp()
        }
    }

    private fun verifyUserIsLoggedIn() {
        val uid = user.uid
        if (uid == null) {
            restartApp()
        }
    }

    private fun restartApp() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}