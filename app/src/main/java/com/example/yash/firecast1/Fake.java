package com.example.yash.firecast1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class Fake extends AppCompatActivity {


    Firebase ref;
    FirebaseAuth auth;
    ListView chat;
    ;
    Button send;
    EditText mess;
    Button logout, clearChat, killKey;
    String name, key, admin;
    String NODE = "ChatId";
    FirebaseListAdapter<String> adapter;

    Firebase mRef = new Firebase("https://fir-basics-106e8.firebaseio.com/keyValues");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);

        name = getIntent().getStringExtra("displayName").toUpperCase();
        key = getIntent().getStringExtra("key");
        admin = getIntent().getStringExtra("admin");


        String child = NODE.concat(key);
        String URL = "https://fir-basics-106e8.firebaseio.com/";
        URL = URL.concat(child);


        ref = new Firebase(URL);

        ref.push().setValue(">>>>>>" + name + " just joined the session <<<<<<");
        chat = (ListView) findViewById(R.id.listView2);
        ;
        send = (Button) findViewById(R.id.buttonFoggy);
        mess = (EditText) findViewById(R.id.editText2);

        auth = FirebaseAuth.getInstance();

        logout = (Button) findViewById(R.id.logout);


    }

    @Override
    protected void onStart() {
        super.onStart();


        String v = " : ";
        name = name.concat(v);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mess.getText().toString();

                message = name.concat(message);

                ref.push().setValue(message);

                mess.setText("");

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();


                ref.push().setValue(">>>>>>" + name + "left the session <<<<<<");

                startActivity(new Intent(Fake.this, MainActivity.class));
                finish();
            }
        });

       /* clearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check=ref.getAuth().getUid();
               // String check=auth.getCurrentUser().getUid();

                if(isAdmin(check)){
                    ref.removeValue();
                }

                else {
                    Toast.makeText(Fake.this,"U dont have authority to perform this action" ,Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        killKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check=mRef.getAuth().getUid();
                //String check=auth.getCurrentUser().getUid();

                if(!isAdmin(check)){
                    Toast.makeText(Fake.this,"U dont have authority to perform this action" ,Toast.LENGTH_SHORT).show();
                    return;
                }

                else{


                }
            }
        });*/


        adapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, ref) {
            @Override
            protected void populateView(View view, String s, int i) {
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(s);
            }
        };

        chat.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ref.push().setValue(">>>>>>" + name + "has paused the session for now <<<<<<");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ref.push().setValue(">>>>>>" + name + "is now back in the session <<<<<<");
    }


   /* private boolean isAdmin(String check){


        if(check.equals(admin))
            return true;

        else return false;
    }
*/
}