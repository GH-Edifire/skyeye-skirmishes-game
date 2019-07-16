package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ReloadBar extends ProgressBar {

    public ReloadBar(int width, int height) {
        super(0f, 1f, 0.01f, false, new ProgressBarStyle());
        this.setName("RELOAD");
        getStyle().background = getColoredDrawable(width, height, Color.GRAY);
        getStyle().knob = getColoredDrawable(0, height, Color.BLUE);
        getStyle().knobBefore = getColoredDrawable(width, height, Color.BLUE);

        setWidth(width);
        setHeight(height);

        setAnimateDuration(0.0f);
        setValue(1f);

        setAnimateDuration(0.25f);
    }

    private static Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }
/*
    @Override
    protected void positionChanged() {
        // position of sprite
        setPosition(getX(),getY());
        super.positionChanged();
    }

    @Override
    public void draw(Batch batch, float alpha){
        setPosition(getX(),getY());
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }
    */
}