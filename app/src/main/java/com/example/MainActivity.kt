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
    val attendance by viewModel.attendance.collectAsStateWithLifecycle()
    val eduShorts by viewModel.eduShorts.collectAsStateWithLifecycle()
    val showOnboarding by viewModel.showOnboarding.collectAsStateWithLifecycle()

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val payeePhone by viewModel.payeePhone.collectAsStateWithLifecycle()
    val cart by viewModel.cart.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    val tabs = listOf(
        TabItem("Home", Icons.Default.Home, "Overview Dashboard"),
        TabItem("Registry", Icons.Default.Assignment, "Academic Hub"),
        TabItem("Messaging", Icons.Default.QuestionAnswer, "Faculty Chat"),
        TabItem("Store", Icons.Default.LocalMall, "Online Campus Shop"),
        TabItem("Wallet", Icons.Default.AccountBalanceWallet, "School Escrow Setup")
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
                    eduShorts = eduShorts,
                    showOnboarding = showOnboarding,
                    onCompleteOnboarding = { viewModel.setOnboardingCompleted() },
                    onToggleLikeShort = { viewModel.toggleLikeShort(it) },
                    onAddCommentToShort = { id, author, text -> viewModel.addCommentToShort(id, text, author, 0) },
                    onUploadEduShort = { title, desc, path, tag -> viewModel.uploadEduShort(title, "Faculty Admin", tag, 0xFF3B82F6.toInt(), 0xFF8B5CF6.toInt()) },
                    onExecuteAiQuery = { role, query, cb -> viewModel.executeAiQuery(role, query, cb) },
                    onNavigateToTab = { selectedTab = it },
                    modifier = Modifier.fillMaxSize()
                )
                1 -> RegistryScreen(
                    students = students,
                    onSaveStudent = { viewModel.saveStudent(it) },
                    onDeleteStudent = { viewModel.deleteStudent(it) },
                    teachers = teachers,
                    onSaveTeacher = { viewModel.saveTeacher(it) },
                    onDeleteTeacher = { viewModel.deleteTeacher(it) },
                    classes = classes,
                    onSaveClass = { viewModel.saveClass(it) },
                    onDeleteClass = { viewModel.deleteClass(it) },
                    notices = notices,
                    onSaveNotice = { viewModel.saveNotice(it) },
                    onDeleteNotice = { viewModel.deleteNotice(it) },
                    attendance = attendance,
                    onSaveAttendance = { d, cls, p, a -> viewModel.saveAttendance(d, cls, p, a) },
                    initialTab = 0,
                    modifier = Modifier.fillMaxSize()
                )
                2 -> MessagingScreen(
                    userProfile = userProfile,
                    chatMessages = chatMessages,
                    onSignIn = { name, email, role, phone -> viewModel.signIn(name, email, role, phone) },
                    onSignOut = { viewModel.signOut() },
                    onUpdateProfile = { name, email, phone, bio -> viewModel.updateProfile(name, email, phone, bio) },
                    onSendMessage = { text -> viewModel.sendMessage(text) },
                    modifier = Modifier.fillMaxSize()
                )
                3 -> StoreScreen(
                    products = viewModel.products,
                    cart = cart,
                    onAddToCart = { viewModel.addToCart(it) },
                    onRemoveFromCart = { viewModel.removeFromCart(it) },
                    onCheckout = { name, phone -> viewModel.checkoutCart(name, phone) },
                    modifier = Modifier.fillMaxSize()
                )
                4 -> WalletScreen(
                    payeePhone = payeePhone,
                    transactions = transactions,
                    onRegisterPayeePhone = { viewModel.registerPayeePhone(it) },
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
