package cc.maicra999.unboom;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class UnboomConfig {

    public static final int CURRENT_CONFIG_VERSION = 1;

    @Comment(
            "Protect block explosions (e.g. TNT and beds). Entity and mob damage protections may still apply even if this is disabled.")
    public boolean applyToBlockExplosion = true;

    @Comment(
            "Protect entity explosions (e.g. creepers and end crystals). Entity and mob damage protections may still apply even if this is disabled.")
    public boolean applyToEntityExplosion = true;

    @Comment("Prevent blocks from being destroyed by explosions.")
    public boolean preventBlockDamage = true;

    @Comment("Prevent hostile mobs (e.g. zombies and skeletons) from taking damages from explosions.")
    public boolean preventHostileMobDamage = false;

    @Comment("Prevent passive mobs (e.g. pigs and horses) from taking damages from explosions.")
    public boolean preventPassiveMobDamage = true;

    @Comment("Prevent other entities (e.g. decorative entities) from taking damages from explosions.")
    public boolean preventEntityDamage = true;

    @Comment("Prevent players from taking damages from explosions.")
    public boolean preventPlayerDamage = false;

    @Comment("List of blocks that are allowed to be destroyed by explosions.")
    public List<Material> excludedBlocks = List.of(Material.TNT);

    @Comment("List of entities that are allowed to take damage from explosions.")
    public List<EntityType> excludedEntities = List.of(EntityType.ITEM);

    @Comment("List of worlds where all protections are not applied.")
    public List<String> excludedWorlds = List.of("shigen_the_end", "shigen", "world_minecraft_shigen");

    @Comment("Logs whenever a protection applies. This is mostly for debugging.")
    public boolean logging = false;

    @Comment("Config version - do not modify this field!")
    public int configVersion = -1;
}
