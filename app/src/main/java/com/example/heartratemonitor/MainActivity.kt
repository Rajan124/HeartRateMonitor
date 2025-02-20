package com.example.heartratemonitor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class MainActivity : ComponentActivity() {

    private lateinit var healthConnectClient: HealthConnectClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Health Connect client
        healthConnectClient = HealthConnectClient.getOrCreate(this)

        // Create permission launcher to request Health Connect permissions
        val permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Log.d("HealthConnect", "All required permissions granted.")
            } else {
                Log.e("HealthConnect", "Some permissions were not granted.")
            }
        }

        setContent {
            MaterialTheme {
                var heartRateHistory by remember { mutableStateOf(listOf<Pair<String, String>>()) }

                // Request permissions on app launch
                LaunchedEffect(Unit) {
                    requestPermissions(permissionsLauncher)
                }

                // UI to save and load heart rate records
                HealthConnectManager(
                    onSaveClick = { heartRate, dateTime ->
                        lifecycleScope.launch {
                            saveHeartRate(heartRate.toLong(), dateTime)
                        }
                    },
                    onLoadClick = {
                        lifecycleScope.launch {
                            heartRateHistory = loadHeartRates()
                        }
                    },
                    heartRateHistory = heartRateHistory
                )
            }
        }
    }

    /**
     * Request permissions for reading and writing health data using Health Connect API.
     */
    private fun requestPermissions(permissionsLauncher: ActivityResultLauncher<Array<String>>) {
        val permissions = arrayOf(
            HealthPermission.getReadPermission(HeartRateRecord::class).toString(),
            HealthPermission.getWritePermission(HeartRateRecord::class).toString()
        )
        permissionsLauncher.launch(permissions)
    }

    /**
     * Save a heart rate record to Health Connect API.
     */
    private suspend fun saveHeartRate(heartRate: Long, dateTime: String) {
        try {
            // Parse the input date-time string
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val localDateTime = java.time.LocalDateTime.parse(dateTime, formatter)

            // Convert LocalDateTime to ZonedDateTime using system's default time zone
            val zonedDateTime = localDateTime.atZone(java.time.ZoneId.systemDefault())

            // Create a HeartRateRecord
            val heartRecord = HeartRateRecord(
                startTime = zonedDateTime.toInstant(),
                startZoneOffset = zonedDateTime.offset,
                endTime = zonedDateTime.toInstant(),
                endZoneOffset = zonedDateTime.offset,
                samples = listOf(HeartRateRecord.Sample(zonedDateTime.toInstant(), heartRate))
            )

            // Insert the record into Health Connect
            healthConnectClient.insertRecords(listOf(heartRecord))
            Log.d("HealthConnect", "Heart rate saved successfully.")
        } catch (e: Exception) {
            Log.e("HealthConnect", "Error saving heart rate: ${e.message}")
        }
    }

    /**
     * Load heart rate records from Health Connect API.
     * @return A list of pairs containing date-time and heart rate values.
     */
    private suspend fun loadHeartRates(): List<Pair<String, String>> {
        return try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = HeartRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.before(Instant.now().plusSeconds(1)) // Load all history
                )
            )
            response.records.map { record ->
                // Format the date-time
                val formattedDateTime =
                    ZonedDateTime.ofInstant(record.startTime, ZoneId.systemDefault())
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                Pair(formattedDateTime, "${record.samples.first().beatsPerMinute} bpm")
            }
        } catch (e: Exception) {
            Log.e("HealthConnect", "Error loading heart rates: ${e.message}")
            emptyList()
        }
    }
}