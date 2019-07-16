package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends Actor {
    private final double ATTACK_COOLDOWN = 1.0;
    private double attackTimer = 0;
    private boolean ableToAttack = true;
    private int owner = 0; // player = 0, enemy = 1
    private float speed = 800;
    private boolean selected = false;
    private boolean isAttacking = false;
    private boolean isFollowing = false;
    private Sprite sprite;
    private final float maxHealth = 500;
    private int health = 500;
    private int damage = 50;
    private Rectangle bounds;

    public Bullet(Sprite newSprite, int owner) {
        this.setName("B");
        sprite = newSprite;
        this.owner = owner;
        setBounds(getX(), getY(), sprite.getWidth(), sprite.getHeight());
        bounds = new Rectangle(getX(), getY(), sprite.getWidth(), sprite.getHeight());
        //setOrigin(0f, 0f);
        setTouchable(Touchable.enabled);
    }

    @Override
    protected void positionChanged() {
        // position of sprite
        setPosition(getX(), getY());
        // position of hitbox
        setXY(getX(), getY());
        super.positionChanged();
    }

    @Override
    public void draw(Batch batch, float alpha) {
        setPosition(getX(), getY());
        //batch.setColor(Color.BLACK);
        batch.setColor(this.getColor());
        batch.draw(sprite, getX(), getY());
        super.draw(batch, alpha);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void setXY(float pX, float pY) {
        setPosition(pX, pY);
        bounds.setX((int) pX);
        bounds.setY((int) pY);
    }

    @Override
    public void act(float delta) {
        if(!this.hasActions()){
            this.setVisible(false);
            setPosition(-500,-500);
        }
        super.act(delta);
    }

    public int getOwner(){return this.owner;}

    public int getDamage(){return this.damage;}

    public void setDamage(int newDamage){damage = newDamage;}

    public void colorUnit(Color color) {
        ColorAction colorAction = new ColorAction();
        colorAction.setEndColor(color);
        colorAction.setDuration(0.001f);
        Bullet.this.addAction(colorAction);
    }

    // Move the unit to indicated position, adjusting x and y from input to image coordinates (lower left corner)
    public void moveUnit(int x, int y, Actor enemy) {
        if (!isFollowing) {
            timer.cancel();
            timer = new Timer();
        }
        this.clearActions();
        System.out.println("x: " + x + " y: " + y);
        System.out.println("getx: " + getX() + " gety: " + getY());
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(x - getWidth() / 2, y - getHeight() / 2);
        float distance = (float) Math.sqrt(Math.pow((x - getWidth() / 2) - getX()-getWidth()/2, 2) + Math.pow((y - getHeight() / 2) - getY()-getHeight()/2, 2));
        moveToAction.setDuration(distance / speed);
        System.out.println(distance);
        System.out.println(distance / speed);
        Bullet.this.addAction(moveToAction);
    }
    private Timer timer = new Timer();
}
