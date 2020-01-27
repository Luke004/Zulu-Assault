package models.war_attenders.windmills;

import logic.WayPointManager;
import models.StaticWarAttender;
import models.animations.smoke.SmokeAnimation;
import models.war_attenders.MovableWarAttender;
import models.weapons.Weapon;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public abstract class Windmill extends StaticWarAttender {
    protected Image turret;
    protected Vector2f turret_position;
    private SmokeAnimation smokeAnimation;
    private final static int SMOKE_ANIMATION_FREQUENCY = 300;  // two per second
    private int smoke_animation_timer;
    private Random random;

    public Windmill(Vector2f startPos, boolean isHostile, Vector2f[] tile_positions) {
        super(startPos, isHostile, tile_positions);
        random = new Random();

        smokeAnimation = new SmokeAnimation(3);

        max_health = 100.f;
        armor = 10.f;
        current_health = max_health;
        scoreValue = 200;

        health_bar_position.x = position.x - 27.5f;
        health_bar_position.y = position.y - 35.f;
    }

    public void init() {
        super.init();
    }

    @Override
    public void update(GameContainer gc, int deltaTime) {
        super.update(gc, deltaTime);
        smokeAnimation.update(deltaTime);
        if (current_health < max_health / 2) {
            smoke_animation_timer += deltaTime;
            if (smoke_animation_timer > SMOKE_ANIMATION_FREQUENCY) {
                smoke_animation_timer = 0;
                int rotation = random.nextInt(360);
                float xVal = (float) (position.x + -Math.sin(((rotation) * Math.PI) / 180) * -30.f);
                float yVal = (float) (position.y + Math.cos(((rotation) * Math.PI) / 180) * -30.f);
                smokeAnimation.play(xVal, yVal, rotation);
            }
        }
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        turret.draw(turret_position.x, turret_position.y);
        smokeAnimation.draw();
    }

    @Override
    public void fireWeapon(MovableWarAttender.WeaponType weaponType) {
        Weapon weapon = null;
        switch (weaponType) {
            case WEAPON_1:
                weapon = getWeapon(MovableWarAttender.WeaponType.WEAPON_1);
                break;
            case WEAPON_2:
                weapon = getWeapon(MovableWarAttender.WeaponType.WEAPON_2);
                break;
            case MEGA_PULSE:
                weapon = getWeapon(MovableWarAttender.WeaponType.MEGA_PULSE);
                break;
        }
        if (weapon == null) return;  // does not have a WEAPON_2, so return
        weapon.fire(position.x, position.y, turret.getRotation());
    }

    @Override
    public void changeAimingDirection(float angle, int deltaTime) {
        float rotation = WayPointManager.getShortestSignedAngle(turret.getRotation(), angle);
        if (rotation < 0) {
            turret.rotate(-turret_rotate_speed * deltaTime);
        } else {
            turret.rotate(turret_rotate_speed * deltaTime);
        }
    }
}
