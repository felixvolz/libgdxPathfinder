package com.felix.component;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.felix.MyActor;
import com.felix.NodeFactory;

/*
//TODO make MyActor target.path a public method, maybe make path finding smoother (bit jerky)
//TODO zoom
 */

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


    float lerp = 0.5f; //the higher towards 1.0, the more the camera lags/floats behind
    public void update(float deltaTime) {

        //default to players current location THEN try to trace future path
        GridPoint2 targetCell  = target.cellPos;
        int i = Math.min(target.path.getCount(),5); //dont want to look too far ahead as Camera may not head in the direction palyer is currently walking
        if(target.path.getCount() > 1) {
              targetCell = target.path.get(Math.max(--i, 1)).getPos();
        }

        position.y += (targetCell.y *  NodeFactory.TileMapDetails.tilewidth- position.y) * (deltaTime / lerp);
        position.x += (targetCell.x *  NodeFactory.TileMapDetails.tilewidth - position.x)  * (deltaTime / lerp);

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