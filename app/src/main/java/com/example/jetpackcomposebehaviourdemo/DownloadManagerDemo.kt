package com.example.jetpackcomposebehaviourdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class DownloadManagerDemo : AppCompatActivity() {
    private lateinit var downloader: Downloader;
    private lateinit var urlGetter: EditText;
    private lateinit var downloadButt: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_manager_demo)
        downloader = Downloader(this);
        urlGetter = findViewById(R.id.DownloadLinkInput);
        downloadButt = findViewById(R.id.DownloadButton);
        downloadButt.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View?) {
                val str = urlGetter.text.toString();
                if(!str.isEmpty()){
                    downloader.DownloadFile(str);
                    Toast.makeText(this@DownloadManagerDemo, "File is Downloaded Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this@DownloadManagerDemo, "Input a link", Toast.LENGTH_SHORT).show();
                }
            }
        })
    }
}