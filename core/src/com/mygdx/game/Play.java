package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;

public class Play implements Screen {

    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.txt");
    private Stage mapStage;
    private Stage uiStage;
    private Viewport mainMap;
    private FitViewport uiViewport;
    private InputMultiplexer inputMultiplexer;
    private Vector3 mapSize = new Vector3(1600,1600,0);
    private QuadTree quad = new QuadTree(0, new Rectangle(0,0,1600,1600));
    private ArrayList<Actor> allObjects = new ArrayList<Actor>();
    private int height = 720;
    private int width = 1280;
    private int centerUnitX = 0;
    private int centerUnitY = 0;
    private int rangedRadius = 0;
    private ImageButton buyMenuButton, youWinButton, youLoseButton, playButton;
    private boolean isPaused;
    private  ArrayList<Actor> playerUnits;
    private  ArrayList<Actor> enemyUnits;
    private int chasingUnit = Integer.MAX_VALUE;
    private float savedDistance = Float.MAX_VALUE;
    private ShapeRenderer shape;
    private ShapeRenderer deployLimit;
    private Battle game;


    public Play(final Battle game, ArrayList<Actor> playerUnits,ArrayList<Actor> enemyUnits){
        this.game = game;
        this.playerUnits = playerUnits;
        this.enemyUnits = enemyUnits;
    }

    @Override
    public void show(){
        //Vector2 mapSize = new Vector2(1600,1600);
        //Vector3 mapSize = new Vector3(1600,1600,0);
        shape = new ShapeRenderer();
        deployLimit = new ShapeRenderer();
        isPaused = true;
        map =  new TmxMapLoader().load("maps/Battle Map 1.tmx");
        addSprites();
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.zoom = 1.0f;
        camera.unproject(mapSize);

        mainMap = new FitViewport(width,height, camera);
        mapStage = new Stage(mainMap);
        uiViewport = new FitViewport(width,height);
        uiStage = new Stage(uiViewport);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mapStage);
        inputMultiplexer.addProcessor(uiStage);
        inputMultiplexer.addProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                camera.zoom += amount * 0.1f;
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Place Player Units
        for(int i = 0; i < game.unitCount; i++){
            allObjects.add(playerUnits.get(i)); // add to collision detection
            mapStage.addActor(playerUnits.get(i));
            if(playerUnits.get(i).getName().equalsIgnoreCase("R")){
//                System.out.println(((Group)playerUnits.get(i)).getChildren());
                Bullet bulletOne = new Bullet(sprites.get("Bullet"),0);
                bulletOne.setVisible(false);
                Bullet bulletTwo = new Bullet(sprites.get("Bullet"),0);
                bulletTwo.setVisible(false);
                Bullet bulletThree = new Bullet(sprites.get("Bullet"),0);
                bulletThree.setVisible(false);
                ((RangedInfantry)playerUnits.get(i)).setBullets(bulletOne,bulletTwo,bulletThree);
                allObjects.add(bulletOne);
                allObjects.add(bulletTwo);
                allObjects.add(bulletThree);
                mapStage.addActor(bulletOne);
                mapStage.addActor(bulletTwo);
                mapStage.addActor(bulletThree);
            }
            else if(playerUnits.get(i).getName().equalsIgnoreCase("A")){
//                System.out.println(((Group)playerUnits.get(i)).getChildren());
                Bullet bulletOne = new Bullet(sprites.get("Bullet"),0);
                bulletOne.setVisible(false);
                bulletOne.setDamage(300);
                ((Artillary)playerUnits.get(i)).setBullets(bulletOne);
                allObjects.add(bulletOne);
                mapStage.addActor(bulletOne);
            }
        }
        // prepare for checking later
        game.unitCount = 0;

        // Place Enemy units
        for(int i = 0; i < game.enemyCount; i++){
            allObjects.add(enemyUnits.get(i)); // add to collision detection
            mapStage.addActor(enemyUnits.get(i));
            if(enemyUnits.get(i).getName().equalsIgnoreCase("R")){
//                System.out.println(((Group)enemyUnits.get(i)).getChildren());
                Bullet bulletOne = new Bullet(sprites.get("Bullet"),1);
                bulletOne.setVisible(false);
                Bullet bulletTwo = new Bullet(sprites.get("Bullet"),1);
                bulletTwo.setVisible(false);
                Bullet bulletThree = new Bullet(sprites.get("Bullet"),1);
                bulletThree.setVisible(false);
                ((RangedInfantry)enemyUnits.get(i)).setBullets(bulletOne,bulletTwo,bulletThree);
                allObjects.add(bulletOne);
                allObjects.add(bulletTwo);
                allObjects.add(bulletThree);
                mapStage.addActor(bulletOne);
                mapStage.addActor(bulletTwo);
                mapStage.addActor(bulletThree);
            }
            else if(enemyUnits.get(i).getName().equalsIgnoreCase("A")){
//                System.out.println(((Group)enemyUnits.get(i)).getChildren());
                Bullet bulletOne = new Bullet(sprites.get("Bullet"),1);
                bulletOne.setVisible(false);
                bulletOne.setDamage(350);
                ((Artillary)enemyUnits.get(i)).setBullets(bulletOne);
                allObjects.add(bulletOne);
                mapStage.addActor(bulletOne);
            }
        }
        // prepare for checking later
        game.enemyCount = 0;

