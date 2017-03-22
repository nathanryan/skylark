package com.nryan.skylark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nathan Ryan x13448212 on 19/02/2017.
 */

public class BirdsSeenFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Birds");
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.birds_seen_fragment, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);

        DatabaseReference birdRef = databaseReference.child(user.getUid());

        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(this.getActivity(), String.class, android.R.layout.simple_list_item_1, birdRef) {
            @Override
            protected void populateView(View v, String model, int position) {

                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

        listView.setAdapter(firebaseListAdapter);
        return view;
    }
}
