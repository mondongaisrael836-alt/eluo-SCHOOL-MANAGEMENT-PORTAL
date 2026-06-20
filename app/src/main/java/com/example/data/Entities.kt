package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val grade: String,
    val rollNumber: String,
    val email: String,
    val phone: String,
    val attendancePercentage: Int = 95,
    val feesPaid: Boolean = true,
    val performanceIndex: Double = 3.5
)

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val subject: String,
    val email: String,
    val phone: String,
    val roomNumber: String,
    val status: String = "Active"
)

@Entity(tableName = "school_classes")
data class SchoolClass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val code: String,
    val scheduleTime: String,
    val teacherName: String,
    val studentCount: Int = 0
)

@Entity(tableName = "notices")
data class Notice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val date: String,
    val priority: String = "Normal"
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val className: String,
    val studentName: String,
    val isPresent: Boolean
)