        // Buy Menu Button
        Texture buyMenuButtonTexture;
        TextureRegion buyMenuButtonTextureRegion;
        TextureRegionDrawable buyMenuButtonDrawable;
        buyMenuButtonTexture = new Texture(Gdx.files.internal("Buy Menu Button.png"));
        buyMenuButtonTextureRegion = new TextureRegion(buyMenuButtonTexture);
        buyMenuButtonDrawable = new TextureRegionDrawable(buyMenuButtonTextureRegion);
        buyMenuButton = new ImageButton(buyMenuButtonDrawable);
        buyMenuButton.setPosition(width/2,2*height/4,999);
        buyMenuButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("Back to buy menu");
                isPaused = true;
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new BuyScreen(game));
            }
        });
        buyMenuButton.setVisible(false);
        buyMenuButton.setName("Buy Menu");
        //mapStage uiStage
        uiStage.addActor(buyMenuButton);

        // You Lose Button
        Texture youLoseButtonTexture;
        TextureRegion youLoseButtonTextureRegion;
        TextureRegionDrawable youLoseButtonDrawable;
        youLoseButtonTexture = new Texture(Gdx.files.internal("Lose Button.png"));
        youLoseButtonTextureRegion = new TextureRegion(youLoseButtonTexture);
        youLoseButtonDrawable = new TextureRegionDrawable(youLoseButtonTextureRegion);
        youLoseButton = new ImageButton(youLoseButtonDrawable);
        youLoseButton.setPosition(width/2,3*height/4,999);
        youLoseButton.setVisible(false);
        youLoseButton.setName("Lose");
        uiStage.addActor(youLoseButton);

        // You Win Button
        Texture youWinButtonTexture;
        TextureRegion youWinButtonTextureRegion;
        TextureRegionDrawable youWinButtonDrawable;
        youWinButtonTexture = new Texture(Gdx.files.internal("Win Button.png"));
        youWinButtonTextureRegion = new TextureRegion(youWinButtonTexture);
        youWinButtonDrawable = new TextureRegionDrawable(youWinButtonTextureRegion);
        youWinButton = new ImageButton(youWinButtonDrawable);
        youWinButton.setPosition(width/2,3*height/4,999);
        youWinButton.setVisible(false);
        youWinButton.setName("Win");
        uiStage.addActor(youWinButton);

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
//                System.out.println("Clicked Play");
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                playButton.setVisible(false);
                isPaused = false;
            }
        });
        playButton.setPosition(width/2,2*height/3 + playButton.getHeight()/2,999);
        uiStage.addActor(playButton);
    }

    private List<Actor> returnObjects = new ArrayList<Actor>();
    @Override
    public void render(float delta){
        //Gdx.gl.glClearColor(.13f, .37f, .13f, 1); // hunter green
        Gdx.gl.glClearColor(0, 0, 0, 1); // black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //stage.act(Gdx.graphics.getDeltaTime());
        //stage.draw();

        // quad tree implementation
        quad.clear();
        for (int i = 0; i < allObjects.size(); i++) {
            quad.insert(allObjects.get(i));
        }

        for (int i = 0; i < allObjects.size(); i++) {
            if(allObjects.get(i).getName().equalsIgnoreCase("delete")){
                allObjects.get(i).remove();
            }
        }

        for (int i = 0; i < allObjects.size(); i++) {
            returnObjects.clear();
            quad.retrieve(returnObjects, allObjects.get(i));

            for (int x = 0; x < returnObjects.size(); x++) {
                // Run collision detection algorithm between objects
                String name = allObjects.get(i).getName();
                String enemyName = allObjects.get(x).getName();
                // TODO assuming both are infantry, need separate cases as more unit types are created
                switch (name) {
                    case "I": {
                        switch (enemyName) {
                            case "I": {
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) returnObjects.get(x);
                                if (infantryOne != infantryTwo // ignore if same object
                                        && infantryOne.getOwner() != infantryTwo.getOwner() // ignore collision on friendly units
                                        && infantryOne.getBounds().overlaps(infantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    infantryOne.clearActions();
                                    infantryTwo.clearActions();
                                    infantryOne.setCollision(true);
                                    infantryTwo.setCollision(true);
                                    infantryOne.attackUnit(infantryOne, infantryTwo);
                                    infantryTwo.attackUnit(infantryTwo, infantryOne);
                                }
                                break;
                            }
                            case "C": {
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) returnObjects.get(x);
                                if (infantryOne.getOwner() != cavalryTwo.getOwner() // ignore collision on friendly units
                                        && infantryOne.getBounds().overlaps(cavalryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    infantryOne.clearActions();
                                    cavalryTwo.clearActions();
                                    infantryOne.setCollision(true);
                                    cavalryTwo.setCollision(true);
                                    infantryOne.attackUnit(infantryOne, cavalryTwo);
                                    cavalryTwo.attackUnit(cavalryTwo, infantryOne);
                                }
                                break;
                            }
                            case "R": {
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) returnObjects.get(x);
                                if (infantryOne.getOwner() != rangedInfantryTwo.getOwner() // ignore collision on friendly units
                                        && infantryOne.getBounds().overlaps(rangedInfantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    infantryOne.clearActions();
                                    rangedInfantryTwo.clearActions();
                                    infantryOne.setCollision(true);
                                    rangedInfantryTwo.setCollision(true);
                                    infantryOne.attackUnit(infantryOne, rangedInfantryTwo);
                                    rangedInfantryTwo.attackUnit(rangedInfantryTwo, infantryOne);
                                }
                                break;
                            }
                            case "A": {
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) returnObjects.get(x);
                                if (infantryOne.getOwner() != artillaryTwo.getOwner() // ignore collision on friendly units
                                        && infantryOne.getBounds().overlaps(artillaryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    infantryOne.clearActions();
                                    artillaryTwo.clearActions();
                                    infantryOne.setCollision(true);
                                    artillaryTwo.setCollision(true);
                                    infantryOne.attackUnit(infantryOne, artillaryTwo);
                                    artillaryTwo.attackUnit(artillaryTwo, infantryOne);
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        break;
                    }
                    case "C": {
                        switch (enemyName) {
                            case "I": {
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) returnObjects.get(x);
                                if (cavalryOne.getOwner() != infantryTwo.getOwner() // ignore collision on friendly units
                                        && cavalryOne.getBounds().overlaps(infantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    cavalryOne.clearActions();
                                    infantryTwo.clearActions();
                                    cavalryOne.setCollision(true);
                                    infantryTwo.setCollision(true);
                                    cavalryOne.attackUnit(cavalryOne, infantryTwo);
                                    infantryTwo.attackUnit(infantryTwo, cavalryOne);
                                }
                                break;
                            }
                            case "C": {
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) returnObjects.get(x);
                                if (cavalryOne != cavalryTwo // ignore if same object
                                        && cavalryOne.getOwner() != cavalryTwo.getOwner() // ignore collision on friendly units
                                        && cavalryOne.getBounds().overlaps(cavalryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    cavalryOne.clearActions();
                                    cavalryTwo.clearActions();
                                    cavalryOne.setCollision(true);
                                    cavalryTwo.setCollision(true);
                                    cavalryOne.attackUnit(cavalryOne, cavalryTwo);
                                    cavalryTwo.attackUnit(cavalryTwo, cavalryOne);
                                }
                                break;
                            }
                            case "R": {
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) returnObjects.get(x);
                                if (cavalryOne.getOwner() != rangedInfantryTwo.getOwner() // ignore collision on friendly units
                                        && cavalryOne.getBounds().overlaps(rangedInfantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    cavalryOne.clearActions();
                                    rangedInfantryTwo.clearActions();
                                    cavalryOne.setCollision(true);
                                    rangedInfantryTwo.setCollision(true);
                                    cavalryOne.attackUnit(cavalryOne, rangedInfantryTwo);
                                    rangedInfantryTwo.attackUnit(rangedInfantryTwo, cavalryOne);
                                }
                                break;
                            }
                            case "A": {
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) returnObjects.get(x);
                                if (cavalryOne.getOwner() != artillaryTwo.getOwner() // ignore collision on friendly units
                                        && cavalryOne.getBounds().overlaps(artillaryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    cavalryOne.clearActions();
                                    artillaryTwo.clearActions();
                                    cavalryOne.setCollision(true);
                                    artillaryTwo.setCollision(true);
                                    cavalryOne.attackUnit(cavalryOne, artillaryTwo);
                                    artillaryTwo.attackUnit(artillaryTwo, cavalryOne);
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        break;
                    }
                    case "R": {
                        switch (enemyName) {
                            case "I": {
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) returnObjects.get(x);
                                if (rangedInfantryOne.getOwner() != infantryTwo.getOwner() // ignore collision on friendly units
                                        && rangedInfantryOne.getBounds().overlaps(infantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    rangedInfantryOne.clearActions();
                                    infantryTwo.clearActions();
                                    rangedInfantryOne.setCollision(true);
                                    infantryTwo.setCollision(true);
                                    rangedInfantryOne.attackUnit(rangedInfantryOne, infantryTwo);
                                    infantryTwo.attackUnit(infantryTwo, rangedInfantryOne);
                                }
                                break;
                            }
                            case "C": {
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) returnObjects.get(x);
                                if ( rangedInfantryOne.getOwner() != cavalryTwo.getOwner() // ignore collision on friendly units
                                        && rangedInfantryOne.getBounds().overlaps(cavalryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    rangedInfantryOne.clearActions();
                                    cavalryTwo.clearActions();
                                    rangedInfantryOne.setCollision(true);
                                    cavalryTwo.setCollision(true);
                                    rangedInfantryOne.attackUnit(rangedInfantryOne, cavalryTwo);
                                    cavalryTwo.attackUnit(cavalryTwo, rangedInfantryOne);
                                }
                                break;
                            }
                            case "R": {
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) returnObjects.get(x);
                                if ( rangedInfantryOne != rangedInfantryTwo // ignore if same object
                                        && rangedInfantryOne.getOwner() != rangedInfantryTwo.getOwner() // ignore collision on friendly units
                                        && rangedInfantryOne.getBounds().overlaps(rangedInfantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    rangedInfantryOne.clearActions();
                                    rangedInfantryTwo.clearActions();
                                    rangedInfantryOne.setCollision(true);
                                    rangedInfantryTwo.setCollision(true);
                                    rangedInfantryOne.attackUnit(rangedInfantryOne, rangedInfantryTwo);
                                    rangedInfantryTwo.attackUnit(rangedInfantryTwo, rangedInfantryOne);
                                }
                                break;
                            }
                            case "A": {
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) returnObjects.get(x);
                                if (rangedInfantryOne.getOwner() != artillaryTwo.getOwner() // ignore collision on friendly units
                                        && rangedInfantryOne.getBounds().overlaps(artillaryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    rangedInfantryOne.clearActions();
                                    artillaryTwo.clearActions();
                                    rangedInfantryOne.setCollision(true);
                                    artillaryTwo.setCollision(true);
                                    rangedInfantryOne.attackUnit(rangedInfantryOne, artillaryTwo);
                                    artillaryTwo.attackUnit(artillaryTwo, rangedInfantryOne);
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        break;
                    }
                    case "A": {
                        switch (enemyName) {
                            case "I": {
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) returnObjects.get(x);
                                if (artillaryOne.getOwner() != infantryTwo.getOwner() // ignore collision on friendly units
                                        && artillaryOne.getBounds().overlaps(infantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    artillaryOne.clearActions();
                                    infantryTwo.clearActions();
                                    artillaryOne.setCollision(true);
                                    infantryTwo.setCollision(true);
                                    artillaryOne.attackUnit(artillaryOne, infantryTwo);
                                    infantryTwo.attackUnit(infantryTwo, artillaryOne);
                                }
                                break;
                            }
                            case "C": {
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) returnObjects.get(x);
                                if ( artillaryOne.getOwner() != cavalryTwo.getOwner() // ignore collision on friendly units
                                        && artillaryOne.getBounds().overlaps(cavalryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    artillaryOne.clearActions();
                                    cavalryTwo.clearActions();
                                    artillaryOne.setCollision(true);
                                    cavalryTwo.setCollision(true);
                                    artillaryOne.attackUnit(artillaryOne, cavalryTwo);
                                    cavalryTwo.attackUnit(cavalryTwo, artillaryOne);
                                }
                                break;
                            }
                            case "R": {
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) returnObjects.get(x);
                                if ( artillaryOne.getOwner() != rangedInfantryTwo.getOwner() // ignore collision on friendly units
                                        && artillaryOne.getBounds().overlaps(rangedInfantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    artillaryOne.clearActions();
                                    rangedInfantryTwo.clearActions();
                                    artillaryOne.setCollision(true);
                                    rangedInfantryTwo.setCollision(true);
                                    artillaryOne.attackUnit(artillaryOne, rangedInfantryTwo);
                                    rangedInfantryTwo.attackUnit(rangedInfantryTwo, artillaryOne);
                                }
                                break;
                            }
                            case "A": {
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) returnObjects.get(x);
                                if ( artillaryOne != artillaryTwo // ignore if same object
                                        && artillaryOne.getOwner() != artillaryTwo.getOwner() // ignore collision on friendly units
                                        && artillaryOne.getBounds().overlaps(artillaryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    artillaryOne.clearActions();
                                    artillaryTwo.clearActions();
                                    artillaryOne.setCollision(true);
                                    artillaryTwo.setCollision(true);
                                    artillaryOne.attackUnit(artillaryOne, artillaryTwo);
                                    artillaryTwo.attackUnit(artillaryTwo, artillaryOne);
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        break;
                    }
                    case "B":
                    {
                        switch (enemyName) {
                            case "I": {
                                Bullet bulletOne = (Bullet) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) returnObjects.get(x);
                                if (bulletOne.getOwner() != infantryTwo.getOwner() // ignore collision on friendly units
                                        && bulletOne.getBounds().overlaps(infantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding bullet");
                                    bulletOne.clearActions();
//                                    System.out.println("bullet damage: " + bulletOne.getDamage());
                                    infantryTwo.setHealth(infantryTwo.getHealth() - bulletOne.getDamage());
                                }
                                break;
                            }
                            case "C": {
                                Bullet bulletOne = (Bullet) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) returnObjects.get(x);
                                if ( bulletOne.getOwner() != cavalryTwo.getOwner() // ignore collision on friendly units
                                        && bulletOne.getBounds().overlaps(cavalryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    bulletOne.clearActions();
                                    cavalryTwo.setHealth(cavalryTwo.getHealth() - bulletOne.getDamage());
                                }
                                break;
                            }
                            case "R": {
                                Bullet bulletOne = (Bullet) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) returnObjects.get(x);
                                if ( bulletOne.getOwner() != rangedInfantryTwo.getOwner() // ignore collision on friendly units
                                        && bulletOne.getBounds().overlaps(rangedInfantryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    bulletOne.clearActions();
                                    rangedInfantryTwo.setHealth(rangedInfantryTwo.getHealth() - bulletOne.getDamage());
                                }
                                break;
                            }
                            case "A": {
                                Bullet bulletOne = (Bullet) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) returnObjects.get(x);
                                if ( bulletOne.getOwner() != artillaryTwo.getOwner() // ignore collision on friendly units
                                        && bulletOne.getBounds().overlaps(artillaryTwo.getBounds())) // stop moving if collide with enemy unit
                                {
//                                    System.out.println("colliding");
                                    bulletOne.clearActions();
                                    artillaryTwo.setHealth(artillaryTwo.getHealth() - bulletOne.getDamage());
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }

        // TODO check cases for all unit types+
        float playerX, playerY, distance;
        // AI enemy choosing which unit to chase
        for (int i = 0; i < allObjects.size(); i++) {
            for (int x = 0; x < allObjects.size(); x++) {
                String name = allObjects.get(i).getName();
                String enemyName = allObjects.get(x).getName();
                switch(name){
                    case "I":
                    {
                        switch(enemyName){
                            case "I":{
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(infantryOne.getOwner() == 1)
                                {
                                    if(infantryTwo.getOwner() == 0)
                                    {
                                        playerX = infantryTwo.getX();
                                        playerY = infantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((infantryOne.getX()-infantryOne.getWidth()/2) - playerX, 2) + Math.pow((infantryOne.getY() - infantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "C":
                            {
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(infantryOne.getOwner() == 1)
                                {
                                    if(cavalryTwo.getOwner() == 0)
                                    {
                                        playerX = cavalryTwo.getX();
                                        playerY = cavalryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((infantryOne.getX()-infantryOne.getWidth()/2) - playerX, 2) + Math.pow((infantryOne.getY() - infantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "R":
                            {
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(infantryOne.getOwner() == 1)
                                {
                                    if(rangedInfantryTwo.getOwner() == 0)
                                    {
                                        playerX = rangedInfantryTwo.getX();
                                        playerY = rangedInfantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((infantryOne.getX()-infantryOne.getWidth()/2) - playerX, 2) + Math.pow((infantryOne.getY() - infantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "A":
                            {
                                Infantry infantryOne = (Infantry) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(infantryOne.getOwner() == 1)
                                {
                                    if(artillaryTwo.getOwner() == 0)
                                    {
                                        playerX = artillaryTwo.getX();
                                        playerY = artillaryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((infantryOne.getX()-infantryOne.getWidth()/2) - playerX, 2) + Math.pow((infantryOne.getY() - infantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    case "C":
                    {
                        switch(enemyName){
                            case "I":{
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(cavalryOne.getOwner() == 1)
                                {
                                    if(infantryTwo.getOwner() == 0)
                                    {
                                        playerX = infantryTwo.getX();
                                        playerY = infantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((cavalryOne.getX()-cavalryOne.getWidth()/2) - playerX, 2) + Math.pow((cavalryOne.getY() - cavalryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "C":
                            {
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(cavalryOne.getOwner() == 1)
                                {
                                    if(cavalryTwo.getOwner() == 0)
                                    {
                                        playerX = cavalryTwo.getX();
                                        playerY = cavalryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((cavalryOne.getX()-cavalryOne.getWidth()/2) - playerX, 2) + Math.pow((cavalryOne.getY() - cavalryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "R":
                            {
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(cavalryOne.getOwner() == 1)
                                {
                                    if(rangedInfantryTwo.getOwner() == 0)
                                    {
                                        playerX = rangedInfantryTwo.getX();
                                        playerY = rangedInfantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((cavalryOne.getX()-cavalryOne.getWidth()/2) - playerX, 2) + Math.pow((cavalryOne.getY() - cavalryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "A":
                            {
                                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(cavalryOne.getOwner() == 1)
                                {
                                    if(artillaryTwo.getOwner() == 0)
                                    {
                                        playerX = artillaryTwo.getX();
                                        playerY = artillaryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((cavalryOne.getX()-cavalryOne.getWidth()/2) - playerX, 2) + Math.pow((cavalryOne.getY() - cavalryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    case "R":
                    {
                        switch(enemyName){
                            case "I":{
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(rangedInfantryOne.getOwner() == 1)
                                {
                                    if(infantryTwo.getOwner() == 0)
                                    {
                                        playerX = infantryTwo.getX();
                                        playerY = infantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((rangedInfantryOne.getX()-rangedInfantryOne.getWidth()/2) - playerX, 2) + Math.pow((rangedInfantryOne.getY() - rangedInfantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "C":
                            {
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(rangedInfantryOne.getOwner() == 1)
                                {
                                    if(cavalryTwo.getOwner() == 0)
                                    {
                                        playerX = cavalryTwo.getX();
                                        playerY = cavalryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((rangedInfantryOne.getX()-rangedInfantryOne.getWidth()/2) - playerX, 2) + Math.pow((rangedInfantryOne.getY() - rangedInfantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "R":
                            {
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(rangedInfantryOne.getOwner() == 1)
                                {
                                    if(rangedInfantryTwo.getOwner() == 0)
                                    {
                                        playerX = rangedInfantryTwo.getX();
                                        playerY = rangedInfantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((rangedInfantryOne.getX()-rangedInfantryOne.getWidth()/2) - playerX, 2) + Math.pow((rangedInfantryOne.getY() - rangedInfantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "A":
                            {
                                RangedInfantry rangedInfantryOne = (RangedInfantry) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(rangedInfantryOne.getOwner() == 1)
                                {
                                    if(artillaryTwo.getOwner() == 0)
                                    {
                                        playerX = artillaryTwo.getX();
                                        playerY = artillaryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((rangedInfantryOne.getX()-rangedInfantryOne.getWidth()/2) - playerX, 2) + Math.pow((rangedInfantryOne.getY() - rangedInfantryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    case "A":
                    {
                        switch(enemyName){
                            case "I":{
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                Infantry infantryTwo = (Infantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(artillaryOne.getOwner() == 1)
                                {
                                    if(infantryTwo.getOwner() == 0)
                                    {
                                        playerX = infantryTwo.getX();
                                        playerY = infantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((artillaryOne.getX()-artillaryOne.getWidth()/2) - playerX, 2) + Math.pow((artillaryOne.getY() - artillaryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "C":
                            {
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                Cavalry cavalryTwo = (Cavalry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(artillaryOne.getOwner() == 1)
                                {
                                    if(cavalryTwo.getOwner() == 0)
                                    {
                                        playerX = cavalryTwo.getX();
                                        playerY = cavalryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((artillaryOne.getX()-artillaryOne.getWidth()/2) - playerX, 2) + Math.pow((artillaryOne.getY() - artillaryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "R":
                            {
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                RangedInfantry rangedInfantryTwo = (RangedInfantry) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(artillaryOne.getOwner() == 1)
                                {
                                    if(rangedInfantryTwo.getOwner() == 0)
                                    {
                                        playerX = rangedInfantryTwo.getX();
                                        playerY = rangedInfantryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((artillaryOne.getX()-artillaryOne.getWidth()/2) - playerX, 2) + Math.pow((artillaryOne.getY() - artillaryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            case "A":
                            {
                                Artillary artillaryOne = (Artillary) allObjects.get(i);
                                Artillary artillaryTwo = (Artillary) allObjects.get(x);
                                // for each enemy unit, look for closest player unit and chase it
                                if(artillaryOne.getOwner() == 1)
                                {
                                    if(artillaryTwo.getOwner() == 0)
                                    {
                                        playerX = artillaryTwo.getX();
                                        playerY = artillaryTwo.getY();
                                        distance = (float) Math.sqrt( Math.pow((artillaryOne.getX()-artillaryOne.getWidth()/2) - playerX, 2) + Math.pow((artillaryOne.getY() - artillaryOne.getHeight()/2) - playerY, 2));
                                        if(distance < savedDistance)
                                        {
                                            chasingUnit = x;
                                            savedDistance = distance;
                                        }
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            // Prevent out of bounds error
            if(chasingUnit == Integer.MAX_VALUE){
                chasingUnit = 0;
            }

            // Enemy chase after unit here
            if(allObjects.get(i).getName().equalsIgnoreCase("i")){
                Infantry infantryOne = (Infantry) allObjects.get(i);
                if(infantryOne.getOwner() == 1
                        && !infantryOne.isAttacking()){
                    infantryOne.moveUnit((int) allObjects.get(chasingUnit).getX() + (int) allObjects.get(chasingUnit).getWidth()/2,
                            (int) allObjects.get(chasingUnit).getY() + (int )allObjects.get(chasingUnit).getHeight()/2);
                }
                else if(infantryOne.getOwner() == 1
                        && infantryOne.isAttacking()){
                    if(savedDistance >= 100){
                        infantryOne.setAttacking(false);
                    }
                }
            }
            else if(allObjects.get(i).getName().equalsIgnoreCase("c")){
                Cavalry cavalryOne = (Cavalry) allObjects.get(i);
                if(cavalryOne.getOwner() == 1
                        && !cavalryOne.isAttacking()){
                    cavalryOne.moveUnit((int) allObjects.get(chasingUnit).getX() + (int) allObjects.get(chasingUnit).getWidth()/2,
                            (int) allObjects.get(chasingUnit).getY() + (int )allObjects.get(chasingUnit).getHeight()/2);
                }
                else if(cavalryOne.getOwner() == 1
                        && cavalryOne.isAttacking()){
                    if(savedDistance >= 100){
                        cavalryOne.setAttacking(false);
                    }
                }
            }
            else if(allObjects.get(i).getName().equalsIgnoreCase("r")){
                RangedInfantry rangedOne = (RangedInfantry) allObjects.get(i);
                if(rangedOne.getOwner() == 1
                        && !rangedOne.isAttacking()){
//                    rangedOne.moveUnit((int) allObjects.get(chasingUnit).getX() + (int) allObjects.get(chasingUnit).getWidth()/2,
//                            (int) allObjects.get(chasingUnit).getY() + (int )allObjects.get(chasingUnit).getHeight()/2);
                    rangedOne.attackUnit(rangedOne,allObjects.get(chasingUnit));
                }
                else if(rangedOne.getOwner() == 1
                        && rangedOne.isAttacking()){
                    if(savedDistance >= 100){
                        rangedOne.setAttacking(false);
                    }
                }
            }
            else if(allObjects.get(i).getName().equalsIgnoreCase("a")){
                Artillary rangedOne = (Artillary) allObjects.get(i);
                if(rangedOne.getOwner() == 1
                        && !rangedOne.isAttacking()){
//                    rangedOne.moveUnit((int) allObjects.get(chasingUnit).getX() + (int) allObjects.get(chasingUnit).getWidth()/2,
//                            (int) allObjects.get(chasingUnit).getY() + (int )allObjects.get(chasingUnit).getHeight()/2);
                    rangedOne.attackUnit(rangedOne,allObjects.get(chasingUnit));
                }
                else if(rangedOne.getOwner() == 1
                        && rangedOne.isAttacking()){
                    if(savedDistance >= 100){
                        rangedOne.setAttacking(false);
                    }
                }
            }
            savedDistance = Float.MAX_VALUE;
            chasingUnit = Integer.MAX_VALUE;
        }



        // TODO add for each unit type
        // constantly check who still has units
        for(int i = 0; i < mapStage.getActors().size; i++){
            if(mapStage.getActors().items[i].getName().equalsIgnoreCase("i")){
                Infantry compare = (Infantry) mapStage.getActors().items[i];
                if(compare.getOwner() == 0){
                    game.unitCount += 1;
                }
                else{
                    game.enemyCount += 1;
                }
            }
            if(mapStage.getActors().items[i].getName().equalsIgnoreCase("c")){
                Cavalry compare = (Cavalry) mapStage.getActors().items[i];
                if(compare.getOwner() == 0){
                    game.unitCount += 1;
                }
                else{
                    game.enemyCount += 1;
                }
            }
            if(mapStage.getActors().items[i].getName().equalsIgnoreCase("r")){
                RangedInfantry compare = (RangedInfantry) mapStage.getActors().items[i];
                if(compare.getOwner() == 0){
                    game.unitCount += 1;
                }
                else{
                    game.enemyCount += 1;
                }
            }
            if(mapStage.getActors().items[i].getName().equalsIgnoreCase("a")){
                Artillary compare = (Artillary) mapStage.getActors().items[i];
                if(compare.getOwner() == 0){
                    game.unitCount += 1;
                }
                else{
                    game.enemyCount += 1;
                }
            }
        }
        if(game.enemyCount == 0){
            isPaused = true;
            youWinButton.setVisible(true);
            buyMenuButton.setVisible(true);
        }
        else if(game.unitCount == 0){
            isPaused = true;
            youLoseButton.setVisible(true);
            buyMenuButton.setVisible(true);
        }

        if(isPaused){
            delta = 0;
        }
        handleInput();
        camera.update();

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        renderer.getBatch().end();
        if(isPaused){
            mapStage.act(delta);
        }
        else{
            mapStage.act(Gdx.graphics.getDeltaTime());
        }
        mapStage.draw();
        uiStage.act();
        uiStage.draw();

        // draw ranged circle
        Gdx.gl.glEnable(GL_BLEND);
        Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.circle(centerUnitX, centerUnitY, rangedRadius);
        shape.end();
        //Gdx.gl.glDisable(GL_BLEND);

        // draw deploy limit
        //Gdx.gl.glEnable(GL_BLEND);
        //Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        deployLimit.setProjectionMatrix(camera.combined);
        deployLimit.begin(ShapeRenderer.ShapeType.Line);
        if(isPaused){
            deployLimit.line(0,400,1600,400);
            deployLimit.setColor(Color.BLUE);
        }
        else{
            deployLimit.setColor(Color.CLEAR);
        }
        deployLimit.end();
        Gdx.gl.glDisable(GL_BLEND);

        // reset unit counts
        game.unitCount = 0;
        game.enemyCount = 0;
    }

    @Override
    public void resize(int width, int height){
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        mainMap.update(width, height);
    }

    @Override
    public void hide(){
        dispose();
    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void dispose(){
        map.dispose();
        renderer.dispose();
        mapStage.dispose();
        uiStage.dispose();
    }

    private void addSprites() {
        Array<AtlasRegion> regions = textureAtlas.getRegions();

        for (AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            sprites.put(region.name, sprite);
        }
    }

    private Actor hitActor;
    private Actor hitActorTwo;
    private Infantry hitInfantry;
    private Cavalry hitCavalry;
    private RangedInfantry hitRangedInfantry;
    private Artillary hitArtillary;
    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-10, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(10, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -10, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, 10, 0);
        }
//        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !isPaused) {

        if(hitRangedInfantry != null && hitRangedInfantry.isSelected()){
            centerUnitX = (int) hitRangedInfantry.getX() + (int) (hitRangedInfantry.getWidth()/2);
            centerUnitY = (int) hitRangedInfantry.getY() + (int) (hitRangedInfantry.getHeight()/2);
            rangedRadius = 400;
            shape.setColor(Color.GOLD);
        }
        else if(hitArtillary != null && hitArtillary.isSelected()){
            centerUnitX = (int) hitArtillary.getX() + (int) (hitArtillary.getWidth()/2);
            centerUnitY = (int) hitArtillary.getY() + (int) (hitArtillary.getHeight()/2);
            rangedRadius = 600;
            shape.setColor(Color.GOLD);
        }
        else{
            shape.setColor(Color.CLEAR);
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            camera.unproject(mapSize.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            hitActor = mapStage.hit(mapSize.x,mapSize.y,true);
            if(hitActor != null){
//                System.out.println("HIT " + hitActor.getName());
                // TODO check cases for all unit types
                if(hitInfantry != null){
//                    System.out.println("unselected");
                    hitInfantry.setSelected(false);
                    hitInfantry.colorUnit(Color.WHITE);
                }
                if(hitCavalry != null){
//                    System.out.println("unselected");
                    hitCavalry.setSelected(false);
                    hitCavalry.colorUnit(Color.WHITE);
                }
                if(hitRangedInfantry != null){
//                    System.out.println("unselected");
                    hitRangedInfantry.setSelected(false);
                    hitRangedInfantry.colorUnit(Color.WHITE);
                }
                if(hitArtillary != null){
//                    System.out.println("unselected");
                    hitArtillary.setSelected(false);
                    hitArtillary.colorUnit(Color.WHITE);

                }
                // TODO check cases for all unit types
                if(hitActor.getName().equalsIgnoreCase("i")){
                    hitInfantry = (Infantry) hitActor;
                    if(hitInfantry.getOwner() == 0){
                        hitInfantry.setSelected(true);
                        hitInfantry.colorUnit(Color.GOLD);
                    }
                }
                else if(hitActor.getName().equalsIgnoreCase("c")){
                    hitCavalry = (Cavalry) hitActor;
                    if(hitCavalry.getOwner() == 0){
                        hitCavalry.setSelected(true);
                        hitCavalry.colorUnit(Color.GOLD);
                    }
                }
                else if(hitActor.getName().equalsIgnoreCase("r")){
                    hitRangedInfantry = (RangedInfantry) hitActor;
                    if(hitRangedInfantry.getOwner() == 0){
                        hitRangedInfantry.setSelected(true);
                        hitRangedInfantry.colorUnit(Color.GOLD);
                    }
                }
                else if(hitActor.getName().equalsIgnoreCase("a")){
                    hitArtillary = (Artillary) hitActor;
                    if(hitArtillary.getOwner() == 0){
                        hitArtillary.setSelected(true);
                        hitArtillary.colorUnit(Color.GOLD);
                    }
                }
            }
            else{
                // TODO check cases for all unit types
                if(hitInfantry != null){
//                    System.out.println("unselected");
                    hitInfantry.setSelected(false);
                    hitInfantry.colorUnit(Color.WHITE);
                }
                if(hitCavalry != null){
//                    System.out.println("unselected");
                    hitCavalry.setSelected(false);
                    hitCavalry.colorUnit(Color.WHITE);
                }
                if(hitRangedInfantry != null){
//                    System.out.println("unselected");
                    hitRangedInfantry.setSelected(false);
                    hitRangedInfantry.colorUnit(Color.WHITE);
                }
                if(hitArtillary != null){
//                    System.out.println("unselected");
                    hitArtillary.setSelected(false);
                    hitArtillary.colorUnit(Color.WHITE);
                }
                hitActor = null;
            }
        }
//        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !isPaused) {
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            camera.unproject(mapSize.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            hitActorTwo = mapStage.hit(mapSize.x,mapSize.y,true);
            if(hitActor != null){
                switch(hitActor.getName()){
                    // TODO check cases for all unit types
                    case "I":
                    {
                        hitInfantry = (Infantry) hitActor;
                        // TODO check cases for all unit types
                        if(hitActorTwo == null){
                            hitInfantry.setFollowing(false);
                            if(isPaused && (int)mapSize.y < 1600/4 && (int)mapSize.y > 0 && (int)mapSize.x <= 1600 && (int)mapSize.x >= 0){
                                hitInfantry.setPosition((int)mapSize.x - 66,(int) mapSize.y - 41);
                            }
                            else if(!isPaused){
                                hitInfantry.moveUnit( (int)mapSize.x,(int) mapSize.y);
                            }
                            break;
                        }
                        switch(hitActorTwo.getName()){
                            case "I":{
                                if(hitInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Infantry)hitActorTwo).getOwner() != hitInfantry.getOwner())){
                                        hitInfantry.setFollowing(false);
                                        hitInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "C":{
                                if(hitInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Cavalry)hitActorTwo).getOwner() != hitInfantry.getOwner())){
                                        hitInfantry.setFollowing(false);
                                        hitInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "R":{
                                if(hitInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((RangedInfantry)hitActorTwo).getOwner() != hitInfantry.getOwner())){
                                        hitInfantry.setFollowing(false);
                                        hitInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "A":{
                                if(hitInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Artillary)hitActorTwo).getOwner() != hitInfantry.getOwner())){
                                        hitInfantry.setFollowing(false);
                                        hitInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    case "C":
                    {
                        hitCavalry = (Cavalry) hitActor;
                        if(hitActorTwo == null){
                            hitCavalry.setFollowing(false);
                            if(isPaused && (int)mapSize.y < 1600/4 && (int)mapSize.y > 0 && (int)mapSize.x <= 1600 && (int)mapSize.x >= 0){
                                hitCavalry.setPosition((int)mapSize.x - 66,(int) mapSize.y - 41);
                            }
                            else if(!isPaused){
                                hitCavalry.moveUnit( (int)mapSize.x,(int) mapSize.y);
                            }
                            break;
                        }
                        switch(hitActorTwo.getName()){
                            case "I":{
                                if(hitCavalry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Infantry)hitActorTwo).getOwner() != hitCavalry.getOwner())){
                                        hitCavalry.setFollowing(false);
                                        hitCavalry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "C":{
                                if(hitCavalry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Cavalry)hitActorTwo).getOwner() != hitCavalry.getOwner())){
                                        hitCavalry.setFollowing(false);
                                        hitCavalry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "R":{
                                if(hitCavalry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((RangedInfantry)hitActorTwo).getOwner() != hitCavalry.getOwner())){
                                        hitCavalry.setFollowing(false);
                                        hitCavalry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "A":{
                                if(hitCavalry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Artillary)hitActorTwo).getOwner() != hitCavalry.getOwner())){
                                        hitCavalry.setFollowing(false);
                                        hitCavalry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    case "R":
                    {
                        hitRangedInfantry = (RangedInfantry) hitActor;
                        if(hitActorTwo == null){
                            hitRangedInfantry.setFollowing(false);
                            if(isPaused && (int)mapSize.y < 1600/4 && (int)mapSize.y > 0 && (int)mapSize.x <= 1600 && (int)mapSize.x >= 0){
                                hitRangedInfantry.setPosition((int)mapSize.x - 66,(int) mapSize.y - 41);
                            }
                            else if(!isPaused){
                                hitRangedInfantry.moveUnit( (int)mapSize.x,(int) mapSize.y);
                            }
                            break;
                        }
                        switch(hitActorTwo.getName()){
                            case "I":{
                                if(hitRangedInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Infantry)hitActorTwo).getOwner() != hitRangedInfantry.getOwner())){
                                        hitRangedInfantry.setFollowing(false);
                                        hitRangedInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "C":{
                                if(hitRangedInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Cavalry)hitActorTwo).getOwner() != hitRangedInfantry.getOwner())){
                                        hitRangedInfantry.setFollowing(false);
                                        hitRangedInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "R":{
                                if(hitRangedInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((RangedInfantry)hitActorTwo).getOwner() != hitRangedInfantry.getOwner())){
                                        hitRangedInfantry.setFollowing(false);
                                        hitRangedInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "A":{
                                if(hitRangedInfantry.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Artillary)hitActorTwo).getOwner() != hitRangedInfantry.getOwner())){
                                        hitRangedInfantry.setFollowing(false);
                                        hitRangedInfantry.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                    case "A":
                    {
                        hitArtillary = (Artillary) hitActor;
                        if(hitActorTwo == null){
                            hitArtillary.setFollowing(false);
                            if(isPaused && (int)mapSize.y < 1600/4 && (int)mapSize.y > 0 && (int)mapSize.x <= 1600 && (int)mapSize.x >= 0){
                                hitArtillary.setPosition((int)mapSize.x - 66,(int) mapSize.y - 41);
                            }
                            else if(!isPaused){
                                hitArtillary.moveUnit( (int)mapSize.x,(int) mapSize.y);
                            }
                            break;
                        }
                        switch(hitActorTwo.getName()){
                            case "I":{
                                if(hitArtillary.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Infantry)hitActorTwo).getOwner() != hitArtillary.getOwner())){
                                        hitArtillary.setFollowing(false);
                                        hitArtillary.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "C":{
                                if(hitArtillary.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Cavalry)hitActorTwo).getOwner() != hitArtillary.getOwner())){
                                        hitArtillary.setFollowing(false);
                                        hitArtillary.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "R":{
                                if(hitArtillary.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((RangedInfantry)hitActorTwo).getOwner() != hitArtillary.getOwner())){
                                        hitArtillary.setFollowing(false);
                                        hitArtillary.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            case "A":{
                                if(hitArtillary.isSelected()){
                                    if(hitActorTwo != null && hitActor != hitActorTwo
                                            && (((Artillary)hitActorTwo).getOwner() != hitArtillary.getOwner())){
                                        hitArtillary.setFollowing(false);
                                        hitArtillary.attackUnit(hitActor,hitActorTwo);
                                    }
                                }
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        camera.zoom = MathUtils.clamp(camera.zoom, .4f, 1.6f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        float width = 1600; // 1600
        float height = 1600; // 1600
        //camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, width - effectiveViewportWidth / 2f);
        //camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, height - effectiveViewportHeight / 2f);
    }
}
