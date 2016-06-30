package com.example.marko.zinit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    List<Holder> holders;
    RecyclerView recyclerView;
    HoldersServices holdersServices;
    String api_key, url_search;
    RetrieveFeedTask retrieveFeedTask;
    RecyclerAdapter recyclerAdapter;
    Handler handler;
    Boolean dialogRun = false;
    Runnable runnable;

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

    private void startTimer() {
        dialogRun = true;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startDialog();
            }
        };
        handler.postDelayed(runnable, 1000 * 60 * 2);
    }

    private void startDialog() {
        if (holders.size() == 0)
            return;

        LayoutInflater inflater = LayoutInflater.from(this);
        final View alertDialog = inflater.inflate(R.layout.alert_dialog, null);
        ImageView imageView = (ImageView) alertDialog
                .findViewById(R.id.selectedImage);
        TextView textView = (TextView) alertDialog.findViewById(R.id.text);
        Holder holder = holders.get(new Random().nextInt(holders.size()));
        Calendar c = Calendar.getInstance();
        String title = new SimpleDateFormat("HH:mm").format(c.getTime());
        final String text = holder.getTitle();
        final Bitmap bitmap = holder.getImage();
        textView.setText(text);
        imageView.setImageBitmap(bitmap);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setView(alertDialog)
                .setPositiveButton("Перейти до опису", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goToDetailsActivity(bitmap, text);
                    }
                })
                .setNegativeButton("Відміна", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startTimer();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                            startTimer();
                        return false;
                    }
                })
                .show();
    }

    private void goToDetailsActivity(Bitmap bitmap, String title) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.putExtra("image", byteArray);
        intent.putExtra("title", title);

        startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!dialogRun)
            startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
            dialogRun = false;
        }
    }
}
