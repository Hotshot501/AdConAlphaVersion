package dataTypes;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {

    public static final int TEN_MB = 10000000;
    private static final int MAX_RATINGS = 250000000;
    //user ID under which a user can be identified. Needs to be bijective
    private final String userID;
    //firstname of the user
    private String name;
    //birthday of the User
    private int day;
    private int month;
    private int year;
    //age of the user
    private int age;
    //home country of the user
    private String country;
    //languages the User speaks
    private String language;
    //description of the User
    private String description;
    //profile picture of the user
    private byte[] profilePicture;

    private double userRating;
    private int ratingsReceived;

    private LinkedList<String> receivedRatingIds;

    public User(String name, int day, int month, int year, String country, String language, String description, File profilePicture)
            throws IllegalArgumentException{
        if(country == null || language == null || profilePicture == null) {
            throw new NullPointerException("A User may not be created under the use of null arguments.");
        }
        this.userID = ID.getID();
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.country = country;
        this.language = language;
        this.description = description;
        this.userRating = 0;
        this.ratingsReceived = 0;
        if(profilePicture.length() > TEN_MB) {
            throw new IllegalArgumentException("The size of your profile picture is to big. It may only be as big as 10MB.");
        }
        String picFileName = profilePicture.getName();
        if(!picFileName.endsWith(".jpg") && !picFileName.endsWith(".jpeg")){
            throw new IllegalArgumentException("The profile picture has to be a jpg file.");
        }
        this.profilePicture = new byte[(int)profilePicture.length()];
        try (FileInputStream fis = new FileInputStream(profilePicture)){
            fis.read(this.profilePicture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.receivedRatingIds = new LinkedList<>();
    }

    //methode to return the UserID of a user
    public String getUserID(){
        return userID;
    }

    //methode to return the name of a user saved under a certain user ID
    public String getName(){
        return name;
    }

    //methode to return the age of a user saved under a certain user ID
    @SuppressLint ("NewApi")
    public int getAge(){
        return Period.between(LocalDate.of(this.year, this.month, this.day), LocalDate.now()).getYears();
    }

    /*methode to return the home Country of a user saved under a certain user ID.
    needs to return a certain String when a user hasn't entered their home country.*/
    public String getCountry(){
        return country;
    }

    //methode to return the language of a user saved under a certain user ID
    public String getLanguage(){
        return language;
    }

    //methode to return the description of a user saved under a certain user ID
    public String getDescription(){
        return description;
    }

    //methode to return a Bitmap of the profile picture of a user saved under a certain user ID
    public byte[] getProfilePicture(){
        return profilePicture;
    }

    //methode to change your user name
    public void setName(String name){
        this.name = name;
    }

    //methode to change, add or remove your home country
    public void setCountry(String country){
        this.country = country;
    }

    //methode to add a language to your list of spoken languages
    public boolean addLanguage(String language){
        if(!this.language.contains(language)){
            this.language = this.language.concat("," + language);
            return true;
        }
        return false;
    }

    //methode to remove a language from your list of spoken languages
    public boolean removeLanguage(String language){
        if(this.language.contains(language)){
            this.language = this.language.replace(language, "");
            return true;
        }
        return false;
    }

    //methode to change your description
    public void setDescription(String description){
        this.description = description;
    }

    //methode to change your profile picture
    public void setProfilePicture(File picture){
        try (FileInputStream fis = new FileInputStream(picture)){
            fis.read(this.profilePicture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getUserRating(){
        if(this.userRating == 0) {
            return 0;
        }
        return this.userRating/this.ratingsReceived;
    }

    public void addUserRating(UserRating rating) {
        if(this.receivedRatingIds.contains(rating.getRatingID())) {
            return;
        }
        this.userRating = this.userRating + rating.getRating();
        this.ratingsReceived = this.ratingsReceived + 1;
        if(this.ratingsReceived >= MAX_RATINGS) {
            this.userRating = getUserRating();
            this.ratingsReceived = 0;
        }
        this.receivedRatingIds.add(rating.getRatingID());
    }

}
