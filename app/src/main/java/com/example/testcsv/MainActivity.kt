package com.example.testcsv

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        // 권한 확인
        checkPermissions()

        // csv 데이터
        // 파일명 지정
        val data = getCsvData()
        val csvFileName = "csvFileTest.csv"

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

//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) { // WRITE_EXTERNAL_STORAGE 권한이 부여되어있는 상태가 아닌 경우
                ActivityCompat.requestPermissions(this, arrayOf(permission), 500) // 500은 임의 지정한 코드값

//            } else { // 권한이 이미 허용된 경우
//                createEmptyTxtFile("emptyFile.txt")
//            }
        } /* else { // 안드로이드 버전이 M 이하인 경우
            createEmptyTxtFile("emptyFile.txt")
        } */
    }

    // onRequestPermissionsResult 메소드 추가
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 500) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 권한이 허용된 경우
//                createEmptyTxtFile("emptyFile.txt") // 왜 2번하고있지?

            } else { // 권한이 거부된 경우
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_LONG).show()

            }
        }
    }


    fun getCsvData() : ArrayList<String> {
        val data = ArrayList<String>()
        data.add("1,John,Doe")
        data.add("2,Jane,Smith")

        return data
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