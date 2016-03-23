package com.cardbookvr.gallery360;

import com.cardbookvr.gallery360.RenderBoxExt.components.Plane;
import com.google.vrtoolkit.cardboard.CardboardView;

/**
 * Created by Schoen and Jonathan on 3/20/2016.
 */
public class Thumbnail {
    final static String TAG = "Thumbnail";

    public Plane plane;
    public Image image;
    CardboardView cardboardView;

    public Thumbnail(CardboardView cardboardView) {
        this.cardboardView = cardboardView;
    }

    public void setImage(Image image) {
        this.image = image;
        // Turn the image into a GPU texture
        image.loadTexture(cardboardView, 4);
        // TODO: wait until texture binding is done
        // show it
        image.showThumbnail(cardboardView, plane);
    }
}
