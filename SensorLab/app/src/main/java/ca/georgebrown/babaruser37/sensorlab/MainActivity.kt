package ca.georgebrown.babaruser37.sensorlab

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import ca.georgebrown.babaruser37.sensorlab.ui.theme.Orange80
import ca.georgebrown.babaruser37.sensorlab.ui.theme.SensorLabTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.georgebrown.babaruser37.sensorlab.ui.theme.success


class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private var temperatureSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null

    // Compose-observable state
    private var temperature by mutableStateOf<Float?>(null)
    private var gyroX by mutableStateOf(0f)
    private var gyroY by mutableStateOf(0f)
    private var gyroZ by mutableStateOf(0f)
    private var lightSensor: Sensor? = null
    private var light by mutableStateOf(0f)
    private var lightAvailable by mutableStateOf(false)


    private var temperatureAvailable by mutableStateOf(false)
    private var gyroscopeAvailable by mutableStateOf(false)
    private var tempMeta by mutableStateOf("")
    private var gyroMeta by mutableStateOf("")
    private var lightMeta by mutableStateOf("")
    private var isMovingFast by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Discover sensors
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) // Third sensor

        // Check availability
        temperatureAvailable = temperatureSensor != null
        gyroscopeAvailable = gyroscopeSensor != null
        lightAvailable = lightSensor != null

        // Helper function to extract metadata properties
        fun getSensorInfo(sensor: Sensor?): String {
            return if (sensor != null) {
                "Name: ${sensor.name} | Vendor: ${sensor.vendor}\nMax Range: ${sensor.maximumRange} | Resolution: ${sensor.resolution}"
            } else {
                "Sensor not available"
            }
        }

        // Populate metadata strings
        tempMeta = getSensorInfo(temperatureSensor)
        gyroMeta = getSensorInfo(gyroscopeSensor)
        lightMeta = getSensorInfo(lightSensor)

        setContent {
            SensorLabTheme {
                SensorScreen(
                    temperature = temperature,
                    temperatureAvailable = temperatureAvailable,
                    tempMeta = tempMeta,
                    gyroX = gyroX,
                    gyroY = gyroY,
                    gyroZ = gyroZ,
                    gyroscopeAvailable = gyroscopeAvailable,
                    gyroMeta = gyroMeta,
                    isMovingFast = isMovingFast,
                    onResetGyro = { resetGyroscopeValues() },
                    light = light,
                    lightAvailable = lightAvailable,
                    lightMeta = lightMeta
                )
            }
        }
    }
    override fun onResume() {
        super.onResume()

        temperatureSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        gyroscopeSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                temperature = event.values[0]
            }
            Sensor.TYPE_LIGHT -> {
                light = event.values[0]
            }
            Sensor.TYPE_GYROSCOPE -> {
                gyroX = event.values[0]
                gyroY = event.values[1]
                gyroZ = event.values[2]

                // Optional: Calculate movement threshold here if you want
                val magnitude = kotlin.math.sqrt(gyroX * gyroX + gyroY * gyroY + gyroZ * gyroZ)
                isMovingFast = magnitude > 1.5f
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not required for this lab.
    }

    fun resetGyroscopeValues() {
        gyroX = 0f
        gyroY = 0f
        gyroZ = 0f
        isMovingFast = false
    }

    @Composable
    fun SensorValueCard(
        title: String,
        available: Boolean,
        statusMessage: String? = null,
        metadata: String,
        content: @Composable () -> Unit
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(metadata, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(8.dp))

                when {
                    !available -> Text("$title is not available.")
                    statusMessage != null -> Text(statusMessage)
                    else -> content()
                }
            }
        }
    }



    @Composable
    fun SensorScreen(
        temperature: Float?,
        temperatureAvailable: Boolean,
        tempMeta: String,
        gyroX: Float,
        gyroY: Float,
        gyroZ: Float,
        gyroscopeAvailable: Boolean,
        gyroMeta: String,
        isMovingFast: Boolean,
        onResetGyro: () -> Unit,
        light: Float,
        lightAvailable: Boolean,
        lightMeta: String
    ) {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Android Advanced Sensor Monitor", style = MaterialTheme.typography.headlineMedium)

                // 1. Temperature Card
                SensorValueCard(
                    title = "Ambient Temperature",
                    available = temperatureAvailable,
                    statusMessage = if (temperature == null) "Waiting for sensor reading..." else null,
                    metadata = tempMeta
                ) {
                    Text(
                        "%.1f °C".format(temperature),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // 2. Gyroscope Card with Movement Indicator & Reset Button
                SensorValueCard(
                    title = "Gyroscope",
                    available = gyroscopeAvailable,
                    metadata = gyroMeta
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("X: %.3f rad/s".format(gyroX))
                        Text("Y: %.3f rad/s".format(gyroY))
                        Text("Z: %.3f rad/s".format(gyroZ))

                        Spacer(Modifier.height(4.dp))
                        // Visual indicator changing color/text based on threshold
                        Text(
                            text = if (isMovingFast) "Status: Rapid Movement Detected!" else "Status: Stable / Slow",
                            color = if (isMovingFast) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.success,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onResetGyro,
                            colors = ButtonDefaults.buttonColors(containerColor = Orange80)
                        ) {
                            Text("Reset Gyroscope")
                        }
                    }
                }

                // 3. Light Sensor Card (Third Sensor)
                SensorValueCard(
                    title = "Light Sensor",
                    available = lightAvailable,
                    metadata = lightMeta
                ) {
                    Text(
                        "%.1f lx".format(light),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
    @Composable
    fun TemperatureCard(
        temperature: Float?,
        available: Boolean
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Ambient Temperature",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (!available) {
                    Text("Temperature sensor is not available.")
                } else if (temperature == null) {
                    Text("Waiting for sensor reading...")
                } else {
                    Text(
                        text = "%.1f °C".format(temperature),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }

    @Composable
    fun GyroscopeCard(
        x: Float,
        y: Float,
        z: Float,
        available: Boolean
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Gyroscope",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (!available) {
                    Text("Gyroscope is not available.")
                } else {
                    Text("X: %.3f rad/s".format(x))
                    Text("Y: %.3f rad/s".format(y))
                    Text("Z: %.3f rad/s".format(z))
                }
            }
        }
    }







    // ...
}
