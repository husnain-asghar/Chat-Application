package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView:RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth:FirebaseAuth
    private lateinit var myDatabaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList=ArrayList()
        adapter=UserAdapter(this,userList)
        mAuth=FirebaseAuth.getInstance()
        myDatabaseReference=FirebaseDatabase.getInstance().getReference()
        userRecyclerView=findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adapter
//Getting data from fire base data base
        myDatabaseReference.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //emptying the list every time before running to ensure latest data is saved in list
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)

                    //so you donot chat with yourself
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)//make this null safe or error

                    }


                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            mAuth.signOut()
            val intent=Intent(this@MainActivity,Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}