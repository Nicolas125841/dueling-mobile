package com.psquaredsoftware.ygomobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class myDbAdapter {
    myDbHelper myhelper;
    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public void exportDb() throws IOException {

        SQLiteDatabase db = myhelper.getWritableDatabase();
        File dbFile = new File(db.getPath());
        File exportFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/yugiohmobile02");
        if(exportFile.exists())
            exportFile.delete();
        exportFile.createNewFile();
        FileInputStream fis = new FileInputStream(dbFile);
        FileOutputStream fos = new FileOutputStream(exportFile);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = fis.read(buffer)) > 0) {

            fos.write(buffer, 0, length);
            Log.d("CONT", new String(buffer));

        }

        fis.close();
        fos.flush();
        fos.close();

        Log.d("RESULT", "GOOD");

    }

    public int importDb(){

        try {

            SQLiteDatabase db = myhelper.getWritableDatabase();
            File exportFile = new File(db.getPath());
            File dbFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/yugiohmobile02");
            Log.d("ADR", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            if (exportFile.exists())
                exportFile.delete();
            exportFile.createNewFile();
            FileInputStream fis = new FileInputStream(dbFile);
            FileOutputStream fos = new FileOutputStream(exportFile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {

                fos.write(buffer, 0, length);

            }

            fis.close();
            fos.flush();
            fos.close();

            Log.d("RESULT", "GOOD");

            return 0;

        }catch (FileNotFoundException e){

            e.printStackTrace();
            return 1;

        } catch (IOException e) {

            e.printStackTrace();
            return 2;

        }

    }

    public long insertData(String name, String pass, String size)
    {

        String[] curn = getData();
        for(int i = 0; i < curn.length; i++){

            if(curn[i].equals(name))
                return -1;

        }
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.MyDeck, pass);
        contentValues.put(myDbHelper.MainCount, size);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return 0;
    }

    public String[] getData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.NAME,myDbHelper.MainCount,myDbHelper.MyDeck};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> buffer= new ArrayList<String>();
        if(cursor == null)
            return null;
        while (cursor.moveToNext())
        {
            //int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            //String deck =cursor.getString(cursor.getColumnIndex(myDbHelper.MyDeck));
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.MainCount))) > 0)
            buffer.add(name);
        }
        return buffer.toArray(new String[0]);
    }

    public String[] getDataGame()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.NAME,myDbHelper.MainCount,myDbHelper.MyDeck};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> buffer= new ArrayList<String>();
        if(cursor == null)
            return null;
        while (cursor.moveToNext())
        {
            //int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            //String deck =cursor.getString(cursor.getColumnIndex(myDbHelper.MyDeck));
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.MainCount))) >= 40)
            buffer.add(name);

        }
        return buffer.toArray(new String[0]);
    }

    public String[] getDataGameOCG(QuoteBank reference)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.NAME,myDbHelper.MainCount,myDbHelper.MyDeck};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> buffer= new ArrayList<String>();
        if(cursor == null)
            return null;
        while (cursor.moveToNext())
        {
            //int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));

            //String deck =cursor.getString(cursor.getColumnIndex(myDbHelper.MyDeck));
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.MainCount))) >= 40){

                //Log.d("NAME", name);
                String[] contents = getDataDeck(name)[0].split("&&");
                boolean good = true;
                String oldComp = "";
                int oldCompCount = 0;
                for(int i = 0; i < contents.length; i++){

                    if(contents[i].equals("EXTRADECK") || contents[i].equals("SIDEDECK"))
                        continue;

                    if(!contents[i].equals(oldComp)){

                        oldComp = contents[i];
                        oldCompCount = 0;

                    }

                    oldCompCount++;

                    if(reference.getStatus(reference.getName(contents[i])) < oldCompCount){
                        //Log.d("CARDBROKES", contents[i]);
                        good = false;
                        break;

                    }

                }

                if(good)
                    buffer.add(name);

            }


        }
        return buffer.toArray(new String[0]);
    }

    public String[] getDataGameTCG(QuoteBank reference)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.NAME,myDbHelper.MainCount,myDbHelper.MyDeck};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> buffer= new ArrayList<String>();
        if(cursor == null)
            return null;
        while (cursor.moveToNext())
        {
            //int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));

            //String deck =cursor.getString(cursor.getColumnIndex(myDbHelper.MyDeck));
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.MainCount))) >= 40){

                //Log.d("NAME", name);
                String[] contents = getDataDeck(name)[0].split("&&");
                boolean good = true;
                String oldComp = "";
                int oldCompCount = 0;
                for(int i = 0; i < contents.length; i++){

                        if(contents[i].equals("EXTRADECK") || contents[i].equals("SIDEDECK"))
                            continue;

                       if(reference.getFormat(reference.getName(contents[i])) == 'O'){
                           //Log.d("CARDBROKET", contents[i]);
                           good = false;
                           break;
                       }

                       if(!contents[i].equals(oldComp)){

                           oldComp = contents[i];
                           oldCompCount = 0;

                       }

                       oldCompCount++;

                       if(reference.getStatus(reference.getName(contents[i])) < oldCompCount){
                           //Log.d("CARDBROKES", contents[i]);
                           good = false;
                           break;

                       }

                }

                if(good)
                buffer.add(name);

            }


        }
        return buffer.toArray(new String[0]);
    }

    public int getDataDeckCount()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.NAME,myDbHelper.MainCount,myDbHelper.MyDeck};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> buffer= new ArrayList<String>();
        if(cursor == null)
            return 0;
        while (cursor.moveToNext())
        {

            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.MainCount))) >= 40)
                return 1;

        }
        return 0;
    }

    public String[] getDataDeck(String uname)
    {
        String[] whereArgs ={uname};
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.NAME,myDbHelper.MainCount,myDbHelper.MyDeck};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns, myDbHelper.NAME+" = ?",whereArgs,null,null,null);
        ArrayList<String> buffer= new ArrayList<String>();
        if(cursor == null)
            return null;
        while (cursor.moveToNext())
        {
            //int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.MyDeck));
            //String deck =cursor.getString(cursor.getColumnIndex(myDbHelper.MyDeck));
            buffer.add(name);
        }

        return buffer.toArray(new String[0]);
    }

    public  int delete(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.NAME+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "yugiohmobile02";    // Database Name
        private static final String TABLE_NAME = "decks02";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String NAME = "Name";    //Column II
        private static final String MyDeck = "Deck";    // Column III
        private static final String MainCount = "Size";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+NAME+" VARCHAR(255) ,"+ MainCount + " VARCHAR(255) ," +MyDeck+" VARCHAR(225));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                //Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                //Message.message(context,""+e);
            }
        }
    }
}
