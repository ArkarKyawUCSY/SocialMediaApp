package com.arkar.intagrm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UserPostActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);
        setTitle("User Profile");

        linearLayout = findViewById(R.id.user_scroll_linear);
        Intent intent = getIntent();
        final String receiveUser = intent.getStringExtra("username");
        FancyToast.makeText(UserPostActivity.this, receiveUser,FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username",receiveUser);
        parseQuery.orderByDescending("createdAt");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject post: objects) {
                        final TextView postDes = new TextView(UserPostActivity.this);
                        postDes.setText(post.get("image_des") + "");
                        ParseFile postPic = (ParseFile) post.get("picture");
                        postPic.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                               if (data != null && e ==null) {

                                   Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                   ImageView postImageView = new ImageView(UserPostActivity.this);
                                   LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                                           ViewGroup.LayoutParams.MATCH_PARENT,
                                           ViewGroup.LayoutParams.WRAP_CONTENT);
                                   imageParams.setMargins(5, 5, 5, 5);
                                   postImageView.setLayoutParams(imageParams);
                                   postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                   postImageView.setImageBitmap(bitmap);

                                   LinearLayout.LayoutParams desParams = new LinearLayout.LayoutParams(
                                           ViewGroup.LayoutParams.MATCH_PARENT,
                                           ViewGroup.LayoutParams.WRAP_CONTENT);
                                   desParams.setMargins(5, 5, 5, 15);
                                   postDes.setLayoutParams(desParams);
                                   postDes.setGravity(Gravity.CENTER);
                                   postDes.setBackgroundColor(Color.BLUE);
                                   postDes.setTextColor(Color.WHITE);
                                   postDes.setTextSize(30f);
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDes);

                               }
                            }
                        });

                        }
                } else {
                        FancyToast.makeText(UserPostActivity.this,receiveUser+"Doesn't have any posts!!",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                        finish();
                }
            }
        });

    }
}