package com.example.testcsv

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testcsv.databinding.ActivityMainBinding
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val data = ArrayList<String>()
        data.add("1,John,Doe")
        data.add("2,Jane,Smith")
        val fileName = "mydata.csv"

        binding.btnCsvDownload.setOnClickListener {
            createCsvFile(this, data, fileName)
        }
    }

    fun createCsvFile(context: Context, data: ArrayList<String>, fileName: String) {
        val folderPath = "${context.getExternalFilesDir(null)?.absolutePath}/Download"

        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val file = File(folderPath, fileName)

        try {
            FileWriter(file).use { writer ->
                for (item in data) {
                    writer.append(item)
                    writer.append("\n")
                }
                writer.flush()
            }

            Log.d("파일 저장 성공", "")

        } catch (e: IOException) {
            Log.d("파일 저장 실패", e.message.toString())

        }
    }
}