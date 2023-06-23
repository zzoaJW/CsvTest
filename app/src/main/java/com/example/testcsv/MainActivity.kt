package com.example.testcsv

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testcsv.databinding.ActivityMainBinding
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 권한 확인
        checkPermissions()

        // csv 데이터
        val data = getCsvData()
        // 파일명 지정 (csvFileTest_yyyyMMdd_HHmmss.csv)
        val csvFileName = getCsvFileName()

        binding.btnCsvDownload.setOnClickListener {
            // csv 파일 생성
            createCsvFile(this, data, csvFileName)
        }

        // 파일명 지정
        val txtFileName = "txtFileTest.txt"

        binding.btnTxtDownload.setOnClickListener {
            // 빈 txt 파일 생성
            createEmptyTxtFile(txtFileName)
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 안드로이드 버전이 M 이상인 경우
            val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) { // WRITE_EXTERNAL_STORAGE 권한이 부여되어있는 상태가 아닌 경우
                ActivityCompat.requestPermissions(this, arrayOf(permission), 500) // 500은 임의 지정한 코드값
            }
        }
    }

    // onRequestPermissionsResult 메소드 추가
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 500) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) { // 권한이 거부된 경우
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun getCsvData() : ArrayList<String> {
        val data = ArrayList<String>()
        // 첫번째 추가 요소 : 결과지의 컬럼명들
        data.add("DATETIME,CAL1,CAL2,CAL3,CAL4")
        data.add("2020-01-01 09:50:00,VAL1,VAL2,VAL3,VAL4")
        data.add("2020-03-01 23:00:00,VAL1,VAL2,VAL3,VAL4")
        data.add("2021-02-01 13:20:00,VAL1,VAL2,VAL3,VAL4")
        data.add("2022-05-17 06:30:00,VAL1,VAL2,VAL3,VAL4")
        data.add("2023-06-23 17:00:00,VAL1,VAL2,VAL3,VAL4")

        return data
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCsvFileName() : String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val formattedCurrentTime = currentTime.format(formatter)

        val fileName = "csvFileTest_$formattedCurrentTime.csv"

        return fileName
    }

    fun createCsvFile(context: Context, data: ArrayList<String>, fileName: String) {
        val folderPath = "${Environment.getExternalStorageDirectory().absolutePath}/Download"

        val folder = File(folderPath)

        if (!folder.exists()) { // /내장메모리/Download가 유효한 folder인지 확인
            folder.mkdirs() // 없으면 만들기
        }

        val file = File(folderPath, fileName) // 파일경로, 파일명 지정 File 객체 생성

        try {
            FileWriter(file).use { writer ->
                for (item in data) { // ArrayList 순회하면서 데이터
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

    fun createEmptyTxtFile(fileName: String) {
        val folderPath = "${Environment.getExternalStorageDirectory().absolutePath}/Download"
        val file = File(folderPath, fileName)

        try {
            file.createNewFile()
            Log.d("파일 저장 성공", "")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("파일 저장 실패", e.message.toString())

        }
    }
}