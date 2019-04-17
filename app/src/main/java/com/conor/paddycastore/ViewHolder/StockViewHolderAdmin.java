package com.conor.paddycastore.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.conor.paddycastore.Model.Stock;
import com.conor.paddycastore.R;

import java.util.List;

public class StockViewHolderAdmin extends RecyclerView.ViewHolder {

    private List<Stock> listData;
    public TextView tvProductName, tvProductPrice, tvProductDescription, tvProductCategory, tvProductManufacturer;
    public ImageView productImage, editProduct;
    public TextView username, name;

    public StockViewHolderAdmin(View itemView) {
        super(itemView);

        tvProductName = (TextView)itemView.findViewById(R.id.tvProductName);
        productImage = (ImageView)itemView.findViewById(R.id.productImage);
        tvProductPrice = (TextView)itemView.findViewById(R.id.tvProductPrice);
        tvProductDescription = (TextView)itemView.findViewById(R.id.tvProductDescription);
        tvProductCategory = (TextView)itemView.findViewById(R.id.tvProductCategory);
        tvProductManufacturer = (TextView)itemView.findViewById(R.id.tvProductManufacturer);
        editProduct = (ImageView)itemView.findViewById(R.id.editProduct);

        //Search views
//        username = itemView.findViewById(R.id.usernameSearchUser);
        name = itemView.findViewById(R.id.nameSearchUser);

    }

}
