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

    // --- PORTAL DATA STRUCTURES & EXTENSIVE IN-MEMORY SESSION STATES ---

    // User Session Profile State
    private val _userProfile = kotlinx.coroutines.flow.MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    // School Payee Mobile Money Registration Phone
    private val _payeePhone = kotlinx.coroutines.flow.MutableStateFlow("+243 897 5555")
    val payeePhone: StateFlow<String> = _payeePhone

    // Store cart state: Product ID -> Quantity
    private val _cart = kotlinx.coroutines.flow.MutableStateFlow<Map<Int, Int>>(emptyMap())
    val cart: StateFlow<Map<Int, Int>> = _cart

    // Transaction Ledger (School incoming cash flow from store)
    private val _transactions = kotlinx.coroutines.flow.MutableStateFlow<List<StoreTransaction>>(listOf(
        StoreTransaction("TX-9281-AM", "Israel Mondonga", "+243 897 1234", "Advanced Algorithms (x1)", 62.0, "10 mins ago", "+243 897 5555"),
        StoreTransaction("TX-9220-SC", "Sophia Carter", "+1 555 1023", "RFID Badge & Uniform (x1)", 57.0, "2 hours ago", "+243 897 5555")
    ))
    val transactions: StateFlow<List<StoreTransaction>> = _transactions

    // Portal direct messaging logs
    private val _chatMessages = kotlinx.coroutines.flow.MutableStateFlow<List<MessageLog>>(listOf(
        MessageLog(1, "Prof. Eluo Mondonga Sr.", "Hello Israel, did you complete your Advanced Algorithms term project?", "09:42 AM", false),
        MessageLog(2, "Me", "Yes Professor! I implemented the balanced AVL Red-Black Trees with screenshot logs.", "09:44 AM", true),
        MessageLog(3, "Prof. Eluo Mondonga Sr.", "Outstanding. Bring the source log printouts to Lab 304 tomorrow.", "09:45 AM", false)
    ))
    val chatMessages: StateFlow<List<MessageLog>> = _chatMessages

    // Available Products Catalog
    val products = listOf(
        StoreProduct(1, "ELsystem Winter Blazer Uniform", "Official academic dark blue tailored blazer with golden heraldic standard crest.", 45.0, "Uniforms"),
        StoreProduct(2, "Advanced Algorithms (EL Edition)", "Second semester textbook by Prof. Eluo Mondonga Sr. with complete code challenges.", 62.0, "Textbooks"),
        StoreProduct(3, "Quantum Physics Student Manual", "Syllabus and safety handbook for labs by Dr. Elena Rostova.", 38.0, "Textbooks"),
        StoreProduct(4, "High Performance Student STEM Laptop", "Intel Core i5, 16GB RAM, 512GB SSD, durable school rugged chassis.", 399.0, "Technology"),
        StoreProduct(5, "ELsystem Smart Card ID Badge (RFID)", "Smart access pass for classrooms, library gates and digital payments.", 12.0, "Equipment"),
        StoreProduct(6, "Elite Level STEM Microcontroller Kit", "Includes breadboards, sensors, servos, and step-by-step experiment modules.", 65.0, "Equipment")
    )

    // Handlers
    fun signIn(name: String, email: String, role: String, phone: String) {
        _userProfile.value = UserProfile(
            name = name,
            email = email,
            role = role,
            phone = phone,
            bio = "Active $role of ELsystem Royal Academy.",
            isLoggedIn = true
        )
    }

    fun updateProfile(name: String, email: String, phone: String, bio: String) {
        _userProfile.value = _userProfile.value.copy(
            name = name,
            email = email,
            phone = phone,
            bio = bio
        )
    }

    fun signOut() {
        _userProfile.value = UserProfile()
        _cart.value = emptyMap()
    }

    fun registerPayeePhone(phone: String) {
        _payeePhone.value = phone
    }

    fun addToCart(productId: Int) {
        val current = _cart.value.toMutableMap()
        current[productId] = (current[productId] ?: 0) + 1
        _cart.value = current
    }

    fun removeFromCart(productId: Int) {
        val current = _cart.value.toMutableMap()
        val count = current[productId] ?: 0
        if (count <= 1) {
            current.remove(productId)
        } else {
            current[productId] = count - 1
        }
        _cart.value = current
    }

    fun clearCart() {
        _cart.value = emptyMap()
    }

    fun checkoutCart(buyerName: String, buyerPhone: String) {
        val currentCart = _cart.value
        if (currentCart.isEmpty()) return

        var sum = 0.0
        val itemsList = mutableListOf<String>()
        currentCart.forEach { (pid, qty) ->
            val p = products.find { it.id == pid }
            if (p != null) {
                sum += p.price * qty
                itemsList.add("${p.title.split(" ").take(2).joinToString(" ")} (x$qty)")
            }
        }

        val summaryStr = itemsList.joinToString(", ")
        val txId = "TX-${(1000..9999).random()}-${buyerName.split(" ").firstOrNull()?.uppercase() ?: "PAY"}"
        
        val newTx = StoreTransaction(
            id = txId,
            buyerName = buyerName,
            buyerPhone = buyerPhone,
            itemsSummary = summaryStr,
            amountPaid = sum,
            timestamp = "Just Now",
            payeePhone = _payeePhone.value
        )

        _transactions.value = listOf(newTx) + _transactions.value
        _cart.value = emptyMap()
    }

    fun sendMessage(text: String, contactName: String = "Prof. Eluo Mondonga Sr.") {
        if (text.isBlank()) return
        
        // Add user's dispatch
        val current = _chatMessages.value.toMutableList()
        val nextId = current.size + 1
        current.add(MessageLog(nextId, "Me", text, "Just Now", true))
        _chatMessages.value = current

        // Auto Faculty AI / Scripted response simulation
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            val updated = _chatMessages.value.toMutableList()
            val responseId = updated.size + 1
            val replyText = when {
                text.lowercase().contains("fees") || text.lowercase().contains("pay") -> 
                    "Thank you for contacting administrative support regarding digital store payments. All mobile payments register instantly under the Wallet Ledger."
                text.lowercase().contains("homework") || text.lowercase().contains("project") || text.lowercase().contains("grade") ->
                    "Excellent query. Grades are being compiled for the upcoming performance review. Please verify that your files are uploaded."
                text.lowercase().contains("uniform") || text.lowercase().contains("book") || text.lowercase().contains("buy") ->
                    "Supplies ordered through the Campus Store will be available for pickup at the Main Hall Bookstore once payment clears."
                else -> "Your dispatch has been received. Let's arrange a time to review this. Please keep up the good work!"
            }
            updated.add(MessageLog(responseId, contactName, replyText, "Just Now", false))
            _chatMessages.value = updated
        }
    }
}

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "Student",
    val bio: String = "",
    val isLoggedIn: Boolean = false
)

data class StoreProduct(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val availableStock: Int = 100
)

data class StoreTransaction(
    val id: String,
    val buyerName: String,
    val buyerPhone: String,
    val itemsSummary: String,
    val amountPaid: Double,
    val timestamp: String,
    val payeePhone: String
)

data class MessageLog(
    val id: Int,
    val senderName: String,
    val text: String,
    val time: String,
    val isFromMe: Boolean
)

