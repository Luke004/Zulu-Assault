package models.war_attenders.planes;

import logic.TileMapInfo;
import logic.WayPointManager;
import main.SoundManager;
import menus.UserSettings;
import models.CollisionModel;
import models.war_attenders.MovableWarAttender;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import static logic.TileMapInfo.*;

public abstract class Plane extends MovableWarAttender {

    protected boolean landing, starting, hasLanded, hasStarted;
    protected PlaneShadow planeShadow;

    public Plane(Vector2f startPos, boolean isHostile, boolean isDrivable) {
        super(startPos, isHostile, isDrivable);
    }

    @Override
    public void init() {
        WIDTH_HALF = base_image.getWidth() / 2;
        HEIGHT_HALF = base_image.getHeight() / 2;

        planeShadow = new PlaneShadow(new Vector2f(position));

        if (!isDrivable) {
            setMoving(true);
            hasStarted = true;    // bot planes are already flying from the start
        } else {
            // for the player, land the plane so he can get in eventually
            landing = true;
            /*
            // this is the code for if the player is starting the level in the plane -> already flying from start
            hasStarted = true;
            setMoving(true);
             */
        }

        collisionModel = new CollisionModel(position, base_image.getWidth(), base_image.getHeight());

        super.init();
    }

    @Override
    public void rotate(RotateDirection r, int deltaTime) {
        if (landing) return;
        float degree;
        switch (r) {
            case ROTATE_DIRECTION_LEFT:
                degree = -getBaseRotateSpeed() * deltaTime;
                base_image.rotate(degree);
                break;
            case ROTATE_DIRECTION_RIGHT:
                degree = getBaseRotateSpeed() * deltaTime;
                base_image.rotate(degree);
                break;
        }
    }

    @Override
    public void changeAimingDirection(float angle, int deltaTime) {
        float rotation_to_make = WayPointManager.getShortestSignedAngle(base_image.getRotation(), angle);

        if (rotation_to_make > 0) {
            base_image.rotate(getBaseRotateSpeed() * deltaTime);
        } else {
            base_image.rotate(-getBaseRotateSpeed() * deltaTime);
        }
    }

    @Override
    public void onCollision(MovableWarAttender enemy) {
        // a plane doesn't have collision
    }

    @Override
    public void setRotation(float degree) {
        base_image.setRotation(degree);
    }

    @Override
    public float getRotation() {
        return base_image.getRotation();
    }

    @Override
    public void fireWeapon(WeaponType weapon) {
        switch (weapon) {
            case WEAPON_1:
                weapons.get(0).fire(position.x, position.y, base_image.getRotation());
                break;
            case WEAPON_2:
                if (weapons.size() < 2) return;    // does not have a WEAPON_2, so return
                weapons.get(1).fire(position.x, position.y, base_image.getRotation());
                break;
            case MEGA_PULSE:
                if (weapons.size() == 2) {   // does not have a WEAPON_2, MEGA_PULSE it at index [1]
                    weapons.get(1).fire(position.x, position.y, base_image.getRotation());
                } else {    // does have a WEAPON_2, MEGA_PULSE it at index [2]
                    weapons.get(2).fire(position.x, position.y, base_image.getRotation());
                }
                break;
        }
    }

    @Override
    public void update(GameContainer gc, int deltaTime) {
        super.update(gc, deltaTime);

        if (isMoving) {
            fly(deltaTime); // the plane is always flying forward
        }

        if (landing) {
            if (hasLanded) return;
            // calc landing shadow positions
            landPlane(deltaTime);
        } else if (starting) {
            setMoving(true);
            startPlane(deltaTime);
        } else {
            if (isDestroyed) {
                // crash the plane and remove it after it has reached the ground
                landPlane(deltaTime);   // land plane -> to simulate a plane crash -> it looks like landing
                if (hasLanded) level_delete_listener.notifyForWarAttenderDeletion(this);
            } else {
                // normal shadow position
                planeShadow.update();
            }
        }

        // WAY POINTS
        if (waypointManager != null) {
            if (!isEnemyNear) {
                // rotate the plane towards the next vector until it's pointing towards it
                if (waypointManager.wish_angle != (int) getRotation()) {
                    rotate(waypointManager.rotate_direction, deltaTime);
                    waypointManager.adjustAfterRotation(this.position, getRotation());
                }

                if (waypointManager.distToNextVector(this.position) < HEIGHT_HALF * 2) {
                    waypointManager.setupNextWayPoint(this.position, getRotation());
                }
            }
        }
    }

    public void fly(int deltaTime) {
        calculateMovementVector(deltaTime, Direction.FORWARD);
        position.add(dir);
        collisionModel.update(base_image.getRotation());
    }

    public abstract void increaseSpeed(int deltaTime);

    public abstract void decreaseSpeed(int deltaTime);

