package electrodynamics.common.tile;

import electrodynamics.DeferredRegisters;
import electrodynamics.SoundRegister;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.common.network.ElectricityUtilities;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTileTicking;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileHydroelectricGenerator extends GenericTileTicking {
    protected CachedTileOutput output;
    public boolean isGenerating = false;
    public boolean directionFlag = false;
    public double savedTickRotation;
    public double rotationSpeed;

    public TileHydroelectricGenerator() {
	super(DeferredRegisters.TILE_HYDROELECTRICGENERATOR.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentTickable().tickServer(this::tickServer).tickCommon(this::tickCommon).tickClient(this::tickClient));
	addComponent(new ComponentPacketHandler().guiPacketReader(this::readNBT).guiPacketWriter(this::writeNBT));
	addComponent(new ComponentElectrodynamic(this).relativeOutput(Direction.NORTH));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
	ComponentDirection direction = getComponent(ComponentType.Direction);
	Direction facing = direction.getDirection();
	return super.getRenderBoundingBox().expand(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
    }

    protected void tickServer(ComponentTickable tickable) {
	ComponentDirection direction = getComponent(ComponentType.Direction);
	Direction facing = direction.getDirection();
	if (output == null) {
	    output = new CachedTileOutput(world, pos.offset(facing.getOpposite()));
	}
	ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
	if (tickable.getTicks() % 20 == 0) {
	    BlockPos shift = pos.offset(facing);
	    BlockState onShift = world.getBlockState(shift);
	    isGenerating = onShift.getFluidState().getFluid() == Fluids.FLOWING_WATER;
	    if (isGenerating && onShift.getBlock() instanceof FlowingFluidBlock) {
		int amount = world.getBlockState(shift).get(FlowingFluidBlock.LEVEL);
		shift = pos.offset(facing).offset(facing.rotateY());
		onShift = world.getBlockState(shift);
		if (onShift.getBlock() instanceof FlowingFluidBlock) {
		    if (amount > onShift.get(FlowingFluidBlock.LEVEL)) {
			directionFlag = true;
		    } else {
			shift = pos.offset(facing).offset(facing.rotateY().getOpposite());
			onShift = world.getBlockState(shift);
			if (onShift.getBlock() instanceof FlowingFluidBlock) {
			    if (amount >= onShift.get(FlowingFluidBlock.LEVEL)) {
				directionFlag = false;
			    } else {
				isGenerating = false;
			    }
			} else {
			    isGenerating = false;
			}
		    }
		} else {
		    shift = pos.offset(facing).offset(facing.rotateY().getOpposite());
		    onShift = world.getBlockState(shift);
		    if (onShift.getBlock() instanceof FlowingFluidBlock) {
			if (amount >= onShift.get(FlowingFluidBlock.LEVEL)) {
			    directionFlag = false;
			} else {
			    isGenerating = false;
			}
		    } else {
			isGenerating = false;
		    }
		}
	    }
	    this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
	    output.update();
	}
	if (isGenerating && output.valid()) {
	    ElectricityUtilities.receivePower(output.getSafe(), facing,
		    TransferPack.ampsVoltage(Constants.HYDROELECTRICGENERATOR_AMPERAGE, electro.getVoltage()), false);
	}
    }

    protected void tickCommon(ComponentTickable tickable) {
	savedTickRotation += (directionFlag ? 1 : -1) * rotationSpeed;
	rotationSpeed = MathHelper.clamp(rotationSpeed + 0.05 * (isGenerating ? 1 : -1), 0.0, 1.0);
    }

    protected void tickClient(ComponentTickable tickable) {
	if (isGenerating && world.rand.nextDouble() < 0.3) {
	    Direction direction = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
	    double d4 = world.rand.nextDouble();
	    double d5 = direction.getAxis() == Direction.Axis.X ? direction.getXOffset() * (direction.getXOffset() == -1 ? 0.2D : 1.2D) : d4;
	    double d6 = world.rand.nextDouble();
	    double d7 = direction.getAxis() == Direction.Axis.Z ? direction.getZOffset() * (direction.getZOffset() == -1 ? 0.2D : 1.2D) : d4;
	    world.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, pos.getX() + d5, pos.getY() + d6, pos.getZ() + d7, 0.0D, 0.0D, 0.0D);
	}
	if (isGenerating && tickable.getTicks() % 100 == 0) {
	    SoundAPI.playSound(SoundRegister.SOUND_HYDROELECTRICGENERATOR.get(), SoundCategory.BLOCKS, 1, 1, pos);
	}
    }

    protected void writeNBT(CompoundNBT nbt) {
	nbt.putBoolean("isGenerating", isGenerating);
	nbt.putBoolean("directionFlag", directionFlag);
    }

    protected void readNBT(CompoundNBT nbt) {
	isGenerating = nbt.getBoolean("isGenerating");
	directionFlag = nbt.getBoolean("directionFlag");
    }
}
