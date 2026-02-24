package com.shidran.hsrphainon.network;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class AnimationEvent {
    public static void handlePacket(UUID playerId, String animationId) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        var player = level.getPlayerByUUID(playerId);

        if (player instanceof AbstractClientPlayer clientPlayer) {
            // 1. AssociatedData から登録したレイヤーを取得
            var data = PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer);
            var layer = data.get(ResourceLocation.fromNamespaceAndPath("hsrphainon", animationId));

            if (layer instanceof ModifierLayer<?> modifierLayer) {
                // 2. アニメーションをレジストリから取得
                var anim = PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath("hsrphainon", animationId));

                if (anim != null) {
                    // 3. 再生
                    ((ModifierLayer<IAnimation>) modifierLayer).setAnimation(new KeyframeAnimationPlayer(anim));
                }
            }
        }
    }

    private static void playOnLayer(AnimationStack stack, dev.kosmx.playerAnim.core.data.KeyframeAnimation anim) {
    }
}
