package com.example.ganesh.ghour.viewpagers;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ganesh.ghour.ChatActivity;
import com.example.ganesh.ghour.HorizontalAdapter;
import com.example.ganesh.ghour.Incident;
import com.example.ganesh.ghour.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    private RecyclerView horizontalList;
    private RecyclerView verticalList;
    private HorizontalAdapter horizontalAdapter;
    private LinearLayout mInsideLinerlayout;


    //  firebase database connection
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentuser;

//  variables from design

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


//      firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("incident");
        mDatabase.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser().getUid();

        horizontalList = (RecyclerView) rootView.findViewById(R.id.horizontal_recycler_view);
        verticalList = (RecyclerView) rootView.findViewById(R.id.vertical_recycler_view);

//        setting horizontal layout
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalList.setLayoutManager(horizontalLayoutManager);
        horizontalList.setHasFixedSize(true);
        horizontalAdapter = new HorizontalAdapter(getActivity());
        horizontalList.setAdapter(horizontalAdapter);

//        setting vertical layout
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        verticalLayoutManager.setReverseLayout(true);
        verticalLayoutManager.setStackFromEnd(true);
        horizontalList.setHasFixedSize(true);
        verticalList.setLayoutManager(verticalLayoutManager);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Incident, VerticalPostHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Incident, VerticalPostHolder>(
                Incident.class,
                R.layout.single_post_layout,
                VerticalPostHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(VerticalPostHolder viewHolder, Incident model, int position) {
                final String incident_key = getRef(position).getKey();

                viewHolder.setDetails(model.getDetails());
                viewHolder.setName(model.getName());

                //go for chat
                viewHolder.mChatImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("incidentkey", incident_key);
                        Intent chatTntent = new Intent(getContext(), ChatActivity.class);
                        chatTntent.putExtra("xy", bundle);
                        startActivity(chatTntent);
                    }
                });

            }
        };

        verticalList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class VerticalPostHolder extends RecyclerView.ViewHolder {
        View mView;
        private ImageButton mChatImageButton;


        public VerticalPostHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mChatImageButton = (ImageButton) mView.findViewById(R.id.card_view_comment_button);

        }

        public void setDetails(String details) {
            TextView incident_details = (TextView) mView.findViewById(R.id.card_view_incident_description);
            incident_details.setText(details);
        }

        public void setName(String name) {
            TextView incident_name = (TextView) mView.findViewById(R.id.card_view_user_name);
            incident_name.setText(name);
        }

    }


}
