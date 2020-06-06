package me.jellysquid.mods.lithium.mixin.block.flatten_states;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The methods in {@link FluidState} involve a lot of indirection through the BlockState/Fluid classes and require
 * property lookups in order to compute the returned value. This shows up as a hot spot in some areas (namely fluid
 * ticking and world generation).
 * <p>
 * Since these are constant for any given fluid state, we can cache them nearby for improved performance and eliminate
 * the overhead.
 */
@Mixin(FluidState.class)
public abstract class MixinFluidStateImpl {
    @Shadow
    public abstract Fluid getFluid();

    private float height;
    private int level;
    private boolean isEmpty;
    private boolean isStill;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        this.isEmpty = getFluid().isEmpty();

        this.level = getFluid().getLevel((FluidState) (Object) this);
        this.height = getFluid().getHeight((FluidState) (Object) this);
        this.isStill = getFluid().isStill((FluidState) (Object) this);
    }

    /**
     * @author Lithium
     */
    @Overwrite
    public boolean isStill() {
        return this.isStill;
    }

    /**
     * @author Lithium
     */
    @Overwrite
    public boolean isEmpty() {
        return this.isEmpty;
    }

    /**
     * @author Lithium
     */
    @Overwrite
    public float getHeight() {
        return this.height;
    }

    /**
     * @author Lithium
     */
    @Overwrite
    public int getLevel() {
        return this.level;
    }
}
