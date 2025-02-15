package com.example.heartratemonitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HealthConnectManager(
    onSaveClick: (String, String) -> Unit,
    onLoadClick: () -> Unit,
    heartRateHistory: List<Pair<String, String>>
) {
    var heartRate by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Modern Gradient Background
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF002855), Color(0xFF0077B6))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush) // Apply gradient background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                "Heart Rate Monitor",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Heart Rate Input Field
            OutlinedTextField(
                value = heartRate,
                onValueChange = {
                    heartRate = it
                    errorMessage = if (it.toIntOrNull() !in 1..300) "Heart rate must be between 1 and 300 bpm" else ""
                },
                label = { Text("Heart Rate (1-300 bpm)", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage.isNotEmpty(),
                textStyle = TextStyle(color = Color.White)
            )
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date/Time Input Field
            OutlinedTextField(
                value = dateTime,
                onValueChange = { dateTime = it },
                label = { Text("Date/Time (yyyy-MM-dd HH:mm)", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onLoadClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF48CAE4)),
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    Text("Load", fontSize = 18.sp, color = Color.White)
                }
                Button(
                    onClick = {
                        if (errorMessage.isEmpty()) {
                            onSaveClick(heartRate, dateTime)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8)),
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    Text("Save", fontSize = 18.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Heart Rate History
            Text(
                "Heart Rate History",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(heartRateHistory) { reading ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF7B68EE)) // MediumSlateBlue
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Date/Time: ${reading.second}",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Heart Rate: ${reading.first} bpm",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Student Info", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
                Text("Student Name: Rajan Boudel", fontSize = 16.sp, color = Color.White)
                Text("Student ID: 301365245", fontSize = 16.sp, color = Color.White)
            }

        }
    }
}
