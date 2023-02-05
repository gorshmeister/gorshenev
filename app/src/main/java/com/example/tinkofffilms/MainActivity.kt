package com.example.tinkofffilms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.tinkofffilms.data.database.AppDataBase
import com.example.tinkofffilms.utils.setUpEdgeToEdge

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setUpEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppDataBase.initInstance(applicationContext)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        if (savedInstanceState != null) {
            navController.navigate(R.id.filmsFragment)
        }
    }
}