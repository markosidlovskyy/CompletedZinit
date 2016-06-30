package com.example.marko.zinit;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Marko on 6/30/2016.
 */
public class Holder implements Parcelable {

    Bitmap image;
    String imageUrl, title;

    public Holder()
    {}

    protected Holder(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        imageUrl = in.readString();
        title = in.readString();
    }

    public static final Creator<Holder> CREATOR = new Creator<Holder>() {
        @Override
        public Holder createFromParcel(Parcel in) {
            return new Holder(in);
        }

        @Override
        public Holder[] newArray(int size) {
            return new Holder[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(image, flags);
        dest.writeString(imageUrl);
        dest.writeString(title);
    }
}
