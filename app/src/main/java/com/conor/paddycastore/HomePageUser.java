package com.conor.paddycastore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Interfaces.ItemClickListener;
import com.conor.paddycastore.Model.Rating;
import com.conor.paddycastore.Model.Stock;
import com.conor.paddycastore.ViewHolder.StockViewHolderUser;
import com.conor.paddycastore.searches.SearchManufacturerActivity;
import com.conor.paddycastore.searches.SearchPriceActivity;
import com.conor.paddycastore.searches.SearchProductNameActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class HomePageUser extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager mylayoutManager;
    RecyclerView.LayoutManager manufacturerLayout;
    RecyclerView.LayoutManager pricelayoutManager;

    Button sortTitle, sortManufacturer, sortPrice;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference stockList;
    DatabaseReference ratingTbl;

    String productId="";

    FirebaseRecyclerAdapter<Stock, StockViewHolderUser> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sortTitle = (Button)findViewById(R.id.sortByTitle);
        sortManufacturer = (Button)findViewById(R.id.sortByManufacturer);
        sortPrice = (Button)findViewById(R.id.sortByPrice);

        //Firebase Init
        database = FirebaseDatabase.getInstance();
        stockList = database.getReference("Stock");
        ratingTbl = database.getReference("Rating");

        //Load Food list
        recyclerView = (RecyclerView)findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        loadMenu();

        sortTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylayoutManager = new LinearLayoutManager(HomePageUser.this);
                ((LinearLayoutManager) mylayoutManager).setReverseLayout(true);
                ((LinearLayoutManager) mylayoutManager).setStackFromEnd(true);
                recyclerView.setLayoutManager(mylayoutManager);
                loadMenu();
            }
        });

        sortManufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manufacturerLayout = new LinearLayoutManager(HomePageUser.this);
                ((LinearLayoutManager) manufacturerLayout).setReverseLayout(true);
                ((LinearLayoutManager) manufacturerLayout).setStackFromEnd(true);
                recyclerView.setLayoutManager(manufacturerLayout);
                loadMenuManufacturerAscending();
            }
        });

        sortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pricelayoutManager = new LinearLayoutManager(HomePageUser.this);
                ((LinearLayoutManager) pricelayoutManager).setReverseLayout(true);
                ((LinearLayoutManager) pricelayoutManager).setStackFromEnd(true);
                recyclerView.setLayoutManager(pricelayoutManager);
                loadMenuPriceAscending();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadMenuPriceAscending() {
        adapter = new FirebaseRecyclerAdapter<Stock, StockViewHolderUser>(
                Stock.class,
                R.layout.stock_item_user,
                StockViewHolderUser.class,
                stockList.orderByChild("price")
        ) {
            @Override
            protected void populateViewHolder(StockViewHolderUser viewHolder, Stock model, final int position) {
                viewHolder.tvProductName.setText(model.getProductName());
                viewHolder.tvProductCategory.setText(model.getCategory());
                viewHolder.tvProductDescription.setText(model.getDescription());
                viewHolder.tvProductManufacturer.setText(model.getManufacturer());
                viewHolder.tvProductPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.productImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start Activity
                        Intent drinkDetail = new Intent(HomePageUser.this, ProductDetail.class);
                        drinkDetail.putExtra("ProductId", adapter.getRef(position).getKey());
                        startActivity(drinkDetail);
                    }
                });

                viewHolder.reviewProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productId = adapter.getRef(position).getKey();
                        showRatingDialog();
                    }
                });

            }
        };

        //Set Adapter
        recyclerView.setAdapter(adapter);
    }

    private void loadMenuManufacturerAscending() {
        adapter = new FirebaseRecyclerAdapter<Stock, StockViewHolderUser>(
                Stock.class,
                R.layout.stock_item_user,
                StockViewHolderUser.class,
                stockList.orderByChild("manufacturer")
        ) {
            @Override
            protected void populateViewHolder(StockViewHolderUser viewHolder, Stock model, final int position) {
                viewHolder.tvProductName.setText(model.getProductName());
                viewHolder.tvProductCategory.setText(model.getCategory());
                viewHolder.tvProductDescription.setText(model.getDescription());
                viewHolder.tvProductManufacturer.setText(model.getManufacturer());
                viewHolder.tvProductPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.productImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start Activity
                        Intent drinkDetail = new Intent(HomePageUser.this, ProductDetail.class);
                        drinkDetail.putExtra("ProductId", adapter.getRef(position).getKey());
                        startActivity(drinkDetail);
                    }
                });

                viewHolder.reviewProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productId = adapter.getRef(position).getKey();
                        showRatingDialog();
                    }
                });

            }
        };

        //Set Adapter
        recyclerView.setAdapter(adapter);
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Stock, StockViewHolderUser>(
                Stock.class,
                R.layout.stock_item_user,
                StockViewHolderUser.class,
                stockList
        ) {
            @Override
            protected void populateViewHolder(StockViewHolderUser viewHolder, Stock model, final int position) {
                viewHolder.tvProductName.setText(model.getProductName());
                viewHolder.tvProductCategory.setText(model.getCategory());
                viewHolder.tvProductDescription.setText(model.getDescription());
                viewHolder.tvProductManufacturer.setText(model.getManufacturer());
                viewHolder.tvProductPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.productImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start Activity
                        Intent drinkDetail = new Intent(HomePageUser.this, ProductDetail.class);
                        drinkDetail.putExtra("ProductId", adapter.getRef(position).getKey());
                        startActivity(drinkDetail);
                    }
                });

                viewHolder.reviewProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productId = adapter.getRef(position).getKey();
                        showRatingDialog();
                    }
                });

            }
        };

        //Set Adapter
        recyclerView.setAdapter(adapter);
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Ok", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this product")
                .setDescription("Please select a rating and give some feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comments here...")
                .setHintTextColor(android.R.color.white)
                .setCommentTextColor(R.color.text_rating)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(HomePageUser.this)
                .show();
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
        getMenuInflater().inflate(R.menu.home_page_user, menu);
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
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_searchTitle) {
            Intent intent = new Intent(HomePageUser.this, SearchProductNameActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_searchManufacturer) {
            Intent intent = new Intent(HomePageUser.this, SearchManufacturerActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_searchPrice) {
            Intent intent = new Intent(HomePageUser.this, SearchPriceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_log_out) {

            Intent logout = new Intent(HomePageUser.this, MainActivity.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Sending Rating to firebase
    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {

        //Get rating and upload to firebase
        final Rating rating = new Rating(Common.currentUser.getUsername(),
                productId,
                String.valueOf(value),
                comments);
        ratingTbl.child(Common.currentUser.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(Common.currentUser.getUsername()).exists()) {
                    ratingTbl.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(rating);
                }
                else {
                    //Update new value
                    ratingTbl.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(rating);

                }

                Toast.makeText(HomePageUser.this, "Thank you for submitting a rating!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
