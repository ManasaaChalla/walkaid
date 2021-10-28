package com.example.walkaid.view.activity

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.walkaid.R
import android.widget.ArrayAdapter
import com.example.walkaid.data.Sessions
import com.example.walkaid.data.WalkAidDB
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {

    lateinit var l: ListView
    lateinit var database: WalkAidDB
    lateinit var sessions: List<Sessions>
//    var tutorials = arrayOf(
//        "Algorithms", "Data Structures",
//        "Languages", "Interview Corner",
//        "GATE", "ISRO CS",
//        "UGC NET CS", "CS Subjects",
//        "Web Technologies"
//    )
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd MMM HH:mm:ss")
        return format.format(date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        database = GlobalContext.getDatabaseInstance(this)

        sessions = database.sessionsDAO.getAllSessions()

        l = findViewById(R.id.list)
        val arr = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            sessions.map { sessions -> "${sessions.sessionId}: ${convertLongToTime(sessions.fromTimestamp)} - ${convertLongToTime(sessions.toTimestamp)}" }
        )
        l.setAdapter(arr)

    }
}