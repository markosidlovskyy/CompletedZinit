package com.example.marko.zinit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Marko on 6/30/2016.
 */
public class HoldersServices {

    List<Holder> holders;

    public List<Holder> getRecipeList(String url) throws JSONException, IOException {
        JSONArray jsonArray = null;
        holders = new ArrayList<>();

        URL urlHttp = new URL(url);
        HttpURLConnection urlConnection = (HttpsURLConnection) urlHttp.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        String json = reader.readLine();

        jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            Holder holder = new Holder();

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            holder.setTitle(Html.fromHtml(jsonObject.getString("title")).toString());
            holder.setImageUrl(Html.fromHtml(jsonObject.getString("image")).toString());

            InputStream in = new java.net.URL(holder.getImageUrl()).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(in);

            holder.setImage(bitmap);

            holders.add(holder);
        }

        return holders;
    }

}
