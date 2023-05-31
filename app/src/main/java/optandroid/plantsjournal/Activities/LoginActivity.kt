package optandroid.plantsjournal.Activities

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import optandroid.plantsjournal.Others.BuildNotificationChannel
import optandroid.plantsjournal.R
import optandroid.plantsjournal.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        auth = Firebase.auth

        //  Click Login button -> User logs in
        binding.loginButtonLogin.setOnClickListener{
            performLogin()
        }

        //  Return to Register Activity
        binding.registerTextviewLogin.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {

        val email = binding.emailEdittextLogin.text.toString()
        var password = binding.passwordEdittextLogin.text.toString()

        //  Check if the data was provided by the user
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Provide an email/password!", Toast.LENGTH_SHORT).show()
            Log.d("LoginActivity", "loginPrecheck:FAILURE - EMPTY EMAIL OR PASSWORD")
            return
        }

        //  Login the user
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "signInWithEmailAndPassword:SUCCESS")
                    Toast.makeText(this, "User logged in successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    //  Send user a notification
                    sendNotification()
                    startActivity(intent)
                } else {
                    Log.d("LoginActivity", "signInWithEmailAndPassword:FAILURE - CREATE")
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener(this) { task ->
                Log.d("LoginActivity", "signInWithEmailAndPassword:FAILURE - " + task.message)
                Toast.makeText(this, task.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun sendNotification() {
        val activityIntent = Intent(this, MainActivity::class.java)
        val activityPending = PendingIntent.getActivity(
            this,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        BuildNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Welcome back to PlantsJournal")
            .setContentText("Don't forget to update you with your latest stories about your plants!")
            .setSmallIcon(R.drawable.plants_journal_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(activityPending)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}