package dynastxu.noitacore.client.language;

import com.mojang.logging.LogUtils;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.*;

public class EnglishTranslationCache {
    private static final Map<String, String> EN_US = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<String> MOD_IDS = new ArrayList<>();

    /**
     * 在客户端初始化时调用一次
     */
    public static void load(String modId) {
        if (MOD_IDS.contains(modId)) {
            return;
        }
        try (InputStream stream = EnglishTranslationCache.class.getResourceAsStream("/assets/" + modId + "/lang/en_us.json")) {
            Objects.requireNonNull(stream);
            Language.loadFromJson(
                    stream,
                    EN_US::put,
                    (_, _) -> {}
            );
            MOD_IDS.add(modId);
        } catch (Exception e) {
            LOGGER.error("Failed to load {}'s en_us.json", modId, e);
        }
    }

    public static void reload() {
        List<String> modIds = new ArrayList<>(MOD_IDS);
        EN_US.clear();
        MOD_IDS.clear();
        modIds.forEach(EnglishTranslationCache::load);
    }

    public static String getString(String key) {
        return EN_US.getOrDefault(key, key);
    }

    public static @NonNull MutableComponent getComponent(String key) {
        return Component.literal(getString(key));
    }
}
