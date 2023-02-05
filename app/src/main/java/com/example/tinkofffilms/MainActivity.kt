package com.example.tinkofffilms

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tinkofffilms.data.database.AppDataBase
import com.example.tinkofffilms.ui.film_details.FilmDetailsFragment
import com.example.tinkofffilms.ui.films.FilmsFragment
import com.example.tinkofffilms.utils.setUpEdgeToEdge


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setUpEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppDataBase.initInstance(applicationContext)
        if (supportFragmentManager.findFragmentByTag(FilmsFragment.TAG) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_list, FilmsFragment(), FilmsFragment.TAG)
                .commit()
        }
        supportFragmentManager.setFragmentResultListener(FRAGMENT_RESULT_KEY, this) { _, result ->
            val filmId = result.getInt(FilmsFragment.ID, 0)
            val detailsFragment = FilmDetailsFragment.newInstance(filmId)
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_list, detailsFragment, FilmDetailsFragment.TAG)
                    .addToBackStack(null)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_details, detailsFragment, FilmDetailsFragment.TAG)
                    .commit()
            }
        }
    }

    companion object {
        const val FRAGMENT_RESULT_KEY = "FRAGMENT_RESULT_KEY"
    }
}