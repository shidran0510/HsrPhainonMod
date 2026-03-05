package com.shidran.hsrphainon.network;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.MOD_ID;

public class AnimationEvent {
    public static void handlePacket(UUID playerId, String animationId) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        var player = level.getPlayerByUUID(playerId);

        if (player instanceof AbstractClientPlayer clientPlayer) {
            var data = PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer);

            if ("stop".equals(animationId)) {
                for (String id : com.shidran.hsrphainon.common.HsrPhainonConstants.animID) {
                    var layer = data.get(ResourceLocation.fromNamespaceAndPath(MOD_ID, id));
                    if (layer instanceof ModifierLayer<?> modifierLayer) {
                        modifierLayer.setAnimation(null);
                    }
                }
                Minecraft.getInstance().getSoundManager().stop(null, net.minecraft.sounds.SoundSource.PLAYERS);
                return;
            }

            var layer = data.get(ResourceLocation.fromNamespaceAndPath(MOD_ID, animationId));
            if (layer instanceof ModifierLayer<?> modifierLayer) {
                var anim = PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(MOD_ID, animationId));

                if (anim != null) {
                    ((ModifierLayer<IAnimation>) modifierLayer).setAnimation(new KeyframeAnimationPlayer(anim));
                }
            }
        }
    }
}
