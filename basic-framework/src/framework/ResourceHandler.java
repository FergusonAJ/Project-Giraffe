/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

/**
 *
 * @author Andrew Polanco
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;


import java.io.BufferedInputStream;
//import org.newdawn.slick.openal.Audio;
//import org.newdawn.slick.openal.AudioLoader;
//import org.newdawn.slick.util.ResourceLoader;

public class ResourceHandler {

/**
 * Returns a buffered reader of the input file from a (non/)streaming resource
 * 
 * @param filePath
 * @param streaming
 * @return
 */
public static BufferedReader loadIntoBufferedReader(String filePath, boolean streaming) {
    BufferedReader reader = null;
    if (streaming) {
        InputStream stream = ResourceLoader.getResourceAsStream(filePath);
        InputStreamReader isr = new InputStreamReader(stream);
        reader = new BufferedReader(isr);
    } else {
        URL url = ResourceLoader.getResource(filePath);

        try (FileReader freader = new FileReader(url.toURI().toString())) {
            reader = new BufferedReader(freader);
        } catch (FileNotFoundException fnfe) {
            System.err.println("File " + filePath + " does not exist!");
            fnfe.printStackTrace();
            System.exit(1);
        } catch (URISyntaxException urise) {
            System.err.println("Bad URI syntax for " + filePath + "!");
            urise.printStackTrace();
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("File " + filePath + " couldn't be read/written to!");
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    return reader;
}

/**
 * Returns an InputStream of the input file
 * 
 * @param filePath
 * @return
 */
public static InputStream loadIntoInputStream(String filePath) {
    InputStream stream = ResourceLoader.getResourceAsStream(filePath);
    return stream;
}

/**
 * Returns an Audio object of the input file as a stream
 * 
 * @param type
 * @param filePath
 * @return
 */
public static Audio loadIntoAudio(String type, String filePath, boolean streaming) {
    Audio audio = null;
    try {
        if (streaming) {
            audio = AudioLoader.getStreamingAudio(type, ResourceLoader.getResource(filePath));
        } else {
            audio = AudioLoader.getAudio(type, ResourceLoader.getResourceAsStream(filePath));
        }
    }
    catch (final IOException ioe) {
        System.err.println("File " + filePath + " couldn't be read/written to!");
        ioe.printStackTrace();
        System.exit(1);
    }
    return audio;
}
