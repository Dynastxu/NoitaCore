package dynastxu.noitacore;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(NoitaCore.MODID)
public class NoitaCore {
    public static final String MODID = "noitacore";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NoitaCore(IEventBus modEventBus, ModContainer modContainer) {
    }
}
