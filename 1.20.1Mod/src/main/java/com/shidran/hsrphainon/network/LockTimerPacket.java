package com.shidran.hsrphainon.network;

import com.shidran.hsrphainon.common.HsrPhainonConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record LockTimerPacket (int ticks) {
    public static void encode(LockTimerPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.ticks);
    }

    public static LockTimerPacket decode(FriendlyByteBuf buf) {
        return new LockTimerPacket(buf.readInt());
    }

    public static void handle(LockTimerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // クライアント側で実行される処理
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
                if (player != null) {
                    // クライアントのPlayerDataに値をセットする
                    player.getPersistentData().putInt(HsrPhainonConstants.LockTimer, msg.ticks);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
