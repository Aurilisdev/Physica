package electrodynamics.prefab.tile.components.type;

import electrodynamics.common.block.BlockGenericMachine;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.Component;
import electrodynamics.prefab.tile.components.ComponentType;
import net.minecraft.util.Direction;

public class ComponentDirection implements Component {
    private GenericTile holder;
    private Direction cachedDirection = null;
    private long last = 0;

    @Override
    public void holder(GenericTile holder) {
	this.holder = holder;
    }

    public Direction getDirection() {
	if (System.currentTimeMillis() - last > 10000 || cachedDirection == null) {
	    if (holder.getBlockState().hasProperty(BlockGenericMachine.FACING)) {
		cachedDirection = holder.getBlockState().get(BlockGenericMachine.FACING);
	    }
	    last = System.currentTimeMillis();
	}
	if (cachedDirection == null) {
	    return Direction.UP;
	}
	return cachedDirection;
    }

    @Override
    public ComponentType getType() {
	return ComponentType.Direction;
    }

}
