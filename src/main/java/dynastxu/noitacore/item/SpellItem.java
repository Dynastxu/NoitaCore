package dynastxu.noitacore.item;

import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class SpellItem extends Item {
    public SpellItem(@NonNull Properties properties) {
        super(properties.stacksTo(1));
    }
}
