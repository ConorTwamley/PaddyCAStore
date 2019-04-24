package com.conor.paddycastore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Interfaces.ItemClickListener;
import com.conor.paddycastore.Model.Order;
import com.conor.paddycastore.Model.Request;
import com.conor.paddycastore.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference orders;

    ArrayList<Order> order = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        //Firebase
        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

            if (Common.isConnectedToInternet(getBaseContext())){
                loadOrders();
            }
            else {
                Toast.makeText(ViewOrders.this, "Please check you internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_item,
                OrderViewHolder.class,
                orders)
        {
            @Override
            protected void populateViewHolder(final OrderViewHolder viewHolder, final Request request, int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                viewHolder.txtOrderName.setText(request.getUsername());
                viewHolder.txtDeliveryAddress.setText(request.getAddress());

                order = (ArrayList) request.getProducts();

                String data = "";
                for(Order productListOrder : order) {
                    data += productListOrder.getProductName() + "\n";
                }
                viewHolder.txtOrderDetails.setText(data);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

//        if(item.getTitle().equals(Common.UPDATE))
//        {
//            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
//        }
//        else
            if(item.getTitle().equals(Common.DELETE)){
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key) {
        orders.child(key).removeValue();
    }
}