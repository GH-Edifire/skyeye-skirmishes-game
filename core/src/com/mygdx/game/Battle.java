package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Battle extends Game{
    public SpriteBatch batch;
    public BitmapFont font;
    public int unitCount = 0;
    public int enemyCount = 0;
    public int playerFunds = 0;
    public int enemyFunds = 0;
    public int savedPlayerFunds = 0;
    public int savedEnemyFunds = 0;

    @Override
    public void create(){
        batch = new SpriteBatch();
        // font is Arial Black
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        font.getData().setScale((float) 1.0);
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose(){
        batch.dispose();
        font.dispose();
        super.dispose();
    }

    @Override
    public void render(){
        super.render();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width,height);
        width = 1280;
        height = 720;
    }

    @Override
    public void pause(){
        super.pause();
    }

    @Override
    public void resume(){
        super.resume();
    }
}
