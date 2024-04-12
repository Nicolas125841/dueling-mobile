package com.psquaredsoftware.ygomobile;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.psquaredsoftware.ygomobile.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class DuelActivity extends AppCompatActivity {

    ArrayList<ImageView> handContents = new ArrayList<ImageView>();
    GridLayout handG;
    ImageView deck1;
    Button toField;
    Button toField2;
    LinearLayout handAction;
    ArrayList<String> mainDeck = new ArrayList<String>();
    ArrayList<String> extraDeck = new ArrayList<String>();
    ArrayList<String> graveyard = new ArrayList<String>();
    ArrayList<String> banishZone = new ArrayList<String>();
    ArrayList<String> hand = new ArrayList<String>();
    ArrayList<Drawable> handR = new ArrayList<Drawable>();
    myDbAdapter helper;
    int state = 0;
    Context con = this;
    Formatter toUrl = new Formatter();
    RelativeLayout field;
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    ImageView selectedCard;
    Button da;
    Button ud;
    Switch set;
    GridLayout deckView;
    ImageView deck;
    GridLayout edView;
    ImageView ed;
    GridLayout graveView;
    ImageView grave;
    GridLayout banView;
    ImageView ban;
    GridLayout graveViewOp;
    ImageView graveOp;
    GridLayout banViewOp;
    ImageView banOp;
    ArrayList<ImageView> deckCon = new ArrayList<ImageView>();
    ArrayList<ImageView> edeckCon = new ArrayList<ImageView>();
    ArrayList<ImageView> banConOp = new ArrayList<ImageView>();
    LinearLayout norm;
    LinearLayout deckNorm;
    Button draw;
    Button banish;
    ImageView prev;
    Button viewCard;
    Button banEd;
    boolean host = false;
    DataOutputStream os = null;
    DataInputStream is = null;
    ServerSocket hoster = null;
    Socket player = null;
    Socket client = null;
    ArrayList<ImageView> filedContents = new ArrayList<ImageView>();
    ArrayList<String> filedNames = new ArrayList<String>();
    ArrayList<ImageView> opphand = new ArrayList<>();
    ArrayList<String> opphandNames = new ArrayList<>();
    ArrayList<ImageView> oppfield = new ArrayList<>();
    ArrayList<String> oppfieldNames = new ArrayList<>();
    GridLayout handOp;
    ArrayList<ImageView> mainDeckContents = new ArrayList<>();

    boolean isOnField = false;
    //final String urlServer = "http://ygo.ignorelist.com/photos/";
    final String urlServer = "http://ygo.ignorelist.com/photos/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        draw = findViewById(R.id.draw);
        handOp = findViewById(R.id.hand2);
        banEd = findViewById(R.id.banishEd);
        viewCard = findViewById(R.id.viewCard);
        prev = findViewById(R.id.imageView2);
        handG =  findViewById(R.id.hand1);
        banish = findViewById(R.id.banish);
        helper = new myDbAdapter(this);
        set = findViewById(R.id.set);
        da = findViewById(R.id.da);
        ud = findViewById(R.id.fud);
        norm = findViewById(R.id.normalControls);
        deckNorm = findViewById(R.id.nonViewDeckControls);
        field = findViewById(R.id.field);
        deck = findViewById(R.id.deck);
        graveOp = findViewById(R.id.grave2);
        banOp = findViewById(R.id.banishZone2);
        deckView = findViewById(R.id.deckContents);
        ed = findViewById(R.id.extradeck);
        edView = findViewById(R.id.edCont);
        banView = findViewById(R.id.banishCont);
        ban = findViewById(R.id.banishZone);
        banViewOp = findViewById(R.id.banishCont2);
        graveViewOp = findViewById(R.id.graveCont2);
        grave = findViewById(R.id.grave);
        graveView = findViewById(R.id.graveCont);
        final Bundle extras = getIntent().getExtras();


        if(extras.getString("TYPE").equals("HOST")){
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hoster = new ServerSocket(1236);
                            client = hoster.accept();
                            os = new DataOutputStream(client.getOutputStream());
                            is = new DataInputStream(client.getInputStream());
                            //os.writeUTF("test");
                        } catch (Exception e) {
                            final Exception ex = e;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage());
                                }
                            });
                        }
                    }
                }).start();
                //os.writeUTF("test");

            }catch(Exception e){
                ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
            }
        }else{
            try {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            player = new Socket(extras.getString("ADRESS"), 1236);
                            os = new DataOutputStream(player.getOutputStream());
                            is = new DataInputStream(player.getInputStream());
                            //os.writeUTF("DRAWABAKI");
                        } catch (Exception e) {
                            final Exception ex = e;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //((TextView) findViewById(R.id.textView3)).setText(ex.getMessage());
                                }
                            });
                        }
                    }
                }).start();

            }catch (Exception e){
                ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
            }
        }
        while(is == null || os == null);
       new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){


                        try {

                            if (is.available() == 0) continue;

                            final String buff = is.readUTF();
                            if(buff.contains("TOFIELD")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((TextView) findViewById(R.id.textView3)).setText(buff);
                                    }
                                });
                                new PutToFieldOp().execute(buff);
                            }else if(buff.contains("REMOVEHAND")){
                                final int ind = Integer.parseInt(buff.substring(10));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        opphand.remove(ind);
                                        opphandNames.remove(ind);
                                        handOp.removeViewAt(ind);
                                        ((TextView) findViewById(R.id.textView3)).setText(buff);
                                    }
                                });

                            }else if(buff.contains("DRAW")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((TextView) findViewById(R.id.textView3)).setText("RECV");
                                    }
                                });
                                new DrawOp().execute(buff);
                            }else if(buff.contains("REMOVEFIELD")){

                                final int ind = Integer.parseInt(buff.substring(11));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageView tmp = oppfield.get(ind);
                                        field.removeView(tmp);
                                        oppfield.remove(ind);
                                        oppfieldNames.remove(ind);
                                        ((TextView) findViewById(R.id.textView3)).setText(buff);
                                    }
                                });

                            }else if(buff.contains("ADDHAND")){
                                new AddHandOp().execute(buff);
                            }else if(buff.contains("DEFENSE")){
                                final int ind = Integer.parseInt(buff.substring(7));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        oppfield.get(ind).setRotation(270f);
                                    }
                                });
                            }else if(buff.contains("ATTACK")){
                                final int ind = Integer.parseInt(buff.substring(6));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        oppfield.get(ind).setRotation(180f);
                                    }
                                });
                            }else if(buff.contains("FACEUP")){
                                final int ind = Integer.parseInt(buff.substring(6));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        oppfield.get(ind).setForeground(null);
                                    }
                                });
                            }else if(buff.contains("FACEDOWN")){
                                final int ind = Integer.parseInt(buff.substring(8));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        oppfield.get(ind).setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                                    }
                                });
                            }else if(buff.contains("BANISHDECK")){
                                //final int ind = Integer.parseInt(buff.substring(6));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final ImageView tmp = new ImageView(con);
                                        //tmp.setImageDrawable((Drawable) d[0]);
                                        tmp.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                                        tmp.setMaxWidth(42);
                                        tmp.setMaxHeight(64);
                                        banViewOp.addView(tmp);
                                        banConOp.add(tmp);
                                        banOp.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                                    }
                                });
                            }else if(buff.contains("BANISHEDDECK")){
                                //final int ind = Integer.parseInt(buff.substring(8));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final ImageView tmp = new ImageView(con);
                                        //tmp.setImageDrawable((Drawable) d[0]);
                                        tmp.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                                        tmp.setMaxWidth(42);
                                        tmp.setMaxHeight(64);
                                        banViewOp.addView(tmp);
                                        banConOp.add(tmp);
                                        banOp.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                                    }
                                });
                            }else if(buff.contains("BANISHGENERIC")){
                                new AddBanishOp().execute(buff);
                            }


                        } catch (Exception e) {
                            final Exception ex = e;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage());
                                }
                            });
                        }


                }
            }
        }).start();


        String[] splitDb = extras.getString("DECK").split("&&");
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
                mainDeck.add(splitDb[i]);
                new PutToDeck().execute(urlServer + toUrl.formatURL(splitDb[i]) + ".jpg");

            } else if (state == 1) {
                extraDeck.add(splitDb[i]);
                new PutToEDeck().execute(urlServer + toUrl.formatURL(splitDb[i]) + ".jpg");
                //new RetrieveImageExtra().execute(urlServer + toUrl.formatURL(splitDb[i]) + ".jpg");

            } else {

                //new RetrieveImageSide().execute(urlServer + toUrl.formatURL(splitDb[i]) + ".jpg");
            }

        }

        for(int i = 0; i < 5; i++){
            while(mainDeck.size() == 0);
             new Draw().execute();

        }

        viewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev.setImageDrawable(selectedCard.getDrawable());
                prev.setVisibility(View.VISIBLE);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev.setVisibility(View.GONE);
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Draw().execute();
            }
        });

        banish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Banish().execute();
            }
        });

        banEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Banish2().execute();
            }
        });

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.edViewCont).setVisibility(View.VISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.INVISIBLE);
            }
        });

        //((TextView) findViewById(R.id.textView3)).setText("Test");

        deck.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deckView.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        grave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                graveView.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        ban.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                banView.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        banOp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                banViewOp.setVisibility(View.VISIBLE);
                //findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
                //deckNorm.setVisibility(View.INVISIBLE);
                //norm.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        ed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                edView.setVisibility(View.VISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.INVISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
                return false;
            }
        });

        deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deckNorm.setVisibility(View.VISIBLE);
                norm.setVisibility(View.INVISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        deckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deckView.setVisibility(View.INVISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        banView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banView.setVisibility(View.INVISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        banViewOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banViewOp.setVisibility(View.INVISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        graveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graveView.setVisibility(View.INVISIBLE);
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        edView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edView.setVisibility(View.INVISIBLE);
                //deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        deck.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:

                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                return true;
                            }

                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            //v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            // v.getBackground().clearColorFilter();

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            ClipData.Item item = event.getClipData().getItemAt(0);

                            String dragData = (String) item.getText();
                            android.widget.RelativeLayout.LayoutParams p;

                            //Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                            // v.getBackground().clearColorFilter();

                            // v.invalidate();

                            ImageView vw = (ImageView) event.getLocalState();
                            ViewGroup owner = (ViewGroup) vw.getParent();
                            owner.removeView(vw);
                            ImageView container = (ImageView) v;
                            container.setImageDrawable(vw.getDrawable());
                            final ImageView tmp = new ImageView(con);

                            tmp.setImageDrawable(vw.getDrawable());
                            tmp.setMaxWidth(42);
                            tmp.setMaxHeight(64);
                            tmp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    selectedCard = tmp;
                                }
                            });
                            tmp.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    try {
                                        if(set.isChecked())
                                            tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                                        v.startDrag(data        // data to be dragged
                                                , dragshadow   // drag shadow builder
                                                , v           // local data about the drag and drop operation
                                                , 0          // flags (not currently used, set to 0)
                                        );
                                    }catch (Exception e){
                                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                                    }
                                    isOnField = false;
                                    return true;

                                }
                            });
                            deckView.addView(tmp);
                            //container.addView(vw);//Add the dragged view
                           /* p = (RelativeLayout.LayoutParams) vw.getLayoutParams();
                            p.leftMargin = (int) event.getX();
                            p.topMargin = (int) event.getY();
                            p.width = 100;
                            p.height = 140;
                            vw.setLayoutParams(p);
                            vw.setVisibility(View.VISIBLE);*/
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            //v.getBackground().clearColorFilter();

                            //v.invalidate();

                            return true;

                        default:
                            // Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }catch (Exception e){
                    ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                }
                return  false;
            }
        });

        ed.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:

                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                return true;
                            }

                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            //v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            // v.getBackground().clearColorFilter();

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            ClipData.Item item = event.getClipData().getItemAt(0);

                            String dragData = (String) item.getText();
                            android.widget.RelativeLayout.LayoutParams p;

                            //Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                            // v.getBackground().clearColorFilter();

                            // v.invalidate();

                            ImageView vw = (ImageView) event.getLocalState();
                            ViewGroup owner = (ViewGroup) vw.getParent();
                            owner.removeView(vw);
                            ImageView container = (ImageView) v;
                            //container.setImageDrawable(vw.getDrawable());
                            final ImageView tmp = new ImageView(con);

                            tmp.setImageDrawable(vw.getDrawable());
                            tmp.setMaxWidth(42);
                            tmp.setMaxHeight(64);
                            tmp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    selectedCard = tmp;
                                }
                            });
                            tmp.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    try {
                                        if(set.isChecked())
                                            tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                                        v.startDrag(data        // data to be dragged
                                                , dragshadow   // drag shadow builder
                                                , v           // local data about the drag and drop operation
                                                , 0          // flags (not currently used, set to 0)
                                        );
                                    }catch (Exception e){
                                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                                    }
                                    isOnField = false;
                                    return true;

                                }
                            });
                            edView.addView(tmp);
                            //container.addView(vw);//Add the dragged view
                           /* p = (RelativeLayout.LayoutParams) vw.getLayoutParams();
                            p.leftMargin = (int) event.getX();
                            p.topMargin = (int) event.getY();
                            p.width = 100;
                            p.height = 140;
                            vw.setLayoutParams(p);
                            vw.setVisibility(View.VISIBLE);*/
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            //v.getBackground().clearColorFilter();

                            //v.invalidate();

                            return true;

                        default:
                            // Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }catch (Exception e){
                    ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                }
                return  false;
            }
        });

        ban.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:

                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                return true;
                            }

                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            //v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            // v.getBackground().clearColorFilter();

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            ClipData.Item item = event.getClipData().getItemAt(0);

                            String dragData = (String) item.getText();
                            android.widget.RelativeLayout.LayoutParams p;

                            ImageView vw = (ImageView) event.getLocalState();
                            ViewGroup owner = (ViewGroup) vw.getParent();
                            owner.removeView(vw);
                            ImageView container = (ImageView) v;
                            container.setImageDrawable(vw.getDrawable());
                            final ImageView tmp = new ImageView(con);

                            tmp.setImageDrawable(vw.getDrawable());
                            tmp.setMaxWidth(42);
                            tmp.setMaxHeight(64);
                            tmp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    selectedCard = tmp;
                                }
                            });
                            tmp.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    try {
                                        if(set.isChecked())
                                            tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                                        v.startDrag(data        // data to be dragged
                                                , dragshadow   // drag shadow builder
                                                , v           // local data about the drag and drop operation
                                                , 0          // flags (not currently used, set to 0)
                                        );
                                    }catch (Exception e){
                                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                                    }
                                    isOnField = false;
                                    return true;

                                }
                            });
                            banView.addView(tmp);

                            int ind = 0;
                            int ind2 = 0;
                            String name = "";
                            if(isOnField) {
                                ind  = filedContents.indexOf((ImageView) vw);
                                name = filedNames.get(ind);
                                filedContents.remove((ImageView) vw);
                                filedNames.remove(ind);
                                //banConOp.add((ImageView) vw);
                            }  else {
                                //banConOp.add((ImageView) vw);
                                ind2 = handContents.indexOf((ImageView) vw);
                                name = hand.get(ind2);
                                hand.remove(ind2);
                                handContents.remove(ind2);
                            }
                            final int indofmov = ind;
                            final int strind = ind2;
                            final String n = name;
                            //while(filedNames.size() <= tmpnum);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if(!isOnField) {
                                            os.writeUTF("REMOVEHAND" + strind);
                                            os.flush();
                                        }else{
                                            os.writeUTF("REMOVEFIELD" + indofmov);
                                            os.flush();
                                        }

                                        os.writeUTF("BANISHGENERIC" + n);
                                        os.flush();
                                        //Thread.sleep(500);


                                    } catch (Exception e) {
                                        final Exception ex = e;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage());
                                            }
                                        });
                                    }
                                }
                            }).start();

                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            //v.getBackground().clearColorFilter();

                            //v.invalidate();

                            return true;

                        default:
                            // Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }catch (Exception e){
                    ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                }
                return  false;
            }
        });

        grave.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:

                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                return true;
                            }

                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            //v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            // v.getBackground().clearColorFilter();

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            ClipData.Item item = event.getClipData().getItemAt(0);

                            String dragData = (String) item.getText();
                            android.widget.RelativeLayout.LayoutParams p;

                            //Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                            // v.getBackground().clearColorFilter();

                            // v.invalidate();

                            ImageView vw = (ImageView) event.getLocalState();
                            ViewGroup owner = (ViewGroup) vw.getParent();
                            owner.removeView(vw);
                            ImageView container = (ImageView) v;
                            container.setImageDrawable(vw.getDrawable());
                            final ImageView tmp = new ImageView(con);

                            tmp.setImageDrawable(vw.getDrawable());
                            tmp.setMaxWidth(42);
                            tmp.setMaxHeight(64);
                            tmp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    selectedCard = tmp;
                                }
                            });
                            tmp.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    try {
                                        if(set.isChecked())
                                            tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                                        v.startDrag(data        // data to be dragged
                                                , dragshadow   // drag shadow builder
                                                , v           // local data about the drag and drop operation
                                                , 0          // flags (not currently used, set to 0)
                                        );
                                    }catch (Exception e){
                                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                                    }
                                    isOnField = false;
                                    return true;

                                }
                            });
                            graveView.addView(tmp);
                            //container.addView(vw);//Add the dragged view
                           /* p = (RelativeLayout.LayoutParams) vw.getLayoutParams();
                            p.leftMargin = (int) event.getX();
                            p.topMargin = (int) event.getY();
                            p.width = 100;
                            p.height = 140;
                            vw.setLayoutParams(p);
                            vw.setVisibility(View.VISIBLE);*/
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            //v.getBackground().clearColorFilter();

                            //v.invalidate();

                            return true;

                        default:
                            // Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }catch (Exception e){
                    ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                }
                return  false;
            }
        });

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        field.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:

                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                return true;
                            }

                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            //v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                           // v.getBackground().clearColorFilter();

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            ClipData.Item item = event.getClipData().getItemAt(0);

                            String dragData = (String) item.getText();
                            final android.widget.RelativeLayout.LayoutParams p;

                            //Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                           // v.getBackground().clearColorFilter();

                           // v.invalidate();

                            final View vw = (View) event.getLocalState();
                            ViewGroup owner = (ViewGroup) vw.getParent();
                            owner.removeView(vw);
                            RelativeLayout container = (RelativeLayout) v;

                            container.addView(vw);//Add the dragged view
                            p = (RelativeLayout.LayoutParams) vw.getLayoutParams();
                            p.leftMargin = (int) event.getX() - 50;
                            p.topMargin = (int) event.getY() - 64;
                            p.width = 100;
                            p.height = 140;
                            vw.setLayoutParams(p);
                            vw.setVisibility(View.VISIBLE);
                            //handContents.indexOf((ImageView) vw);

                            vw.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    try {
                                        if(set.isChecked())
                                            vw.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                                        v.startDrag(data        // data to be dragged
                                                , dragshadow   // drag shadow builder
                                                , v           // local data about the drag and drop operation
                                                , 0          // flags (not currently used, set to 0)
                                        );
                                    }catch (Exception e){
                                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                                    }
                                    isOnField = true;
                                    return true;

                                }
                            });
                            int ind = 0;
                            int ind2 = 0;
                            if(isOnField) {
                                ind  = filedContents.indexOf((ImageView) vw);
                                String name = filedNames.get(ind);
                                filedContents.remove((ImageView) vw);
                                filedNames.remove(ind);
                                filedNames.add(name);
                                filedContents.add((ImageView) vw);
                            }  else {

                                filedContents.add((ImageView) vw);
                                ind2 = handContents.indexOf((ImageView) vw);
                                //ind tmpnum = filedNames.size();
                                filedNames.add(hand.get(ind2));
                                hand.remove(ind2);
                                handContents.remove(ind2);
                            }
                            final int indofmov = ind;
                            final int strind = ind2;
                            //while(filedNames.size() <= tmpnum);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if(!isOnField) {
                                            os.writeUTF("REMOVEHAND" + strind);
                                            os.flush();
                                        }else{
                                            os.writeUTF("REMOVEFIELD" + indofmov);
                                            os.flush();
                                        }

                                            os.writeUTF("TOFIELD" + p.leftMargin + "X" + p.topMargin + "Y" + filedNames.get(filedNames.size() - 1));
                                            os.flush();
                                            //Thread.sleep(500);


                                    } catch (Exception e) {
                                        final Exception ex = e;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage());
                                            }
                                        });
                                    }
                                }
                            }).start();
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            //v.getBackground().clearColorFilter();

                            //v.invalidate();

                            return true;

                        default:
                            // Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }catch (Exception e){
                    ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                }
                return  false;
            }
        });

        handG.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:

                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                return true;
                            }

                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            //v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            // v.getBackground().clearColorFilter();

                            //v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            ClipData.Item item = event.getClipData().getItemAt(0);

                            String dragData = (String) item.getText();

                            //Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                            // v.getBackground().clearColorFilter();

                            // v.invalidate();

                            ImageView vw = (ImageView) event.getLocalState();
                            ViewGroup owner = (ViewGroup) vw.getParent();

                            //GridLayout container = (GridLayout) v;
                            //container.addView(vw);//Add the dragged view
                            final ImageView tmp = new ImageView(con);

                            tmp.setImageDrawable(vw.getDrawable());
                            tmp.setMaxWidth(42);
                            tmp.setMaxHeight(64);
                            tmp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    selectedCard = tmp;
                                    deckNorm.setVisibility(View.INVISIBLE);
                                    norm.setVisibility(View.VISIBLE);
                                }
                            });
                            tmp.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    try {
                                        if(set.isChecked())
                                            tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                                        v.startDrag(data        // data to be dragged
                                                , dragshadow   // drag shadow builder
                                                , v           // local data about the drag and drop operation
                                                , 0          // flags (not currently used, set to 0)
                                        );
                                    }catch (Exception e){
                                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                                    }
                                    isOnField = false;
                                    return true;

                                }
                            });

                            handContents.add(tmp);
                            hand.add(filedNames.get(filedContents.indexOf(vw)));
                            owner.removeView(vw);
                            handG.addView(tmp);
                            if(isOnField){
                                final int ind  = filedContents.indexOf((ImageView) vw);
                                final String name = filedNames.get(ind);
                                filedContents.remove((ImageView) vw);
                                filedNames.remove(ind);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            os.writeUTF("REMOVEFIELD" + ind);
                                            os.flush();
                                            os.writeUTF("ADDHAND" + name);
                                            os.flush();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                            //vw.setVisibility(View.VISIBLE);
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            //v.getBackground().clearColorFilter();

                            //v.invalidate();

                            return true;

                        default:
                            // Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }catch (Exception e){
                    ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                }
                return  false;
            }
        });
        handG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deckNorm.setVisibility(View.INVISIBLE);
                norm.setVisibility(View.VISIBLE);
                findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
            }
        });

        da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCard != null){
                    if(selectedCard.getRotation() == 0f) {
                        selectedCard.setRotation(90f);
                        final int ind = filedContents.indexOf(selectedCard);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    os.writeUTF("DEFENSE" + ind);
                                    os.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }else {
                        final int ind = filedContents.indexOf(selectedCard);
                        selectedCard.setRotation(0f);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    os.writeUTF("ATTACK" + ind);
                                    os.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }
                }
            }
        });

        ud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCard != null){
                        if(selectedCard.getForeground() == null) {
                            selectedCard.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                           // selectedCard.setRotation(90f);
                            final int ind = filedContents.indexOf(selectedCard);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        os.writeUTF("FACEDOWN" + ind);
                                        os.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                        }else {
                            selectedCard.setForeground(null);
                            //selectedCard.setRotation(90f);
                            final int ind = filedContents.indexOf(selectedCard);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        os.writeUTF("FACEUP" + ind);
                                        os.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();
                        }
                }
            }
        });

    }

    private class PutToDeck extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {
                is = (InputStream) new URL(url[0]).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = toUrl.formatURLReverse(url[0].substring(29, url[0].lastIndexOf('.')));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {

            //findViewById(R.id.field)
            final ImageView tmp = new ImageView(con);

            tmp.setImageDrawable((Drawable) d[0]);
            tmp.setMaxWidth(42);
            tmp.setMaxHeight(64);
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCard = tmp;
                    deckNorm.setVisibility(View.INVISIBLE);
                    norm.setVisibility(View.VISIBLE);
                    findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
                }
            });
            tmp.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        if(set.isChecked())
                            tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                        v.startDrag(data        // data to be dragged
                                , dragshadow   // drag shadow builder
                                , v           // local data about the drag and drop operation
                                , 0          // flags (not currently used, set to 0)
                        );
                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                    isOnField = false;
                    return true;

                }
            });
            try{
                deckView.addView(tmp);
                deckCon.add(tmp);
                mainDeck.add((String) d[1]);
                //((TextView) findViewById(R.id.textView3)).setText("Size" + deckCon.size());
            }catch (Exception e){
                ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
            }
            //handG.setAdapter(new CardImageAdapter(con, handR));
        }
    }

    private class PutToFieldOp extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            final String buff = url[0];
            Object[] ret = new Object[3];
            try {

                is = (InputStream) new URL(urlServer + toUrl.formatURL(buff.substring(buff.indexOf('Y') + 1)) + ".jpg").getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));

                Drawable d = Drawable.createFromStream(is, "src name");

                ret[0] = d;
                ret[1] = buff.substring(buff.indexOf('Y') + 1);
                ret[2] = url[0];
            } catch (Exception e) {
                final Exception ex = e;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage() + "DRawing");
                    }
                });
                //e.printStackTrace();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Object[] r) {
            final Object[] d = r;
            final String buff = (String)d[2];
            oppfieldNames.add((String) d[1]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int x = Integer.parseInt(buff.substring(7, buff.indexOf('X')));
                        int y = Integer.parseInt(buff.substring(buff.indexOf('X') + 1, buff.indexOf('Y')));
                        //String imgUrl = buff.substring(buff.indexOf('Y') + 1);
                        final ImageView img = new ImageView(con);
                        img.setImageDrawable((Drawable) d[0]);
                        img.setVisibility(View.INVISIBLE);
                        field.addView(img);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) img.getLayoutParams();
                        //x += (2 * (358 - x));
                        //y += (2 * (376 - x));
                        //((TextView) findViewById(R.id.textView3)).setText("X" + x + "Y" + y);
                        p.leftMargin = (int) 586-x;
                        p.topMargin = (int) 607-y;
                        p.width = 100;
                        p.height = 140;
                        img.setLayoutParams(p);
                        img.setRotation(180);
                        img.setVisibility(View.VISIBLE);
                        oppfield.add(img);

                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage() + "mov");
                    }
                }
            });
        }
    }

    private class PutToEDeck extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            try {
                is = (InputStream) new URL(url[0]).getContent();
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, "src name");
            Object[] ret = new Object[2];
            ret[0] = d;
            ret[1] = toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.')));
            return ret;
        }

        @Override
        protected void onPostExecute(Object[] d) {
            Object[] r = d;
            //findViewById(R.id.field)
            final ImageView tmp = new ImageView(con);

            tmp.setImageDrawable((Drawable) d[0]);
            tmp.setMaxWidth(42);
            tmp.setMaxHeight(64);
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCard = tmp;
                    deckNorm.setVisibility(View.INVISIBLE);
                    norm.setVisibility(View.VISIBLE);
                    findViewById(R.id.edViewCont).setVisibility(View.INVISIBLE);
                }
            });
            tmp.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        if(set.isChecked())
                            tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));

                        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        ClipData data = new ClipData((CharSequence) v.getTag(), mimeTypes, item);

                        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);

                        v.startDrag(data        // data to be dragged
                                , dragshadow   // drag shadow builder
                                , v           // local data about the drag and drop operation
                                , 0          // flags (not currently used, set to 0)
                        );
                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                    isOnField = false;
                    return true;

                }
            });
            try{
                edView.addView(tmp);
                edeckCon.add(tmp);
                extraDeck.add((String) d[1]);
                //deckCon.add(tmp);
                //((TextView) findViewById(R.id.textView3)).setText("Size" + deckCon.size());
            }catch (Exception e){
                ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
            }
            //handG.setAdapter(new CardImageAdapter(con, handR));
        }
    }

    private class Draw extends AsyncTask<Void, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Void... v) {
            int tmpnum = hand.size();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ImageView tmp = (ImageView) deckView.getChildAt(0);
                        final int in2 = deckCon.indexOf(tmp);
                        deckView.removeViewAt(0);
                        handG.addView(tmp);
                        handContents.add(tmp);
                        hand.add(mainDeck.get(in2));
                        mainDeck.remove(in2);
                        deckCon.remove(tmp);


                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                }
            });
            while(hand.size() <= tmpnum);
