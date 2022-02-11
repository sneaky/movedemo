package com.sneak.movedemo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private Player player;
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        // get the map we want to render
        map = new TmxMapLoader().load("map.tmx");

        // pass it to the renderer
        renderer = new OrthogonalTiledMapRenderer(map);

        // initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 576); // magic numbers for testing only

        // initialize player with provided spite
        player = new Player(new Sprite(new Texture("rogueSprite-simple.png")),
                (TiledMapTileLayer) map.getLayers().get(0) );

        // initialize player location
        player.setPosition(3 * player.getCollisionLayer().getTileWidth(),
                4 * player.getCollisionLayer().getTileHeight());

        // all user input is directed to the player object
        Gdx.input.setInputProcessor(player);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // clear screen
        ScreenUtils.clear(0, 0, 0, 1);

        // tell the renderer to render what the camera sees
        renderer.setView(camera);
        renderer.render();

        // get the sprite batch
        renderer.getBatch().begin();
        // draw the player sprite
        player.draw(renderer.getBatch());
        // close the sprite batch
        renderer.getBatch().end();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        dispose();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
