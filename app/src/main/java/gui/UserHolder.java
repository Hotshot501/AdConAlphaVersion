package gui;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adcon.R;

//code for RecyclerView from https://www.geeksforgeeks.org/how-to-implement-recylerview-in-a-fragment-in-android/
public class UserHolder extends RecyclerView.ViewHolder {

    ImageView userImageView;
    TextView userTextView;
    TextView ageTextView;
    Button showUserButton;
    public UserHolder(@NonNull View itemView) {
        super(itemView);
        userImageView = itemView.findViewById(R.id.UserImageView);
        userTextView = itemView.findViewById(R.id.UserTextView);
        ageTextView = itemView.findViewById(R.id.AgeTextView);
        showUserButton = itemView.findViewById(R.id.showUserButton);
    }
}
