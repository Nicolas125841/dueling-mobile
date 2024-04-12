package com.psquaredsoftware.ygomobile;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
//import android.graphics.Color;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
//import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import com.psquaredsoftware.ygomobile.R;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TestDeck extends AppCompatActivity {

    //Locations
    final int DECK = 0;
    final int EDECK = 1;
    final int HAND = 2;
    final int FIELD = 3;
    final int GRAVE = 4;
    final int BANISH = 5;

    private float x1,x2;
    final int MIN_DISTANCE = 150;
    private boolean useNonEssentialButtons = true;

    //Card Holder
    ConcurrentHashMap<String, Drawable> cardMap;

    //Generic Variables
    int prevLocAni = -1;
    int prevXAni = -1;
    int prevYAni = -1;
    int[] prevXYZ = new int[2];
    boolean opR = false;
    boolean isToken = false;
    int mainDeckSize = 0;
    int extraDeckSize = 0;
    ImageView opSelectedCard = null;
    int diceRollValue = 1;
    double heightToDivide = 5;
    Display display;
    Point screenSize;
    double width;
    double height;
    Context con;
    Formatter urlConverter = new Formatter();
    ImageView selectedCard;
    int cardLocaion = 0;
    boolean attacking = false;
    Button flipCoin;
    //String cardImageServerAdress = "http://ygo.ignorelist.com/photos/";
    //192.169.0.39
    //String cardImageServerAdress = "http://192.168.0.39/photos/";
    //String cardImageServerAdress2 = "http://192.168.0.39/photosSm/";
    String cardImageServerAdress = "http://ygo.ignorelist.com/photos/";
    String cardImageServerAdress2 = "http://ygo.ignorelist.com/photosSm/";
    //Lobby Variables
    ArrayAdapter adapter = null;
    Bundle extras;
    Spinner list = null;
    myDbAdapter helper;
    String deckContentsString = "";
    Button readyButton;
    TextView opponentReady;
    TextView usReady;
    RelativeLayout exitPrev;
    int sizeB;
    ImageView oppRevealSelect;
    String oppRevealName;
    ImageView attachRevealSelect;
    String attachName;

    //Backend field information
    ArrayList<Integer> fieldCounters = new ArrayList<Integer>();
    ArrayList<Integer> fieldCountersOpponent = new ArrayList<Integer>();
    ArrayList<String> deckNames = new ArrayList<String>();
    ArrayList<String> graveyardNames = new ArrayList<String>();
    ArrayList<String> banishNames = new ArrayList<String>();
    ArrayList<String> fieldNames = new ArrayList<String>();
    ArrayList<String> extraDeckNames = new ArrayList<String>();
    ArrayList<String> handNames = new ArrayList<String>();
    ArrayList<String> deckNamesOpponent = new ArrayList<String>();
    ArrayList<String> graveyardNamesOpponent = new ArrayList<String>();
    ArrayList<String> banishNamesOpponent = new ArrayList<String>();
    ArrayList<String> fieldNamesOpponent = new ArrayList<String>();
    ArrayList<String> extraDeckNamesOpponent = new ArrayList<String>();
    ArrayList<String> handNamesOpponent = new ArrayList<String>();
    ArrayList<String> resetMainDeckNameList = new ArrayList<String>();
    ArrayList<String> resetExtraDeckNameList = new ArrayList<String>();
    ArrayList<String> sideDeckNameList = new ArrayList<String>();

    ArrayList<ImageView> deckContents = new ArrayList<ImageView>();
    ArrayList<ImageView> handContents = new ArrayList<ImageView>();
    ArrayList<ImageView> extraDeckContents = new ArrayList<ImageView>();
    ArrayList<ImageView> fieldContents = new ArrayList<ImageView>();
    ArrayList<ImageView> graveContents = new ArrayList<ImageView>();
    ArrayList<ImageView> banishContents = new ArrayList<ImageView>();
    ArrayList<ImageView> deckContentsOpponent = new ArrayList<ImageView>();
    ArrayList<ImageView> handContentsOpponent = new ArrayList<ImageView>();
    ArrayList<ImageView> extraDeckContentsOpponent = new ArrayList<ImageView>();
    ArrayList<ImageView> fieldContentsOpponent = new ArrayList<ImageView>();
    ArrayList<ImageView> graveContentsOpponent = new ArrayList<ImageView>();
    ArrayList<ImageView> banishContentsOpponent = new ArrayList<ImageView>();

    //Frontend access UI
    ImageView tokenCard;
    ImageView yourDeck;
    ImageView yourGrave;
    ImageView yourBanish;
    ImageView yourExtraDeck;
    ImageView opponentGrave;
    ImageView opponentBanish;
    ImageView opponentExtraDeck;
    ImageView previewCard;
    TextView opponentHealth;
    TextView yourHealth;
    TextView dieNumber;

    //Frontend modification UI
    ScrollView showScroll;
    ScrollView showAttached;
    ScrollView deckScroll;
    ScrollView graveScroll;
    ScrollView banishScroll;
    ScrollView extraDeckScroll;
    ScrollView opponentExtraDeckScroll;
    ScrollView opponentGraveScroll;
    ScrollView opponentBanishScroll;
    GridLayout opponentShow;
    GridLayout attachedShow;
    GridLayout yourHand;
    GridLayout opponentHand;
    GridLayout yourDeckContents;
    GridLayout yourExtraDeckContents;
    GridLayout yourGraveContents;
    GridLayout yourBanishContents;
    GridLayout opponentExtraDeckContents;
    GridLayout opponentGraveContents;
    GridLayout opponentBanishContents;
    RelativeLayout gameField;
    RelativeLayout aniCover;
    ScrollView extraOptions;
    RelativeLayout dieResults;
    RelativeLayout resetConfirm;
    RelativeLayout sideConfirm;

    //UI Stuff
    Button showHandToOpponent;
    Button changeControl;
    Button toGraveButton;
    Button toBanishButton;
    Button toDeckButton;
    Button toEdeckButton;
    Button toHandButton;
    Button attackDec;
    Button viewAttached;
    Button millFromTop;
    Button confirmSide;
    Button confirmReset;
    Button sideCards;
    Button resetGame;
    Button leaveGame;
    Button surrender;
    Button addCounter;
    Button removeCounter;
    Button shuffleDeck;
    Button shuffleHand;
    Button shuffleEDeck;
    Button specialControlsButton;
    Button rollDice;
    Button drawFromMainDeck;
    Button banishFromMainDeck;
    Button banishFromExtraDeck;
    Button cardFaceUpOrDown;
    Button cardDefenseOrAttack;
    Button viewCard;
    Switch autoSet;
    LinearLayout opponentRevealControlsE;
    LinearLayout opponentRevealControlD;
    LinearLayout attachControls;
    LinearLayout cardControls;
    LinearLayout deckControls;
    LinearLayout extraDeckControls;
    Activity activity;
    AudioRecord ar;
    AudioTrack audioTrack;

    //Side Deck Stuff
    Button finishSiding;
    Button sideToMain;
    Button sideToExtra;
    Button sideToSide;
    GridView mainDeckSide;
    GridView sideDeckSide;
    GridView extraDeckSide;
    ImageView previewCardSide;
    TextView mainCount;
    TextView extraCount;
    TextView sideCount;
    double heightForSiding;
    ArrayList<Drawable> mainDeckList = new ArrayList<Drawable>();
    ArrayList<Drawable> extraDeckList = new ArrayList<Drawable>();
    ArrayList<Drawable> sideDeckList = new ArrayList<Drawable>();
    Drawable selectedSide;
    int selectedIndex;
    boolean ending = false;
    int amountRead;
    int amountRecorded;
    QuoteBank cardInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            //Init Lobby Variables
            setContentView(R.layout.lobby_view);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            cardInfo = new QuoteBank(this);
            helper = new myDbAdapter(this);
            while(!cardInfo.initialized);
            readyButton = findViewById(R.id.testB);
            opponentReady = findViewById(R.id.opId);
            opponentReady.setVisibility(View.GONE);
            usReady = findViewById(R.id.youId);
            list = findViewById(R.id.deckChoice);
            con = this;
            //Get deck list
            if(helper.getData() != null) {
                adapter = new ArrayAdapter<String>(this, R.layout.dek_list_view, helper.getData());
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                list.setAdapter(adapter);
            }

            findViewById(R.id.lobbycover).setVisibility(View.INVISIBLE);

            readyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        duelStart();
                    } catch (Exception ex) {
                        final Exception e = ex;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseCrashlytics.getInstance().recordException(e);
                                gameExitCrash();
                                Log.d("ERROR", e.getMessage());
                            }
                        });
                    }
                }
            });

            activity = this;

        }catch (Exception e){

            FirebaseCrashlytics.getInstance().recordException(e);
            readyButton.setText(e.getMessage());

        }

    }

    @Override
    public void onBackPressed() {
            if(findViewById(R.id.messagebutton) != null)
            clearForDrag();
    }

    public void duelStart() throws Exception {

        try {

            deckContentsString = helper.getDataDeck(list.getSelectedItem().toString())[0];
            setContentView(R.layout.game_layout);

            defineVariables();

            initializeGame();
        }catch(Exception e){
            AlertDialog.Builder prompt = new AlertDialog.Builder(con);
            prompt.setTitle("The deck name is outdated, or something went wrong. Please try again.");
            prompt.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            prompt.show();
        }

    }

    public void gameExitCrash(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void initializeGame() throws InterruptedException {

        String[] splitDb = deckContentsString.split("&&");

        int state = 0;

        for (int i = 0; i < splitDb.length; i++) {

            if (splitDb[i].equals("EXTRADECK")) {
                state = 1;
                mainDeckSize = i;
                continue;
            }

            if (splitDb[i].equals("SIDEDECK")) {
                state = 2;
                continue;
            }

            if (state == 0) {

                //Add to the main deck
                resetMainDeckNameList.add(splitDb[i]);
                new putToDeck().execute(cardImageServerAdress2 + urlConverter.formatURL(splitDb[i]) + ".jpg");

            } else if (state == 1) {

                //Add to the extra deck
                if(yourExtraDeck.getAlpha() == 0.1f){

                    yourExtraDeck.setAlpha(1.0f);

                }
                extraDeckSize++;
                resetExtraDeckNameList.add(splitDb[i]);
                new putToExtraDeck().execute(cardImageServerAdress2 + urlConverter.formatURL(splitDb[i]) + ".jpg");

            } else {

                sideDeckNameList.add(splitDb[i]);

            }

        }

        startFunctionality();
    }

    public void startFunctionality(){

        ((LinearLayout) findViewById(R.id.banishPadding)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        ((LinearLayout) findViewById(R.id.extraDeckPadding)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        ((LinearLayout) findViewById(R.id.extraDeckPaddingOP)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        ((LinearLayout) findViewById(R.id.gravePadding)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        ((LinearLayout) findViewById(R.id.deckPadding)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        ((LinearLayout) findViewById(R.id.opponentGravePadding)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        ((LinearLayout) findViewById(R.id.opponentBanishPadding)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        dieResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dieResults.setVisibility(View.INVISIBLE);
            }
        });

        flipCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    flipTheCoin();
                    dieNumber.setText((diceRollValue < 7) ? String.valueOf(diceRollValue) : ((diceRollValue == 9) ? "Heads" : "Tails"));
                    dieResults.setVisibility(View.VISIBLE);
                    ColorDrawable colfod = new ColorDrawable();
                    colfod.setColor(Color.argb(125, 91, 236, 17));
                    dieResults.setBackground(colfod);
                    extraOptions.setVisibility(View.INVISIBLE);

                }catch(Exception w){
                    FirebaseCrashlytics.getInstance().recordException(w);
                    gameExitCrash();
                }

            }
        });

        rollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rollTheDice();
                    dieNumber.setText(String.valueOf(diceRollValue));
                    dieResults.setVisibility(View.VISIBLE);
                    extraOptions.setVisibility(View.INVISIBLE);

                }catch(Exception w){
                    FirebaseCrashlytics.getInstance().recordException(w);
                    gameExitCrash();
                }
            }
        });

        specialControlsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraOptions.setVisibility(View.VISIBLE);
            }
        });

        extraOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraOptions.setVisibility(View.INVISIBLE);
            }
        });

        shuffleDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleDeck();
                dieResults.setVisibility(View.INVISIBLE);
                extraOptions.setVisibility(View.INVISIBLE);
            }
        });

        shuffleHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleHand();
                dieResults.setVisibility(View.INVISIBLE);
                extraOptions.setVisibility(View.INVISIBLE);
            }
        });

        shuffleEDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleExtradeck();
                dieResults.setVisibility(View.INVISIBLE);
                extraOptions.setVisibility(View.INVISIBLE);
            }
        });

        leaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        removeCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCard != null) {

                    Bitmap counters = Bitmap.createBitmap((int) width / 7, (int) ((int) height / heightToDivide), Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(counters);

                    final int tempIn = fieldContents.indexOf(selectedCard);
                    fieldCounters.set(tempIn, fieldCounters.get(tempIn) - 1);

                    if(fieldCounters.get(tempIn) > 0) {

                        Paint paint = new Paint();
                        paint.setDither(true);
                        paint.setAntiAlias(true);
                        paint.setFilterBitmap(true);
                        paint.setTextSize(48.0f);
                        paint.setColor(Color.GREEN);
                        canvas.drawText(fieldCounters.get(tempIn) + "", (float) (width / 60), (float) (height / (1.05f * heightToDivide)), paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawBitmap(counters, 0, 0, paint);

                        Drawable overlay = new BitmapDrawable(getResources(), counters);

                        selectedCard.setForeground(overlay);

                        final int amount = fieldCounters.get(tempIn);

                    }else{

                        selectedCard.setForeground(null);
                        fieldCounters.set(tempIn, 0);
                    }

                }
            }
        });

        addCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCard != null) {

                    Bitmap counters = Bitmap.createBitmap((int) width / 7, (int) ((int) height / heightToDivide), Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(counters);

                    final int tempIn = fieldContents.indexOf(selectedCard);
                    fieldCounters.set(tempIn, fieldCounters.get(tempIn) + 1);

                    Paint paint = new Paint();
                    paint.setDither(true);
                    paint.setAntiAlias(true);
                    paint.setFilterBitmap(true);
                    paint.setTextSize(48.0f);
                    paint.setColor(Color.GREEN);
                    canvas.drawText(fieldCounters.get(tempIn) + "", (float) (width / 60), (float) (height/(1.05f * heightToDivide)), paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(counters, 0, 0, paint);

                    Drawable overlay = new BitmapDrawable(getResources(), counters);

                    selectedCard.setForeground(overlay);

                    final int amount = fieldCounters.get(tempIn);

                }
            }
        });

        gameField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        opponentHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        yourExtraDeckContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        yourDeckContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearForDrag();
                showHand();
            }
        });

        yourBanishContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        yourGraveContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        opponentBanishContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        opponentExtraDeckContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        opponentGraveContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        opponentExtraDeckScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForDrag();
                showHand();
            }
        });

        opponentBanish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                opponentBanishContents.setVisibility(View.VISIBLE);
                opponentBanishScroll.setVisibility(View.VISIBLE);
                showHand();

                return false;
            }
        });

        opponentGrave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                opponentGraveContents.setVisibility(View.VISIBLE);
                opponentGraveScroll.setVisibility(View.VISIBLE);
                showHand();

                return false;
            }
        });

        opponentExtraDeck.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                opponentExtraDeckContents.setVisibility(View.VISIBLE);
                opponentExtraDeckScroll.setVisibility(View.VISIBLE);
                showHand();

                return false;
            }
        });

        yourExtraDeck.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                yourExtraDeckContents.setVisibility(View.VISIBLE);
                extraDeckScroll.setVisibility(View.VISIBLE);
                showHand();

                return false;
            }
        });

        yourBanish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                yourBanishContents.setVisibility(View.VISIBLE);
                banishScroll.setVisibility(View.VISIBLE);
                showHand();

                return false;
            }
        });

        yourGrave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                graveScroll.setVisibility(View.VISIBLE);
                showHand();

                return false;
            }
        });

        yourDeck.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                yourDeckContents.setVisibility(View.VISIBLE);
                deckScroll.setVisibility(View.VISIBLE);
                showHand();

                return false;
            }
        });

        viewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (selectedCard == null)
                        return;


                    Drawable imageTemp = selectedCard.getDrawable().getConstantState().newDrawable().mutate();
                    imageTemp.setColorFilter(null);

                    if (selectedCard.getForeground() != null) {

                        previewCard.setImageDrawable(imageTemp);

                        for (int i = 0; i < fieldContentsOpponent.size(); i++) {

                            if (fieldContentsOpponent.get(i).equals(selectedCard)) {

                                previewCard.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
                                break;

                            }


                        }

                        if(previewCard.getDrawable().equals(imageTemp)) {
                            new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(fieldNames.get(fieldContents.indexOf(selectedCard))) + ".jpg");
                        }else
                            previewCard.setVisibility(View.VISIBLE);

                        findViewById(R.id.messagebutton).setVisibility(View.INVISIBLE);

                    } else {

                        findViewById(R.id.messagebutton).setVisibility(View.INVISIBLE);

                        switch(cardLocaion){

                            case HAND:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(handNames.get(handContents.indexOf(selectedCard))) + ".jpg");
                                break;
                            case FIELD:
                                if(!fieldNames.get(fieldContents.indexOf(selectedCard)).equals("TOKEN"))
                                    new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(fieldNames.get(fieldContents.indexOf(selectedCard))) + ".jpg");
                                else {
                                    previewCard.setImageDrawable(imageTemp);
                                    previewCard.setVisibility(View.VISIBLE);
                                }
                                break;
                            case GRAVE:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(graveyardNames.get(graveContents.indexOf(selectedCard))) + ".jpg");
                                break;
                            case BANISH:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(banishNames.get(banishContents.indexOf(selectedCard))) + ".jpg");
                                break;
                            case EDECK:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(extraDeckNames.get(extraDeckContents.indexOf(selectedCard))) + ".jpg");
                                break;
                            case 10:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(banishNamesOpponent.get(banishContentsOpponent.indexOf(selectedCard))) + ".jpg");
                                break;
                            case 11:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(graveyardNamesOpponent.get(graveContentsOpponent.indexOf(selectedCard))) + ".jpg");
                                break;
                            case 13:
                                if(!fieldNamesOpponent.get(fieldContentsOpponent.indexOf(selectedCard)).equals("TOKEN"))
                                    new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(fieldNamesOpponent.get(fieldContentsOpponent.indexOf(selectedCard))) + ".jpg");
                                else {
                                    previewCard.setImageDrawable(imageTemp);
                                    previewCard.setVisibility(View.VISIBLE);
                                }
                                break;
                            case DECK:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(deckNames.get(deckContents.indexOf(selectedCard))) + ".jpg");
                                break;
                            case 14:
                                new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(extraDeckNamesOpponent.get(extraDeckContentsOpponent.indexOf(selectedCard))) + ".jpg");
                                break;

                        }

                        //previewCard.setImageDrawable(imageTemp);
                        //previewCard.setVisibility(View.VISIBLE);

                    }

                }catch (Exception e){
                    FirebaseCrashlytics.getInstance().recordException(e);
                    //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();

                }

            }
        });



        viewAttached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attachRevealSelect = null;
                attachName = "";
                attachedShow.removeAllViews();
                ArrayList<String> attachedNames = new ArrayList<String>();
                int sepIt = 0;

                showHand();

                if(fieldContents.contains(selectedCard)) {

                    for (int i = 0; i < fieldContents.size(); i++) {

                        if (!fieldContents.get(i).equals(selectedCard) && isViewOverlapping(fieldContents.get(i), selectedCard)) {

                            attachedNames.add(fieldNames.get(i));
                            ImageView showCardO = new ImageView(con);
                            showCardO.setImageDrawable(fieldContents.get(i).getDrawable().getConstantState().newDrawable().mutate());
                            attachedShow.addView(showCardO);
                            GridLayout.LayoutParams params = (GridLayout.LayoutParams) showCardO.getLayoutParams();
                            params.width = (int) (width / 7);
                            params.height = (int) (height / heightToDivide);
                            showCardO.setLayoutParams(params);
                            final int in = sepIt;
                            showCardO.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (attachRevealSelect != null)
                                        attachRevealSelect.setColorFilter(null);

                                    attachRevealSelect = (ImageView) view;
                                    attachRevealSelect.setColorFilter(Color.argb(60, 0, 255, 0));
                                    attachName = attachedNames.get(in);
                                    attachControls.setVisibility(View.VISIBLE);
                                }
                            });

                            sepIt++;

                        }

                    }

                    findViewById(R.id.toGraveBAD).setVisibility(View.VISIBLE);

                }else{

                    for (int i = 0; i < fieldContentsOpponent.size(); i++) {

                        if (!fieldContentsOpponent.get(i).equals(selectedCard) && isViewOverlapping(fieldContentsOpponent.get(i), selectedCard)) {

                            attachedNames.add(fieldNamesOpponent.get(i));
                            ImageView showCardO = new ImageView(con);
                            showCardO.setImageDrawable(fieldContentsOpponent.get(i).getDrawable().getConstantState().newDrawable().mutate());
                            attachedShow.addView(showCardO);
                            GridLayout.LayoutParams params = (GridLayout.LayoutParams) showCardO.getLayoutParams();
                            params.width = (int) (width / 7);
                            params.height = (int) (height / heightToDivide);
                            showCardO.setLayoutParams(params);
                            final int in = sepIt;
                            showCardO.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (attachRevealSelect != null)
                                        attachRevealSelect.setColorFilter(null);

                                    attachRevealSelect = (ImageView) view;
                                    attachRevealSelect.setColorFilter(Color.argb(60, 0, 255, 0));
                                    attachName = attachedNames.get(in);
                                    attachControls.setVisibility(View.VISIBLE);
                                }
                            });

                            sepIt++;

                        }

                    }

                    findViewById(R.id.toGraveBAD).setVisibility(View.INVISIBLE);

                }

                showAttached.setVisibility(View.VISIBLE);

            }
        });

        attackDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attacking = true;
                clearForDrag();

            }
        });

        toGraveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int indexOfCard;

                if(cardLocaion == BANISH) {

                    indexOfCard = banishContents.indexOf(selectedCard);
                    String name5 = banishNames.get(indexOfCard);

                    banishContents.remove(indexOfCard);
                    graveContents.add(selectedCard);
                    yourBanishContents.removeView(selectedCard);
                    yourGraveContents.addView(selectedCard);
                    banishNames.remove(indexOfCard);
                    graveyardNames.add(name5);
                    if (banishContents.size() > 0) {
                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable().mutate());

                    }else
                        yourBanish.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == DECK) {

                    indexOfCard = deckContents.indexOf(selectedCard);
                    String name3 = deckNames.get(indexOfCard);

                    deckContents.remove(indexOfCard);
                    graveContents.add(selectedCard);
                    yourDeckContents.removeView(selectedCard);
                    yourGraveContents.addView(selectedCard);
                    deckNames.remove(indexOfCard);
                    graveyardNames.add(name3);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;


                }else if(cardLocaion == HAND) {

                    indexOfCard = handContents.indexOf(selectedCard);
                    String name = handNames.get(indexOfCard);

                    handContents.remove(indexOfCard);
                    graveContents.add(selectedCard);
                    yourHand.removeView(selectedCard);

                    yourGraveContents.addView(selectedCard);
                    handNames.remove(indexOfCard);
                    graveyardNames.add(name);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(300);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;


                }else if(cardLocaion == EDECK) {

                    indexOfCard = extraDeckContents.indexOf(selectedCard);
                    String name6 = extraDeckNames.get(indexOfCard);

                    extraDeckContents.remove(indexOfCard);
                    graveContents.add(selectedCard);
                    yourExtraDeckContents.removeView(selectedCard);
                    yourGraveContents.addView(selectedCard);
                    extraDeckNames.remove(indexOfCard);
                    graveyardNames.add(name6);

                    if (extraDeckContents.size() > 0)
                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourExtraDeck.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = 0;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }

                cardLocaion = GRAVE;
                clearAllOptions();

                Drawable tempD = selectedCard.getDrawable().getConstantState().newDrawable().mutate();
                if ((selectedCard != null && selectedCard.equals(selectedCard)))
                    tempD.setColorFilter(Color.argb(60, 0, 255, 0), PorterDuff.Mode.OVERLAY);
                else if (opSelectedCard != null && opSelectedCard.equals(selectedCard))
                    tempD.setColorFilter(Color.argb(60, 255, 0, 0), PorterDuff.Mode.OVERLAY);
                yourGrave.setBackground(tempD);
                yourGrave.setImageDrawable(null);
                yourGrave.setAlpha(1.0f);
                findViewById(R.id.gravePadding).setVisibility(View.VISIBLE);


            }
        });



        toBanishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int indexOfCard;

                if(cardLocaion == GRAVE) {

                    indexOfCard = graveContents.indexOf(selectedCard);
                    String name5 = graveyardNames.get(indexOfCard);

                    graveContents.remove(indexOfCard);
                    banishContents.add(selectedCard);
                    yourGraveContents.removeView(selectedCard);
                    yourBanishContents.addView(selectedCard);
                    graveyardNames.remove(indexOfCard);
                    banishNames.add(name5);
                    if (graveContents.size() > 0)
                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourGrave.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;


                }else if(cardLocaion == DECK) {

                    indexOfCard = deckContents.indexOf(selectedCard);
                    String name3 = deckNames.get(indexOfCard);

                    deckContents.remove(indexOfCard);
                    banishContents.add(selectedCard);
                    yourDeckContents.removeView(selectedCard);
                    yourBanishContents.addView(selectedCard);
                    deckNames.remove(indexOfCard);
                    banishNames.add(name3);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;


                }else if(cardLocaion == HAND) {

                    indexOfCard = handContents.indexOf(selectedCard);
                    String name = handNames.get(indexOfCard);

                    handContents.remove(indexOfCard);
                    banishContents.add(selectedCard);
                    yourHand.removeView(selectedCard);
                    //TODO check height isn't crashing
                    yourBanishContents.addView(selectedCard);
                    handNames.remove(indexOfCard);
                    banishNames.add(name);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(300);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;


                }else if(cardLocaion == EDECK) {

                    indexOfCard = extraDeckContents.indexOf(selectedCard);
                    String name6 = extraDeckNames.get(indexOfCard);

                    extraDeckContents.remove(indexOfCard);
                    banishContents.add(selectedCard);
                    yourExtraDeckContents.removeView(selectedCard);
                    yourBanishContents.addView(selectedCard);
                    extraDeckNames.remove(indexOfCard);
                    banishNames.add(name6);

                    if (extraDeckContents.size() > 0)
                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourExtraDeck.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = 0;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }

                cardLocaion = BANISH;
                clearAllOptions();

                yourBanish.setImageDrawable(selectedCard.getDrawable());
                yourBanish.setAlpha(1.0f);


            }
        });

        toDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int indexOfCard;

                if(cardLocaion == BANISH) {

                    indexOfCard = banishContents.indexOf(selectedCard);
                    String name5 = banishNames.get(indexOfCard);

                    banishContents.remove(indexOfCard);
                    deckContents.add(selectedCard);
                    yourBanishContents.removeView(selectedCard);
                    yourDeckContents.addView(selectedCard);
                    banishNames.remove(indexOfCard);
                    deckNames.add(name5);
                    if (banishContents.size() > 0)
                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourBanish.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == GRAVE) {

                    indexOfCard = graveContents.indexOf(selectedCard);
                    String name4 = graveyardNames.get(indexOfCard);

                    graveContents.remove(indexOfCard);
                    deckContents.add(selectedCard);
                    yourGraveContents.removeView(selectedCard);
                    yourDeckContents.addView(selectedCard);
                    graveyardNames.remove(indexOfCard);
                    deckNames.add(name4);
                    if(graveContents.size() > 0)
                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourGrave.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == HAND) {

                    indexOfCard = handContents.indexOf(selectedCard);
                    String name = handNames.get(indexOfCard);

                    handContents.remove(indexOfCard);
                    deckContents.add(selectedCard);
                    yourHand.removeView(selectedCard);

                    yourDeckContents.addView(selectedCard);
                    handNames.remove(indexOfCard);
                    deckNames.add(name);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(300);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == EDECK) {

                    indexOfCard = extraDeckContents.indexOf(selectedCard);
                    String name6 = extraDeckNames.get(indexOfCard);

                    extraDeckContents.remove(indexOfCard);
                    deckContents.add(selectedCard);
                    yourExtraDeckContents.removeView(selectedCard);
                    yourDeckContents.addView(selectedCard);
                    extraDeckNames.remove(indexOfCard);
                    deckNames.add(name6);

                    if (extraDeckContents.size() > 0)
                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourExtraDeck.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = 0;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }


                cardLocaion = DECK;
                clearAllOptions();

            }
        });

        toHandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int indexOfCard;

                if(cardLocaion == BANISH) {

                    indexOfCard = banishContents.indexOf(selectedCard);
                    String name5 = banishNames.get(indexOfCard);

                    banishContents.remove(indexOfCard);
                    handContents.add(selectedCard);
                    yourBanishContents.removeView(selectedCard);
                    yourHand.addView(selectedCard);
                    banishNames.remove(indexOfCard);
                    handNames.add(name5);
                    if (banishContents.size() > 0)
                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourBanish.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == DECK) {

                    indexOfCard = deckContents.indexOf(selectedCard);
                    String name3 = deckNames.get(indexOfCard);

                    deckContents.remove(indexOfCard);
                    handContents.add(selectedCard);
                    yourDeckContents.removeView(selectedCard);
                    yourHand.addView(selectedCard);
                    deckNames.remove(indexOfCard);
                    handNames.add(name3);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == GRAVE) {

                    indexOfCard = graveContents.indexOf(selectedCard);
                    String name = graveyardNames.get(indexOfCard);

                    graveContents.remove(indexOfCard);
                    handContents.add(selectedCard);
                    yourGraveContents.removeView(selectedCard);

                    yourHand.addView(selectedCard);
                    graveyardNames.remove(indexOfCard);
                    handNames.add(name);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(300);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == EDECK) {

                    indexOfCard = extraDeckContents.indexOf(selectedCard);
                    String name6 = extraDeckNames.get(indexOfCard);

                    extraDeckContents.remove(indexOfCard);
                    handContents.add(selectedCard);
                    yourExtraDeckContents.removeView(selectedCard);
                    yourHand.addView(selectedCard);
                    extraDeckNames.remove(indexOfCard);
                    handNames.add(name6);

                    if (extraDeckContents.size() > 0)
                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourExtraDeck.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = 0;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }

                cardLocaion = HAND;
                clearAllOptions();

            }
        });

        toEdeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int indexOfCard;

                if(cardLocaion == BANISH) {

                    indexOfCard = banishContents.indexOf(selectedCard);
                    String name5 = banishNames.get(indexOfCard);

                    banishContents.remove(indexOfCard);
                    extraDeckContents.add(selectedCard);
                    yourBanishContents.removeView(selectedCard);
                    yourExtraDeckContents.addView(selectedCard);
                    banishNames.remove(indexOfCard);
                    extraDeckNames.add(name5);
                    if (banishContents.size() > 0)
                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourBanish.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(0, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == DECK) {

                    indexOfCard = deckContents.indexOf(selectedCard);
                    String name3 = deckNames.get(indexOfCard);

                    deckContents.remove(indexOfCard);
                    extraDeckContents.add(selectedCard);
                    yourDeckContents.removeView(selectedCard);
                    yourExtraDeckContents.addView(selectedCard);
                    deckNames.remove(indexOfCard);
                    extraDeckNames.add(name3);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(0, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == HAND) {

                    indexOfCard = handContents.indexOf(selectedCard);
                    String name = handNames.get(indexOfCard);

                    handContents.remove(indexOfCard);
                    extraDeckContents.add(selectedCard);
                    yourHand.removeView(selectedCard);

                    yourExtraDeckContents.addView(selectedCard);
                    handNames.remove(indexOfCard);
                    extraDeckNames.add(name);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(0, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(300);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }else if(cardLocaion == GRAVE) {

                    indexOfCard = graveContents.indexOf(selectedCard);
                    String name6 = graveyardNames.get(indexOfCard);

                    graveContents.remove(indexOfCard);
                    graveContents.add(selectedCard);
                    yourGraveContents.removeView(selectedCard);
                    yourGraveContents.addView(selectedCard);
                    graveyardNames.remove(indexOfCard);
                    graveyardNames.add(name6);

                    if (graveContents.size() > 0)
                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                    else
                        yourGrave.setAlpha(0.1f);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(0, aniCover.getMeasuredHeight() - ((1 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    final int indexToSend = indexOfCard;
                    final ImageView compCard = selectedCard;



                }

                cardLocaion = EDECK;
                clearAllOptions();

                yourExtraDeck.setImageDrawable(null);
                yourExtraDeck.setBackground(selectedCard.getDrawable().getConstantState().newDrawable().mutate());
                yourExtraDeck.setAlpha(1.0f);

            }
        });

        aniCover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(attacking){

                    int xt = (int) motionEvent.getX();
                    int yt = (int) motionEvent.getY();
                    int xo = ((RelativeLayout.LayoutParams) selectedCard.getLayoutParams()).leftMargin;
                    int yo = ((RelativeLayout.LayoutParams) selectedCard.getLayoutParams()).topMargin + opponentHand.getMeasuredHeight();
                    //yo doesn't work when sent recalc for socket communication

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(selectedCard.getDrawable());
                    tempAni.setForeground(selectedCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = xo;
                    ncardPositionParameters.topMargin = yo;
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(xt, yt);
                    path.lineTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(800);
                    selectedCard.setVisibility(View.INVISIBLE);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            selectedCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                    attacking = false;

                    final double fxt = (double) xt / (double) aniCover.getMeasuredWidth();
                    final double fyt = (double) yt / (double) aniCover.getMeasuredHeight();
                    final double fxo = (double) xo / (double) aniCover.getMeasuredWidth();
                    final double fyo = (double) yo / (double) aniCover.getMeasuredHeight();
                    final int ind = gameField.indexOfChild(selectedCard);

                    return true;

                }

                return false;
            }
        });

        surrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        previewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewCard.setVisibility(View.INVISIBLE);
                findViewById(R.id.messagebutton).setVisibility(View.VISIBLE);
            }
        });

        drawFromMainDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new drawHandler().execute();

            }
        });

        millFromTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(deckContents.size() > 0 && deckNames.size() > 0) {

                    ImageView cardToBanish = deckContents.get(0);
                    String nameToBanish = deckNames.get(0);

                    graveContents.add(cardToBanish);
                    graveyardNames.add(nameToBanish);
                    yourDeckContents.removeViewAt(0);
                    yourGraveContents.addView(cardToBanish);
                    deckContents.remove(0);
                    deckNames.remove(0);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(cardToBanish.getDrawable());
                    tempAni.setForeground(cardToBanish.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            try {
                                aniCover.removeView(tempAni);
                                yourGrave.setBackground(cardToBanish.getDrawable().getConstantState().newDrawable().mutate());
                                yourGrave.setImageDrawable(null);
                                yourGrave.setAlpha(1.0f);
                            }catch(Exception e){
                                if(graveContents.size() > 0){
                                    yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable().mutate());
                                    yourGrave.setImageDrawable(null);
                                    yourGrave.setAlpha(1.0f);
                                }else{
                                    yourGrave.setAlpha(0.1f);
                                }//TODO catch mill error
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                }else{

                    AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                    prompt.setTitle("Your deck is empty!");
                    prompt.setNegativeButton("OK", null);
                    prompt.show();

                }

            }
        });

        ((Switch) findViewById(R.id.canShowButtons)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useNonEssentialButtons = !b;
                toGraveButton.setVisibility(View.GONE);
                toDeckButton.setVisibility(View.GONE);
                toEdeckButton.setVisibility(View.GONE);
                toBanishButton.setVisibility(View.GONE);
                toHandButton.setVisibility(View.GONE);
            }
        });

        banishFromMainDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(deckContents.size() > 0 && deckNames.size() > 0) {

                    ImageView cardToBanish = deckContents.get(0);
                    String nameToBanish = deckNames.get(0);

                    banishContents.add(cardToBanish);
                    banishNames.add(nameToBanish);
                    yourDeckContents.removeViewAt(0);
                    yourBanishContents.addView(cardToBanish);
                    deckContents.remove(0);
                    deckNames.remove(0);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(cardToBanish.getDrawable());
                    tempAni.setForeground(cardToBanish.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            cardToBanish.setVisibility(View.VISIBLE);
                            yourBanish.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
                            yourBanish.setAlpha(1.0f);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                }else{

                    AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                    prompt.setTitle("Your deck is empty!");
                    prompt.setNegativeButton("OK", null);
                    prompt.show();

                }

            }
        });

        banishFromExtraDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(extraDeckContents.size() > 0 && extraDeckNames.size() > 0) {

                    ImageView cardToBanish = extraDeckContents.get(0);
                    String nameToBanish = extraDeckNames.get(0);

                    banishContents.add(cardToBanish);
                    banishNames.add(nameToBanish);
                    yourExtraDeckContents.removeViewAt(0);
                    yourBanishContents.addView(cardToBanish);
                    extraDeckContents.remove(0);
                    extraDeckNames.remove(0);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(cardToBanish.getDrawable());
                    tempAni.setForeground(cardToBanish.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = 0;
                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            cardToBanish.setVisibility(View.VISIBLE);
                            yourBanish.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
                            yourBanish.setAlpha(1.0f);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                }else{

                    AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                    prompt.setTitle("Your extra deck is empty!");
                    prompt.setNegativeButton("OK", null);
                    prompt.show();

                }

            }
        });

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                gameReset();
                extraOptions.setVisibility(View.GONE);

            }
        });

        sideCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                gameSide();
                extraOptions.setVisibility(View.GONE);

            }
        });

        confirmSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    sideConfirm.setVisibility(View.GONE);
                    gameSide();
                }catch (Exception e){
                    FirebaseCrashlytics.getInstance().recordException(e);
                    //((TextView) findViewById(R.id.textView3)).setText("error");
                    gameExitCrash();
                }

            }
        });

        confirmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    resetConfirm.setVisibility(View.GONE);
                    gameReset();
                }catch (Exception e){
                    FirebaseCrashlytics.getInstance().recordException(e);
                    //((TextView) findViewById(R.id.textView3)).setText("error");
                    gameExitCrash();

                }

            }
        });

        resetConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetConfirm.setVisibility(View.GONE);

            }
        });

        findViewById(R.id.coverforgame).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        sideConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sideConfirm.setVisibility(View.GONE);

            }
        });

        yourExtraDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExtraDeckOptions();
            }
        });

        yourDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeckOptions();
            }
        });

        yourExtraDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExtraDeckOptions();
            }
        });

        findViewById(R.id.attachPadding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearForDrag();
                showHand();

            }
        });

        findViewById(R.id.msgpadding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.messagebutton).setVisibility(View.VISIBLE);
                findViewById(R.id.chatcon).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageView4).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.msglist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.messagebutton).setVisibility(View.VISIBLE);
                findViewById(R.id.chatcon).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageView4).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.deckInOpt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        tokenCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData.Item item;
                String[] mimeTypes;
                ClipData data;
                View.DragShadowBuilder dragshadow;

                if (autoSet.isChecked())
                    tokenCard.setForeground(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());

                item = new ClipData.Item((CharSequence) view.getTag());
                mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                data = new ClipData((CharSequence) view.getTag(), mimeTypes, item);
                dragshadow = new View.DragShadowBuilder(view);

                view.startDrag(data, dragshadow, view, 0);
                clearForDrag();

                return true;
            }
        });

        cardDefenseOrAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedCard == null)
                    return;

                final int cardToTurn = fieldContents.indexOf(selectedCard);

                if(selectedCard.getRotation() == 90f) {

                    selectedCard.setRotation(0f);

                }else {

                    selectedCard.setRotation(90f);

                }

                clearAllOptions();
                showCardOptions();

            }
        });

        cardFaceUpOrDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedCard == null)
                    return;

                final int cardToFlip = fieldContents.indexOf(selectedCard);

                if(selectedCard.getForeground() == null || fieldCounters.get(fieldContents.indexOf(selectedCard)) > 0) {

                    fieldCounters.set(fieldContents.indexOf(selectedCard), 0);
                    selectedCard.setForeground(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
                    selectedCard.setColorFilter(null);
                    selectedCard.getForeground().setColorFilter(Color.argb(125, 0, 255, 0), PorterDuff.Mode.OVERLAY);

                }else {
                    selectedCard.setForeground(null);
                    selectedCard.setColorFilter(Color.argb(125, 0, 255, 0));

                }

                clearAllOptions();
                showCardOptions();

            }

        });

        yourExtraDeck.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                try {

                    int userAction = dragEvent.getAction();

                    switch (userAction) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                                return true;
                            else
                                return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;

                        case DragEvent.ACTION_DROP:

                            //ClipData.Item draggedCardData = dragEvent.getClipData().getItemAt(0);
                            //android.widget.RelativeLayout.LayoutParams cardPositionParameters;
                            ImageView draggedCard = (ImageView) dragEvent.getLocalState();
                            int origix = -1;
                            int origiy = -1;
                            int indexOfCard = 0;

                            switch (cardLocaion) {

                                case EDECK:
                                    indexOfCard = extraDeckContents.indexOf(draggedCard);
                                    String name6 = extraDeckNames.get(indexOfCard);

                                    extraDeckContents.remove(indexOfCard);
                                    extraDeckContents.add(draggedCard);
                                    yourExtraDeckContents.removeView(draggedCard);
                                    yourExtraDeckContents.addView(draggedCard);
                                    extraDeckNames.remove(indexOfCard);
                                    extraDeckNames.add(name6);

                                    if(extraDeckContents.size() > 0)
                                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourExtraDeck.setAlpha(0.1f);

                                    break;

                                case BANISH:
                                    indexOfCard = banishContents.indexOf(draggedCard);
                                    String name5 = banishNames.get(indexOfCard);

                                    banishContents.remove(indexOfCard);
                                    extraDeckContents.add(draggedCard);
                                    yourBanishContents.removeView(draggedCard);
                                    yourExtraDeckContents.addView(draggedCard);
                                    banishNames.remove(indexOfCard);
                                    extraDeckNames.add(name5);
                                    if(banishContents.size() > 0)
                                        yourBanish.setBackground(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourBanish.setAlpha(0.1f);

                                    break;

                                case GRAVE:
                                    indexOfCard = graveContents.indexOf(draggedCard);
                                    String name4 = graveyardNames.get(indexOfCard);

                                    graveContents.remove(indexOfCard);
                                    extraDeckContents.add(draggedCard);
                                    yourGraveContents.removeView(draggedCard);
                                    yourExtraDeckContents.addView(draggedCard);
                                    graveyardNames.remove(indexOfCard);
                                    extraDeckNames.add(name4);
                                    if(graveContents.size() > 0)
                                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourGrave.setAlpha(0.1f);

                                    break;

                                case DECK:
                                    indexOfCard = deckContents.indexOf(draggedCard);
                                    String name3 = deckNames.get(indexOfCard);

                                    deckContents.remove(indexOfCard);
                                    extraDeckContents.add(draggedCard);
                                    yourDeckContents.removeView(draggedCard);
                                    yourExtraDeckContents.addView(draggedCard);
                                    deckNames.remove(indexOfCard);
                                    extraDeckNames.add(name3);

                                    break;

                                case HAND:

                                    indexOfCard = handContents.indexOf(draggedCard);
                                    String name = handNames.get(indexOfCard);

                                    handContents.remove(indexOfCard);
                                    extraDeckContents.add(draggedCard);
                                    yourHand.removeView(draggedCard);

                                    yourExtraDeckContents.addView(draggedCard);
                                    handNames.remove(indexOfCard);
                                    extraDeckNames.add(name);

                                    break;

                                case FIELD:

                                    indexOfCard = fieldContents.indexOf(draggedCard);
                                    String name2 = fieldNames.get(indexOfCard);

                                    if(!name2.equals("TOKEN")) {

                                        origix = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).leftMargin;
                                        origiy = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).topMargin;

                                        fieldContents.remove(indexOfCard);
                                        extraDeckContents.add(draggedCard);
                                        gameField.removeView(draggedCard);
                                        yourExtraDeckContents.addView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        extraDeckNames.add(name2);
                                        fieldCounters.remove(indexOfCard);

                                    }else{

                                        fieldContents.remove(indexOfCard);
                                        gameField.removeView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        fieldCounters.remove(indexOfCard);

                                    }

                                    break;

                            }

                            if(!isToken) {

                                //draggedCard.setLayoutParams(null);
                                GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                                newParams.width = (int) (width / 7.0);
                                newParams.height = (int) (height / heightToDivide);
                                draggedCard.setLayoutParams(newParams);
                                draggedCard.setForeground(null);
                                draggedCard.setRotation(0);
                                draggedCard.setVisibility(View.VISIBLE);
                                yourExtraDeck.setImageDrawable(null);
                                yourExtraDeck.setBackground(draggedCard.getDrawable().getConstantState().newDrawable().mutate());
                                yourExtraDeck.setAlpha(1.0f);

                                if(cardLocaion == FIELD) {

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setRotation(draggedCard.getRotation());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = origix;
                                    ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(0, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == GRAVE){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(0, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == DECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(0, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == HAND){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(0, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(300);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == BANISH){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3*ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(0, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else
                                    draggedCard.setVisibility(View.VISIBLE);

                            }

                            final int indexToSend = indexOfCard;
                            final ImageView compCard = draggedCard;

                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;

                        default:
                            break;

                    }

                }catch (Exception e){
                    FirebaseCrashlytics.getInstance().recordException(e);
                    //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();
                }

                return false;
            }
        });

        yourBanish.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                try {

                    int userAction = dragEvent.getAction();

                    switch (userAction) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                                return true;
                            else
                                return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;

                        case DragEvent.ACTION_DROP:

                            //ClipData.Item draggedCardData = dragEvent.getClipData().getItemAt(0);
                            //android.widget.RelativeLayout.LayoutParams cardPositionParameters;
                            ImageView draggedCard = (ImageView) dragEvent.getLocalState();
                            int origix = -1;
                            int origiy = -1;
                            int indexOfCard = 0;
                            boolean flipped = false;

                            switch (cardLocaion) {

                                case EDECK:
                                    indexOfCard = extraDeckContents.indexOf(draggedCard);
                                    String name6 = extraDeckNames.get(indexOfCard);

                                    extraDeckContents.remove(indexOfCard);
                                    banishContents.add(draggedCard);
                                    yourExtraDeckContents.removeView(draggedCard);
                                    yourBanishContents.addView(draggedCard);
                                    extraDeckNames.remove(indexOfCard);
                                    banishNames.add(name6);

                                    if(extraDeckContents.size() > 0)
                                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourExtraDeck.setAlpha(0.1f);

                                    break;

                                case BANISH:
                                    indexOfCard = banishContents.indexOf(draggedCard);
                                    String name5 = banishNames.get(indexOfCard);

                                    banishContents.remove(indexOfCard);
                                    banishContents.add(draggedCard);
                                    yourBanishContents.removeView(draggedCard);
                                    yourBanishContents.addView(draggedCard);
                                    banishNames.remove(indexOfCard);
                                    banishNames.add(name5);
                                    if(banishContents.size() > 0)
                                        yourBanish.setBackground(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourBanish.setAlpha(0.1f);

                                    break;

                                case GRAVE:
                                    indexOfCard = graveContents.indexOf(draggedCard);
                                    String name4 = graveyardNames.get(indexOfCard);

                                    graveContents.remove(indexOfCard);
                                    banishContents.add(draggedCard);
                                    yourGraveContents.removeView(draggedCard);
                                    yourBanishContents.addView(draggedCard);
                                    graveyardNames.remove(indexOfCard);
                                    banishNames.add(name4);
                                    if(graveContents.size() > 0)
                                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourGrave.setAlpha(0.1f);

                                    break;

                                case DECK:
                                    indexOfCard = deckContents.indexOf(draggedCard);
                                    String name3 = deckNames.get(indexOfCard);

                                    deckContents.remove(indexOfCard);
                                    banishContents.add(draggedCard);
                                    yourDeckContents.removeView(draggedCard);
                                    yourBanishContents.addView(draggedCard);
                                    deckNames.remove(indexOfCard);
                                    banishNames.add(name3);

                                    break;

                                case HAND:

                                    indexOfCard = handContents.indexOf(draggedCard);
                                    String name = handNames.get(indexOfCard);

                                    handContents.remove(indexOfCard);
                                    banishContents.add(draggedCard);
                                    yourHand.removeView(draggedCard);

                                    yourBanishContents.addView(draggedCard);
                                    handNames.remove(indexOfCard);
                                    banishNames.add(name);

                                    break;

                                case FIELD:

                                    indexOfCard = fieldContents.indexOf(draggedCard);
                                    String name2 = fieldNames.get(indexOfCard);

                                    if(draggedCard.getForeground() != null)
                                        flipped = true;

                                    if(!name2.equals("TOKEN")) {

                                        origix = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).leftMargin;
                                        origiy = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).topMargin;

                                        fieldContents.remove(indexOfCard);
                                        banishContents.add(draggedCard);
                                        gameField.removeView(draggedCard);
                                        yourBanishContents.addView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        banishNames.add(name2);
                                        fieldCounters.remove(indexOfCard);
                                        isToken = false;

                                    }else{

                                        fieldContents.remove(indexOfCard);
                                        gameField.removeView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        fieldCounters.remove(indexOfCard);
                                        isToken = true;

                                    }
                                    break;

                            }

                            if(!isToken) {

                                GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                                newParams.width = (int) (width / 7.0);
                                newParams.height = (int) (height / heightToDivide);
                                draggedCard.setLayoutParams(newParams);
                                draggedCard.setForeground(null);
                                draggedCard.setRotation(0);
                                draggedCard.setVisibility(View.INVISIBLE);
                                if ((selectedCard != null && selectedCard.equals(draggedCard)))
                                    draggedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                else if (opSelectedCard != null && opSelectedCard.equals(draggedCard))
                                    draggedCard.setColorFilter(Color.argb(60, 255, 0, 0));


                                //draggedCard.setRotation(0);
                                if(cardLocaion == FIELD) {

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setRotation(draggedCard.getRotation());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = origix;
                                    ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == DECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == EDECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = 0;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == HAND){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(300);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == GRAVE){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2*ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((3 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else
                                    draggedCard.setVisibility(View.VISIBLE);

                                yourBanish.setImageDrawable(draggedCard.getDrawable());
                                yourBanish.setAlpha(1.0f);

                            }

                            final int indexToSend = indexOfCard;
                            final ImageView compCard = draggedCard;

                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;

                        default:
                            break;

                    }

                }catch (Exception e){
                    FirebaseCrashlytics.getInstance().recordException(e);
                    // ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();
                }

                return false;
            }
        });

        yourGrave.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                try {

                    int userAction = dragEvent.getAction();

                    switch (userAction) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                                return true;
                            else
                                return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;

                        case DragEvent.ACTION_DROP:

                            //ClipData.Item draggedCardData = dragEvent.getClipData().getItemAt(0);
                            //android.widget.RelativeLayout.LayoutParams cardPositionParameters;
                            ImageView draggedCard = (ImageView) dragEvent.getLocalState();
                            int origix = -1;
                            int origiy = -1;
                            int indexOfCard = 0;

                            switch (cardLocaion) {

                                case EDECK:
                                    indexOfCard = extraDeckContents.indexOf(draggedCard);
                                    String name6 = extraDeckNames.get(indexOfCard);

                                    extraDeckContents.remove(indexOfCard);
                                    graveContents.add(draggedCard);
                                    yourExtraDeckContents.removeView(draggedCard);
                                    yourGraveContents.addView(draggedCard);
                                    extraDeckNames.remove(indexOfCard);
                                    graveyardNames.add(name6);

                                    if(extraDeckContents.size() > 0)
                                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourExtraDeck.setAlpha(0.1f);

                                    break;

                                case BANISH:
                                    indexOfCard = banishContents.indexOf(draggedCard);
                                    String name5 = banishNames.get(indexOfCard);

                                    banishContents.remove(indexOfCard);
                                    graveContents.add(draggedCard);
                                    yourBanishContents.removeView(draggedCard);
                                    yourGraveContents.addView(draggedCard);
                                    banishNames.remove(indexOfCard);
                                    graveyardNames.add(name5);
                                    if(banishContents.size() > 0)
                                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourBanish.setAlpha(0.1f);

                                    break;

                                case GRAVE:
                                    indexOfCard = graveContents.indexOf(draggedCard);
                                    String name4 = graveyardNames.get(indexOfCard);

                                    graveContents.remove(indexOfCard);
                                    graveContents.add(draggedCard);
                                    yourGraveContents.removeView(draggedCard);
                                    yourGraveContents.addView(draggedCard);
                                    graveyardNames.remove(indexOfCard);
                                    graveyardNames.add(name4);
                                    if(graveContents.size() > 0)
                                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourGrave.setAlpha(0.1f);

                                    break;

                                case DECK:
                                    indexOfCard = deckContents.indexOf(draggedCard);
                                    String name3 = deckNames.get(indexOfCard);

                                    deckContents.remove(indexOfCard);
                                    graveContents.add(draggedCard);
                                    yourDeckContents.removeView(draggedCard);
                                    yourGraveContents.addView(draggedCard);
                                    deckNames.remove(indexOfCard);
                                    graveyardNames.add(name3);

                                    break;

                                case HAND:

                                    indexOfCard = handContents.indexOf(draggedCard);
                                    String name = handNames.get(indexOfCard);

                                    handContents.remove(indexOfCard);
                                    graveContents.add(draggedCard);
                                    yourHand.removeView(draggedCard);

                                    yourGraveContents.addView(draggedCard);
                                    handNames.remove(indexOfCard);
                                    graveyardNames.add(name);

                                    break;

                                case FIELD:

                                    indexOfCard = fieldContents.indexOf(draggedCard);
                                    String name2 = fieldNames.get(indexOfCard);

                                    if(!name2.equals("TOKEN")) {

                                        origix = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).leftMargin;
                                        origiy = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).topMargin;

                                        fieldContents.remove(indexOfCard);
                                        graveContents.add(draggedCard);
                                        gameField.removeView(draggedCard);
                                        yourGraveContents.addView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        graveyardNames.add(name2);
                                        fieldCounters.remove(indexOfCard);
                                        isToken = false;

                                    }else{

                                        fieldContents.remove(indexOfCard);
                                        gameField.removeView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        fieldCounters.remove(indexOfCard);
                                        isToken = true;

                                    }
                                    break;

                            }

                            final int indexToSend = indexOfCard;

                            if(!isToken) {

                                //findViewById(R.id.gravePadding).setVisibility(View.GONE);

                                //draggedCard.setLayoutParams(null);
                                GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                                newParams.width = (int)(width/7.0);
                                newParams.height = (int)(height/heightToDivide);
                                draggedCard.setLayoutParams(newParams);
                                draggedCard.setForeground(null);
                                draggedCard.setRotation(0);
                                draggedCard.setVisibility(View.INVISIBLE);

                                yourGraveContents.setVisibility(View.VISIBLE);
                                Drawable tempD = draggedCard.getDrawable().getConstantState().newDrawable().mutate();
                                if ((selectedCard != null && selectedCard.equals(draggedCard)))
                                    tempD.setColorFilter(Color.argb(60, 0, 255, 0), PorterDuff.Mode.OVERLAY);
                                else if (opSelectedCard != null && opSelectedCard.equals(draggedCard))
                                    tempD.setColorFilter(Color.argb(60, 255, 0, 0), PorterDuff.Mode.OVERLAY);
                                yourGrave.setBackground(tempD);
                                yourGrave.setImageDrawable(null);
                                yourGrave.setAlpha(1.0f);
                                findViewById(R.id.gravePadding).setVisibility(View.VISIBLE);

                                if ((selectedCard != null && selectedCard.equals(draggedCard)))
                                    draggedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                else if (opSelectedCard != null && opSelectedCard.equals(draggedCard))
                                    draggedCard.setColorFilter(Color.argb(60, 255, 0, 0));

                                if(cardLocaion == FIELD) {

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setRotation(draggedCard.getRotation());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = origix;
                                    ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == DECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == EDECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = 0;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == HAND){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(300);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == BANISH){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3*ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else
                                    draggedCard.setVisibility(View.VISIBLE);

                            }

                            final ImageView compCard = draggedCard;


                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;

                        default:
                            break;

                    }

                }catch (Exception e){

                    //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();

                }

                return false;
            }
        });

        yourDeck.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                try {

                    int userAction = dragEvent.getAction();

                    switch (userAction) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                                return true;
                            else
                                return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;

                        case DragEvent.ACTION_DROP:

                            //ClipData.Item draggedCardData = dragEvent.getClipData().getItemAt(0);
                            //android.widget.RelativeLayout.LayoutParams cardPositionParameters;
                            ImageView draggedCard = (ImageView) dragEvent.getLocalState();
                            int origix = -1;
                            int origiy = -1;
                            int indexOfCard = 0;

                            switch (cardLocaion) {

                                case EDECK:
                                    indexOfCard = extraDeckContents.indexOf(draggedCard);
                                    String name6 = extraDeckNames.get(indexOfCard);

                                    extraDeckContents.remove(indexOfCard);
                                    deckContents.add(draggedCard);
                                    yourExtraDeckContents.removeView(draggedCard);
                                    yourDeckContents.addView(draggedCard);
                                    extraDeckNames.remove(indexOfCard);
                                    deckNames.add(name6);

                                    if(extraDeckContents.size() > 0)
                                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourExtraDeck.setAlpha(0.1f);

                                    break;

                                case BANISH:
                                    indexOfCard = banishContents.indexOf(draggedCard);
                                    String name5 = banishNames.get(indexOfCard);

                                    banishContents.remove(indexOfCard);
                                    deckContents.add(draggedCard);
                                    yourBanishContents.removeView(draggedCard);
                                    yourDeckContents.addView(draggedCard);
                                    banishNames.remove(indexOfCard);
                                    deckNames.add(name5);
                                    if(banishContents.size() > 0)
                                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourBanish.setAlpha(0.1f);

                                    break;

                                case GRAVE:
                                    indexOfCard = graveContents.indexOf(draggedCard);
                                    String name4 = graveyardNames.get(indexOfCard);

                                    graveContents.remove(indexOfCard);
                                    deckContents.add(draggedCard);
                                    yourGraveContents.removeView(draggedCard);
                                    yourDeckContents.addView(draggedCard);
                                    graveyardNames.remove(indexOfCard);
                                    deckNames.add(name4);
                                    if(graveContents.size() > 0)
                                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourGrave.setAlpha(0.1f);

                                    break;

                                case DECK:
                                    indexOfCard = deckContents.indexOf(draggedCard);
                                    String name3 = deckNames.get(indexOfCard);

                                    deckContents.remove(indexOfCard);
                                    deckContents.add(draggedCard);
                                    yourDeckContents.removeView(draggedCard);
                                    yourDeckContents.addView(draggedCard);
                                    deckNames.remove(indexOfCard);
                                    deckNames.add(name3);

                                    break;

                                case HAND:

                                    indexOfCard = handContents.indexOf(draggedCard);
                                    String name = handNames.get(indexOfCard);

                                    handContents.remove(indexOfCard);
                                    deckContents.add(draggedCard);
                                    yourHand.removeView(draggedCard);

                                    yourDeckContents.addView(draggedCard);
                                    handNames.remove(indexOfCard);
                                    deckNames.add(name);
                                    break;



                                case FIELD:

                                    indexOfCard = fieldContents.indexOf(draggedCard);
                                    String name2 = fieldNames.get(indexOfCard);

                                    if(!name2.equals("TOKEN")) {

                                        origix = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).leftMargin;
                                        origiy = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).topMargin;

                                        fieldContents.remove(indexOfCard);
                                        deckContents.add(draggedCard);
                                        gameField.removeView(draggedCard);
                                        yourDeckContents.addView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        deckNames.add(name2);
                                        fieldCounters.remove(indexOfCard);
                                        isToken = false;

                                    }else{

                                        fieldContents.remove(indexOfCard);
                                        gameField.removeView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        fieldCounters.remove(indexOfCard);
                                        isToken = true;

                                    }

                                    break;

                            }

                            if(!isToken) {

                                //draggedCard.setLayoutParams(null);
                                GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                                newParams.width = (int) (width / 7.0);
                                newParams.height = (int) (height / heightToDivide);
                                draggedCard.setLayoutParams(newParams);
                                draggedCard.setForeground(null);
                                draggedCard.setRotation(0);
                                draggedCard.setVisibility(View.INVISIBLE);

                                if(cardLocaion == FIELD) {

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setRotation(draggedCard.getRotation());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = origix;
                                    ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == GRAVE){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == EDECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = 0;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == HAND){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(300);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == BANISH){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3*ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height));
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else
                                    draggedCard.setVisibility(View.VISIBLE);

                                //draggedCard.setRotation(0);
                            }

                            final int indexToSend = indexOfCard;
                            final ImageView compCard = draggedCard;


                            findViewById(R.id.deckInOpt).setVisibility(View.VISIBLE);

                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;

                        default:
                            break;

                    }

                }catch (Exception e){

                    // ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();

                }

                return false;
            }
        });

        findViewById(R.id.shufbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffleDeck();
                findViewById(R.id.deckInOpt).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.topbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempName = deckNames.get(deckNames.size() - 1);
                ImageView tempImg = deckContents.get(deckContents.size() - 1);
                deckNames.remove(deckNames.size() - 1);
                deckContents.remove(deckContents.size() - 1);
                yourDeckContents.removeViewAt(yourDeckContents.getChildCount() - 1);
                deckNames.add(0, tempName);
                deckContents.add(0, tempImg);
                yourDeckContents.addView(tempImg, 0);
                findViewById(R.id.deckInOpt).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.botbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.deckInOpt).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.handcont).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                try {

                    int userAction = dragEvent.getAction();

                    switch (userAction) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                                return true;
                            else
                                return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;

                        case DragEvent.ACTION_DROP:

                            //ClipData.Item draggedCardData = dragEvent.getClipData().getItemAt(0);
                            //android.widget.RelativeLayout.LayoutParams cardPositionParameters;
                            ImageView draggedCard = (ImageView) dragEvent.getLocalState();

                            int origix = -1;
                            int origiy = -1;

                            int indexOfCard = 0;

                            switch (cardLocaion) {

                                case EDECK:
                                    indexOfCard = extraDeckContents.indexOf(draggedCard);
                                    String name6 = extraDeckNames.get(indexOfCard);

                                    extraDeckContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourExtraDeckContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    extraDeckNames.remove(indexOfCard);
                                    handNames.add(name6);

                                    if(extraDeckContents.size() > 0)
                                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourExtraDeck.setAlpha(0.1f);

                                    break;

                                case BANISH:
                                    indexOfCard = banishContents.indexOf(draggedCard);
                                    String name5 = banishNames.get(indexOfCard);

                                    banishContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourBanishContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    banishNames.remove(indexOfCard);
                                    handNames.add(name5);
                                    if(banishContents.size() > 0)
                                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourBanish.setAlpha(0.1f);

                                    break;

                                case GRAVE:
                                    indexOfCard = graveContents.indexOf(draggedCard);
                                    String name4 = graveyardNames.get(indexOfCard);

                                    graveContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourGraveContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    graveyardNames.remove(indexOfCard);
                                    handNames.add(name4);
                                    if(graveContents.size() > 0)
                                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourGrave.setAlpha(0.1f);

                                    break;

                                case DECK:
                                    indexOfCard = deckContents.indexOf(draggedCard);
                                    String name3 = deckNames.get(indexOfCard);

                                    deckContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourDeckContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    deckNames.remove(indexOfCard);
                                    handNames.add(name3);

                                    break;

                                case HAND:

                                    indexOfCard = handContents.indexOf(draggedCard);
                                    String name = handNames.get(indexOfCard);

                                    handContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourHand.removeView(draggedCard);

                                    yourHand.addView(draggedCard);
                                    handNames.remove(indexOfCard);
                                    handNames.add(name);
                                    break;



                                case FIELD:

                                    indexOfCard = fieldContents.indexOf(draggedCard);
                                    String name2 = fieldNames.get(indexOfCard);

                                    origix = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).leftMargin;
                                    origiy = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).topMargin;

                                    fieldContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    gameField.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    fieldNames.remove(indexOfCard);
                                    handNames.add(name2);
                                    fieldCounters.remove(indexOfCard);
                                    break;

                            }



                            GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                            newParams.width = (int)(width/7.0);
                            newParams.height = (int)(height/heightToDivide);
                            draggedCard.setLayoutParams(newParams);
                            draggedCard.setForeground(null);
                            draggedCard.setRotation(0);
                            draggedCard.setVisibility(View.INVISIBLE);

                            if(cardLocaion == FIELD) {

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setRotation(draggedCard.getRotation());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = origix;
                                ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(200);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == DECK){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(200);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == EDECK){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = 0;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(200);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == GRAVE){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2*ncardPositionParameters.height) +  + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(300);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == BANISH){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3*ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(400);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else
                                draggedCard.setVisibility(View.VISIBLE);

                            final int indexToSend = indexOfCard;

                            final ImageView compCard = draggedCard;



                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;

                        default:
                            break;

                    }

                }catch (Exception e){

                    //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();
                }

                return false;
            }
        });

        yourHand.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                try {

                    int userAction = dragEvent.getAction();

                    switch (userAction) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                                return true;
                            else
                                return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;

                        case DragEvent.ACTION_DROP:

                            //ClipData.Item draggedCardData = dragEvent.getClipData().getItemAt(0);
                            //android.widget.RelativeLayout.LayoutParams cardPositionParameters;
                            ImageView draggedCard = (ImageView) dragEvent.getLocalState();
                            int origix = -1;
                            int origiy = -1;
                            int indexOfCard = 0;

                            switch (cardLocaion) {

                                case EDECK:
                                    indexOfCard = extraDeckContents.indexOf(draggedCard);
                                    String name6 = extraDeckNames.get(indexOfCard);

                                    extraDeckContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourExtraDeckContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    extraDeckNames.remove(indexOfCard);
                                    handNames.add(name6);

                                    if(extraDeckContents.size() > 0)
                                        yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourExtraDeck.setAlpha(0.1f);

                                    break;

                                case BANISH:
                                    indexOfCard = banishContents.indexOf(draggedCard);
                                    String name5 = banishNames.get(indexOfCard);

                                    banishContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourBanishContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    banishNames.remove(indexOfCard);
                                    handNames.add(name5);
                                    if(banishContents.size() > 0)
                                        yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourBanish.setAlpha(0.1f);

                                    break;

                                case GRAVE:
                                    indexOfCard = graveContents.indexOf(draggedCard);
                                    String name4 = graveyardNames.get(indexOfCard);

                                    graveContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourGraveContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    graveyardNames.remove(indexOfCard);
                                    handNames.add(name4);
                                    if(graveContents.size() > 0)
                                        yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                    else
                                        yourGrave.setAlpha(0.1f);

                                    break;

                                case DECK:
                                    indexOfCard = deckContents.indexOf(draggedCard);
                                    String name3 = deckNames.get(indexOfCard);

                                    deckContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourDeckContents.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    deckNames.remove(indexOfCard);
                                    handNames.add(name3);

                                    break;

                                case HAND:

                                    indexOfCard = handContents.indexOf(draggedCard);
                                    String name = handNames.get(indexOfCard);

                                    handContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    yourHand.removeView(draggedCard);

                                    yourHand.addView(draggedCard);
                                    handNames.remove(indexOfCard);
                                    handNames.add(name);
                                    break;



                                case FIELD:

                                    indexOfCard = fieldContents.indexOf(draggedCard);
                                    String name2 = fieldNames.get(indexOfCard);

                                    origix = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).leftMargin;
                                    origiy = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).topMargin;

                                    fieldContents.remove(indexOfCard);
                                    handContents.add(draggedCard);
                                    gameField.removeView(draggedCard);
                                    yourHand.addView(draggedCard);
                                    fieldNames.remove(indexOfCard);
                                    handNames.add(name2);
                                    fieldCounters.remove(indexOfCard);
                                    break;

                            }



                            GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                            newParams.width = (int)(width/7.0);
                            newParams.height = (int)(height/heightToDivide);
                            draggedCard.setLayoutParams(newParams);
                            draggedCard.setForeground(null);
                            draggedCard.setRotation(0);
                            draggedCard.setVisibility(View.INVISIBLE);

                            if(cardLocaion == FIELD) {

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setRotation(draggedCard.getRotation());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = origix;
                                ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(200);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == DECK){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(200);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == EDECK){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = 0;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(200);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == GRAVE){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2*ncardPositionParameters.height) +  + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(300);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else if(cardLocaion == BANISH){

                                ImageView tempAni = new ImageView(con);
                                tempAni.setImageDrawable(draggedCard.getDrawable());
                                tempAni.setForeground(draggedCard.getForeground());
                                aniCover.addView(tempAni);
                                RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3*ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                tempAni.setLayoutParams(ncardPositionParameters);
                                tempAni.setVisibility(View.VISIBLE);
                                Path path = new Path();
                                path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                                ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                ani.setDuration(400);
                                ani.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        aniCover.removeView(tempAni);
                                        draggedCard.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                                ani.start();

                            }else
                                draggedCard.setVisibility(View.VISIBLE);

                            final int indexToSend = indexOfCard;

                            final ImageView compCard = draggedCard;


                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;

                        default:
                            break;

                    }

                }catch (Exception e){

                    //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();
                }

                return false;
            }
        });

        gameField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHand();
            }
        });

        findViewById(R.id.viewCardOE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(oppRevealSelect != null)
                    new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(oppRevealName) + ".jpg");

            }
        });

        findViewById(R.id.viewCardAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(attachRevealSelect != null)
                    new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(attachName) + ".jpg");

            }
        });

        findViewById(R.id.viewCardOD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(oppRevealSelect != null)
                    new DisplayFullCard().execute(cardImageServerAdress + urlConverter.formatURL(oppRevealName) + ".jpg");

            }
        });


        findViewById(R.id.toGraveBAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int indexOfCard = fieldNames.indexOf(attachName);
                final ImageView attCard = fieldContents.get(indexOfCard);
                String name2 = attachName;
                int origix, origiy;
                origix = origiy = 0;

                if(!name2.equals("TOKEN")) {

                    origix = ((RelativeLayout.LayoutParams)(attCard.getLayoutParams())).leftMargin;
                    origiy = ((RelativeLayout.LayoutParams)(attCard.getLayoutParams())).topMargin;

                    fieldContents.remove(indexOfCard);
                    graveContents.add(attCard);
                    gameField.removeView(attCard);
                    yourGraveContents.addView(attCard);
                    fieldNames.remove(indexOfCard);
                    graveyardNames.add(name2);
                    fieldCounters.remove(indexOfCard);
                    isToken = false;

                }else{

                    fieldContents.remove(indexOfCard);
                    gameField.removeView(attCard);
                    fieldNames.remove(indexOfCard);
                    fieldCounters.remove(indexOfCard);
                    isToken = true;

                }

                if(!isToken){

                    GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                    newParams.width = (int)(width/7.0);
                    newParams.height = (int)(height/heightToDivide);
                    attCard.setLayoutParams(newParams);
                    attCard.setForeground(null);
                    attCard.setRotation(0);
                    attCard.setVisibility(View.INVISIBLE);

                    yourGraveContents.setVisibility(View.VISIBLE);
                    Drawable tempD = attCard.getDrawable().getConstantState().newDrawable().mutate();
                    if ((selectedCard != null && selectedCard.equals(attCard)))
                        tempD.setColorFilter(Color.argb(60, 0, 255, 0), PorterDuff.Mode.OVERLAY);
                    else if (opSelectedCard != null && opSelectedCard.equals(attCard))
                        tempD.setColorFilter(Color.argb(60, 255, 0, 0), PorterDuff.Mode.OVERLAY);
                    yourGrave.setBackground(tempD);
                    yourGrave.setImageDrawable(null);
                    yourGrave.setAlpha(1.0f);
                    findViewById(R.id.gravePadding).setVisibility(View.VISIBLE);

                    ImageView tempAni = new ImageView(con);
                    tempAni.setImageDrawable(attCard.getDrawable());
                    tempAni.setRotation(attCard.getRotation());
                    tempAni.setForeground(attCard.getForeground());
                    aniCover.addView(tempAni);
                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                    ncardPositionParameters.leftMargin = origix;
                    ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                    tempAni.setLayoutParams(ncardPositionParameters);
                    tempAni.setVisibility(View.VISIBLE);
                    Path path = new Path();
                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                    path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, aniCover.getMeasuredHeight() - ((2 * ncardPositionParameters.height) + yourHand.getLayoutParams().height));
                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                    ani.setDuration(200);
                    ani.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniCover.removeView(tempAni);
                            attCard.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    ani.start();

                }


                attachedShow.removeView(attachRevealSelect);
                attachRevealSelect = null;
                attachName = "";
                showHand();

            }
        });



        gameField.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                try {

                    int userAction = dragEvent.getAction();

                    switch (userAction) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                                return true;
                            else
                                return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;

                        case DragEvent.ACTION_DROP:

                            //ClipData.Item draggedCardData = dragEvent.getClipData().getItemAt(0);
                            android.widget.RelativeLayout.LayoutParams cardPositionParameters;
                            final ImageView draggedCard = (ImageView) dragEvent.getLocalState();
                            int origix = -1;
                            int origiy = -1;
                            int[] measure = new int[2];
                            int indexOfCard = 0;
                            boolean attac = false;

                            if(!draggedCard.equals(tokenCard)) {

                                switch (cardLocaion) {

                                    case EDECK:
                                        indexOfCard = extraDeckContents.indexOf(draggedCard);
                                        String name6 = extraDeckNames.get(indexOfCard);

                                        extraDeckContents.remove(indexOfCard);
                                        fieldContents.add(draggedCard);
                                        yourExtraDeckContents.removeView(draggedCard);
                                        gameField.addView(draggedCard);
                                        extraDeckNames.remove(indexOfCard);
                                        fieldNames.add(name6);
                                        fieldCounters.add(0);

                                        if(extraDeckContents.size() > 0)
                                            yourExtraDeck.setBackground(extraDeckContents.get(extraDeckContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                        else
                                            yourExtraDeck.setAlpha(0.1f);

                                        break;

                                    case BANISH:
                                        indexOfCard = banishContents.indexOf(draggedCard);
                                        String name5 = banishNames.get(indexOfCard);

                                        banishContents.remove(indexOfCard);
                                        fieldContents.add(draggedCard);
                                        yourBanishContents.removeView(draggedCard);
                                        gameField.addView(draggedCard);
                                        banishNames.remove(indexOfCard);
                                        fieldNames.add(name5);
                                        fieldCounters.add(0);
                                        if(banishContents.size() > 0)
                                            yourBanish.setImageDrawable(banishContents.get(banishContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                        else
                                            yourBanish.setAlpha(0.1f);

                                        break;

                                    case GRAVE:
                                        indexOfCard = graveContents.indexOf(draggedCard);
                                        String name4 = graveyardNames.get(indexOfCard);

                                        graveContents.remove(indexOfCard);
                                        fieldContents.add(draggedCard);
                                        yourGraveContents.removeView(draggedCard);
                                        gameField.addView(draggedCard);
                                        graveyardNames.remove(indexOfCard);
                                        fieldNames.add(name4);
                                        fieldCounters.add(0);
                                        if(graveContents.size() > 0)
                                            yourGrave.setBackground(graveContents.get(graveContents.size() - 1).getDrawable().getConstantState().newDrawable());
                                        else
                                            yourGrave.setAlpha(0.1f);

                                        break;

                                    case DECK:
                                        indexOfCard = deckContents.indexOf(draggedCard);
                                        String name3 = deckNames.get(indexOfCard);

                                        deckContents.remove(indexOfCard);
                                        fieldContents.add(draggedCard);
                                        yourDeckContents.removeView(draggedCard);
                                        gameField.addView(draggedCard);
                                        deckNames.remove(indexOfCard);
                                        fieldNames.add(name3);
                                        fieldCounters.add(0);

                                        break;

                                    case HAND:

                                        indexOfCard = handContents.indexOf(draggedCard);
                                        String name = handNames.get(indexOfCard);

                                        handContents.remove(indexOfCard);
                                        fieldContents.add(draggedCard);
                                        yourHand.removeView(draggedCard);

                                        gameField.addView(draggedCard);
                                        handNames.remove(indexOfCard);
                                        fieldNames.add(name);
                                        fieldCounters.add(0);
                                        break;


                                    case FIELD:

                                        draggedCard.getLocationOnScreen(measure);
                                        indexOfCard = fieldContents.indexOf(draggedCard);
                                        String name2 = fieldNames.get(indexOfCard);
                                        int cnt = fieldCounters.get(indexOfCard);

                                        origix = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).leftMargin;
                                        origiy = ((RelativeLayout.LayoutParams)(draggedCard.getLayoutParams())).topMargin;

                                        fieldContents.remove(indexOfCard);
                                        fieldContents.add(draggedCard);
                                        gameField.removeView(draggedCard);
                                        gameField.addView(draggedCard);
                                        fieldNames.remove(indexOfCard);
                                        fieldNames.add(name2);
                                        fieldCounters.remove(indexOfCard);
                                        fieldCounters.add(cnt);

                                        break;

                                }

                                cardPositionParameters = (RelativeLayout.LayoutParams) draggedCard.getLayoutParams();
                                cardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                cardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                cardPositionParameters.leftMargin = (int) dragEvent.getX() - (cardPositionParameters.width / 2);
                                cardPositionParameters.topMargin = (int) dragEvent.getY() - (cardPositionParameters.height / 2);

                                draggedCard.setLayoutParams(cardPositionParameters);
                                draggedCard.setVisibility(View.INVISIBLE);

                                if((draggedCard.equals(selectedCard) && draggedCard.getForeground() != null && fieldCounters.get(fieldContents.indexOf(draggedCard)) <= 0)){

                                    draggedCard.setColorFilter(null);
                                    draggedCard.getForeground().setColorFilter(Color.argb(125, 0, 255, 0), PorterDuff.Mode.OVERLAY);

                                }else if(draggedCard.equals(opSelectedCard) && draggedCard.getForeground() != null && fieldCounters.get(fieldContents.indexOf(draggedCard)) <= 0){

                                    draggedCard.setColorFilter(null);
                                    draggedCard.getForeground().setColorFilter(Color.argb(60, 255, 0, 0), PorterDuff.Mode.OVERLAY);

                                }

                                //LOOP
                                final int originx = origix;
                                final int originy = origiy;
                                final int origin[] = measure;

                                if(cardLocaion == FIELD) {

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setRotation(draggedCard.getRotation());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = origix;
                                    ncardPositionParameters.topMargin = origiy + opponentHand.getLayoutParams().height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(origix, origiy + opponentHand.getLayoutParams().height);
                                    path.lineTo(cardPositionParameters.leftMargin, cardPositionParameters.topMargin + opponentHand.getLayoutParams().height);
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);

                                            if(!cardInfo.getXYZ(cardInfo.getName(fieldNames.get(fieldContents.indexOf(draggedCard))))) {

                                                for (int i = 0; i < fieldContents.size(); i++) {

                                                    if (!fieldContents.get(i).equals(draggedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNames.get(i))) && isViewOverlapping(fieldContents.get(i), draggedCard)) {

                                                        gameField.removeView(fieldContents.get(i));
                                                        gameField.addView(fieldContents.get(i));
                                                        break;

                                                    }

                                                }

                                            }else{

                                                for (int i = 0; i < fieldContents.size(); i++) {

                                                    if (isViewOverlappingVals(fieldContents.get(i), origin, draggedCard, false) && gameField.indexOfChild(fieldContents.get(i)) < gameField.indexOfChild(draggedCard)) {

                                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fieldContents.get(i).getLayoutParams();

                                                        params.leftMargin += ((RelativeLayout.LayoutParams)draggedCard.getLayoutParams()).leftMargin - originx;
                                                        params.topMargin += ((RelativeLayout.LayoutParams)draggedCard.getLayoutParams()).topMargin - originy;

                                                        fieldContents.get(i).setLayoutParams(params);

                                                    }

                                                }

                                            }

                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == HAND){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = (int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2);
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ncardPositionParameters.height;
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(cardPositionParameters.leftMargin, cardPositionParameters.topMargin + opponentHand.getLayoutParams().height);
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                            for(int i = 0; i < fieldContents.size(); i++){

                                                if(!fieldContents.get(i).equals(draggedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNames.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNames.get(fieldContents.indexOf(draggedCard)))) && isViewOverlapping(fieldContents.get(i), draggedCard)){

                                                    gameField.removeView(fieldContents.get(i));
                                                    gameField.addView(fieldContents.get(i));
                                                    break;

                                                }

                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == DECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(cardPositionParameters.leftMargin, cardPositionParameters.topMargin + opponentHand.getLayoutParams().height);
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                            for(int i = 0; i < fieldContents.size(); i++){

                                                if(!fieldContents.get(i).equals(draggedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNames.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNames.get(fieldContents.indexOf(draggedCard)))) && isViewOverlapping(fieldContents.get(i), draggedCard)){

                                                    gameField.removeView(fieldContents.get(i));
                                                    gameField.addView(fieldContents.get(i));
                                                    break;

                                                }

                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == EDECK){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = 0;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(cardPositionParameters.leftMargin, cardPositionParameters.topMargin + opponentHand.getLayoutParams().height);
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                            for(int i = 0; i < fieldContents.size(); i++){

                                                if(!fieldContents.get(i).equals(draggedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNames.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNames.get(fieldContents.indexOf(draggedCard)))) && isViewOverlapping(fieldContents.get(i), draggedCard)){

                                                    gameField.removeView(fieldContents.get(i));
                                                    gameField.addView(fieldContents.get(i));
                                                    break;

                                                }

                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == GRAVE){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((2*ncardPositionParameters.height) +  + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(cardPositionParameters.leftMargin, cardPositionParameters.topMargin + opponentHand.getLayoutParams().height);
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                            for(int i = 0; i < fieldContents.size(); i++){

                                                if(!fieldContents.get(i).equals(draggedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNames.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNames.get(fieldContents.indexOf(draggedCard)))) && isViewOverlapping(fieldContents.get(i), draggedCard)){

                                                    gameField.removeView(fieldContents.get(i));
                                                    gameField.addView(fieldContents.get(i));
                                                    break;

                                                }

                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else if(cardLocaion == BANISH){

                                    ImageView tempAni = new ImageView(con);
                                    tempAni.setImageDrawable(draggedCard.getDrawable());
                                    tempAni.setForeground(draggedCard.getForeground());
                                    aniCover.addView(tempAni);
                                    RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                                    ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                    ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                    ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                                    ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - ((3*ncardPositionParameters.height) + yourHand.getLayoutParams().height);
                                    tempAni.setLayoutParams(ncardPositionParameters);
                                    tempAni.setVisibility(View.VISIBLE);
                                    Path path = new Path();
                                    path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                                    path.lineTo(cardPositionParameters.leftMargin, cardPositionParameters.topMargin + opponentHand.getLayoutParams().height);
                                    ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                                    ani.setDuration(200);
                                    ani.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            aniCover.removeView(tempAni);
                                            draggedCard.setVisibility(View.VISIBLE);
                                            for(int i = 0; i < fieldContents.size(); i++){

                                                if(!fieldContents.get(i).equals(draggedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNames.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNames.get(fieldContents.indexOf(draggedCard)))) && isViewOverlapping(fieldContents.get(i), draggedCard)){

                                                    gameField.removeView(fieldContents.get(i));
                                                    gameField.addView(fieldContents.get(i));
                                                    break;

                                                }

                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    });
                                    ani.start();

                                }else
                                    draggedCard.setVisibility(View.VISIBLE);


                                final boolean attach = attac;
                                final double x = (double) cardPositionParameters.leftMargin / (double) gameField.getMeasuredWidth();
                                final double y = (double) cardPositionParameters.topMargin / (double) gameField.getMeasuredHeight();
                                final boolean facing = (draggedCard.getForeground() != null && fieldCounters.get(fieldContents.indexOf(draggedCard)) <= 0) ? true : false;
                                final int indexToSend = indexOfCard;
                                final boolean turned = (draggedCard.getRotation() == 0f);
                                final int cnts = (fieldCounters.get(fieldContents.indexOf(draggedCard)) <= 0) ? -1 : fieldCounters.get(fieldContents.indexOf(draggedCard));

                                final ImageView compCard = draggedCard;



                            }else{

                                final ImageView tokenToAdd = new ImageView(con);
                                tokenToAdd.setImageDrawable(getResources().getDrawable(R.drawable.token).getConstantState().newDrawable().mutate());

                                gameField.addView(tokenToAdd);
                                fieldContents.add(tokenToAdd);
                                fieldNames.add("TOKEN");
                                fieldCounters.add(0);

                                ((TextView) findViewById(R.id.textView3)).setText(fieldContents.size() + "");

                                cardPositionParameters = (RelativeLayout.LayoutParams) tokenToAdd.getLayoutParams();
                                cardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                                cardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                                cardPositionParameters.leftMargin = (int) dragEvent.getX() - (cardPositionParameters.width / 2);
                                cardPositionParameters.topMargin = (int) dragEvent.getY() - (cardPositionParameters.height / 2);

                                tokenToAdd.setLayoutParams(cardPositionParameters);
                                tokenToAdd.setVisibility(View.VISIBLE);

                                tokenToAdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if(yourGrave.getBackground() != null)
                                            yourGrave.getBackground().setColorFilter(null);
                                        yourBanish.setColorFilter(null);

                                        if(selectedCard != null)
                                            selectedCard.setColorFilter(null);
                                        tokenToAdd.setColorFilter(Color.argb(60, 0, 255, 0));


                                        selectedCard = tokenToAdd;
                                        cardLocaion = FIELD;



                                        showCardOptionsToken();
                                    }
                                });

                                tokenToAdd.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        ClipData.Item item;
                                        String[] mimeTypes;
                                        ClipData data;
                                        View.DragShadowBuilder dragshadow;

                                        item = new ClipData.Item((CharSequence) view.getTag());
                                        mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                                        data = new ClipData((CharSequence) view.getTag(), mimeTypes, item);
                                        dragshadow = new View.DragShadowBuilder(view);

                                        view.startDrag(data, dragshadow, view, 0);
                                        clearForDrag();

                                        cardLocaion = FIELD;

                                        return true;

                                    }
                                });

                                final double x = (double) cardPositionParameters.leftMargin / (double) gameField.getMeasuredWidth();
                                final double y = (double) cardPositionParameters.topMargin / (double) gameField.getMeasuredHeight();



                            }

                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;

                        default:
                            break;

                    }

                }catch (Exception e){

                    //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    gameExitCrash();
                }

                return false;
            }
        });

    }

    public ImageView createPlaceHolderCardED(){

        final ImageView cardPlaceHolder = new ImageView(con);

        cardPlaceHolder.setMinimumHeight((int)(height/heightToDivide));
        cardPlaceHolder.setMinimumWidth((int)(width/7));
        cardPlaceHolder.setMaxHeight((int)(height/heightToDivide));
        cardPlaceHolder.setMaxWidth((int)(width/7));
        cardPlaceHolder.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
        cardPlaceHolder.setAdjustViewBounds(true);

        cardPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearAllOptions();
                        if(selectedCard != null)
                            selectedCard.setColorFilter(null);
                        cardPlaceHolder.setColorFilter(Color.argb(60, 0, 255, 0));
                    }
                });

                selectedCard = cardPlaceHolder;



            }
        });

        return cardPlaceHolder;

    }

    public ImageView createPlaceHolderCard(){

        final ImageView cardPlaceHolder = new ImageView(con);

        cardPlaceHolder.setMinimumHeight((int)(height/heightToDivide));
        cardPlaceHolder.setMinimumWidth((int)(width/7));
        cardPlaceHolder.setMaxHeight((int)(height/heightToDivide));
        cardPlaceHolder.setMaxWidth((int)(width/7));
        cardPlaceHolder.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
        cardPlaceHolder.setAdjustViewBounds(true);

        cardPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearAllOptions();
                        if(selectedCard != null)
                            selectedCard.setColorFilter(null);
                        cardPlaceHolder.setColorFilter(Color.argb(60, 0, 255, 0));
                    }
                });

                selectedCard = cardPlaceHolder;



            }
        });

        return cardPlaceHolder;

    }

    public void gameReset(){

        findViewById(R.id.gamecover).setForeground(getResources().getDrawable(R.drawable.loadingscreen));
        findViewById(R.id.coverforgame).setVisibility(View.VISIBLE);
        opR = false;
        selectedCard = null;
        opSelectedCard = null;
        yourDeckContents.removeAllViews();
        yourExtraDeckContents.removeAllViews();
        yourGraveContents.removeAllViews();
        yourBanishContents.removeAllViews();
        yourHand.removeAllViews();

        opponentBanishContents.removeAllViews();
        opponentGraveContents.removeAllViews();
        opponentHand.removeAllViews();

        for(int i = gameField.getChildCount() - 1; i > 11; i--){

            gameField.removeViewAt(i);

        }
        //opponentExtraDeckContents.removeAllViews();

        deckContents.clear();
        extraDeckContents.clear();
        graveContents.clear();
        banishContents.clear();
        handContents.clear();
        fieldContents.clear();
        deckContentsOpponent.clear();
        extraDeckContentsOpponent.clear();
        graveContentsOpponent.clear();
        banishContentsOpponent.clear();
        handContentsOpponent.clear();
        fieldContentsOpponent.clear();

        deckNames.clear();
        extraDeckNames.clear();
        graveyardNames.clear();
        banishNames.clear();
        handNames.clear();
        fieldNames.clear();
        deckNamesOpponent.clear();
        extraDeckNamesOpponent.clear();
        graveyardNamesOpponent.clear();
        banishNamesOpponent.clear();
        handNamesOpponent.clear();
        fieldNamesOpponent.clear();

        yourGrave.setAlpha(0.1f);
        yourGrave.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
        yourBanish.setAlpha(0.1f);
        yourBanish.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
        opponentBanish.setAlpha(0.1f);
        opponentBanish.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
        opponentGrave.setAlpha(0.1f);
        opponentGrave.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());

        //resetMainDeckNameList.add(splitDb[i]);
        //new putToDeck().execute(cardImageServerAdress2 + urlConverter.formatURL(splitDb[i]) + ".jpg");

        //resetExtraDeckNameList.add(splitDb[i]);
        //new putToExtraDeck().execute(cardImageServerAdress2 + urlConverter.formatURL(splitDb[i]) + ".jpg");

        for (int i = 0; i < resetMainDeckNameList.size(); i++) {

            new putToDeck().execute(cardImageServerAdress2 + urlConverter.formatURL(resetMainDeckNameList.get(i)) + ".jpg");

        }

        for (int i = 0; i < resetExtraDeckNameList.size(); i++) {

            if(yourExtraDeck.getAlpha() == 0.1f){

                yourExtraDeck.setAlpha(1.0f);


            }
            new putToExtraDeck().execute(cardImageServerAdress2 + urlConverter.formatURL(resetExtraDeckNameList.get(i)) + ".jpg");

        }

    }

    public void setOpponentCounters(String[] vals){

        try {

            int indx = Integer.parseInt(vals[1]);
            int num = Integer.parseInt(vals[0]);

            //while(fieldContentsOpponent.size() <= indx);

            Bitmap counters = Bitmap.createBitmap((int) width / 7, (int) ((int) height / heightToDivide), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(counters);

            final int tempIn = indx;

            fieldCountersOpponent.set(tempIn, fieldCountersOpponent.get(tempIn) + 1);
//check t see if the counters gets updated
            Paint paint = new Paint();
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setTextSize(48.0f);
            paint.setColor(Color.GREEN);
            canvas.drawText(num + "", (float) (width / 60), (float) (height / (1.05f * heightToDivide)), paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(counters, 0, 0, paint);

            Drawable overlay = new BitmapDrawable(getResources(), counters);

            fieldContentsOpponent.get(indx).setForeground(overlay);
        }catch (Exception e){
            // ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
            gameExitCrash();
        }

    }

    public void shuffleExtradeck(){

        int len = extraDeckContents.size();
        ArrayList<Integer> tempArry = new ArrayList<Integer>();

        for(int i = 0; i < len; i++){

            tempArry.add(i);

        }
        yourExtraDeckContents.removeAllViews();
        //deckContents.removeA
        ArrayList<ImageView> tempImage = new ArrayList<ImageView>();
        ArrayList<String> tempName = new ArrayList<String>();
        Collections.shuffle(tempArry);
        for(int i = 0; i < len; i++){
            yourExtraDeckContents.addView(extraDeckContents.get(tempArry.get(i)));
            tempImage.add(extraDeckContents.get(tempArry.get(i)));
            tempName.add(extraDeckNames.get(tempArry.get(i)));
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) extraDeckContents.get(tempArry.get(i)).getLayoutParams();
            params.width = (int) (width/7);
            params.height = (int)(height/heightToDivide);
            extraDeckContents.get(tempArry.get(i)).setLayoutParams(params);
        }
        extraDeckContents = (ArrayList<ImageView>) tempImage.clone();
        extraDeckNames = (ArrayList<String>) tempName.clone();

    }

    public void shuffleHand(){

        int len = handContents.size();
        ArrayList<Integer> tempArry = new ArrayList<Integer>();

        for(int i = 0; i < len; i++){

            tempArry.add(i);

        }
        yourHand.removeAllViews();

        //deckContents.removeA
        ArrayList<ImageView> tempImage = new ArrayList<ImageView>();
        ArrayList<String> tempName = new ArrayList<String>();
        Collections.shuffle(tempArry);
        for(int i = 0; i < len; i++){
            yourHand.addView(handContents.get(tempArry.get(i)));
            tempImage.add(handContents.get(tempArry.get(i)));
            tempName.add(handNames.get(tempArry.get(i)));
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) handContents.get(tempArry.get(i)).getLayoutParams();
            params.width = (int) (width/7);
            params.height = (int)(height/heightToDivide);
            handContents.get(tempArry.get(i)).setLayoutParams(params);
        }
        handContents = (ArrayList<ImageView>) tempImage.clone();
        handNames = (ArrayList<String>) tempName.clone();

    }

    public void shuffleDeck(){

        int len = deckContents.size();

        ArrayList<Integer> tempArry = new ArrayList<Integer>();

        for(int i = 0; i < len; i++){

            tempArry.add(i);

        }

        yourDeckContents.removeAllViews();
        //deckContents.removeA

        ArrayList<ImageView> tempImage = new ArrayList<ImageView>();
        ArrayList<String> tempName = new ArrayList<String>();

        Collections.shuffle(tempArry);

        for(int i = 0; i < len; i++){

            yourDeckContents.addView(deckContents.get(tempArry.get(i)));
            tempImage.add(deckContents.get(tempArry.get(i)));
            tempName.add(deckNames.get(tempArry.get(i)));

            GridLayout.LayoutParams params = (GridLayout.LayoutParams) deckContents.get(tempArry.get(i)).getLayoutParams();
            params.width = (int) (width/7);
            params.height = (int)(height/heightToDivide);
            deckContents.get(tempArry.get(i)).setLayoutParams(params);

        }

        deckContents = (ArrayList<ImageView>) tempImage.clone();
        deckNames = (ArrayList<String>) tempName.clone();

    }

    public void highlightCard(String[] vals){

        try {

            final int loc = Integer.parseInt(vals[0]);
            int index = 0;
            if(vals.length > 1)
                index = Integer.parseInt(vals[1]);
            final int indx = index;
            if(opponentBanish.getBackground() != null)
                opponentBanish.getBackground().setColorFilter(null);
            if(opponentGrave.getBackground() != null)
                opponentGrave.getBackground().setColorFilter(null);
            if(opponentExtraDeck.getBackground() != null)
                opponentExtraDeck.getBackground().setColorFilter(null);

            if (opSelectedCard != null) {
                if(opSelectedCard.getForeground() == null || (opSelectedCard.getParent().equals(gameField) && fieldContentsOpponent.contains(opSelectedCard) && fieldCountersOpponent.get(fieldContentsOpponent.indexOf(opSelectedCard)) > 0))
                    opSelectedCard.setColorFilter(null);
                else
                    opSelectedCard.getForeground().setColorFilter(null);
                opSelectedCard.invalidate();
            }

            if(vals.length == 1)
                return;

            switch (loc) {

                case FIELD:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(gameField.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(((ImageView) gameField.getChildAt(indx)).getForeground() == null || ((fieldContentsOpponent.contains(gameField.getChildAt(indx)) && fieldCountersOpponent.get(fieldContentsOpponent.indexOf(gameField.getChildAt(indx))) > 0) || (fieldCounters.contains(gameField.getChildAt(indx)) && fieldCounters.get(fieldContents.indexOf(gameField.getChildAt(indx))) > 0)))
                                        ((ImageView) gameField.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    else
                                        ((ImageView) gameField.getChildAt(indx)).getForeground().setColorFilter(Color.argb(60, 255, 0, 0), PorterDuff.Mode.OVERLAY);
                                    opSelectedCard = (ImageView) gameField.getChildAt(indx);
                                    opSelectedCard.invalidate();
                                }
                            });
                        }
                    }).start();
                    break;
                case GRAVE:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(opponentGraveContents.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) opponentGraveContents.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opponentGrave.setBackground(((ImageView) opponentGraveContents.getChildAt(indx)).getDrawable().getConstantState().newDrawable());
                                    opSelectedCard = ((ImageView) opponentGraveContents.getChildAt(indx));
                                }
                            });
                        }
                    }).start();
                    break;
                case EDECK:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(opponentExtraDeckContents.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) opponentExtraDeckContents.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opponentExtraDeck.setBackground(((ImageView) opponentExtraDeckContents.getChildAt(indx)).getDrawable().getConstantState().newDrawable());
                                    opSelectedCard = ((ImageView) opponentExtraDeckContents.getChildAt(indx));
                                }
                            });
                        }
                    }).start();
                    break;
                case BANISH:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(opponentBanishContents.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ((ImageView) opponentBanishContents.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opponentBanish.setBackground(((ImageView) opponentBanishContents.getChildAt(indx)).getDrawable().getConstantState().newDrawable());
                                    opSelectedCard = ((ImageView) opponentBanishContents.getChildAt(indx));

                                }
                            });
                        }
                    }).start();
                    break;
                case HAND:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(opponentHand.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ((ImageView) opponentHand.getChildAt(opponentHand.getChildCount() - (indx + 1))).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opSelectedCard = ((ImageView) opponentHand.getChildAt(opponentHand.getChildCount() - (indx + 1)));

                                }
                            });
                        }
                    }).start();
                    break;
                case 10:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(yourBanishContents.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) yourBanishContents.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opSelectedCard = ((ImageView) yourBanishContents.getChildAt(indx));
                                }
                            });
                        }
                    }).start();
                    break;
                case 11:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(yourGraveContents.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ((ImageView) yourGraveContents.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opSelectedCard = ((ImageView) yourGraveContents.getChildAt(indx));

                                }
                            });
                        }
                    }).start();
                    break;
                case 12:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(yourHand.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) yourHand.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opSelectedCard = ((ImageView) yourHand.getChildAt(indx));
                                }
                            });
                        }
                    }).start();
                    break;
                case 14:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(yourExtraDeckContents.getChildCount() <= indx);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ((ImageView) yourExtraDeckContents.getChildAt(indx)).setColorFilter(Color.argb(60, 255, 0, 0));
                                    opSelectedCard = ((ImageView) yourExtraDeckContents.getChildAt(indx));

                                }
                            });
                        }
                    }).start();
                    break;

            }

        }catch (Exception e){
            //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
            FirebaseCrashlytics.getInstance().recordException(e);
            gameExitCrash();
        }

    }

    public void rollDiceOpp(){

        dieNumber.setText(String.valueOf(diceRollValue));
        dieResults.setVisibility(View.VISIBLE);
        extraOptions.setVisibility(View.INVISIBLE);
    }

    public void rollTheDice(){

        diceRollValue = ThreadLocalRandom.current().nextInt(1, 7);

    }

    public void flipTheCoin(){

        diceRollValue = ThreadLocalRandom.current().nextInt(8, 10);

    }

    public void changeOpponentHealth(int newHealth){

        opponentHealth.setText("" + newHealth);

    }

    public void turnCard(String move, int index){

        ImageView cardToTurnSearcher = fieldContentsOpponent.get(index);
        int fieldIndex = gameField.indexOfChild(cardToTurnSearcher);

        if(move.equals("O"))
            gameField.getChildAt(fieldIndex).setRotation(180f);
        else
            gameField.getChildAt(fieldIndex).setRotation(270f);

    }

    public void flipCard(String move, int index){

        ImageView cardToTurnSearcher = fieldContentsOpponent.get(index);
        int fieldIndex = gameField.indexOfChild(cardToTurnSearcher);

        if(move.equals("U")) {
            gameField.getChildAt(fieldIndex).setForeground(null);
            if((selectedCard != null && selectedCard.equals(gameField.getChildAt(fieldIndex))))
                ((ImageView) gameField.getChildAt(fieldIndex)).setColorFilter(Color.argb(60, 0, 255, 0));
            else if(opSelectedCard != null && opSelectedCard.equals(gameField.getChildAt(fieldIndex)))
                ((ImageView) gameField.getChildAt(fieldIndex)).setColorFilter(Color.argb(60, 255, 0, 0));

        }else {
            try{
                fieldCountersOpponent.set(fieldContentsOpponent.indexOf(gameField.getChildAt(fieldIndex)), 0);
                gameField.getChildAt(fieldIndex).setForeground(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());
                if((selectedCard != null && selectedCard.equals(gameField.getChildAt(fieldIndex)))) {
                    ((ImageView) gameField.getChildAt(fieldIndex)).setColorFilter(null);
                    gameField.getChildAt(fieldIndex).getForeground().setColorFilter(Color.argb(125, 0, 255, 0), PorterDuff.Mode.OVERLAY);
                }else if(gameField.getChildAt(fieldIndex).equals(opSelectedCard)){
                    ((ImageView) gameField.getChildAt(fieldIndex)).setColorFilter(null);
                    gameField.getChildAt(fieldIndex).getForeground().setColorFilter(Color.argb(60, 255, 0, 0), PorterDuff.Mode.OVERLAY);
                }
            }catch (Exception e){
                gameExitCrash();
                //((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
            }
        }

    }

    public void defineVariables(){

        display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        width = screenSize.x;
        height = screenSize.y;
        cardMap = new ConcurrentHashMap<String, Drawable>();

        heightToDivide = (((height / width) - 1.0) * 4.0) + 5;

        yourDeck = findViewById(R.id.deck);
        yourGrave = findViewById(R.id.grave);
        yourBanish = findViewById(R.id.banishZone);
        yourExtraDeck = findViewById(R.id.extradeck);
        opponentGrave = findViewById(R.id.grave2);
        opponentBanish = findViewById(R.id.banishZone2);
        opponentExtraDeck = findViewById(R.id.extradeck2);
        yourHealth = findViewById(R.id.yourHP);
        opponentHealth = findViewById(R.id.opponentHP);
        flipCoin = findViewById(R.id.coinFlip);

        //Frontend modification UI
        showHandToOpponent = findViewById(R.id.showHand);
        millFromTop = findViewById(R.id.mill);
        confirmReset = findViewById(R.id.resetConfirm);
        confirmSide = findViewById(R.id.sideConfirm);
        leaveGame = findViewById(R.id.exitButton);
        exitPrev = findViewById(R.id.exitGame);
        surrender = findViewById(R.id.surrenderButton);
        specialControlsButton = findViewById(R.id.moreOptions);
        showScroll = findViewById(R.id.showCards);
        showAttached = findViewById(R.id.attachCards);
        deckScroll = findViewById(R.id.decksc);
        extraDeckScroll = findViewById(R.id.edecksc);
        opponentExtraDeckScroll = findViewById(R.id.opedecksc);
        graveScroll = findViewById(R.id.yourgrvsc);
        banishScroll = findViewById(R.id.yourbsc);
        opponentBanishScroll = findViewById(R.id.oppbsc);
        opponentGraveScroll = findViewById(R.id.oppgravesc);
        yourHand = findViewById(R.id.hand1);
        opponentHand = findViewById(R.id.hand2);
        opponentShow = findViewById(R.id.showContents);
        attachedShow = findViewById(R.id.attachContents);
        yourDeckContents = findViewById(R.id.deckContents);
        yourExtraDeckContents = findViewById(R.id.edCont);
        yourGraveContents = findViewById(R.id.graveCont);
        yourBanishContents = findViewById(R.id.banishCont);
        opponentExtraDeckContents = findViewById(R.id.opedCont);
        opponentGraveContents = findViewById(R.id.graveCont2);
        opponentBanishContents = findViewById(R.id.banishCont2);
        gameField = findViewById(R.id.field);
        aniCover = findViewById(R.id.moveani);

        //UI Stuff
        changeControl = findViewById(R.id.changeControl);
        sideCards = findViewById(R.id.sideDeckButton);
        resetGame = findViewById(R.id.resetButton);
        addCounter = findViewById(R.id.addCounter);
        removeCounter = findViewById(R.id.removeCounter);
        shuffleDeck = findViewById(R.id.shuffleDeck);
        shuffleHand = findViewById(R.id.shuffleHand);
        shuffleEDeck = findViewById(R.id.shuffleEDeck);
        dieNumber = findViewById(R.id.dieNumber);
        previewCard = findViewById(R.id.imageView2);
        rollDice = findViewById(R.id.dieRoll);
        drawFromMainDeck = findViewById(R.id.draw);
        banishFromMainDeck = findViewById(R.id.banish);
        banishFromExtraDeck = findViewById(R.id.banishEd);
        cardFaceUpOrDown = findViewById(R.id.fud);
        cardDefenseOrAttack = findViewById(R.id.da);
        viewCard = findViewById(R.id.viewCard);
        autoSet = findViewById(R.id.set);
        opponentRevealControlD = findViewById(R.id.oppViewControlsD);
        attachControls = findViewById(R.id.attachViewControls);
        opponentRevealControlsE = findViewById(R.id.oppViewControlsE);
        cardControls = findViewById(R.id.normalControls);
        deckControls = findViewById(R.id.nonViewDeckControls);
        extraDeckControls = findViewById(R.id.edViewCont);
        extraOptions = findViewById(R.id.specialCommandsWrapper);
        dieResults = findViewById(R.id.dieResult);
        tokenCard = findViewById(R.id.token);
        resetConfirm = findViewById(R.id.resetGameBack);
        sideConfirm = findViewById(R.id.sideCardsB);
        attackDec = findViewById(R.id.attackbut);
        viewAttached = findViewById(R.id.viewAttached);
        toBanishButton = findViewById(R.id.toBanishB);
        toGraveButton = findViewById(R.id.toGraveB);
        toDeckButton = findViewById(R.id.toDeckB);
        toEdeckButton = findViewById(R.id.toEdB);
        toHandButton = findViewById(R.id.toHB);

        //Modify Hands
        yourHand.setUseDefaultMargins(false);
        yourHand.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        yourHand.setRowOrderPreserved(false);

        opponentHand.setUseDefaultMargins(false);
        opponentHand.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        opponentHand.setRowOrderPreserved(false);



        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tokenCard.getLayoutParams();
        params.width = (int) (width/7);
        params.height = (int)(height/heightToDivide);
        tokenCard.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) findViewById(R.id.showTopDeck).getLayoutParams();
        params.width = (int) width - findViewById(R.id.numToReveal).getMeasuredWidth();
        findViewById(R.id.showTopDeck).setLayoutParams(params);

        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) gameField.getLayoutParams();
        p.height = (int) width;
        p.width = (int) width;
        gameField.setLayoutParams(p);

        p = (ConstraintLayout.LayoutParams) extraDeckControls.getLayoutParams();
        p.width = (int) width;
        p.height = (int) (height/heightToDivide);
        extraDeckControls.setLayoutParams(p);

        p = (ConstraintLayout.LayoutParams) deckControls.getLayoutParams();
        p.width = (int) width;
        p.height = (int) (height/heightToDivide);
        deckControls.setLayoutParams(p);

        p = (ConstraintLayout.LayoutParams) cardControls.getLayoutParams();
        p.width = (int) width;
        p.height = (int) (height/heightToDivide);
        cardControls.setLayoutParams(p);

        LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) banishFromExtraDeck.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = LinearLayout.LayoutParams.MATCH_PARENT;
        banishFromExtraDeck.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) banishFromMainDeck.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/3);
        banishFromMainDeck.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) drawFromMainDeck.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/3);
        drawFromMainDeck.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) millFromTop.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/3);
        millFromTop.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) toBanishButton.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        toBanishButton.setLayoutParams(par);
        toBanishButton.setVisibility(View.GONE);

        par = (LinearLayout.LayoutParams) changeControl.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        changeControl.setLayoutParams(par);
        changeControl.setVisibility(View.GONE);

        par = (LinearLayout.LayoutParams) toGraveButton.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        toGraveButton.setLayoutParams(par);
        toGraveButton.setVisibility(View.GONE);

        par = (LinearLayout.LayoutParams) toDeckButton.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        toDeckButton.setLayoutParams(par);
        toDeckButton.setVisibility(View.GONE);

        par = (LinearLayout.LayoutParams) toHandButton.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        toHandButton.setLayoutParams(par);
        toHandButton.setVisibility(View.GONE);

        par = (LinearLayout.LayoutParams) toEdeckButton.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        toEdeckButton.setLayoutParams(par);
        toEdeckButton.setVisibility(View.GONE);

        par = (LinearLayout.LayoutParams) autoSet.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        autoSet.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) cardFaceUpOrDown.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        cardFaceUpOrDown.setLayoutParams(par);

        autoSet.setTrackDrawable(scaleImage(getResources().getDrawable(R.drawable.switchtrack), 5));
        autoSet.setThumbDrawable(scaleImage(getResources().getDrawable(R.drawable.switchthumb), 6));

        par = (LinearLayout.LayoutParams) cardDefenseOrAttack.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        cardDefenseOrAttack.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) viewCard.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        viewCard.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) addCounter.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        addCounter.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) removeCounter.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        removeCounter.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) attackDec.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        attackDec.setLayoutParams(par);

        par = (LinearLayout.LayoutParams) viewAttached.getLayoutParams();
        par.height = LinearLayout.LayoutParams.MATCH_PARENT;
        par.width = (int) (width/7);
        viewAttached.setLayoutParams(par);

        try {

            params = (RelativeLayout.LayoutParams) yourDeck.getLayoutParams();
            params.leftMargin = (int) (width * (371.0/437.0));
            params.topMargin = (int) (width * (388.0/480.0));
            params.width = (int) (width * ((430.0-371.0)/437.0));
            params.height = (int) (width * ((473.0-388.0)/480.0));
            yourDeck.setLayoutParams(params);
            yourDeck.setScaleType(ImageView.ScaleType.FIT_XY);

            params = (RelativeLayout.LayoutParams) yourExtraDeck.getLayoutParams();
            params.leftMargin = (int) (width * (5.0/437.0));
            params.topMargin = (int) (width * (389.0/480.0));
            params.width = (int) (width * ((61.0-5.0)/437.0));
            params.height = (int) (width * ((476.0-389.0)/480.0));
            yourExtraDeck.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) yourGrave.getLayoutParams();
            params.leftMargin = (int) (width * (371.0/437.0));
            params.topMargin = (int) (width * (301.0/480.0));
            params.width = (int) (width * ((430.0-371.0)/437.0));
            params.height = (int) (width * ((382.0-301.0)/480.0));
            yourGrave.setLayoutParams(params);
            yourGrave.setScaleType(ImageView.ScaleType.FIT_XY);

            params = (RelativeLayout.LayoutParams) yourBanish.getLayoutParams();
            params.leftMargin = (int) (width * (371.0/437.0));
            params.topMargin = (int) (width * (204.0/480.0));
            params.width = (int) (width * ((430.0-371.0)/437.0));
            params.height = (int) (width * ((295.0-204.0)/480.0));
            yourBanish.setLayoutParams(params);


            params = (RelativeLayout.LayoutParams) opponentExtraDeck.getLayoutParams();
            params.leftMargin = (int) (width * (371.0/437.0));
            params.topMargin = (int) (width * (4.0/480.0));
            params.width = (int) (width * ((431.0-371.0)/437.0));
            params.height = (int) (width * ((96.0-4.0)/480.0));
            opponentExtraDeck.setLayoutParams(params);
            opponentExtraDeck.setScaleType(ImageView.ScaleType.FIT_XY);

            params = (RelativeLayout.LayoutParams) ((ImageView) findViewById(R.id.deck2)).getLayoutParams();
            params.leftMargin = (int) (width * (6.0/437.0));
            params.topMargin = (int) (width * (5.0/480.0));
            params.width = (int) (width * ((62.0-6.0)/437.0));
            params.height = (int) (width * ((96.0-5.0)/480.0));
            ((ImageView) findViewById(R.id.deck2)).setLayoutParams(params);
            ((ImageView) findViewById(R.id.deck2)).setScaleType(ImageView.ScaleType.FIT_XY);

            params = (RelativeLayout.LayoutParams) opponentGrave.getLayoutParams();
            params.leftMargin = (int) (width * (6.0/437.0));
            params.topMargin = (int) (width * (102.0/480.0));
            params.width = (int) (width * ((62.0-6.0)/437.0));
            params.height = (int) (width * ((198.0-102.0)/480.0));
            opponentGrave.setLayoutParams(params);
            opponentGrave.setScaleType(ImageView.ScaleType.FIT_XY);

            params = (RelativeLayout.LayoutParams) opponentBanish.getLayoutParams();
            params.leftMargin = (int) (width * (6.0/437.0));
            params.topMargin = (int) (width * (204.0/480.0));
            params.width = (int) (width * ((62.0-6.0)/437.0));
            params.height = (int) (width * ((295.0-204.0)/480.0));
            opponentBanish.setLayoutParams(params);
            opponentBanish.setScaleType(ImageView.ScaleType.FIT_XY);

            params = (RelativeLayout.LayoutParams) specialControlsButton.getLayoutParams();
            params.leftMargin = (int) (width * (186.0/437.0));
            params.topMargin = (int) (width * (204.0/480.0));
            params.width = (int) (width * ((240.0-186.0)/437.0));
            params.height = (int) (width * ((295.0-204.0)/480.0));
            specialControlsButton.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) yourHealth.getLayoutParams();
            params.leftMargin = (int) (width * (67.0/437.0));
            params.topMargin = (int) (width * (204.0/480.0));
            params.width = (int) (width * ((120.0-67.0)/437.0));
            params.height = (int) (width * ((295.0-204.0)/480.0));
            yourHealth.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) opponentHealth.getLayoutParams();
            params.leftMargin = (int) (width * (305.0/437.0));
            params.topMargin = (int) (width * (204.0/480.0));
            params.width = (int) (width * ((366.0-305.0)/437.0));
            params.height = (int) (width * ((295.0-204.0)/480.0));
            opponentHealth.setLayoutParams(params);


            HorizontalScrollView.LayoutParams handP = (HorizontalScrollView.LayoutParams) yourHand.getLayoutParams();
            handP.height = (int) (height/heightToDivide);
            handP.width = (int) width;
            yourHand.setLayoutParams(handP);

            HorizontalScrollView.LayoutParams handPO = (HorizontalScrollView.LayoutParams) opponentHand.getLayoutParams();
            handPO.height = (int) (height/heightToDivide);
            handPO.width = (int) (width);
            opponentHand.setLayoutParams(handPO);

        }catch (Exception e){
            gameExitCrash();
            ((TextView)findViewById(R.id.textView3)).setText(e.getMessage());

        }

    }

    public void promptSideConfirm(){

        sideConfirm.setVisibility(View.VISIBLE);

    }

    public void promptResetConfirm(){

        resetConfirm.setVisibility(View.VISIBLE);

    }

    public Drawable scaleImage (Drawable image, int scaleFactor) {

        try {
            if ((image == null) || !(image instanceof BitmapDrawable)) {
                return image;
            }

            Bitmap b = ((BitmapDrawable) image).getBitmap();
            int sizeX;
            if (scaleFactor == 5)
                sizeX = (int) (cardFaceUpOrDown.getLayoutParams().width * 0.86);
            else
                sizeX = (int) ((double) (cardFaceUpOrDown.getLayoutParams().width * 0.86) * (558.0 / 968.0));
            int sizeY = (int) (cardControls.getLayoutParams().height * 0.86);

            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

            image = new BitmapDrawable(getResources(), bitmapResized);

        }catch (Exception e){
            gameExitCrash();
            ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());

        }
        return image;

    }

    public void showCardOptionsOpponent(){

        changeControl.setVisibility(View.GONE);
        toBanishButton.setVisibility(View.GONE);
        toGraveButton.setVisibility(View.GONE);
        toDeckButton.setVisibility(View.GONE);
        toEdeckButton.setVisibility(View.GONE);
        toHandButton.setVisibility(View.GONE);
        extraDeckControls.setVisibility(View.GONE);
        deckControls.setVisibility(View.GONE);
        cardControls.setVisibility(View.VISIBLE);
        viewCard.setVisibility(View.VISIBLE);
        cardFaceUpOrDown.setVisibility(View.GONE);
        cardDefenseOrAttack.setVisibility(View.GONE);
        autoSet.setVisibility(View.GONE);
        addCounter.setVisibility(View.GONE);
        removeCounter.setVisibility(View.GONE);
        cardDefenseOrAttack.setVisibility(View.GONE);
        cardFaceUpOrDown.setVisibility(View.GONE);
        autoSet.setVisibility(View.GONE);
        attackDec.setVisibility(View.GONE);
        if(selectedCard != null && cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(fieldContentsOpponent.indexOf(selectedCard)))))
            viewAttached.setVisibility(View.VISIBLE);
        else
            viewAttached.setVisibility(View.GONE);

    }

    public void showCardOptionsToken(){

        viewAttached.setVisibility(View.GONE);
        changeControl.setVisibility(View.GONE);
        toBanishButton.setVisibility(View.GONE);
        toGraveButton.setVisibility(View.GONE);
        toDeckButton.setVisibility(View.GONE);
        toEdeckButton.setVisibility(View.GONE);
        toHandButton.setVisibility(View.GONE);
        extraDeckControls.setVisibility(View.GONE);
        deckControls.setVisibility(View.GONE);
        cardControls.setVisibility(View.VISIBLE);
        viewCard.setVisibility(View.VISIBLE);
        cardFaceUpOrDown.setVisibility(View.GONE);
        cardDefenseOrAttack.setVisibility(View.VISIBLE);
        autoSet.setVisibility(View.GONE);
        addCounter.setVisibility(View.VISIBLE);
        removeCounter.setVisibility(View.VISIBLE);
        if(selectedCard != null && selectedCard.getRotation() == 0f)
            attackDec.setVisibility(View.VISIBLE);
        else
            attackDec.setVisibility(View.GONE);
        viewAttached.setVisibility(View.GONE);

    }

    public void showCardOptions(){

        extraDeckControls.setVisibility(View.GONE);
        deckControls.setVisibility(View.GONE);
        cardControls.setVisibility(View.VISIBLE);
        viewAttached.setVisibility(View.GONE);

        switch(cardLocaion){

            case HAND:
                changeControl.setVisibility(View.GONE);
                viewCard.setVisibility(View.VISIBLE);
                cardFaceUpOrDown.setVisibility(View.GONE);
                cardDefenseOrAttack.setVisibility(View.GONE);
                autoSet.setVisibility(View.VISIBLE);
                addCounter.setVisibility(View.GONE);
                removeCounter.setVisibility(View.GONE);
                attackDec.setVisibility(View.GONE);
                if(useNonEssentialButtons) {
                    toHandButton.setVisibility(View.GONE);
                    toBanishButton.setVisibility(View.VISIBLE);
                    toGraveButton.setVisibility(View.VISIBLE);
                    toDeckButton.setVisibility(View.VISIBLE);
                    toEdeckButton.setVisibility(View.VISIBLE);
                }
                break;
            case FIELD:
                changeControl.setVisibility(View.VISIBLE);
                toBanishButton.setVisibility(View.GONE);
                toGraveButton.setVisibility(View.GONE);
                toDeckButton.setVisibility(View.GONE);
                toEdeckButton.setVisibility(View.GONE);
                toHandButton.setVisibility(View.GONE);
                viewCard.setVisibility(View.VISIBLE);
                cardFaceUpOrDown.setVisibility(View.VISIBLE);
                cardDefenseOrAttack.setVisibility(View.VISIBLE);
                autoSet.setVisibility(View.GONE);
                if(selectedCard != null && (selectedCard.getForeground() == null || fieldCounters.get(fieldContents.indexOf(selectedCard)) > 0)) {
                    addCounter.setVisibility(View.VISIBLE);
                    removeCounter.setVisibility(View.VISIBLE);
                }else{
                    addCounter.setVisibility(View.GONE);
                    removeCounter.setVisibility(View.GONE);
                }
                if(selectedCard != null && selectedCard.getRotation() == 0f && selectedCard.getForeground() == null)
                    attackDec.setVisibility(View.VISIBLE);
                else
                    attackDec.setVisibility(View.GONE);
                if(selectedCard != null && cardInfo.getXYZ(cardInfo.getName(fieldNames.get(fieldContents.indexOf(selectedCard)))))
                    viewAttached.setVisibility(View.VISIBLE);
                else
                    viewAttached.setVisibility(View.GONE);
                break;
            case GRAVE:
                changeControl.setVisibility(View.GONE);
                toGraveButton.setVisibility(View.GONE);
                viewCard.setVisibility(View.VISIBLE);
                cardFaceUpOrDown.setVisibility(View.GONE);
                cardDefenseOrAttack.setVisibility(View.GONE);
                autoSet.setVisibility(View.VISIBLE);
                addCounter.setVisibility(View.GONE);
                removeCounter.setVisibility(View.GONE);
                attackDec.setVisibility(View.GONE);
                viewAttached.setVisibility(View.GONE);
                if(useNonEssentialButtons) {
                    toBanishButton.setVisibility(View.VISIBLE);
                    toHandButton.setVisibility(View.VISIBLE);
                    toDeckButton.setVisibility(View.VISIBLE);
                    toEdeckButton.setVisibility(View.VISIBLE);
                }
                break;
            case BANISH:
                changeControl.setVisibility(View.GONE);
                toBanishButton.setVisibility(View.GONE);
                viewCard.setVisibility(View.VISIBLE);
                cardFaceUpOrDown.setVisibility(View.GONE);
                cardDefenseOrAttack.setVisibility(View.GONE);
                autoSet.setVisibility(View.VISIBLE);
                addCounter.setVisibility(View.GONE);
                removeCounter.setVisibility(View.GONE);
                attackDec.setVisibility(View.GONE);
                viewAttached.setVisibility(View.GONE);
                if(useNonEssentialButtons) {
                    toGraveButton.setVisibility(View.VISIBLE);
                    toHandButton.setVisibility(View.VISIBLE);
                    toDeckButton.setVisibility(View.VISIBLE);
                    toEdeckButton.setVisibility(View.VISIBLE);
                }
                break;
            case EDECK:
                changeControl.setVisibility(View.GONE);
                toEdeckButton.setVisibility(View.GONE);
                viewCard.setVisibility(View.VISIBLE);
                cardFaceUpOrDown.setVisibility(View.GONE);
                cardDefenseOrAttack.setVisibility(View.GONE);
                autoSet.setVisibility(View.VISIBLE);
                addCounter.setVisibility(View.GONE);
                removeCounter.setVisibility(View.GONE);
                attackDec.setVisibility(View.GONE);
                viewAttached.setVisibility(View.GONE);
                if(useNonEssentialButtons) {
                    toBanishButton.setVisibility(View.VISIBLE);
                    toHandButton.setVisibility(View.VISIBLE);
                    toDeckButton.setVisibility(View.VISIBLE);
                    toGraveButton.setVisibility(View.VISIBLE);
                }
                break;
            case DECK:
                changeControl.setVisibility(View.GONE);
                toDeckButton.setVisibility(View.GONE);
                viewCard.setVisibility(View.VISIBLE);
                cardFaceUpOrDown.setVisibility(View.GONE);
                cardDefenseOrAttack.setVisibility(View.GONE);
                autoSet.setVisibility(View.VISIBLE);
                addCounter.setVisibility(View.GONE);
                removeCounter.setVisibility(View.GONE);
                attackDec.setVisibility(View.GONE);
                viewAttached.setVisibility(View.GONE);
                if(useNonEssentialButtons) {
                    toBanishButton.setVisibility(View.VISIBLE);
                    toHandButton.setVisibility(View.VISIBLE);
                    toGraveButton.setVisibility(View.VISIBLE);
                    toEdeckButton.setVisibility(View.VISIBLE);
                }
                break;

        }
        //cardDefenseOrAttack.setVisibility(View.VISIBLE);
        //cardFaceUpOrDown.setVisibility(View.VISIBLE);
        //autoSet.setVisibility(View.VISIBLE);

    }

    public void clearAllOptions(){

        //opponentRevealControlD.setVisibility(View.INVISIBLE);
        //opponentRevealControlsE.setVisibility(View.INVISIBLE);
        extraDeckControls.setVisibility(View.INVISIBLE);
        deckControls.setVisibility(View.INVISIBLE);
        cardControls.setVisibility(View.INVISIBLE);
        cardDefenseOrAttack.setVisibility(View.INVISIBLE);
        cardFaceUpOrDown.setVisibility(View.INVISIBLE);
        autoSet.setVisibility(View.INVISIBLE);

    }

    public void showHand(){

        attachControls.setVisibility(View.INVISIBLE);
        opponentRevealControlD.setVisibility(View.INVISIBLE);
        opponentRevealControlsE.setVisibility(View.INVISIBLE);
        extraDeckControls.setVisibility(View.INVISIBLE);
        deckControls.setVisibility(View.INVISIBLE);
        cardControls.setVisibility(View.INVISIBLE);

    }

    public void showDeckOptions(){

        extraDeckControls.setVisibility(View.INVISIBLE);
        deckControls.setVisibility(View.VISIBLE);
        cardControls.setVisibility(View.INVISIBLE);

    }

    public void showExtraDeckOptions(){

        extraDeckControls.setVisibility(View.VISIBLE);
        deckControls.setVisibility(View.INVISIBLE);
        cardControls.setVisibility(View.INVISIBLE);

    }

    public void clearForDrag(){

        showAttached.setVisibility(View.INVISIBLE);
        showScroll.setVisibility(View.INVISIBLE);
        findViewById(R.id.messagebutton).setVisibility(View.VISIBLE);
        extraOptions.setVisibility(View.INVISIBLE);
        dieResults.setVisibility(View.INVISIBLE);
        yourDeckContents.setVisibility(View.INVISIBLE);
        yourExtraDeckContents.setVisibility(View.INVISIBLE);
        yourBanishContents.setVisibility(View.INVISIBLE);
        opponentGraveContents.setVisibility(View.INVISIBLE);
        opponentBanishContents.setVisibility(View.INVISIBLE);
        deckScroll.setVisibility(View.INVISIBLE);
        graveScroll.setVisibility(View.INVISIBLE);
        banishScroll.setVisibility(View.INVISIBLE);
        extraDeckScroll.setVisibility(View.INVISIBLE);
        opponentGraveScroll.setVisibility(View.INVISIBLE);
        opponentBanishScroll.setVisibility(View.INVISIBLE);
        opponentExtraDeckScroll.setVisibility(View.INVISIBLE);
        opponentExtraDeckContents.setVisibility(View.INVISIBLE);

        clearAllOptions();


    }

    public void drawCardFromDeck(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    if(deckContents.size() > 0 && deckNames.size() > 0) {

                        final ImageView drawnCard = deckContents.get(0);
                        String drawnCardName = deckNames.get(0);

                        yourDeckContents.removeViewAt(0);

                        handContents.add(drawnCard);
                        yourHand.addView(drawnCard);
                        handNames.add(drawnCardName);

                        drawnCard.setVisibility(View.INVISIBLE);

                        deckContents.remove(0);
                        deckNames.remove(0);

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(drawnCard.getDrawable());
                        tempAni.setForeground(drawnCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
                        ncardPositionParameters.topMargin = aniCover.getMeasuredHeight() - (ncardPositionParameters.height + yourHand.getLayoutParams().height);
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo((int) (aniCover.getMeasuredWidth() / 2.0) - (ncardPositionParameters.width / 2), aniCover.getMeasuredHeight() - ncardPositionParameters.height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                drawnCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else{

                        AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                        prompt.setTitle("Your deck is empty!");
                        prompt.setNegativeButton("OK", null);
                        prompt.show();

                    }

                }catch (Exception e){
                    gameExitCrash();
                    ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());

                }

            }
        });



    }

    public void addCardToOpponentED(String sel){

        ImageView cardToAdd = createPlaceHolderCardED();

        opponentExtraDeckContents.addView(cardToAdd);
        extraDeckContentsOpponent.add(cardToAdd);
        extraDeckNamesOpponent.add("PLACEHOLDER");

        if(sel.equals("S")){

            if (opSelectedCard != null)
                opSelectedCard.setColorFilter(null);
            cardToAdd.setColorFilter(Color.argb(60, 255, 0, 0));
            opSelectedCard = cardToAdd;

        }

    }

    public void addCardToOpponentHand(String sel){

        ImageView cardToAdd = createPlaceHolderCard();

        opponentHand.addView(cardToAdd);
        handContentsOpponent.add(cardToAdd);
        handNamesOpponent.add("PLACEHOLDER");

        if(sel.equals("S")){

            if (opSelectedCard != null)
                opSelectedCard.setColorFilter(null);
            cardToAdd.setColorFilter(Color.argb(60, 255, 0, 0));
            opSelectedCard = cardToAdd;

        }

        cardToAdd.setVisibility(View.INVISIBLE);

        if(prevLocAni == FIELD){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(cardToAdd.getDrawable());
            tempAni.setRotation(cardToAdd.getRotation());
            tempAni.setForeground(cardToAdd.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = prevXAni;
            ncardPositionParameters.topMargin = prevYAni + opponentHand.getLayoutParams().height;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo((gameField.getMeasuredWidth() / 2) - (ncardPositionParameters.width / 2), 0);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    cardToAdd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

        }else if(prevLocAni == GRAVE || prevLocAni == BANISH || prevLocAni == EDECK){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(cardToAdd.getDrawable());
            tempAni.setRotation(cardToAdd.getRotation());
            tempAni.setForeground(cardToAdd.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = prevXAni;
            ncardPositionParameters.topMargin = prevYAni;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo((gameField.getMeasuredWidth() / 2) - (ncardPositionParameters.width / 2), 0);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    cardToAdd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

        }else if(prevLocAni == -1) {

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(cardToAdd.getDrawable());
            tempAni.setRotation(cardToAdd.getRotation());
            tempAni.setForeground(cardToAdd.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = 0;
            ncardPositionParameters.topMargin = opponentHand.getMeasuredHeight();
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo((gameField.getMeasuredWidth() / 2) - (ncardPositionParameters.width / 2), 0);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    cardToAdd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

        }else
            cardToAdd.setVisibility(View.VISIBLE);

        prevLocAni = -1;



    }

    public void addFaceDownBanishedCardToOpponent(char loc){

        ImageView cardToAdd = createPlaceHolderCard();

        opponentBanishContents.addView(cardToAdd);
        banishContentsOpponent.add(cardToAdd);
        banishNamesOpponent.add("PLACEHOLDER");
        opponentBanish.setImageDrawable(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());


        cardToAdd.setVisibility(View.INVISIBLE);

        if(loc == 'D'){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(cardToAdd.getDrawable());
            tempAni.setRotation(cardToAdd.getRotation());
            tempAni.setForeground(cardToAdd.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = 0;
            ncardPositionParameters.topMargin = opponentHand.getMeasuredHeight();
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height + opponentGrave.getLayoutParams().height);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    cardToAdd.setVisibility(View.VISIBLE);
                    opponentBanish.setAlpha(1.0f);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

        }else{

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(cardToAdd.getDrawable());
            tempAni.setRotation(cardToAdd.getRotation());
            tempAni.setForeground(cardToAdd.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = aniCover.getMeasuredWidth() - ncardPositionParameters.width;
            ncardPositionParameters.topMargin = opponentHand.getMeasuredHeight();
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height + opponentGrave.getLayoutParams().height);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    cardToAdd.setVisibility(View.VISIBLE);
                    opponentBanish.setAlpha(1.0f);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

        }

    }

    public void removeCardFromOpponentHand(int indexToRemove, char des){

        prevLocAni = HAND;
        prevXAni = (aniCover.getMeasuredWidth() / 2);
        prevYAni = 0;

        ImageView rem = handContentsOpponent.get(indexToRemove);

        if(handContentsOpponent.get(indexToRemove).equals(opSelectedCard))
            opSelectedCard = null;

        handContentsOpponent.remove(indexToRemove);
        handNamesOpponent.remove(indexToRemove);
        opponentHand.removeViewAt(indexToRemove);

        if(des == 'D'){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(rem.getDrawable());
            tempAni.setRotation(rem.getRotation());
            tempAni.setForeground(rem.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = prevXAni;
            ncardPositionParameters.topMargin = prevYAni;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo(0, opponentHand.getLayoutParams().height);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    rem.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

            prevLocAni = -1;

        }

    }

    public void removeCardFromOpponentDeck(int indexToRemove){

        deckContentsOpponent.remove(indexToRemove);
        deckNamesOpponent.remove(indexToRemove);

    }

    public void removeCardFromOpponentGrave(int indexToRemove, char des){

        prevLocAni = GRAVE;
        prevXAni = 0;
        prevYAni = opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height;

        ImageView rem = graveContentsOpponent.get(indexToRemove);

        graveContentsOpponent.remove(indexToRemove);
        graveyardNamesOpponent.remove(indexToRemove);
        opponentGraveContents.removeViewAt(indexToRemove);

        if(graveContentsOpponent.size() > 0)
            opponentGrave.setBackground(graveContentsOpponent.get(graveContentsOpponent.size() - 1).getDrawable().getConstantState().newDrawable());
        else
            opponentGrave.setAlpha(0.1f);

        if(des == 'D'){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(rem.getDrawable());
            tempAni.setRotation(rem.getRotation());
            tempAni.setForeground(rem.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = prevXAni;
            ncardPositionParameters.topMargin = prevYAni;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo(0, opponentHand.getLayoutParams().height);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    rem.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

            prevLocAni = -1;

        }

    }

    public void removeCardFromOpponentBanish(int indexToRemove, char des){

        prevLocAni = BANISH;
        prevXAni = 0;
        prevYAni = opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height + opponentGrave.getLayoutParams().height;

        ImageView rem = banishContentsOpponent.get(indexToRemove);

        banishContentsOpponent.remove(indexToRemove);
        banishNamesOpponent.remove(indexToRemove);
        opponentBanishContents.removeViewAt(indexToRemove);

        if(banishContentsOpponent.size() > 0)
            opponentBanish.setBackground(banishContentsOpponent.get(banishContentsOpponent.size() - 1).getDrawable().getConstantState().newDrawable());
        else
            opponentBanish.setAlpha(0.1f);

        if(des == 'D'){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(rem.getDrawable());
            tempAni.setRotation(rem.getRotation());
            tempAni.setForeground(rem.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = prevXAni;
            ncardPositionParameters.topMargin = prevYAni;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo(0, opponentHand.getLayoutParams().height);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    rem.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

            prevLocAni = -1;

        }

    }

    public void removeCardFromOpponentExtraDeck(int indexToRemove, char des){

        prevLocAni = EDECK;
        prevXAni = aniCover.getMeasuredHeight() - opponentExtraDeck.getMeasuredWidth();
        prevYAni = opponentHand.getLayoutParams().height;

        ImageView rem = extraDeckContentsOpponent.get(indexToRemove);

        extraDeckContentsOpponent.remove(indexToRemove);
        extraDeckNamesOpponent.remove(indexToRemove);
        opponentExtraDeckContents.removeViewAt(indexToRemove);

        if(extraDeckContentsOpponent.size() > 0)
            opponentExtraDeck.setBackground(extraDeckContentsOpponent.get(extraDeckContentsOpponent.size() - 1).getDrawable().getConstantState().newDrawable());
        else
            opponentExtraDeck.setAlpha(0.1f);

        if(des == 'D'){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(rem.getDrawable());
            tempAni.setRotation(rem.getRotation());
            tempAni.setForeground(rem.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = prevXAni;
            ncardPositionParameters.topMargin = prevYAni;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo(0, opponentHand.getLayoutParams().height);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    rem.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

            prevLocAni = -1;

        }

    }

    public void removeCardFromOpponentField(int indexToRemove, char des){


        prevLocAni = FIELD;
        prevXAni = ((RelativeLayout.LayoutParams) fieldContentsOpponent.get(indexToRemove).getLayoutParams()).leftMargin;
        prevYAni = ((RelativeLayout.LayoutParams) fieldContentsOpponent.get(indexToRemove).getLayoutParams()).topMargin + findViewById(R.id.handcont2).getLayoutParams().height;
        prevXYZ = new int[2];
        fieldContentsOpponent.get(indexToRemove).getLocationOnScreen(prevXYZ);

        ImageView rem = fieldContentsOpponent.get(indexToRemove);

        if(rem.equals(opSelectedCard))
            opSelectedCard = null;

        gameField.removeView(fieldContentsOpponent.get(indexToRemove));
        fieldContentsOpponent.remove(indexToRemove);
        fieldNamesOpponent.remove(indexToRemove);
        fieldCountersOpponent.remove(indexToRemove);

        if(des == 'D'){

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(rem.getDrawable());
            tempAni.setRotation(rem.getRotation());
            tempAni.setForeground(rem.getForeground());
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = prevXAni;
            ncardPositionParameters.topMargin = prevYAni + opponentHand.getLayoutParams().height;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo(0, opponentHand.getLayoutParams().height);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(200);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    rem.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

            prevLocAni = -1;

        }

    }

    public void updateNumbers(){

        mainCount.setText("M: " + mainDeckList.size());
        extraCount.setText("E: " + extraDeckList.size());
        sideCount.setText("S: " + sideDeckList.size());

    }

    public void gameSide(){

        selectedCard = null;
        opSelectedCard = null;
        setContentView(R.layout.sideing_layout);
        findViewById(R.id.covsid).setForeground(getResources().getDrawable(R.drawable.loadingscreen));
        finishSiding = findViewById(R.id.finishSiding);
        sideToMain = findViewById(R.id.toMainS);
        sideToExtra = findViewById(R.id.toExtraS);
        //sideToSide = findViewById(R.id.toSideS);
        mainDeckSide = findViewById(R.id.mainDeckS);
        sideDeckSide = findViewById(R.id.siededeckS);
        extraDeckSide = findViewById(R.id.extradeckS);
        previewCardSide = findViewById(R.id.prevSide);
        mainCount = findViewById(R.id.mainCoS);
        extraCount = findViewById(R.id.extraCoS);
        sideCount = findViewById(R.id.sideCoS);
        heightForSiding = ((((height / width) - 1.0) * 4.0) + 5) * (10/7);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainDeckSide.getLayoutParams();
        params.height = (int) ((height/(heightToDivide * (10/7))) * 6);
        mainDeckSide.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) sideDeckSide.getLayoutParams();
        params.height = (int) ((height/(heightToDivide * (10/7))) * 2);
        sideDeckSide.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) extraDeckSide.getLayoutParams();
        params.height = (int) ((height/(heightToDivide * (10/7))) * 2);
        extraDeckSide.setLayoutParams(params);

        mainDeckSide.setVerticalSpacing(0);
        sideDeckSide.setVerticalSpacing(0);
        extraDeckSide.setVerticalSpacing(0);

        mainDeckList.clear();
        extraDeckList.clear();
        sideDeckList.clear();

        findViewById(R.id.sidcovr).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        for (int i = 0; i < resetMainDeckNameList.size(); i++) {

            new RetrieveImageMain().execute(cardImageServerAdress2 + urlConverter.formatURL(resetMainDeckNameList.get(i)) + ".jpg");

        }

        for (int i = 0; i < resetExtraDeckNameList.size(); i++) {

            new RetrieveImageExtra().execute(cardImageServerAdress2 + urlConverter.formatURL(resetExtraDeckNameList.get(i)) + ".jpg");

        }

        for (int i = 0; i < sideDeckNameList.size(); i++) {

            new RetrieveImageSide().execute(cardImageServerAdress2 + urlConverter.formatURL(sideDeckNameList.get(i)) + ".jpg");

        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(sideDeckList.size() != sideDeckNameList.size() || mainDeckList.size() != resetMainDeckNameList.size() || extraDeckList.size() != resetExtraDeckNameList.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.covsid).setForeground(null);
                        findViewById(R.id.sidcovr).setVisibility(View.GONE);
                    }
                });

            }
        }).start();
        //Do siding addition

        finishSiding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resetMainDeckNameList.size() == mainDeckSize && resetExtraDeckNameList.size() == extraDeckSize) {

                    findViewById(R.id.sidcovr).setVisibility(View.VISIBLE);

                    setContentView(R.layout.game_layout);
                    defineVariables();
                    gameReset();
                    startFunctionality();

                }else{

                    AlertDialog.Builder prompt = new AlertDialog.Builder(con);
                    prompt.setTitle("Card Amounts Must Be Same As Original");
                    prompt.setNegativeButton("OK", null);
                    prompt.show();

                }

            }

        });

        previewCardSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewCardSide.setVisibility(View.GONE);
                finishSiding.setVisibility(View.VISIBLE);
                mainCount.setVisibility(View.VISIBLE);
                sideCount.setVisibility(View.VISIBLE);
                extraCount.setVisibility(View.VISIBLE);
            }
        });

        sideToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedSide != null && cardInfo.getType(cardInfo.getName(sideDeckNameList.get(selectedIndex))).equals("M")){
                    //final ImageView temp = new ImageView(c);
                    //temp.setImageDrawable(selected);
                    mainDeckList.add(selectedSide);
                    resetMainDeckNameList.add(sideDeckNameList.get(selectedIndex));
                    sideDeckList.remove(selectedSide);
                    sideDeckNameList.remove(selectedIndex);
                    mainDeckSide.setAdapter(new CardImageAdapter(con, mainDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                    sideDeckSide.setAdapter(new CardImageAdapter(con, sideDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                    updateNumbers();
                    selectedSide = null;

                }
            }
        });
        sideToExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSide != null && !cardInfo.getType(cardInfo.getName(sideDeckNameList.get(selectedIndex))).equals("M")){
                    //final ImageView temp = new ImageView(c);
                    //temp.setImageDrawable(selected);
                    extraDeckList.add(selectedSide);
                    resetExtraDeckNameList.add(sideDeckNameList.get(selectedIndex));
                    sideDeckList.remove(selectedSide);
                    sideDeckNameList.remove(selectedIndex);
                    extraDeckSide.setAdapter(new CardImageAdapter(con, extraDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                    sideDeckSide.setAdapter(new CardImageAdapter(con, sideDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                    updateNumbers();
                    selectedSide = null;
                }
            }
        });
        mainDeckSide.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,int position, long id) {

                try {

                    new ShowCardPreview().execute(cardImageServerAdress + urlConverter.formatURL(resetMainDeckNameList.get(position)) + ".jpg");

                }catch (Exception e){

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
        mainDeckSide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainDeckSide.requestFocus();
                sideDeckList.add(mainDeckList.get(i));
                sideDeckNameList.add(resetMainDeckNameList.get(i));
                mainDeckList.remove(i);
                resetMainDeckNameList.remove(i);
                mainDeckSide.setAdapter(new CardImageAdapter(con, mainDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                sideDeckSide.setAdapter(new CardImageAdapter(con, sideDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                updateNumbers();
            }
        });
        extraDeckSide.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,int position, long id) {

                new ShowCardPreview().execute(cardImageServerAdress + urlConverter.formatURL(resetExtraDeckNameList.get(position)) + ".jpg");

                /*prev.setImageDrawable(extraDeckList.get(position));
                prev.setVisibility(View.VISIBLE);
                mDeckExitButton.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                findViewById(R.id.button5).setVisibility(View.GONE);*/
                return true;
            }
        });
        extraDeckSide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                extraDeckSide.requestFocus();
                sideDeckList.add(extraDeckList.get(i));
                sideDeckNameList.add(resetExtraDeckNameList.get(i));
                extraDeckList.remove(i);
                resetExtraDeckNameList.remove(i);
                extraDeckSide.setAdapter(new CardImageAdapter(con, extraDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                sideDeckSide.setAdapter(new CardImageAdapter(con, sideDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
                updateNumbers();
            }
        });
        sideDeckSide.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,int position, long id) {

                new ShowCardPreview().execute(cardImageServerAdress + urlConverter.formatURL(sideDeckNameList.get(position)) + ".jpg");

                /*prev.setImageDrawable(sideDeckList.get(position));
                prev.setVisibility(View.VISIBLE);
                mDeckExitButton.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                findViewById(R.id.button5).setVisibility(View.GONE);*/
                return true;
            }
        });
        sideDeckSide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(selectedSide != null)
                    sideDeckSide.getChildAt(selectedIndex).setForeground(null);

                selectedIndex = i;
                selectedSide = sideDeckList.get(i);

                sideDeckSide.getChildAt(i).setForeground(getResources().getDrawable(R.drawable.highlight).mutate());

            }
        });

    }

    private class putToExtraDeck extends AsyncTask<String, Void, Object[]>{

        @Override
        protected Object[] doInBackground(String... strings) {

            InputStream cardImageDataStream;
            Drawable cardImage;
            String cardName;
            Object[] returnValues = new Object[2];

            try {

                cardName = urlConverter.formatURLReverse(strings[0].substring(cardImageServerAdress2.length(), strings[0].lastIndexOf('.')));
                if(!cardMap.containsKey(cardName)) {
                    cardImageDataStream = (InputStream) new URL(strings[0]).getContent();
                    cardImage = Drawable.createFromStream(cardImageDataStream, "src name");
                    cardMap.put(cardName, cardImage.getConstantState().newDrawable().mutate());
                }else
                    cardImage = cardMap.get(cardName).getConstantState().newDrawable().mutate();

                returnValues[0] = cardImage;
                returnValues[1] = cardName;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnValues;

        }

        @Override
        protected void onPostExecute(Object[] obj) {

            final Object[] objects = obj;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ImageView newCreatedCard = new ImageView(con);

                    newCreatedCard.setImageDrawable((Drawable) objects[0]);

                    newCreatedCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ColorMatrix cm = new ColorMatrix();
                                    ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
                                    cm.setRotate(0, 30f);

                                    if(yourGrave.getBackground() != null)
                                        yourGrave.getBackground().setColorFilter(null);
                                    yourBanish.setColorFilter(null);
                                    if(selectedCard != null) {
                                        if(selectedCard.getForeground() == null)
                                            selectedCard.setColorFilter(null);
                                        else {
                                            selectedCard.getForeground().setColorFilter(null);
                                            selectedCard.setColorFilter(null);
                                            selectedCard.invalidate();
                                        }
                                        selectedCard.invalidate();
                                    }
                                    if(newCreatedCard.getForeground() == null  || (newCreatedCard.getParent().equals(gameField) && fieldCounters.get(fieldContents.indexOf(newCreatedCard)) > 0))
                                        newCreatedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                    else
                                        newCreatedCard.getForeground().setColorFilter(cf);


                                    newCreatedCard.invalidate();

                                }
                            });

                            selectedCard = newCreatedCard;

                            if(selectedCard.getParent().equals(yourDeckContents))
                                cardLocaion = DECK;
                            else if(selectedCard.getParent().equals(yourHand))
                                cardLocaion = HAND;
                            else if(selectedCard.getParent().equals(gameField))
                                cardLocaion = FIELD;
                            else if(selectedCard.getParent().equals(yourExtraDeckContents))
                                cardLocaion = EDECK;
                            else if(selectedCard.getParent().equals(yourGraveContents))
                                cardLocaion = GRAVE;
                            else if(selectedCard.getParent().equals(yourBanishContents))
                                cardLocaion = BANISH;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(cardLocaion == GRAVE)
                                        yourGrave.setColorFilter(Color.argb(60, 0, 255, 0));
                                    else if(cardLocaion == BANISH)
                                        yourBanish.setColorFilter(Color.argb(60, 0, 255, 0));
                                }
                            });



                            showCardOptions();

                        }
                    });

                    newCreatedCard.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            ClipData.Item item;
                            String[] mimeTypes;
                            ClipData data;
                            View.DragShadowBuilder dragshadow;

                            if (autoSet.isChecked())
                                newCreatedCard.setForeground(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());

                            item = new ClipData.Item((CharSequence) view.getTag());
                            mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                            data = new ClipData((CharSequence) view.getTag(), mimeTypes, item);
                            dragshadow = new View.DragShadowBuilder(view);

                            view.startDrag(data, dragshadow, view, 0);
                            clearForDrag();

                            if(view.getParent().equals(yourDeckContents))
                                cardLocaion = DECK;
                            else if(view.getParent().equals(yourHand))
                                cardLocaion = HAND;
                            else if(view.getParent().equals(gameField))
                                cardLocaion = FIELD;
                            else if(view.getParent().equals(yourExtraDeckContents))
                                cardLocaion = EDECK;
                            else if(view.getParent().equals(yourGraveContents))
                                cardLocaion = GRAVE;
                            else if(view.getParent().equals(yourBanishContents))
                                cardLocaion = BANISH;

                            return true;
                        }
                    });

                    yourExtraDeckContents.addView(newCreatedCard);
                    extraDeckContents.add(newCreatedCard);
                    extraDeckNames.add((String) objects[1]);

                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) newCreatedCard.getLayoutParams();
                    params.width = (int) (width/7);
                    params.height = (int)(height/heightToDivide);
                    newCreatedCard.setLayoutParams(params);

                }
            });

        }


    }

    private class putToDeck extends AsyncTask<String, Void, Object[]>{

        @Override
        protected Object[] doInBackground(String... strings) {

            InputStream cardImageDataStream;
            Drawable cardImage;
            String cardName;
            Object[] returnValues = new Object[2];

            try {

                cardName = urlConverter.formatURLReverse(strings[0].substring(cardImageServerAdress2.length(), strings[0].lastIndexOf('.')));
                if(!cardMap.containsKey(cardName)) {
                    cardImageDataStream = (InputStream) new URL(strings[0]).getContent();
                    cardImage = Drawable.createFromStream(cardImageDataStream, "src name");
                    cardMap.put(cardName, cardImage.getConstantState().newDrawable().mutate());
                }else
                    cardImage = cardMap.get(cardName).getConstantState().newDrawable().mutate();

                returnValues[0] = cardImage;
                returnValues[1] = cardName;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnValues;

        }

        @Override
        protected void onPostExecute(Object[] obj) {

            final Object[] objects = obj;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ImageView newCreatedCard = new ImageView(con);

                    newCreatedCard.setImageDrawable((Drawable) objects[0]);
                    //newCreatedCard.setScaleType(ImageView.ScaleType.FIT_XY);

                    newCreatedCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ColorMatrix cm = new ColorMatrix();
                                    ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
                                    cm.setRotate(0, 30f);

                                    if(yourGrave.getBackground() != null)
                                        yourGrave.getBackground().setColorFilter(null);
                                    yourBanish.setColorFilter(null);
                                    if(selectedCard != null) {
                                        if(selectedCard.getForeground() == null)
                                            selectedCard.setColorFilter(null);
                                        else {
                                            selectedCard.getForeground().setColorFilter(null);
                                            selectedCard.setColorFilter(null);
                                            selectedCard.invalidate();
                                        }
                                    }
                                    if(newCreatedCard.getForeground() == null || (newCreatedCard.getParent().equals(gameField) && fieldCounters.get(fieldContents.indexOf(newCreatedCard)) > 0))
                                        newCreatedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                    else
                                        newCreatedCard.getForeground().setColorFilter(Color.argb(125, 0, 255, 0), PorterDuff.Mode.OVERLAY);

                                    newCreatedCard.invalidate();
                                }
                            });

                            selectedCard = newCreatedCard;

                            if(selectedCard.getParent().equals(yourDeckContents))
                                cardLocaion = DECK;
                            else if(selectedCard.getParent().equals(yourHand))
                                cardLocaion = HAND;
                            else if(selectedCard.getParent().equals(gameField))
                                cardLocaion = FIELD;
                            else if(selectedCard.getParent().equals(yourExtraDeckContents))
                                cardLocaion = EDECK;
                            else if(selectedCard.getParent().equals(yourGraveContents))
                                cardLocaion = GRAVE;
                            else if(selectedCard.getParent().equals(yourBanishContents))
                                cardLocaion = BANISH;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(cardLocaion == GRAVE)
                                        yourGrave.setBackground(selectedCard.getDrawable().getConstantState().newDrawable());
                                    else if(cardLocaion == BANISH)
                                        yourBanish.setImageDrawable(selectedCard.getDrawable().getConstantState().newDrawable());
                                }
                            });



                            showCardOptions();

                        }
                    });

                    newCreatedCard.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            ClipData.Item item;
                            String[] mimeTypes;
                            ClipData data;
                            View.DragShadowBuilder dragshadow;

                            if (autoSet.isChecked())
                                newCreatedCard.setForeground(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());

                            item = new ClipData.Item((CharSequence) view.getTag());
                            mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                            data = new ClipData((CharSequence) view.getTag(), mimeTypes, item);
                            dragshadow = new View.DragShadowBuilder(view);

                            view.startDrag(data, dragshadow, view, 0);
                            clearForDrag();

                            if(view.getParent().equals(yourDeckContents))
                                cardLocaion = DECK;
                            else if(view.getParent().equals(yourHand))
                                cardLocaion = HAND;
                            else if(view.getParent().equals(gameField))
                                cardLocaion = FIELD;
                            else if(view.getParent().equals(yourExtraDeckContents))
                                cardLocaion = EDECK;
                            else if(view.getParent().equals(yourGraveContents))
                                cardLocaion = GRAVE;
                            else if(view.getParent().equals(yourBanishContents))
                                cardLocaion = BANISH;

                            return true;
                        }
                    });

                    //yourDeckContents.addView(newCreatedCard);
                    deckContents.add(newCreatedCard);
                    deckNames.add((String) objects[1]);

                    if(deckContents.size() == mainDeckSize){

                        shuffleDeck();
                        /*for(int i = 0; i < 5; i++){
                            new drawHandler().execute();
                        }*/
                        deckControls.setVisibility(View.VISIBLE);


                        findViewById(R.id.gamecover).setForeground(null);
                        findViewById(R.id.coverforgame).setVisibility(View.GONE);


                    }

                    //if(yourDeckContents.getChildCount() == )

                }
            });

        }


    }

    public void makeAttack(String[] params){

        try {

            double xt = aniCover.getMeasuredWidth() * (1.0 - Double.parseDouble(params[0]));
            double yt = aniCover.getMeasuredHeight() * (1.0 - Double.parseDouble(params[1]));
            double xo = aniCover.getMeasuredWidth() * (1.0 - Double.parseDouble(params[2]));
            double yo = aniCover.getMeasuredHeight() * (1.0 - Double.parseDouble(params[3]));
            int ind = Integer.parseInt(params[4]);

            while(ind >= gameField.getChildCount());

            ImageView sc = (ImageView) gameField.getChildAt(ind);

            ImageView tempAni = new ImageView(con);
            tempAni.setImageDrawable(sc.getDrawable());
            tempAni.setForeground(sc.getForeground());
            tempAni.setRotation(180f);
            aniCover.addView(tempAni);
            RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
            ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
            ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
            ncardPositionParameters.leftMargin = (int) xo - ncardPositionParameters.width;
            ncardPositionParameters.topMargin = (int) yo - ncardPositionParameters.height;
            tempAni.setLayoutParams(ncardPositionParameters);
            tempAni.setVisibility(View.VISIBLE);
            Path path = new Path();
            path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            path.lineTo((float) (xt - ncardPositionParameters.width), (float) (yt - ncardPositionParameters.height));
            path.lineTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
            ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
            ani.setDuration(800);
            sc.setVisibility(View.INVISIBLE);
            ani.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    aniCover.removeView(tempAni);
                    sc.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ani.start();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    public boolean isViewOverlappingVals(View firstView, int[] b, View secondView, boolean r) {

        int[] cords1 = new int[2];
        int[] cords2 = new int[2];

        if(firstView.getVisibility() == View.INVISIBLE)
            return false;

        firstView.getLocationOnScreen(cords1);
        cords2[0] = b[0];
        cords2[1] = b[1];

        Rect card1 = new Rect();
        Rect card2 = new Rect();

        if(firstView.getRotation() == 90f)
            cords1[0] -= (firstView.getMeasuredHeight() * 0.5 + firstView.getMeasuredWidth() * 0.5);
        else if(firstView.getRotation() == 270f)
            cords1[0] += (firstView.getMeasuredHeight() - firstView.getMeasuredWidth() * 0.5);

        if(secondView.getRotation() == 90f)
            cords2[0] -= (secondView.getMeasuredHeight() * 0.5 + secondView.getMeasuredWidth() * 0.5);
        else if(secondView.getRotation() == 270f)
            cords2[0] += (secondView.getMeasuredHeight() - secondView.getMeasuredWidth() * 0.5);

        cords1[0] += firstView.getMeasuredWidth() * 0.25;
        cords2[0] += secondView.getMeasuredWidth() * 0.25;
        cords1[1] += firstView.getMeasuredHeight() * 0.25;
        cords2[1] += secondView.getMeasuredHeight() * 0.25;

        card1.set(cords1[0], cords1[1], (int) (cords1[0] + (firstView.getMeasuredWidth() * 0.5)), (int) (cords1[1] + (firstView.getMeasuredHeight() * 0.5)));
        card2.set(cords2[0], cords2[1], (int) (cords2[0] + (secondView.getMeasuredWidth() * 0.5)), (int) (cords2[1] + (secondView.getMeasuredHeight() * 0.5)));

        Log.d("Loc", cords1[0] + "  " + cords2[0] + " " + cords1[1] + " " + cords2[1]);

        return Rect.intersects(card1, card2);

    }

    public boolean isViewOverlapping(View firstView, View secondView) {

        int[] cords1 = new int[2];
        int[] cords2 = new int[2];

        if(firstView.getVisibility() == View.INVISIBLE || secondView.getVisibility() == View.INVISIBLE)
            return false;

        firstView.getLocationOnScreen(cords1);
        secondView.getLocationOnScreen(cords2);

        Rect card1 = new Rect();
        Rect card2 = new Rect();

        if(firstView.getRotation() == 90f)
            cords1[0] -= (firstView.getMeasuredHeight() * 0.5 + firstView.getMeasuredWidth() * 0.5);
        else if(firstView.getRotation() == 270f)
            cords1[0] += (firstView.getMeasuredHeight() - firstView.getMeasuredWidth() * 0.5);

        if(secondView.getRotation() == 90f)
            cords2[0] -= (secondView.getMeasuredHeight() * 0.5 + secondView.getMeasuredWidth() * 0.5);
        else if(secondView.getRotation() == 270f)
            cords2[0] += (secondView.getMeasuredHeight() - secondView.getMeasuredWidth() * 0.5);

        cords1[0] += firstView.getMeasuredWidth() * 0.25;
        cords2[0] += secondView.getMeasuredWidth() * 0.25;
        cords1[1] += firstView.getMeasuredHeight() * 0.25;
        cords2[1] += secondView.getMeasuredHeight() * 0.25;

        card1.set(cords1[0], cords1[1], (int) (cords1[0] + (firstView.getMeasuredWidth() * 0.5)), (int) (cords1[1] + (firstView.getMeasuredHeight() * 0.5)));
        card2.set(cords2[0], cords2[1], (int) (cords2[0] + (secondView.getMeasuredWidth() * 0.5)), (int) (cords2[1] + (secondView.getMeasuredHeight() * 0.5)));


        Log.d("Loc", cords1[0] + "  " + cords2[0] + " " + cords1[1] + " " + cords2[1] + " " + firstView.getMeasuredWidth());

        return Rect.intersects(card1, card2);

    }

    private class drawHandler extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            //while(deckContents.size() == 0);

            drawCardFromDeck();

            return null;

        }
    }

    private class putToMap extends AsyncTask<String, Void, Object[]>{

        @Override
        protected Object[] doInBackground(String... strings) {

            InputStream cardImageDataStream;
            Drawable cardImage;
            String cardName = strings[0];
            Object[] returnValues = new Object[3];

            try {

                if(!cardMap.containsKey(cardName)) {
                    cardImageDataStream = (InputStream) new URL(cardImageServerAdress2 + urlConverter.formatURL(cardName) + ".jpg").getContent();
                    cardImage = Drawable.createFromStream(cardImageDataStream, "src name");
                    cardMap.put(cardName, cardImage.getConstantState().newDrawable().mutate());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnValues;

        }

        @Override
        protected void onPostExecute(Object[] obj) {


        }

    }

    private class putToOpponentEd extends AsyncTask<String, Void, Object[]>{

        @Override
        protected Object[] doInBackground(String... strings) {

            InputStream cardImageDataStream;
            Drawable cardImage;
            String cardName = strings[0].split("&&")[0];
            Object[] returnValues = new Object[3];

            try {
                if(!cardName.equals("PLHLD")) {
                    if(!cardMap.containsKey(cardName)) {
                        cardImageDataStream = (InputStream) new URL(cardImageServerAdress2 + urlConverter.formatURL(cardName) + ".jpg").getContent();
                        cardImage = Drawable.createFromStream(cardImageDataStream, "src name");
                        cardMap.put(cardName, cardImage.getConstantState().newDrawable().mutate());
                    }else
                        cardImage = cardMap.get(cardName).getConstantState().newDrawable().mutate();
                }else{
                    cardImage = getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate();
                }
                returnValues[0] = cardImage;
                returnValues[1] = cardName;
                returnValues[2] = strings[0].split("&&")[1];

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnValues;

        }

        @Override
        protected void onPostExecute(Object[] obj) {

            final Object[] parameters = obj;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String cardName = (String) parameters[1];
                    Drawable cardImage = (Drawable) parameters[0];
                    final ImageView addedCard = new ImageView(con);

                    addedCard.setImageDrawable(cardImage);

                    addedCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(selectedCard != null)
                                        selectedCard.setColorFilter(null);
                                    addedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                }
                            });

                            selectedCard = addedCard;
                            cardLocaion = 14;



                            showCardOptionsOpponent();
                        }
                    });

                    if(parameters[2].equals("S")){

                        if (opSelectedCard != null)
                            opSelectedCard.setColorFilter(null);
                        addedCard.setColorFilter(Color.argb(60, 255, 0, 0));
                        opSelectedCard = addedCard;

                    }

                    opponentExtraDeckContents.addView(addedCard);
                    opponentExtraDeck.setBackground(cardImage.getConstantState().newDrawable());
                    opponentExtraDeck.setImageDrawable(null);
                    opponentExtraDeck.setAlpha(1.0f);

                    addedCard.setVisibility(View.VISIBLE);

                    extraDeckContentsOpponent.add(addedCard);
                    extraDeckNamesOpponent.add(cardName);

                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) addedCard.getLayoutParams();
                    params.width = (int) (width/7);
                    params.height = (int)(height/heightToDivide);
                    addedCard.setLayoutParams(params);

                    if(prevLocAni == FIELD){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni + opponentHand.getLayoutParams().height;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == HAND){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni - (ncardPositionParameters.width / 2);
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == GRAVE || prevLocAni == BANISH){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == -1){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = 0;
                        ncardPositionParameters.topMargin = opponentHand.getMeasuredHeight();
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(aniCover.getMeasuredWidth() - ncardPositionParameters.width, opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else
                        addedCard.setVisibility(View.VISIBLE);

                    prevLocAni = -1;

                }
            });

        }

    }

    private class putToOpponentBanish extends AsyncTask<String, Void, Object[]>{

        @Override
        protected Object[] doInBackground(String... strings) {

            InputStream cardImageDataStream;
            Drawable cardImage;
            String cardName = strings[0].split("&&")[0];
            Object[] returnValues = new Object[3];

            try {

                if(strings[0].split("&&").length == 2) {
                    if(!cardMap.containsKey(cardName)) {
                        cardImageDataStream = (InputStream) new URL(cardImageServerAdress2 + urlConverter.formatURL(cardName) + ".jpg").getContent();
                        cardImage = Drawable.createFromStream(cardImageDataStream, "src name");
                        cardMap.put(cardName, cardImage.getConstantState().newDrawable().mutate());
                    }else
                        cardImage = cardMap.get(cardName).getConstantState().newDrawable().mutate();
                }else{
                    cardImage = getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate();
                    cardName = "PH";
                }

                returnValues[0] = cardImage;
                returnValues[1] = cardName;
                returnValues[2] = strings[0].split("&&")[1];

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnValues;

        }

        @Override
        protected void onPostExecute(Object[] obj) {

            final Object[] parameters = obj;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String cardName = (String) parameters[1];
                    Drawable cardImage = (Drawable) parameters[0];
                    final ImageView addedCard = new ImageView(con);

                    addedCard.setImageDrawable(cardImage);

                    addedCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(selectedCard != null)
                                        selectedCard.setColorFilter(null);
                                    addedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                }
                            });

                            selectedCard = addedCard;
                            cardLocaion = 10;



                            showCardOptionsOpponent();
                        }
                    });

                    if(parameters[2].equals("S")){

                        if (opSelectedCard != null)
                            opSelectedCard.setColorFilter(null);
                        addedCard.setColorFilter(Color.argb(60, 255, 0, 0));
                        opSelectedCard = addedCard;

                    }

                    opponentBanishContents.addView(addedCard);
                    opponentBanish.setBackground(cardImage.getConstantState().newDrawable());
                    opponentBanish.setImageDrawable(null);
                    opponentBanish.setAlpha(1.0f);

                    addedCard.setVisibility(View.VISIBLE);

                    banishContentsOpponent.add(addedCard);
                    banishNamesOpponent.add(cardName);

                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) addedCard.getLayoutParams();
                    params.width = (int) (width/7);
                    params.height = (int)(height/heightToDivide);
                    addedCard.setLayoutParams(params);

                    if(prevLocAni == FIELD){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni + opponentHand.getLayoutParams().height;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height + opponentGrave.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == HAND){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni - (ncardPositionParameters.width / 2);
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height + opponentGrave.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == GRAVE || prevLocAni == EDECK){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height + opponentGrave.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == -1) {

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = 0;
                        ncardPositionParameters.topMargin = opponentHand.getMeasuredHeight();
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height + opponentGrave.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else
                        addedCard.setVisibility(View.VISIBLE);

                    prevLocAni = -1;

                }
            });

        }

    }

    private class putToOpponentGrave extends AsyncTask<String, Void, Object[]>{

        @Override
        protected Object[] doInBackground(String... strings) {

            InputStream cardImageDataStream;
            Drawable cardImage;
            String cardName = strings[0].split("&&")[0];
            Object[] returnValues = new Object[3];

            try {
                if(!cardMap.containsKey(cardName)) {
                    cardImageDataStream = (InputStream) new URL(cardImageServerAdress2 + urlConverter.formatURL(cardName) + ".jpg").getContent();
                    cardImage = Drawable.createFromStream(cardImageDataStream, "src name");
                    cardMap.put(cardName, cardImage.getConstantState().newDrawable().mutate());
                }else
                    cardImage = cardMap.get(cardName).getConstantState().newDrawable().mutate();

                returnValues[0] = cardImage;
                returnValues[1] = cardName;
                returnValues[2] = strings[0].split("&&")[1];

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnValues;

        }

        @Override
        protected void onPostExecute(Object[] obj) {

            final Object[] parameters = obj;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String cardName = (String) parameters[1];
                    Drawable cardImage = (Drawable) parameters[0];
                    final ImageView addedCard = new ImageView(con);

                    addedCard.setImageDrawable(cardImage);
                    addedCard.setMinimumHeight((int)(height/heightToDivide));
                    addedCard.setMinimumWidth((int)(width/7));
                    addedCard.setMaxHeight((int)(height/heightToDivide));
                    addedCard.setMaxWidth((int)(width/7));
                    addedCard.setAdjustViewBounds(true);

                    addedCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(selectedCard != null)
                                        selectedCard.setColorFilter(null);
                                    addedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                }
                            });

                            selectedCard = addedCard;
                            cardLocaion = 11;



                            showCardOptionsOpponent();
                        }
                    });


                    if(parameters[2].equals("S")){

                        if (opSelectedCard != null)
                            opSelectedCard.setColorFilter(null);
                        addedCard.setColorFilter(Color.argb(60, 255, 0, 0));
                        opSelectedCard = addedCard;

                    }

                    opponentGraveContents.addView(addedCard);
                    opponentGrave.setBackground(cardImage.getConstantState().newDrawable());
                    opponentGrave.setImageDrawable(null);
                    opponentGrave.setAlpha(1.0f);

                    addedCard.setVisibility(View.VISIBLE);

                    graveContentsOpponent.add(addedCard);
                    graveyardNamesOpponent.add(cardName);

                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) addedCard.getLayoutParams();
                    params.width = (int) (width/7);
                    params.height = (int)(height/heightToDivide);
                    addedCard.setLayoutParams(params);

                    if(prevLocAni == FIELD){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni + opponentHand.getLayoutParams().height;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == HAND){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni - (ncardPositionParameters.width / 2);
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == BANISH || prevLocAni == EDECK){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == -1) {

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = 0;
                        ncardPositionParameters.topMargin = opponentHand.getMeasuredHeight();
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(0, opponentHand.getLayoutParams().height + findViewById(R.id.deck2).getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else
                        addedCard.setVisibility(View.VISIBLE);

                    prevLocAni = -1;

                }
            });

        }

    }

    private class putToOpponentField extends AsyncTask<String[], Void, Object[]>{

        @Override
        protected Object[] doInBackground(String[]... strings) {

            InputStream cardImageDataStream;
            Drawable cardImage;
            String cardName = strings[0][3];
            Object[] returnValues = new Object[8];

            try {

                if(cardName.equals("TOKEN")){

                    cardImage = getResources().getDrawable(R.drawable.token);

                }else{

                    if(!cardMap.containsKey(cardName)) {
                        cardImageDataStream = (InputStream) new URL(cardImageServerAdress2 + urlConverter.formatURL(cardName) + ".jpg").getContent();
                        cardImage = Drawable.createFromStream(cardImageDataStream, "src name");
                        cardMap.put(cardName, cardImage.getConstantState().newDrawable().mutate());
                    }else
                        cardImage = cardMap.get(cardName).getConstantState().newDrawable().mutate();

                }

                returnValues[0] = cardImage;
                returnValues[1] = cardName;
                returnValues[2] = strings[0][0];
                returnValues[3] = strings[0][1];
                returnValues[4] = strings[0][2];
                returnValues[5] = strings[0][4];
                returnValues[6] = strings[0][5];
                returnValues[7] = (strings[0].length != 7) ? -1 : Integer.parseInt(strings[0][6]);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnValues;

        }

        @Override
        protected void onPostExecute(Object[] obj) {

            final Object[] parameters = obj;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    double xCoord = Double.parseDouble((String) parameters[2]);
                    double yCoord = Double.parseDouble((String) parameters[3]);
                    String cardSide = (String) parameters[4];
                    String defOf = (String) parameters[6];
                    String cardName = (String) parameters[1];
                    Drawable cardImage = (Drawable) parameters[0];
                    final ImageView addedCard = new ImageView(con);
                    RelativeLayout.LayoutParams locationParameters;

                    addedCard.setImageDrawable(cardImage);
                    addedCard.setVisibility(View.INVISIBLE);

                    gameField.addView(addedCard);

                    locationParameters = (RelativeLayout.LayoutParams) addedCard.getLayoutParams();
                    locationParameters.width = (int)((width/7) * 0.8);
                    locationParameters.height = (int) ((height/heightToDivide) * 0.8);
                    locationParameters.leftMargin = (int) ((1.0 - xCoord) * (double)gameField.getMeasuredWidth()) - (int)((double) locationParameters.width * 1.25);
                    locationParameters.topMargin = (int) ((int) ((1.0-yCoord) * (double)gameField.getMeasuredHeight()) - (locationParameters.height * 0.90));


                    addedCard.setLayoutParams(locationParameters);

                    if(defOf.equals("O"))
                        addedCard.setRotation(180);
                    else
                        addedCard.setRotation(270);

                    if(cardSide.equals("D"))
                        addedCard.setForeground(getResources().getDrawable(R.drawable.cardname).getConstantState().newDrawable().mutate());


                    addedCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(selectedCard != null) {
                                        if(selectedCard.getForeground() == null)
                                            selectedCard.setColorFilter(null);
                                        else {
                                            selectedCard.getForeground().setColorFilter(null);
                                            selectedCard.setColorFilter(null);
                                            selectedCard.invalidate();
                                        }

                                        selectedCard.invalidate();

                                    }

                                    if(addedCard.getForeground() == null || (addedCard.getParent().equals(gameField) && fieldCountersOpponent.get(fieldContentsOpponent.indexOf(addedCard)) > 0))
                                        addedCard.setColorFilter(Color.argb(60, 0, 255, 0));
                                    else
                                        addedCard.getForeground().setColorFilter(Color.argb(125, 0, 255, 0), PorterDuff.Mode.OVERLAY);

                                    addedCard.invalidate();
                                }
                            });

                            selectedCard = addedCard;
                            cardLocaion = 13;


                            showCardOptionsOpponent();
                        }
                    });

                    fieldContentsOpponent.add(addedCard);
                    fieldNamesOpponent.add(cardName);
                    fieldCountersOpponent.add(((Integer)parameters[7] == -1) ? 0 : (Integer)parameters[7]);
                    Log.d("NUM", parameters[7] + "");
                    if((Integer)parameters[7] > 0) {
                        try{
                            Bitmap counters = Bitmap.createBitmap((int) width / 7, (int) ((int) height / heightToDivide), Bitmap.Config.ARGB_8888);

                            Canvas canvas = new Canvas(counters);

                            Paint paint = new Paint();
                            paint.setDither(true);
                            paint.setAntiAlias(true);
                            paint.setFilterBitmap(true);
                            paint.setTextSize(48.0f);
                            paint.setColor(Color.GREEN);
                            canvas.drawText(parameters[7] + "", (float) (width / 60), (float) (height / (1.05f * heightToDivide)), paint);
                            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                            canvas.drawBitmap(counters, 0, 0, paint);

                            Drawable overlay = new BitmapDrawable(getResources(), counters);

                            addedCard.setForeground(overlay);
                            addedCard.invalidate();

                        }catch (Exception e){
                            // ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                            gameExitCrash();
                        }
                    }

                    ((TextView) findViewById(R.id.textView3)).setText(gameField.getChildCount() + "");

                    if(parameters[5].equals("S")){

                        if (opSelectedCard != null) {
                            if(opSelectedCard.getForeground() == null)
                                opSelectedCard.setColorFilter(null);
                            else
                                opSelectedCard.getForeground().setColorFilter(null);
                        }
                        if(addedCard.getForeground() == null || (Integer)parameters[7] > 0)
                            addedCard.setColorFilter(Color.argb(60, 255, 0, 0));
                        else
                            addedCard.getForeground().setColorFilter(Color.argb(60, 255, 0, 0), PorterDuff.Mode.OVERLAY);
                        opSelectedCard = addedCard;

                    }

                    if(prevLocAni == FIELD){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni + opponentHand.getLayoutParams().height;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(locationParameters.leftMargin, locationParameters.topMargin + opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);

                                if(!cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(fieldContentsOpponent.indexOf(addedCard))))) {

                                    for (int i = 0; i < fieldContentsOpponent.size(); i++) {

                                        if (!fieldContentsOpponent.get(i).equals(addedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(i))) && isViewOverlapping(fieldContentsOpponent.get(i), addedCard)) {

                                            gameField.removeView(fieldContentsOpponent.get(i));
                                            gameField.addView(fieldContentsOpponent.get(i));
                                            break;

                                        }

                                    }

                                }else{

                                    for (int i = 0; i < fieldContentsOpponent.size(); i++) {

                                        if (isViewOverlappingVals(fieldContentsOpponent.get(i), prevXYZ, addedCard, false) && gameField.indexOfChild(fieldContentsOpponent.get(i)) < gameField.indexOfChild(addedCard)) {

                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fieldContentsOpponent.get(i).getLayoutParams();

                                            params.leftMargin += ((RelativeLayout.LayoutParams)addedCard.getLayoutParams()).leftMargin - prevXAni;
                                            params.topMargin += ((RelativeLayout.LayoutParams)addedCard.getLayoutParams()).topMargin - prevYAni;

                                            fieldContentsOpponent.get(i).setLayoutParams(params);

                                        }

                                    }

                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == HAND){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni - (ncardPositionParameters.width / 2);
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(locationParameters.leftMargin, locationParameters.topMargin + opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                                for(int i = 0; i < fieldContentsOpponent.size(); i++){

                                    if(!fieldContentsOpponent.get(i).equals(addedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(fieldContentsOpponent.indexOf(addedCard)))) && isViewOverlapping(fieldContentsOpponent.get(i), addedCard)){

                                        gameField.removeView(fieldContentsOpponent.get(i));
                                        gameField.addView(fieldContentsOpponent.get(i));
                                        break;

                                    }

                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == GRAVE || prevLocAni == BANISH || prevLocAni == EDECK){

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = prevXAni;
                        ncardPositionParameters.topMargin = prevYAni;
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(locationParameters.leftMargin, locationParameters.topMargin + opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                                for(int i = 0; i < fieldContentsOpponent.size(); i++){

                                    if(!fieldContentsOpponent.get(i).equals(addedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(fieldContentsOpponent.indexOf(addedCard)))) && isViewOverlapping(fieldContentsOpponent.get(i), addedCard)){

                                        gameField.removeView(fieldContentsOpponent.get(i));
                                        gameField.addView(fieldContentsOpponent.get(i));
                                        break;

                                    }

                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else if(prevLocAni == -1) {

                        ImageView tempAni = new ImageView(con);
                        tempAni.setImageDrawable(addedCard.getDrawable());
                        tempAni.setRotation(addedCard.getRotation());
                        tempAni.setForeground(addedCard.getForeground());
                        aniCover.addView(tempAni);
                        RelativeLayout.LayoutParams ncardPositionParameters = (RelativeLayout.LayoutParams) tempAni.getLayoutParams();
                        ncardPositionParameters.width = (int) ((gameField.getMeasuredWidth() / 7) * 0.8);
                        ncardPositionParameters.height = (int) ((gameField.getMeasuredHeight() / 5) * 0.8);
                        ncardPositionParameters.leftMargin = 0;
                        ncardPositionParameters.topMargin = opponentHand.getMeasuredHeight();
                        tempAni.setLayoutParams(ncardPositionParameters);
                        tempAni.setVisibility(View.VISIBLE);
                        Path path = new Path();
                        path.moveTo(ncardPositionParameters.leftMargin, ncardPositionParameters.topMargin);
                        path.lineTo(locationParameters.leftMargin, locationParameters.topMargin + opponentHand.getLayoutParams().height);
                        ObjectAnimator ani = ObjectAnimator.ofFloat(tempAni, View.X, View.Y, path);
                        ani.setDuration(200);
                        ani.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                aniCover.removeView(tempAni);
                                addedCard.setVisibility(View.VISIBLE);
                                for(int i = 0; i < fieldContentsOpponent.size(); i++){

                                    if(!fieldContentsOpponent.get(i).equals(addedCard) && cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(i))) && !cardInfo.getXYZ(cardInfo.getName(fieldNamesOpponent.get(fieldContentsOpponent.indexOf(addedCard)))) && isViewOverlapping(fieldContentsOpponent.get(i), addedCard)){

                                        gameField.removeView(fieldContentsOpponent.get(i));
                                        gameField.addView(fieldContentsOpponent.get(i));
                                        break;

                                    }

                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ani.start();

                    }else
                        addedCard.setVisibility(View.VISIBLE);

                    prevLocAni = -1;

                }
            });

        }

    }

    private class DisplayFullCard extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... url) {

            Drawable ret = null;
            InputStream is = null;

            try {
                is = (InputStream) new URL(url[0]).getContent();
                ret = Drawable.createFromStream(is, "src name");
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (Exception e) {

                final Exception ex = e;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage());
                    }
                });

            }

            return ret;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {

            final Drawable imageTemp = drawable;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    previewCard.setImageDrawable(imageTemp);
                    previewCard.setVisibility(View.VISIBLE);

                }
            });

        }

    }

    private class RetrieveImageMain extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {
                is = (InputStream) new URL(url[0]).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = urlConverter.formatURLReverse(url[0].substring(cardImageServerAdress2.length(), url[0].lastIndexOf('.')));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {
            mainDeckList.add((Drawable) d[0]);
            mainDeckSide.setAdapter(new CardImageAdapter(con, mainDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
            updateNumbers();
        }
    }
    private class RetrieveImageExtra extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {
                is = (InputStream) new URL(url[0]).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = urlConverter.formatURLReverse(url[0].substring(cardImageServerAdress2.length(), url[0].lastIndexOf('.')));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {
            extraDeckList.add((Drawable) d[0]);
            extraDeckSide.setAdapter(new CardImageAdapter(con, extraDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
            updateNumbers();
        }
    }
    private class RetrieveImageSide extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {
                is = (InputStream) new URL(url[0]).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = urlConverter.formatURLReverse(url[0].substring(cardImageServerAdress2.length(), url[0].lastIndexOf('.')));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {
            sideDeckList.add((Drawable) d[0]);
            sideDeckSide.setAdapter(new CardImageAdapter(con, sideDeckList, (int) (width/10), (int) (height/(heightToDivide * (10/7)))));
            updateNumbers();
        }
    }

    private class ShowCardPreview extends AsyncTask<String, Void, Drawable>{

        @Override
        protected Drawable doInBackground(String... url) {

            Drawable ret = null;
            InputStream is = null;

            try {
                is = (InputStream) new URL(url[0]).getContent();
                ret = Drawable.createFromStream(is, "src name");
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (Exception e) {

                final Exception ex = e;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainCount.setText(ex.getMessage());
                    }
                });

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
                            previewCardSide.setImageDrawable(prevImg);
                            previewCardSide.setVisibility(View.VISIBLE);
                            finishSiding.setVisibility(View.GONE);
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