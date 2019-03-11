package models.war_attenders.tanks;

import models.war_attenders.WarAttender;
import models.war_attenders.soldiers.Soldier;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import java.util.Iterator;

public abstract class Tank extends WarAttender {
    Image turret;
    float turret_rotate_speed;

    public Tank(Vector2f startPos, boolean isHostile) {
        super(startPos, isHostile);
    }

    @Override
    public void draw(Graphics graphics) {
        base_image.draw(position.x - base_image.getWidth() / 2, position.y - base_image.getHeight() / 2);
        turret.draw(position.x - turret.getWidth() / 2, position.y - turret.getHeight() / 2);
        if (show_accessible_animation) {
            accessible_animation.draw(position.x - base_image.getWidth() / 4, position.y - base_image.getHeight() + 17);
        }
        collisionModel.draw(graphics);

        for (Bullet b : bullet_list) {
            //bullet_image.draw(b.bullet_pos.x, b.bullet_pos.y);
            b.draw(graphics);
        }
    }

    @Override
    public void update(GameContainer gc, int deltaTime) {
        if (show_accessible_animation) {
            accessible_animation.update(deltaTime);
        }
        collisionModel.update(base_image.getRotation());
        //System.out.println(collisionModel.update(base_image.getRotation())[1].x);

        if (current_reload_time < shot_reload_time) {
            current_reload_time += deltaTime;
        }

        Iterator<Bullet> iter = bullet_list.iterator();
        while (iter.hasNext()) {
            Bullet b = iter.next();
            b.update(deltaTime);
            if (b.bullet_lifetime > MAX_BULLET_LIFETIME) {
                iter.remove();
            }
        }
    }

    public Vector2f calculateSoldierSpawnPosition() {
        // set player 10 pixels behind the tank
        final float DISTANCE = 10;
        final float SPAWN_X = 0;
        final float SPAWN_Y = base_image.getHeight() / 2 + DISTANCE;

        float xVal = (float) (Math.cos(((base_image.getRotation()) * Math.PI) / 180) * SPAWN_X
                + -Math.sin(((base_image.getRotation()) * Math.PI) / 180) * SPAWN_Y);
        float yVal = (float) (Math.sin(((base_image.getRotation()) * Math.PI) / 180) * SPAWN_X
                + Math.cos(((base_image.getRotation()) * Math.PI) / 180) * SPAWN_Y);
        return new Vector2f(xVal + position.x, yVal + position.y);
    }

    public void rotateTurret(RotateDirection r, int deltaTime) {
        switch (r) {
            case ROTATE_DIRECTION_LEFT:
                turret.rotate(-turret_rotate_speed * deltaTime);
                break;
            case ROTATE_DIRECTION_RIGHT:
                turret.rotate(turret_rotate_speed * deltaTime);
                break;
        }
    }

    @Override
    public float getRotation() {
        return base_image.getRotation();
    }

    @Override
    public void rotate(RotateDirection r, int deltaTime) {
        float degree;
        switch (r) {
            case ROTATE_DIRECTION_LEFT:
                degree = -rotate_speed * deltaTime;
                base_image.rotate(degree);
                turret.rotate(degree);
                break;
            case ROTATE_DIRECTION_RIGHT:
                degree = rotate_speed * deltaTime;
                base_image.rotate(degree);
                turret.rotate(degree);
                break;
        }
    }

    public void onCollision(WarAttender enemy) {
        if (enemy instanceof Tank) {  // enemy is a tank
            current_speed = 0.f;    // stop movement as long as there's collision
        } else if (enemy instanceof Soldier) {   // enemy is a soldier (bad for him)
            // kill soldier
        }
        // plane instanceof is not needed, nothing happens there
    }

    /*
    let the tank bounce a few meters back from its current position
     */
    private void bounceBack() {
        // TODO
    }

    /*
    The standard method a tank uses to shoot. Should be overwritten if a tank shoots another way.
     */
    @Override
    public void shoot() {
        if (canShoot()) {
            current_reload_time = 0;    // reset the reload time when a shot is fired
            float rotation_angle = turret.getRotation();
            float spawnX = position.x;
            float spawnY = position.y;
            spawnX += -Math.sin(((rotation_angle) * Math.PI) / 180) * -30.f;
            spawnY += Math.cos(((rotation_angle) * Math.PI) / 180) * -30.f;
            Vector2f bullet_spawn = new Vector2f(spawnX, spawnY);

            float xVal = (float) Math.sin(rotation_angle * Math.PI / 180);
            float yVal = (float) -Math.cos(rotation_angle * Math.PI / 180);
            Vector2f bullet_dir = new Vector2f(xVal, yVal);

            Bullet bullet = new Bullet(bullet_spawn, bullet_dir, rotation_angle);
            bullet_list.add(bullet);
        }
    }

    boolean canShoot() {
        return current_reload_time >= shot_reload_time;
    }
}
