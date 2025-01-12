package com.example.adcon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import appLogic.BluetoothDataImplementation;
import dataStrorage.DataStorageImplementation;
import dataTypes.User;
import gui.UserAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private static List<User> users = new LinkedList<User>();


    public MainScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment main_screen.
     */
    // TODO: Rename and change types and number of parameters
    public static MainScreen newInstance() {
        MainScreen fragment = new MainScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.user_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new UserAdapter(getContext(), users));
        DataStorageImplementation.setContext(getContext());
        view.findViewById(R.id.yourProfileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserScreen.setUserID("main");
                NavHostFragment.findNavController(MainScreen.this).navigate(R.id.action_mainScreen_to_userScreen);
            }
        });
        BluetoothDataImplementation.setContext(getContext());
        view.findViewById(R.id.refreshButtonMainScreen).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                try {
                    BluetoothDataImplementation.getBleutoothDataImplementation().registerUsers();
                    BluetoothDataImplementation.getBleutoothDataImplementation().registerRatings();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        UserAdapter.setScreen(this);
        return view;
    }

    public static void addUsersToList(User user){
        users.add(user);
    }

    public static void removeUsersFromList(User user) {
        users.remove(user);
    }


}