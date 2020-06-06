package me.jellysquid.mods.lithium.common.world.interests;

import me.jellysquid.mods.lithium.common.world.chunk.LithiumHashPalette;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.ChunkSection;

import java.util.Set;

import net.minecraft.world.chunk.Palette;

public class PointOfInterestTypeHelper {
    private static Set<BlockState> TYPES;

    public static void init(Set<BlockState> types) {
        if (TYPES != null) {
            throw new IllegalStateException("Already initialized");
        }

        TYPES = types;
    }

    public static boolean shouldScan(ChunkSection section) {
        for (BlockState state : TYPES) {
            Palette<BlockState> palette = section.getContainer().palette;
            if (palette instanceof LithiumHashPalette) {
                if (((LithiumHashPalette<BlockState>) palette).tableContainsKey(state)) {
                    return true;
                }
            } else if (palette.accepts(state::equals)) {
                return true;
            }
        }

        return false;
    }

}
