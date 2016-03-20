package com.cardbookvr.gallery360;

/**
 * Created by Schoen and Jonathan on 3/20/2016.
 */
public class Image {
    final static String TAG = "image";
    String path;

    public Image(String path) {
        this.path = path;
    }

    public static boolean isValidImage(String path){
        String extension = getExtension(path);
        if(extension == null)
            return false;
        switch (extension){
            case "jpg":
                return true;
            case "jpeg":
                return true;
            case "png":
                return true;
        }
        return false;
    }

    static String getExtension(String path){
        String[] split = path.split("\\.");
        if(split== null || split.length < 2)
            return null;
        return split[split.length - 1].toLowerCase();
    }
}
