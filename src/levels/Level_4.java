package levels;

import logic.WayPointManager;
import main.ZuluAssault;
import models.interaction_circles.HealthCircle;
import models.interaction_circles.TeleportCircle;
import models.items.*;
import models.war_attenders.MovableWarAttender;
import models.war_attenders.planes.Helicopter;
import models.war_attenders.soldiers.EnemySoldier;
import models.war_attenders.soldiers.RocketSoldier;
import models.war_attenders.tanks.CannonTank;
import models.war_attenders.tanks.NapalmTank;
import models.war_attenders.tanks.RocketTank;
import models.war_attenders.tanks.ShellTank;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.List;

public class Level_4 extends AbstractLevel implements GameState {

    private static Sound level4_intro_sound;
    private static Music level4_music;

    public Level_4() {
        super();
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        if (!calledOnce) {
            mission_title = "Recapture Tank";
            briefing_message = "Our scanners have found several small battle groups around the area. One of theses " +
                    "groups has reportedly captured one of our tanks. Your mission is to find the armor unit and take" +
                    "out the enemy with it.";
            debriefing_message = "";
            calledOnce = true;
            return;
        }

        resetLevel();    // reset the level before init

        level_intro_sound = level4_intro_sound;
        level_music = level4_music;


        // SETUP ITEMS
        // silver wrenches
        Item silver_wrench_1 = new SilverWrenchItem(new Vector2f(3283, 1921));
        items.add(silver_wrench_1);

        Item silver_wrench_2 = new SilverWrenchItem(new Vector2f(3474, 1630));
        items.add(silver_wrench_2);

        Item silver_wrench_3 = new SilverWrenchItem(new Vector2f(3443, 1568));
        items.add(silver_wrench_3);

        Item silver_wrench_4 = new SilverWrenchItem(new Vector2f(3540, 340));
        items.add(silver_wrench_4);

        Item silver_wrench_5 = new SilverWrenchItem(new Vector2f(3172, 35));
        items.add(silver_wrench_5);

        Item silver_wrench_6 = new SilverWrenchItem(new Vector2f(2052, 563));
        items.add(silver_wrench_6);

        Item silver_wrench_7 = new SilverWrenchItem(new Vector2f(2170, 563));
        items.add(silver_wrench_7);

        Item silver_wrench_8 = new SilverWrenchItem(new Vector2f(2297, 567));
        items.add(silver_wrench_8);

        Item silver_wrench_9 = new SilverWrenchItem(new Vector2f(2435, 567));
        items.add(silver_wrench_9);

        Item silver_wrench_10 = new SilverWrenchItem(new Vector2f(1120, 1282));
        items.add(silver_wrench_10);

        Item silver_wrench_11 = new SilverWrenchItem(new Vector2f(1146, 1235));
        items.add(silver_wrench_11);

        Item silver_wrench_12 = new SilverWrenchItem(new Vector2f(1075, 1212));
        items.add(silver_wrench_12);

        Item silver_wrench_13 = new SilverWrenchItem(new Vector2f(1036, 1148));
        items.add(silver_wrench_13);

        Item silver_wrench_14 = new SilverWrenchItem(new Vector2f(950, 1050));
        items.add(silver_wrench_14);
        // the next two wrenches are on water - weird, i know, but it's in the game
        Item silver_wrench_15 = new SilverWrenchItem(new Vector2f(2170, 900));
        items.add(silver_wrench_15);

        Item silver_wrench_16 = new SilverWrenchItem(new Vector2f(1545, 890));
        items.add(silver_wrench_16);

        Item silver_wrench_17 = new SilverWrenchItem(new Vector2f(691, 1046));
        items.add(silver_wrench_17);

        Item silver_wrench_18 = new SilverWrenchItem(new Vector2f(518, 1130));
        items.add(silver_wrench_18);

        Item silver_wrench_19 = new SilverWrenchItem(new Vector2f(330, 1270));
        items.add(silver_wrench_19);

        Item silver_wrench_20 = new SilverWrenchItem(new Vector2f(440, 1370));
        items.add(silver_wrench_20);

        Item silver_wrench_21 = new SilverWrenchItem(new Vector2f(90, 2130));
        items.add(silver_wrench_21);

        Item silver_wrench_22 = new SilverWrenchItem(new Vector2f(90, 2290));
        items.add(silver_wrench_22);

        Item silver_wrench_23 = new SilverWrenchItem(new Vector2f(90, 2450));
        items.add(silver_wrench_23);

        Item silver_wrench_24 = new SilverWrenchItem(new Vector2f(195, 2665));
        items.add(silver_wrench_24);

        Item silver_wrench_25 = new SilverWrenchItem(new Vector2f(290, 2725));
        items.add(silver_wrench_25);

        Item silver_wrench_26 = new SilverWrenchItem(new Vector2f(590, 2910));
        items.add(silver_wrench_26);

        Item silver_wrench_27 = new SilverWrenchItem(new Vector2f(690, 2873));
        items.add(silver_wrench_27);

        Item silver_wrench_28 = new SilverWrenchItem(new Vector2f(810, 2790));
        items.add(silver_wrench_28);

        // golden wrenches
        Item golden_wrench_1 = new GoldenWrenchItem(new Vector2f(2345, 505));
        items.add(golden_wrench_1);

        // invincibility
        Item invincibility_item_1 = new InvincibilityItem(new Vector2f(620, 3700));
        items.add(invincibility_item_1);

        // mega pulse
        Item mega_pulse_item_1 = new MegaPulseItem(new Vector2f(1222, 3700));
        items.add(mega_pulse_item_1);

        Item mega_pulse_item_2 = new MegaPulseItem(new Vector2f(1742, 3700));
        items.add(mega_pulse_item_2);

        // SETUP HEALTH CIRCLES
        HealthCircle health_circle_1 = new HealthCircle(new Vector2f(1898, 1232));
        health_circles.add(health_circle_1);

        // SETUP TELEPORT CIRCLES
        TeleportCircle teleport_circle_1 = new TeleportCircle(new Vector2f(140, 1635));
        teleport_circles.add(teleport_circle_1);

        TeleportCircle teleport_circle_2 = new TeleportCircle(new Vector2f(3825, 1819));
        teleport_circles.add(teleport_circle_2);


        // SETUP ENEMY WAR ATTENDERS
        // bottom middle
        MovableWarAttender enemy_tank_1 = new NapalmTank(new Vector2f(2360, 2430), true, false);
        enemy_tank_1.setRotation(315);
        enemy_tank_1.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_1);

