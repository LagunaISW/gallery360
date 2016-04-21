package com.cardbookvr.gallery360;

import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;

import com.cardbookvr.gallery360.RenderBoxExt.components.Plane;
import com.cardbookvr.gallery360.RenderBoxExt.materials.BorderMaterial;
import com.cardbookvr.renderbox.IRenderBox;
import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.renderbox.Transform;
import com.cardbookvr.renderbox.components.Camera;
import com.cardbookvr.renderbox.components.Sphere;
import com.cardbookvr.renderbox.materials.UnlitTexMaterial;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CardboardActivity implements IRenderBox {
    private static final String TAG = "Gallery360";

    CardboardView cardboardView;

    final int DEFAULT_BACKGROUND = R.drawable.bg;

    Sphere photosphere;
    Plane screen;
    int bgTextureHandle;

    final List<Image> images = new ArrayList<>();
    final String imagesPath = "/storage/emulated/0/DCIM/Camera";

    final int GRID_X = 5;
    final int GRID_Y = 3;

    final List<Thumbnail> thumbnails = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(new RenderBox(this, this));
        setCardboardView(cardboardView);
    }

    @Override
    public void setup() {
        setupMaxTextureSize();
        setupBackground();
        setupScreen();
        loadImageList(imagesPath);
        setupThumbnailGrid();
        updateThumbnails();
    }

    void setupBackground() {
        photosphere = new Sphere(DEFAULT_BACKGROUND, false);
        new Transform()
                .setLocalScale(Camera.Z_FAR * 0.99f, -Camera.Z_FAR * 0.99f, Camera.Z_FAR * 0.99f)
                .addComponent(photosphere);
        UnlitTexMaterial mat = (UnlitTexMaterial) photosphere.getMaterial();
        bgTextureHandle = mat.getTexture();
    }

    void setupScreen() {
        Transform screenRoot = new Transform()
                .setLocalScale(4, 4, 1)
                .setLocalRotation(0, -90, 0)
                .setLocalPosition(-5, 0, 0);

        screen = new Plane();
        BorderMaterial screenMaterial = new BorderMaterial();
        screen.setupBorderMaterial(screenMaterial);

        new Transform()
                .setParent(screenRoot, false)
                .addComponent(screen);
    }

    void setupThumbnailGrid() {
        int count = 0;
        for (int i = 0; i < GRID_Y; i++) {
            for (int j = 0; j < GRID_X; j++) {
                if (count < images.size()) {
                    Thumbnail thumb = new Thumbnail(cardboardView);
                    thumbnails.add(thumb);

                    Transform image = new Transform();
                    image.setLocalPosition(-4 + j * 2.1f, 3 - i * 3, -5);
                    Plane imgPlane = new Plane();
                    thumb.plane = imgPlane;
                    BorderMaterial material = new BorderMaterial();
                    imgPlane.setupBorderMaterial(material);
                    image.addComponent(imgPlane);
                }
                count++;
            }
        }
    }

    void updateThumbnails() {
        int count = 0;
        for (Thumbnail thumb : thumbnails) {
            if (count < images.size()) {
                thumb.setImage(images.get(count));
                thumb.setVisible(true);
            } else {
                thumb.setVisible(false);
            }
            count++;
        }
    }


    @Override
    public void preDraw() {

    }

    @Override
    public void postDraw() {

    }

    int loadImageList(String path) {
        File f = new File(path);
        File[] file = f.listFiles();
        if (file == null)
            return 0;
        for (int i = 0; i < file.length; i++) {
            if (Image.isValidImage(file[i].getName())) {
                Log.d(TAG, file[i].getName());
                Image img = new Image(path + "/" + file[i].getName());
                images.add(img);
            }
        }
        return file.length;
    }

    void showImage(Image image) {
        UnlitTexMaterial bgMaterial = (UnlitTexMaterial) photosphere.getMaterial();
        image.loadFullTexture(cardboardView);
        if (image.isPhotosphere) {
            bgMaterial.setTexture(image.textureHandle);
            screen.enabled = false;
        } else {
            bgMaterial.setTexture(bgTextureHandle);
            screen.enabled = true;
            image.show(cardboardView, screen);
        }
    }


    static int MAX_TEXTURE_SIZE = 2048;

    void setupMaxTextureSize() {
        //get max texture size
        int[] maxTextureSize = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        MAX_TEXTURE_SIZE = maxTextureSize[0];
        Log.i(TAG, "Max texture size = " + MAX_TEXTURE_SIZE);
    }

}
