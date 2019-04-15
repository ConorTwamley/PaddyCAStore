package com.conor.paddycastore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conor.paddycastore.Common.Common;
import com.conor.paddycastore.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

        EditText etUsername, etPassword;
        Button btnSignIn;

        FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference table_user;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            etUsername = (EditText) findViewById(R.id.etUsername);
            etPassword = (EditText) findViewById(R.id.etPassword);
            btnSignIn = (Button) findViewById(R.id.btnSignIn);

            database = FirebaseDatabase.getInstance();
            table_user = database.getReference("User");

            firebaseAuth = FirebaseAuth.getInstance();

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                    mDialog.setMessage("Please wait... ");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Check if user exists in the databases
                            if (dataSnapshot.child(etUsername.getText().toString()).exists()) {

                                mDialog.dismiss();
                                //Get user information
                                User user = dataSnapshot.child(etUsername.getText().toString()).getValue(User.class);
                                user.setUsername(etUsername.getText().toString()); //set Phone
                                if (user.getPassword().equals(etPassword.getText().toString())) {

                                    if (user.getIsStaff().equals("true"))
                                    {
                                        Intent intent = new Intent(LoginActivity.this, HomePageAdmin.class);
                                        Common.currentUser = user;
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(LoginActivity.this, HomePageUser.class);
                                        Common.currentUser = user;
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Sign in failed!! ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "User does not exist in databases ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }