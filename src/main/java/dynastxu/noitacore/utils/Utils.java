package dynastxu.noitacore.utils;

import net.minecraft.core.NonNullList;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class Utils {
    public static <T> @NonNull NonNullList<T> copy(@NonNull NonNullList<T> list) {
        NonNullList<T> copy = NonNullList.createWithCapacity(list.size());
        copy.addAll(list);
        return copy;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NonNull Vec3 copy(@NonNull Vec3 vec3) {
        return new Vec3(vec3.x, vec3.y, vec3.z);
    }
}
