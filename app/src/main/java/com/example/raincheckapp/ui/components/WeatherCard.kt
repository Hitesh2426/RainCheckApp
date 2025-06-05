package com.example.raincheckapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.raincheckapp.domain.model.WeatherInfo

@Composable
fun WeatherCard(weatherInfo: WeatherInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weather Icon
            AsyncImage(
                model = weatherInfo.iconUrl,
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            // Temperature
            Text(
                text = "${weatherInfo.temperature}°C",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Feels like
            Text(
                text = "Feels like: ${weatherInfo.feelsLike}°C",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Condition
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Cloud,
                    contentDescription = "Condition Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = weatherInfo.weatherDescription,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Humidity
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = "Humidity Icon",
                    tint = Color.Blue
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Humidity: ${weatherInfo.humidity}%",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Rain Expectation
            val willItRain = weatherInfo.weatherMain in setOf("Rain", "Drizzle", "Thunderstorm")

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (willItRain) Color(0xFF448AFF) else Color.Gray
                )
            ) {
                Text(text = if (willItRain) "Rain Expected 🌧️" else "No Rain Expected ☀️")
            }

            // Date
            Text(
                text = "Forecast Time: ${weatherInfo.dateTime}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}