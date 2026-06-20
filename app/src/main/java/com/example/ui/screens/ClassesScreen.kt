package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SchoolClass
import com.example.ui.components.PortalFooter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    classes: List<SchoolClass>,
    onSaveClass: (SchoolClass) -> Unit,
    onDeleteClass: (SchoolClass) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingClass by remember { mutableStateOf<SchoolClass?>(null) }

    val filteredClasses = remember(classes, searchQuery) {
        classes.filter { c ->
            c.name.contains(searchQuery, ignoreCase = true) ||
                    c.code.contains(searchQuery, ignoreCase = true) ||
                    c.teacherName.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("classes_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingClass = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_class_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Academic Course")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input Block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search classes, course codes...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear Search")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("class_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    ),
                    singleLine = true
                )
            }

            // Academic Courses List representation
            if (filteredClasses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "No Courses",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "No academic courses discovered",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredClasses, key = { it.id }) { item ->
                        ClassCardItem(
                            schoolClass = item,
                            onEdit = {
                                editingClass = item
                                showDialog = true
                            },
                            onDelete = { onDeleteClass(item) }
                        )
                    }

                    item {
                        PortalFooter()
                    }
                }
            }
        }
    }

    if (showDialog) {
        ClassFormDialog(
            schoolClass = editingClass,
            onDismiss = { showDialog = false },
            onConfirm = { item ->
                onSaveClass(item)
                showDialog = false
            }
        )
    }
}

@Composable
fun ClassCardItem(
    schoolClass: SchoolClass,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("class_card_${schoolClass.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Book icon",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = schoolClass.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = schoolClass.code,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                            )
                            Text(
                                text = "${schoolClass.studentCount} Students",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse info" else "Expand info"
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            LabelValueRow(label = "Primary Faculty Teacher", value = schoolClass.teacherName)
                            Spacer(modifier = Modifier.height(6.dp))
                            LabelValueRow(label = "Weekly Schedule Period", value = schoolClass.scheduleTime)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = onEdit,
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit class settings", modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = onDelete,
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Dissolve academic class", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassFormDialog(
    schoolClass: SchoolClass?,
    onDismiss: () -> Unit,
    onConfirm: (SchoolClass) -> Unit
) {
    var name by remember { mutableStateOf(schoolClass?.name ?: "") }
    var code by remember { mutableStateOf(schoolClass?.code ?: "") }
    var scheduleTime by remember { mutableStateOf(schoolClass?.scheduleTime ?: "") }
    var teacherName by remember { mutableStateOf(schoolClass?.teacherName ?: "") }
    var studentCount by remember { mutableStateOf(schoolClass?.studentCount?.toString() ?: "20") }

    var hasError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (schoolClass == null) "Schedule New Class" else "Configure Academic Course",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Course Name") },
                        modifier = Modifier.fillMaxWidth().testTag("class_name_field"),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Course Identity Code") },
                        placeholder = { Text("e.g. ALGO-401") },
                        modifier = Modifier.fillMaxWidth().testTag("class_code_field"),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = scheduleTime,
                        onValueChange = { scheduleTime = it },
                        label = { Text("Weekly Lecture Period") },
                        placeholder = { Text("e.g. Mon, Wed 10:00 AM") },
                        modifier = Modifier.fillMaxWidth().testTag("class_schedule_field"),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = teacherName,
                        onValueChange = { teacherName = it },
                        label = { Text("Appointed Faculty Teacher") },
                        modifier = Modifier.fillMaxWidth().testTag("class_teacher_field"),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = studentCount,
                        onValueChange = { studentCount = it },
                        label = { Text("Expected Class Roll Count") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("class_count_field"),
                        singleLine = true
                    )
                }

                if (hasError) {
                    item {
                        Text(
                            text = "Please complete Name, Code, and Lecture Period fields.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank() || code.isBlank() || scheduleTime.isBlank()) {
                        hasError = true
                    } else {
                        val parsedCount = studentCount.toIntOrNull() ?: 20
                        onConfirm(
                            SchoolClass(
                                id = schoolClass?.id ?: 0,
                                name = name,
                                code = code,
                                scheduleTime = scheduleTime,
                                teacherName = teacherName,
                                studentCount = parsedCount
                            )
                        )
                    }
                },
                modifier = Modifier.testTag("class_save_confirm")
            ) {
                Text("Confirm Allocation")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
