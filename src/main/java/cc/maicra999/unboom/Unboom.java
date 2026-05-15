package cc.maicra999.unboom;

import cc.maicra999.unboom.util.Logs;
import java.io.IOException;
import java.nio.file.Path;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class Unboom extends JavaPlugin {

    private static final Logger LOGGER = Logs.logger();

    private final YamlConfigurationLoader configLoader;

    private UnboomConfig config;

    public Unboom() {
        this.configLoader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .path(getConfigPath())
                .build();
    }

    @Override
    public void onEnable() {
        try {
            loadAndUpdateConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to load or update config.yml");
            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(new UnboomListener(this), this);
    }

    public void loadAndUpdateConfig() throws IOException {
        CommentedConfigurationNode root = configLoader.load();
        this.config = root.get(UnboomConfig.class);

        if (config != null && config.configVersion < UnboomConfig.CURRENT_CONFIG_VERSION) {
            // Save the config file with new fields
            config.configVersion = UnboomConfig.CURRENT_CONFIG_VERSION;
            root.set(UnboomConfig.class, config);
            configLoader.save(root);
        }
    }

    public UnboomConfig config() {
        return config;
    }

    private Path getConfigPath() {
        return getDataFolder().toPath().resolve("config.yml");
    }
}
