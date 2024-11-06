package appLogic;

import java.util.HashMap;

public interface BluetoothDataInterface {

    //returns the Main-User as a User object (class User to be defined at a later point in time)
    public User getUserToSend(int userID);

    //returns a map of UserRating objects with ratingID as key values (class UserRating to be defined at a later point in time)
    public HashMap<UserRating> getRatingsToSend();

    //methode which hands a user which got received over the network to the App-Logic
    public boolean registerUser(User user);

    //methode which hands a map of user rating which got received over the network to the App-Logic
    public boolean registerRating(HashMap<UserRating> ratings);
}
