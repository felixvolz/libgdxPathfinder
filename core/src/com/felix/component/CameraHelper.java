package com.felix.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.felix.MyActor;
import com.felix.NodeFactory;

public class CameraHelper {

    private static final String TAG = CameraHelper.class.getName();
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;
    private Vector2 position;
    private float zoom;
    private MyActor target;
    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
    }


    float lerp = 0.1f;
    public void update(float deltaTime) {
        if (!hasTarget()) return;
        if(null == target.goal) return;
Vector2 coords = target.getStage().stageToScreenCoordinates(new Vector2(target.goal.x * NodeFactory.TileMapDetails.tilewidth , target.goal.y * NodeFactory.TileMapDetails.tilewidth));


       // if(coords.y - 200 < 0 || coords.y + 200 > Gdx.graphics.getHeight())
               position.y += (target.goal.y *  NodeFactory.TileMapDetails.tilewidth- position.y) * (deltaTime / lerp);

       // if(coords.x - 200 < 0 || coords.x + 200 > Gdx.graphics.getWidth())
            position.x += (target.goal.x *  NodeFactory.TileMapDetails.tilewidth - position.x)  * (deltaTime / lerp);

    }


    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public float getZoom() {
        return zoom;
    }

    public void setTarget(MyActor target) {
        this.target = target;
    }

    public MyActor getTarget() {
        return target;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasTarget(Sprite target) {
        return hasTarget() && this.target.equals(target);

    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

}