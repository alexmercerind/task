package com.alexmercerind.envirocar.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alexmercerind.envirocar.data.remote.envirocar.dto.Feature
import com.alexmercerind.envirocar.data.remote.envirocar.dto.SensorProperties
import com.alexmercerind.envirocar.repository.EnviroCarRepository
import com.alexmercerind.envirocar.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: EnviroCarRepository,
    private val application: Application
) : AndroidViewModel(application) {
    private val _sensor: MutableStateFlow<SensorProperties?> = MutableStateFlow(null)
    val sensor = _sensor.asStateFlow()

    private val _points = MutableStateFlow<List<Feature?>>(emptyList())
    val points = _points.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val response = repository.track(
                    Constants.USERNAME,
                    Constants.TRACK_ID,
                    Constants.USERNAME,
                    Constants.TOKEN
                )
                _sensor.value = response.properties.sensor.properties
                _points.value = response.features
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(application, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
