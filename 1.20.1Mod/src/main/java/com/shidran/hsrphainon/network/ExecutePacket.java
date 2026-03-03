package com.shidran.hsrphainon.network;

import com.shidran.hsrphainon.item.ItemDawnmaker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.shidran.hsrphainon.common.HsrPhainonConstants.*;

public class ExecutePacket {
    private final int type;

    public ExecutePacket(int type) {
        this.type = type;
    }

    public ExecutePacket(FriendlyByteBuf buf) {
        this.type = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(type);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ItemStack stack = stack(player);

                if (stack.getItem() instanceof ItemDawnmaker dawnmaker) {

                    switch (this.type) {
                        case BasicATK:
                            dawnmaker.BasicATK(stack,player);
                            break;
                        case Skill1:
                            dawnmaker.Skill1(stack, player);
                            break;
                        case Skill2:
                            dawnmaker.Skill2(stack, player);
                            break;
                        case Ultimate:
                            dawnmaker.Ultimate(stack, player);
                            break;
                        default:
            }
        }
        context.setPacketHandled(true);
    }
        });
    }
}
