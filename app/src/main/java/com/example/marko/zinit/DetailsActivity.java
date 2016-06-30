package com.example.marko.zinit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Marko on 6/30/2016.
 */
public class DetailsActivity extends Activity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        Holder holder = bundle.getParcelable("item");

        imageView=(ImageView) findViewById(R.id.image);
        textView=(TextView) findViewById(R.id.title);

        imageView.setImageBitmap(holder.getImage());
        textView.setText(holder.getTitle());
    }
}
