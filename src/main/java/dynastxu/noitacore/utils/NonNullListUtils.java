package dynastxu.noitacore.utils;

import net.minecraft.core.NonNullList;
import org.jspecify.annotations.NonNull;

public class NonNullListUtils {
    public static <T> @NonNull NonNullList<T> copy(@NonNull NonNullList<T> list, @NonNull T defaultValue) {
        NonNullList<T> copy = NonNullList.withSize(list.size(), defaultValue);
        for (int i = 0; i < list.size(); i++) {
            copy.set(i, list.get(i));
        }
        return copy;
    }
}
