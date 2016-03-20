package com.cardbookvr.gallery360;

import android.os.Bundle;
import android.util.Log;

import com.cardbookvr.gallery360.RenderBoxExt.components.Plane;
import com.cardbookvr.gallery360.RenderBoxExt.materials.BorderMaterial;
import com.cardbookvr.renderbox.IRenderBox;
import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.renderbox.Transform;
import com.cardbookvr.renderbox.components.Camera;
import com.cardbookvr.renderbox.components.RenderObject;
import com.cardbookvr.renderbox.components.Sphere;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CardboardActivity implements IRenderBox {
    private static final String TAG = "Gallery360";

    final int DEFAULT_BACKGROUND = R.drawable.bg;

    CardboardView cardboardView;
    Sphere photosphere;
    Plane screen;
    final List<Image> images = new ArrayList<>();
    final String imagesPath = "/storage/emulated/0/DCIM/Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        cardboardView.setRenderer(new RenderBox(this, this));
        setCardboardView(cardboardView);
    }

    @Override
    public void setup() {
        setupBackground();
        setupScreen();
        loadImageList(imagesPath);
        showImage(images.get(0));
    }

    @Override
    public void preDraw() {

    }

    @Override
    public void postDraw() {

    }

    void setupBackground() {
        photosphere = new Sphere(DEFAULT_BACKGROUND, false);
        new Transform()
                .setLocalScale(-Camera.Z_FAR, -Camera.Z_FAR, -Camera.Z_FAR)
                .addComponent(photosphere);
    }

    void setupScreen() {
        Transform screenRoot = new Transform()
                .setLocalScale(4, 4, 1)
                .setLocalPosition(0, 0, 5);

        screen = new Plane(R.drawable.sample360, false);

        new Transform()
                .setParent(screenRoot, false)
                .addComponent(screen);

        BorderMaterial screenMaterial = new BorderMaterial();
        screenMaterial.setTexture(RenderObject.loadTexture(R.drawable.sample360));
        screen.setupBorderMaterial(screenMaterial);
    }

    int loadImageList(String path) {
        File f = new File(path);
        File[] file = f.listFiles();
        if (file==null)
            return 0;
        for (int i = 0; i < file.length; i++) {
            String name = file[i].getName();
            if (Image.isValidImage(name)) {
                Log.d(TAG, "image name: " + name);
                Image img = new Image(path + "/" + name);
                images.add(img);
            }
        }
        return file.length;
    }

    void showImage(Image image) {
        image.show(cardboardView, screen);
    }

}
