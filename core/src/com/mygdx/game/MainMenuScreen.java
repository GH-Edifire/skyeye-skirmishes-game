package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.HashMap;

public class MainMenuScreen implements Screen{
    private final Battle game;
    private int height;
    private int width;
    private OrthographicCamera camera;
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.txt");
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    private Stage stage;
    private ImageButton quitButton;
    private ImageButton playButton;
    private String titleText = "SkyEye Skirmishes";
    private GlyphLayout glyphLayout = new GlyphLayout();

    public MainMenuScreen(final Battle game) {
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

        // center text
        game.font.getData().setScale((float) 1.5);
        glyphLayout.setText(game.font,titleText, Color.WHITE, Gdx.graphics.getWidth(), Align.center,true);

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
                System.out.println("Going to Funds Screen");
                game.font.getData().setScale((float) 1.0);
                game.setScreen(new FundsScreen(game));
            }
        });
        playButton.setPosition(width/2 - playButton.getWidth()/2,5*height/8-playButton.getHeight()/2);
        stage.addActor(playButton);

        // Quit Button
        Texture quitButtonTexture;
        TextureRegion quitButtonTextureRegion;
        TextureRegionDrawable quitButtonDrawable;
        quitButtonTexture = new Texture(Gdx.files.internal("Quit Button.png"));
        quitButtonTextureRegion = new TextureRegion(quitButtonTexture);
        quitButtonDrawable = new TextureRegionDrawable(quitButtonTextureRegion);
        quitButton = new ImageButton(quitButtonDrawable);
        quitButton.setPosition(width/2 - quitButton.getWidth()/2,3*height/8-quitButton.getHeight()/2);
        quitButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                System.exit(0);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                // nothing
            }
        });
        quitButton.setVisible(true);
        stage.addActor(quitButton);



        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render text
        game.batch.begin();
        game.font.draw(game.batch,glyphLayout,0, 7*Gdx.graphics.getHeight()/8 + glyphLayout.height/2);
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
