package game.menu.screens;

import game.audio.MenuSounds;
import game.graphics.fonts.FontManager;
import settings.SettingStorage;
import game.menu.Menu;
import game.menu.elements.Arrow;
import game.menu.elements.Slider;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import settings.UserSettings;

import static game.menu.Menu.*;
import static game.menu.screens.MainScreen.MENU_OPTION_HEIGHT;
import static settings.UserSettings.VOLUME_MAX_LEVEL;


public class OptionsScreen extends AbstractMenuScreen {

    private Arrow arrow;
    private Slider sound_volume_slider, music_volume_slider;
    private static final TrueTypeFont menu_drawer;
    private int back_btn_width, back_btn_height;
    private Vector2f back_btn_position;

    static {
        menu_drawer = FontManager.getStencilBigFont();
    }

    public OptionsScreen(BasicGameState gameState, GameContainer gameContainer) {
        super(gameState);
        back_btn_width = menu_drawer.getWidth("BACK");
        back_btn_height = MENU_OPTION_HEIGHT;
        try {
            back_btn_position = new Vector2f(
                    gameContainer.getWidth() / 2.f - back_btn_width / 2.f,
                    gameContainer.getHeight() / 2.f);
            Texture slider_texture = new Image("assets/menus/slider.png").getTexture();
            Texture slider_value_texture = new Image("assets/menus/slider_value.png").getTexture();
            sound_volume_slider = new Slider(slider_texture, slider_value_texture, new Vector2f(
                    back_btn_position.x - 50,
                    back_btn_position.y + back_btn_height
            ), "Sound Volume", VOLUME_MAX_LEVEL);
            sound_volume_slider.setValue(UserSettings.soundVolumeLevel);

            music_volume_slider = new Slider(slider_texture, slider_value_texture, new Vector2f(
                    back_btn_position.x - 50,
                    back_btn_position.y + back_btn_height * 2
            ), "Music Volume", VOLUME_MAX_LEVEL);
            music_volume_slider.setValue(UserSettings.musicVolumeLevel);

            arrow = new Arrow(gameContainer, 3, (int) back_btn_position.y);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer gameContainer) {
        super.render(gameContainer);
        menu_drawer.drawString(gameContainer.getWidth() / 2.f - back_btn_width / 2.f,
                gameContainer.getHeight() / 2.f,
                "BACK", Color.lightGray);
        sound_volume_slider.draw();
        music_volume_slider.draw();
        arrow.draw();
        Menu.drawInfoStrings(gameContainer);
    }

    @Override
    public void handleKeyInput(GameContainer gameContainer, StateBasedGame stateBasedGame) {
        if (gameContainer.getInput().isKeyPressed(Input.KEY_UP)) {
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            arrow.moveUp();
        } else if (gameContainer.getInput().isKeyPressed(Input.KEY_DOWN)) {
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            arrow.moveDown();
        } else if (gameContainer.getInput().isKeyPressed(Input.KEY_ENTER)) {
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            handleMenuItemChoice(arrow.currIdx);
        } else if (gameContainer.getInput().isKeyPressed(Input.KEY_LEFT)) {
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            onLeftKeyPress();
        } else if (gameContainer.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            onRightKeyPress();
        } else if (gameContainer.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            returnToPreviousMenu();
        }
    }

    private void handleMenuItemChoice(int idx) {
        switch (idx) {
            case 0: // BACK
                Menu.returnToPreviousMenu();
                // store the settings from user in the file 'config.properties'
                SettingStorage.Property[] properties = new SettingStorage.Property[2];
                properties[0] = new SettingStorage.Property("sound_volume_level",
                        Integer.toString(sound_volume_slider.getValue()));
                properties[1] = new SettingStorage.Property("music_volume_level",
                        Integer.toString(music_volume_slider.getValue()));

                SettingStorage.storeSettings(properties);
                break;
            case 1: // SOUND VOLUME

                break;
            case 2: // MUSIC VOLUME

                break;
        }
    }

    @Override
    public void onMouseClick(GameContainer gameContainer, StateBasedGame stateBasedGame, int mouseX, int mouseY) {
        if (mouseX > back_btn_position.x && mouseX < back_btn_position.x + back_btn_width) {
            if (mouseY > back_btn_position.y && mouseY < back_btn_position.y + back_btn_height) {
                MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
                arrow.currIdx = 0;
                handleMenuItemChoice(0);
            }
        }
        if (sound_volume_slider.onClick(mouseX, mouseY)) {
            arrow.currIdx = 1;
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            UserSettings.setSoundVolume(sound_volume_slider.getValue());
        }
        if (music_volume_slider.onClick(mouseX, mouseY)) {
            arrow.currIdx = 2;
            MenuSounds.CLICK_SOUND.play(1.f, UserSettings.soundVolume);
            UserSettings.setMusicVolume(music_volume_slider.getValue());
            Menu.updateMainMenuMusicVolume();
        }
    }

    private void onLeftKeyPress() {
        switch (arrow.currIdx) {
            case 1: // SOUND VOLUME
                sound_volume_slider.decreaseValue();
                UserSettings.setSoundVolume(sound_volume_slider.getValue());
                break;
            case 2: // MUSIC VOLUME
                music_volume_slider.decreaseValue();
                UserSettings.setMusicVolume(music_volume_slider.getValue());
                Menu.updateMainMenuMusicVolume();
                break;
        }
    }

    private void onRightKeyPress() {
        switch (arrow.currIdx) {
            case 1: // SOUND VOLUME
                sound_volume_slider.increaseValue();
                UserSettings.setSoundVolume(sound_volume_slider.getValue());
                break;
            case 2: // MUSIC VOLUME
                music_volume_slider.increaseValue();
                UserSettings.setMusicVolume(music_volume_slider.getValue());
                Menu.updateMainMenuMusicVolume();
                break;
        }
    }

    @Override
    public void onEnterState(GameContainer gc) {

    }

    @Override
    public void onLeaveState(GameContainer gameContainer) {
        main_menu_intro_sound.stop();
        main_menu_music.stop();
    }
}
