package com.example.adcon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import dataStrorage.DataStorageImplementation;
import dataTypes.UserRating;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserScreen extends Fragment {

    private static String userID = "main";

    public UserScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static UserScreen newInstance() {
        UserScreen fragment = new UserScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_screen, container, false);
        DataStorageImplementation ds = DataStorageImplementation.getDataStorage();
        TextView name = view.findViewById(R.id.nameInfoValue);
        TextView age = view.findViewById(R.id.ageInfoValue);
        TextView country = view.findViewById(R.id.countryInfoValue);
        TextView language = view.findViewById(R.id.languageInfoValue);
        TextView description = view.findViewById(R.id.descriptionInfoValue);
        ImageView profilePicture = view.findViewById(R.id.profilePictureView);
        TextView currentRating = view.findViewById(R.id.ratingTextValue);
        name.setText(ds.getUser(UserScreen.userID).getName());
        age.setText(String.valueOf(ds.getUser(UserScreen.userID).getAge()));
        country.setText(ds.getUser(UserScreen.userID).getCountry());
        language.setText(ds.getUser(UserScreen.userID).getLanguage());
        description.setText(ds.getUser(UserScreen.userID).getDescription());
        currentRating.setText(String.valueOf(ds.getUser(UserScreen.userID).getUserRating()));
        Bitmap profilePictureBitmap = BitmapFactory.decodeByteArray(ds.getUser(UserScreen.userID).getProfilePicture(), 0, ds.getUser(UserScreen.userID).getProfilePicture().length);
        if(profilePictureBitmap.getWidth() > profilePictureBitmap.getHeight()) {
            view.findViewById(R.id.profilePictureView).setRotation(90);
        }
        profilePicture.setImageBitmap(profilePictureBitmap);
        view.findViewById(R.id.rateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserScreen.userID.equals(ds.getUser("main").getUserID())) {
                    return;
                }
                EditText rate = getView().findViewById(R.id.rateUserEditText);
                String rated = rate.getText().toString();
                if(rated.isEmpty()) {
                    return;
                }
                int rating = Integer.parseInt(rate.getText().toString());
                UserRating ur = new UserRating(UserScreen.userID, ds.getUser("main").getUserID(), rating);
                ds.toStore(ur);
            }
        });
        return view;
    }

    public static void setUserID(String userID) {
        UserScreen.userID = userID;
    }

}