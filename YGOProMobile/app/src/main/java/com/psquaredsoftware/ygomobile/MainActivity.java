package com.psquaredsoftware.ygomobile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.play.core.assetpacks.AssetPackManager;
import com.google.android.play.core.assetpacks.AssetPackManagerFactory;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.AssetPackStateUpdateListener;
import com.google.android.play.core.assetpacks.AssetPackStates;
import com.google.android.play.core.assetpacks.model.AssetPackStatus;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.psquaredsoftware.ygomobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private ListView mDeckList;
    private ListView mGameList;
    private Button mDeckCreateButton;
    private Button mDuel;
    private Button mDuel2;
    private Button mDuel3;
    private Button testButton;

    //CHANGE PACKAGE

    //private Button mDeckEditButton;
    //private Button mDeckDeleteButton;
    String urlServer2 = "https://storage.googleapis.com/ygoprodeck.com/pics_small/";
    String[] deckList = {};
    ArrayAdapter adapter;
    myDbAdapter helper;
    Context con = this;
    //Button mOpenButton;
    AppCompatActivity activity;
    Socket socket = null;
    DataOutputStream os;
    DataInputStream is;
    public boolean close = false;
    ServerSocket host = null;
    AssetManager usernameStore;
    File userNameFile;
    File cardData;
    BufferedReader fileIn;
    BufferedWriter fileOut;
    BufferedReader urlIn;
    InputStream fromApi;
    FileWriter jsonOut;
    Button impo;
    Button expo;
    ScrollView frontPage;
    Button tutorialButton;
    Button surveyButton;
    boolean systemsGo = true;
    boolean inter = false;
    boolean hosting = false;
    public int cardCoutn = 0;
    public int progress = 0;
    public boolean waitForWifiConfirmationShown = false;

    //https://stackoverflow.com/questions/5158323/android-mediarecorder-setoutputfile-to-stream-using-socket This will get the audio and work
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(findViewById(R.id.maincontainer).getForeground() != null)
                return true;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    findViewById(R.id.waiting).setVisibility(View.INVISIBLE);
                    //findViewById(R.id.deckListCont).setVisibility(View.INVISIBLE);
                    //findViewById(R.id.gameListCont).setVisibility(View.INVISIBLE);
                    mDeckList.setVisibility(View.INVISIBLE);
                    mGameList.setVisibility(View.INVISIBLE);
                    mDeckCreateButton.setVisibility(View.INVISIBLE);
                    frontPage.setVisibility(View.VISIBLE);
                    //impo.setVisibility(View.INVISIBLE);
                    //expo.setVisibility(View.INVISIBLE);
                    mDuel.setVisibility(View.INVISIBLE);
                    mDuel2.setVisibility(View.INVISIBLE);
                    mDuel3.setVisibility(View.INVISIBLE);
                    testButton.setVisibility(View.INVISIBLE);
                    ((TextView) findViewById(R.id.showname)).setVisibility(View.INVISIBLE);
                    if(socket != null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    os.writeUTF("QUIT");
                                    os.flush();
                                    close = true;
                                    return;
                                }catch (Exception ex) {
                                    final Exception e = ex;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            FirebaseCrashlytics.getInstance().recordException(e);
                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Deck Editor");
                    findViewById(R.id.waiting).setVisibility(View.INVISIBLE);
                    //findViewById(R.id.deckListCont).setVisibility(View.VISIBLE);
                    //findViewById(R.id.gameListCont).setVisibility(View.INVISIBLE);
                    mDeckList.setVisibility(View.VISIBLE);
                    mGameList.setVisibility(View.INVISIBLE);
                    mDeckCreateButton.setVisibility(View.VISIBLE);
                    frontPage.setVisibility(View.INVISIBLE);
                    //impo.setVisibility(View.VISIBLE);
                    //expo.setVisibility(View.VISIBLE);
                    mDuel.setVisibility(View.INVISIBLE);
                    mDuel2.setVisibility(View.INVISIBLE);
                    mDuel3.setVisibility(View.INVISIBLE);
                    testButton.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.showname)).setVisibility(View.INVISIBLE);
                    if(socket != null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    close = true;//TODO put close above os writes
                                    os.writeUTF("QUIT");
                                    os.flush();

                                    return;
                                }catch (Exception ex) {
                                    final Exception e = ex;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            FirebaseCrashlytics.getInstance().recordException(e);
                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    if(helper.getData() != null) {
                        adapter = new ArrayAdapter<String>(con, R.layout.dek_list_view, helper.getData());
                        mDeckList.setAdapter(adapter);
                    }
                    return true;
                case R.id.navigation_notifications:

                    if(helper.getDataDeckCount() == 1) {

                        hosting = false;

                        findViewById(R.id.waiting).setVisibility(View.INVISIBLE);


                        mTextMessage.setText("Duel Zone");
                        //findViewById(R.id.deckListCont).setVisibility(View.INVISIBLE);
                        //findViewById(R.id.gameListCont).setVisibility(View.VISIBLE);
                        mDeckList.setVisibility(View.INVISIBLE);
                        frontPage.setVisibility(View.INVISIBLE);
                        //impo.setVisibility(View.INVISIBLE);
                        //expo.setVisibility(View.INVISIBLE);
                        mGameList.setVisibility(View.VISIBLE);
                        mDuel.setVisibility(View.VISIBLE);
                        mDuel2.setVisibility(View.VISIBLE);
                        mDuel3.setVisibility(View.VISIBLE);
                        testButton.setVisibility(View.INVISIBLE);
                        ((TextView) findViewById(R.id.showname)).setVisibility(View.VISIBLE);
                        mDeckCreateButton.setVisibility(View.INVISIBLE);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    socket = new Socket("ygo.ignorelist.com", 1236);
                                    //socket = new Socket("ygo.ignorelist.com", 1236);
                                    is = new DataInputStream(socket.getInputStream());
                                    os = new DataOutputStream(socket.getOutputStream());
                                    close = false;
                                    /*
                                while(!close){
                                    Thread.sleep(100);
                                    if(!close && is.available() != 0){
                                        final String buff = is.readUTF();
                                        if(buff.equals("JOIN")){

                                            while(is.available() == 0);

                                            String hostS = is.readUTF();
                                            //Socket op = new Socket(hostS, 1236);
                                            Intent myIntent = new Intent(con, NewDuelActivity.class);
                                            myIntent.putExtra("KEY", buff.substring(4));
                                            startActivityForResult(myIntent, 0);

                                            try{
                                                os.writeUTF("QUIT");
                                                os.flush();
                                                Thread.sleep(100);
                                                //os.close();
                                                close = true;
                                                return;
                                            }catch(IOException e){
                                                e.printStackTrace();
                                                return;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }else if(buff.contains("HOST")){

                                            //host = new ServerSocket(1236);
                                            //Socket opponent = host.accept();

                                            Intent myIntent = new Intent(con, NewDuelActivity.class);
                                            myIntent.putExtra("KEY", buff.substring(4));
                                            startActivityForResult(myIntent, 0);

                                            try{
                                                os.writeUTF("QUIT");
                                                os.flush();
                                                Thread.sleep(100);
                                                //os.close();
                                                close = true;
                                                return;
                                            }catch(IOException e){
                                                e.printStackTrace();
                                                return;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        adapter = new ArrayAdapter<String>(con, R.layout.dek_list_view, buff.split("/"));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        return;
                                                    }
                                                    mGameList.setAdapter(adapter);
                                                }
                                            });
                                        }
                                    }
                                }
                                */
                                    while (!close) {

                                        String buffer = is.readUTF();//TODO writeUTF issue catch and continue

                                        if (buffer.contains("JOININGGAME")) {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    findViewById(R.id.coverimg).setVisibility(View.VISIBLE);
                                                    findViewById(R.id.maincontainer).setForeground(getResources().getDrawable(R.drawable.loadingscreen));
                                                }
                                            });

                                            Intent intent = new Intent(con, NewDuelActivity.class);
                                            intent.putExtra("KEY", buffer.substring(12));
                                            intent.putExtra("FORMAT", buffer.substring(11, 12));
                                            startActivity(intent);
                                            close = true;

                                        } else if (buffer.contains("@")) {

                                            /*runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mGameList.setAdapter(null);
                                                }
                                            });*/

                                        }else if(buffer.equals("")){

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mGameList.setAdapter(null);
                                                }
                                            });

                                        } else if(buffer.equals("NAMEREJECT")){

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                                                    prompt.setTitle("Name has been taken");
                                                    prompt.setNegativeButton("OK", null);
                                                    prompt.show();
                                                }
                                            });

                                        }else if (buffer.contains("HOSTCONFIRM")) {

                                            hosting = true;

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    findViewById(R.id.waiting).setVisibility(View.VISIBLE);


                                                }
                                            });

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    while (!close && socket != null && os != null) {

                                                        try {

                                                            os.writeUTF("PING");
                                                            os.flush();
                                                            Thread.sleep(500);

                                                        } catch (Exception ex) {
                                                            final Exception e = ex;
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    FirebaseCrashlytics.getInstance().recordException(e);
                                                                    mTextMessage.setText(R.string.title_home);
                                                                    findViewById(R.id.waiting).setVisibility(View.INVISIBLE);
                                                                    //findViewById(R.id.deckListCont).setVisibility(View.INVISIBLE);
                                                                    //findViewById(R.id.gameListCont).setVisibility(View.INVISIBLE);
                                                                    mDeckList.setVisibility(View.INVISIBLE);
                                                                    mGameList.setVisibility(View.INVISIBLE);
                                                                    mDeckCreateButton.setVisibility(View.INVISIBLE);
                                                                    frontPage.setVisibility(View.VISIBLE);
                                                                    //impo.setVisibility(View.INVISIBLE);
                                                                    //expo.setVisibility(View.INVISIBLE);
                                                                    mDuel.setVisibility(View.INVISIBLE);
                                                                    mDuel2.setVisibility(View.INVISIBLE);
                                                                    mDuel3.setVisibility(View.INVISIBLE);
                                                                    testButton.setVisibility(View.INVISIBLE);
                                                                    ((TextView) findViewById(R.id.showname)).setVisibility(View.INVISIBLE);
                                                                    if(socket != null){
                                                                        close = true;
                                                                    }
                                                                }
                                                            });
                                                            break;
                                                            //e.printStackTrace();
                                                        }

                                                    }

                                                }
                                            }).start();

                                        } else {
                                            List<String> gamesS = Arrays.asList(buffer.split("/"));
                                            Collections.reverse(gamesS);
                                           // adapter = new ArrayAdapter(con, R.layout.dek_list_view, gamesS.toArray());
                                            adapter = new GameAdapter(con, R.layout.dek_list_view, gamesS.toArray());
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mGameList.setAdapter(adapter);
                                                }
                                            });
                                        }

                                    }

                                    socket.close();
                                    socket = null;
                                    is = null;
                                    os = null;
                                    return;
                                } catch (Exception ex) {
                                    final Exception e = ex;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            FirebaseCrashlytics.getInstance().recordException(e);
                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        mGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final String target = adapterView.getItemAtPosition(i).toString();
                                if(!target.equals("") && !hosting) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try{
                                                os.writeUTF(target);
                                                os.flush();
                                                } catch (IOException e) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            FirebaseCrashlytics.getInstance().recordException(e);
                                                        }
                                                    });
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                }

                            }
                        });
                        return true;

                    }

            }
            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode <= 3){

            if(permissions.length == 0){

                finishAndRemoveTask();
                Log.d("SYSTEM", "DOWN2");

            }else{

                for(int i = 0; i < grantResults.length; i++){

                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){

                        finishAndRemoveTask();
                        Log.d("SYSTEM", "DOWN" + i);

                    }

                }

                Log.d("SYSTEM", "GO");
                systemsGo = true;

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId(getResources().getString(R.string.appid)).setProjectId(getResources().getString(R.string.basename)).setApiKey(getResources().getString(R.string.apikey)).build();
            if(FirebaseApp.getApps(this).isEmpty())
            FirebaseApp.initializeApp(this, options, "YGOMobile");

            /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED){

                AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                prompt.setTitle("Please Allow App Permissions In Settings");
                prompt.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAndRemoveTask();
                    }
                });
                prompt.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finishAndRemoveTask();
                    }
                });
                prompt.show();
                return;

            }*/

            activity = this;
            adapter = new ArrayAdapter<String>(this, R.layout.dek_list_view, deckList);
            setContentView(R.layout.activity_main);
            ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {

                AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                prompt.setTitle("Please Connect to Internet");
                prompt.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAndRemoveTask();
                    }
                });
                prompt.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finishAndRemoveTask();
                    }
                });
                prompt.show();

            }else{
               // inter = true;

            /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                systemsGo = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            }
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){

                systemsGo = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 2);

            }*/
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                systemsGo = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

            }
            /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED){

                systemsGo = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);

            }*/

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!systemsGo) ;
                        verifyVersion("http://ygo.ignorelist.com/version.txt");
                        //verifyVersion("http://ygo.ignorelist.com/version.txt");
                    } catch (Exception ex) {
                        final Exception e = ex;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
            mTextMessage = findViewById(R.id.message);
            userNameFile = new File(con.getFilesDir(), "username.txt");
            if (userNameFile.length() == 0) {

                userNameFile.createNewFile();
                fileOut = new BufferedWriter(new FileWriter(userNameFile));
                fileOut.write("Username");
                fileOut.flush();
                fileOut.close();

            }
            fileIn = new BufferedReader(new FileReader(userNameFile));
            ((EditText) findViewById(R.id.showname)).setText(fileIn.readLine());
            fileIn.close();
            cardData = new File(con.getFilesDir(), "cardInfo.txt");
            cardData.delete();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!systemsGo) ;
                        makeJSONFromUrl("http://ygo.ignorelist.com/cardInfo.txt", cardData);
                        //makeJSONFromUrl("http://ygo.ignorelist.com/cardInfo.txt", cardData);
                    } catch (Exception ex) {
                        final Exception e = ex;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();

            frontPage = findViewById(R.id.initscrol);
            mDuel = findViewById(R.id.duelButton);
            mDuel2 = findViewById(R.id.duelButton2);
            mDuel3 = findViewById(R.id.duelButton3);
            testButton = findViewById(R.id.testButton);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            mDeckList = findViewById(R.id.decklist);
            mDeckList.setAdapter(adapter);
            mGameList = findViewById(R.id.gamelist);
            mDeckCreateButton = findViewById(R.id.button);
            helper = new myDbAdapter(this);
            tutorialButton = findViewById(R.id.tutbutton);
            surveyButton = findViewById(R.id.survey);
            //impo = findViewById(R.id.importt);
            //expo = findViewById(R.id.export);
            navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            findViewById(R.id.coverimg).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

            findViewById(R.id.waiting).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

            tutorialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(findViewById(R.id.maincontainer).getForeground() != null)
                        return;
                    try {
                        Intent toTutPage = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ygo.ignorelist.com/tutorial.pdf"));
                        startActivity(toTutPage);
                    }catch(Exception e){
                        AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                        prompt.setTitle("There is no default browser to open this link. Would you like to copy it instead?");
                        prompt.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Link", "http://ygo.ignorelist.com/tutorial.pdf");
                                clipboard.setPrimaryClip(clip);
                            }
                        });
                        prompt.show();
                    }
                }
            });

            findViewById(R.id.aboutus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(findViewById(R.id.maincontainer).getForeground() != null)
                        return;
                    try {
                        Intent toTutPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://duelingmobile.wixsite.com/website"));
                        startActivity(toTutPage);
                    }catch(Exception e){
                        AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                        prompt.setTitle("There is no default browser to open this link. Would you like to copy it instead?");
                        prompt.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Link", "https://duelingmobile.wixsite.com/website");
                                clipboard.setPrimaryClip(clip);
                            }
                        });
                        prompt.show();
                    }
                }
            });

                surveyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(findViewById(R.id.maincontainer).getForeground() != null)
                            return;
                        try {
                            Intent toTutPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://duelingmobile.typeform.com/to/FQbwSIEC"));
                            startActivity(toTutPage);
                        }catch(Exception e){
                            AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                            prompt.setTitle("There is no default browser to open this link. Would you like to copy it instead?");
                            prompt.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Link", "https://duelingmobile.typeform.com/to/FQbwSIEC");
                                    clipboard.setPrimaryClip(clip);
                                }
                            });
                            prompt.show();
                        }
                    }
                });

            findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(findViewById(R.id.maincontainer).getForeground() != null)
                        return;
                    Intent myIntent = new Intent(view.getContext(), DonationController.class);
                    startActivity(myIntent);
                }
            });

            mDeckCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    findViewById(R.id.coverimg).setVisibility(View.VISIBLE);
                    findViewById(R.id.maincontainer).setForeground(getResources().getDrawable(R.drawable.loadingscreen));
                    Intent myIntent = new Intent(view.getContext(), DeckEditorActivity.class);
                    startActivityForResult(myIntent, 0);

                }
            });
            mDeckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        findViewById(R.id.coverimg).setVisibility(View.VISIBLE);
                        findViewById(R.id.maincontainer).setForeground(getResources().getDrawable(R.drawable.loadingscreen));
                        Intent myIntent = new Intent(view.getContext(), DeckEditorActivity.class);
                        myIntent.putExtra("DECK_NAME", ((TextView) adapterView.getChildAt(i)).getText().toString());
                        startActivityForResult(myIntent, 0);
                    } catch(Exception e){
                        FirebaseCrashlytics.getInstance().recordException(e);
                        AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                        prompt.setTitle("Error in opening deck, please try again");
                        prompt.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        prompt.show();
                    }
                }
            });

            findViewById(R.id.waiting2).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

            findViewById(R.id.feedbackbut).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(findViewById(R.id.maincontainer).getForeground() != null)
                        return;
                    Intent myIntent = new Intent(view.getContext(), EmailController.class);
                    startActivity(myIntent);
                }
            });

            mDuel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (((TextView) findViewById(R.id.showname)).getText().toString().equals("") || ((TextView) findViewById(R.id.showname)).getText().toString().contains(" ") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("@") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("PING") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("JOININGGAME") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTT") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTO") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTD") || ((TextView) findViewById(R.id.showname)).getText().toString().contains("/") ||  ((TextView) findViewById(R.id.showname)).getText().toString().equals("NAMEREJECT")) {

                        AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                        prompt.setTitle("Invalid Name");
                        prompt.setNegativeButton("OK", null);
                        prompt.show();


                    } else {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    os.writeUTF("HOSTT");
                                    os.flush();
                                    os.writeUTF(((TextView) findViewById(R.id.showname)).getText().toString());
                                    os.flush();
                                    return;
                                } catch (Exception ex) {
                                    final Exception e = ex;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            FirebaseCrashlytics.getInstance().recordException(e);
                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }

                }
            });

                mDuel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (((TextView) findViewById(R.id.showname)).getText().toString().equals("") || ((TextView) findViewById(R.id.showname)).getText().toString().contains(" ") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("@") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("PING") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("JOININGGAME") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTT") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTO") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTD") || ((TextView) findViewById(R.id.showname)).getText().toString().contains("/") ||  ((TextView) findViewById(R.id.showname)).getText().toString().equals("NAMEREJECT")) {

                            AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                            prompt.setTitle("Invalid Name");
                            prompt.setNegativeButton("OK", null);
                            prompt.show();


                        } else {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        os.writeUTF("HOSTO");
                                        os.flush();
                                        os.writeUTF(((TextView) findViewById(R.id.showname)).getText().toString());
                                        os.flush();
                                        return;
                                    } catch (Exception ex) {
                                        final Exception e = ex;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                FirebaseCrashlytics.getInstance().recordException(e);
                                            }
                                        });
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }

                    }
                });

                testButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.coverimg).setVisibility(View.VISIBLE);
                        findViewById(R.id.maincontainer).setForeground(getResources().getDrawable(R.drawable.loadingscreen));
                        Intent intent = new Intent(con, TestDeck.class);
                        startActivity(intent);
                    }
                });

                mDuel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (((TextView) findViewById(R.id.showname)).getText().toString().equals("") || ((TextView) findViewById(R.id.showname)).getText().toString().contains(" ") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("@") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("PING") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("JOININGGAME") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTT") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTO") || ((TextView) findViewById(R.id.showname)).getText().toString().equals("HOSTD") || ((TextView) findViewById(R.id.showname)).getText().toString().contains("/") ||  ((TextView) findViewById(R.id.showname)).getText().toString().equals("NAMEREJECT")) {

                            AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                            prompt.setTitle("Invalid Name");
                            prompt.setNegativeButton("OK", null);
                            prompt.show();


                        } else {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        os.writeUTF("HOSTD");
                                        os.flush();
                                        os.writeUTF(((TextView) findViewById(R.id.showname)).getText().toString());
                                        os.flush();
                                        return;
                                    } catch (Exception ex) {
                                        final Exception e = ex;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                FirebaseCrashlytics.getInstance().recordException(e);
                                            }
                                        });
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }

                    }
                });



            /*impo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    helper.importDb();
                    helper = new myDbAdapter(con);
                    if(helper.getData() != null) {
                        adapter = new ArrayAdapter<String>(con, R.layout.dek_list_view, helper.getData());
                        mDeckList.setAdapter(adapter);
                    }
                }
            });

            expo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        helper.exportDb();
                    } catch (IOException e) {
                        mTextMessage.setText(e.getMessage());
                    }
                }
            });*/

            ((EditText) findViewById(R.id.showname)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                    try {
                        fileOut = new BufferedWriter(new FileWriter(userNameFile, false));
                        fileOut.write(((TextView) findViewById(R.id.showname)).getText().toString());
                        fileOut.flush();
                        fileOut.close();
                        InputMethodManager imm = (InputMethodManager) con.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                        textView.clearFocus();
                    } catch (IOException e) {
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }

                    return true;
                }
            });
        }

        }catch(Exception e){

            AlertDialog.Builder prompt = new AlertDialog.Builder(con);
            prompt.setTitle("An error occured please restart: " + e.getMessage());
            prompt.setNegativeButton("OK", null);
            prompt.show();

            FirebaseCrashlytics.getInstance().recordException(e);

        }

    }

    @Override
    protected void onPause() {

        hosting = false;

        if(findViewById(R.id.waiting) != null)
        findViewById(R.id.waiting).setVisibility(View.INVISIBLE);

        if(socket != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        close = true;
                        os.writeUTF("QUIT");
                        os.flush();
                        return;
                    }catch (Exception ex) {
                        final Exception e = ex;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        super.onPause();

    }


    @Override
    protected void onResume() {

        if (inter) {
            findViewById(R.id.coverimg).setVisibility(View.INVISIBLE);
            findViewById(R.id.maincontainer).setForeground(null);

        mTextMessage.setText(R.string.title_home);
        findViewById(R.id.waiting).setVisibility(View.INVISIBLE);
        //findViewById(R.id.deckListCont).setVisibility(View.INVISIBLE);
        //findViewById(R.id.gameListCont).setVisibility(View.INVISIBLE);
        mDeckList.setVisibility(View.INVISIBLE);
        mGameList.setVisibility(View.INVISIBLE);
        mDeckCreateButton.setVisibility(View.INVISIBLE);
        frontPage.setVisibility(View.VISIBLE);
        //impo.setVisibility(View.INVISIBLE);
        //expo.setVisibility(View.INVISIBLE);
        mDuel.setVisibility(View.INVISIBLE);
        mDuel2.setVisibility(View.INVISIBLE);
        mDuel3.setVisibility(View.INVISIBLE);
        testButton.setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.showname)).setVisibility(View.INVISIBLE);

        super.onResume();

        /*if(!inter) {
            super.onResume();
        }else {

            try {

                if(mTextMessage.getText().equals("Deck Editor"))
                    findViewById(R.id.maincontainer).setForeground(null);

                if (helper.getData() != null) {
                    adapter = new ArrayAdapter<String>(con, R.layout.dek_list_view, helper.getData());
                    mDeckList.setAdapter(adapter);
                    fileIn = new BufferedReader(new FileReader(userNameFile));
                    ((EditText) findViewById(R.id.showname)).setText(fileIn.readLine());
                    fileIn.close();
                }
            } catch (Exception e) {
                mTextMessage.setText(e.getMessage());
            }
            if (mTextMessage.getText().equals("Duel Zone")) {
                findViewById(R.id.maincontainer).setForeground(null);
                hosting = false;
                findViewById(R.id.waiting).setVisibility(View.INVISIBLE);
                mDeckList.setVisibility(View.INVISIBLE);
                mGameList.setVisibility(View.VISIBLE);
                mDuel.setVisibility(View.VISIBLE);
                mDeckCreateButton.setVisibility(View.INVISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket("192.168.0.39", 1236);
                            //socket = new Socket("ygo.ignorelist.com", 1236);
                            is = new DataInputStream(socket.getInputStream());
                            os = new DataOutputStream(socket.getOutputStream());
                            close = false;

                            while (!close) {

                                String buffer = is.readUTF();

                                if (buffer.contains("JOININGGAME")) {

                                    Intent intent = new Intent(con, NewDuelActivity.class);
                                    intent.putExtra("KEY", buffer.substring(11));
                                    startActivity(intent);
                                    close = true;

                                } else if (buffer.contains("@")) {

                                    /*runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mGameList.setAdapter(null);
                                        }
                                    });

                                }  else if(buffer.equals("")){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mGameList.setAdapter(null);
                                        }
                                    });

                                } else if(buffer.equals("NAMEREJECT")){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                                            prompt.setTitle("Name has been taken");
                                            prompt.setNegativeButton("OK", null);
                                            prompt.show();
                                        }
                                    });

                                }else if (buffer.contains("HOSTCONFIRM")) {

                                    hosting = true;

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            findViewById(R.id.waiting).setVisibility(View.VISIBLE);


                                        }
                                    });

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            while (!close && socket != null && os != null) {

                                                try {

                                                    os.writeUTF("PING");
                                                    os.flush();
                                                    Thread.sleep(500);
                                                } catch (Exception e) {


                                                }

                                            }

                                        }
                                    }).start();

                                } else {
                                    List<String> gamesS = Arrays.asList(buffer.split("/"));
                                    Collections.reverse(gamesS);
                                    adapter = new ArrayAdapter(con, R.layout.dek_list_view, gamesS.toArray());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mGameList.setAdapter(adapter);
                                        }
                                    });
                                }

                            }

                            socket.close();
                            socket = null;
                            is = null;
                            os = null;
                            return;
                        } catch (Exception e) {
                            final Exception ex = e;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mTextMessage.setText(ex.getMessage());

                                }
                            });
                            return;
                        }
                    }
                }).start();

                mGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String target = adapterView.getItemAtPosition(i).toString();
                        if (!target.equals("")) {
                            try {
                                os.writeUTF(target);
                                os.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

            }
            super.onResume();
        }*/
    }else
        super.onResume();
    }

    private String readAll(Reader rd) throws Exception {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
            if(cp == '\n')
                cardCoutn++;
        }
        return sb.toString();
    }

    public void makeJSONFromUrl(String url2, File f) throws IOException {
        try{
        URL url = new URL(url2);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        con.setRequestMethod("GET");
        con.connect();
        Log.d("URL", "CONNECTED");
        fromApi = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(fromApi, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            fromApi.close();
            con.disconnect();
            f.createNewFile();
            FileWriter fileW = new FileWriter(f);
            fileW.write(jsonText);
            fileW.flush();
            fileW.close();

            Log.d("CONTENT", "RECEIVED");
            inter = true;

            getImages(f);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.coverimg).setVisibility(View.INVISIBLE);
                    findViewById(R.id.maincontainer).setForeground(null);
                }
            });

        } catch (Exception ex) {
            final Exception e = ex;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            });
            e.printStackTrace();
        }

    }

    public void getImages(File f) {

        Log.i("TAG", "ATTEMPTING");

        AssetPackManager assetPackManager = AssetPackManagerFactory.getInstance(con);
        assetPackManager.registerListener(new AssetPackStateUpdateListener() {
            @Override
            public void onStateUpdate(AssetPackState assetPackState) {
                switch (assetPackState.status()) {
                    case AssetPackStatus.PENDING:
                        Log.i("TAG", "Pending");
                        break;

                    case AssetPackStatus.DOWNLOADING:
                        long downloaded = assetPackState.bytesDownloaded();
                        long totalSize = assetPackState.totalBytesToDownload();
                        double percent = 100.0 * downloaded / totalSize;

                        Log.i("TAG", "PercentDone=" + String.format("%.2f", percent));
                        break;

                    case AssetPackStatus.TRANSFERRING:
                        // 100% downloaded and assets are being transferred.
                        // Notify user to wait until transfer is complete.
                        Log.i("TAG", "Packages are transferring");
                        break;

                    case AssetPackStatus.COMPLETED:
                        // Asset pack is ready to use. Start the game.
                        Log.i("TAG", "Packages have been installed");
                        break;

                    case AssetPackStatus.FAILED:
                        // Request failed. Notify user.
                        Log.i("TAG", String.valueOf(assetPackState.errorCode()));
                        break;

                    case AssetPackStatus.CANCELED:
                        // Request canceled. Notify user.
                        Log.i("TAG", "Packages have been canceled");
                        break;

                    case AssetPackStatus.WAITING_FOR_WIFI:
                        if (!waitForWifiConfirmationShown) {
                            assetPackManager.showCellularDataConfirmation(MainActivity.this)
                                    .addOnSuccessListener(new OnSuccessListener<Integer>() {
                                        @Override
                                        public void onSuccess(Integer resultCode) {
                                            if (resultCode == RESULT_OK) {
                                                Log.i("TAG", "Confirmation dialog has been accepted.");
                                            } else if (resultCode == RESULT_CANCELED) {
                                                Log.i("TAG", "Confirmation dialog has been denied by the user.");
                                            }
                                        }
                                    });
                            waitForWifiConfirmationShown = true;
                        }
                        break;

                    case AssetPackStatus.NOT_INSTALLED:
                        // Asset pack is not downloaded yet.
                        Log.i("TAG", "Packages have not been installed");
                        break;
                }
            }
        });

        assetPackManager.getPackStates(Collections.singletonList("cards")).addOnCompleteListener(new OnCompleteListener<AssetPackStates>() {
            @Override
            public void onComplete(Task<AssetPackStates> task) {
                AssetPackStates assetPackStates = task.getResult();
                AssetPackState assetPackState = assetPackStates.packStates().get("cards");
                Log.i("TAG", String.valueOf(assetPackState.totalBytesToDownload()));
            }
        });

        assetPackManager.fetch(Collections.singletonList("cards"));

      /*  Log.d("NEWCARD", "GETCARDS");

        try {

            File dir = new File(con.getFilesDir(), "cards");

            if (!dir.exists()) {
                dir.mkdir();
            }

            ArrayList<String> currentNames = new ArrayList<String>(Arrays.asList(dir.list()));

            if (currentNames.size() < cardCoutn) {

                cardCoutn -= currentNames.size();

                final int cnt = dir.list().length;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((ProgressBar)findViewById(R.id.downloadprogress)).setMax((cardCoutn-cnt));
                        ((ProgressBar)findViewById(R.id.downloadprogress)).setProgress(0);

                    }
                });

                BufferedReader fileIn = new BufferedReader(new FileReader(f));
                String info;
                String[] temp;
                int iter = 0;

                while ((info = fileIn.readLine()) != null) {

                    temp = info.split("\\$\\$");
                    if (!currentNames.contains(temp[1] + ".jpg")) {


                            Log.d("NEWCARD", temp[1]);
                            new GetCards().execute(new Object[]{urlServer2 + temp[1] + ".jpg"});


                    }

                }

            }
        }catch(Exception e){

            Log.d("ERROR", e.getMessage());

        }*/

    }

    public void verifyVersion(String url2) throws IOException {
        try{
            URL url = new URL(url2);
            HttpURLConnection con2 = (HttpURLConnection)url.openConnection();
            con2.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            con2.setRequestMethod("GET");
            con2.connect();
            DataInputStream ver = new DataInputStream(con2.getInputStream());
            String v = "";
            boolean movf = false;
            while((v = ver.readLine()) != null && !v.equals("")) {
                if (v.equals("1.0.6"))
                    movf = true;
            }
            ver.close();
            con2.disconnect();
            if(!movf){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                        prompt.setTitle("Please Update Dueling Mobile");
                        prompt.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finishAndRemoveTask();
                            }
                        });
                        prompt.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                finishAndRemoveTask();
                            }
                        });
                        prompt.show();
                    }
                });

            }


        } catch (Exception ex) {
            final Exception e = ex;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            });
            e.printStackTrace();
        }

    }

    private class GetCards extends AsyncTask<Object, Void, Void> {


        @Override
        protected Void doInBackground(Object... url) {

            InputStream is = null;
            HttpURLConnection cone = null;
            int tries = 0;
            boolean read = false;

            while(tries <= 3 && !read) {

                tries++;

                Log.d("TRY", tries + " " + url[0]);

                try {
                    URL url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                    //URL url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                    cone = (HttpURLConnection) url2.openConnection();
                    Log.d("RESPONSE", ((String) url[0]).substring(urlServer2.length(), ((String) url[0]).lastIndexOf('.')) + "vs" + url[0]);
                    if (cone.getResponseCode() != 200) {

                        Log.d("GENERATING", (String) url[0]);

                        url2 = new URL(((String) url[0]));
                        cone = (HttpURLConnection) url2.openConnection();
                        boolean sent = false;
                        int triesSocket = 0;
                        while (!sent && triesSocket <= 3) {
                            triesSocket++;
                            try {
                                Log.d("TRY", "SOCKET" + tries + " " + url[0]);
                                Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                                //Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                                DataOutputStream request = new DataOutputStream(requestCardUpdate.getOutputStream());
                                request.writeUTF(((String) url[0]).substring(urlServer2.length(), ((String) url[0]).lastIndexOf('.')));
                                request.flush();
                                request.close();
                                requestCardUpdate.close();
                                sent = true;
                            } catch (Exception e) {
                                final Exception ex = e;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FirebaseCrashlytics.getInstance().recordException(ex);
                                    }
                                });

                            }
                        }

                    } else {

                        //url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                        url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                        cone = (HttpURLConnection) url2.openConnection();

                    }

                    cone.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    cone.setRequestMethod("GET");
                    cone.connect();
                    is = cone.getInputStream();
                    //is = (InputStream) new URL(url[0]).getContent();
                    //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
                } catch (IOException ex) {
                    final Exception e = ex;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    });
                    e.printStackTrace();
                }

                File imageFile = new File(con.getFilesDir(), "cards/" + ((String) url[0]).substring(urlServer2.length()));
                Log.d("MAKECARD", imageFile.getPath());

                try {

                    imageFile.createNewFile();
                    FileOutputStream fileOut = new FileOutputStream(imageFile);
                    byte[] imgBuffer = new byte[2048];
                    int length = 0;

                    while ((length = is.read(imgBuffer)) != -1) {

                        fileOut.write(imgBuffer, 0, length);

                    }

                    fileOut.flush();
                    fileOut.close();

                    is.close();
                    cone.disconnect();
                    read = true;

                } catch (Exception e) {

                    e.printStackTrace();

                }

                Log.d("MADE", (String) url[0]);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progress++;
                    ((ProgressBar)findViewById(R.id.downloadprogress)).setProgress(progress);
                    ((TextView) findViewById(R.id.fillertext)).setText(progress + " out of " + (cardCoutn));
                    Log.d("PROGRESS", progress+"");

                }
            });

            super.onPostExecute(aVoid);
        }


    }

}