        MovableWarAttender enemy_tank_2 = new NapalmTank(new Vector2f(2525, 2265), true, false);
        enemy_tank_2.setRotation(135);
        enemy_tank_2.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_2);

        MovableWarAttender enemy_tank_3 = new RocketTank(new Vector2f(2520, 2394), true, false);
        enemy_tank_3.setRotation(225);
        enemy_tank_3.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_3);
        //top right
        MovableWarAttender enemy_tank_4 = new ShellTank(new Vector2f(3550, 481), true, false);
        enemy_tank_4.setRotation(225);
        enemy_tank_4.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_4);

        MovableWarAttender enemy_tank_5 = new ShellTank(new Vector2f(3640, 600), true, false);
        enemy_tank_5.setRotation(255);
        enemy_tank_5.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_5);

        MovableWarAttender enemy_tank_6 = new ShellTank(new Vector2f(3776, 536), true, false);
        enemy_tank_6.setRotation(105);
        enemy_tank_6.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_6);

        MovableWarAttender enemy_tank_7 = new ShellTank(new Vector2f(3822, 770), true, false);
        enemy_tank_7.setRotation(290);
        enemy_tank_7.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_7);
        // top left
        MovableWarAttender enemy_tank_8 = new NapalmTank(new Vector2f(400, 689), true, false);
        enemy_tank_8.setRotation(115);
        enemy_tank_8.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_8);

        MovableWarAttender enemy_tank_9 = new NapalmTank(new Vector2f(712, 535), true, false);
        enemy_tank_9.setRotation(165);
        enemy_tank_9.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_9);
        // TODO: add mega pulse tank below
        MovableWarAttender enemy_tank_10 = new RocketTank(new Vector2f(928, 216), true, false);
        enemy_tank_10.setRotation(295);
        enemy_tank_10.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_10);

        MovableWarAttender enemy_tank_11 = new ShellTank(new Vector2f(908, 300), true, false);
        enemy_tank_11.setRotation(245);
        enemy_tank_11.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_tank_11);

        MovableWarAttender enemy_soldier_1 = new RocketSoldier(new Vector2f(304, 484), true);
        enemy_soldier_1.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_1);

        MovableWarAttender enemy_soldier_2 = new EnemySoldier(new Vector2f(488, 342), true);
        enemy_soldier_2.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_2);

        MovableWarAttender enemy_soldier_3 = new EnemySoldier(new Vector2f(477, 373), true);
        enemy_soldier_3.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_3);

        MovableWarAttender enemy_soldier_4 = new EnemySoldier(new Vector2f(482, 404), true);
        enemy_soldier_4.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_4);

        MovableWarAttender enemy_soldier_5 = new RocketSoldier(new Vector2f(548, 220), true);
        enemy_soldier_5.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_5);

        MovableWarAttender enemy_soldier_6 = new RocketSoldier(new Vector2f(468, 164), true);
        enemy_soldier_6.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_6);

        MovableWarAttender enemy_soldier_7 = new RocketSoldier(new Vector2f(558, 115), true);
        enemy_soldier_7.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_7);

        MovableWarAttender enemy_soldier_8 = new RocketSoldier(new Vector2f(495, 77), true);
        enemy_soldier_8.setAsMandatory();
        hostile_movable_war_attenders.add(enemy_soldier_8);

        // create the waypoint list for the 2 cannon tanks
        List<Vector2f> wayPoints = new ArrayList<>();
        wayPoints.add(new Vector2f(2976, 157));
        wayPoints.add(new Vector2f(3150, 237));
        wayPoints.add(new Vector2f(3243, 1300));
        wayPoints.add(new Vector2f(3233, 1589));
        wayPoints.add(new Vector2f(2976, 1817));
        wayPoints.add(new Vector2f(957, 1850));
        wayPoints.add(new Vector2f(525, 1710));
        wayPoints.add(new Vector2f(344, 1520));
        wayPoints.add(new Vector2f(262, 1302));
        wayPoints.add(new Vector2f(334, 1085));
        wayPoints.add(new Vector2f(496, 884));
        wayPoints.add(new Vector2f(945, 598));
        wayPoints.add(new Vector2f(1193, 523));
        wayPoints.add(new Vector2f(1322, 357));
        wayPoints.add(new Vector2f(1634, 211));

        MovableWarAttender enemy_tank_12 = new CannonTank(new Vector2f(1607, 209), true, false);
        enemy_tank_12.setRotation(90);
        enemy_tank_12.setAsMandatory();
        enemy_tank_12.addWayPoints(new WayPointManager(enemy_tank_12.getPosition(),
                enemy_tank_12.getRotation(),
                wayPoints,
                0));
        hostile_movable_war_attenders.add(enemy_tank_12);

        MovableWarAttender enemy_tank_13 = new CannonTank(new Vector2f(314, 1152), true, false);
        enemy_tank_13.setRotation(45);
        enemy_tank_13.setAsMandatory();
        enemy_tank_13.addWayPoints(new WayPointManager(enemy_tank_13.getPosition(),
                enemy_tank_13.getRotation(),
                wayPoints,
                9));
        hostile_movable_war_attenders.add(enemy_tank_13);


        // SETUP PLAYER'S DRIVABLE WAR ATTENDERS
        MovableWarAttender player_drivable_tank_1 = new NapalmTank(new Vector2f(2730, 2218), false, true);
        player_drivable_tank_1.setRotation(80);
        drivable_war_attenders.add(player_drivable_tank_1);

        // SETUP THE PLAYER START POSITION AND WAR ATTENDER
        Vector2f playerStartPos = new Vector2f(3830, 3850);

        Helicopter helicopter = new Helicopter(playerStartPos, false, true);
        helicopter.setStarting();

        // DEFINE THE MAP
        map = new TiledMap("assets/maps/level_4.tmx");

        player.init(helicopter);

        super.init(gameContainer, stateBasedGame);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        super.render(gameContainer, stateBasedGame, graphics);
    }

    @Override
    public void loadLevelMusic() {
        if (level4_intro_sound == null) {
            try {
                level4_intro_sound = new Sound("audio/music/level_4_intro.ogg");
                level4_music = new Music("audio/music/level_4.ogg");
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getID() {
        return ZuluAssault.LEVEL_4;
    }
}