package com.conor.paddycastore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Model.Stock;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddStock extends AppCompatActivity {

    EditText productName, manufacturer, category, price, quantity, description;
    ImageView imageView;
    Button btnAddImage,btnAddProduct;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference stockList;
    StorageReference storageReference;
    FirebaseStorage storage;

    Uri saveUri;
    Stock newStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        //Firebase Init
        database = FirebaseDatabase.getInstance();
        stockList = database.getReference("Stock");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        productName = findViewById(R.id.etProductName);
        manufacturer = findViewById(R.id.etManufacturer);
        category = findViewById(R.id.etCategory);
        price = findViewById(R.id.etPrice);
        quantity = findViewById(R.id.etQuantity);
        description = findViewById(R.id.etDescription);
        imageView = findViewById(R.id.addImageView);
        btnAddImage = findViewById(R.id.btnSelect);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveUri != null) {
                    uploadImage();
                }
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnAddImage.setText("Image Selected!");
            Picasso.with(getBaseContext()).load(data.getData())
                    .into(imageView);
        }
    }

    private void uploadImage() {
        if(saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(AddStock.this, "Uploaded!! ", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newStock = new Stock();
                                    newStock.setProductName(productName.getText().toString());
                                    newStock.setManufacturer(manufacturer.getText().toString());
                                    newStock.setCategory(category.getText().toString());
                                    newStock.setDescription(description.getText().toString());
                                    newStock.setPrice(price.getText().toString());
                                    newStock.setImage(uri.toString());

                                    if(newStock != null) {
                                        stockList.push().setValue(newStock);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Error uploading Stock to database!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AddStock.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+ "%");


                        }
                    });
        }
    }
}
