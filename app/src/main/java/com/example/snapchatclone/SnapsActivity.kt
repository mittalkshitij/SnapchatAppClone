package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.snapchatclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class SnapsActivity : AppCompatActivity() {

    val mAuth= FirebaseAuth.getInstance()

    var snapsListView:ListView?=null
    var emails: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)

        snapsListView=findViewById(R.id.snapsListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        snapsListView?.adapter = adapter

        mAuth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().reference.child("users").child(
                it
            ).child("snaps").addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        emails.add(snapshot.child("from").value as String)
                        adapter.notifyDataSetChanged()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.snaps,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item?.itemId==R.id.createSnap)
        {
            val intent=Intent(this,CreateSnapActivity::class.java)
            startActivity(intent)

        }else if (item?.itemId==R.id.logout)
        {
            mAuth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }
}