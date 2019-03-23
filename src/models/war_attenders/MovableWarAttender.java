package models.war_attenders;

import models.CollisionModel;
import models.weapons.Weapon;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import player.Player;

public abstract class MovableWarAttender extends WarAttender {
    // model related
    public Image base_image;
    public CollisionModel collisionModel;

    // math related
    public Vector2f dir;

    // specs related
    public static final float DAMAGE_TO_DESTRUCTIBLE_TILE = 5.f;
    public float max_speed, current_speed;
    public float rotate_speed;

    // booleans
    public boolean isMoving;

    // invincibility item related
    public boolean isInvincible, invincibility_animation_switch;
    private int invincibility_lifetime;
    private final int INVINCIBILITY_TIME = 10000;   // 10 sec
    public int invincible_animation_time_switch = 1000;    // once every sec switch to white color while invincible
    public int invincible_animation_current_time;


    public MovableWarAttender(Vector2f startPos, boolean isHostile) {
        super(startPos, isHostile);
        dir = new Vector2f(0, 0);
    }


    public void update(GameContainer gc, int deltaTime) {
        super.update(gc, deltaTime);

        // COLLISION RELATED STUFF
        collisionModel.update(base_image.getRotation());

        // ITEMS RELATED STUFF

        // INVINCIBILITY
        if (isInvincible) {
            // invincibility logic itself
            invincibility_lifetime += deltaTime;
            if (invincibility_lifetime > INVINCIBILITY_TIME) {
                isInvincible = false;
                invincibility_lifetime = 0;
            }

            // invincibility animation related
            invincible_animation_current_time += deltaTime;
            if (invincible_animation_current_time >= invincible_animation_time_switch) {
                if (invincibility_animation_switch) invincibility_animation_switch = false;
                else invincibility_animation_switch = true;
                invincible_animation_current_time = 0;
            }
        }
    }

    @Override
    public void draw(Graphics graphics) {
        health_bar_image.draw(position.x - health_bar_image.getWidth() / 2 - 7.5f, position.y - base_image.getHeight() / 2 - 15);

        // draw health bar damage using a black rectangle
        graphics.setColor(Color.black);
        if (current_health > 0) {
            graphics.fillRect(position.x + 14, position.y - base_image.getHeight() / 2 - 14, -(29 - (((float) current_health) / (((float) max_health) / 29))), 5);
        } else {    // destroyed (rectangle is full black size)
            graphics.fillRect(position.x + 14, position.y - base_image.getHeight() / 2 - 14, -29, 5);
        }

        // BULLETS
        for (Weapon weapon : weapons) {
            weapon.draw(graphics);
        }

        // COLLISION RELATED STUFF
        collisionModel.draw(graphics);
    }

    public void calculateMovementVector(int deltaTime, Direction direction) {
        dir.x = (float) Math.sin(getRotation() * Math.PI / 180);
        dir.y = (float) -Math.cos(getRotation() * Math.PI / 180);
        if (direction == Direction.BACKWARDS) {
            dir.x *= -1;
            dir.y *= -1;
        }
        dir.x *= deltaTime * current_speed;
        dir.y *= deltaTime * current_speed;
    }

    public CollisionModel getCollisionModel() {
        return collisionModel;
    }

    public boolean isHostile() {
        return isHostile;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setMoving(boolean b) {
        isMoving = b;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public abstract void onCollision(MovableWarAttender enemy);

    public abstract void blockMovement();

    public abstract float getRotation();

    public abstract void rotate(RotateDirection r, int deltaTime);



    public void activateItem(Player.Item item) {
        switch (item) {
            case INVINCIBLE:
                isInvincible = true;
                break;
            case EMP:
                // TODO: destroy all planes here
                break;
            case MEGA_PULSE:
                fireWeapon(WeaponType.MEGA_PULSE);
                break;
            case EXPAND:
                // TODO: reflect enemy bullets here
                break;
        }
    }

    public enum RotateDirection {
        ROTATE_DIRECTION_LEFT, ROTATE_DIRECTION_RIGHT
    }

    public enum Direction {
        FORWARD, BACKWARDS
    }
}
