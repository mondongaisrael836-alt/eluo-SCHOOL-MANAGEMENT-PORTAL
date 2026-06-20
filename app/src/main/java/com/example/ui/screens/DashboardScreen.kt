package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.components.PortalFooter
import com.example.ui.components.SkeletonDashboardGrid
import com.example.ui.SchoolViewModel.EduShortVideo
import com.example.ui.SchoolViewModel.EduShortComment
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.statusBarsPadding
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    students: List<Student>,
    teachers: List<Teacher>,
    classes: List<SchoolClass>,
    notices: List<Notice>,
    eduShorts: List<EduShortVideo>,
    showOnboarding: Boolean,
    onCompleteOnboarding: () -> Unit,
    onToggleLikeShort: (Int) -> Unit,
    onAddCommentToShort: (Int, String, String) -> Unit,
    onUploadEduShort: (String, String, String, String) -> Unit,
    onExecuteAiQuery: (String, String, (String) -> Unit) -> Unit,
    onNavigateToTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1200) // Beautiful initial loading simulation
        isLoading = false
    }

    if (isLoading) {
        Column(modifier = modifier.fillMaxSize()) {
            // Mini Header explaining sync
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Synchronizing encrypted academic records...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            SkeletonDashboardGrid(modifier = Modifier.weight(1f))
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("dashboard_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcoming Hero Banner with Logo & Refresh Sync action
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Real Generated Logo Drawable
                                Image(
                                    painter = painterResource(id = R.drawable.img_portal_logo),
                                    contentDescription = "ELsystem Academic Seal",
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                                )
                                Column {
                                    Text(
                                        text = "ELsystem Portal",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = "Royal Academy",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.secondary,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }
                            }
                            
                            // Re-sync loader trigger button
                            IconButton(
                                onClick = { isLoading = true },
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                    .size(36.dp)
                                    .testTag("refresh_dashboard_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Re-sync system",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Unified Academic Analytics",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        )

                        Text(
                            text = "Direct administrative ledger & performance monitor. Registered. Verified. Secure.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }

            // Stats row cards block
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardStatCard(
                        title = "Students",
                        value = students.size.toString(),
                        icon = Icons.Default.People,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToTab(1) }
                    )
                    DashboardStatCard(
                        title = "Faculties",
                        value = teachers.size.toString(),
                        icon = Icons.Default.SupervisorAccount,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToTab(1) } // Tab 1 contains Academic Hub/Registry!
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardStatCard(
                        title = "Course Sessions",
                        value = classes.size.toString(),
                        icon = Icons.Default.Class,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToTab(1) }
                    )
                    val urgentNotices = notices.count { it.priority.lowercase() == "high" }
                    DashboardStatCard(
                        title = "Active Circulars",
                        value = notices.size.toString(),
                        icon = Icons.Default.Campaign,
                        color = if (urgentNotices > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToTab(4) }
                    )
                }
            }

            // DASHBOARD COMPONENT 1: Canvas pie chart tracking fee compliance
            item {
                val paidCount = students.count { it.feesPaid }
                val unpaidCount = students.size - paidCount
                PieChartFinanceCard(
                    paidCount = paidCount,
                    unpaidCount = unpaidCount
                )
            }

            // DASHBOARD COMPONENT 2: Performance analytics Bar charts
            item {
                BarChartPerformanceCard(students = students)
            }

            // Spotlight highlight card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "Honor Spotlight Logo",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1.0f)) {
                            Text(
                                text = "Academia Valedictorian",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "Israel Mondonga",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Class of 2026 • Outstanding GPA: 4.0 • Science & Tech Department",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Recent Notices Quick View
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "School Bulletins Feed",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Button(
                                onClick = { onNavigateToTab(4) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text("See All", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (notices.isEmpty()) {
                            Text(
                                text = "No current school declarations.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        } else {
                            notices.take(2).forEach { notice ->
                                NoticeItemTinyRow(notice = notice)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // DASHBOARD COMPONENT 3: Onboarding Tutorial Guide (Skip or complete as they wish)
            if (showOnboarding) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("onboarding_help_card"),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Text(
                                        "ELsystem Guide & Walkthrough",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                TextButton(onClick = onCompleteOnboarding) {
                                    Text("Skip Tutorial", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }
                            }

                            Text(
                                "Welcome to ELsystem! Here is your quick walkthrough checklist. Follow or mark complete at your convenience:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                TutorialStepCheckRow(text = "Mark student attendance under the Registry -> Attendance subtab.", completed = true)
                                TutorialStepCheckRow(text = "Navigate to Store screen to buy blazers or books with Mobile Cash.", completed = true)
                                TutorialStepCheckRow(text = "Link your student profile by inputting your username below.", completed = false)
                                TutorialStepCheckRow(text = "Run educational query tasks using Gemini AI integration.", completed = false)
                            }

                            Button(
                                onClick = onCompleteOnboarding,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Got it, Archive Walkthrough", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }

            // DASHBOARD COMPONENT 4: Sliding Pages of Campus Photos Sliding Block
            item {
                var activePhotoIdx by remember { mutableStateOf(0) }
                val slideshowDescriptions = listOf(
                    "ELsystem High Tech Research & Computing Lab - Room 304",
                    "Scholastic Annual STEM Fair 2026 Innovation Challenge",
                    "Valedictorian Israel Mondonga receiving Faculty Honors"
                )
                val slideshowIcons = listOf(
                    Icons.Default.School,
                    Icons.Default.MilitaryTech,
                    Icons.Default.LaptopChromebook
                )

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(4000)
                        activePhotoIdx = (activePhotoIdx + 1) % slideshowDescriptions.size
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().testTag("campus_slideshow_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "📸 Campus Memories Slideshow",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.secondaryContainer,
                                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = slideshowIcons[activePhotoIdx],
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = slideshowDescriptions[activePhotoIdx],
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }

                            // Dot selectors overlay
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                slideshowDescriptions.forEachIndexed { i, _ ->
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (activePhotoIdx == i) MaterialTheme.colorScheme.secondary
                                                else Color.White.copy(alpha = 0.6f)
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // DASHBOARD COMPONENT 5: School Video Spotlights list
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("campus_videos_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "🎥 Academic Video Highlights",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f))
                                    .padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.8f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Algorithms Lecture", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                Text("4.0 GPA Series", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f))
                                    .padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.8f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("STEM Fair Vlog", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                Text("By Israel Mondonga", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            // DASHBOARD COMPONENT 6: Interactive Parent child linkage username lookup
            item {
                var parentSearchQuery by remember { mutableStateOf("") }
                var queriedStudentResult by remember { mutableStateOf<Student?>(null) }
                var searchAttempted by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth().testTag("parent_link_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f))
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(
                                "🛡️ Parent Portal Access Desk",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            "Secure verification linking. Put your child's complete name or Roll ID to instantly view their digital reports, fee compliance ledger, and daily audits.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.61f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = parentSearchQuery,
                                onValueChange = { parentSearchQuery = it },
                                placeholder = { Text("e.g. Israel Mondonga") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    queriedStudentResult = students.find { 
                                        it.name.equals(parentSearchQuery, ignoreCase = true) || 
                                        it.rollNumber.equals(parentSearchQuery, ignoreCase = true) 
                                    }
                                    searchAttempted = true
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Link")
                            }
                        }

                        if (searchAttempted) {
                            val result = queriedStudentResult
                            if (result != null) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            "✓ Secure Parent Link Verified!",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Text(
                                            result.name,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Grade: ${result.grade}", style = MaterialTheme.typography.bodySmall)
                                            Text("GPA Index: ${result.performanceIndex} / 4.0", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Attendance percentage: ${result.attendancePercentage}% Ratio", style = MaterialTheme.typography.bodySmall)
                                            Text(
                                                if (result.feesPaid) "Tuition: Settled" else "Tuition: Outstanding Debt",
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                color = if (result.feesPaid) Color(0xFF047857) else Color(0xFFB91C1C)
                                            )
                                        }
                                    }
                                }
                            } else {
                                Surface(
                                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "✗ No registered student matched. Please check spelling or contact the administrator.",
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // DASHBOARD COMPONENT 7: Unified Gemini AI Chatbot query box (incorporates student and admin/management AI roles)
            item {
                var selectedAiRole by remember { mutableStateOf("Student") }
                var customAiPromptQuery by remember { mutableStateOf("") }
                var parsedAiReplyText by remember { mutableStateOf("") }
                var executingAiQuerySpinner by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth().testTag("ai_assistant_desk"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(
                                "🤖 Unified ELsystem AI Advisor",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            "Select your credential role below and consult either the Student Personal AI Tutor or Admin Management Advisor directly.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        // Role Selector Filters row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("Student", "Management").forEach { role ->
                                val active = selectedAiRole == role
                                FilterChip(
                                    selected = active,
                                    onClick = { selectedAiRole = role },
                                    label = { Text("$role Mode AI") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        OutlinedTextField(
                            value = customAiPromptQuery,
                            onValueChange = { customAiPromptQuery = it },
                            placeholder = { 
                                Text(
                                    if (selectedAiRole == "Student") "Ask about AVL trees, quantum physics waves..." 
                                    else "Ask for timetable feedback or circular drafts..."
                                ) 
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                if (executingAiQuerySpinner) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    IconButton(
                                        onClick = {
                                            if (customAiPromptQuery.isNotBlank()) {
                                                executingAiQuerySpinner = true
                                                onExecuteAiQuery(selectedAiRole, customAiPromptQuery) { reply ->
                                                    parsedAiReplyText = reply
                                                    executingAiQuerySpinner = false
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = "Query Gemini AI")
                                    }
                                }
                            }
                        )

                        if (parsedAiReplyText.isNotEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        "⚡ Gemini AI Insights:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        parsedAiReplyText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // DASHBOARD COMPONENT 8: Past Papers Cabinet Library Shelf
            item {
                val pastPapersList = listOf(
                    Triple("Calculus III Midterm Paper (2025)", "Mathematics", "A4_Calculus_2025.pdf"),
                    Triple("Advanced AVL Rotations Exercise", "Computer Science", "CSE_AVL_Rotations.pdf"),
                    Triple("Quantum Physics Quantum States Solution", "Physics", "PHYS_Quantum_States.pdf")
                )
                val contextForPaper = LocalContext.current

                Card(
                    modifier = Modifier.fillMaxWidth().testTag("past_papers_shelf"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "📚 Past Papers Repository",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Download study sheets instantly",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Icon(Icons.Default.CollectionsBookmark, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            pastPapersList.forEach { (title, subject, filename) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f), RoundedCornerShape(10.dp))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                        Text(subject, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                    }

                                    IconButton(
                                        onClick = {
                                            // Simulate downloading and preparing past study sheets
                                            android.widget.Toast.makeText(contextForPaper, "Downloaded study file: $filename!", android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                                            contentColor = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = "Download $filename", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // DASHBOARD COMPONENT 9: TikTok-like EduShort Video Launcher Spotlight
            item {
                var openEduShortOverlay by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth().testTag("edushorts_spotlight_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    onClick = { openEduShortOverlay = true }
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SlowMotionVideo, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }

                        Column(modifier = Modifier.weight(1.0f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "🚀 EduShort • Educational Shorts Feed",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "Watch and swipe bite-sized STEM videos from classrooms. Tap to launch full screen player!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }

                if (openEduShortOverlay) {
                    EduShortFullscreenPlayerDialog(
                        videos = eduShorts,
                        onToggleLike = onToggleLikeShort,
                        onAddComment = onAddCommentToShort,
                        onUploadShort = onUploadEduShort,
                        onDismiss = { openEduShortOverlay = false }
                    )
                }
            }

            // Beautiful permanent Developer Brand Footer
            item {
                PortalFooter()
            }
        }
    }
}

@Composable
fun PieChartFinanceCard(
    paidCount: Int,
    unpaidCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tuition Settlement Analytics",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Current account status of registered students",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            val total = (paidCount + unpaidCount).toFloat().coerceAtLeast(1f)
            val paidAngle = (paidCount / total) * 360f
            val unpaidAngle = 360f - paidAngle

            val paidPercent = ((paidCount / total) * 100).toInt()
            val unpaidPercent = 100 - paidPercent

            val paidColor = MaterialTheme.colorScheme.primary
            val unpaidColor = MaterialTheme.colorScheme.error

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Canvas drawn pie chart
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(120.dp)) {
                        drawArc(
                            color = paidColor,
                            startAngle = -90f,
                            sweepAngle = paidAngle,
                            useCenter = true
                        )
                        drawArc(
                            color = unpaidColor,
                            startAngle = -90f + paidAngle,
                            sweepAngle = unpaidAngle,
                            useCenter = true
                        )
                    }
                    // Mini hollow center for a stylish donut flavor
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    )
                }

                // Legend
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(paidColor)
                        )
                        Column {
                            Text(
                                text = "Paid (Compliance)",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$paidCount students ($paidPercent%)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(unpaidColor)
                        )
                        Column {
                            Text(
                                text = "Outstanding Debt",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$unpaidCount students ($unpaidPercent%)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BarChartPerformanceCard(
    students: List<Student>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Department GPA Benchmarks",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Performance Index averages mapped dynamically",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Graph GPA distribution using custom columns
            val chartData = if (students.isEmpty()) {
                listOf("Gr. 12-A" to 3.8f, "Gr. 11-A" to 3.5f, "Gr. 11-B" to 3.1f, "Gr. 10-A" to 2.8f)
            } else {
                students.groupBy { it.grade }.map { (grade, list) ->
                    val avgGpa = list.map { it.performanceIndex }.average().toFloat()
                    grade to avgGpa
                }.sortedByDescending { it.second }.take(4)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                chartData.forEach { (label, value) ->
                    val maxGpa = 4.0f
                    val normalizedHeight = (value / maxGpa).coerceIn(0.1f, 1.0f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = String.format("%.2f", value),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .fillMaxHeight(normalizedHeight)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                                        )
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(105.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                )

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Stat Logo",
                        tint = color,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun NoticeItemTinyRow(notice: Notice) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (notice.priority.lowercase() == "high") MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.secondary
                )
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notice.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = notice.content,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = notice.date,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun TutorialStepCheckRow(text: String, completed: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (completed) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            fontWeight = if (completed) FontWeight.Normal else FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EduShortFullscreenPlayerDialog(
    videos: List<EduShortVideo>,
    onToggleLike: (Int) -> Unit,
    onAddComment: (Int, String, String) -> Unit,
    onUploadShort: (String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var activeIndex by remember { mutableStateOf(0) }
    var showCommentListSheet by remember { mutableStateOf(false) }
    var textCommentInput by remember { mutableStateOf("") }
    var showUploadForm by remember { mutableStateOf(false) }

    val activeVideo = videos.getOrNull(activeIndex) ?: EduShortVideo(
        id = 999,
        title = "No Shorts available",
        authorName = "System",
        authorRole = "Faculty Developer",
        authorAvatarIndex = 0,
        likesCount = 0,
        comments = emptyList(),
        hasLiked = false,
        durationSeconds = 10,
        startColor = 0xFF1E293B.toInt(),
        endColor = 0xFF0F172A.toInt()
    )

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false // Complete fullscreen overlay!
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0F172A) // Sleek dark midnight canvas for video shorts
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Video visualizer background representation representation
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SlowMotionVideo,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.08f),
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "[ Simulating Loop Video Stream: ${activeVideo.title} ]",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.padding(horizontal = 24.dp),
                        textAlign = TextAlign.Center
                    )
                }

                // Top Close button and tag
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Close Player", tint = Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = activeVideo.authorRole.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = { showUploadForm = true },
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.VideoCall, contentDescription = "Upload Short", tint = Color.White)
                    }
                }

                // Center Navigation Arrows (In place of continuous hand swiping)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (activeIndex > 0) activeIndex--
                        },
                        enabled = activeIndex > 0,
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Previous Short",
                            tint = if (activeIndex > 0) Color.White else Color.White.copy(alpha = 0.2f)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (activeIndex < videos.size - 1) activeIndex++
                        },
                        enabled = activeIndex < videos.size - 1,
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Next Short",
                            tint = if (activeIndex < videos.size - 1) Color.White else Color.White.copy(alpha = 0.2f)
                        )
                    }
                }

                // Bottom Left Details Overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .padding(bottom = 32.dp)
                        .widthIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activeVideo.authorName.take(2).uppercase(),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Text(
                            text = "@${activeVideo.authorName.replace(" ", "").lowercase()}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Text(
                        text = activeVideo.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White
                    )

                    Text(
                        text = activeVideo.authorRole,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 3
                    )
                }

                // Right Side Floating HUD Controls (Likes & Comments triggers)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Likes button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { onToggleLike(activeVideo.id) },
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Like Short",
                                tint = if (activeVideo.hasLiked) Color.Red else Color.White
                            )
                        }
                        Text(
                            text = "${activeVideo.likesCount}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    // Comments button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { showCommentListSheet = true },
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Comment, contentDescription = "View Comments", tint = Color.White)
                        }
                        Text(
                            text = "${activeVideo.comments.size}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    // Developer watermark
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                        Text(
                            text = "ELsystem",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                // Full screen Sliding Comment panel bottom sheet replacement
                if (showCommentListSheet) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .align(Alignment.BottomCenter),
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Comments (${activeVideo.comments.size})",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                IconButton(onClick = { showCommentListSheet = false }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Comments", tint = Color.White)
                                }
                            }

                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (activeVideo.comments.isEmpty()) {
                                    item {
                                        Text(
                                            text = "No responses logged yet. Start the scholastic discussion below!",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(vertical = 12.dp)
                                        )
                                    }
                                } else {
                                    items(activeVideo.comments) { comment ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                                                .padding(8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White.copy(alpha = 0.1f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = comment.author.take(1).uppercase(),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Color.White
                                                )
                                            }
                                            Column {
                                                Text(comment.author, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                                Text(comment.text, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                                            }
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = textCommentInput,
                                    onValueChange = { textCommentInput = it },
                                    placeholder = { Text("Add response...", color = Color.Gray) },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                                        unfocusedContainerColor = Color.Black.copy(alpha = 0.2f)
                                    )
                                )

                                Button(
                                    onClick = {
                                        if (textCommentInput.isNotBlank()) {
                                            onAddComment(activeVideo.id, "Me", textCommentInput)
                                            textCommentInput = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text("Post", color = Color.White)
                                }
                            }
                        }
                    }
                }

                // Video Upload form overlay
                if (showUploadForm) {
                    var uploadTitle by remember { mutableStateOf("") }
                    var uploadDesc by remember { mutableStateOf("") }
                    var uploadTag by remember { mutableStateOf("Computer Science") }

                    AlertDialog(
                        onDismissRequest = { showUploadForm = false },
                        title = {
                            Text(
                                "Publish Academic EduShort",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    "Provide metadata parameters to synchronize your educational video recording into the institutional school library database.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )

                                OutlinedTextField(
                                    value = uploadTitle,
                                    onValueChange = { uploadTitle = it },
                                    label = { Text("Short Subject Title") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = uploadDesc,
                                    onValueChange = { uploadDesc = it },
                                    label = { Text("Scholastic Description") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = uploadTag,
                                    onValueChange = { uploadTag = it },
                                    label = { Text("Course Tag (e.g. Physics)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (uploadTitle.isNotBlank() && uploadDesc.isNotBlank()) {
                                        onUploadShort(uploadTitle, uploadDesc, "", uploadTag)
                                        showUploadForm = false
                                    }
                                }
                            ) {
                                Text("Publish Short")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showUploadForm = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

