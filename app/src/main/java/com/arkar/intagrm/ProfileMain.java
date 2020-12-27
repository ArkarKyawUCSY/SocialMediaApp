package com.arkar.intagrm;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import static com.parse.ParseUser.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileMain extends Fragment {

    EditText edtProfileName, edtBio, edtProfession, edtHobbies, edtFavSports;
    Button btnUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         //Inflate layout for this fragment
         View view = inflater.inflate(R.layout.fragment_profile_main,container,false);
         edtProfileName = view.findViewById(R.id.edt_profile_name);
         edtBio = view.findViewById(R.id.edt_bio);
         edtProfession = view.findViewById(R.id.edt_proffession);
         edtHobbies = view.findViewById(R.id.edt_hobbies);
         edtFavSports = view.findViewById(R.id.edt_fav_sport);
         btnUpdate = view.findViewById(R.id.btn_update_info);

            final ParseUser parseUser = ParseUser.getCurrentUser();
            if(parseUser.get("profileName") == null) {
                edtProfileName.setText("");
            } else {
                edtProfileName.setText(parseUser.get("profileName").toString());
            }
            edtBio.setText(parseUser.get("profileBio")+"");
            edtHobbies.setText(parseUser.get("profileHobbies")+"");
            edtProfession.setText(parseUser.get("profileProfession")+"");
            edtFavSports.setText(parseUser.get("profileFavSports")+"");

         btnUpdate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.d("TAG","ProfileName" +edtProfileName.getText().toString());
                 Log.d("TAG","Bio" +edtBio.getText().toString());
                 Log.d("TAG","Profession" +edtProfession.getText().toString());
                 Log.d("TAG","Hobbies" +edtHobbies.getText().toString());
                 Log.d("TAG","FavSorot" +edtFavSports.getText().toString());
                 parseUser.put("profileName", edtProfileName.getText().toString());
                 parseUser.put("profileBio",edtBio.getText().toString());
                 parseUser.put("profileProfession",edtProfession.getText().toString());
                 parseUser.put("profileHobbies",edtHobbies.getText().toString());
                 parseUser.put("profileFavSports",edtFavSports.getText().toString());
                 parseUser.saveInBackground(new SaveCallback() {
                     @Override
                     public void done(ParseException e) {
                         if (e == null) {
                             FancyToast.makeText(getContext(),"Info Updated",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                         } else {
                             FancyToast.makeText(getContext(),"Info Error Updated"+e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                         }
                     }
                 });
             }
         });
    return view;
    }
}