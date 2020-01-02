package models.weapons;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Plasma extends Weapon {
    public Plasma(boolean isDrivable) {
        super();
        try {
            projectile_texture = new Image("assets/bullets/plasma.png").getTexture();
        } catch (SlickException e) {
            e.printStackTrace();
        }
        // individual Plasma specs
        bullet_damage = 100;
        shot_reload_time = 330;
        if (!isDrivable) shot_reload_time *= 5;
    }
}
