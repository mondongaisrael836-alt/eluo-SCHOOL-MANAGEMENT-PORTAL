package com.example.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.data.Student
import com.example.data.AttendanceRecord
import java.io.File
import java.io.FileOutputStream

object PdfReportGenerator {

    /**
     * Generates an official report card PDF for a single student.
     */
    fun printStudentReportPdf(context: Context, student: Student): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size: 595 x 842 points
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        val textPaint = Paint()

        // 1. Draw border
        paint.color = Color.parseColor("#1E3A8A") // Dark Navy
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        canvas.drawRect(20f, 20f, 575f, 822f, paint)

        paint.strokeWidth = 1f
        canvas.drawRect(25f, 25f, 570f, 817f, paint)

        // 2. Headings & School Seal
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#1E3A8A")
        canvas.drawRect(40f, 40f, 555f, 130f, paint)

        textPaint.color = Color.WHITE
        textPaint.textSize = 24f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("ELsystem ROYAL ACADEMIC REPORT", 75f, 80f, textPaint)

        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        canvas.drawText("Direct Ledger Administration & Certification Portal", 75f, 105f, textPaint)

        // 3. Document Details
        textPaint.color = Color.BLACK
        textPaint.textSize = 14f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("STUDENT CHARACTER & ACADEMIC TRANSCRIPT", 40f, 170f, textPaint)

        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 11f
        
        var y = 210f
        val lineSpacing = 24f

        canvas.drawText("Full Name:  ${student.name}", 40f, y, textPaint)
        canvas.drawText("Roll Number ID:  ${student.rollNumber}", 320f, y, textPaint)
        y += lineSpacing

        canvas.drawText("Grade / Class Section:  ${student.grade}", 40f, y, textPaint)
        canvas.drawText("Scholastic Email:  ${student.email}", 320f, y, textPaint)
        y += lineSpacing

        canvas.drawText("Registered Phone:  ${student.phone}", 40f, y, textPaint)
        canvas.drawText("Academic Calendar:  Class of 2026", 320f, y, textPaint)
        y += 40f

        // 4. Performance Indexes Grid Header
        paint.color = Color.parseColor("#E5E7EB") // Light Gray
        canvas.drawRect(40f, y, 555f, y + 25f, paint)

        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("ACADEMIC METRIC", 50f, y + 17f, textPaint)
        canvas.drawText("INDEX RECORD", 280f, y + 17f, textPaint)
        canvas.drawText("COMPLIANCE STATUS", 420f, y + 17f, textPaint)
        y += 25f

        // Table Rows
        val metrics = listOf(
            Triple("Dynamic Performance GPA", "${student.performanceIndex} / 4.0 GPA", "Outstanding Merit"),
            Triple("Daily Session Attendance", "${student.attendancePercentage}% Ratio", if (student.attendancePercentage >= 90) "Excellent Attendance" else "Warning < 90%"),
            Triple("Tuition Escrow Payment", if (student.feesPaid) "Fully Cleared" else "Outstanding Overdue", if (student.feesPaid) "Synchronized" else "Debit Alert Pending")
        )

        metrics.forEach { (metric, value, compliance) ->
            y += 30f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(metric, 50f, y, textPaint)
            
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(value, 280f, y, textPaint)
            
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            if (compliance.contains("Warning") || compliance.contains("Debit")) {
                textPaint.color = Color.RED
            } else {
                textPaint.color = Color.parseColor("#059669") // Green
            }
            canvas.drawText(compliance, 420f, y, textPaint)
            textPaint.color = Color.BLACK
            
            paint.color = Color.parseColor("#F3F4F6")
            canvas.drawLine(40f, y + 8f, 555f, y + 8f, paint)
        }

        y += 60f

        // 5. Official Verification Signature block
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Issued by ELsystem Administrative Board", 40f, y, textPaint)
        canvas.drawText("Developer: eluo mondonga israel", 350f, y, textPaint)
        y += 20f

        paint.color = Color.BLACK
        canvas.drawLine(40f, y, 200f, y, paint)
        canvas.drawLine(350f, y, 510f, y, paint)
        y += 15f

        textPaint.textSize = 9f
        canvas.drawText("Authorized Academic Registrar", 40f, y, textPaint)
        canvas.drawText("Lead Architectural Developer", 350f, y, textPaint)

