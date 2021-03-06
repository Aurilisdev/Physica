package electrodynamics.common.tile.network;

import electrodynamics.DeferredRegisters;
import electrodynamics.common.block.connect.BlockPipe;
import electrodynamics.common.block.subtype.SubtypePipe;
import electrodynamics.common.tile.generic.GenericTilePipe;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

public class TilePipe extends GenericTilePipe {
    public double transmit = 0;

    public TilePipe() {
	super(DeferredRegisters.TILE_PIPE.get());
    }

    public SubtypePipe pipe = null;

    @Override
    public SubtypePipe getPipeType() {
	if (pipe == null) {
	    pipe = ((BlockPipe) getBlockState().getBlock()).pipe;
	}
	return pipe;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
	compound.putInt("ord", getPipeType().ordinal());
	return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
	super.read(state, compound);
	pipe = SubtypePipe.values()[compound.getInt("ord")];
    }

    @Override
    protected void writeCustomPacket(CompoundNBT nbt) {
	nbt.putDouble("transmit", transmit);
    }

    @Override
    protected void readCustomPacket(CompoundNBT nbt) {
	transmit = nbt.getDouble("transmit");
    }
}
