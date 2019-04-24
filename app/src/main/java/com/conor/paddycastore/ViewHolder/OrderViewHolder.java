package com.conor.paddycastore.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Interfaces.ItemClickListener;
import com.conor.paddycastore.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderName, txtOrderDetails, txtDeliveryAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderName = (TextView)itemView.findViewById(R.id.order_name);
        txtOrderDetails = (TextView)itemView.findViewById(R.id.order_details);
        txtDeliveryAddress = (TextView)itemView.findViewById(R.id.order_delivery_address);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
    {
        contextMenu.setHeaderTitle("Select the action");

//        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE );
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

}
