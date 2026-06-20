package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SchoolViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = SchoolRepository(database.schoolDao())

    val students: StateFlow<List<Student>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val teachers: StateFlow<List<Teacher>> = repository.allTeachers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val classes: StateFlow<List<SchoolClass>> = repository.allClasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notices: StateFlow<List<Notice>> = repository.allNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        viewModelScope.launch {
            val currentStudents = repository.allStudents.first()
            if (currentStudents.isEmpty()) {
                val seedStudents = listOf(
                    Student(name = "Israel Mondonga", grade = "Grade 12-A", rollNumber = "EL-2026-001", email = "israel.m@school.edu", phone = "+243 897 1234", attendancePercentage = 98, feesPaid = true, performanceIndex = 4.0),
                    Student(name = "Sophia Carter", grade = "Grade 11-B", rollNumber = "EL-2026-012", email = "sophia.c@school.edu", phone = "+1 555 1023", attendancePercentage = 94, feesPaid = true, performanceIndex = 3.8),
                    Student(name = "David Vance", grade = "Grade 10-A", rollNumber = "EL-2026-105", email = "david.v@school.edu", phone = "+1 555 4981", attendancePercentage = 88, feesPaid = false, performanceIndex = 2.9),
                    Student(name = "Amara Okafor", grade = "Grade 12-A", rollNumber = "EL-2026-054", email = "amara.o@school.edu", phone = "+234 813 5432", attendancePercentage = 97, feesPaid = true, performanceIndex = 3.9),
                    Student(name = "Yuki Sato", grade = "Grade 11-A", rollNumber = "EL-2026-089", email = "yuki.s@school.edu", phone = "+81 90 9876", attendancePercentage = 96, feesPaid = true, performanceIndex = 3.7)
                )
                seedStudents.forEach { repository.insertStudent(it) }

                val seedTeachers = listOf(
                    Teacher(name = "Prof. Eluo Mondonga Sr.", subject = "Advanced Algorithms", email = "info.eluo@school.edu", phone = "+243 897 5555", roomNumber = "Lab 304", status = "Active"),
                    Teacher(name = "Dr. Elena Rostova", subject = "Quantum Physics", email = "elena.r@school.edu", phone = "+1 555 5002", roomNumber = "Science Hall 101", status = "Active"),
                    Teacher(name = "James Thompson", subject = "World Literature", email = "james.t@school.edu", phone = "+1 555 8731", roomNumber = "Humanities 205", status = "On Leave"),
                    Teacher(name = "Sarah Jenkins", subject = "Calculus & Analysis", email = "sarah.j@school.edu", phone = "+1 555 3014", roomNumber = "Math Wing 103", status = "Active")
                )
                seedTeachers.forEach { repository.insertTeacher(it) }

                val seedClasses = listOf(
                    SchoolClass(name = "Advanced Algorithms", code = "ALGO-401", scheduleTime = "Mon, Wed 10:00 AM", teacherName = "Prof. Eluo Mondonga Sr.", studentCount = 28),
                    SchoolClass(name = "Quantum Physics", code = "PHYS-302", scheduleTime = "Tue, Thu 01:30 PM", teacherName = "Dr. Elena Rostova", studentCount = 22),
                    SchoolClass(name = "Calculus III", code = "MATH-201", scheduleTime = "Mon, Fri 08:30 AM", teacherName = "Sarah Jenkins", studentCount = 35),
                    SchoolClass(name = "Shakespearean Drama", code = "LIT-215", scheduleTime = "Wed 03:00 PM", teacherName = "James Thompson", studentCount = 15)
                )
                seedClasses.forEach { repository.insertClass(it) }

                val seedNotices = listOf(
                    Notice(title = "ELsystem Annual STEM Fair 2026", content = "The annual science and technology exposition is scheduled for July 15. All project proposals must be submitted to Prof. Eluo Mondonga Sr. by the end of next week. Big cash prizes for the top three innovative ideas!", date = "June 20, 2026", priority = "High"),
                    Notice(title = "Mid-Term Examination Schedule", content = "Mid-term exams will begin on July 2nd. Study schedules and assigned halls are posted on the notices tab of your classrooms.", date = "June 19, 2026", priority = "High"),
                    Notice(title = "New Lab Equipment Installed", content = "We are happy to announce the installation of 15 new high-performance computing rigs in the Advanced Computer Science Lab (Room 304).", date = "June 17, 2026", priority = "Normal")
                )
                seedNotices.forEach { repository.insertNotice(it) }
            }
        }
    }

    fun saveStudent(student: Student) {
        viewModelScope.launch {
            if (student.id == 0) {
                repository.insertStudent(student)
            } else {
                repository.updateStudent(student)
            }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudentById(student.id)
        }
    }

    fun saveTeacher(teacher: Teacher) {
        viewModelScope.launch {
            if (teacher.id == 0) {
                repository.insertTeacher(teacher)
            } else {
                repository.updateTeacher(teacher)
            }
        }
    }

    fun deleteTeacher(teacher: Teacher) {
        viewModelScope.launch {
            repository.deleteTeacherById(teacher.id)
        }
    }

    fun saveClass(schoolClass: SchoolClass) {
        viewModelScope.launch {
            if (schoolClass.id == 0) {
                repository.insertClass(schoolClass)
            } else {
                repository.updateClass(schoolClass)
            }
        }
    }

    fun deleteClass(schoolClass: SchoolClass) {
        viewModelScope.launch {
            repository.deleteClassById(schoolClass.id)
        }
    }

    fun saveNotice(notice: Notice) {
        viewModelScope.launch {
            repository.insertNotice(notice)
        }
    }

    fun deleteNotice(notice: Notice) {
        viewModelScope.launch {
            repository.deleteNoticeById(notice.id)
        }
    }
}
