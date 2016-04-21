package com.cardbookvr.gallery360;

import android.os.Bundle;

import com.cardbookvr.gallery360.RenderBoxExt.components.Plane;
import com.cardbookvr.renderbox.IRenderBox;
import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.renderbox.Transform;
import com.cardbookvr.renderbox.components.Camera;
import com.cardbookvr.renderbox.components.Sphere;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

public class MainActivity extends CardboardActivity implements IRenderBox {
    private static final String TAG = "Gallery360";

    CardboardView cardboardView;

    final int DEFAULT_BACKGROUND = R.drawable.bg;

    Sphere photosphere;
    Plane screen;

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
        setupBackground();
        setupScreen();
    }

    void setupBackground() {
        photosphere = new Sphere(DEFAULT_BACKGROUND, false);
        new Transform()
                .setLocalScale(Camera.Z_FAR * 0.99f, -Camera.Z_FAR *
                        0.99f, Camera.Z_FAR * 0.99f)
                .addComponent(photosphere);
    }

    void setupScreen() {
        Transform screenRoot = new Transform()
                .setLocalScale(-4, 4, 1)
                .setLocalRotation(0, 0, 180)
                .setLocalPosition(0, 0, -5);

        screen = new Plane(R.drawable.sample360, false);

        new Transform()
                .setParent(screenRoot, false)
                .addComponent(screen);
    }


    @Override
    public void preDraw() {

    }

    @Override
    public void postDraw() {

    }
}
