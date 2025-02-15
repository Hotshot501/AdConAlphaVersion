package appLogic;

import android.graphics.Bitmap;

import java.io.File;
import java.util.HashSet;

import dataTypes.UserRating;

public interface DataDisplayInterface {

    //methode to return the name of a user saved under a certain user ID
    public String getName(String userID);

    //methode to return the age of a user saved under a certain user ID
    public int getAge(String userID);

    /*methode to return the home Country of a user saved under a certain user ID.
    needs to return a certain String when a user hasn't entered their home country.*/
    public String getCountry(String userID);

    //methode to return the language of a user saved under a certain user ID
    public String getLanguage(String userID);

    //methode to return the description of a user saved under a certain user ID
    public String getDescription(String userID);

    //methode to return a Bitmap of the profile picture of a user saved under a certain user ID
    public byte[] getProfilePicture(String userID);

    //methode to change your user name
    public void setName(String name);

    //methode to change, add or remove your home country
    public void setCountry(String country);

    //methode to add a language to your list of spoken languages
    public boolean addLanguage(String language);

    //methode to remove a language from your list of spoken languages
    public boolean removeLanguage(String language);

    //methode to change your description
    public void setDescription(String description);

    //methode to change your profile picture
    public void setProfilePicture(File picture);

    //methode to call when rating a user
    public UserRating createRating(String userID, int rating);
}
