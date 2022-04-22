package com.parth8199.employeebonding

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.parse.ParseUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speedDialView = findViewById<SpeedDialView>(R.id.speedDial)
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.fab_log_out,
                R.drawable.ic_baseline_exit_to_app_24_white
            )
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.purple_200,
                        getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelColor(Color.DKGRAY)
                .setLabelBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelClickable(false)
                .setLabel("Log Out")
                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(R.id.fab_compose_screen, R.drawable.ic_baseline_create_24)
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.purple_200,
                        getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelColor(Color.DKGRAY)
                .setLabelBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelClickable(false)
                .setLabel("Create Discussion")
                .create()
        )
        speedDialView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_log_out -> {
                    ParseUser.logOut()
                    val currentUser = ParseUser.getCurrentUser()
                    if (currentUser == null) {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
                    speedDialView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
                R.id.fab_compose_screen -> {
                    val intent = Intent(this@MainActivity, ComposeActivity::class.java)
                    startActivity(intent)
                    //finish()
                    Toast.makeText(this, "Going to Compose Screen ", Toast.LENGTH_SHORT).show()
                    speedDialView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.item_logout, menu);
        return true;
    }

    fun onLogoutAction(mi: MenuItem) {
        // handle click here
        ParseUser.logOut()
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}