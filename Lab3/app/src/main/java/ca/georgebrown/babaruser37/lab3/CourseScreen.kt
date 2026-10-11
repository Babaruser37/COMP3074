package ca.georgebrown.babaruser37.lab3

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CourseScreen() {
    val context = LocalContext.current

    val courses = remember {
        mutableStateListOf<Course>().apply {
            addAll(loadCourses(context))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8FC))
            .padding(16.dp) // Added padding back here to fix the left/right margins
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        LazyCourseList(courses = courses)
    }
}

@Composable
fun CourseItem(course: Course) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Red circular avatar containing the Android icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(width = 2.dp, color = Color(0xffc3dad5), shape = CircleShape)
                .clip(CircleShape)
                .background(Color(0xffdad5c3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Android,
                contentDescription = "Avatar Icon",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Name and chat bubble container
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = course.code,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff36454F)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Rounded message bubble
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xffc3dad5),
                border = BorderStroke(1.dp, Color(0xFFE8E2E6))
            ) {
                Text(
                    text = course.name,
                    fontSize = 18.sp,
                    color = Color(0xFF1D1B20),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
fun SimpleCourseList(courses: List<Course>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        courses.forEach { course ->
            CourseItem(course)
        }
    }
}

@Composable
fun LazyCourseList(courses: List<Course>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(courses) { course ->
            CourseItem(course)
        }
    }
}