package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.SchoolViewModel
import com.example.ui.components.PortalHeader
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainPortalApp()
            }
        }
    }
}

@Composable
fun MainPortalApp(
    viewModel: SchoolViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }

    val students by viewModel.students.collectAsStateWithLifecycle()
    val teachers by viewModel.teachers.collectAsStateWithLifecycle()
    val classes by viewModel.classes.collectAsStateWithLifecycle()
    val notices by viewModel.notices.collectAsStateWithLifecycle()

    val tabs = listOf(
        TabItem("Home", Icons.Default.Home, "Overview Dashboard"),
        TabItem("Students", Icons.Default.Person, "Management"),
        TabItem("Teachers", Icons.Default.Face, "Faculty Members"),
        TabItem("Classes", Icons.Default.Book, "Course Sessions"),
        TabItem("Bulletin", Icons.Default.Notifications, "School Notices")
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        topBar = {
            val highPriorityCount = notices.count { it.priority.lowercase() == "high" }
            PortalHeader(
                currentTabTitle = tabs[selectedTab].subTitle,
                noticeCount = highPriorityCount
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("main_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(text = tab.title)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_item_$index")
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(
                    students = students,
                    teachers = teachers,
                    classes = classes,
                    notices = notices,
                    onNavigateToTab = { selectedTab = it },
                    modifier = Modifier.fillMaxSize()
                )
                1 -> StudentsScreen(
                    students = students,
                    onSaveStudent = { viewModel.saveStudent(it) },
                    onDeleteStudent = { viewModel.deleteStudent(it) },
                    modifier = Modifier.fillMaxSize()
                )
                2 -> TeachersScreen(
                    teachers = teachers,
                    onSaveTeacher = { viewModel.saveTeacher(it) },
                    onDeleteTeacher = { viewModel.deleteTeacher(it) },
                    modifier = Modifier.fillMaxSize()
                )
                3 -> ClassesScreen(
                    classes = classes,
                    onSaveClass = { viewModel.saveClass(it) },
                    onDeleteClass = { viewModel.deleteClass(it) },
                    modifier = Modifier.fillMaxSize()
                )
                4 -> NoticesScreen(
                    notices = notices,
                    onSaveNotice = { viewModel.saveNotice(it) },
                    onDeleteNotice = { viewModel.deleteNotice(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

data class TabItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val subTitle: String
)
