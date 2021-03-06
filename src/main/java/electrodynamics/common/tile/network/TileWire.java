package electrodynamics.common.tile.network;

import electrodynamics.DeferredRegisters;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.tile.generic.GenericTileWire;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class TileWire extends GenericTileWire {
    public double transmit = 0;

    public TileWire() {
	super(DeferredRegisters.TILE_WIRE.get());
    }

    public TileWire(TileEntityType<TileLogisticalWire> tileEntityType) {
	super(tileEntityType);
    }

    public SubtypeWire wire = null;

    @Override
    public SubtypeWire getWireType() {
	if (wire == null) {
	    wire = ((BlockWire) getBlockState().getBlock()).wire;
	}
	return wire;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
	compound.putInt("ord", getWireType().ordinal());
	return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
	super.read(state, compound);
	wire = SubtypeWire.values()[compound.getInt("ord")];
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
