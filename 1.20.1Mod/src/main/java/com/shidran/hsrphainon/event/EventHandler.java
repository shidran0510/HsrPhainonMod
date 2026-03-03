package com.shidran.hsrphainon.event;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import com.shidran.hsrphainon.item.LogicDawnmaker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.Mode;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }

        Player player = event.player;

        boolean hasSword = hasDawnmakerInInventory(player);

        if (!hasSword && player.isNoGravity()) {
            LogicDawnmaker.StopPlayerLock(player);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack stack = stack(player);
        if (!(stack.getItem() instanceof ItemDawnmaker)) return;

        CompoundTag tag = tag(stack);

        float damage = event.getAmount();

        if (tag.getBoolean(Mode) && damage >= player.getHealth()) {
            event.setCanceled(true);

            player.setHealth(player.getMaxHealth());
            player.removeAllEffects();

            if (stack.getItem() instanceof ItemDawnmaker dawnmaker) {
                dawnmaker.Ultimate(stack, player);

                tag.putBoolean(Mode, false);
            }
        }
    }

    private static boolean hasDawnmakerInInventory(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemDawnmaker) {
                return true;
            }
        }
        return false;
    }
}