    protected void movePlaneShadow(int deltaTime, Vector2f target_pos) {
        float angle = WayPointManager.calculateAngleToRotateTo(planeShadow.current_shadow_pos, target_pos);
        float moveX = (float) Math.sin(angle * Math.PI / 180);
        float moveY = (float) -Math.cos(angle * Math.PI / 180);
        moveX *= deltaTime * PlaneShadow.STARTING_LANDING_SPEED;
        moveY *= deltaTime * PlaneShadow.STARTING_LANDING_SPEED;
        Vector2f m_dir = new Vector2f(moveX, moveY);
        planeShadow.current_shadow_pos.add(m_dir);  // add the dir of the shadow movement
        planeShadow.current_shadow_pos.add(dir);    // add the dir of the plane as well
        planeShadow.origin_pos.x = position.x - WIDTH_HALF * 2;
        planeShadow.origin_pos.y = position.y;
    }

    private void startPlane(int deltaTime) {
        // move the plane's shadow away from the plane towards the origin position of the shadow
        movePlaneShadow(deltaTime, planeShadow.origin_pos);

        if (WayPointManager.dist(planeShadow.current_shadow_pos, planeShadow.origin_pos)
                <= PlaneShadow.STARTING_LANDING_SPEED * 4) {
            hasStarted = true;
            starting = false;
        }
    }

    private void landPlane(int deltaTime) {
        Vector2f plane_pos = new Vector2f(position.x - WIDTH_HALF, position.y - HEIGHT_HALF);
        movePlaneShadow(deltaTime, plane_pos);  // move the plane's shadow towards the plane
        if (WayPointManager.dist(planeShadow.current_shadow_pos, plane_pos)
                <= 2.f) {
            hasLanded = true;
            setMoving(false);
        }
    }

    @Override
    public void blockMovement() {
        // a plane doesn't get its movement blocked
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        // draw the plane's shadow
        if (!hasLanded) {
            base_image.drawFlash(planeShadow.current_shadow_pos.x, planeShadow.current_shadow_pos.y,
                    WIDTH_HALF * 2, HEIGHT_HALF * 2, Color.black);
        }
        drawBaseImage();

        // TODO: add destruction animation to plane crash
        /*
        if (isDestroyed) {
            //destructionAnimation.draw(graphics);
        }
         */
    }

    /*
        this is an extra method so helicopter can draw the base image above its shadow wings
     */
    protected void drawBaseImage() {
        if (isInvincible) {
            if (!invincibility_animation_switch) {
                base_image.drawFlash(position.x - WIDTH_HALF, position.y - HEIGHT_HALF);
            } else {
                base_image.draw(position.x - WIDTH_HALF, position.y - HEIGHT_HALF);
            }
        } else {
            base_image.draw(position.x - WIDTH_HALF, position.y - HEIGHT_HALF);
        }
    }

    @Override
    public void showAccessibleAnimation(boolean activate) {
        super.showAccessibleAnimation(activate);
        // stop the plane when player leaves it, start it again when player enters it back
        if (!activate && hasLanded) {
            initStart();    // start the plane
        }
    }

    public void initLanding() {
        if (!canLand()) {
            SoundManager.ERROR_SOUND.play(1.f, UserSettings.SOUND_VOLUME);
            return;
        }
        landing = true;
        starting = false;
        hasStarted = false;
    }

    protected boolean canLand() {
        // check the next six tiles before the plane, if they are collision tiles, the plane can't land

        // the direction the plane is heading towards
        float m_dir_x = (float) Math.sin(getRotation() * Math.PI / 180);
        float m_dir_y = (float) -Math.cos(getRotation() * Math.PI / 180);

        final int NEXT_TILE_OFFSET = 40;
        int tile_idx = 1;
        do {
            Vector2f tile_before_plane = new Vector2f(
                    position.x + m_dir_x * (NEXT_TILE_OFFSET * tile_idx),
                    position.y + m_dir_y * (NEXT_TILE_OFFSET * tile_idx));
            int mapX = (int) (tile_before_plane.x / TILE_WIDTH);
            if (mapX < 0 || mapX >= LEVEL_WIDTH_TILES) return false;  // player wants to land out of map
            int mapY = (int) (tile_before_plane.y / TILE_HEIGHT);
            if (mapY < 0 || mapY >= LEVEL_HEIGHT_TILES) return false;  // player wants to land out of map
            int tileID = map.getTileId(mapX, mapY, LANDSCAPE_TILES_LAYER_IDX);
            if (TileMapInfo.isCollisionTile(tileID)) return false;
        } while (tile_idx++ < 6);    // do it six times (6 tiles before the plane)

        return true;
    }

    private void initStart() {
        landing = false;
        hasLanded = false;
        starting = true;
    }

    public boolean hasLanded() {
        return hasLanded;
    }

    protected class PlaneShadow {
        protected Vector2f current_shadow_pos;
        final static float STARTING_LANDING_SPEED = 0.05f;
        Vector2f origin_pos;  // the original position/ standard drawing position of the plane's shadow

        PlaneShadow(Vector2f current_shadow_pos) {
            this.current_shadow_pos = current_shadow_pos;
            origin_pos = new Vector2f(current_shadow_pos.x - WIDTH_HALF * 2, current_shadow_pos.y);
        }

        void update() {
            planeShadow.current_shadow_pos.x = position.x - WIDTH_HALF * 2;
            planeShadow.current_shadow_pos.y = position.y;
        }

    }
}
