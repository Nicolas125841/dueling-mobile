package com.psquaredsoftware.ygomobile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.psquaredsoftware.ygomobile.R;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DeckEditorActivity extends AppCompatActivity {

    private Button mDeckExitButton;
    ArrayAdapter adapter;
    Bitmap[] imgList = new Bitmap[1];
    private LinearLayout mSearchList;
    Context c;
    QuoteBank textRet;
    List<String> names;
    ImageView prev;
    Formatter toUrl;
    AutoCompleteTextView input;
    Drawable selected;
    Button toMain;
    Button toExtra;
    Button toSide;
    Button delete;
    GridView mainDeck;
    ArrayList<Drawable> mainDeckList;
    ArrayList<String> mainDeckListNames;
    GridView extraDeck;
    ArrayList<Drawable> extraDeckList;
    ArrayList<String> extraDeckListNames;
    GridView sideDeck;
    ArrayList<Drawable> sideDeckList;
    ArrayList<String> sideDeckListNames;
    ImageView high = null;
    ArrayList<String> searchResults;
    int selectedIndex = 0;
    myDbAdapter helper;
    Button mSaveButton;
    String m_Text = "";
    Boolean edit = false;
    String value;
    int height, width;
    double heightToDivide;
    Display display;
    Point screenSize;
    TextView mainCount;
    TextView extraCount;
    TextView sideCount;
    final String urlServer = "https://storage.googleapis.com/ygoprodeck.com/pics/";
    final String urlServer2 = "https://storage.googleapis.com/ygoprodeck.com/pics_small/";
    int cardSearchIndex = 0;
    Context context;
    int totalCount = 0;
    int expected = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            textRet = new QuoteBank(this);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("QUOTERROR", e.getMessage());
        }
        while(!textRet.initialized);
        searchResults = new ArrayList<String>();
        mainDeckList = new ArrayList<Drawable>();
        extraDeckList = new ArrayList<Drawable>();
        sideDeckList = new ArrayList<Drawable>();
        mainDeckListNames = new ArrayList<String>();
        extraDeckListNames = new ArrayList<String>();
        sideDeckListNames = new ArrayList<String>();
        setContentView(R.layout.deck_editor_layout);
        delete = findViewById(R.id.deleteButton);
        toMain = findViewById(R.id.toMainB);
        mainDeck = (GridView)findViewById(R.id.mainDeck);
        extraDeck = findViewById(R.id.extradeck);
        sideDeck = findViewById(R.id.siededeck);
        c = this;
        mSaveButton = findViewById(R.id.button5);
        input = findViewById(R.id.editText4);
        toUrl = new Formatter();
        mDeckExitButton = findViewById(R.id.button4);
        toExtra = findViewById(R.id.toExtraB);
        toSide = findViewById(R.id.toSideB);
        helper = new myDbAdapter(this);
        Bundle extras = getIntent().getExtras();
        display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        width = screenSize.x;
        height = screenSize.y;
        heightToDivide = ((((height / width) - 1.0) * 4.0) + 5) * (10/7);
        mainCount = findViewById(R.id.mainCo);
        extraCount = findViewById(R.id.extraCo);
        sideCount = findViewById(R.id.sideCo);
        //heightToDivide = 5 * (10/7);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainDeck.getLayoutParams();
        params.height = (int) ((height/heightToDivide) * 2.4);
        mainDeck.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) sideDeck.getLayoutParams();
        params.height = (int) ((height/heightToDivide) * 0.77);
        sideDeck.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) extraDeck.getLayoutParams();
        params.height = (int) ((height/heightToDivide) * 0.77);
        extraDeck.setLayoutParams(params);

        mainDeck.setVerticalSpacing(0);
        sideDeck.setVerticalSpacing(0);
        extraDeck.setVerticalSpacing(0);

        //extras = getIntent().getExtras();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        mSearchList = findViewById(R.id.cards);

        HorizontalScrollView.LayoutParams parm = (HorizontalScrollView.LayoutParams) mSearchList.getLayoutParams();
        parm.height = (int) (height/heightToDivide);
        mSearchList.setLayoutParams(parm);

        context = this;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, textRet.getNamesList());
        input.setAdapter(adapter);

        //new RetrieveImage().execute("http://192.168.0.33/photos/ORCUST%20BRASS%20BOMBARD.jpg");

        if(extras != null) {
            value = extras.getString("DECK_NAME");
            if (value != null && !value.equals("")) {
                String[] dbGet = helper.getDataDeck(value);
                edit = true;
               final String[] splitDb = dbGet[0].split("&&");
               expected = splitDb.length - 3;
               // mDeckExitButton.setText(splitDb[19]);

                int state = 0;

                for (int i = 0; i < splitDb.length; i++) {

                    if (splitDb[i].equals("EXTRADECK")) {
                        state = 1;
                        continue;
                    }

                    if (splitDb[i].equals("SIDEDECK")) {
                        state = 2;
                        continue;
                    }

                    if (state == 0) {

                        //new RetrieveImageMain().execute(urlServer2 + splitDb[i] + ".jpg");

                        Object[] d = new Object[2];
                        try {
                            Log.d("CARDSELECT", splitDb[i]);
                            InputStream is = new FileInputStream(new File(context.getFilesDir(), "cards/" + splitDb[i] + ".jpg"));
                            d[0] = Drawable.createFromStream(is, splitDb[i]);
                            d[1] = splitDb[i];
                            Log.d("CARDSELECT", splitDb[i]);
                            is.close();
                        }catch (Exception e){
                            e.printStackTrace();

                        }

                        Drawable im = (Drawable) d[0];

                        if(textRet.getStatus(textRet.getName((String) d[1])) == 0) {

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.banned).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else if(textRet.getStatus(textRet.getName((String) d[1])) == 1) {

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.limited).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else  if(textRet.getStatus(textRet.getName((String) d[1])) == 2){

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.semi).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else  if(textRet.getFormat(textRet.getName((String) d[1])) == 'O'){

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.ocg).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }


                        mainDeckList.add(im);
                        mainDeckListNames.add((String) d[1]);
                        totalCount++;
                        if(totalCount >= expected) {
                            mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width / 10, (int) (height / heightToDivide)));
                            mainDeck.invalidate();
                            extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                            extraDeck.invalidate();
                            sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width / 10, (int) (height / heightToDivide)));
                            sideDeck.invalidate();
                        }
                        updateNumbers();


                    } else if (state == 1) {
                        //new RetrieveImageExtra().execute(urlServer2 + splitDb[i] + ".jpg");
                        Object[] d = new Object[2];
                        try {
                            Log.d("CARDSELECT", splitDb[i]);
                            InputStream is = new FileInputStream(new File(context.getFilesDir(), "cards/" + splitDb[i] + ".jpg"));
                            d[0] = Drawable.createFromStream(is, splitDb[i]);
                            d[1] = splitDb[i];
                            Log.d("CARDSELECT", splitDb[i]);
                            is.close();
                        }catch (Exception e){
                            e.printStackTrace();

                        }

                        Drawable im = (Drawable) d[0];

                        if(textRet.getStatus(textRet.getName((String) d[1])) == 0) {

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.banned).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else if(textRet.getStatus(textRet.getName((String) d[1])) == 1) {

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.limited).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else  if(textRet.getStatus(textRet.getName((String) d[1])) == 2){

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.semi).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else  if(textRet.getFormat(textRet.getName((String) d[1])) == 'O'){

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.ocg).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }


                        extraDeckList.add(im);
                        extraDeckListNames.add((String) d[1]);
                        totalCount++;
                        if(totalCount >= expected) {
                            mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width / 10, (int) (height / heightToDivide)));
                            mainDeck.invalidate();
                            extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                            extraDeck.invalidate();
                            sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width / 10, (int) (height / heightToDivide)));
                            sideDeck.invalidate();
                        }
                        updateNumbers();

                    } else {

                        //new RetrieveImageSide().execute(urlServer2 + splitDb[i] + ".jpg");
                        Object[] d = new Object[2];
                        try {
                            Log.d("CARDSELECT", splitDb[i]);
                            InputStream is = new FileInputStream(new File(context.getFilesDir(), "cards/" + splitDb[i] + ".jpg"));
                            d[0] = Drawable.createFromStream(is, splitDb[i]);
                            d[1] = splitDb[i];
                            Log.d("CARDSELECT", splitDb[i]);
                            is.close();
                        }catch (Exception e){
                            e.printStackTrace();

                        }

                        Drawable im = (Drawable) d[0];

                        if(textRet.getStatus(textRet.getName((String) d[1])) == 0) {

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.banned).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else if(textRet.getStatus(textRet.getName((String) d[1])) == 1) {

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.limited).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else  if(textRet.getStatus(textRet.getName((String) d[1])) == 2){

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.semi).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }
                        else  if(textRet.getFormat(textRet.getName((String) d[1])) == 'O'){

                            Drawable[] layers = new Drawable[2];
                            layers[0] = (Drawable) d[0];
                            layers[1] = getResources().getDrawable(R.drawable.ocg).getConstantState().newDrawable().mutate();
                            LayerDrawable layerDrawable = new LayerDrawable(layers);
                            im = layerDrawable;

                        }


                        sideDeckList.add(im);
                        sideDeckListNames.add((String) d[1]);
                        totalCount++;
                        if(totalCount >= expected) {
                            mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width / 10, (int) (height / heightToDivide)));
                            mainDeck.invalidate();
                            extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                            extraDeck.invalidate();
                            sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width / 10, (int) (height / heightToDivide)));
                            sideDeck.invalidate();
                        }
                        updateNumbers();

                    }

                }


            }
        }
         prev = findViewById(R.id.imageView);
       // ViewCompat.setTranslationZ(prev, 1);
       // ViewCompat.setTranslationZ(mDeckExitButton, 2);
         prev.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View view) {
                 prev.setVisibility(View.GONE);
                 mDeckExitButton.setVisibility(View.VISIBLE);
                 delete.setVisibility(View.VISIBLE);
                 findViewById(R.id.button5).setVisibility(View.VISIBLE);
                 mainCount.setVisibility(View.VISIBLE);
                 sideCount.setVisibility(View.VISIBLE);
                 extraCount.setVisibility(View.VISIBLE);
             }
         });
         mSaveButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String dbInsert = "";

                 //First Sort the cards.
                 bubbleSort(mainDeckListNames, mainDeckList);
                 bubbleSort(extraDeckListNames, extraDeckList);
                 bubbleSort(sideDeckListNames, sideDeckList);

                 mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width/10, (int) (height/heightToDivide)));
                 extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                 sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width/10, (int) (height/heightToDivide)));

                 if(mainDeckListNames.size() == 0 && sideDeckListNames.size() == 0 && extraDeckListNames.size() == 0){

                     android.app.AlertDialog.Builder prompt = new android.app.AlertDialog.Builder(c);
                     prompt.setTitle("Can't save an empty deck.");
                     prompt.setNegativeButton("OK", null);
                     prompt.show();
                     return;

                 }

                 for(int i = 0; i < mainDeckListNames.size(); i++)
                     dbInsert += mainDeckListNames.get(i) + "&&";
                 dbInsert += "EXTRADECK&&";
                 for(int i = 0; i < extraDeckListNames.size(); i++)
                     dbInsert += extraDeckListNames.get(i) + "&&";
                 dbInsert += "SIDEDECK&&";
                 for(int i = 0; i < sideDeckListNames.size(); i++)
                     dbInsert += sideDeckListNames.get(i) + "&&";
                 //helper.delete("Altergeist");
                 String m_Text;
                 final String dbIn = dbInsert;
                 if(!edit) {
                     final AlertDialog.Builder builder = new AlertDialog.Builder(c);
                     builder.setTitle("Name Your Deck");

                     //TODO weird deck saving issue
                     final EditText input = new EditText(c);

                     input.setInputType(InputType.TYPE_CLASS_TEXT);
                     builder.setView(input);

                     builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             //m_Text =
                             //final String in = dbInsert;
                             long res = -1;
                             if(!input.getText().toString().equals("") && !input.getText().toString().contains(" ")) {
                                 res = helper.insertData(input.getText().toString(), dbIn, mainDeckList.size() + "");
                                 if(res != -1) {
                                     value = input.getText().toString();
                                     edit = true;
                                 }
                             }
                             mainDeck.requestFocus();
                             InputMethodManager imm = (InputMethodManager)getSystemService(
                                     Context.INPUT_METHOD_SERVICE);
                             imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                             if(res == -1){

                                 android.app.AlertDialog.Builder prompt = new android.app.AlertDialog.Builder(c);
                                 prompt.setTitle("Name is Invalid or Already Exists");
                                 prompt.setNegativeButton("OK", null);
                                 prompt.show();

                             }
                         }
                     });
                     builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                             mainDeck.requestFocus();
                         }
                     });

                     builder.show();
                 }else{

                     helper.delete(value);
                     helper.insertData(value, dbIn, mainDeckList.size() + "");

                 }

             }
         });
        mDeckExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //helper.insertData("temp", "");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit) {
                    helper.delete(value);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selected != null && mainDeckList.size() < 60 && checkIfThree(names.get(selectedIndex)) < 3 && textRet.getType(textRet.getName(names.get(selectedIndex))).equals("M")) {
                        //final ImageView temp = new ImageView(c);
                        //temp.setImageDrawable(selected);
                        mainDeckList.add(selected);
                        mainDeckListNames.add(names.get(selectedIndex));
                        mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width / 10, (int) (height / heightToDivide)));
                        updateNumbers();

                    }
                }catch(Exception e){

                        android.app.AlertDialog.Builder prompt = new android.app.AlertDialog.Builder(context);
                        prompt.setTitle("Something went wrong, please save and reopen the editor");
                        prompt.setNegativeButton("OK", null);
                        prompt.show();

                }
            }
        });
        toExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selected != null && extraDeckList.size() < 15 && checkIfThree(names.get(selectedIndex)) < 3 && textRet.getType(textRet.getName(names.get(selectedIndex))).equals("E")) {
                        //final ImageView temp = new ImageView(c);
                        //temp.setImageDrawable(selected);
                        extraDeckList.add(selected);
                        extraDeckListNames.add(names.get(selectedIndex));
                        extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width / 10, (int) (height / heightToDivide)));
                        updateNumbers();

                    }
                }catch(Exception e){

                    android.app.AlertDialog.Builder prompt = new android.app.AlertDialog.Builder(context);
                    prompt.setTitle("Something went wrong, please save and reopen the editor");
                    prompt.setNegativeButton("OK", null);
                    prompt.show();

                }
            }
        });
        toSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (selected != null && sideDeckList.size() < 15 && checkIfThree(names.get(selectedIndex)) < 3) {
                        //final ImageView temp = new ImageView(c);
                        //temp.setImageDrawable(selected);
                        sideDeckList.add(selected);
                        sideDeckListNames.add(names.get(selectedIndex));
                        sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width / 10, (int) (height / heightToDivide)));
                        updateNumbers();

                    }
                }catch(Exception e){

                    android.app.AlertDialog.Builder prompt = new android.app.AlertDialog.Builder(context);
                    prompt.setTitle("Something went wrong, please save and reopen the editor");
                    prompt.setNegativeButton("OK", null);
                    prompt.show();

                }
            }
        });
        mainDeck.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,int position, long id) {

                try {

                    new ShowCardPreview().execute(urlServer + mainDeckListNames.get(position) + ".jpg");

                }catch (Exception e){
                    FirebaseCrashlytics.getInstance().recordException(e);
                    mainCount.setText(e.getMessage());

                }

                /*prev.setImageDrawable(mainDeckList.get(position));
                prev.setVisibility(View.VISIBLE);
                mDeckExitButton.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                findViewById(R.id.button5).setVisibility(View.GONE);*/
                return true;
            }
        });
        mainDeck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainDeck.requestFocus();
                mainDeckList.remove(i);
                mainDeckListNames.remove(i);
                mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width/10, (int) (height/heightToDivide)));
                updateNumbers();
            }
        });
        extraDeck.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,int position, long id) {

                new ShowCardPreview().execute(urlServer + extraDeckListNames.get(position) + ".jpg");

                /*prev.setImageDrawable(extraDeckList.get(position));
                prev.setVisibility(View.VISIBLE);
                mDeckExitButton.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                findViewById(R.id.button5).setVisibility(View.GONE);*/
                return true;
            }
        });
        extraDeck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                extraDeck.requestFocus();
                extraDeckList.remove(i);
                extraDeckListNames.remove(i);
                extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                updateNumbers();
            }
        });
        sideDeck.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,int position, long id) {

                new ShowCardPreview().execute(urlServer + sideDeckListNames.get(position) + ".jpg");

                /*prev.setImageDrawable(sideDeckList.get(position));
                prev.setVisibility(View.VISIBLE);
                mDeckExitButton.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                findViewById(R.id.button5).setVisibility(View.GONE);*/
                return true;
            }
        });
        sideDeck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sideDeck.requestFocus();
                sideDeckList.remove(i);
                sideDeckListNames.remove(i);
                sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width/10, (int) (height/heightToDivide)));
                updateNumbers();
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //searchResults.clear();

                if(i == EditorInfo.IME_ACTION_DONE) {
                    cardSearchIndex++;
                    if (cardSearchIndex >= 1000)
                        cardSearchIndex = 0;
                    mSearchList.removeAllViews();
                    names = textRet.readLine(input.getText().toString());
                    input.dismissDropDown();
                    // mDeckExitButton.setText("" + names.size());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final int ind = cardSearchIndex;
                            for (int j = 0; j < names.size(); j++) {
                                //mDeckExitButton.setText(URLEncoder.encode(names.get(i), "UTF-8"));
                                if (j >= 15 || ind != cardSearchIndex)
                                    break;
                                new RetrieveImage().execute(new Object[]{urlServer2 + names.get(j) + ".jpg", cardSearchIndex});

                            }

                        }
                    }).start();

                }

                return false;
            }
        });

    }

    void bubbleSort(ArrayList<String> m, ArrayList<Drawable> t)
    {
        int n = m.size();
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (m.get(j).compareToIgnoreCase(m.get(j + 1)) < 0)
                {
                    // swap arr[j+1] and arr[i]
                    String temp = m.get(j);
                    Drawable temp2 = t.get(j);
                    m.set(j, m.get(j + 1));
                    t.set(j, t.get(j + 1));
                    m.set(j + 1, temp);
                    t.set(j + 1, temp2);
                }
    }

    private ArrayList<Drawable> makeDeepCopyDrawable(ArrayList<Drawable> old){
        ArrayList<Drawable> copy = new ArrayList<Drawable>(old.size());
        for(Drawable i : old){
            copy.add(i.getConstantState().newDrawable().mutate());
        }
        return copy;
    }

    public int checkIfThree(String name){

        int count = 0;

        for(int i = 0; i < mainDeckListNames.size(); i++){

            if(mainDeckListNames.get(i).equals(name))
                count++;

        }

        for(int i = 0; i < extraDeckListNames.size(); i++){

            if(extraDeckListNames.get(i).equals(name))
                count++;

        }

        for(int i = 0; i < sideDeckListNames.size(); i++){

            if(sideDeckListNames.get(i).equals(name))
                count++;

        }

        return count;

    }

    private class RetrieveImage extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object... url) {

            if((int)url[1] == cardSearchIndex) {

                InputStream is = null;
                /*try {
                    URL url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                    //URL url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                    HttpURLConnection con = (HttpURLConnection) url2.openConnection();
                    Log.d("RESPONSE", ((String) url[0]).substring(urlServer2.length(), ((String) url[0]).lastIndexOf('.')) + "vs" + url[0]);
                    if (con.getResponseCode() != 200) {

                        url2 = new URL(((String) url[0]));
                        con = (HttpURLConnection) url2.openConnection();
                        Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                        //Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                        DataOutputStream request = new DataOutputStream(requestCardUpdate.getOutputStream());
                        request.writeUTF(((String) url[0]).substring(urlServer2.length(), ((String) url[0]).lastIndexOf('.')));
                        request.flush();
                        request.close();
                        requestCardUpdate.close();

                    } else {

                        //url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                        url2 = new URL("http://ygo.ignorelist.com/photosSm/" + ((String) url[0]).substring(urlServer2.length()));
                        con = (HttpURLConnection) url2.openConnection();

                    }

                    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    con.setRequestMethod("GET");
                    con.connect();
                    is = con.getInputStream();
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
                //Drawable d = new BitmapDrawable(BitmapFactory.decodeStream(is));*/
                try {
                    is = new FileInputStream(new File(context.getFilesDir(), "cards/" + ((String) url[0]).substring(urlServer2.length())));
                }catch (Exception e){
                    e.printStackTrace();

                }
                Drawable d = Drawable.createFromStream(is, "src name");
                return new Object[]{d, url[1],((String) url[0]).substring(urlServer2.length(), ((String) url[0]).lastIndexOf('.'))};
            }else
                return new Object[]{null, url[1], ((String) url[0]).substring(urlServer2.length(), ((String) url[0]).lastIndexOf('.'))};
        }

        @Override
        protected void onPostExecute(Object[] r) {
            int ind = (int) r[1];
            if(ind == cardSearchIndex && r[0] != null){
                Drawable d = (Drawable) r[0];
            final ImageView img;
            img = new ImageView(c);

                if(textRet.getStatus(textRet.getName((String) r[2])) == 0) {
                    Drawable[] layers = new Drawable[2];
                    layers[0] = (Drawable) d;
                    layers[1] = getResources().getDrawable(R.drawable.banned).getConstantState().newDrawable().mutate();
                    LayerDrawable layerDrawable = new LayerDrawable(layers);
                    d = layerDrawable;
                }
                else if(textRet.getStatus(textRet.getName((String) r[2])) == 1) {
                    Drawable[] layers = new Drawable[2];
                    layers[0] = (Drawable) d;
                    layers[1] = getResources().getDrawable(R.drawable.limited).getConstantState().newDrawable().mutate();
                    LayerDrawable layerDrawable = new LayerDrawable(layers);
                    d = layerDrawable;
                }
                else  if(textRet.getStatus(textRet.getName((String) r[2])) == 2) {
                    Drawable[] layers = new Drawable[2];
                    layers[0] = (Drawable) d;
                    layers[1] = getResources().getDrawable(R.drawable.semi).getConstantState().newDrawable().mutate();
                    LayerDrawable layerDrawable = new LayerDrawable(layers);
                    d = layerDrawable;
                }else if(textRet.getFormat(textRet.getName((String) r[2])) == 'O'){
                    Drawable[] layers = new Drawable[2];
                    layers[0] = (Drawable) d;
                    layers[1] = getResources().getDrawable(R.drawable.ocg).getConstantState().newDrawable().mutate();
                    LayerDrawable layerDrawable = new LayerDrawable(layers);
                    d = layerDrawable;

                }

            img.setImageDrawable(d);
            //img.setCropToPadding(true);
            mSearchList.addView(img);
            LinearLayout.LayoutParams parmsss = (LinearLayout.LayoutParams) img.getLayoutParams();
            parmsss.height = (int) ((int) (height / heightToDivide) * 0.5);
            parmsss.width = width / 10;
            img.setLayoutParams(parmsss);

            //img.setScaleType(ImageView.ScaleType.FIT_XY);
            //img.setAdjustViewBounds(true);
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new ShowCardPreview().execute(urlServer + names.get(mSearchList.indexOfChild(img)) + ".jpg");

                    /*prev.setImageDrawable(img.getDrawable());
                    prev.setVisibility(View.VISIBLE);
                    mDeckExitButton.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                    findViewById(R.id.button5).setVisibility(View.GONE);*/
                    return false;
                }
            });
            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    selected = img.getDrawable();
                    selectedIndex = mSearchList.indexOfChild(img);
                    //mDeckExitButton.setText(names.get(selectedIndex));
                    if (high != null)
                        high.setForeground(null);

                    high = img;
                    Drawable highlight = getResources().getDrawable(R.drawable.highlight);
                    img.setForeground(highlight);

                }
            });
        }
        }
    }

    public void updateNumbers(){

        mainCount.setText("M: " + mainDeckList.size());
        extraCount.setText("E: " + extraDeckList.size());
        sideCount.setText("S: " + sideDeckList.size());

    }

    private class RetrieveImageMain extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {
                /*URL url2 = new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length()));
                HttpURLConnection con = (HttpURLConnection)url2.openConnection();
                //Log.d("RESPONSE", url[0].substring(urlServer2.length(), url[0].lastIndexOf('.')) + "vs" + url[0]);
                if(con.getResponseCode() != 200){

                    url2 = new URL(url[0]);
                    con = (HttpURLConnection)url2.openConnection();
                    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    con.setRequestMethod("GET");
                    Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                    DataOutputStream request = new DataOutputStream(requestCardUpdate.getOutputStream());
                    request.writeUTF(url[0].substring(urlServer2.length(), url[0].lastIndexOf('.')));
                    request.flush();
                    request.close();
                    requestCardUpdate.close();

                }else{

                    url2 = new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length()));
                    con = (HttpURLConnection)url2.openConnection();

                }

                con.connect();
                is = con.getInputStream();*/
                is = new FileInputStream(new File(context.getFilesDir(), "cards/" + url[0].substring(urlServer2.length())));
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length())).getContent();
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length())).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = url[0].substring(urlServer2.length(), url[0].lastIndexOf('.'));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {

            Drawable im = (Drawable) d[0];

            if(textRet.getStatus(textRet.getName((String) d[1])) == 0) {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.banned).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }
            else if(textRet.getStatus(textRet.getName((String) d[1])) == 1) {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.limited).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }
            else  if(textRet.getStatus(textRet.getName((String) d[1])) == 2){

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.semi).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }
            else  if(textRet.getFormat(textRet.getName((String) d[1])) == 'O'){

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.ocg).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }


            mainDeckList.add(im);
            mainDeckListNames.add((String) d[1]);
            totalCount++;
            if(totalCount >= expected) {
                mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width / 10, (int) (height / heightToDivide)));
                mainDeck.invalidate();
                extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                extraDeck.invalidate();
                sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width / 10, (int) (height / heightToDivide)));
                sideDeck.invalidate();
            }
            updateNumbers();
        }
    }
    private class RetrieveImageExtra extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {

                /*URL url2 = new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length()));
                HttpURLConnection con = (HttpURLConnection)url2.openConnection();
                //Log.d("RESPONSE", url[0].substring(urlServer2.length(), url[0].lastIndexOf('.')) + "vs" + url[0]);
                if(con.getResponseCode() != 200){

                    url2 = new URL(url[0]);
                    con = (HttpURLConnection)url2.openConnection();
                    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    con.setRequestMethod("GET");
                    Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                    DataOutputStream request = new DataOutputStream(requestCardUpdate.getOutputStream());
                    request.writeUTF(url[0].substring(urlServer2.length(), url[0].lastIndexOf('.')));
                    request.flush();
                    request.close();
                    requestCardUpdate.close();

                }else{

                    url2 = new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length()));
                    con = (HttpURLConnection)url2.openConnection();

                }

                con.connect();
                is = con.getInputStream();*/
                is = new FileInputStream(new File(context.getFilesDir(), "cards/" + url[0].substring(urlServer2.length())));
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length())).getContent();
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length())).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = url[0].substring(urlServer2.length(), url[0].lastIndexOf('.'));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {

            Drawable im = (Drawable) d[0];

            if(textRet.getStatus(textRet.getName((String) d[1])) == 0) {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.banned).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }
            else if(textRet.getStatus(textRet.getName((String) d[1])) == 1) {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.limited).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }
            else  if(textRet.getStatus(textRet.getName((String) d[1])) == 2){

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.semi).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }
            else  if(textRet.getFormat(textRet.getName((String) d[1])) == 'O'){

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.ocg).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }

            extraDeckList.add(im);
            extraDeckListNames.add((String) d[1]);
            totalCount++;
            if(totalCount >= expected) {
                mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width / 10, (int) (height / heightToDivide)));
                mainDeck.invalidate();
                extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                extraDeck.invalidate();
                sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width / 10, (int) (height / heightToDivide)));
                sideDeck.invalidate();
            }
            updateNumbers();
        }
    }
    private class RetrieveImageSide extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {

                /*URL url2 = new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length()));
                HttpURLConnection con = (HttpURLConnection)url2.openConnection();
                //Log.d("RESPONSE", url[0].substring(urlServer2.length(), url[0].lastIndexOf('.')) + "vs" + url[0]);
                if(con.getResponseCode() != 200){

                    url2 = new URL(url[0]);
                    con = (HttpURLConnection)url2.openConnection();
                    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    con.setRequestMethod("GET");
                    Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                    DataOutputStream request = new DataOutputStream(requestCardUpdate.getOutputStream());
                    request.writeUTF(url[0].substring(urlServer2.length(), url[0].lastIndexOf('.')));
                    request.flush();
                    request.close();
                    requestCardUpdate.close();

                }else{

                    url2 = new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length()));
                    con = (HttpURLConnection)url2.openConnection();

                }

                con.connect();
                is = con.getInputStream();*/
                is = new FileInputStream(new File(context.getFilesDir(), "cards/" + url[0].substring(urlServer2.length())));
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length())).getContent();
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photosSm/" + url[0].substring(urlServer2.length())).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = url[0].substring(urlServer2.length(), url[0].lastIndexOf('.'));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {

            Drawable im = (Drawable) d[0];

            if (textRet.getStatus(textRet.getName((String) d[1])) == 0) {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.banned).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            } else if (textRet.getStatus(textRet.getName((String) d[1])) == 1) {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.limited).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            } else if (textRet.getStatus(textRet.getName((String) d[1])) == 2) {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.semi).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            } else if (textRet.getFormat(textRet.getName((String) d[1])) == 'O') {

                Drawable[] layers = new Drawable[2];
                layers[0] = (Drawable) d[0];
                layers[1] = getResources().getDrawable(R.drawable.ocg).getConstantState().newDrawable().mutate();
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                im = layerDrawable;

            }

            sideDeckList.add(im);
            sideDeckListNames.add((String) d[1]);
            totalCount++;
            if(totalCount >= expected) {
                mainDeck.setAdapter(new CardImageAdapter(c, mainDeckList, width / 10, (int) (height / heightToDivide)));
                mainDeck.invalidate();
                extraDeck.setAdapter(new CardImageAdapter(c, extraDeckList, width/10, (int) (height/heightToDivide)));
                extraDeck.invalidate();
                sideDeck.setAdapter(new CardImageAdapter(c, sideDeckList, width / 10, (int) (height / heightToDivide)));
                sideDeck.invalidate();
            }
            updateNumbers();
        }
    }


    private class ShowCardPreview extends AsyncTask<String, Void, Drawable>{

        @Override
        protected Drawable doInBackground(String... url) {

            Drawable ret = null;
            InputStream is = null;

            try {

                /*URL url2 = new URL("http://ygo.ignorelist.com/photos/" + url[0].substring(urlServer.length()));
                HttpURLConnection con = (HttpURLConnection)url2.openConnection();
                //Log.d("RESPONSE", url[0].substring(urlServer2.length(), url[0].lastIndexOf('.')) + "vs" + url[0]);
                if(con.getResponseCode() != 200){

                    url2 = new URL(url[0]);
                    con = (HttpURLConnection)url2.openConnection();
                    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    con.setRequestMethod("GET");
                    Socket requestCardUpdate = new Socket("ygo.ignorelist.com", 1240);
                    DataOutputStream request = new DataOutputStream(requestCardUpdate.getOutputStream());
                    request.writeUTF(url[0].substring(urlServer.length(), url[0].lastIndexOf('.')));
                    request.flush();
                    request.close();
                    requestCardUpdate.close();

                }

                con.connect();
                is = con.getInputStream();
                ret = Drawable.createFromStream(is, "src name");*/

                is = new FileInputStream(new File(context.getFilesDir(), "cards/" + url[0].substring(urlServer.length())));
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photos/" + url[0].substring(urlServer.length())).getContent();
                //is = (InputStream) new URL("http://ygo.ignorelist.com/photos/" + url[0].substring(urlServer.length())).getContent();
                ret = Drawable.createFromStream(is, "src name");
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ret;

        }

        @Override
        protected void onPostExecute(Drawable drawable) {

            try {

                final Drawable prevImg = drawable;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            prev.setImageDrawable(prevImg);
                            prev.setVisibility(View.VISIBLE);
                            mDeckExitButton.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                            findViewById(R.id.button5).setVisibility(View.GONE);
                            mainCount.setVisibility(View.GONE);
                            sideCount.setVisibility(View.GONE);
                            extraCount.setVisibility(View.GONE);
                        }catch (Exception e){

                            mainCount.setText(e.getMessage());

                        }

                    }
                });

            }catch (Exception e){

                final Exception ex = e;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainCount.setText(ex.getMessage());
                    }
                });

            }

        }
    }

}