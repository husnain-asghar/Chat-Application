package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView:RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var myDatabaseReference: DatabaseReference
    var receiverRoom: String?=null
    var senderRoom: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatRecyclerView=findViewById(R.id.charRecyclerView)
        messageBox= findViewById(R.id.messageBox)
        sendButton=findViewById(R.id.sendImage)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)
        myDatabaseReference=FirebaseDatabase.getInstance().getReference()
        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
        val name=intent.getStringExtra("name")
        val receiverUid=intent.getStringExtra("uid")

        senderRoom= receiverUid + senderUid
        receiverRoom=senderUid+receiverUid
//adding name to tool bar
        supportActionBar?.title=name

        myDatabaseReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        sendButton.setOnClickListener{
            val message =messageBox.text.toString()
            val messageObject=Message(message,senderUid)
            myDatabaseReference.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    myDatabaseReference.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            //making send box empty
            messageBox.setText("")
        }
    }
}