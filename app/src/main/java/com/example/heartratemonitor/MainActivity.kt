package com.example.heartratemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heartratemonitor.ui.theme.HeartRateMonitorTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeartRateApp()
        }
    }
}

@Composable
fun HeartRateApp() {
    var heartRate by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf(LocalDateTime.now().toString()) }
    val heartRateHistory = remember { mutableStateListOf<Pair<String, String>>() }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = heartRate,
            onValueChange = { heartRate = it },
            label = { Text("Heart Rate (1-300 bpm)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = timestamp,
            onValueChange = { timestamp = it },
            label = { Text("Date/Time") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = { heartRateHistory.add(Pair(heartRate, timestamp)) }) {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /*  Load Function */ }) {
                Text("Load")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Heart Rate History", style = MaterialTheme.typography.headlineLarge)
        LazyColumn {
            items(heartRateHistory.size) { index ->
                Text("${heartRateHistory[index].first} bpm, ${heartRateHistory[index].second}")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Student Name: Rajan Boudel\nStudent ID: 301365245")
    }
}


