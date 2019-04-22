package com.conor.paddycastore;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Database.Database;
import com.conor.paddycastore.Model.Order;
import com.conor.paddycastore.Model.Rating;
import com.conor.paddycastore.Model.Stock;
import com.conor.paddycastore.ViewHolder.StockViewHolderUser;
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

public class ProductDetail extends AppCompatActivity implements RatingDialogListener {

    //UI
    TextView product_name, product_price, product_description;
    ImageView product_image_detail;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button btnAddToCart;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;

    String productId="";

    //Firebase
    FirebaseDatabase database;
    DatabaseReference products;
    DatabaseReference ratingTbl;

    FirebaseRecyclerAdapter<Stock, StockViewHolderUser> adapter;

    Stock currentProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        product_description = (TextView)findViewById(R.id.product_description);
        product_name = (TextView)findViewById(R.id.product_name_detail);
        product_price = (TextView)findViewById(R.id.product_price_detail);
        product_image_detail = (ImageView)findViewById(R.id.img_product);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        //Firebase Init
        database = FirebaseDatabase.getInstance();
        products = database.getReference("Stock");
        ratingTbl = database.getReference("Rating");


        //Get drink id from intent
        if(getIntent() != null)
        {
            productId = getIntent().getStringExtra("ProductId");
        }
        if(!productId.isEmpty()){
            if(Common.isConnectedToInternet(getBaseContext()))
            {
                getDetailProduct(productId);
                getRatingProduct(productId);
            }
            else {
                Toast.makeText(ProductDetail.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        btnAddToCart = (Button)findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        currentProduct.getProductName(),
                        "1",
                        currentProduct.getPrice(),
                        currentProduct.getImage()
                ));

                Toast.makeText(ProductDetail.this, currentProduct.getProductName() + " added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getRatingProduct(String key) {
        Query drinkRating = ratingTbl.orderByChild("productId").equalTo(key);

        drinkRating.addValueEventListener(new ValueEventListener() {
            int count =0, sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count !=0)
                {
                    float average = sum / count;

                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //populating the view with the drinks information
    private void getDetailProduct(final String productId) {
        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentProduct = dataSnapshot.getValue(Stock.class);

                //Set image
                Picasso.with(getBaseContext()).load(currentProduct.getImage())
                        .into(product_image_detail);

                product_price.setText(currentProduct.getPrice());
                product_name.setText(currentProduct.getProductName());
                product_description.setText(currentProduct.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

                Toast.makeText(ProductDetail.this, "Thank you for submitting a rating!!", Toast.LENGTH_SHORT).show();
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
