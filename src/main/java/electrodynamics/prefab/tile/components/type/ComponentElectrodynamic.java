package electrodynamics.prefab.tile.components.type;

import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

import electrodynamics.api.electricity.CapabilityElectrodynamic;
import electrodynamics.api.electricity.IElectrodynamic;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.Component;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.utilities.UtilitiesTiles;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class ComponentElectrodynamic implements Component, IElectrodynamic {
    private GenericTile holder;

    @Override
    public void holder(GenericTile holder) {
	this.holder = holder;
    }

    public GenericTile getHolder() {
	return holder;
    }

    protected BiFunction<TransferPack, Boolean, TransferPack> functionReceivePower = IElectrodynamic.super::receivePower;
    protected BiFunction<TransferPack, Boolean, TransferPack> functionExtractPower = IElectrodynamic.super::extractPower;
    protected Consumer<Double> setJoules = null;
    protected HashSet<Direction> relativeOutputDirections = new HashSet<>();
    protected HashSet<Direction> relativeInputDirections = new HashSet<>();
    protected HashSet<Direction> outputDirections = new HashSet<>();
    protected HashSet<Direction> inputDirections = new HashSet<>();
    protected double voltage = CapabilityElectrodynamic.DEFAULT_VOLTAGE;
    protected double maxJoules = 0;
    protected double joules = 0;
    protected DoubleSupplier getJoules = () -> joules;
    protected BooleanSupplier hasCapability = () -> true;
    private Direction lastReturnedSide = Direction.UP;

    public ComponentElectrodynamic(GenericTile source) {
	holder(source);
	if (holder.hasComponent(ComponentType.PacketHandler)) {
	    ComponentPacketHandler handler = holder.getComponent(ComponentType.PacketHandler);
	    handler.guiPacketWriter(this::writeGuiPacket);
	    handler.guiPacketReader(this::readGuiPacket);
	}
    }

    private void writeGuiPacket(CompoundNBT nbt) {
	nbt.putDouble("voltage", voltage);
	nbt.putDouble("maxJoules", maxJoules);
	nbt.putDouble("joules", joules);
    }

    private void readGuiPacket(CompoundNBT nbt) {
	voltage = nbt.getDouble("voltage");
	maxJoules = nbt.getDouble("maxJoules");
	joules = nbt.getDouble("joules");
    }

    @Override
    public void loadFromNBT(BlockState state, CompoundNBT nbt) {
	joules = nbt.getDouble("joules");
    }

    @Override
    public void saveToNBT(CompoundNBT nbt) {
	nbt.putDouble("joules", joules);
    }

    @Override
    public double getVoltage() {
	return voltage;
    }

    @Override
    @Deprecated
    public void setJoulesStored(double joules) {
	joules(joules);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, Direction side) {
	lastReturnedSide = side;
	if (capability != CapabilityElectrodynamic.ELECTRODYNAMIC || !hasCapability.getAsBoolean()) {
	    return false;
	}
	if (side == null || inputDirections.contains(side) || outputDirections.contains(side)) {
	    return true;
	}
	Direction dir = holder.hasComponent(ComponentType.Direction) ? holder.<ComponentDirection>getComponent(ComponentType.Direction).getDirection()
		: null;
	if (dir != null) {
	    return relativeInputDirections.contains(UtilitiesTiles.getRelativeSide(dir, side))
		    || relativeOutputDirections.contains(UtilitiesTiles.getRelativeSide(dir, side));
	}
	return false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
	lastReturnedSide = side;
	return hasCapability(capability, side) ? (LazyOptional<T>) LazyOptional.of(() -> this) : LazyOptional.empty();
    }

    @Override
    public TransferPack extractPower(TransferPack transfer, boolean debug) {
	if (outputDirections.contains(lastReturnedSide) || holder.hasComponent(ComponentType.Direction) && relativeOutputDirections.contains(
		UtilitiesTiles.getRelativeSide(holder.<ComponentDirection>getComponent(ComponentType.Direction).getDirection(), lastReturnedSide))) {
	    return functionExtractPower.apply(transfer, debug);
	}
	return TransferPack.EMPTY;
    }

    @Override
    public TransferPack receivePower(TransferPack transfer, boolean debug) {
	if (inputDirections.contains(lastReturnedSide) || holder.hasComponent(ComponentType.Direction) && relativeInputDirections.contains(
		UtilitiesTiles.getRelativeSide(holder.<ComponentDirection>getComponent(ComponentType.Direction).getDirection(), lastReturnedSide))) {
	    return functionReceivePower.apply(transfer, debug);
	}
	return TransferPack.EMPTY;
    }

    public ComponentElectrodynamic joules(double joules) {
	if (setJoules != null) {
	    setJoules.accept(joules);
	} else {
	    this.joules = Math.max(0, Math.min(maxJoules, joules));
	}
	return this;
    }

    public ComponentElectrodynamic maxJoules(double maxJoules) {
	this.maxJoules = Math.max(maxJoules, 0);
	if (joules > maxJoules) {
	    joules = maxJoules;
	}
	return this;
    }

    public ComponentElectrodynamic enableUniversalInput() {
	for (Direction dir : Direction.values()) {
	    input(dir);
	}
	return this;
    }

    public ComponentElectrodynamic input(Direction dir) {
	inputDirections.add(dir);
	return this;
    }

    public ComponentElectrodynamic output(Direction dir) {
	outputDirections.add(dir);
	return this;
    }

    public ComponentElectrodynamic relativeInput(Direction dir) {
	relativeInputDirections.add(dir);
	return this;
    }

    public ComponentElectrodynamic relativeOutput(Direction dir) {
	relativeOutputDirections.add(dir);
	return this;
    }

    public ComponentElectrodynamic receivePower(BiFunction<TransferPack, Boolean, TransferPack> receivePower) {
	functionReceivePower = receivePower;
	return this;
    }

    public ComponentElectrodynamic extractPower(BiFunction<TransferPack, Boolean, TransferPack> extractPower) {
	functionExtractPower = extractPower;
	return this;
    }

    public ComponentElectrodynamic setJoules(Consumer<Double> setJoules) {
	this.setJoules = setJoules;
	return this;
    }

    public ComponentElectrodynamic getJoules(DoubleSupplier getJoules) {
	this.getJoules = getJoules;
	return this;
    }

    public ComponentElectrodynamic voltage(double voltage) {
	this.voltage = voltage;
	return this;
    }

    public ComponentElectrodynamic drainElectricItem(int slot) {
	if (holder.hasComponent(ComponentType.Inventory)) {
	    ComponentInventory inventory = holder.getComponent(ComponentType.Inventory);
	    ItemStack stack = inventory.getStackInSlot(slot);
	    if (stack.getItem() instanceof ItemElectric) {
		ItemElectric el = (ItemElectric) stack.getItem();
		receivePower(el.extractPower(stack, maxJoules - joules, false), false);
	    }
	}
	return this;
    }

    public ComponentElectrodynamic fillElectricItem(int slot) {
	if (holder.hasComponent(ComponentType.Inventory)) {
	    ComponentInventory inventory = holder.getComponent(ComponentType.Inventory);
	    ItemStack stack = inventory.getStackInSlot(slot);
	    if (stack.getItem() instanceof ItemElectric) {
		ItemElectric el = (ItemElectric) stack.getItem();
		extractPower(el.receivePower(stack, TransferPack.joulesVoltage(joules, voltage), false), false);
	    }
	}
	return this;
    }

    @Override
    public double getJoulesStored() {
	return getJoules.getAsDouble();
    }

    @Override
    public double getMaxJoulesStored() {
	return maxJoules;
    }

    @Override
    public void overVoltage(TransferPack transfer) {
	World world = holder.getWorld();
	BlockPos pos = holder.getPos();
	world.setBlockState(pos, Blocks.AIR.getDefaultState());
	world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), (float) Math.log10(10 + transfer.getVoltage() / getVoltage()), Mode.DESTROY);
    }

    @Override
    public ComponentType getType() {
	return ComponentType.Electrodynamic;
    }

    public ComponentElectrodynamic setCapabilityTest(BooleanSupplier test) {
	hasCapability = test;
	return this;
    }
}
