package com.psquaredsoftware.ygomobile;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QuoteBank {

    private JSONArray cards;
    private File cardDb;
    private BufferedReader fileIn;
    private Context mContext;
    private HashMap<String, String[]> namesAndIds;
    public boolean initialized = false;

    public QuoteBank(Context context) {

        try {

            this.mContext = context;
            namesAndIds = new HashMap<>();

            cardDb = new File(context.getFilesDir(), "cardInfo.txt");
            fileIn = new BufferedReader(new FileReader(cardDb));

            String info;
            String params[];
            String temp[];

            while ((info = fileIn.readLine()) != null) {

                temp = info.split("\\$\\$");
                if (temp.length == 4)
                    params = new String[]{temp[1], temp[2], temp[3]};
                else
                    params = new String[]{temp[1], temp[2], temp[3], temp[4]};
                namesAndIds.put(temp[0], params);

                //Log.d("INFO", info);

            }

            Log.d("INFO", "FINISHED");

            initialized = true;

        }catch(Exception e){

            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();

        }

        /*cards = new JSONObject(fileIn.readLine()).getJSONArray("data");

        for(int i = 0; i < cards.length(); i++){

            String params[];
            if(cards.getJSONObject(i).has("banlist_info") && cards.getJSONObject(i).getJSONObject("banlist_info").has("ban_tcg")) {
                params = new String[]{(cards.getJSONObject(i).get("id") + ""), (cards.getJSONObject(i).get("type") + ""), cards.getJSONObject(i).getJSONObject("banlist_info").getString("ban_tcg")};
                Log.d("NEWBAN", params[2]);
            }else
            params = new String[]{(cards.getJSONObject(i).get("id") + ""), (cards.getJSONObject(i).get("type") + "")};
            namesAndIds.put(cards.getJSONObject(i).getString("name"), params);

        }*/


    }

    public String[] getNamesList(){

        return (String[]) namesAndIds.keySet().toArray(new String[0]);

    }

    public String getName(String id){

            for(String key : namesAndIds.keySet()){

                if(id.equals(namesAndIds.get(key)[0]))
                    return key;

            }

        return "-1";

    }

    public String getId(String cardName){

        if(namesAndIds.containsKey(cardName)){

            return namesAndIds.get(cardName)[0];

        }

        return "-1";

    }

    public char getFormat(String cardName){

        if(namesAndIds.containsKey(cardName)){

            String[] info;

            info = namesAndIds.get(cardName);

            if(info[info.length - 1].equals("TCG"))
                return 'T';
            else
                return 'O';


        }

        return 'O';

    }

    public int getStatus(String cardName){

        if(namesAndIds.containsKey(cardName)){

            String[] info;

            if((info = namesAndIds.get(cardName)).length == 4){

                if(info[2].equals("Limited"))
                    return 1;
                else if(info[2].equals("Banned"))
                    return 0;
                else
                    return 2;

            }

            return 3;

        }

        return -1;

    }

    public String getType(String cardName){

        if(namesAndIds.containsKey(cardName)){

            if(namesAndIds.get(cardName)[1].equals("Fusion Monster") || namesAndIds.get(cardName)[1].equals("Link Monster") || namesAndIds.get(cardName)[1].equals("Pendulum Effect Fusion Monster") || namesAndIds.get(cardName)[1].equals("Synchro Monster") || namesAndIds.get(cardName)[1].equals("Synchro Pendulum Effect Monster") || namesAndIds.get(cardName)[1].equals("Synchro Tuner Monster") || namesAndIds.get(cardName)[1].equals("XYZ Monster") || namesAndIds.get(cardName)[1].equals("XYZ Pendulum Effect Monster"))
                return "E";
            else
                return "M";

        }

        return "-1";

    }

    public boolean getXYZ(String cardName){

        if(namesAndIds.containsKey(cardName)){

            Log.d("Loc", namesAndIds.get(cardName)[1]);

            if(namesAndIds.get(cardName)[1].equals("XYZ Monster") || namesAndIds.get(cardName)[1].equals("XYZ Pendulum Effect Monster"))
                return true;
            else
                return false;

        }

        return false;

    }

    public List<String> readLine(String param) {
        List<String> mLines = new ArrayList<>();

        Iterator mapIt = namesAndIds.entrySet().iterator();
        int i = 0;

        while (mapIt.hasNext() && i <= 15){

            Map.Entry card = (Map.Entry) mapIt.next();
            if(((String)card.getKey()).toUpperCase().contains(param.toUpperCase())){

                mLines.add(namesAndIds.get(card.getKey())[0]);
                i++;

            }

        }

        /*AssetManager am = mContext.getAssets();

        try {
            InputStream is = am.open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                if(line.toUpperCase().contains(param.toUpperCase()))
                mLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return mLines;
    }
}
