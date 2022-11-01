package com.andflube.storyapp.ui.mapActivity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.andflube.storyapp.R
import com.andflube.storyapp.ViewModelFactory
import com.andflube.storyapp.databinding.ActivityMapsBinding
import com.andflube.storyapp.model.Story
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.network.ResultResponse


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mapsViewModel: MapsViewModel

    private lateinit var preference: UserPreference
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Maps"

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        preference = UserPreference(this)

        setupViewModel()




    }

    private fun setupViewModel() {
        mapsViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val indonesian = LatLng(-6.2000000, 106.816666)
        mMap.addMarker(MarkerOptions().position(indonesian).title("Indonesia"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indonesian))

        getMyLocation()

        token = preference.token
        mapsViewModel.getLocation("Bearer $token")
            .observe(this) {
                when(it){
                    is ResultResponse.Loading -> {
                        binding.progressBarMaps.visibility = View.VISIBLE
                    }
                    is ResultResponse.Success -> {
                        binding.progressBarMaps.visibility = View.GONE
                        val data = it.data.listStory
                        addStoriesMarkers(data)

                    }
                    is ResultResponse.Error -> {
                        binding.progressBarMaps.visibility = View.GONE
                    }
                }
            }
    }



    private fun addStoriesMarkers(storiesList: List<Story>) {
        for (story in storiesList) {
            val latLng = LatLng(
                story.lat,
                story.lon
            )

            val marker = MarkerOptions()
                .position(latLng)
                .title(story.name)
                .snippet(story.description)

            mMap.addMarker(marker)
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}