package electrodynamics.common.block.subtype;

import electrodynamics.api.ISubtype;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.tile.TileAdvancedSolarPanel;
import electrodynamics.common.tile.TileBatteryBox;
import electrodynamics.common.tile.TileChemicalCrystallizer;
import electrodynamics.common.tile.TileChemicalMixer;
import electrodynamics.common.tile.TileCircuitBreaker;
import electrodynamics.common.tile.TileCoalGenerator;
import electrodynamics.common.tile.TileCombustionChamber;
import electrodynamics.common.tile.TileElectricFurnace;
import electrodynamics.common.tile.TileElectricPump;
import electrodynamics.common.tile.TileFermentationPlant;
import electrodynamics.common.tile.TileHydroelectricGenerator;
import electrodynamics.common.tile.TileMineralCrusher;
import electrodynamics.common.tile.TileMineralGrinder;
import electrodynamics.common.tile.TileMineralWasher;
import electrodynamics.common.tile.TileOxidationFurnace;
import electrodynamics.common.tile.TileSolarPanel;
import electrodynamics.common.tile.TileTeleporter;
import electrodynamics.common.tile.TileThermoelectricGenerator;
import electrodynamics.common.tile.TileTransformer;
import electrodynamics.common.tile.TileWindmill;
import electrodynamics.common.tile.TileWireMill;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public enum SubtypeMachine implements ISubtype {
    electricfurnace(true, TileElectricFurnace.class), electricfurnacerunning(false, TileElectricFurnace.class),
    // split
    coalgenerator(true, TileCoalGenerator.class), coalgeneratorrunning(false, TileCoalGenerator.class),
    // split
    wiremill(true, TileWireMill.class),
    // split
    mineralcrusher(true, TileMineralCrusher.class, true),
    // split
    mineralgrinder(true, TileMineralGrinder.class, true),
    // split
    batterybox(true, TileBatteryBox.class, true),
    // split
    oxidationfurnace(true, TileOxidationFurnace.class), oxidationfurnacerunning(false, TileOxidationFurnace.class),
    // split
    downgradetransformer(true, TileTransformer.class, false, VoxelShapes.create(0, 0, 0, 1, 15.0 / 16.0, 1)),
    // split
    upgradetransformer(true, TileTransformer.class, false, VoxelShapes.create(0, 0, 0, 1, 15.0 / 16.0, 1)),
    // split
    solarpanel(true, TileSolarPanel.class, false, VoxelShapes.create(0, 0, 0, 1, 9.0 / 16.0, 1)),
    // split
    advancedsolarpanel(true, TileAdvancedSolarPanel.class),
    // split
    electricpump(true, TileElectricPump.class),
    // split
    thermoelectricgenerator(true, TileThermoelectricGenerator.class),
    // split
    fermentationplant(true, TileFermentationPlant.class),
    // split
    combustionchamber(true, TileCombustionChamber.class),
    // split
    hydroelectricgenerator(true, TileHydroelectricGenerator.class),
    // split
    windmill(true, TileWindmill.class),
    // split
    mineralwasher(true, TileMineralWasher.class),
    // split
    chemicalmixer(true, TileChemicalMixer.class, true),
    // split
    chemicalcrystallizer(true, TileChemicalCrystallizer.class),
    // split
    circuitbreaker(true, TileCircuitBreaker.class),
    // split
    teleporter(true, TileTeleporter.class, true);

    public final Class<? extends TileEntity> tileclass;
    public final boolean showInItemGroup;
    private BlockRenderType type = BlockRenderType.MODEL;
    private VoxelShape customShape = null;

    private SubtypeMachine(boolean showInItemGroup, Class<? extends TileEntity> tileclass) {
	this.showInItemGroup = showInItemGroup;
	this.tileclass = tileclass;
    }

    private SubtypeMachine(boolean showInItemGroup, Class<? extends TileEntity> tileclass, boolean customModel) {
	this(showInItemGroup, tileclass);
	if (customModel) {
	    type = BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
    }

    private SubtypeMachine(boolean showInItemGroup, Class<? extends TileEntity> tileclass, boolean customModel, VoxelShape shape) {
	this(showInItemGroup, tileclass, customModel);
	customShape = shape;
    }

    public BlockRenderType getRenderType() {
	return type;
    }

    public static boolean shouldBreakOnReplaced(BlockState before, BlockState after) {
	Block bb = before.getBlock();
	Block ba = after.getBlock();
	if (bb instanceof BlockMachine && ba instanceof BlockMachine) {
	    SubtypeMachine mb = ((BlockMachine) bb).machine;
	    SubtypeMachine ma = ((BlockMachine) ba).machine;
	    if (mb == electricfurnace && ma == electricfurnacerunning || mb == electricfurnacerunning && ma == electricfurnace
		    || mb == coalgenerator && ma == coalgeneratorrunning || mb == coalgeneratorrunning && ma == coalgenerator
		    || mb == oxidationfurnace && ma == oxidationfurnacerunning || mb == oxidationfurnacerunning && ma == oxidationfurnace) {
		return false;
	    }
	}
	return true;
    }

    public TileEntity createTileEntity() {
	if (tileclass != null) {
	    try {
		return tileclass.newInstance();
	    } catch (InstantiationException | IllegalAccessException e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    @Override
    public String tag() {
	return name();
    }

    @Override
    public String forgeTag() {
	return tag();
    }

    @Override
    public boolean isItem() {
	return false;
    }

    public VoxelShape getCustomShape() {
	return customShape;
    }
}
