package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Artillary extends Group {
    private final double ATTACK_COOLDOWN = 1.0;
    private final double RELOAD_COOLDOWN = 6.0;
    private double attackTimer = 0;
    private double reloadTimer = 0;
    private double reloadBarMax = 1.0;
    private double reloadBarCurrent = 1.0;
    private boolean ableToAttack = true;
    private boolean ableToFire = true;
    private boolean canMove = true;
    private int owner = 0; // player = 0, enemy = 1
    private float speed = 35; // 75
    private boolean selected = false;
    private boolean collision = false;
    private boolean isAttacking = false;
    private boolean isFollowing = false;
    private Sprite sprite;
    private final float maxHealth = 500;
    private int health = 500;
    private int damage = 25; // 25
    private Rectangle bounds;
    private HealthBar healthBar;
    private ReloadBar reloadBar;
    private Bullet bulletOne;
    private double distance;
    private float volume = 0.25f;
    private Sound rangedSoundOne;
    private Sound rangedSoundTwo;
    private Sound meleeSoundOne;
    private Sound meleeSoundTwo;
    private Sound meleeSoundThree;
    private Sound meleeSoundFour;
    private Sound movementSound;
    private int soundTracker;

    public Artillary(Sprite newSprite, int owner) {
        this.setName("A");
        sprite = newSprite;
        this.owner = owner;
        setBounds(getX(), getY(), sprite.getWidth(), sprite.getHeight());
        bounds = new Rectangle(getX(), getY(), sprite.getWidth(), sprite.getHeight());
//        setOrigin(0f, 0f);
        setTouchable(Touchable.enabled);
        healthBar = new HealthBar(129, 10);
        healthBar.setPosition(2, sprite.getHeight() - 12);
        this.addActor(healthBar);
        reloadBar = new ReloadBar(129, 10);
        reloadBar.setPosition(2, 0);
        this.addActor(reloadBar);
        rangedSoundOne = Gdx.audio.newSound(Gdx.files.internal("sound/battle_cannon_01.wav"));
        rangedSoundTwo = Gdx.audio.newSound(Gdx.files.internal("sound/battle_cannon_02.wav"));
        meleeSoundOne = Gdx.audio.newSound(Gdx.files.internal("sound/battle_sword_01.wav"));
        meleeSoundTwo = Gdx.audio.newSound(Gdx.files.internal("sound/battle_sword_02.wav"));
        meleeSoundThree = Gdx.audio.newSound(Gdx.files.internal("sound/battle_sword_03.wav"));
        meleeSoundFour = Gdx.audio.newSound(Gdx.files.internal("sound/battle_sword_04.wav"));
        movementSound = Gdx.audio.newSound(Gdx.files.internal("sound/give_army_move_order.wav"));
        soundTracker = 3; // default value

        // Bullets
//        bulletOne = new Bullet(bulletSprite,this.owner);
//        bulletOne.setPosition(0*sprite.getWidth()/5,sprite.getHeight()/2);
//        bulletOne.setVisible(false);
//        bulletTwo = new Bullet(bulletSprite,this.owner);
//        bulletTwo.setPosition(2*sprite.getWidth()/5,sprite.getHeight()/2);
//        bulletTwo.setVisible(false);
//        bulletThree = new Bullet(bulletSprite,this.owner);
//        bulletThree.setPosition(4*sprite.getWidth()/5,sprite.getHeight()/2);
//        bulletThree.setVisible(false);
//        this.addActor(bulletOne);
//        this.addActor(bulletTwo);
//        this.addActor(bulletThree);
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
        healthBar.setValue(this.getHealth() / maxHealth);
        reloadBar.setValue((float) (reloadBarCurrent / reloadBarMax));

        if (getHealth() <= 0) {
            this.clearActions();
            timer.cancel();
            timer = new Timer();
            this.setName("delete");
            this.setVisible(true);
            //this.addAction(Actions.removeActor());
            bounds.set(new Rectangle(0, 0, 0, 0));
            setPosition(500, -500);
            System.out.println("dead");
            //this.remove();
            //this.removeActor(this);
        }

        attackTimer += Gdx.graphics.getDeltaTime();
        if (!ableToAttack && (attackTimer >= ATTACK_COOLDOWN)) {
            ableToAttack = true;
            attackTimer = 0;
        }

        reloadTimer += Gdx.graphics.getDeltaTime();
        if (!ableToFire && (reloadTimer >= RELOAD_COOLDOWN)) {
            ableToFire = true;
            canMove = true;
            reloadTimer = 0;
            reloadBarCurrent = reloadBarMax;
        }
        super.act(delta);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public void setBullets(Bullet bulletOne){
        this.bulletOne = bulletOne;
    }

    public void colorUnit(Color color) {
        ColorAction colorAction = new ColorAction();
        colorAction.setEndColor(color);
        colorAction.setDuration(0.000f);
        Artillary.this.addAction(colorAction);
    }

    // Move the unit to indicated position, adjusting x and y from input to image coordinates (lower left corner)
    public void moveUnit(int x, int y) {
        if (!isFollowing) {
            timer.cancel();
            timer = new Timer();
        }

        if(owner == 0 && soundTracker == 3){
            movementSound.play(volume*0.5f);
            System.out.println("DEV PLAYING MOVEMENT SOUND");
        }
        if(soundTracker > 3){
            soundTracker = 0;
        }
        else{
            soundTracker++;
        }

        this.clearActions();
        setCollision(false);
        setAttacking(false);
        System.out.println("x: " + x + " y: " + y);
        System.out.println("getx: " + getX() + " gety: " + getY());
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(x - getWidth() / 2, y - getHeight() / 2);
        float distance = (float) Math.sqrt(Math.pow((x - getWidth() / 2) - getX(), 2) + Math.pow((y - getHeight() / 2) - getY(), 2));
        moveToAction.setDuration(distance / speed);
        System.out.println(distance);
        System.out.println(distance / speed);
        if(canMove){
            Artillary.this.addAction(moveToAction);
        }
    }

    private Timer timer = new Timer();
    // TODO need cases for all unit types
    public void attackUnit(Actor player, Actor enemy) {
        if (owner == 0 && !isFollowing) {
            timer.cancel();
            timer = new Timer();
        }
        this.clearActions();
        Random rand = new Random();
        int random = rand.nextInt(2);
        distance = Math.sqrt( Math.pow((enemy.getX()-enemy.getWidth()/2) - this.getX(), 2) + Math.pow((enemy.getY() - enemy.getHeight()/2) - this.getY(), 2));
        if (!collision && ableToFire && distance < 600){
            switch(random){
                case 0:
                {
                    rangedSoundOne.play(volume);
                    break;
                }
                case 1:
                {
                    rangedSoundTwo.play(volume);
                    break;
                }
                default:
                {
                    break;
                }
            }
            reloadBarCurrent = 0;
            ableToFire = false;
            canMove = false;
            setFollowing(false);
            setAttacking(true);
            bulletOne.setPosition(getX()+2*this.getWidth()/5,getY()+this.getHeight()/2);
            bulletOne.setVisible(true);
            bulletOne.moveUnit((int) (enemy.getX()+2*enemy.getWidth()/5),(int) (enemy.getY()+enemy.getHeight()/2), enemy);
        }
        else if (!collision && canMove) {
            setFollowing(true);
            moveUnit((int) enemy.getX() + (int) enemy.getWidth() / 2, (int) enemy.getY() + (int) enemy.getHeight());
        }
        else if(distance < 100){
            setFollowing(false);
            setAttacking(true);
            String name = enemy.getName();
            switch (name) {
                case "I": {
                    if(ableToAttack) {
                        System.out.println("current health " + getHealth());
                        if (((Infantry) enemy).isCollision()) {
                            ((Infantry) enemy).setHealth(((Infantry) enemy).getHealth() - getDamage());
                            this.ableToAttack = false;
                            switch(random){
                                case 0:
                                {
                                    meleeSoundOne.play(volume);
                                    break;
                                }
                                case 1:
                                {
                                    meleeSoundTwo.play(volume);
                                    break;
                                }
                                case 2:
                                {
                                    meleeSoundThree.play(volume);
                                    break;
                                }
                                case 3:
                                {
                                    meleeSoundFour.play(volume);
                                    break;
                                }
                                default:
                                {
                                    break;
                                }
                            }
                        }
//                        else if (!((Infantry) enemy).isCollision()) {
//                            System.out.println("follow 2");
//                            setFollowing(true);
//                            // TODO set up timer to follow?
//                            moveUnit((int) enemy.getX() + (int) enemy.getWidth() / 2, (int) enemy.getY() + (int) enemy.getHeight());
//                        }
                    }
                    break;
                }
                case "C": {
                    if(ableToAttack) {
                        System.out.println("current health " + getHealth());
                        if (((Cavalry) enemy).isCollision()) {
                            ((Cavalry) enemy).setHealth(((Cavalry) enemy).getHealth() - getDamage());
                            this.ableToAttack = false;
                            switch(random){
                                case 0:
                                {
                                    meleeSoundOne.play(volume);
                                    break;
                                }
                                case 1:
                                {
                                    meleeSoundTwo.play(volume);
                                    break;
                                }
                                case 2:
                                {
                                    meleeSoundThree.play(volume);
                                    break;
                                }
                                case 3:
                                {
                                    meleeSoundFour.play(volume);
                                    break;
                                }
                                default:
                                {
                                    break;
                                }
                            }
                        }
//                        else if (!((Cavalry) enemy).isCollision()) {
//                            System.out.println("follow 2");
//                            setFollowing(true);
//                            // TODO set up timer to follow?
//                            moveUnit((int) enemy.getX() + (int) enemy.getWidth() / 2, (int) enemy.getY() + (int) enemy.getHeight());
//                        }
                    }
                    break;
                }
                case "R": {
                    if(ableToAttack) {
                        System.out.println("current health " + getHealth());
                        if (((RangedInfantry) enemy).isCollision()) {
                            ((RangedInfantry) enemy).setHealth(((RangedInfantry) enemy).getHealth() - getDamage());
                            this.ableToAttack = false;
                            switch(random){
                                case 0:
                                {
                                    meleeSoundOne.play(volume);
                                    break;
                                }
                                case 1:
                                {
                                    meleeSoundTwo.play(volume);
                                    break;
                                }
                                case 2:
                                {
                                    meleeSoundThree.play(volume);
                                    break;
                                }
                                case 3:
                                {
                                    meleeSoundFour.play(volume);
                                    break;
                                }
                                default:
                                {
                                    break;
                                }
                            }
                        }
//                        else if (!((RangedInfantry) enemy).isCollision()) {
//                            System.out.println("follow 2");
//                            setFollowing(true);
//                            // TODO set up timer to follow?
//                            moveUnit((int) enemy.getX() + (int) enemy.getWidth() / 2, (int) enemy.getY() + (int) enemy.getHeight());
//                        }
                    }
                    break;
                }
                case "A": {
                    if(ableToAttack) {
                        System.out.println("current health " + getHealth());
                        if (((Artillary) enemy).isCollision()) {
                            ((Artillary) enemy).setHealth(((Artillary) enemy).getHealth() - getDamage());
                            this.ableToAttack = false;
                            switch(random){
                                case 0:
                                {
                                    meleeSoundOne.play(volume);
                                    break;
                                }
                                case 1:
                                {
                                    meleeSoundTwo.play(volume);
                                    break;
                                }
                                case 2:
                                {
                                    meleeSoundThree.play(volume);
                                    break;
                                }
                                case 3:
                                {
                                    meleeSoundFour.play(volume);
                                    break;
                                }
                                default:
                                {
                                    break;
                                }
                            }
                        }
//                        else if (!((RangedInfantry) enemy).isCollision()) {
//                            System.out.println("follow 2");
//                            setFollowing(true);
//                            // TODO set up timer to follow?
//                            moveUnit((int) enemy.getX() + (int) enemy.getWidth() / 2, (int) enemy.getY() + (int) enemy.getHeight());
//                        }
                    }
                    break;
                }
            }
            switch (enemy.getName()) {
                case "I": {
                    if (((Infantry) enemy).getHealth() <= 0) {
                        setCollision(false);
                        setAttacking(false);
                    }
                    break;
                }
                case "C": {
                    if (((Cavalry) enemy).getHealth() <= 0) {
                        setCollision(false);
                        setAttacking(false);
                    }
                    break;
                }
                case "R": {
                    if (((RangedInfantry) enemy).getHealth() <= 0) {
                        setCollision(false);
                        setAttacking(false);
                    }
                    break;
                }
                case "A": {
                    if (((Artillary) enemy).getHealth() <= 0) {
                        setCollision(false);
                        setAttacking(false);
                    }
                    break;
                }
                default:
                    break;
            }
        }
//        if(owner == 0){
//            TimerTask myTask = new TimerTask() {
//                @Override
//                public void run() {
//                    attackUnit(player,enemy);
//                }
//            };
//            timer.schedule(myTask, 0, 500);
//        }
    }
}
