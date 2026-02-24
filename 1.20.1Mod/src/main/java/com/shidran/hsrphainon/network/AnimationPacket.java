package com.shidran.hsrphainon.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.UUID;
import java.util.function.Supplier;

public class AnimationPacket {
    public final UUID playerId;
    public final String animationId;

    public AnimationPacket(UUID playerId, String animationId) {
        this.playerId = playerId;
        this.animationId = animationId;
    }

    public static void encode(AnimationPacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId);
        buf.writeUtf(msg.animationId);
    }

    public static AnimationPacket decode(FriendlyByteBuf buf) {
        return new AnimationPacket(buf.readUUID(), buf.readUtf());
    }

    public static void handle(AnimationPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    AnimationEvent.handlePacket(msg.playerId, msg.animationId));
        });
        ctx.get().setPacketHandled(true);
    }
}