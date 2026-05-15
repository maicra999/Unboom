package cc.maicra999.unboom;

import cc.maicra999.unboom.util.Logs;
import org.bukkit.GameRules;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.slf4j.Logger;

public class UnboomListener implements Listener {

    private static final Logger LOGGER = Logs.logger();

    private final Unboom unboom;

    public UnboomListener(Unboom unboom) {
        this.unboom = unboom;
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        UnboomConfig config = unboom.config();

        if (config.excludedWorlds.contains(event.getBlock().getWorld().getName())) {
            return;
        }

        if (!config.applyToBlockExplosion) {
            return;
        }

        if (config.preventBlockDamage) {
            event.blockList().removeIf(block -> !config.excludedBlocks.contains(block.getType()));
        }

        if (config.logging) {
            LOGGER.info(
                    "Block explosion at {} in world '{}'",
                    formatLocation(event.getBlock().getLocation()),
                    event.getBlock().getWorld().getName());
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        UnboomConfig config = unboom.config();

        if (config.excludedWorlds.contains(event.getLocation().getWorld().getName())) {
            return;
        }

        if (!config.applyToEntityExplosion) {
            return;
        }

        if (config.preventBlockDamage) {
            event.blockList().removeIf(block -> !config.excludedBlocks.contains(block.getType()));
        }

        if (config.logging) {
            LOGGER.info(
                    "Entity explosion at {} in world '{}'",
                    formatLocation(event.getEntity().getLocation()),
                    event.getLocation().getWorld().getName());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        UnboomConfig config = unboom.config();

        if (config.excludedWorlds.contains(event.getEntity().getWorld().getName())) {
            return;
        }

        if (config.excludedEntities.contains(event.getEntityType())) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            return;
        }

        switch (event.getEntity()) {
            case Player _ -> {
                if (config.preventPlayerDamage) {
                    event.setCancelled(true);
                }
            }
            case Monster _ -> {
                if (config.preventHostileMobDamage) {
                    event.setCancelled(true);
                }
            }
            case Animals _ -> {
                if (config.preventPassiveMobDamage) {
                    event.setCancelled(true);
                }
            }
            default -> {
                if (config.preventEntityDamage) {
                    event.setCancelled(true);
                }
            }
        }

        if (config.logging) {
            LOGGER.info(
                    "{} took explosion damage at {} in world '{}' (cancelled = {})",
                    event.getEntity().getType(),
                    formatLocation(event.getEntity().getLocation()),
                    event.getEntity().getWorld().getName(),
                    event.isCancelled());
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        UnboomConfig config = unboom.config();

        if (config.excludedWorlds.contains(event.getEntity().getWorld().getName())) {
            return;
        }

        if (config.excludedEntities.contains(event.getEntity().getType())) {
            return;
        }

        if (event.getCause() != HangingBreakEvent.RemoveCause.EXPLOSION) {
            return;
        }

        if (config.preventEntityDamage) {
            event.setCancelled(true);
        }

        if (config.logging) {
            LOGGER.info(
                    "{} was destroyed from explosion at {} in world '{}' (cancelled = {})",
                    event.getEntity().getType(),
                    formatLocation(event.getEntity().getLocation()),
                    event.getEntity().getWorld().getName(),
                    event.isCancelled());
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!event.getWorld().getGameRuleValue(GameRules.MOB_GRIEFING)) {
            LOGGER.warn(
                    "Mob griefing is disabled in world '{}'! We recommend turning it on as some explosion events may not trigger correctly otherwise.",
                    event.getWorld().getName());
        }
    }

    private static String formatLocation(Location location) {
        return String.format("[%d, %d, %d]", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
