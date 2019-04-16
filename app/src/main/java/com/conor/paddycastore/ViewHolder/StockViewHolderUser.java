package com.conor.paddycastore.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.conor.paddycastore.Model.Stock;
import com.conor.paddycastore.R;

import java.util.List;

public class StockViewHolderUser extends RecyclerView.ViewHolder {

    private List<Stock> listData;
    public TextView tvProductName, tvProductPrice, tvProductDescription, tvProductCategory, tvProductManufacturer;
    public ImageView productImage, addToCart;


    public StockViewHolderUser(View itemView) {
        super(itemView);

        tvProductName = (TextView)itemView.findViewById(R.id.tvProductName);
        productImage = (ImageView)itemView.findViewById(R.id.productImage);
        tvProductPrice = (TextView)itemView.findViewById(R.id.tvProductPrice);
        tvProductDescription = (TextView)itemView.findViewById(R.id.tvProductDescription);
        tvProductCategory = (TextView)itemView.findViewById(R.id.tvProductCategory);
        tvProductManufacturer = (TextView)itemView.findViewById(R.id.tvProductManufacturer);
        addToCart = (ImageView)itemView.findViewById(R.id.addToCart);
    }

}

