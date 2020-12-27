package com.arkar.intagrm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Social Media");

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.my_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.postImageItem) {
            // android marshmallow and above
            if (Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, 3000);
            } else {
                captureImage();
            }
        } else if (item.getItemId() == R.id.logoutUserItem) {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);

            ParseUser.getCurrentUser().logOut();
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3000) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }
    }

    private void captureImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4000 && resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();

                // captured image is assigned to mBitmap as a bitmap
                 bitmap = MediaStore.Images.Media.
                        getBitmap(this.getContentResolver(), selectedImage);

                Toast.makeText(LoginActivity.this, "Your image is being uploaded to the server," + "\n" +
                        "Please Wait for a message showing it has completed", Toast.LENGTH_SHORT).show();

                // redirect to Async Task to divert non-UI coding from main thread
                AsyncTask CompressAndSend = new CompressAndSend().execute();

                // return from Async thread

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CompressAndSend extends AsyncTask<String, Void, ParseObject> {
        @Override
        protected ParseObject doInBackground(String... strings) {

            // create a new instance of byteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


            // compression takes (format, quality, bytearrayoutputstream object) as parameters
            // this process appears to compress a png file and rewrite to storage using the same file name, or it could be stored in memory (not sure which)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            // convert the byteArrayOutputStream (compressed?) to a Byte Array and store it in bytes
            byte[] bytes = byteArrayOutputStream.toByteArray();

            ParseFile parseFile = new ParseFile("img.png", bytes);
            ParseObject parseObject = new ParseObject("Photo");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM:dd:yy", Locale.JAPAN);
            parseObject.put("picture", parseFile);
            parseObject.put("image_des", "my pic " + dateFormat);
            parseObject.put("username", ParseUser.getCurrentUser().getUsername());


            return parseObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ParseObject parseObject) {
            super.onPostExecute(parseObject);

            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(LoginActivity.this, "Your image has been posted to the server",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(LoginActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}