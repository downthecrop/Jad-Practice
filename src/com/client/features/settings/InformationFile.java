package com.client.features.settings;

import com.client.utilities.FileOperations;
import com.client.Stream;
import com.client.StreamLoader;
import com.client.sign.Signlink;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class InformationFile {

    /**
     * The location of the file that contains, or will contain information
     * about the client.
     */
    private final Path FILE_LOCATION = Paths.get(Signlink.getCacheDirectory(), "settings.dat");

    /**
     * Determines if the user name is remembered or not.
     */
    private boolean usernameRemembered, passwordRemembered;

    /**
     * Remembers the roof option
     */
    private boolean rememberRoof = false;

    /**
     * Remember items on ground
     */
    private boolean rememberVisibleItemNames;

    /**
     * The user name to be stored, if {@link #storedUsername}'s state is true.
     */
    private String storedUsername = new String();
    private String storedPassword = new String();

    /**
     * Writes some information to a file about the client.
     *
     * @throws IOException thrown if the file cannot be deleted, cannot be created, or if
     *                     the backing buffer cannot be written to the file.
     */
    public void write() throws IOException {
        // Deletes the existing file if it exists
        Files.deleteIfExists(FILE_LOCATION);

        // Create a new file in the system directory if the file does not exist
        Files.createFile(FILE_LOCATION);

        // Create a new stream to store information in
        Stream stream = Stream.create();

        // Writes the opcode '0' and a string of characters that make up the players user name
        stream.writeByte(0);
        stream.writeByte(usernameRemembered ? 1 : 0);

        // Writes the opcode '1' and a string of characters that make up the players pass word
        stream.writeByte(1);
        stream.writeString(storedUsername);

        // Writes the opcode '2' and a string of characters that make up the roofs
        stream.writeByte(2);
        stream.writeByte(rememberRoof ? 1 : 0);

        // Writes the opcode '3' and a string of characters that make up the ground item names
        stream.writeByte(3);
        stream.writeByte(rememberVisibleItemNames ? 1 : 0);

        stream.writeByte(4);
        stream.writeByte(passwordRemembered ? 1 : 0);

        // Writes the opcode '1' and a string of characters that make up the players pass word
        stream.writeByte(5);
        stream.writeString(storedPassword);

        // Writes all bytes to the file from a new byte array which has been resized
        FileOperations.writeFile(FILE_LOCATION.toString(), Arrays.copyOf(stream.buffer, stream.currentOffset));
    }

    /**
     * Reads some information from the file, if the file exists.
     *
     * @throws IOException           refer to the function {@link StreamLoader#getBytesFromFile(File)}
     * @throws IllegalStateException thrown if an opcode read cannot be found
     */
    public void read() throws IOException, IllegalStateException {
        // Determines the location of the file and generates one based off of the path
        File file = FILE_LOCATION.toFile();

        // Determines if the file exists, and discontinues if it does not
        if (!file.exists()) {
            //throw some no file found exception if necessary
            return;
        }

        // Creates a new byte array with the information from the file
        byte[] buffer = StreamLoader.getBytesFromFile(file);

        // Creates a new stream using the byte buffer as the backing array
        Stream stream = new Stream(buffer);

        // Continues to read from the buffer until it can no longer
        while (stream.currentOffset < buffer.length) {

            // Reads the first byte that determines what data we're reading
            int opcode = stream.readSignedByte();

            // Here we read information from the underlays depending on the opcode
            switch (opcode) {
                case 0:
                    usernameRemembered = stream.readSignedByte() == 1 ? true : false;
                    break;

                case 1:
                    storedUsername = stream.readString();
                    break;

                case 2:
                    rememberRoof = stream.readSignedByte() == 1 ? true : false;
                    break;

                case 3:
                    rememberVisibleItemNames = stream.readSignedByte() == 1 ? true : false;
                    break;

                case 4:
                    passwordRemembered = stream.readSignedByte() == 1 ? true : false;
                    break;

                case 5:
                    storedPassword = stream.readString();
                    break;

                default:
                    throw new IllegalStateException("Opcode #" + opcode + " does not exist.");
            }
        }
    }

    /**
     * Modifies the current string that is being stored as the user name
     *
     * @param storedUsername the new stored user name
     */
    public void setStoredUsername(String storedUsername) {
        this.storedUsername = storedUsername;
    }

    /**
     * Retrieves the currently stored user name
     *
     * @return the stored user name
     */
    public String getStoredUsername() {
        return storedUsername;
    }

    /**
     * Modifies the current string that is being stored as the user name
     *
     * @param storedPassword the new stored user name
     */
    public void setStoredPassword(String storedPassword) {
        this.storedPassword = storedPassword;
    }

    /**
     * Retrieves the currently stored user name
     *
     * @return the stored user name
     */
    public String getStoredPassword() {
        return storedPassword;
    }

    /**
     * Determines if the stored user name should be remembered and stored
     *
     * @param usernameRemembered {@code true} if the user name should be remembered, otherwise false.
     */
    public void setUsernameRemembered(boolean usernameRemembered) {
        this.usernameRemembered = usernameRemembered;
    }

    public void setPasswordRemembered(boolean passwordRemembered) {
        this.passwordRemembered = passwordRemembered;
    }

    /**
     * Determines if the user name is being remembered in the file or not
     *
     * @return    {@code true} if the user name is to be remembered.
     */
    public boolean isUsernameRemembered() {
        return usernameRemembered;
    }

    public boolean isPasswordRemembered() {
        return passwordRemembered;
    }

    public boolean isRememberRoof() {
        return rememberRoof;
    }

    public void setRememberRoof(boolean rememberRoof) {
        this.rememberRoof = rememberRoof;
    }

    /**
     * @return the rememberVisibleItemNames
     */
    public boolean isRememberVisibleItemNames() {
        return rememberVisibleItemNames;
    }

    /**
     * @param rememberVisibleItemNames the rememberVisibleItemNames to set
     */
    public void setRememberVisibleItemNames(boolean rememberVisibleItemNames) {
        this.rememberVisibleItemNames = rememberVisibleItemNames;
    }
}
