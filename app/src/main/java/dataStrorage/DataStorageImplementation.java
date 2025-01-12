package dataStrorage;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.adcon.MainScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import dataTypes.User;
import dataTypes.UserRating;

public class DataStorageImplementation implements DataStorageInterface {

    FileOutputStream userStorage;
    FileOutputStream ratingStorage;
    InputStream reader;
    private static DataStorageImplementation dataStorage;

    private static Context context = null;

    public static DataStorageImplementation getDataStorage() {
        if(dataStorage == null){
            dataStorage = new DataStorageImplementation();
        }
        return dataStorage;
    }
    @Override
    @SuppressLint("NewApi")
    public boolean toStore(User user)
        throws NullPointerException {
        if(user == null) {
            throw new NullPointerException("the user may not be null");
        }
        File dir = new File(context.getFilesDir(),"users");
        dir.mkdirs();
        String fileName = user.getUserID();
        try {
            Files.deleteIfExists(Paths.get("users", fileName));
        } catch (IOException e) {
            return false;
        }
        File tmp = new File(dir, fileName);
        try {
            tmp.createNewFile();
        } catch (IOException e) {
            return false;
        }
        try {
            userStorage = new FileOutputStream(tmp);
        } catch (FileNotFoundException e) {
            return false;
        }
        try (ObjectOutputStream users = new ObjectOutputStream(userStorage)){
            users.writeObject(user);
        } catch (IOException e) {
            return false;
        }
        try {
            userStorage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @SuppressLint("NewApi")
    public boolean toStoreMain(User user)
            throws NullPointerException {
        if(user == null) {
            throw new NullPointerException("the user may not be null");
        }
        File dir = new File(context.getFilesDir(), "users");
        dir.mkdirs();
        String fileName = "main";
        try {
            Files.deleteIfExists(Paths.get("users", fileName));
        } catch (IOException e) {
            return false;
        }
        File tmp = new File(dir, fileName);
        try {
            tmp.createNewFile();
        } catch (IOException e) {
            return false;
        }
        try {
            userStorage = new FileOutputStream(tmp);
        } catch (FileNotFoundException e) {
            return false;
        }
        try (ObjectOutputStream users = new ObjectOutputStream(userStorage)){
            users.writeObject(user);
        } catch (IOException e) {
            return false;
        }
        try {
            userStorage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    @SuppressLint("NewApi")
    public boolean toStore(UserRating rating)
        throws NullPointerException {
        if(rating == null) {
            throw new NullPointerException("the rating may not be null");
        }
        File dir = new File(context.getFilesDir(), "ratings");
        dir.mkdirs();
        String fileName = rating.getRatingID();
        File tmp = new File(dir, fileName);
        try {
            tmp.createNewFile();
        } catch (IOException e) {
            return false;
        }
        try {
            ratingStorage = new FileOutputStream(tmp);
        } catch (FileNotFoundException e) {
            return false;
        }
        try (ObjectOutputStream ratings = new ObjectOutputStream(ratingStorage)){
            ratings.writeObject(rating);
        } catch (IOException e) {
            return false;
        }
        try {
            ratingStorage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public User getUser(String userID) {
        String fileName = userID;
        User user = null;
        try {
            reader = new FileInputStream(new File(context.getFilesDir(), "users/"+fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try(ObjectInputStream users = new ObjectInputStream(reader)) {
            user = (User)users.readObject();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user == null) {
            throw new NullPointerException("No user was read");
        }
        return user;
    }
    @Override
    public UserRating getUserRating(String ratingID){
        String fileName = ratingID;
        UserRating rating = null;
        try {
            reader = new FileInputStream("ratings/"+fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try(ObjectInputStream users = new ObjectInputStream(reader)) {
            rating = (UserRating)users.readObject();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rating == null) {
            throw new NullPointerException("No user was read");
        }
        return rating;
    }

    public static void setContext(Context storageContext) {
        context = storageContext;
    }
}

