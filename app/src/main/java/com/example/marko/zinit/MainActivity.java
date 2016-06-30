package com.example.marko.zinit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Holder> holders;
    RecyclerView recyclerView;
    HoldersServices holdersServices;
    String api_key, url_search;
    RetrieveFeedTask retrieveFeedTask;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveFeedTask = new RetrieveFeedTask();
        api_key = getString(R.string.api_key);
        url_search = "https://api.ukrbash.org/1/pictures.getPublished.json";

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        retrieveFeedTask.execute(url_search + "?client=" + api_key);

    }


    class RetrieveFeedTask extends AsyncTask<String, String, List<Holder>> {

        private ProgressDialog dialog;

        public RetrieveFeedTask() {
            dialog = new ProgressDialog(MainActivity.this);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Please wait for request");
            this.dialog.show();
        }

        protected List<Holder> doInBackground(String... urls) {

            holdersServices = new HoldersServices();
            try {
                holders = holdersServices.getRecipeList(urls[0]);
            } catch (JSONException e) {
                publishProgress("Cannot get response");
            } catch (IOException e) {
                publishProgress("Unknown error");
            }

            return holders;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            showErrorDialog(values[0]);
        }

        protected void onPostExecute(List<Holder> holders) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            recyclerAdapter = new RecyclerAdapter(MainActivity.this, holders);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    private void showErrorDialog(final String massage) {
        runOnUiThread(new Runnable() {
            public void run() {
                final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage(massage);
                dialog.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                final android.app.AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

}
