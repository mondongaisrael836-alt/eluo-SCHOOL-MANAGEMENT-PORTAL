package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistryScreen(
    students: List<Student>,
    onSaveStudent: (Student) -> Unit,
    onDeleteStudent: (Student) -> Unit,
    teachers: List<Teacher>,
    onSaveTeacher: (Teacher) -> Unit,
    onDeleteTeacher: (Teacher) -> Unit,
    classes: List<SchoolClass>,
    onSaveClass: (SchoolClass) -> Unit,
    onDeleteClass: (SchoolClass) -> Unit,
    notices: List<Notice>,
    onSaveNotice: (Notice) -> Unit,
    onDeleteNotice: (Notice) -> Unit,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    var selectedTopTab by remember { mutableStateOf(initialTab) }
    val tabs = listOf("Students", "Teachers", "Sessions", "Circulars")

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("registry_screen")
    ) {
        // High Contrast Top Scrolling Tab Row
        TabRow(
            selectedTabIndex = selectedTopTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth().testTag("registry_tab_row")
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTopTab == index,
                    onClick = { selectedTopTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    icon = {
                        val iconVector = when (index) {
                            0 -> Icons.Default.People
                            1 -> Icons.Default.SupervisorAccount
                            2 -> Icons.Default.Class
                            else -> Icons.Default.Campaign
                        }
                        Icon(imageVector = iconVector, contentDescription = title)
                    },
                    modifier = Modifier.testTag("registry_top_tab_$index")
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTopTab) {
                0 -> StudentsScreen(
                    students = students,
                    onSaveStudent = onSaveStudent,
                    onDeleteStudent = onDeleteStudent,
                    modifier = Modifier.fillMaxSize()
                )
                1 -> TeachersScreen(
                    teachers = teachers,
                    onSaveTeacher = onSaveTeacher,
                    onDeleteTeacher = onDeleteTeacher,
                    modifier = Modifier.fillMaxSize()
                )
                2 -> ClassesScreen(
                    classes = classes,
                    onSaveClass = onSaveClass,
                    onDeleteClass = onDeleteClass,
                    modifier = Modifier.fillMaxSize()
                )
                3 -> NoticesScreen(
                    notices = notices,
                    onSaveNotice = onSaveNotice,
                    onDeleteNotice = onDeleteNotice,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
