package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.*
import com.example.ui.screens.DashboardScreen
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val mockStudents = listOf(
        Student(id = 1, name = "Israel Mondonga", grade = "Grade 12-A", rollNumber = "EL-2026-001", email = "israel.m@school.edu", phone = "+243 897 1234", performanceIndex = 4.0, attendancePercentage = 98, feesPaid = true)
    )
    val mockTeachers = listOf(
        Teacher(id = 1, name = "Prof. Eluo Mondonga Sr.", subject = "Advanced Algorithms", email = "info.eluo@school.edu", phone = "+243 897 5555", roomNumber = "Lab 304", status = "Active")
    )
    val mockClasses = listOf(
        SchoolClass(id = 1, name = "Advanced Algorithms", code = "ALGO-401", scheduleTime = "Mon, Wed 10:00 AM", teacherName = "Prof. Eluo Mondonga Sr.", studentCount = 28)
    )
    val mockNotices = listOf(
        Notice(id = 1, title = "Welcome to ELsystem", content = "This is a clean academic management portal.", date = "June 20, 2026", priority = "Normal")
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        DashboardScreen(
            students = mockStudents,
            teachers = mockTeachers,
            classes = mockClasses,
            notices = mockNotices,
            onNavigateToTab = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
