package bluetooth;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.ASAPPeerFS;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BluetoothImplementation implements NetworkDataInterface{

    @SuppressLint("NewApi")
    @Override
    public int getNumberOfConnectedProfiles() {
        try {
            return Files.list(Path.of("users")).toArray().length;
        } catch (IOException e) {
            return -1;
        }

    }
}
