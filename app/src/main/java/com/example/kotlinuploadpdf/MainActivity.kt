package com.example.kotlinuploadpdf

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var btnSelect:Button
    lateinit var upload:FloatingActionButton
    lateinit var fileSelected:TextView
    lateinit var progressBar:ProgressBar
    lateinit var coffee: CheckBox

    lateinit var pdfLink: Uri
     var id: Long = 0
     var pri: Int =0
    lateinit var mStorageRef: StorageReference
    lateinit var myRef: DatabaseReference

    //val currentuser: String ="User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelect = findViewById(R.id.Select)
        upload = findViewById(R.id.upload)
        fileSelected = findViewById(R.id.fileSelected)
        progressBar = findViewById(R.id.progressBar)

        btnSelect.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                selectFile()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    6
                )
            }
        }

        upload.setOnClickListener {
            if (pdfLink != null)
                uploadFile()
            else
                Toast.makeText(this@MainActivity, "Select a file", Toast.LENGTH_SHORT).show()
        }


    }

    private fun uploadFile() {

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid


        if(btnprivate.isChecked)
        {
            pri=1
            Toast.makeText(this@MainActivity, "Hello", Toast.LENGTH_SHORT).show()

        }
        progressBar.progress = 0
        val file = pdfLink
        mStorageRef = FirebaseStorage.getInstance().reference
        ++id
        val fileName = (System.currentTimeMillis()).toString() + ""

        val fileName1 = getRealPathFromURI(file)

        mStorageRef.child("Uploads").child(fileName).putFile(file)
        .addOnSuccessListener(object: OnSuccessListener<UploadTask.TaskSnapshot> {
        override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
        val firebaseUri = taskSnapshot.storage.downloadUrl
        firebaseUri.addOnSuccessListener(object: OnSuccessListener<Uri> {
            override fun onSuccess(uri:Uri) {
            val downloadUrl = uri.toString()
 // complete the rest of your code
            if (pri == 1)
            {
            myRef = FirebaseDatabase.getInstance().reference
            myRef.child(currentuser).child(fileName).child("file").setValue(downloadUrl).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(this@MainActivity, "File uploaded Successfully ", Toast.LENGTH_SHORT).show()
        }    else {
        Toast.makeText(this@MainActivity, "File Not uploaded va Successfully $task", Toast.LENGTH_SHORT).show()

    }
}
        myRef.child(currentuser).child(fileName).child("name").setValue(fileName1)
        myRef.child(currentuser).child(fileName).child("access").setValue(pri)

    }
        else
        {
        myRef = FirebaseDatabase.getInstance().reference.child("public")
        myRef.child(fileName).child("file").setValue(downloadUrl).addOnCompleteListener(object:
        OnCompleteListener<Void> {
        override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful)
        {
        Toast.makeText(this@MainActivity, "File uploaded Successfully ", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this@MainActivity, "File Not uploaded va Successfully $task", Toast.LENGTH_SHORT).show()

        }
        }
        })
        myRef.child(fileName).child("name").setValue(fileName1)

        myRef.child(fileName).child("access").setValue(pri)
    }

    }
        })

        }
        })
        .addOnFailureListener(object: OnFailureListener {
        override fun onFailure(exception:Exception) {
        Toast.makeText(this@MainActivity, "File Not uploaded Successfully $exception", Toast.LENGTH_SHORT).show()


        }
        }).addOnProgressListener(object: OnProgressListener<UploadTask.TaskSnapshot> {
        override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {

        val c = (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            progressBar.progress = c

        }
        })

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 6 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFile()
        } else {
            Toast.makeText(this, "Allow permission to access files", Toast.LENGTH_SHORT).show()
        }

    }

    private fun selectFile() {
        var a = Intent()

        a=getFileChooserIntentForImageAndPdf()
        startActivityForResult(a, 7)
       }
    fun getFileChooserIntentForImageAndPdf(): Intent {
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/* video/* |application/pdf")
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7 && resultCode == Activity.RESULT_OK && data != null) {
            pdfLink = data.data!!
            val displayName = getRealPathFromURI(pdfLink)
            fileSelected.text = "" + displayName!!
        } else {
            Toast.makeText(this, "Please Select a file", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getRealPathFromURI(uri: Uri): String? {
        val uriString = uri.toString()
        val myFile = File(uriString)
        val path = myFile.absolutePath
        var displayName: String? = null

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = this@MainActivity.contentResolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        return displayName
    }


}
