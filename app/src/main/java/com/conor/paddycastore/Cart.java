package com.conor.paddycastore;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.conor.paddycastore.Adapter.CartAdapter;
import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Database.Database;
import com.conor.paddycastore.Model.Order;
import com.conor.paddycastore.Model.Request;
import com.conor.paddycastore.StrategyPattern.CreditCardPayment;
import com.conor.paddycastore.StrategyPattern.PaymentStrategy;
import com.conor.paddycastore.StrategyPattern.PaypalPayment;
import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart  extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    DatabaseReference stock;
    DatabaseReference payment;

    EditText edtAddress, edtPaypalUsername, edtPaypalPassword;
    TextView txtTotalPrice;
    Button btnPlace, btnCancel, btnCreditCard, btnPaypal;

    double total = 0.00d;
    int stockLevel;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;
    CardForm cardForm;
    TextView txtDes;
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        payment = database.getReference("PaymentMethod");
        stock = database.getReference("Stock");

        //Init
        recyclerView = (RecyclerView)findViewById(R.id.cartList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (Button)findViewById(R.id.btnPlaceOrder);
        btnCancel = (Button)findViewById(R.id.btnCancelOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Cart deleted please exit the cart to add products back into your basket", Toast.LENGTH_LONG).show();
            }
        });

        loadListProducts();
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("How Would you like to pay");
        alertDialog.setMessage("Please fill in address for delivery");

        LayoutInflater inflater = this.getLayoutInflater();
        View pop_up_dialog = inflater.inflate(R.layout.payment_choice_layout, null);

        edtAddress = pop_up_dialog.findViewById(R.id.edtAddress);
        btnCreditCard = pop_up_dialog.findViewById(R.id.btnCreditCard);
        btnPaypal = pop_up_dialog.findViewById(R.id.btnPaypal);

        //Event for button
        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditCardPayment(edtAddress.getText().toString());
            }
        });

        btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaypalPayment(edtAddress.getText().toString());
            }
        });

        alertDialog.setView(pop_up_dialog);
        alertDialog.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);

//        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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

    private void PaypalPayment(final String edtAddress) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Paypal Form");
        alertDialog.setMessage("Please fill in full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View paypal_dialog = inflater.inflate(R.layout.paypal_layout, null);

        edtPaypalUsername = paypal_dialog.findViewById(R.id.edtPaypalUsername);
        edtPaypalPassword = paypal_dialog.findViewById(R.id.edtPaypalPassword);

        alertDialog.setView(paypal_dialog);
        alertDialog.setIcon(R.drawable.ic_payment_black_24dp);

//        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Create a new request
                Request request = new Request(
                        Common.currentUser.getUsername(),
                        Common.currentUser.getName(),
                        edtAddress,
                        txtTotalPrice.getText().toString(),
                        cart
                );

                PaymentStrategy paypal = new PaypalPayment(
                        edtPaypalUsername.getText().toString(),
                        edtPaypalPassword.getText().toString(),
                        cart,
                        Common.currentUser.getUsername()
                );

//                Order order = new Order();
//                order.adjustQuantity();

                String orderRef = String.valueOf(System.currentTimeMillis());

                //Send to firebase
                requests.child(orderRef).setValue(request);

                payment.child(orderRef).setValue(paypal);

                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(getBaseContext(), "Thank you, Order Placed", Toast.LENGTH_SHORT).show();
                finish();
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

    private void CreditCardPayment(final String address) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("How Would you like to pay");
        alertDialog.setMessage("Please fill in address for delivery");
        LayoutInflater inflater = this.getLayoutInflater();
        View pop_up_dialog = inflater.inflate(R.layout.credit_card_layout, null);

        cardForm = (CardForm) pop_up_dialog.findViewById(R.id.card_form);
        txtDes = (TextView)pop_up_dialog.findViewById(R.id.payment_amount_holder);
        btnPay = (Button)pop_up_dialog.findViewById(R.id.btn_pay);

        txtDes.setText(txtTotalPrice.getText().toString());
        btnPay.setText(String.format("Payer %s", txtDes.getText()));

        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(Cart.this, "Number: "+card.getNumber() + " | CVC: " + card.getCVC(),
                        Toast.LENGTH_SHORT).show();

                //Create a new request
                Request request = new Request(
                        Common.currentUser.getUsername(),
                        Common.currentUser.getName(),
                        address,
                        txtTotalPrice.getText().toString(),
                        cart
                );

                CreditCardPayment creditCardPayment = new CreditCardPayment(
                        card.getName(),
                        card.getNumber(),
                        card.getCVC(),
                        card.getExpMonth().toString(),
                        card.getExpYear().toString(),
                        cart,
                        txtTotalPrice.getText().toString()
                );

                String orderRef = String.valueOf(System.currentTimeMillis());

                //Send to firebase
                requests.child(orderRef).setValue(request);

                payment.child(orderRef).setValue(creditCardPayment);

                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(getBaseContext(), "Thank you, Order Placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setView(pop_up_dialog);
        alertDialog.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);

        alertDialog.show();

    }


    private void loadListProducts() {
        cart = new Database(this).getCart();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate the total price

        for(Order orders : cart) {
            double price = Double.parseDouble(orders.getPrice());
            int quantity = Integer.parseInt(orders.getQuantity());
            total += price * quantity;
        }

        Locale locale = new Locale("en", "IE");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));

        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