        pdfDocument.finishPage(page)

        // Save PDF to downloads directory
        return savePdfToDevice(context, pdfDocument, "ELsystem_${student.name.replace(" ", "_")}_Report.pdf")
    }

    /**
     * Generates a persistent daily session/monthly summary attendance record PDF.
     */
    fun printAttendanceSummaryPdf(context: Context, className: String, records: List<AttendanceRecord>): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        val textPaint = Paint()

        // Border
        paint.color = Color.parseColor("#059669") // Green
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        canvas.drawRect(20f, 20f, 575f, 822f, paint)

        // Header Rect
        paint.style = Paint.Style.FILL
        canvas.drawRect(40f, 40f, 555f, 130f, paint)

        textPaint.color = Color.WHITE
        textPaint.textSize = 22f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("ELsystem ATTENDANCE MASTER SHEET", 70f, 82f, textPaint)

        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        canvas.drawText("Unified Daily Sessions Record Ledgers", 70f, 107f, textPaint)

        // Title info
        textPaint.color = Color.BLACK
        textPaint.textSize = 13f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("ACTIVE COURSE ID:  $className", 40f, 170f, textPaint)

        val totalRecords = records.size
        val presentCount = records.count { it.isPresent }
        val absentCount = totalRecords - presentCount
        val ratio = if (totalRecords > 0) (presentCount * 100) / totalRecords else 100

        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 11f
        canvas.drawText("Total Registered Audits:  $totalRecords Logs", 40f, 195f, textPaint)
        canvas.drawText("Aggregated Present:  $presentCount Units", 40f, 215f, textPaint)
        canvas.drawText("Aggregated Absent:  $absentCount Units", 320f, 195f, textPaint)
        canvas.drawText("Attendance Settle Ratio:  $ratio% Compliance", 320f, 215f, textPaint)

        // Header Grid
        var y = 250f
        paint.color = Color.parseColor("#E5E7EB")
        paint.style = Paint.Style.FILL
        canvas.drawRect(40f, y, 555f, y + 25f, paint)

        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("DATE RECORDED", 50f, y + 17f, textPaint)
        canvas.drawText("STUDENT CANDIDATE NAME", 200f, y + 17f, textPaint)
        canvas.drawText("CHECKOUT STATE", 420f, y + 17f, textPaint)
        y += 25f

        // Print rows
        records.take(15).forEach { record ->
            y += 26f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(record.date, 50f, y, textPaint)
            canvas.drawText(record.studentName, 200f, y, textPaint)

            if (record.isPresent) {
                textPaint.color = Color.parseColor("#059669")
                canvas.drawText("✓ PRESENT", 420f, y, textPaint)
            } else {
                textPaint.color = Color.RED
                canvas.drawText("✗ ABSENT", 420f, y, textPaint)
            }
            textPaint.color = Color.BLACK

            paint.color = Color.parseColor("#F3F4F6")
            canvas.drawLine(40f, y + 6f, 555f, y + 6f, paint)
        }

        if (records.size > 15) {
            y += 28f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            canvas.drawText("... and ${records.size - 15} more attendance records logged successfully.", 50f, y, textPaint)
        }

        y += 50f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 10f
        canvas.drawText("Developer: eluo mondonga israel", 40f, y, textPaint)
        canvas.drawText("Verify security stamp via ELsystem admin dashboard", 300f, y, textPaint)

        pdfDocument.finishPage(page)
        return savePdfToDevice(context, pdfDocument, "Attendance_Summary_${className.replace(" ", "_")}.pdf")
    }

    private fun savePdfToDevice(context: Context, pdfDocument: PdfDocument, filename: String): File? {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, filename)
        try {
            if (!path.exists()) {
                path.mkdirs()
            }
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF Report Printed! Saved to Downloads: $filename", Toast.LENGTH_LONG).show()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: save to internal app cache
            try {
                val fallbackFile = File(context.cacheDir, filename)
                pdfDocument.writeTo(FileOutputStream(fallbackFile))
                Toast.makeText(context, "PDF saved to app cache: $filename", Toast.LENGTH_LONG).show()
                return fallbackFile
            } catch (ex: Exception) {
                Toast.makeText(context, "PDF print failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        } finally {
            pdfDocument.close()
        }
        return null
    }
}
