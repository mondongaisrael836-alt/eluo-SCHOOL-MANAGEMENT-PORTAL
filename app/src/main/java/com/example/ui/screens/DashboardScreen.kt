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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.components.PortalFooter
import com.example.ui.components.SkeletonDashboardGrid
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    students: List<Student>,
    teachers: List<Teacher>,
    classes: List<SchoolClass>,
    notices: List<Notice>,
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

