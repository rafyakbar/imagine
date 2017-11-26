package com.majapahit.imagine.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.majapahit.imagine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafy on 25/11/2017.
 */

public class FriendListAdapter extends ArrayAdapter<String> {
    private Activity context;
    List<StorageReference> image = new ArrayList();
    List<String> name = new ArrayList();
    List<String> about = new ArrayList();

    public FriendListAdapter(Activity context, List image, List name, List about) {
        super(context, R.layout.list_friend, name);
        this.context = context;
        this.name = name;
        this.about = about;
        this.image = image;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_friend, null, true);
        TextView txtName = rowView.findViewById(R.id.name_list_friend);
        TextView txtAbout = rowView.findViewById(R.id.about_list_friend);
        ImageView imageView = rowView.findViewById(R.id.image_list_friend);
        Glide.with(getContext()).using(new FirebaseImageLoader()).load(image.get(position)).into(imageView);
        txtName.setText(this.name.get(position));
        txtAbout.setText(this.about.get(position));

        return rowView;
    }
}
