package com.shidran.hsrphainon.registry;

import com.shidran.hsrphainon.network.AnimationPacket;
import com.shidran.hsrphainon.network.ExecutePacket;
import com.shidran.hsrphainon.network.LockTimerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.MOD_ID;

public class PacketRegistry {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++; }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(ExecutePacket.class, id())
                .decoder(ExecutePacket::new)
                .encoder(ExecutePacket::encode)
                .consumerMainThread(ExecutePacket::handle)
                .add();

        INSTANCE.messageBuilder(AnimationPacket.class, id())
                .decoder(AnimationPacket::decode)
                .encoder(AnimationPacket::encode)
                .consumerMainThread(AnimationPacket::handle)
                .add();

        INSTANCE.registerMessage(id(),
                LockTimerPacket.class,
                LockTimerPacket::encode,
                LockTimerPacket::decode,
                LockTimerPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToAllClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
    public static <MSG> void sendToPlayer(MSG message, net.minecraft.server.level.ServerPlayer player) {
        INSTANCE.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}