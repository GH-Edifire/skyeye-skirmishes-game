package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BuyScreen implements Screen {
    private final Battle game;
    private int height;
    private int width;
    private OrthographicCamera camera;
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.txt");
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    private Stage stage;
    private ImageButton infantryButton;
    private ImageButton infantryCountButton;
    private ImageButton cavalryButton;
    private ImageButton cavalryCountButton;
    private ImageButton rangedInfantryButton;
    private ImageButton rangedInfantryCountButton;
    private ImageButton artillaryButton;
    private ImageButton artillaryCountButton;
    private ImageButton playButton;
    private ImageButton backButton;
    private int infantryCost = 100;
    private int cavalryCost = 200;
    private int rangedInfantryCost = 150;
    private int artillaryCost = 250;
    private int unitCap = 10;
    private int infantryCount = 0;
    private int cavalryCount = 0;
    private int rangedInfantryCount = 0;
    private int artillaryCount = 0;
    private ArrayList<Actor> playerUnits = new ArrayList<>();
    private ArrayList<Actor> enemyUnits = new ArrayList<>();

    public BuyScreen(final Battle game) {
        this.game = game;
        height = 720;
        width = 1280;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void show() {
        int random;
        addSprites();
        stage = new Stage(new ScreenViewport());
        Random rand = new Random();

        // reset funds based on previously saved
        game.enemyFunds = game.savedEnemyFunds;
        game.playerFunds = game.savedPlayerFunds;

        // populate enemy list based on funds
        // TODO make enemy selection random based + adjust while conditions
        int populateIndex = 0;
        while(game.enemyFunds > 0 && game.enemyCount < 10){
            random = rand.nextInt(5);
            switch(random){
                // enemy infantry
                case 0:
                case 1:
                {
                    if(game.enemyFunds >= infantryCost){
                        Infantry enemy = new Infantry(sprites.get("NATO Infantry Enemy"));
                        enemy.setOwner(1);
                        enemyUnits.add(enemy);
                        if(populateIndex < 5){
                            enemyUnits.get(populateIndex).setPosition(100+ 300*populateIndex,1500);
                        }
                        else{
                            enemyUnits.get(populateIndex).setPosition(100+ 300*(populateIndex-5),1500 - 150);
                        }
                        game.enemyCount +=1;
                        game.enemyFunds -= infantryCost;
                        populateIndex++;
                    }
                    break;
                }
                // enemy cavalry
                case 2:
                {
                    if(game.enemyFunds >= cavalryCost){
                        Cavalry enemytwo = new Cavalry(sprites.get("NATO Cavalry Enemy"));
                        enemytwo.setOwner(1);
                        enemyUnits.add(enemytwo);
                        if(populateIndex < 5){
                            enemyUnits.get(populateIndex).setPosition(100+ 300*populateIndex,1500);
                        }
                        else{
                            enemyUnits.get(populateIndex).setPosition(100+ 300*(populateIndex-5),1500 - 150);
                        }
                        game.enemyCount +=1;
                        game.enemyFunds -= cavalryCost;
                        populateIndex++;
                    }
                    break;
                }
                // enemy ranged
                case 3:
                {
                    if(game.enemyFunds >= rangedInfantryCost){
                        RangedInfantry enemythree = new RangedInfantry(sprites.get("NATO Ranged Infantry Enemy"),1);
                        enemythree.setOwner(1);
                        enemyUnits.add(enemythree);
                        if(populateIndex < 5){
                            enemyUnits.get(populateIndex).setPosition(100+ 300*populateIndex,1500);
                        }
                        else{
                            enemyUnits.get(populateIndex).setPosition(100+ 300*(populateIndex-5),1500 - 150);
                        }
                        game.enemyCount +=1;
                        game.enemyFunds -= rangedInfantryCost;
                        populateIndex++;
                    }
                    break;
                }
                // enemy artillery
                case 4:
                {
                    if(game.enemyFunds >= artillaryCost){
                        Artillary enemyfour = new Artillary(sprites.get("NATO Artillary Enemy"),1);
                        enemyfour.setOwner(1);
                        enemyUnits.add(enemyfour);
                        if(populateIndex < 5){
                            enemyUnits.get(populateIndex).setPosition(100+ 300*populateIndex,1500);
                        }
                        else{
                            enemyUnits.get(populateIndex).setPosition(100+ 300*(populateIndex-5),1500 - 150);
                        }
                        game.enemyCount +=1;
                        game.enemyFunds -= artillaryCost;
                        populateIndex++;
                    }
                    break;
                }
                default:
                {
                    break;
                }
            }
            if(game.enemyFunds < 100){
                break;
            }
        }

        // Infantry Buy Button
        Texture infantryButtonTexture;
        TextureRegion infantryButtonTextureRegion;
        TextureRegionDrawable infantryTextureDrawable;
        infantryButtonTexture = new Texture(Gdx.files.internal("NATO Infantry Player.png"));
        infantryButtonTextureRegion = new TextureRegion(infantryButtonTexture);
        infantryTextureDrawable = new TextureRegionDrawable(infantryButtonTextureRegion);
        infantryButton = new ImageButton(infantryTextureDrawable);
        infantryButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Infantry Button");
                if(game.playerFunds >= infantryCost && game.unitCount < unitCap){
                    game.playerFunds -= infantryCost;
                    infantryCount += 1;
//                    playerUnits.add(new Infantry(sprites.get("NATO Infantry Player")));
//                    playerUnits.get(game.unitCount).setPosition(100+ 300*game.unitCount,100);
                    game.unitCount += 1;
                    System.out.println("bought infantry");
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Infantry Button");
            }
        });
        infantryButton.setPosition(2*width/10-infantryButton.getWidth()/2,6*height/10);
        stage.addActor(infantryButton);

        // Infantry Count Button
        Texture infantryCountTexture;
        TextureRegion infantryCountTextureRegion;
        TextureRegionDrawable infantryCountDrawable;
        infantryCountTexture = new Texture(Gdx.files.internal("NATO Infantry Player.png"));
        infantryCountTextureRegion = new TextureRegion(infantryCountTexture);
        infantryCountDrawable = new TextureRegionDrawable(infantryCountTextureRegion);
        infantryCountButton = new ImageButton(infantryCountDrawable);
        infantryCountButton.setPosition(width/20,height/20);
        infantryCountButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Refund Infantry");
                if(infantryCount > 0){
                    game.playerFunds += infantryCost;
//                    playerUnits.remove(new Infantry(sprites.get("NATO Infantry Player")));
                    infantryCount -= 1;
                    game.unitCount -= 1;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                // nothing
            }
        });
        infantryCountButton.setVisible(true);
        stage.addActor(infantryCountButton);

        // Cavalry Buy Button
        Texture cavalryButtonTexture;
        TextureRegion cavalryButtonTextureRegion;
        TextureRegionDrawable cavalryTextureDrawable;
        cavalryButtonTexture = new Texture(Gdx.files.internal("NATO Cavalry Player.png"));
        cavalryButtonTextureRegion = new TextureRegion(cavalryButtonTexture);
        cavalryTextureDrawable = new TextureRegionDrawable(cavalryButtonTextureRegion);
        cavalryButton = new ImageButton(cavalryTextureDrawable);
        cavalryButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Infantry Button");
                if(game.playerFunds >= cavalryCost && game.unitCount < unitCap){
                    game.playerFunds -= cavalryCost;
                    cavalryCount += 1;
//                    playerUnits.add(new Cavalry(sprites.get("NATO Cavalry Player")));
//                    playerUnits.get(game.unitCount).setPosition(100+ 300*game.unitCount,100);
                    game.unitCount += 1;
                    System.out.println("bought cavalry");
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Infantry Button");
            }
        });
        cavalryButton.setPosition(5*width/10-cavalryButton.getWidth()/2,6*height/10);
        stage.addActor(cavalryButton);

        // Cavalry Count Button
        Texture cavalryCountTexture;
        TextureRegion cavalryCountTextureRegion;
        TextureRegionDrawable cavalryCountDrawable;
        cavalryCountTexture = new Texture(Gdx.files.internal("NATO Cavalry Player.png"));
        cavalryCountTextureRegion = new TextureRegion(cavalryCountTexture);
        cavalryCountDrawable = new TextureRegionDrawable(cavalryCountTextureRegion);
        cavalryCountButton = new ImageButton(cavalryCountDrawable);
        cavalryCountButton.setPosition(5*width/20,height/20);
        cavalryCountButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Refund Cavalry");
                if(cavalryCount > 0){
                    game.playerFunds += cavalryCost;
//                    playerUnits.remove(new Cavalry(sprites.get("NATO Cavalry Player")));
                    cavalryCount -= 1;
                    game.unitCount -= 1;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                // nothing
            }
        });
        cavalryCountButton.setVisible(true);
        stage.addActor(cavalryCountButton);

        // Ranged Infantry Buy Button
        Texture rangedInfantryButtonTexture;
        TextureRegion rangedInfantryButtonTextureRegion;
        TextureRegionDrawable rangedInfantryTextureDrawable;
        rangedInfantryButtonTexture = new Texture(Gdx.files.internal("NATO Ranged Infantry Player.png"));
        rangedInfantryButtonTextureRegion = new TextureRegion(rangedInfantryButtonTexture);
        rangedInfantryTextureDrawable = new TextureRegionDrawable(rangedInfantryButtonTextureRegion);
        rangedInfantryButton = new ImageButton(rangedInfantryTextureDrawable);
        rangedInfantryButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Infantry Button");
                if(game.playerFunds >= rangedInfantryCost && game.unitCount < unitCap){
                    game.playerFunds -= rangedInfantryCost;
                    rangedInfantryCount += 1;
//                    playerUnits.add(new RangedInfantry(sprites.get("NATO Ranged Infantry Player"),0));
//                    playerUnits.get(game.unitCount).setPosition(100+ 300*game.unitCount,100);
                    game.unitCount += 1;
                    System.out.println("bought ranged infantry");
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Ranged Infantry Button");
            }
        });
        rangedInfantryButton.setPosition(2*width/10-cavalryButton.getWidth()/2,3*height/10);
        stage.addActor(rangedInfantryButton);

        // Ranged Infantry Count Button
        Texture rangedInfantryButtonCountTexture;
        TextureRegion rangedInfantryButtonCountTextureRegion;
        TextureRegionDrawable rangedInfantryButtonCountDrawable;
        rangedInfantryButtonCountTexture = new Texture(Gdx.files.internal("NATO Ranged Infantry Player.png"));
        rangedInfantryButtonCountTextureRegion = new TextureRegion(rangedInfantryButtonCountTexture);
        rangedInfantryButtonCountDrawable = new TextureRegionDrawable(rangedInfantryButtonCountTextureRegion);
        rangedInfantryCountButton = new ImageButton(rangedInfantryButtonCountDrawable);
        rangedInfantryCountButton.setPosition(10*width/20,height/20);
        rangedInfantryCountButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Refund Ranged Infantry");
                if(rangedInfantryCount > 0){
                    game.playerFunds += rangedInfantryCost;
//                    playerUnits.remove(new RangedInfantry(sprites.get("NATO Ranged Infantry Player"),0));
                    rangedInfantryCount -= 1;
                    game.unitCount -= 1;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                // nothing
            }
        });
        rangedInfantryCountButton.setVisible(true);
        stage.addActor(rangedInfantryCountButton);

        // Artillary Buy Button
        Texture artillaryButtonTexture;
        TextureRegion artillaryButtonTextureRegion;
        TextureRegionDrawable artillaryTextureDrawable;
        artillaryButtonTexture = new Texture(Gdx.files.internal("NATO Artillary Player.png"));
        artillaryButtonTextureRegion = new TextureRegion(artillaryButtonTexture);
        artillaryTextureDrawable = new TextureRegionDrawable(artillaryButtonTextureRegion);
        artillaryButton = new ImageButton(artillaryTextureDrawable);
        artillaryButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Infantry Button");
                if(game.playerFunds >= artillaryCost && game.unitCount < unitCap){
                    game.playerFunds -= artillaryCost;
                    artillaryCount += 1;
//                    playerUnits.add(new Artillary(sprites.get("NATO Artillary Player"),0));
//                    playerUnits.get(game.unitCount).setPosition(100+ 300*game.unitCount,100);
                    game.unitCount += 1;
                    System.out.println("bought artillary");
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Artillary Button");
            }
        });
        artillaryButton.setPosition(5*width/10-cavalryButton.getWidth()/2,3*height/10);
        stage.addActor(artillaryButton);

        // Artillary Count Button
        Texture artillaryButtonCountTexture;
        TextureRegion artillaryButtonCountTextureRegion;
        TextureRegionDrawable artillaryButtonCountDrawable;
        artillaryButtonCountTexture = new Texture(Gdx.files.internal("NATO Artillary Player.png"));
        artillaryButtonCountTextureRegion = new TextureRegion(artillaryButtonCountTexture);
        artillaryButtonCountDrawable = new TextureRegionDrawable(artillaryButtonCountTextureRegion);
        artillaryCountButton = new ImageButton(artillaryButtonCountDrawable);
        artillaryCountButton.setPosition(15*width/20,height/20);
        artillaryCountButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Refund Ranged Infantry");
                if(artillaryCount > 0){
                    game.playerFunds += artillaryCost;
//                    playerUnits.remove(new Artillary(sprites.get("NATO Artillary Player"),0));
                    artillaryCount -= 1;
                    game.unitCount -= 1;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                // nothing
            }
        });
        artillaryCountButton.setVisible(true);
        stage.addActor(artillaryCountButton);

        // Play Button
        Texture playButtonTexture;
        TextureRegion playButtonTextureRegion;
        TextureRegionDrawable playTextureDrawable;
        playButtonTexture = new Texture(Gdx.files.internal("Play Button.png"));
        playButtonTextureRegion = new TextureRegion(playButtonTexture);
        playTextureDrawable = new TextureRegionDrawable(playButtonTextureRegion);
        playButton = new ImageButton(playTextureDrawable);
        playButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Play");
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(game.unitCount > 0 && game.unitCount <= 10) {
                    System.out.println("Going to Play Screen");
                    for(int i = 0; i < infantryCount; i++){
                        playerUnits.add(new Infantry(sprites.get("NATO Infantry Player")));
                    }
                    for(int i = 0; i < cavalryCount; i++){
                        playerUnits.add(new Cavalry(sprites.get("NATO Cavalry Player")));
                    }
                    for(int i = 0; i < rangedInfantryCount; i++){
                        playerUnits.add(new RangedInfantry(sprites.get("NATO Ranged Infantry Player"),0));
                    }
                    for(int i = 0; i < artillaryCount; i++){
                        playerUnits.add(new Artillary(sprites.get("NATO Artillary Player"),0));
                    }
                    for(int i = 0; i < game.unitCount; i++){
                        if(i < 5){
                            playerUnits.get(i).setPosition(100+ 300*i,100);
                        }
                        else{
                            playerUnits.get(i).setPosition(100+ 300*(i-5),250);
                        }
                    }
                    game.setScreen(new Play(game, playerUnits,enemyUnits));
                }
            }
        });
        playButton.setPosition(width-playButton.getWidth()-width/20,5*height/20);
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);

        // Back Button
        Texture backButtonTexture;
        TextureRegion backButtonTextureRegion;
        TextureRegionDrawable backTextureDrawable;
        backButtonTexture = new Texture(Gdx.files.internal("Back Button.png"));
        backButtonTextureRegion = new TextureRegion(backButtonTexture);
        backTextureDrawable = new TextureRegionDrawable(backButtonTextureRegion);
        backButton = new ImageButton(backTextureDrawable);
        backButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Back");
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Going to Menu Screen");
                game.unitCount = 0;
                game.enemyCount = 0;
                game.setScreen(new FundsScreen(game));
            }
        });
        backButton.setPosition(width-backButton.getWidth()-width/20,17*height/20-backButton.getHeight()/2);
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render text
        game.batch.begin();
        game.font.draw(game.batch, "Funds: "+ game.playerFunds, width/20, height-height/20);

        game.font.draw(game.batch, "Infantry", 2*width/10 - infantryButton.getWidth()/2, 7*height/10 + infantryButton.getHeight()/2 );
        game.font.draw(game.batch, "Price: " + infantryCost, 2*width/10 - infantryButton.getWidth()/2, 6*height/10 - infantryButton.getHeight()/8 );

        game.font.draw(game.batch, "Cavalry", 5*width/10 - cavalryButton.getWidth()/2, 7*height/10 + cavalryButton.getHeight()/2 );
        game.font.draw(game.batch, "Price: " + cavalryCost, 5*width/10 - cavalryButton.getWidth()/2, 6*height/10 - cavalryButton.getHeight()/8 );

        game.font.draw(game.batch, "Ranged Infantry", 2*width/10 - rangedInfantryButton.getWidth()/2, 4*height/10 + rangedInfantryButton.getHeight()/2 );
        game.font.draw(game.batch, "Price: " + rangedInfantryCost, 2*width/10 - rangedInfantryButton.getWidth()/2, 3*height/10 - rangedInfantryButton.getHeight()/8 );

        game.font.draw(game.batch, "Artillery", 5*width/10 - artillaryButton.getWidth()/2, 4*height/10 + artillaryButton.getHeight()/2 );
        game.font.draw(game.batch, "Price: " + artillaryCost, 5*width/10 - artillaryButton.getWidth()/2, 3*height/10 - artillaryButton.getHeight()/8 );

        if(infantryCount > 0){
            infantryCountButton.setVisible(true);
            game.font.draw(game.batch, "x " + infantryCount, width/20+infantryCountButton.getWidth()+5, height/20 + infantryCountButton.getHeight()/4 );
        }
        else{
            infantryCountButton.setVisible(false);
        }
        if(cavalryCount > 0){
            cavalryCountButton.setVisible(true);
            game.font.draw(game.batch, "x " + cavalryCount, 5*width/20+cavalryCountButton.getWidth()+5, height/20 + cavalryCountButton.getHeight()/4 );
        }
        else{
            cavalryCountButton.setVisible(false);
        }
        if(rangedInfantryCount > 0){
            rangedInfantryCountButton.setVisible(true);
            game.font.draw(game.batch, "x " + rangedInfantryCount, 10*width/20+rangedInfantryCountButton.getWidth()+5, height/20 + rangedInfantryCountButton.getHeight()/4 );
        }
        else{
            rangedInfantryCountButton.setVisible(false);
        }
        if(artillaryCount > 0){
            artillaryCountButton.setVisible(true);
            game.font.draw(game.batch, "x " + artillaryCount, 15*width/20+artillaryCountButton.getWidth()+5, height/20 + artillaryCountButton.getHeight()/4 );
        }
        else{
            artillaryCountButton.setVisible(false);
        }
        game.batch.end();

        camera.update();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // TODO dispose actors/stages
        stage.dispose();
    }

    private void addSprites() {
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);
            sprites.put(region.name, sprite);
        }
    }
}
