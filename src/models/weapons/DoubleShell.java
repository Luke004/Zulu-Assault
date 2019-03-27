package models.weapons;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class DoubleShell extends Shell {

    public DoubleShell(){
        try {
            smokeAnimation.addNewInstance();   // add another instance
        } catch (SlickException e) {
            e.printStackTrace();
        }
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
            Weapon.Bullet bullet = new Weapon.Bullet(bullet_spawn, bullet_dir, rotation_angle);
            bullet_list.add(bullet);
            smokeAnimation.play(bullet_spawn.x, bullet_spawn.y, rotation_angle);

            // second bullet (left turret)
            spawnX_1 = (float) (spawnX + Math.cos(((rotation_angle) * Math.PI) / 180) * -19.5f
                    + -Math.sin(((rotation_angle) * Math.PI) / 180) * -30.f);
            spawnY_1 = (float) (spawnY + Math.sin(((rotation_angle) * Math.PI) / 180) * -19.5f
                    + Math.cos(((rotation_angle) * Math.PI) / 180) * -30.f);

            Vector2f bullet_spawn2 = new Vector2f(spawnX_1, spawnY_1);
            Weapon.Bullet bullet2 = new Weapon.Bullet(bullet_spawn2, bullet_dir, rotation_angle);
            bullet_list.add(bullet2);
            smokeAnimation.play(bullet_spawn2.x, bullet_spawn2.y, rotation_angle);
        }
    }
}