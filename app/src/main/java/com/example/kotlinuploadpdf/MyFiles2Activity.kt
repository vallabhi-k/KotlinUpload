package com.example.kotlinuploadpdf

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MyFiles2Activity : AppCompatActivity() {
   lateinit var recycle: RecyclerView
   lateinit var myRef: DatabaseReference
    var ab = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.kotlinuploadpdf.R.layout.activity_fetch)
        recycle = findViewById(com.example.kotlinuploadpdf.R.id.recycle)
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
        myRef = FirebaseDatabase.getInstance().reference.child(currentuser)
        myRef!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                dataSnapshot: DataSnapshot,
                @Nullable s: String?
            ) {
                val fileName = dataSnapshot.child("name").getValue(
                    String::class.java
                )!!
                val url =
                    dataSnapshot.child("file").getValue(String::class.java)!!
                val acc = dataSnapshot.child("access").value as Long?
                if (acc != null) {
                    if (acc == 1L) (recycle.getAdapter() as AdapterClass?)!!.update(
                        fileName,
                        url,
                        ab
                    )
                }
            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                @Nullable s: String?
            ) {
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(
                dataSnapshot: DataSnapshot,
                @Nullable s: String?
            ) {
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val itemClickListener: ItemClickListener = object : ItemClickListener {
            override fun onClick(view: View, position: Int, longclick: Boolean) {
            }
        }
        recycle.setLayoutManager(LinearLayoutManager(this@MyFiles2Activity))
        val obj = AdapterClass(
            recycle,
            this@MyFiles2Activity,
            ArrayList(),
            ArrayList(),
            ab,
            itemClickListener
        )
        recycle.setAdapter(obj)
    }
}
