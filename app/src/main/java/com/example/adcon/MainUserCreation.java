package com.example.adcon;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adcon.databinding.FragmentMainUserCreationBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;

import dataStrorage.CompressFile;
import dataStrorage.DataStorageImplementation;
import dataTypes.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainUserCreation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainUserCreation extends Fragment {

    private static File profilePicture;

    public MainUserCreation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainUserCreation.
     */
    // TODO: Rename and change types and number of parameters
    public static MainUserCreation newInstance(String param1, String param2) {
        MainUserCreation fragment = new MainUserCreation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Files.exists(new File(getContext().getFilesDir(), "users/main").toPath())) {
            this.transferToMainScreen();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_user_creation, container, false);
        view.findViewById(R.id.SubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        view.findViewById(R.id.SelectImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelection();
            }
        });
        DataStorageImplementation.setContext(getContext());
        return view;
    }

    //code snippets for image selection from https://stackoverflow.com/questions/75102472/how-to-pick-image-from-gallery-on-fragment
    private void openImageSelection() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 3838);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectProfilePicture(data.getData());
        TextView imagePath = getView().findViewById(R.id.imagePathText);
        imagePath.setText(getRealPathFromURI(data.getData()));
    }

    // code from https://www.androidsnippets.com/get-file-path-of-gallery-image.html
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContext().getContentResolver().query( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @SuppressLint("NewApi")
    private void submit() {
        if(entriesAreLegal()) {
            User main = new User(this.getName(), this.getBirthday().getDayOfMonth(), this.getBirthday().getMonthValue(), this.getBirthday().getYear(), this.getCountry(), this.getLanguage(), this.getDescription(), profilePicture);
            DataStorageImplementation.getDataStorage().toStoreMain(main);
            MainScreen.addUsersToList(getTestUser());
            this.transferToMainScreen();
        }
    }

    @SuppressLint("NewApi")
    private boolean entriesAreLegal() {
        String name = this.getName();
        LocalDate dob = null;
        try{
            dob = this.getBirthday();
        } catch(DateTimeException e) {
            return false;
        }
        String country = this.getCountry();
        String language = this.getLanguage();
        String description = this.getDescription();
        if(name.isEmpty()
                || dob == null
                || profilePicture == null
                || country.isEmpty()
                || language.isEmpty()
                || description.isEmpty()) {
            return false;
        }
        if(description.length() > 100) {
            return false;
        }
        return true;
    }

    private String getName() {
        EditText nameEdit = getView().findViewById(R.id.NameEditText);
        return nameEdit.getText().toString();
    }

    @SuppressLint("NewApi")
    private LocalDate getBirthday()
        throws DateTimeException {
        EditText ageEdit = getView().findViewById(R.id.DOBEditText);
        String dob = ageEdit.getText().toString();
        if(!dob.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new DateTimeException("not a date");
        }
        int day;
        if(dob.substring(0, 1).equals("0")) {
            day = Integer.parseInt(dob.substring(1, 2));
        } else {
            day = Integer.parseInt(dob.substring(0, 2));
        }
        int month;
        if(dob.substring(3, 4).equals("0")) {
            month = Integer.parseInt(dob.substring(4, 5));
        } else {
            month = Integer.parseInt(dob.substring(3, 5));
        }
        int year = Integer.parseInt(dob.substring(6, 10));
        LocalDate birthday = LocalDate.of(year, month, day);
        return birthday;
    }

    private String getCountry() {
        EditText countryEdit = getView().findViewById(R.id.HomeCountryEditText);
        return countryEdit.getText().toString();
    }

    private String getLanguage() {
        EditText languageEdit = getView().findViewById(R.id.LanguagesEditText);
        return languageEdit.getText().toString();
    }

    private String getDescription() {
        EditText descriptionEdit = getView().findViewById(R.id.DescriptionEditText);
        return descriptionEdit.getText().toString();
    }

    private void transferToMainScreen() {
        NavHostFragment.findNavController(MainUserCreation.this).navigate((R.id.action_mainUserCreation_to_mainScreen));
    }

    public void selectProfilePicture(Uri uri) {
        if(uri == null) {
            throw new NullPointerException("uri is null");
        }
        profilePicture = new File(getRealPathFromURI(uri));
        profilePicture = CompressFile.getCompressedImageFile(profilePicture, getContext());
        if(profilePicture.length() < 800000) {
            profilePicture = null;
        }
    }

    public static User getTestUser() {
        User testUser = new User("tester", 12, 8, 2004, "England", "English", "this is a test-dummy", profilePicture);
        DataStorageImplementation.getDataStorage().toStore(testUser);
        return testUser;
    }

}