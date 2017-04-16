package com.example.yash.firecast1;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private String usernameToDisplay;

    private FirebaseAuth mAuth;
    Firebase ref;
    String admin;
    FirebaseListAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ref=new Firebase("https://fir-basics-106e8.firebaseio.com/keyValues");

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText pass = (EditText) findViewById(R.id.password);

        final EditText key1=(EditText) findViewById(R.id.etkey);


        final Button signIn = (Button) findViewById(R.id.signIn);
        Button signUp = (Button) findViewById(R.id.signUp);
        Button generate=(Button) findViewById(R.id.key);


        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String user = username.getText().toString().trim();
                String passwo = pass.getText().toString().trim();





                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(passwo)) {
                    Toast.makeText(MainActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();


                    username.setText("");
                    pass.setText("");
                    key1.setText("");

                } else {


                    String email = user.concat("@yash.com");
                    String password = passwo;

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "User Registered",
                                                Toast.LENGTH_SHORT).show();

                                        FirebaseAuth.getInstance().signOut();

                                    }

                                }
                            });


                    username.setText("");
                    pass.setText("");



                }

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user = username.getText().toString();
                final String passwo = pass.getText().toString();
                final String key = key1.getText().toString();

                usernameToDisplay = user;

                boolean en = keyDoesExist(key);
                if (!en) {
                    Toast.makeText(MainActivity.this, "The key you entered does not exist", Toast.LENGTH_SHORT).show();

                } else {

                    if (TextUtils.isEmpty(user) || TextUtils.isEmpty(passwo)) {
                        Toast.makeText(MainActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();

                        username.setText("");
                        pass.setText("");
                    } else {

                        String email = user.concat("@yash.com");
                        String password = passwo;


                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {


                                        if (!task.isSuccessful()) {

                                            Toast.makeText(MainActivity.this, "Login Failed",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Logged In",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent myIntent = new Intent(getApplicationContext(), Fake.class);

                                            myIntent.putExtra("key", key);
                                            myIntent.putExtra("displayName", usernameToDisplay);
                                           // myIntent.putExtra("admin",admin);


                                            startActivity(myIntent);
                                            finish();
                                        }
                                    }
                                });


                        username.setText("");

                        pass.setText("");

                    }

                }
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String key=key1.getText().toString();

               boolean ex=keyDoesExist(key);

                if(ex){
                    Toast.makeText(MainActivity.this,"Try another Key",Toast.LENGTH_SHORT).show();
                    key1.setText("");

                }
                else{
                    ref.push().setValue(key);
                    Toast.makeText(MainActivity.this,"Your Key is Generated",Toast.LENGTH_SHORT).show();
                    //admin=mAuth.getCurrentUser().getUid();

                }

            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();

        adapter=new FirebaseListAdapter<String>(this,String.class,android.R.layout.simple_list_item_1,ref) {
            @Override
            protected void populateView(View view, String s, int i) {
                TextView text=(TextView) view.findViewById(android.R.id.text1);
                text.setText(s);
            }
        };


    }

    private boolean keyDoesExist(String key){


        int n=adapter.getCount();


        while(n-->0){
            if(adapter.getItem(n).equals(key)){
                return true;

            }

        }


        return false;
    }
}



