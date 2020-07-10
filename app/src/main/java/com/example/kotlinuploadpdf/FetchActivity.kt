package com.example.kotlinuploadpdf

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*

class FetchActivity : AppCompatActivity() {
    lateinit var recycle: RecyclerView
    var myRef: DatabaseReference? = null
    var ab = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch)
        recycle = findViewById(R.id.recycle)
        //final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //myRef= FirebaseDatabase.getInstance().getReference().child(currentuser);
        myRef = FirebaseDatabase.getInstance().reference.child("public")
        myRef!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                dataSnapshot: DataSnapshot,
                s: String?
            ) {
                val fileName = dataSnapshot.child("name").getValue(
                    String::class.java
                )!!
                val url = dataSnapshot.child("file").getValue(
                    String::class.java
                )!!
                val acc = dataSnapshot.child("access").value as Long?
                if (acc != 1L) (recycle.getAdapter() as AdapterClass?)!!.update(fileName, url, ab)
            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                s: String?
            ) {
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(
                dataSnapshot: DataSnapshot,
                s: String?
            ) {
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val itemClickListener: ItemClickListener = object : ItemClickListener {
            override fun onClick(
                view: View,
                position: Int,
                longclick: Boolean
            ) {
              }
        }
        recycle.setLayoutManager(LinearLayoutManager(this@FetchActivity))
        val obj = AdapterClass(
            recycle,
            this@FetchActivity,
            ArrayList(),
            ArrayList(),
            ab,
            itemClickListener
        )
        recycle.setAdapter(obj)
    }
}