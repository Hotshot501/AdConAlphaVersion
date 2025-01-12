package appLogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.adcon.MainActivity;
import com.example.adcon.MainScreen;

import net.sharksystem.asap.ASAPChannel;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.ASAPPeerFS;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;
import net.sharksystem.asap.utils.ASAPSerialization;
import net.sharksystem.utils.SerializationHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataStrorage.DataStorageImplementation;
import dataTypes.User;
import dataTypes.UserRating;

public class BluetoothDataImplementation implements BluetoothDataInterface{

    private final int MAX_USERS = 33;

    private final int MAX_RATINGS = 100;

    private static Context context = null;

    private static BluetoothDataImplementation bluetoothDataImplementation;
    public static BluetoothDataImplementation getBleutoothDataImplementation() {
        if(bluetoothDataImplementation == null) {
            bluetoothDataImplementation = new BluetoothDataImplementation();
        }
        return bluetoothDataImplementation;
    }
    @Override
    public boolean getUserToSend() {
        ASAPAndroidPeer peer = ASAPAndroidPeer.getASAPAndroidPeer();
        User main = DataStorageImplementation.getDataStorage().getUser("main");
        byte[] serializedMain = this.serializeUser(main);
        try {
            peer.sendASAPMessage("application/x-AdCon", "userTransfer", serializedMain);
        } catch (ASAPException e) {
            System.err.println("cannot send message: " + e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public boolean getRatingsToSend(UserRating rating) {
        ASAPAndroidPeer peer = ASAPAndroidPeer.getASAPAndroidPeer();
        try {
            peer.sendASAPMessage("application/x-AdCon", "ratingTransfer", this.serializeRating(rating));
        } catch (ASAPException e) {
            System.err.println("cannot send message: " + e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean registerUsers() throws IOException {
        ASAPStorage asapStorage = null;
        try {
            asapStorage = ASAPAndroidPeer.getASAPAndroidPeer().getASAPStorage("application/x-AdCon");
        } catch (IOException | ASAPException e) {
            return false;
        }
        ASAPChannel channel = null;
        ASAPMessages messages = null;
        try {
            channel = asapStorage.getChannel("userTransfer");
            messages = channel.getMessages();
        } catch (ASAPException | IOException e) {
            return false;
        }
        for(int i = 0; i < messages.size(); i++) {
            byte[] message = null;
            try {
                message = messages.getMessage(i, true);
            } catch (ASAPException e) {
                return false;
            }
            if(message == null) {
                return false;
            }
            User user = null;
            try {
                user = desirializeUser(message);
            } catch (IOException e) {
                continue;
            }
            if(!user.getUserID().equals(DataStorageImplementation.getDataStorage().getUser("main").getUserID())) {
                DataStorageImplementation.getDataStorage().toStore(user);
                MainScreen.addUsersToList(user);
                File[] allUsers = new File(context.getFilesDir(), "users").listFiles();
                while(allUsers.length > MAX_USERS) {
                    File oldestFile = allUsers[0];
                    for(int j = 0; j < allUsers.length; j++) {
                        if(allUsers[j].lastModified() < oldestFile.lastModified() && !allUsers[j].getName().equals("main")){
                            oldestFile = allUsers[j];
                        }
                    }
                    MainScreen.removeUsersFromList(DataStorageImplementation.getDataStorage().getUser(oldestFile.getName()));
                    Files.delete(oldestFile.toPath());
                }
                resetPeer();
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean registerRatings() throws IOException{
        ASAPStorage asapStorage = null;
        try {
            asapStorage = ASAPAndroidPeer.getASAPAndroidPeer().getASAPStorage("application/x-AdCon");
        } catch (IOException | ASAPException e) {
            return false;
        }
        ASAPChannel channel = null;
        ASAPMessages messages = null;
        try {
            channel = asapStorage.getChannel("ratingTransfer");
            messages = channel.getMessages();
        } catch (ASAPException | IOException e) {
            return false;
        }
        for(int i = 0; i < messages.size(); i++) {
            byte[] message = null;
            try {
                message = messages.getMessage(i, true);
            } catch (ASAPException e) {
                return false;
            }
            if(message == null) {
                return false;
            }
            UserRating rating = null;
            try {
                rating = desirializeRating(message);
            } catch (IOException e) {
                continue;
            }
            if(rating.getUserID().equals(DataStorageImplementation.getDataStorage().getUser("main").getUserID())) {
                DataStorageImplementation.getDataStorage().getUser("main").addUserRating(rating);
                return true;
            }
            DataStorageImplementation.getDataStorage().toStore(rating);
            File[] allRatings = new File(context.getFilesDir(), "ratings").listFiles();
            while(allRatings.length > MAX_RATINGS) {
                File oldestFile = allRatings[0];
                for(int j = 0; j < allRatings.length; j++) {
                    if(allRatings[j].lastModified() < oldestFile.lastModified() && !allRatings[j].getName().equals("main")){
                        oldestFile = allRatings[j];
                    }
                }
                MainScreen.removeUsersFromList(DataStorageImplementation.getDataStorage().getUser(oldestFile.getName()));
                Files.delete(oldestFile.toPath());
            }
            resetPeer();
        }
        return true;
    }

    private User desirializeUser (byte[] message)
        throws IOException {
        if(message == null) {
            throw new NullPointerException("message is null");
        }
        User user = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(message);
             ObjectInputStream ois = new ObjectInputStream(bais)){
            user = (User)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("cannot deserialize user");
        }
        return user;
    }

    private UserRating desirializeRating (byte[] message)
        throws IOException {
        if(message == null) {
            throw new NullPointerException("message is null");
        }
        UserRating rating = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(message);
             ObjectInputStream ois = new ObjectInputStream(bais)){
            rating = (UserRating) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("cannot deserialize user");
        }
        return rating;
    }

    private byte[] serializeUser (User user) {
        if(user == null) {
            throw new NullPointerException("user is null");
        }
        byte[] message = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
              ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(user);
            message = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private byte[] serializeRating (UserRating rating) {
        if(rating == null) {
            throw new NullPointerException("rating is null");
        }
        byte[] message = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(rating);
            message = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static void setContext(Context mainActivityContext) {
        context = mainActivityContext;
    }

    public static void resetPeer() {
        ASAPAndroidPeer peer = ASAPAndroidPeer.getASAPAndroidPeer();
        try {
            peer.getASAPStorage("application/x-AdCon").removeChannel("userTransfer");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASAPException e) {
            e.printStackTrace();
        }

        try {
            peer.getASAPStorage("application/x-AdCon").createChannel("userTransfer");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASAPException e) {
            e.printStackTrace();
        }

        ExecutorService sendUser = Executors.newSingleThreadExecutor();
        Runnable taskUser = () -> BluetoothDataImplementation.getBleutoothDataImplementation().getUserToSend();
        sendUser.submit(taskUser);
        sendUser.shutdown();

        try {
            peer.getASAPStorage("application/x-AdCon").removeChannel("ratingTransfer");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASAPException e) {
            e.printStackTrace();
        }

        try {
            peer.getASAPStorage("application/x-AdCon").createChannel("ratingTransfer");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASAPException e) {
            e.printStackTrace();
        }

        ExecutorService sendRatings = Executors.newSingleThreadExecutor();
        Runnable taskRating = () -> {
            File[] allRatings = new File(context.getFilesDir(), "ratings").listFiles();
            for(File rating : allRatings) {
                UserRating tmp = DataStorageImplementation.getDataStorage().getUserRating(rating.getName());
                BluetoothDataImplementation.getBleutoothDataImplementation().getRatingsToSend(tmp);
            }};
        sendRatings.submit(taskRating);
        sendRatings.shutdown();
    }
}
