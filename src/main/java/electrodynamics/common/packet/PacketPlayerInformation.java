package electrodynamics.common.packet;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketPlayerInformation {

    private String information;

    public PacketPlayerInformation() {
	String actual = "";
	List<ModInfo> total = FMLLoader.getLoadingModList().getMods();
	for (ModInfo info : total) {
	    actual += info.getModId() + ":";
	}
	for (ResourcePackInfo pack : Minecraft.getInstance().getResourcePackList().getAllPacks()) {
	    actual += pack.getName() + "," + pack.getTitle().getString() + "," + pack.getDescription() + ":";
	}
	information = actual;
    }

    public PacketPlayerInformation(String info) {
	information = info;
    }

    public static void handle(PacketPlayerInformation message, Supplier<Context> context) {
	Context ctx = context.get();
	ctx.enqueueWork(() -> NetworkHandler.playerInformation.put(context.get().getSender().getName().getString(), message.information));
	ctx.setPacketHandled(true);
    }

    public static void encode(PacketPlayerInformation pkt, PacketBuffer buf) {
	buf.writeString(pkt.information);
    }

    public static PacketPlayerInformation decode(PacketBuffer buf) {
	return new PacketPlayerInformation(buf.readString(999999));
    }
}