package com.conor.paddycastore.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.conor.paddycastore.Interfaces.ItemClickListener;
import com.conor.paddycastore.Model.Stock;
import com.conor.paddycastore.R;

import java.util.List;

public class StockViewHolderUser extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvProductName, tvProductPrice, tvProductDescription, tvProductCategory, tvProductManufacturer;
    public TextView searchProductName, searchProductPrice, searchProductDescription, searchProductCategory, searchProductManufacturer;
    public ImageView productImage, addToCart, searchImageProduct, reviewProduct;
    public RatingBar ratingBar;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    public StockViewHolderUser(View itemView) {
        super(itemView);

        tvProductName = (TextView)itemView.findViewById(R.id.tvProductName);
        productImage = (ImageView)itemView.findViewById(R.id.productImage);
        tvProductPrice = (TextView)itemView.findViewById(R.id.tvProductPrice);
        tvProductDescription = (TextView)itemView.findViewById(R.id.tvProductDescription);
        tvProductCategory = (TextView)itemView.findViewById(R.id.tvProductCategory);
        tvProductManufacturer = (TextView)itemView.findViewById(R.id.tvProductManufacturer);
        addToCart = (ImageView)itemView.findViewById(R.id.addToCart);
        ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
        reviewProduct = (ImageView)itemView.findViewById(R.id.reviewProduct);

        //Search view
        searchProductName = (TextView)itemView.findViewById(R.id.titleSearchStock);
        searchProductPrice = (TextView)itemView.findViewById(R.id.priceSearchItem);
        searchProductDescription = (TextView)itemView.findViewById(R.id.descriptionSearchItem);
        searchProductCategory = (TextView)itemView.findViewById(R.id.categorySearchStock);
        searchProductManufacturer = (TextView)itemView.findViewById(R.id.manufacturerSearchItem);
        searchImageProduct = (ImageView)itemView.findViewById(R.id.stockImage);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(), false);
    }
}

