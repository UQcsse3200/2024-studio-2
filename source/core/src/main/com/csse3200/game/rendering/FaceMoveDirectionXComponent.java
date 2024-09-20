package com.csse3200.game.rendering;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;

public class FaceMoveDirectionXComponent extends Component {
    private static final float EPS = 1e-3f;
    private Vector2 lastPos;

    @Override
    public void create() {
        lastPos = entity.getPosition();
    }
    @Override
    public void update() {
        Vector2 currPos = entity.getPosition();
        if (lastPos.x - currPos.x > EPS) {
            entity.getEvents().trigger("faceLeft");
        } else if (currPos.x - lastPos.x > EPS) {
            entity.getEvents().trigger("faceRight");
        }
        lastPos = currPos;
    }
}
