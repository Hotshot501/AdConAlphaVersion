package com.example.adcon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.sharksystem.asap.ASAP;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class StartupActivity extends AppCompatActivity {

    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
        if(Build.VERSION.SDK_INT >= 34 && getApplicationInfo().targetSdkVersion >= 34) {
            return super.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED);
        } else {
            return super.registerReceiver(receiver, filter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(!ASAPAndroidPeer.peerInitialized()) {
                Collection<CharSequence> formats = new ArrayList<>();

                formats.add("application/x-AdCon");
                String peerName = ASAP.createUniqueID();
                ASAPAndroidPeer.initializePeer(peerName, formats, this);
            }

            if(!ASAPAndroidPeer.peerStarted()) {
                ASAPAndroidPeer.startPeer(this);
            }
        } catch (IOException | ASAPException e) {
            e.printStackTrace();
            Toast.makeText(this, "fatal: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        this.finish();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

}