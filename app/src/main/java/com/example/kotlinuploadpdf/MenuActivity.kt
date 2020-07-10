package com.example.kotlinuploadpdf

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {
    lateinit var btnFetch: Button;
    lateinit var btnUpload:Button
    lateinit var btnViewMyfile:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.kotlinuploadpdf.R.layout.activity_menu)

        btnFetch = findViewById(com.example.kotlinuploadpdf.R.id.btnFetch)
        btnUpload = findViewById(com.example.kotlinuploadpdf.R.id.btnUpload)
        btnViewMyfile = findViewById(com.example.kotlinuploadpdf.R.id.btnViewMyfile)

        btnUpload.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val a = Intent(this@MenuActivity, MainActivity::class.java)
                startActivity(a)
            }
        })
        btnFetch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val a = Intent(this@MenuActivity, FetchActivity::class.java)
                startActivity(a)
            }
        })

        btnViewMyfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val a = Intent(this@MenuActivity, MyFiles2Activity::class.java)
                startActivity(a)
            }
        })

    }
}
