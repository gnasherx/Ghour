package com.example.ganesh.ghour.viewpagers;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ganesh.ghour.EditProfileActivity;
import com.example.ganesh.ghour.Help;
import com.example.ganesh.ghour.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    //    private Button mShareToFriendsBtn;
    private TextView mUserNameTextView;
    private TextView mUserWorkTextView;
    private TextView mGoToEditProfileTextView;
    private RecyclerView mRecyclerView;
    private boolean hidden = true;

    //    Firebase Database
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabasePeopleHelp;
    private FirebaseAuth mAuth;
    private String currentuser;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

//        this is just tp..
//        mShareToFriendsBtn=(Button)rootview.findViewById(R.id.share_to_friends);
//        mShareToFriendsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                String title="Need Your help";
//                String details="I need your help,Please help me";
//                intent.putExtra(Intent.EXTRA_SUBJECT,title);
//                intent.putExtra(Intent.EXTRA_TEXT,details);
//                startActivity(Intent.createChooser(intent,"Help"));
//            }
//        });


        mUserNameTextView = (TextView) rootview.findViewById(R.id.profile_name);
        mUserWorkTextView = (TextView) rootview.findViewById(R.id.profile_work);
        mGoToEditProfileTextView = (TextView) rootview.findViewById(R.id.profile_edit_profile_textview);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.help_recyclerview);


        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser);
        mDatabasePeopleHelp = FirebaseDatabase.getInstance().getReference().child("addhelp").child(currentuser);


        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String work = (String) dataSnapshot.child("work").getValue();
                mUserNameTextView.setText(name);
                mUserWorkTextView.setText(work);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled:Unable to retrive name and work of user! ");
            }
        });

        mGoToEditProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        verticalLayoutManager.setReverseLayout(true);
        verticalLayoutManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(verticalLayoutManager);


        return rootview;
    }




    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Help,VerticalHelpAdapter> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Help, VerticalHelpAdapter>(
                Help.class,
                R.layout.single_help_layout,
                VerticalHelpAdapter.class,
                mDatabasePeopleHelp

        ) {
            @Override
            protected void populateViewHolder(VerticalHelpAdapter viewHolder, Help model, int position) {
                viewHolder.setName(model.getName());
                Log.d(TAG, "populateViewHolder: name="+model.getName());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setContact(model.getContact());
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public void showMe() {
        FirebaseRecyclerAdapter<Help, VerticalHelpAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Help, VerticalHelpAdapter>(
                Help.class,
                R.layout.single_help_layout,
                VerticalHelpAdapter.class,
                mDatabasePeopleHelp

        ) {
            @Override
            protected void populateViewHolder(VerticalHelpAdapter viewHolder, Help model, int position) {
                viewHolder.setName(model.getName());
                Log.d(TAG, "populateViewHolder: name=" + model.getName());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setContact(model.getContact());
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class VerticalHelpAdapter extends RecyclerView.ViewHolder {
        View mView;

        public VerticalHelpAdapter(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView help_name = (TextView) mView.findViewById(R.id.help_name_textview);
            help_name.setText(name);
        }

        public void setContact(String contact) {
            TextView help_email = (TextView) mView.findViewById(R.id.help_contact_textview);
            help_email.setText(contact);
        }

        public void setEmail(String email) {
            TextView help_contact = (TextView) mView.findViewById(R.id.help_email_textview);
            help_contact.setText(email);
        }


    }

}
