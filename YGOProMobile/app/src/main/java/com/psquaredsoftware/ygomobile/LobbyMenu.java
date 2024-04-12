package com.psquaredsoftware.ygomobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.psquaredsoftware.ygomobile.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LobbyMenu extends AppCompatActivity {

    ServerSocket host = null;
    DataOutputStream os;
    DataInputStream is;
    Socket client = null;
    Socket player = null;
    ArrayAdapter adapter = null;
    Spinner list = null;
    myDbAdapter helper;
    int selected = 0;
    //Button readyB;
    boolean hosting = false;
    boolean selfReady = false;
    boolean clientReady = false;
    TextView op;
    TextView us;
    Context con;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_view);
        helper = new myDbAdapter(this);
        final Button test = findViewById(R.id.testB);
        final Bundle extras = getIntent().getExtras();
        op = findViewById(R.id.opId);
        us = findViewById(R.id.youId);
        list = findViewById(R.id.deckChoice);
        con = this;
        if(helper.getData() != null) {
            adapter = new ArrayAdapter<String>(this, R.layout.dek_list_view, helper.getData());
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            list.setAdapter(adapter);
        }
        //Intent myIntent = new Intent(con, DuelActivity.class);
        //myIntent.putExtra("TYPE", "JOIN");
        //myIntent.putExtra("ADRESS", extras.getString("ADRESS"));
        //myIntent.putExtra("DECK", helper.getDataDeck(list.getSelectedItem().toString())[0]);
        //startActivity(myIntent);
        //readyB = findViewById(R.id.testB);
        new Thread(new Runnable() {
            @Override
            public void run() {

                if(extras.getString("TYPE").equals("HOST")){

                    try {
                        hosting = true;
                        findViewById(R.id.waiting).setVisibility(View.VISIBLE);

                        host = new ServerSocket(1236);
                        client = host.accept();
                            os = new DataOutputStream(client.getOutputStream());
                            is = new DataInputStream(client.getInputStream());
                            while (is.available() == 0) ;
                            final String temp = is.readUTF();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                        test.setText(temp);

                                }
                            });

                            while(is.available() == 0);
                            if(is.readUTF().equals("READY")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        op.setText("Opponent - Ready");

                                    }
                                });
                                if(selfReady){
                                    os.writeUTF("START");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                os.close();
                                                is.close();
                                                client.close();
                                                host.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            test.setText("START" + list.getSelectedItem().toString());
                                            Intent myIntent = new Intent(con, DuelActivity.class);
                                            myIntent.putExtra("TYPE", "HOST");
                                            //myIntent.putExtra("ADRESS", extras.getString("ADRESS"));
                                            myIntent.putExtra("DECK", helper.getDataDeck(list.getSelectedItem().toString())[0]);
                                            startActivity(myIntent);

                                            //myIntent.putExtra("ADRESS", extras.getString("ADRESS"));

                                        }
                                    });
                                    return;
                                    //test.setText("START" + adapter.getItem(selected).toString());
                                }else{
                                    while(!selfReady);
                                    os.writeUTF("START");

                                    //client.close();
                                   // host.close();
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    os.close();
                                                    is.close();
                                                    client.close();
                                                    host.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                test.setText("STARTD" + list.getSelectedItem().toString());
                                                Intent myIntent = new Intent(con, DuelActivity.class);
                                                myIntent.putExtra("TYPE", "HOST");

                                                //myIntent.putExtra("ADRESS", extras.getString("ADRESS"));
                                                    myIntent.putExtra("DECK", helper.getDataDeck(list.getSelectedItem().toString())[0]);
                                                    startActivity(myIntent);

                                                //myIntent.putExtra("ADRESS", extras.getString("ADRESS"));

                                            }
                                        });
                                        return;
                                    }catch(Exception e){
                                        final Exception ex = e;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                test.setText(ex.getMessage());

                                            }
                                        });
                                    }

                                    //test.setText("START" + adapter.getItem(selected).toString());
                                }
                            }

                    } catch (Exception e) {
                        final Exception ex = e;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    test.setText(ex.getMessage());

                            }
                        });
                    }

                }else{

                    try {
                        Thread.sleep(500);
                        player = new Socket(extras.getString("ADRESS"), 1236);
                        os = new DataOutputStream(player.getOutputStream());
                        is = new DataInputStream(player.getInputStream());
                        os.writeUTF("We Have Connected");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    test.setText("SENT");

                            }
                        });
                        while(true) {
                            if(is.available() != 0) {
                                if (is.readUTF().equals("START")) {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                os.close();
                                                is.close();
                                                player.close();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            test.setText("START" + list.getSelectedItem().toString());
                                            Intent myIntent = new Intent(con, DuelActivity.class);
                                            myIntent.putExtra("TYPE", "JOIN");
                                            myIntent.putExtra("ADRESS", extras.getString("ADRESS"));
                                            myIntent.putExtra("DECK", helper.getDataDeck(list.getSelectedItem().toString())[0]);
                                            startActivity(myIntent);


                                        }
                                    });
                                    //player.close();


                                    break;
                                    //test.setText("START" + adapter.getItem(selected).toString());
                                }else{

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            op.setText("Opponent - Ready");

                                        }
                                    });
                                    //text view

                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

       test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        us.setText("You - Ready");

                    }
                });
                if(!hosting){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    os.writeUTF("READY");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                }else{

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                os.writeUTF("READY");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    selfReady = true;

                }
            }
        });

    }

}
