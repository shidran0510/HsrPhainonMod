package com.shidran.hsrphainon.client.event;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import com.shidran.hsrphainon.network.ExecutePacket;
import com.shidran.hsrphainon.registry.KeyBindRegistry;
import com.shidran.hsrphainon.registry.PacketRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.Ultimate;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != 0) {
            int keyCode = event.getKey();
            int scanCode = event.getScanCode();

            if (KeyBindRegistry.hsrphainonKey[BasicATK].matches(keyCode, scanCode)) {
                PacketRegistry.sendToServer(new ExecutePacket(BasicATK));
            } else if (KeyBindRegistry.hsrphainonKey[Skill1].matches(keyCode, scanCode)) {
                PacketRegistry.sendToServer(new ExecutePacket(Skill1));
            } else if (KeyBindRegistry.hsrphainonKey[Skill2].matches(keyCode, scanCode)) {
                PacketRegistry.sendToServer(new ExecutePacket(Skill2));
            } else if (KeyBindRegistry.hsrphainonKey[Ultimate].matches(keyCode, scanCode)) {
                PacketRegistry.sendToServer(new ExecutePacket(Ultimate));
            }
        }
    }

    @SubscribeEvent
    public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();

        if (mc.player == null) return;

        if (event.isAttack()) {
            ItemStack stack = mc.player.getMainHandItem();

            if (tagPlayerData(mc.player).getInt(LockTimer) > 0) {
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }

    @SubscribeEvent
    public static void onMovementInput(net.minecraftforge.client.event.MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        ItemStack stack = stack(player);

        if (stack.getItem() instanceof ItemDawnmaker && stack.hasTag()) {
            if (tagPlayerData(player).getInt(LockTimer) > 0) {
                net.minecraft.client.player.Input input = event.getInput();

                input.forwardImpulse = 0;
                input.leftImpulse = 0;
                input.up = false;
                input.down = false;
                input.left = false;
                input.right = false;
                input.jumping = false;
                input.shiftKeyDown = false;
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack stack = mc.player.getMainHandItem();
        if (tagPlayerData(mc.player).getInt(LockTimer) > 0) {
            while (mc.options.keyDrop.consumeClick());
        }
    }

    @SubscribeEvent
    public static void onKeyInputExtra(InputEvent.Key event) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null) return;

        if (tagPlayerData(mc.player).getInt(LockTimer) > 0) {
            int keyCode = event.getKey();
            int scanCode = event.getScanCode();

            for (int i = 0; i < 9; i++) {
                if (mc.options.keyHotbarSlots[i].matches(keyCode, scanCode)) {
                    while (mc.options.keyHotbarSlots[i].consumeClick());
                }
            }

            if (mc.options.keySwapOffhand.matches(keyCode, scanCode)) {
                while (mc.options.keySwapOffhand.consumeClick());
            }

            if (mc.options.keyInventory.matches(keyCode, scanCode)) {
                while (mc.options.keyInventory.consumeClick());
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player != null && tagPlayerData(mc.player).getInt(LockTimer) > 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickEmpty(InputEvent.InteractionKeyMappingTriggered event) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null) return;

        if (event.isUseItem() && tagPlayerData(mc.player).getInt(LockTimer) > 0) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }
}
