package models.war_attenders.windmills;

import models.war_attenders.MovableWarAttender;
import models.war_attenders.WarAttender;
import models.weapons.Weapon;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public abstract class Windmill extends WarAttender {
    Image turret;
    float turret_rotate_speed;
    private int key;

    public Windmill(Vector2f startPos, boolean isHostile, int key) {
        super(startPos, isHostile);
        this.key = key;

        max_health = 100;
        current_health = max_health;
        turret_rotate_speed = 0.2f;

        try {
            turret = new Image("assets/windmills/GreenWindmill_turret.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        super.init();
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
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
        weapon.fire(position.x + 20, position.y + 20, turret.getRotation());
    }

    @Override
    public void rotateTowardsPlayer(float angle) {
        turret.setRotation(angle);
    }

    public int getKey(){
        return key;
    }
}
