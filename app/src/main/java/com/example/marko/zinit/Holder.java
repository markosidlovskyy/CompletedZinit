package com.example.marko.zinit;

import android.graphics.Bitmap;

/**
 * Created by Marko on 6/30/2016.
 */
public class Holder {

    Bitmap image;
    String imageUrl, title;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
