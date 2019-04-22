package com.conor.paddycastore.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.conor.paddycastore.Cart;
import com.conor.paddycastore.Database.Database;
import com.conor.paddycastore.Model.Order;
import com.conor.paddycastore.R;
import com.conor.paddycastore.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData;
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(cart);
        View itemView = layoutInflater.inflate(R.layout.cart_item, viewGroup, false);
        return new CartViewHolder(itemView);
    }

//    @Override
//    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int position) {
//
//        Picasso.with(cart).load(listData.get(position).getImage())
//                .resize(70,70)
//                .centerCrop()
//                .into(cartViewHolder.cart_image);
//
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(""+ listData.get(position).getQuantity(), Color.RED);
//
//        Locale locale = new Locale("en", "IE");
//        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//        int price = (Integer.parseInt(listData.get(position).getPrice()));
//
//        cartViewHolder.txt_price.setText(fmt.format(price));
////        cartViewHolder.txt_price.setText(listData.get(position).getPrice());
//        cartViewHolder.txt_cart_name.setText(listData.get(position).getProductName());
//    }
    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int position) {

        Picasso.with(cart).load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(cartViewHolder.cart_image);

        double price = Double.parseDouble(listData.get(position).getPrice());
        int quantity = Integer.parseInt(listData.get(position).getQuantity());
        double total = price * quantity;

        Locale locale = new Locale("en", "IE");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        cartViewHolder.txt_price.setText(fmt.format(total));

        cartViewHolder.txt_cart_name.setText(new StringBuilder(listData.get(position).getProductName()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position)
    {
        return listData.get(position);
    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item,int position) {
        listData.add(position, item);
        notifyItemInserted(position);
    }
}