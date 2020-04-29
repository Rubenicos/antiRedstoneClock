package com.trafalcraft.antiRedstoneClock.util.plotSquared;

import com.plotsquared.core.configuration.Captions;
import com.plotsquared.core.plot.flag.types.BooleanFlag;
import org.jetbrains.annotations.NotNull;

public class AntiRedstoneClockFlag extends BooleanFlag<AntiRedstoneClockFlag> {

    public static final AntiRedstoneClockFlag REDSTONE_TRUE = new AntiRedstoneClockFlag(true);
    public static final AntiRedstoneClockFlag REDSTONE_FALSE = new AntiRedstoneClockFlag(false);

    public AntiRedstoneClockFlag(boolean defaultValue) {
        super(defaultValue, Captions.FLAG_DESCRIPTION_REDSTONE);
    }

    @Override
    protected AntiRedstoneClockFlag flagOf(@NotNull Boolean aBoolean) {
        return aBoolean ? REDSTONE_TRUE : REDSTONE_FALSE;
    }

    @Override
    public AntiRedstoneClockFlag merge(@NotNull Boolean newValue) {
        return this.flagOf(newValue);
    }
}
