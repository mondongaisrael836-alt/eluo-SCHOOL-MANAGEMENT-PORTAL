package com.example.data

import kotlinx.coroutines.flow.Flow

class SchoolRepository(private val schoolDao: SchoolDao) {
    val allStudents: Flow<List<Student>> = schoolDao.getAllStudents()
    val allTeachers: Flow<List<Teacher>> = schoolDao.getAllTeachers()
    val allClasses: Flow<List<SchoolClass>> = schoolDao.getAllClasses()
    val allNotices: Flow<List<Notice>> = schoolDao.getAllNotices()

    suspend fun insertStudent(student: Student) = schoolDao.insertStudent(student)
    suspend fun updateStudent(student: Student) = schoolDao.updateStudent(student)
    suspend fun deleteStudentById(id: Int) = schoolDao.deleteStudentById(id)

    suspend fun insertTeacher(teacher: Teacher) = schoolDao.insertTeacher(teacher)
    suspend fun updateTeacher(teacher: Teacher) = schoolDao.updateTeacher(teacher)
    suspend fun deleteTeacherById(id: Int) = schoolDao.deleteTeacherById(id)

    suspend fun insertClass(schoolClass: SchoolClass) = schoolDao.insertClass(schoolClass)
    suspend fun updateClass(schoolClass: SchoolClass) = schoolDao.updateClass(schoolClass)
    suspend fun deleteClassById(id: Int) = schoolDao.deleteClassById(id)

    suspend fun insertNotice(notice: Notice) = schoolDao.insertNotice(notice)
    suspend fun deleteNoticeById(id: Int) = schoolDao.deleteNoticeById(id)
}
