package com.conor.paddycastore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Model.Stock;
import com.conor.paddycastore.ViewHolder.StockViewHolderAdmin;
import com.conor.paddycastore.searches.SearchCustomers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class HomePageAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    EditText etProductName, etProductPrice, etProductDescription, etProductCategory, etProductManufacturer;
    ImageView productImage;
    Button btnSelect;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference stockList;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Stock, StockViewHolderAdmin> adapter;

    Uri saveUri;
    Stock newStock;

    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Firebase Init
        database = FirebaseDatabase.getInstance();
        stockList = database.getReference("Stock");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Load Food list
        recyclerView = (RecyclerView)findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);


        loadMenu();
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Stock, StockViewHolderAdmin>(
                Stock.class,
                R.layout.stock_item_admin,
                StockViewHolderAdmin.class,
                stockList)
        {
            @Override
            protected void populateViewHolder(StockViewHolderAdmin viewHolder, Stock model, final int position) {
                viewHolder.tvProductName.setText(model.getProductName());
                viewHolder.tvProductCategory.setText(model.getCategory());
                viewHolder.tvProductDescription.setText(model.getDescription());
                viewHolder.tvProductManufacturer.setText(model.getManufacturer());
                viewHolder.tvProductPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.productImage);

                viewHolder.editProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProduct(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });
            }
        };

        //Set Adapter
        recyclerView.setAdapter(adapter);
    }

    private void updateProduct(final String key, final Stock item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomePageAdmin.this);
        alertDialog.setTitle("Update Product");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View edit_product_layout = inflater.inflate(R.layout.activity_add_stock, null);

        etProductName = edit_product_layout.findViewById(R.id.etProductName);
        etProductCategory = edit_product_layout.findViewById(R.id.etCategory);
        etProductDescription = edit_product_layout.findViewById(R.id.etDescription);
        etProductManufacturer = edit_product_layout.findViewById(R.id.etManufacturer);
        etProductPrice = edit_product_layout.findViewById(R.id.etPrice);
        btnSelect = edit_product_layout.findViewById(R.id.btnSelect);
        productImage = edit_product_layout.findViewById(R.id.addImageView);

        //Set default name
        etProductName.setText(item.getProductName());
        etProductCategory.setText(item.getCategory());
        etProductManufacturer.setText(item.getManufacturer());
        etProductDescription.setText(item.getDescription());
        etProductPrice.setText(item.getPrice());
        Picasso.with(getBaseContext()).load(item.getImage())
                .into(productImage);


        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //let user select image from the gallery and save the URI of the image
            }
        });

        alertDialog.setView(edit_product_layout);
        alertDialog.setIcon(R.drawable.ic_edit_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                changeImage(item, key);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialog.show();
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
            btnSelect.setText("Image Selected!");
            Picasso.with(getBaseContext()).load(data.getData())
                    .into(productImage);
        }
    }

    private void changeImage(final Stock item, final String key) {
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
                            Toast.makeText(HomePageAdmin.this, "Uploaded!! ", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Update information
                                    item.setProductName(etProductName.getText().toString());
                                    item.setPrice(etProductPrice.getText().toString());
                                    item.setDescription(etProductDescription.getText().toString());
                                    item.setCategory(etProductCategory.getText().toString());
                                    item.setManufacturer(etProductManufacturer.getText().toString());
                                    item.setImage(uri.toString());
                                    stockList.child(key).setValue(item);
                                    loadMenu();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(HomePageAdmin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_shop) {
            // Handle the camera action
        } else if (id == R.id.nav_add_stock) {
            Intent intent = new Intent(HomePageAdmin.this, AddStock.class);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {

            Intent intent = new Intent(HomePageAdmin.this, ViewOrders.class);
            startActivity(intent);

        } else if (id == R.id.nav_searchCustomers) {

            Intent intent = new Intent(HomePageAdmin.this, SearchCustomers.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            Intent logout = new Intent(HomePageAdmin.this, MainActivity.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