try {
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                os.writeUTF("DRAW" + hand.get(hand.size() - 1));
                os.flush();
            } catch (Exception e) {
                final Exception ex = e;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage() + "DRawing");
                    }
                });
            }
        }
    }).start();
}catch (Exception e){
    final Exception ex = e;
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            ((TextView) findViewById(R.id.textView3)).setText(ex.getMessage());
        }
    });

}
            return null;
        }

        @Override
        protected void onPostExecute(Object[] d) {


        }
    }

    private class DrawOp extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            Object[] ret = new Object[2];
            try {
                is = (InputStream) new URL(urlServer +  toUrl.formatURL(url[0].substring(4)) + ".jpg").getContent();
                Drawable d = Drawable.createFromStream(is, "src name");

                ret[0] = d;
                ret[1] = url[0].substring(4);
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Object[] r) {

            final Object[] d = r;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        opphandNames.add((String) d[1]);
                        final ImageView tmp = new ImageView(con);

                        tmp.setImageDrawable((Drawable) d[0]);
                        tmp.setMaxWidth(42);
                        tmp.setMaxHeight(64);
                        opphand.add(tmp);
                        handOp.addView(tmp);
                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                }
            });


        }
    }

    private class AddHandOp extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            Object[] ret = new Object[2];
            try {
                is = (InputStream) new URL(urlServer +  toUrl.formatURL(url[0].substring(7)) + ".jpg").getContent();
                Drawable d = Drawable.createFromStream(is, "src name");

                ret[0] = d;
                ret[1] = url[0].substring(7);
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Object[] r) {

            final Object[] d = r;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        opphandNames.add((String) d[1]);
                        final ImageView tmp = new ImageView(con);

                        tmp.setImageDrawable((Drawable) d[0]);
                        tmp.setMaxWidth(42);
                        tmp.setMaxHeight(64);
                        opphand.add(tmp);
                        handOp.addView(tmp);
                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                }
            });


        }
    }

    private class AddBanishOp extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... url) {
            InputStream is = null;
            Object[] ret = new Object[2];
            try {
                is = (InputStream) new URL(urlServer +  toUrl.formatURL(url[0].substring(13)) + ".jpg").getContent();
                Drawable d = Drawable.createFromStream(is, "src name");

                ret[0] = d;
                ret[1] = url[0].substring(7);
                //searchResults.add(toUrl.formatURLReverse(url[0].substring(27, url[0].lastIndexOf('.'))));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Object[] r) {

            final Object[] d = r;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        final ImageView tmp = new ImageView(con);
                        tmp.setImageDrawable((Drawable) d[0]);
                        tmp.setMaxWidth(42);
                        tmp.setMaxHeight(64);
                        banViewOp.addView(tmp);
                        banConOp.add(tmp);
                        banOp.setImageDrawable((Drawable) d[0]);

                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                }
            });


        }
    }

    private class Banish extends AsyncTask<Void, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Void... v) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ImageView tmp = (ImageView) deckView.getChildAt(0);
                        tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                        deckView.removeViewAt(0);
                        deckCon.remove(tmp);
                        ban.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                        banView.addView(tmp);


                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                os.writeUTF("BANISHDECK" + mainDeck.get(0));
                                os.flush();
                                mainDeck.remove(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object[] d) {


        }
    }

    private class Banish2 extends AsyncTask<Void, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Void... v) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ImageView tmp = (ImageView) edeckCon.get(0);
                        tmp.setForeground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                        edView.removeView(tmp);
                        ban.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
                        banView.addView(tmp);

                    }catch (Exception e){
                        ((TextView) findViewById(R.id.textView3)).setText(e.getMessage());
                    }
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        os.writeUTF("BANISHEDDECK" + extraDeck.get(0));
                        os.flush();
                        extraDeck.remove(0);
                        edeckCon.remove(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return null;
        }

        @Override
        protected void onPostExecute(Object[] d) {


        }
    }

}


