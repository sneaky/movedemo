package com.sneak.movedemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;



public class Player extends Sprite implements InputProcessor {
    private Vector2 velocity = new Vector2();
    private float speed = 120f;
    private TiledMapTileLayer collisionLayer;


    public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    private void update(float delta) {
        float oldX = getX(), oldY = getY();

        float newX = (float) (getX() + velocity.x * delta);
        float newY = (float) (getY() + velocity.y * delta);

        boolean xCollision = false, yCollision = false;
    
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();

        setX(newX);
        setY(newY);

        /* check for x collision */
        if (velocity.x > 0) { // moving right
            // right top
            xCollision = isCellBlocked(getX() + getWidth(), getY() + getHeight());
            // right mid
            if (!xCollision) {
                xCollision = isCellBlocked(getX() + getWidth(), getY() + getHeight() / 2);
            }
            // right bot
            if (!xCollision) {
                xCollision = isCellBlocked(getX() + getWidth(), getY());
            }
        } else if (velocity.x < 0) { // moving left
            // left top
            xCollision = isCellBlocked(getX(), getY() + getHeight());
            // left mid
            if (!xCollision) {
                xCollision = isCellBlocked(getX(), (getY() + getHeight() / 2));
            }
            // left bot
            if (!xCollision) {
                xCollision = isCellBlocked(getX(), getY());
            }
        }

        // handle x collision
        if (xCollision) {
            setX(oldX);
            velocity.x = 0;
        }

        /* check for y collision */
        if (velocity.y > 0) { // moving up
            //top left
            yCollision = isCellBlocked(getX(), getY() + getHeight());
            //top mid
            if (!yCollision) {
                yCollision = isCellBlocked(getX() + getWidth() / 2, getY() + getHeight());
            }
            //top right
            if (!yCollision) {
                yCollision = isCellBlocked(getX() + getWidth(), getY() + getHeight());
            }
        } else if (velocity.y < 0) {// moving down ??? reversed ??? we'll see...
            // bot left
            yCollision = isCellBlocked(getX(), getY());
            // bot mid
            if (!yCollision) {
                yCollision = isCellBlocked(getX() + getWidth() / 2, getY());
            }
            // bot right
            if (!yCollision) {
                yCollision = isCellBlocked(getX() + getWidth(), getY() + getHeight());
            }
        }

        // handle y collision
        if (yCollision) {
            setY(oldY);
            velocity.y = 0;
        }
    }

    private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell( (int) (x / collisionLayer.getTileWidth()),
                                        (int) y / collisionLayer.getTileHeight() );
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
    }

    /*
    TODO: clean up the update function a bit more
    private boolean collidesTop() {
        boolean collides = false;
        
        for (float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2) {
            if (collides = isCellBlocked(getX() + step, getY()));
        }
    }
    */


    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                velocity.y = speed;
                break;
            case Input.Keys.S:
                velocity.y = -speed;
                break;
            case Input.Keys.A:
                velocity.x = -speed;
                break;
            case Input.Keys.D:
                velocity.x = speed;
                break;
        }
        return true;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.S:
                velocity.y = 0;
                break;
            case Input.Keys.A:
            case Input.Keys.D:
                velocity.x = 0;
                break;
        }
        return true;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            // [ size of tile-layer in # of tiles ] * [ size of a tile in pixels ]
            float height = collisionLayer.getHeight() * collisionLayer.getTileHeight();
            // move to mouse click
            setX(screenX);
            setY(height - screenY);
            // TODO: make sure new loc is valid (i.e. not in a colliding block)
        }
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amountX the horizontal scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @param amountY the vertical scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }
}
