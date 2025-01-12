package appLogic;

import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;

import java.io.IOException;
import java.util.HashMap;

import dataTypes.User;
import dataTypes.UserRating;

public interface BluetoothDataInterface {

    //returns the Main-User as a User object (class User to be defined at a later point in time)
    public boolean getUserToSend();

    //returns a map of UserRating objects with ratingID as key values (class UserRating to be defined at a later point in time)
    public boolean getRatingsToSend(UserRating rating);

    //methode which hands a user which got received over the network to the App-Logic
    public boolean registerUsers() throws IOException;

    //methode which hands a map of user rating which got received over the network to the App-Logic
    public boolean registerRatings() throws IOException;
}
