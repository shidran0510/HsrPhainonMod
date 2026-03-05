package com.shidran.hsrphainon.event;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import com.shidran.hsrphainon.item.LogicDawnmaker;
import com.shidran.hsrphainon.network.AnimationPacket;
import com.shidran.hsrphainon.network.LockTimerPacket;
import com.shidran.hsrphainon.registry.PacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class EventHandler {
    private static final java.util.Map<java.util.UUID, ItemStack> lastHeidItems = new java.util.HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }

        Player player = event.player;
        CompoundTag playerTag = tagPlayerData(player);
        CompoundTag itemTag = tagMainHand(player);
        Level world = world(player);
        ItemStack stack = stack(player);
        ItemStack currentStack = player.getMainHandItem();
        ItemStack lastStack = lastHeidItems.getOrDefault(player.getUUID(), ItemStack.EMPTY);

        LogicDawnmaker.RunTimer(player);

        boolean hasSword = hasDawnmakerInInventory(player) || player.getMainHandItem().getItem() instanceof ItemDawnmaker;

        if (!hasSword && playerTag.contains(LockTimer)) {

            LogicDawnmaker.StopPlayerLock(player);

            PacketRegistry.sendToAllClients(new AnimationPacket(player.getUUID(), "stop"));

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                PacketRegistry.sendToPlayer(new LockTimerPacket(0), serverPlayer);
            }

            playerTag.remove(LockTimer);
            playerTag.remove(DelayTimer);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack stack = stack(player);
        if (!(stack.getItem() instanceof ItemDawnmaker)) return;

        CompoundTag playerTag = tagPlayerData(player);
        CompoundTag itemTag = tagMainHand(player);
        float damage = event.getAmount();

        if (itemTag.getBoolean(Mode) && damage >= player.getHealth()) {
            event.setCanceled(true);

            player.setHealth(player.getMaxHealth());
            player.removeAllEffects();

            if (stack.getItem() instanceof ItemDawnmaker dawnmaker) {
                dawnmaker.Ultimate(stack, player);

                playerTag.putBoolean(Mode, false);
            }
        }
    }

    public static boolean hasDawnmakerInInventory(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemDawnmaker) {
                return true;
            }
        }
        return false;
    }
}
