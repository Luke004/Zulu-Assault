package models.animations.explosion;

import models.animations.AbstractVolatileAnimation;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;

public class BigExplosionAnimation extends AbstractVolatileAnimation {

    private static Texture big_explosion_animation_texture;

    public BigExplosionAnimation(final int BUFFER_SIZE) {
        super(BUFFER_SIZE);
    }

    @Override
    public void initTexture() throws SlickException {
        if (big_explosion_animation_texture == null)
            big_explosion_animation_texture = new Image("assets/animations/big_explosion.png").getTexture();
    }

    @Override
    public void addNewInstance() {
        Image smoke_animation_image = new Image(big_explosion_animation_texture);
        Animation explosion_animation = new Animation(false);
        int IMAGE_COUNT = 4;
        int x = 0;

        for (int idx = 0; idx < IMAGE_COUNT; ++idx) {
            explosion_animation.addFrame(smoke_animation_image.getSubImage(x, 0, 40, 50), 160);
            x += 40;
        }
        explosion_animation.setLooping(false);
        buffered_instances.add(new MovableAnimationInstance(explosion_animation));
    }

    public void playTenTimes(float xPos, float yPos, float rotation) {
        final int PLAY_TIMES = 10;
        int counter = 0;
        do {
            super.play(xPos, yPos, rotation);
            counter++;
        } while (counter < PLAY_TIMES);
    }
}
