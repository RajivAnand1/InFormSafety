package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;
import static com.example.informsafety.EncryptDecrypt.encrypt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.annotation.Nullable;

public class ChildProtectionNoteActivity extends AppCompatActivity {

    AutoCompleteTextView child;
    EditText note;
    Button save, date;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    String myKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Child Protection Note");
        setContentView(R.layout.activity_child_protection_note);

        // Get user and database refs
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();

        // Get references for form elements
        child = findViewById(R.id.child);
        date = findViewById(R.id.date);
        note = findViewById(R.id.note);
        save = findViewById(R.id.save);

        // Call button functions
        save.setOnClickListener(v -> ClickSave());


        // Autocomplete text + dropdown for Child
        ArrayList<String> childList = new ArrayList<>();
        ArrayAdapter childAdapter = new ArrayAdapter<String>(this, R.layout.list_item, childList);
        child.setAdapter(childAdapter);
        child.setThreshold(1);

        // Get child names
        DatabaseReference childRef = ref.child("Child");
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    childList.add(decrypt(snapshot.child("Name").getValue().toString()));
                }
                childAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Add a dropdown when clicked
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                child.showDropDown();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChildProtectionNoteActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_date, null);
                dialog.setView(dialogView);
                dialog.show();
            }
        });




        // If user opened a saved form, populate the form with the saved values
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myKey = extras.getString("Key");

            // Query the database for the clicked record
            DatabaseReference childProtRef = ref.child("Child Protection");
            Query myDraftQuery = childProtRef.orderByKey().equalTo(myKey);
            myDraftQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                        // Set form elements to show the saved values
                        child.setText(decrypt(snapshot.child("childName").getValue().toString()));
                        date.setText(snapshot.child("date").getValue().toString());
                        note.setText(decrypt(snapshot.child("note").getValue().toString()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }


    // When user clicks Save, add the entered information into Firebase Realtime Database
    private void ClickSave() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get UID of logged in user
                String myUID = mAuth.getCurrentUser().getUid();

                // Get text from form elements
                String myChild = child.getText().toString();
                String myDate = date.getText().toString();
                String myNote = note.getText().toString();

                // Create a HashMap of incident form contents
                HashMap<String, Object> map = new HashMap<>();
                map.put("userID", myUID);
                map.put("childName", encrypt(myChild));
                map.put("date", myDate);
                map.put("note", encrypt(myNote));

                // Insert to Realtime Database
                // If already created, update values instead
                if (myKey != null) {
                    ref.child("Child Protection").child(myKey).setValue(map);
                } else {
                    myKey = ref.child("Child Protection").push().getKey();
                    ref.child("Child Protection").child(myKey).setValue(map);
                }

                Toast.makeText(ChildProtectionNoteActivity.this, "Saved", Toast.LENGTH_SHORT).show();

            }
        });
    }
}