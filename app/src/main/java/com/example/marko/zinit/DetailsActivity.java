package com.example.marko.zinit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Marko on 6/30/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();

        byte[] byteArray = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String title = bundle.getString("title");

        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.title);

        imageView.setImageBitmap(bmp);
        textView.setText(title);
    }
}
