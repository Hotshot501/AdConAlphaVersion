package dataStrorage;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DataStorageInterface {

    /*stores a User at the appropriate place and adds a timestamp of the time at which the storage process happened.
    Returns true if successful, else false*/
    public boolean toStore(User user, LocalDateTime timestamp);

    /*stores a UserRating at the appropriate place.
    Returns true if successful, else false*/
    public boolean toStore(UserRating rating);

    // returns a User from storage
    public User getUser(int userID);

    //returns a UserRating from storage
    public UserRating getUserRating(int ratingID);
}
