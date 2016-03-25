package com.cardbookvr.gallery360;

import com.cardbookvr.gallery360.RenderBoxExt.components.Plane;
import com.cardbookvr.renderbox.components.Sphere;
import com.cardbookvr.renderbox.materials.UnlitTexMaterial;
import com.google.vrtoolkit.cardboard.CardboardView;

/**
 * Created by Schoen and Jonathan on 3/20/2016.
 */
public class Thumbnail {
    final static String TAG = "Thumbnail";

    public Plane plane;
    public Sphere sphere;
    public Image image;
    CardboardView cardboardView;

    public Thumbnail(CardboardView cardboardView) {
        this.cardboardView = cardboardView;
    }

    public void setImage(Image image) {
        this.image = image;
        // Turn the image into a GPU texture
        image.loadTexture(cardboardView, 4);
        // wait until texture binding is done
        try {
            while (Image.loadLock) {
                if (MainActivity.cancelUpdate)
                    return;
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // show it
        if (image.isPhotosphere) {
            UnlitTexMaterial material = (UnlitTexMaterial) sphere.getMaterial();
            material.setTexture(image.textureHandle);
        } else {
            image.showThumbnail(cardboardView, plane);
        }
    }

    public void setVisible(boolean visible) {
        if(visible) {
            if(image.isPhotosphere){
                plane.enabled = false;
                sphere.enabled = true;
            } else{
                plane.enabled = true;
                sphere.enabled = false;
            }
        } else {
            plane.enabled = false;
            sphere.enabled = false;
        }
    }
}
