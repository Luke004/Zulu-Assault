package models.weapons;

import logic.level_listeners.GroundTileDamageListener;
import menus.UserSettings;
import models.animations.explosion.BigExplosionAnimation;
import models.weapons.projectiles.AirRocket;
import models.weapons.projectiles.Projectile;
import models.weapons.projectiles.iGroundTileDamageWeapon;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class AGM extends RocketLauncher implements iGroundTileDamageWeapon {

    private static BigExplosionAnimation bigExplosionAnimation;
    public GroundTileDamageListener groundTileDamageListener;

    public AGM(boolean isDrivable) {
        super(isDrivable);

        try {
            weapon_hud_image = new Image("assets/hud/weapons/agm.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }

        BUFFER_SIZE *= 2;   // double the buffer size, since this is a double rocket launcher
        bigExplosionAnimation = new BigExplosionAnimation(100);
    }

    @Override
    public void fire(float spawnX, float spawnY, float rotation_angle) {
        if (canFire()) {
            current_reload_time = 0;    // reset the reload time when a shot is fired

            float xVal = (float) Math.sin(rotation_angle * Math.PI / 180);
            float yVal = (float) -Math.cos(rotation_angle * Math.PI / 180);
            Vector2f bullet_dir = new Vector2f(xVal, yVal);

            // first bullet (right turret)
            float spawnX_1 = (float) (spawnX + Math.cos(((rotation_angle) * Math.PI) / 180) * 19.5f
                    + -Math.sin(((rotation_angle) * Math.PI) / 180) * -30.f);
            float spawnY_1 = (float) (spawnY + Math.sin(((rotation_angle) * Math.PI) / 180) * 19.5f
                    + Math.cos(((rotation_angle) * Math.PI) / 180) * -30.f);
            Vector2f bullet_spawn = new Vector2f(spawnX_1, spawnY_1);

            Animation fresh_rocket = getNextFreshRocket();

            for (int idx = 0; idx < fresh_rocket.getFrameCount(); ++idx) {
                fresh_rocket.getImage(idx).setRotation(rotation_angle);
            }
            fresh_rocket.setPingPong(true);
            fresh_rocket.setLooping(false);
            fresh_rocket.setCurrentFrame(0);
            fresh_rocket.start();

            Projectile rocket1 = new AirRocket(bullet_spawn, bullet_dir, rotation_angle, projectile_texture, fresh_rocket);
            projectile_list.add(rocket1);

            // second bullet (left turret)
            spawnX_1 = (float) (spawnX + Math.cos(((rotation_angle) * Math.PI) / 180) * -19.5f
                    + -Math.sin(((rotation_angle) * Math.PI) / 180) * -30.f);
            spawnY_1 = (float) (spawnY + Math.sin(((rotation_angle) * Math.PI) / 180) * -19.5f
                    + Math.cos(((rotation_angle) * Math.PI) / 180) * -30.f);

            Vector2f bullet_spawn2 = new Vector2f(spawnX_1, spawnY_1);
            Animation fresh_rocket2 = getNextFreshRocket();

            for (int idx = 0; idx < fresh_rocket.getFrameCount(); ++idx) {
                fresh_rocket2.getImage(idx).setRotation(rotation_angle);
            }
            fresh_rocket2.setPingPong(true);
            fresh_rocket2.setLooping(false);
            fresh_rocket2.setCurrentFrame(0);
            fresh_rocket2.start();

            Projectile airRocket2 = new AirRocket(bullet_spawn2, bullet_dir, rotation_angle, projectile_texture, fresh_rocket2);
            projectile_list.add(airRocket2);

            fire_sound.play(1.f, UserSettings.SOUND_VOLUME);
        }
    }

    @Override
    public void update(int deltaTime) {
        super.update(deltaTime);
        bigExplosionAnimation.update(deltaTime);
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        bigExplosionAnimation.draw();
    }

    public static void playDestructionAnimation(float xPos, float yPos) {
        bigExplosionAnimation.playTenTimes(xPos, yPos, 90);
    }

    @Override
    public void addListener(GroundTileDamageListener groundTileDamageListener) {
        this.groundTileDamageListener = groundTileDamageListener;
    }

    @Override
    public GroundTileDamageListener getListener() {
        return groundTileDamageListener;
    }
}