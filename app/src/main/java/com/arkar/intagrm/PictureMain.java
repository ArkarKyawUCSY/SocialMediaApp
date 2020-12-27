package com.arkar.intagrm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ManifestInfo;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PictureMain extends Fragment implements View.OnClickListener {
    ImageView imageView;
    EditText edtDescription;
    Button btnShare;
    Bitmap receiveImageBitmap;
    ProgressBar progressBar;
    ConstraintLayout constraintLayoutPic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_picture_main,container,false);
        imageView = view.findViewById(R.id.sample_image);
        edtDescription = view.findViewById(R.id.edt_description);
        btnShare = view.findViewById(R.id.btn_share);
        constraintLayoutPic = view.findViewById(R.id.constrain_pic);

        imageView.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sample_image:
                // android marshmallow and above
                if (Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                } else {
                    getChosenImage();
                }
                break;
            case R.id.btn_share:
                if(receiveImageBitmap != null) {
                    if (edtDescription.getText().toString().equals("")) {
                        FancyToast.makeText(getContext(),"Please Fill Description",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    } else {
                        AsyncTask uploadImage = new uploadImage().execute();
                    }
                } else {
                    FancyToast.makeText(getContext(),"Please Choose An Image",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
                    break;
        }
    }

    class uploadImage extends AsyncTask<String, Void, ParseObject>

    {

        @Override
        protected ParseObject doInBackground(String... strings) {

            // converted into byte
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            receiveImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            ParseFile parseFile = new ParseFile("img.png", bytes);
            ParseObject parseObject = new ParseObject("Photo");
            parseObject.put("picture", parseFile);
            parseObject.put("image_des", edtDescription.getText().toString());
            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.show();
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(getContext(), "Done!!!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        dialog.dismiss();
                    } else {
                        FancyToast.makeText(getContext(), "Unknown error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                }
            });

            return parseObject;
        }
    }

    private void getChosenImage() {
        //FancyToast.makeText(getContext(),"Now we can access image",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == 2000) {
           if (resultCode == Activity.RESULT_OK) {
               try {
                   Uri selectedImage = data.getData();
                   String[] filePathColumn = {MediaStore.Images.Media.DATA};
                   Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                   cursor.moveToFirst();
                   int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                   String picturePath = cursor.getString(columnIndex);
                   cursor.close();
                   receiveImageBitmap = BitmapFactory.decodeFile(picturePath);
                   imageView.setImageBitmap(receiveImageBitmap);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
    }
}