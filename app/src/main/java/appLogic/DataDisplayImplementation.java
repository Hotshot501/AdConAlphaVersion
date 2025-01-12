package appLogic;

import android.annotation.SuppressLint;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import dataStrorage.DataStorageImplementation;
import dataTypes.User;
import dataTypes.UserRating;

public class DataDisplayImplementation implements DataDisplayInterface {

    private static DataDisplayImplementation dataDisplay;

    public static DataDisplayImplementation getDataDisplayImplementation() {
        if(dataDisplay == null) {
            dataDisplay = new DataDisplayImplementation();
        }
        return dataDisplay;
    }
    @SuppressLint("NewApi")
    @Override
    public String getName(String userID)
            throws IllegalArgumentException{
        if(userID == null || !Files.exists(Path.of("users\\"+userID))) {
            throw new IllegalArgumentException("The user could not be found.");
        }
        return DataStorageImplementation.getDataStorage().getUser(userID).getName();
    }
    @SuppressLint("NewApi")
    @Override
    public int getAge(String userID)
            throws IllegalArgumentException{
        if(userID == null || !Files.exists(Path.of("users\\"+userID))) {
            throw new IllegalArgumentException("The user could not be found.");
        }
        return DataStorageImplementation.getDataStorage().getUser(userID).getAge();
    }
    @SuppressLint("NewApi")
    @Override
    public String getCountry(String userID)
            throws IllegalArgumentException{
        if(userID == null || !Files.exists(Path.of("users\\"+userID))) {
            throw new IllegalArgumentException("The user could not be found.");
        }
        return DataStorageImplementation.getDataStorage().getUser(userID).getCountry();
    }
    @SuppressLint("NewApi")
    @Override
    public String getLanguage(String userID)
            throws IllegalArgumentException{
        if(userID == null || !Files.exists(Path.of("users\\"+userID))) {
            throw new IllegalArgumentException("The user could not be found.");
        }
       return DataStorageImplementation.getDataStorage().getUser(userID).getLanguage();
    }
    @SuppressLint("NewApi")
    @Override
    public String getDescription(String userID)
            throws IllegalArgumentException{
        if(userID == null || !Files.exists(Path.of("users\\"+userID))) {
            throw new IllegalArgumentException("The user could not be found.");
        }
        return DataStorageImplementation.getDataStorage().getUser(userID).getDescription();
    }
    @SuppressLint("NewApi")
    @Override
    public byte[] getProfilePicture(String userID)
            throws IllegalArgumentException{
        if(userID == null || !Files.exists(Path.of("users\\"+userID))) {
            throw new IllegalArgumentException("The user could not be found.");
        }
        return DataStorageImplementation.getDataStorage().getUser(userID).getProfilePicture();
    }
    @SuppressLint("NewApi")
    @Override
    public void setName(String name)
            throws IllegalArgumentException{
        if(name == null || !Files.exists(Path.of("users\\main"))) {
            throw new IllegalArgumentException("The user could not be edited.");
        }
        User main = DataStorageImplementation.getDataStorage().getUser("main");
        main.setName(name);
        DataStorageImplementation.getDataStorage().toStoreMain(main);
    }
    @SuppressLint("NewApi")
    @Override
    public void setCountry(String country)
            throws IllegalArgumentException{
        if(country == null || !Files.exists(Path.of("users\\main"))) {
            throw new IllegalArgumentException("The user could not be edited.");
        }
        User main = DataStorageImplementation.getDataStorage().getUser("main");
        main.setCountry(country);
        DataStorageImplementation.getDataStorage().toStoreMain(main);
    }
    @SuppressLint("NewApi")
    @Override
    public boolean addLanguage(String language)
            throws IllegalArgumentException{
        if(language == null || !Files.exists(Path.of("users\\main"))) {
            throw new IllegalArgumentException("The user could not be edited.");
        }
        User main = DataStorageImplementation.getDataStorage().getUser("main");
        language = " "+language;
        boolean success = main.addLanguage(language);
        if(success == true) {
            DataStorageImplementation.getDataStorage().toStoreMain(main);
        }
        return success;
    }
    @SuppressLint("NewApi")
    @Override
    public boolean removeLanguage(String language)
            throws IllegalArgumentException{
        if(language == null || !Files.exists(Path.of("users\\main"))) {
            throw new IllegalArgumentException("The user could not be edited.");
        }
        User main = DataStorageImplementation.getDataStorage().getUser("main");
        boolean success = main.removeLanguage(language);
        if(success == true) {
            DataStorageImplementation.getDataStorage().toStoreMain(main);
        }
        return success;
    }
    @SuppressLint("NewApi")
    @Override
    public void setDescription(String description)
            throws IllegalArgumentException{
        if(description == null || !Files.exists(Path.of("users\\main"))) {
            throw new IllegalArgumentException("The user could not be edited.");
        }
        User main = DataStorageImplementation.getDataStorage().getUser("main");
        main.setDescription(description);
        DataStorageImplementation.getDataStorage().toStoreMain(main);
    }
    @SuppressLint("NewApi")
    @Override
    public void setProfilePicture(File picture)
            throws IllegalArgumentException{
        if(picture == null || !Files.exists(Path.of("users\\main"))) {
            throw new IllegalArgumentException("The user could not be edited.");
        }
        User main =     DataStorageImplementation.getDataStorage().getUser("main");
        main.setProfilePicture(picture);
        DataStorageImplementation.getDataStorage().toStoreMain(main);
    }
    @Override
    public UserRating createRating(String userID, int rating)
        throws IllegalArgumentException{
        if(userID == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("The user rating could not be created.");
        }
        return new UserRating(userID, DataStorageImplementation.getDataStorage().getUser("main").getUserID(), rating);
    }
}
