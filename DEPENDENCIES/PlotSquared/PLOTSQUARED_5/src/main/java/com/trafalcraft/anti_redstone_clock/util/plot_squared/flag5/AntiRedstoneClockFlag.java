package com.trafalcraft.anti_redstone_clock.util.plot_squared.flag5;

import com.plotsquared.core.configuration.Caption;
import com.plotsquared.core.plot.flag.types.BooleanFlag;
import org.jetbrains.annotations.NotNull;

public class AntiRedstoneClockFlag extends BooleanFlag<AntiRedstoneClockFlag> {

    public static final AntiRedstoneClockFlag REDSTONE_TRUE = new AntiRedstoneClockFlag(true);
    public static final AntiRedstoneClockFlag REDSTONE_FALSE = new AntiRedstoneClockFlag(false);

    public AntiRedstoneClockFlag(boolean defaultValue) {
        super(defaultValue, new Caption() {
            @Override
            public String getTranslated() {
                return "Set to `false` to disable antiRedstoneClock in the plot.";
            }

            @Override
            public boolean usePrefix() {
                return true;
            }
        });
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
