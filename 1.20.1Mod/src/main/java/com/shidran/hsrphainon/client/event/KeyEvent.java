package com.shidran.hsrphainon.client.event;

import com.shidran.hsrphainon.HsrPhainon;
import com.shidran.hsrphainon.item.ItemDawnmaker;
import com.shidran.hsrphainon.network.ExecutePacket;
import com.shidran.hsrphainon.registry.KeyBindRegistry;
import com.shidran.hsrphainon.registry.PacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;
import static com.shidran.hsrphainon.common.HsrPhainonConstants.Ultimate;

// クライアント側キー入力イベント
@Mod.EventBusSubscriber(modid = HsrPhainon.MOD_ID, value = Dist.CLIENT)
public class KeyEvent {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() == 1) {
            if (KeyBindRegistry.hsrphainonKey[BasicATK].consumeClick()) {
                PacketRegistry.sendToServer(new ExecutePacket(BasicATK));
            }
            if (KeyBindRegistry.hsrphainonKey[Skill1].consumeClick()) {
                PacketRegistry.sendToServer(new ExecutePacket(Skill1));
            }
            if (KeyBindRegistry.hsrphainonKey[Skill2].consumeClick()) {
                PacketRegistry.sendToServer(new ExecutePacket(Skill2));
            }
            if (KeyBindRegistry.hsrphainonKey[Ultimate].consumeClick()) {
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

            if (stack.getItem() instanceof ItemDawnmaker && stack.hasTag() && stack.getTag().getInt("LockTimer") > 0) {
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }

    @SubscribeEvent
    public static void onMovementInput(net.minecraftforge.client.event.MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof ItemDawnmaker && stack.hasTag()) {
            if (stack.getTag().getInt("LockTimer") > 0) {
                net.minecraft.client.player.Input input = event.getInput();

                event.getInput().forwardImpulse = 0;
                event.getInput().leftImpulse = 0;
                event.getInput().up = false;
                event.getInput().down = false;
                event.getInput().left = false;
                event.getInput().right = false;
                event.getInput().jumping = false;
                event.getInput().shiftKeyDown = false;
            }
        }
    }

}
