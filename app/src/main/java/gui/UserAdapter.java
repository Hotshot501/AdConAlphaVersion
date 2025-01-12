package gui;

import android.annotation.SuppressLint;
import android.app.RemoteAction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adcon.MainScreen;
import com.example.adcon.R;
import com.example.adcon.UserScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import dataTypes.User;

//code for RecyclerView from https://www.geeksforgeeks.org/how-to-implement-recylerview-in-a-fragment-in-android/
public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    private Context context;
    private List<User> users;

    private static Fragment screen;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(context).inflate(R.layout.user_view, parent, false));
    }

    @Override
    @SuppressLint("NewApi")
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.userTextView.setText(users.get(position).getName());
        holder.ageTextView.setText(String.valueOf(users.get(position).getAge()));
        File pictureStorage = new File(users.get(position).getUserID() + "ProfilePicture.jpg");
        try (FileOutputStream fos = new FileOutputStream(pictureStorage)) {
            fos.write(users.get(position).getProfilePicture());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap profilePicture = BitmapFactory.decodeFile(pictureStorage.toPath().toString());
        holder.userImageView.setImageBitmap(profilePicture);
        holder.showUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserScreen.setUserID(users.get(holder.getAdapterPosition()).getUserID());
                NavHostFragment.findNavController(screen).navigate((R.id.action_mainScreen_to_userScreen));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static void setScreen(Fragment mainScreen) {
        screen = mainScreen;
    }
}
