package models.weapons;

import menus.UserSettings;
import models.weapons.projectiles.Bullet;
import models.weapons.projectiles.Projectile;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

public class Cannon extends Uzi {
    private boolean switch_bullet_spawn_side;

    public Cannon(boolean isDrivable) {
        super(isDrivable);

        try {
            weapon_hud_image = new Image("assets/hud/weapons/cannon.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void fire(float spawnX, float spawnY, float rotation_angle) {
        if (canFire()) {
            current_reload_time = 0;    // reset the reload time when a shot is fired
            if (switch_bullet_spawn_side) {
                spawnX += (float) (Math.cos(((rotation_angle) * Math.PI) / 180) * 4.f
                        + -Math.sin(((rotation_angle) * Math.PI) / 180) * -30.f);
                spawnY += (float) (Math.sin(((rotation_angle) * Math.PI) / 180) * 4.f
                        + Math.cos(((rotation_angle) * Math.PI) / 180) * -30.f);
                switch_bullet_spawn_side = false;
            } else {
                spawnX += (float) (Math.cos(((rotation_angle) * Math.PI) / 180) * -4.f
                        + -Math.sin(((rotation_angle) * Math.PI) / 180) * -30.f);
                spawnY += (float) (Math.sin(((rotation_angle) * Math.PI) / 180) * -4.f
                        + Math.cos(((rotation_angle) * Math.PI) / 180) * -30.f);
                switch_bullet_spawn_side = true;
            }
            Vector2f bullet_spawn = new Vector2f(spawnX, spawnY);

            float dirX = (float) Math.sin(rotation_angle * Math.PI / 180);
            float dirY = (float) -Math.cos(rotation_angle * Math.PI / 180);
            Vector2f bullet_dir = new Vector2f(dirX, dirY);

            Projectile bullet = new Bullet(bullet_spawn, bullet_dir, rotation_angle, projectile_texture);
            projectile_list.add(bullet);

            for (int idx = 0; idx < fire_animation.getFrameCount(); ++idx) {
                fire_animation.getImage(idx).setRotation(rotation_angle - 180);
            }
            xPos = bullet_spawn.x;
            yPos = bullet_spawn.y;
            fire_animation.setCurrentFrame(0);
            fire_animation.start();

            fire_sound.play(1.f, UserSettings.SOUND_VOLUME);
        }
    }
}