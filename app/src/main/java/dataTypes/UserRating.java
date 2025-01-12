package dataTypes;

import java.io.Serializable;

public class UserRating implements Serializable {

    //id of the rating
    private final String ratingID;

    //id of the user which got rated
    private String userID;

    //id of the user who is creating the rating
    private String ratingUserID;

    //value of the rating
    private double rating;

    public UserRating(String userID, String ratingUserID, double rating) {
        this.ratingID = ID.getID();
        this.userID = userID;
        this.ratingUserID = ratingUserID;
        this.rating = rating;
    }

    public String getRatingID() {
        return ratingID;
    }

    public String getUserID(){
        return userID;
    }

    public double getRating(){
        return rating;
    }
}