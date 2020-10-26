package logic;

import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileMapInfo {

    // the map itself
    public static TiledMap map;

    // tile size info
    public static int TILE_WIDTH, TILE_HEIGHT, LEVEL_WIDTH_TILES, LEVEL_HEIGHT_TILES, LEVEL_WIDTH_PIXELS,
            LEVEL_HEIGHT_PIXELS;

    // tilesets
    public static final int LANDSCAPE_TILES_TILESET_IDX = 1;
    public static final String TILESETS_LOCATION = "assets/tilesets";

    // layers
    public static final int LANDSCAPE_TILES_LAYER_IDX = 0;
    public static final int DESTRUCTION_TILES_LAYER_IDX = 1;

    public static int[] CLASS_GRASS, CLASS_CONCRETE, CLASS_DIRT;

    public static final int[] collision_replace_indices;
    public static final int[] destructible_tile_indices;
    public static final int[] destructible_tile_replace_indices;
    public static final int[] indestructible_tile_indices;

    private static final float CHANCE_FOR_TILE_DESTRUCTION = 0.1f;  // 10% chance for another tile to be destroyed

    public static Map<Integer, Float> destructible_tiles_health_info;

    static {
        collision_replace_indices = new int[]{
                144,    // DIRT
                145,    // CONCRETE
                146     // GRASS
        };
        destructible_tile_indices = new int[]{1, 2, 26, 27, 37, 97, 100, 123, 132, 133};
        destructible_tile_replace_indices = new int[]{48, 49, 50, 51, 52, 53, 139, 138, 137, 135};
        indestructible_tile_indices = new int[]{60, 72, 73, 74, 75, 76, 77, 78, 79, 84, 85, 86, 87, 88, 89, 96, 98, 99,
                101, 108, 109, 110, 111, 112, 113};
    }

    public static void init() {
        TILE_WIDTH = map.getTileWidth();
        TILE_HEIGHT = map.getTileHeight();

        // create TileInfo for 'landscape_tiles' TileSet
        TileSet landscape_tiles = map.getTileSet(LANDSCAPE_TILES_TILESET_IDX);
        if (!landscape_tiles.name.equals("landscape_tiles"))
            throw new IllegalAccessError(
                    "Wrong tileset index: [" + LANDSCAPE_TILES_TILESET_IDX + "] is not landscape_tiles");
        else {
            int idx;
            for (idx = 0; idx < destructible_tile_indices.length; ++idx) {
                destructible_tile_indices[idx] += landscape_tiles.firstGID;
                destructible_tile_replace_indices[idx] += landscape_tiles.firstGID;
            }
            for (idx = 0; idx < indestructible_tile_indices.length; ++idx) {
                indestructible_tile_indices[idx] += landscape_tiles.firstGID;
            }
            for (idx = 0; idx < collision_replace_indices.length; ++idx) {
                collision_replace_indices[idx] += landscape_tiles.firstGID;
            }
        }

        // init tiles of grass for tile replacement
        int[] grass_indices = new int[]{
                0, 3, 4, 5, 6, 12, 28, 29, 30, 31
        };
        CLASS_GRASS = new int[grass_indices.length];
        for (int i = 0; i < grass_indices.length; ++i) {
            CLASS_GRASS[i] = landscape_tiles.firstGID + grass_indices[i];
        }
        // init tiles of concrete for tile replacement
        int[] concrete_indices = new int[]{
                62, 63, 64, 65, 66, 67, 90, 91, 102, 103, 114, 115, 120, 121, 122, 124, 125, 126, 127, 134, 151
        };
        CLASS_CONCRETE = new int[concrete_indices.length];
        for (int i = 0; i < concrete_indices.length; ++i) {
            CLASS_CONCRETE[i] = landscape_tiles.firstGID + concrete_indices[i];
        }
        // init tiles of dirt for tile replacement
        int[] dirt_indices = new int[]{
                13, 14, 15, 16, 17, 18, 19, 24, 25, 39, 40, 41, 42
        };
        CLASS_DIRT = new int[dirt_indices.length];
        for (int i = 0; i < dirt_indices.length; ++i) {
            CLASS_DIRT[i] = landscape_tiles.firstGID + dirt_indices[i];
        }

        /*
        // create TileInfo for 'enemy_tiles' TileSet
        TileSet enemy_tiles = map.getTileSet(ENEMY_TILES_TILESET_IDX);
        if (!enemy_tiles.name.equals("enemy_tiles"))
            throw new IllegalAccessError("Wrong tileset index: [" + ENEMY_TILES_TILESET_IDX + "] is not enemy_tiles");
        else {
            for (int idx = 0; idx < windmill_indices.length; ++idx) {
                windmill_indices[idx] += enemy_tiles.firstGID;
            }
        }
         */

        /*
        // create TileInfo for 'plane_tiles' TileSet
        TileSet plane_tiles = map.getTileSet(PLANE_TILES_TILESET_IDX);
        if (!plane_tiles.name.equals("plane_tiles"))
            throw new IllegalAccessError("Wrong tileset index: [" + PLANE_TILES_TILESET_IDX + "] is not plane_tiles");
        else {
            for (int idx = 0; idx < static_plane_creation_indices.length; ++idx) {
                static_plane_creation_indices[idx] += plane_tiles.firstGID;
            }
            for (int idx = 0; idx < static_plane_collision_indices.length; ++idx) {
                static_plane_collision_indices[idx] += plane_tiles.firstGID;
            }
        }
        static_entity_indices = new int[static_plane_collision_indices.length + windmill_indices.length];
        for (int i = 0; i < static_entity_indices.length; ++i) {
            if (i < static_plane_collision_indices.length)
                static_entity_indices[i] = static_plane_collision_indices[i];
            else static_entity_indices[i] = windmill_indices[i - static_plane_collision_indices.length];
        }
 */

        destructible_tiles_health_info = new HashMap<>();
    }

    /* this is called every time a new map is loaded - maps can have different sizes! */
    public static boolean updateMapSize() {
        int prevWidth = LEVEL_WIDTH_TILES, prevHeight = LEVEL_HEIGHT_TILES;
        LEVEL_WIDTH_TILES = map.getWidth();
        LEVEL_HEIGHT_TILES = map.getHeight();
        LEVEL_WIDTH_PIXELS = LEVEL_WIDTH_TILES * TILE_WIDTH;
        LEVEL_HEIGHT_PIXELS = LEVEL_HEIGHT_TILES * TILE_HEIGHT;
        if (prevHeight == 0 && prevWidth == 0) return true;    // first call
        return prevHeight != LEVEL_HEIGHT_TILES || prevWidth != LEVEL_WIDTH_TILES;
    }

    public static void reset() {
        if (destructible_tiles_health_info == null) destructible_tiles_health_info = new HashMap<>();
        else destructible_tiles_health_info.clear();
    }

    public static void setMap(TiledMap myMap) {
        map = myMap;
    }

    public static void doCollateralTileDamage(int x, int y) {
        // maybe destroy nearby tiles
        List<Tile> tiles = new ArrayList<>();
        if (y > 0) {
            // top tile
            tiles.add(new Tile(x, y - 1, map.getTileId(x, y - 1, LANDSCAPE_TILES_LAYER_IDX)));
            if (x > 0) {
                // top left tile
                tiles.add(new Tile(x - 1, y - 1, map.getTileId(x - 1, y - 1, LANDSCAPE_TILES_LAYER_IDX)));
            }
            if (x < map.getWidth() - 1) {
                // top right tile
                tiles.add(new Tile(x + 1, y - 1, map.getTileId(x + 1, y - 1, LANDSCAPE_TILES_LAYER_IDX)));
            }
        }

        if (y < map.getHeight() - 1) {
            // bottom tile
            tiles.add(new Tile(x, y + 1, map.getTileId(x, y + 1, LANDSCAPE_TILES_LAYER_IDX)));
            if (x > 0) {
                // bottom left tile
                tiles.add(new Tile(x - 1, y + 1, map.getTileId(x - 1, y + 1, LANDSCAPE_TILES_LAYER_IDX)));
            }
            if (x < map.getWidth() - 1) {
                // bottom right tile
                tiles.add(new Tile(x + 1, y + 1, map.getTileId(x + 1, y + 1, LANDSCAPE_TILES_LAYER_IDX)));
            }
        }

        if (x > 0) {
            // left tile
            tiles.add(new Tile(x - 1, y, map.getTileId(x - 1, y, LANDSCAPE_TILES_LAYER_IDX)));
        }

        if (x < map.getWidth() - 1) {
            // right tile
            tiles.add(new Tile(x + 1, y, map.getTileId(x + 1, y, LANDSCAPE_TILES_LAYER_IDX)));
        }

        for (Tile tile : tiles) {
            double d = Math.random();
            boolean isDestructibleTile = false;
            for (int idx2 = 0; idx2 < destructible_tile_indices.length; ++idx2) {
                if (tile.tileID == destructible_tile_indices[idx2]) {
                    isDestructibleTile = true;
                    if (d < CHANCE_FOR_TILE_DESTRUCTION) {  // look for chance for it to be destroyed
                        map.setTileId(tile.xVal, tile.yVal, LANDSCAPE_TILES_LAYER_IDX, destructible_tile_replace_indices[idx2]);
                        destructible_tiles_health_info.remove(tile.key);
                    }
                    break;
                }
            }
            if (!isDestructibleTile) {
                if (d < CHANCE_FOR_TILE_DESTRUCTION) {
                    int replacement_tile_id = TileMapInfo.getReplacementTileID(tile.tileID);
                    if (replacement_tile_id != -1)
                        map.setTileId(tile.xVal, tile.yVal, DESTRUCTION_TILES_LAYER_IDX, replacement_tile_id);
                }
            }
        }
    }

    public static int getReplacementTileID(int tileID) {
        for (int value : CLASS_DIRT) {
            if (tileID == value) return collision_replace_indices[0];
        }
        for (int value : CLASS_CONCRETE) {
            if (tileID == value) return collision_replace_indices[1];
        }
        for (int value : CLASS_GRASS) {
            if (tileID == value) return collision_replace_indices[2];
        }
        return -1;
    }

    public static boolean isCollisionTile(int tileID) {
        for (int value : indestructible_tile_indices) {
            if (tileID == value) return true;
        }
        for (int value : destructible_tile_indices) {
            if (tileID == value) return true;
        }
        return false;
    }

    private static class Tile {
        int tileID, xVal, yVal, key;

        Tile(int xVal, int yVal, int tileID) {
            this.tileID = tileID;
            this.xVal = xVal;
            this.yVal = yVal;
            this.key = generateKey(xVal, yVal);
        }
    }

    public static int generateKey(int xPos, int yPos) {
        return xPos > yPos ? -xPos * yPos : xPos * yPos;
    }

}
