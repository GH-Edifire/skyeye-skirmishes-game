package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.HashMap;

public class FundsScreen implements Screen, Input.TextInputListener {
    private final Battle game;
    private int height;
    private int width;
    private OrthographicCamera camera;
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.txt");
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    private Stage stage;
    private ImageButton smallButton;
    private ImageButton mediumButton;
    private ImageButton largeButton;
    private ImageButton customButton;
    private ImageButton playerPlusButton;
    private ImageButton playerMinusButton;
    private ImageButton enemyPlusButton;
    private ImageButton enemyMinusButton;
    private ImageButton playButton;
    private ImageButton backButton;
    private int selectedOption = 0;
    private String titleText = "Funds";
    private GlyphLayout glyphLayout = new GlyphLayout();
    private final int smallLimit = 500;
    private final int mediumLimit = 1000;
    private final int largeLimit = 1500;


    public FundsScreen(final Battle game) {
        this.game = game;
        height = 720;
        width = 1280;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void show() {
        addSprites();
        stage = new Stage(new ScreenViewport());

        glyphLayout.setText(game.font,titleText, Color.WHITE, Gdx.graphics.getWidth(), Align.center,true);

        // Small funds Button
        Texture smallButtonTexture;
        TextureRegion smallButtonTextureRegion;
        TextureRegionDrawable smallTextureDrawable;
        smallButtonTexture = new Texture(Gdx.files.internal("Small Button.png"));
        smallButtonTextureRegion = new TextureRegion(smallButtonTexture);
        smallTextureDrawable = new TextureRegionDrawable(smallButtonTextureRegion);
        smallButton = new ImageButton(smallTextureDrawable);
        smallButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Small Funds Button");
                selectedOption = 1;
                game.playerFunds = smallLimit;
                game.enemyFunds = smallLimit;
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Small Funds Button");
            }
        });
        smallButton.setPosition(2*width/10-smallButton.getWidth()/2,7*height/10-smallButton.getHeight()/2);
        stage.addActor(smallButton);

        // Medium funds Button
        Texture mediumButtonTexture;
        TextureRegion mediumButtonTextureRegion;
        TextureRegionDrawable mediumTextureDrawable;
        mediumButtonTexture = new Texture(Gdx.files.internal("Medium Button.png"));
        mediumButtonTextureRegion = new TextureRegion(mediumButtonTexture);
        mediumTextureDrawable = new TextureRegionDrawable(mediumButtonTextureRegion);
        mediumButton = new ImageButton(mediumTextureDrawable);
        mediumButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Medium Funds Button");
                selectedOption = 2;
                game.playerFunds = mediumLimit;
                game.enemyFunds = mediumLimit;
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Medium Funds Button");
            }
        });
        mediumButton.setPosition(2*width/10-mediumButton.getWidth()/2,5*height/10-mediumButton.getHeight()/2);
        stage.addActor(mediumButton);

        // Large funds Button
        Texture largeButtonTexture;
        TextureRegion largeButtonTextureRegion;
        TextureRegionDrawable largeTextureDrawable;
        largeButtonTexture = new Texture(Gdx.files.internal("Large Button.png"));
        largeButtonTextureRegion = new TextureRegion(largeButtonTexture);
        largeTextureDrawable = new TextureRegionDrawable(largeButtonTextureRegion);
        largeButton = new ImageButton(largeTextureDrawable);
        largeButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Large Funds Button");
                selectedOption = 3;
                game.playerFunds = largeLimit;
                game.enemyFunds = largeLimit;
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Large Funds Button");
            }
        });
        largeButton.setPosition(6*width/10-largeButton.getWidth()/2,7*height/10-largeButton.getHeight()/2);
        stage.addActor(largeButton);

        // Custom funds Button
        Texture customButtonTexture;
        TextureRegion customButtonTextureRegion;
        TextureRegionDrawable customTextureDrawable;
        customButtonTexture = new Texture(Gdx.files.internal("Custom Button.png"));
        customButtonTextureRegion = new TextureRegion(customButtonTexture);
        customTextureDrawable = new TextureRegionDrawable(customButtonTextureRegion);
        customButton = new ImageButton(customTextureDrawable);
        customButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked Custom Funds Button");
                selectedOption = 4;
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release Custom Funds Button");
            }
        });
        customButton.setPosition(6*width/10-customButton.getWidth()/2,5*height/10-customButton.getHeight()/2);
        stage.addActor(customButton);

        // player plus Button
        Texture ppButtonTexture;
        TextureRegion ppButtonTextureRegion;
        TextureRegionDrawable ppTextureDrawable;
        ppButtonTexture = new Texture(Gdx.files.internal("Plus Button.png"));
        ppButtonTextureRegion = new TextureRegion(ppButtonTexture);
        ppTextureDrawable = new TextureRegionDrawable(ppButtonTextureRegion);
        playerPlusButton = new ImageButton(ppTextureDrawable);
        playerPlusButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked plus Button");
                if(game.playerFunds < 3000 && selectedOption == 4){
                    game.playerFunds += 100;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release plus Button");
            }
        });
        playerPlusButton.setPosition(17*width/20-playerPlusButton.getWidth()/2,10*height/20-playerPlusButton.getHeight()/2);
        stage.addActor(playerPlusButton);

        // player minus Button
        Texture pmButtonTexture;
        TextureRegion pmButtonTextureRegion;
        TextureRegionDrawable pmTextureDrawable;
        pmButtonTexture = new Texture(Gdx.files.internal("Minus Button.png"));
        pmButtonTextureRegion = new TextureRegion(pmButtonTexture);
        pmTextureDrawable = new TextureRegionDrawable(pmButtonTextureRegion);
        playerMinusButton = new ImageButton(pmTextureDrawable);
        playerMinusButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked minus Button");
                if(game.playerFunds > 0 && selectedOption == 4){
                    game.playerFunds -= 100;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release minus Button");
            }
        });
        playerMinusButton.setPosition(15*width/20-playerMinusButton.getWidth()/2,10*height/20-playerMinusButton.getHeight()/2);
        stage.addActor(playerMinusButton);

        // enemy plus Button
        Texture epButtonTexture;
        TextureRegion epButtonTextureRegion;
        TextureRegionDrawable epTextureDrawable;
        epButtonTexture = new Texture(Gdx.files.internal("Plus Button.png"));
        epButtonTextureRegion = new TextureRegion(epButtonTexture);
        epTextureDrawable = new TextureRegionDrawable(epButtonTextureRegion);
        enemyPlusButton = new ImageButton(epTextureDrawable);
        enemyPlusButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked plus Button");
                if(game.enemyFunds < 3000 && selectedOption == 4){
                    game.enemyFunds += 100;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release plus Button");
            }
        });
        enemyPlusButton.setPosition(17*width/20-enemyPlusButton.getWidth()/2,7*height/20-enemyPlusButton.getHeight()/2);
        stage.addActor(enemyPlusButton);

        // enemy minus Button
        Texture emButtonTexture;
        TextureRegion emButtonTextureRegion;
        TextureRegionDrawable emTextureDrawable;
        emButtonTexture = new Texture(Gdx.files.internal("Minus Button.png"));
        emButtonTextureRegion = new TextureRegion(emButtonTexture);
        emTextureDrawable = new TextureRegionDrawable(emButtonTextureRegion);
        enemyMinusButton = new ImageButton(emTextureDrawable);
        enemyMinusButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Clicked minus Button");
                if(game.enemyFunds > 0 && selectedOption == 4){
                    game.enemyFunds -= 100;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Release minus Button");
            }
        });
        enemyMinusButton.setPosition(15*width/20-enemyMinusButton.getWidth()/2,7*height/20-enemyMinusButton.getHeight()/2);
        stage.addActor(enemyMinusButton);

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
                if(selectedOption > 0) {
                    System.out.println("Going to Play Screen");
                    game.savedEnemyFunds = game.enemyFunds;
                    game.savedPlayerFunds = game.enemyFunds;
                    game.setScreen(new BuyScreen(game));
                }
            }
        });
        playButton.setPosition(width-playButton.getWidth()-width/20,height/20);
        stage.addActor(playButton);

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
                game.setScreen(new MainMenuScreen(game));
            }
        });
        backButton.setPosition(width/20,height/20);
        stage.addActor(backButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch(selectedOption){
            // Small button selected
            case 1:
            {
                smallButton.getImage().setColor(Color.GOLD);
                mediumButton.getImage().setColor(Color.WHITE);
                largeButton.getImage().setColor(Color.WHITE);
                customButton.getImage().setColor(Color.WHITE);
                break;
            }
            // Medium button selected
            case 2:
            {
                smallButton.getImage().setColor(Color.WHITE);
                mediumButton.getImage().setColor(Color.GOLD);
                largeButton.getImage().setColor(Color.WHITE);
                customButton.getImage().setColor(Color.WHITE);
                break;
            }
            // Large button selected
            case 3:
            {
                smallButton.getImage().setColor(Color.WHITE);
                mediumButton.getImage().setColor(Color.WHITE);
                largeButton.getImage().setColor(Color.GOLD);
                customButton.getImage().setColor(Color.WHITE);
                break;
            }
            // Custom button selected
            case 4:
            {
                smallButton.getImage().setColor(Color.WHITE);
                mediumButton.getImage().setColor(Color.WHITE);
                largeButton.getImage().setColor(Color.WHITE);
                customButton.getImage().setColor(Color.GOLD);
                break;
            }
            default:
            {
                break;
            }
        }

        // Render text
        game.batch.begin();
        game.font.draw(game.batch,glyphLayout,0, 7*Gdx.graphics.getHeight()/8 + glyphLayout.height/2);
        game.font.draw(game.batch,""+smallLimit,3*width/10, 7*height/10);
        game.font.draw(game.batch,""+mediumLimit,3*width/10, 5*height/10);
        game.font.draw(game.batch,""+largeLimit,7*width/10, 7*height/10);
        game.font.draw(game.batch,"Player: " + game.playerFunds,7*width/10, 12*height/20);
        game.font.draw(game.batch,"Enemy: " + game.enemyFunds,7*width/10, 9*height/20);
        game.font.draw(game.batch,"Max 3000",21*width/40, 8*height/20);
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

    // for text input
    @Override
    public void input(String text) {

    }

    @Override
    public void canceled() {

    }
}
