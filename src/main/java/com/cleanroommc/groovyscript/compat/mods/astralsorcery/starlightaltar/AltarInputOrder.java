package com.cleanroommc.groovyscript.compat.mods.astralsorcery.starlightaltar;

import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.tile.TileAltar;

import java.util.Arrays;

public class AltarInputOrder {

    // @formatter:off
    public static final int[][] DISCOVERY = new int[][]{
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
    };
    public static final int[][] ATTUNEMENT = new int[][]{
            {9, -1, -1, -1, 10},
            {-1, 0, 1, 2, -1},
            {-1, 3, 4, 5, -1},
            {-1, 6, 7, 8, -1},
            {11, -1, -1, -1, 12}
    };
    public static final int[][] CONSTELLATION_CRAFT = new int[][]{
            {9, 13, -1, 14, 10},
            {15, 0, 1, 2, 16},
            {-1, 3, 4, 5, -1},
            {17, 6, 7, 8, 18},
            {11, 19, -1, 20, 12}
    };
    public static final int[][] TRAIT_CRAFT = new int[][]{
            {9, 13, 21, 14, 10},
            {15, 0, 1, 2, 16},
            {22, 3, 4, 5, 23},
            {17, 6, 7, 8, 18},
            {11, 19, 24, 20, 12}
    };
    // @formatter:on

    public static int[][] getMap(TileAltar.AltarLevel level) {
        return switch (level) {
            case DISCOVERY -> AltarInputOrder.DISCOVERY;
            case ATTUNEMENT -> AltarInputOrder.ATTUNEMENT;
            case CONSTELLATION_CRAFT -> AltarInputOrder.CONSTELLATION_CRAFT;
            case TRAIT_CRAFT -> AltarInputOrder.TRAIT_CRAFT;
            case BRILLIANCE -> null;
        };
    }

    public static ItemHandle[] initInputList(TileAltar.AltarLevel level) {
        ItemHandle[] rVal = switch (level) {
            case DISCOVERY -> new ItemHandle[9];
            case ATTUNEMENT -> new ItemHandle[13];
            case CONSTELLATION_CRAFT -> new ItemHandle[21];
            case TRAIT_CRAFT -> new ItemHandle[25];
            case BRILLIANCE -> null;
        };
        if (rVal != null) Arrays.fill(rVal, null);
        return rVal;
    }
}
