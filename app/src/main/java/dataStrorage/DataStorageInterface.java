package dataStrorage;

import android.content.Context;

import java.time.LocalDateTime;

import dataTypes.User;
import dataTypes.UserRating;

public interface DataStorageInterface {

    /*stores a User at the appropriate place and adds a timestamp of the time at which the storage process happened.
    Returns true if successful, else false*/
    public boolean toStore(User user);

    /*stores a UserRating at the appropriate place.
    Returns true if successful, else false*/
    public boolean toStore(UserRating rating);

    // returns a User from storage
    public User getUser(String userID);

    //returns a UserRating from storage
    public UserRating getUserRating(String ratingID);
}
