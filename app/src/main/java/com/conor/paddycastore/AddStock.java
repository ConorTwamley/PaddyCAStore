package com.conor.paddycastore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddStock extends AppCompatActivity {

    EditText productName, manufacturer, category, price, quanity, description;
    ImageView imageView;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference drinkList;
    StorageReference storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        //Firebase Init
        database = FirebaseDatabase.getInstance();
        drinkList = database.getReference("Venues");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
}
