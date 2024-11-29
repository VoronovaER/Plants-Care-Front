package com.me.test1.network;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadFile{

    public File download(InputStream body) {
        File file = new File(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), ""), "image.jpg");
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int read;

            Log.d("Image", "Attempting to write to: " + "image.jpg");
            while ((read = body.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                Log.v("Image", "Writing to buffer to output stream.");
            }
            Log.d("Image", "Flushing output stream.");
            output.flush();
            Log.d("Image", "Output flushed.");
        } catch (IOException e) {
            Log.e("Image", "IO Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (output != null) {
                    output.close();
                    Log.d("Image", "Output stream closed sucessfully.");
                }
                else{
                    Log.d("Image", "Output stream is null");
                }
            } catch (IOException e){
                Log.e("Image", "Couldn't close output stream: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        Log.d("Image", "Finished writing to file.");
        return file;
    }
}
