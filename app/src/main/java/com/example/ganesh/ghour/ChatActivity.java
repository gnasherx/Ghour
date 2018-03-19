package com.example.ganesh.ghour;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private TextView mChatTextViewUsername;
    private TextView mIncidentDescription;
    private RecyclerView recyclerview;
    private EditText mChatEditText;
    private RecyclerView mChatRecyclerview;
    private ImageButton mChatSendButton;

    //    firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentuser;
    private String incidentKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getBundleExtra("xy");
        incidentKey = bundle.getString("incidentkey");

        initializeScreen();


        mChatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statChatForThisIncident();
            }
        });

    }


    private void statChatForThisIncident() {
        final String incidentChat = mChatEditText.getText().toString();
        if (!TextUtils.isEmpty(incidentChat)) {

            DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser);
            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseReference chatIncidentRef = mDatabase.push();
                    chatIncidentRef.keepSynced(true);

                    String chat_username = String.valueOf(dataSnapshot.child("name").getValue());
                    chatIncidentRef.child("name").setValue(chat_username);
                    chatIncidentRef.child("chat").setValue(incidentChat);

                    mChatEditText.setText("");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: Error while chating!");
                }
            });

        }
    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);

        mChatTextViewUsername = (TextView) findViewById(R.id.chat_text_view_username);
        mIncidentDescription = (TextView) findViewById(R.id.chat_text_view_description);
        recyclerview = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mChatEditText = (EditText) findViewById(R.id.chat_edit_text);
        mChatSendButton = (ImageButton) findViewById(R.id.chat_send_image_button);
        mChatRecyclerview = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mChatRecyclerview.setHasFixedSize(true);
        mChatRecyclerview.setLayoutManager(new LinearLayoutManager(this));

//        firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(incidentKey);
        Log.d(TAG, "initializeScreen: mdatabase="+mDatabase);

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getUid();
        mDatabase.keepSynced(true);
    }


    //    fetching chat data
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Chat, ChatHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, ChatHolder>(
                Chat.class,
                R.layout.single_chat_layout,
                ChatHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ChatHolder viewHolder, Chat model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setChat(model.getChat());
            }
        };

        mChatRecyclerview.setAdapter(firebaseRecyclerAdapter);

    }

    public void onStop() {
        super.onStop();
    }


    public static class ChatHolder extends RecyclerView.ViewHolder {
        View mView;

        public ChatHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
        }

        public void setName(String name) {
            TextView name_text = (TextView) mView.findViewById(R.id.single_chat_name);
            name_text.setText(name);
        }

        public void setChat(String chat) {
            TextView chat_text = (TextView) mView.findViewById(R.id.single_chat_details);
            chat_text.setText(chat);
        }
    }


}
