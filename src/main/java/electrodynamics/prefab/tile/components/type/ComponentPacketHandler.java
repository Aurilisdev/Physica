package electrodynamics.prefab.tile.components.type;

import java.util.function.Consumer;

import electrodynamics.common.packet.NetworkHandler;
import electrodynamics.common.packet.PacketUpdateTile;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.Component;
import electrodynamics.prefab.tile.components.ComponentType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

public class ComponentPacketHandler implements Component {
    private GenericTile holder;

    @Override
    public void holder(GenericTile holder) {
	this.holder = holder;
    }

    protected Consumer<CompoundNBT> customPacketWriter;
    protected Consumer<CompoundNBT> guiPacketWriter;
    protected Consumer<CompoundNBT> customPacketReader;
    protected Consumer<CompoundNBT> guiPacketReader;

    public ComponentPacketHandler customPacketWriter(Consumer<CompoundNBT> consumer) {
	Consumer<CompoundNBT> safe = consumer;
	if (customPacketWriter != null) {
	    safe = safe.andThen(customPacketWriter);
	}
	customPacketWriter = safe;
	return this;
    }

    public ComponentPacketHandler guiPacketWriter(Consumer<CompoundNBT> consumer) {
	Consumer<CompoundNBT> safe = consumer;
	if (guiPacketWriter != null) {
	    safe = safe.andThen(guiPacketWriter);
	}
	guiPacketWriter = safe;
	return this;
    }

    public ComponentPacketHandler customPacketReader(Consumer<CompoundNBT> consumer) {
	Consumer<CompoundNBT> safe = consumer;
	if (customPacketReader != null) {
	    safe = safe.andThen(customPacketReader);
	}
	customPacketReader = safe;
	return this;
    }

    public ComponentPacketHandler guiPacketReader(Consumer<CompoundNBT> consumer) {
	Consumer<CompoundNBT> safe = consumer;
	if (guiPacketReader != null) {
	    safe = safe.andThen(guiPacketReader);
	}
	guiPacketReader = safe;
	return this;
    }

    public Consumer<CompoundNBT> getCustomPacketSupplier() {
	return customPacketWriter;
    }

    public Consumer<CompoundNBT> getGuiPacketSupplier() {
	return guiPacketWriter;
    }

    public Consumer<CompoundNBT> getCustomPacketConsumer() {
	return customPacketReader;
    }

    public Consumer<CompoundNBT> getGuiPacketConsumer() {
	return guiPacketReader;
    }

    public void sendCustomPacket() {
	PacketUpdateTile packet = new PacketUpdateTile(this, holder.getPos(), false, new CompoundNBT());
	World world = holder.getWorld();
	BlockPos pos = holder.getPos();
	if (world instanceof ServerWorld) {
	    ((ServerWorld) world).getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false)
		    .forEach(p -> NetworkHandler.CHANNEL.sendTo(packet, p.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
	}
    }

    public void sendGuiPacketToTracking() {
	PacketUpdateTile packet = new PacketUpdateTile(this, holder.getPos(), true, new CompoundNBT());
	World world = holder.getWorld();
	BlockPos pos = holder.getPos();
	if (world instanceof ServerWorld) {
	    ((ServerWorld) world).getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false)
		    .forEach(p -> NetworkHandler.CHANNEL.sendTo(packet, p.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
	}
    }

    @Override
    public ComponentType getType() {
	return ComponentType.PacketHandler;
    }
}
