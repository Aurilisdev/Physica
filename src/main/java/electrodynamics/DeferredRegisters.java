package electrodynamics;

import java.util.HashMap;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

import electrodynamics.api.ISubtype;
import electrodynamics.api.References;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.block.BlockMultiSubnode;
import electrodynamics.common.block.BlockOre;
import electrodynamics.common.block.connect.BlockPipe;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypePipe;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.blockitem.BlockItemDescriptable;
import electrodynamics.common.blockitem.BlockItemWire;
import electrodynamics.common.fluid.FluidEthanol;
import electrodynamics.common.fluid.FluidMineral;
import electrodynamics.common.fluid.FluidSulfuricAcid;
import electrodynamics.common.inventory.container.ContainerBatteryBox;
import electrodynamics.common.inventory.container.ContainerChemicalCrystallizer;
import electrodynamics.common.inventory.container.ContainerChemicalMixer;
import electrodynamics.common.inventory.container.ContainerCoalGenerator;
import electrodynamics.common.inventory.container.ContainerDO2OProcessor;
import electrodynamics.common.inventory.container.ContainerElectricFurnace;
import electrodynamics.common.inventory.container.ContainerFermentationPlant;
import electrodynamics.common.inventory.container.ContainerMineralWasher;
import electrodynamics.common.inventory.container.ContainerO2OProcessor;
import electrodynamics.common.item.ItemFrequencyCard;
import electrodynamics.common.item.ItemMultimeter;
import electrodynamics.common.item.ItemProcessorUpgrade;
import electrodynamics.common.item.ItemWrench;
import electrodynamics.common.item.subtype.SubtypeCeramic;
import electrodynamics.common.item.subtype.SubtypeCircuit;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeGear;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeMineralFluid;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.item.subtype.SubtypePlate;
import electrodynamics.common.item.subtype.SubtypeProcessorUpgrade;
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
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.common.tile.TileOxidationFurnace;
import electrodynamics.common.tile.TileSolarPanel;
import electrodynamics.common.tile.TileTeleporter;
import electrodynamics.common.tile.TileThermoelectricGenerator;
import electrodynamics.common.tile.TileTransformer;
import electrodynamics.common.tile.TileWindmill;
import electrodynamics.common.tile.TileWireMill;
import electrodynamics.common.tile.network.TileLogisticalWire;
import electrodynamics.common.tile.network.TilePipe;
import electrodynamics.common.tile.network.TileWire;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DeferredRegisters {
    public static final HashMap<ISubtype, Item> SUBTYPEITEM_MAPPINGS = new HashMap<>();
    public static final HashMap<Item, ISubtype> ITEMSUBTYPE_MAPPINGS = new HashMap<>();
    public static final HashMap<ISubtype, Block> SUBTYPEBLOCK_MAPPINGS = new HashMap<>();
    public static final HashMap<ISubtype, RegistryObject<Block>> SUBTYPEBLOCKREGISTER_MAPPINGS = new HashMap<>();
    public static HashMap<ISubtype, FluidMineral> SUBTYPEMINERALFLUID_MAPPINGS = new HashMap<>();
    public static HashMap<FluidMineral, ISubtype> MINERALFLUIDSUBTYPE_MAPPINGS = new HashMap<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, References.ID);
    public static BlockMultiSubnode multi = new BlockMultiSubnode();
    public static FluidEthanol fluidEthanol;
    public static FluidSulfuricAcid fluidSulfuricAcid;
    static {
	for (SubtypeOre subtype : SubtypeOre.values()) {
	    SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(new BlockOre(subtype), subtype)));
	}
	for (SubtypeMachine subtype : SubtypeMachine.values()) {
	    SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(new BlockMachine(subtype), subtype)));
	}
	for (SubtypeWire subtype : SubtypeWire.values()) {
	    SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(new BlockWire(subtype), subtype)));
	}
	for (SubtypePipe subtype : SubtypePipe.values()) {
	    SUBTYPEBLOCKREGISTER_MAPPINGS.put(subtype, BLOCKS.register(subtype.tag(), supplier(new BlockPipe(subtype), subtype)));
	}
	FLUIDS.register("fluidethanol", supplier(fluidEthanol = new FluidEthanol()));
	FLUIDS.register("fluidsulfuricacid", supplier(fluidSulfuricAcid = new FluidSulfuricAcid()));
	for (SubtypeMineralFluid mineral : SubtypeMineralFluid.values()) {
	    FluidMineral fluid = new FluidMineral(mineral);
	    SUBTYPEMINERALFLUID_MAPPINGS.put(mineral, fluid);
	    MINERALFLUIDSUBTYPE_MAPPINGS.put(fluid, mineral);
	    FLUIDS.register("fluidmineral" + mineral.name(), supplier(fluid));
	}
    }

    private static void registerSubtypeItem(ISubtype[] array) {
	for (ISubtype subtype : array) {
	    ITEMS.register(subtype.tag(), supplier(new Item(new Item.Properties().group(References.CORETAB)), subtype));
	}
    }

    private static void registerSubtypeBlockItem(ISubtype[] array) {
	for (ISubtype subtype : array) {
	    ITEMS.register(subtype.tag(), supplier(
		    new BlockItemDescriptable(SUBTYPEBLOCK_MAPPINGS.get(subtype), new Item.Properties().group(References.CORETAB)), subtype));
	}
    }

    static {
	registerSubtypeBlockItem(SubtypeOre.values());
	registerSubtypeBlockItem(SubtypeMachine.values());
	for (SubtypeWire subtype : SubtypeWire.values()) {
	    ITEMS.register(subtype.tag(), supplier(
		    new BlockItemWire((BlockWire) SUBTYPEBLOCK_MAPPINGS.get(subtype), new Item.Properties().group(References.CORETAB)), subtype));
	}
	for (SubtypePipe subtype : SubtypePipe.values()) {
	    ITEMS.register(subtype.tag(), supplier(
		    new BlockItemDescriptable(SUBTYPEBLOCK_MAPPINGS.get(subtype), new Item.Properties().group(References.CORETAB)), subtype));
	}
	registerSubtypeItem(SubtypeIngot.values());
	registerSubtypeItem(SubtypeDust.values());
	registerSubtypeItem(SubtypeImpureDust.values());
	registerSubtypeItem(SubtypeCrystal.values());
	registerSubtypeItem(SubtypeOxide.values());
	registerSubtypeItem(SubtypeGear.values());
	registerSubtypeItem(SubtypePlate.values());
	registerSubtypeItem(SubtypeCircuit.values());
	for (SubtypeProcessorUpgrade subtype : SubtypeProcessorUpgrade.values()) {
	    ITEMS.register(subtype.tag(), supplier(new ItemProcessorUpgrade(new Item.Properties().group(References.CORETAB), subtype), subtype));
	}
	registerSubtypeItem(SubtypeCeramic.values());

	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.downgradetransformer),
		"|translate|tooltip.transformer.energyloss");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.upgradetransformer),
		"|translate|tooltip.transformer.energyloss");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.oxidationfurnace),
		"|translate|tooltip.oxidationfurnace.voltage");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.mineralcrusher), "|translate|tooltip.mineralcrusher.voltage");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.oxidationfurnacerunning),
		"|translate|tooltip.oxidationfurnacerunning.voltage");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.advancedsolarpanel),
		"|translate|tooltip.advancedsolarpanel.voltage");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.mineralwasher), "|translate|tooltip.mineralwasher.voltage");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.chemicalmixer), "|translate|tooltip.chemicalmixer.voltage");
	BlockItemDescriptable.addDescription(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.chemicalcrystallizer),
		"|translate|tooltip.chemicalcrystallizer.voltage");
	BLOCKS.register("multisubnode", supplier(multi));
	ITEMS.register("multisubnode", supplier(new BlockItemDescriptable(multi, new Item.Properties())));
    }

    public static final RegistryObject<Item> ITEM_INSULATION = ITEMS.register("insulation",
	    supplier(new Item(new Item.Properties().group(References.CORETAB))));
    public static final RegistryObject<Item> ITEM_MOTOR = ITEMS.register("motor",
	    supplier(new Item(new Item.Properties().group(References.CORETAB))));
    public static final RegistryObject<Item> ITEM_BATTERY = ITEMS.register("battery",
	    supplier(new Item(new Item.Properties().group(References.CORETAB).maxStackSize(1))));
    public static final RegistryObject<Item> ITEM_COIL = ITEMS.register("coil", supplier(new Item(new Item.Properties().group(References.CORETAB))));
    public static final RegistryObject<Item> ITEM_MULTIMETER = ITEMS.register("multimeter",
	    supplier(new ItemMultimeter(new Item.Properties().group(References.CORETAB).maxStackSize(1))));
    public static final RegistryObject<Item> ITEM_WRENCH = ITEMS.register("wrench",
	    supplier(new ItemWrench(new Item.Properties().group(References.CORETAB))));
    public static final RegistryObject<Item> ITEM_SOLARPANELPLATE = ITEMS.register("solarpanelplate",
	    supplier(new Item(new Item.Properties().group(References.CORETAB))));
    public static final RegistryObject<Item> ITEM_FREQUENCYCARD = ITEMS.register("frequencycard",
	    supplier(new ItemFrequencyCard(new Item.Properties().group(References.CORETAB))));
    public static final RegistryObject<TileEntityType<TileCoalGenerator>> TILE_COALGENERATOR = TILES.register(SubtypeMachine.coalgenerator.tag(),
	    () -> new TileEntityType<>(TileCoalGenerator::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.coalgenerator),
		    SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.coalgeneratorrunning)), null));
    public static final RegistryObject<TileEntityType<TileSolarPanel>> TILE_SOLARPANEL = TILES.register(SubtypeMachine.solarpanel.tag(),
	    () -> new TileEntityType<>(TileSolarPanel::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.solarpanel)), null));
    public static final RegistryObject<TileEntityType<TileAdvancedSolarPanel>> TILE_ADVANCEDSOLARPANEL = TILES
	    .register(SubtypeMachine.advancedsolarpanel.tag(), () -> new TileEntityType<>(TileAdvancedSolarPanel::new,
		    Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.advancedsolarpanel)), null));
    public static final RegistryObject<TileEntityType<TileElectricFurnace>> TILE_ELECTRICFURNACE = TILES.register(
	    SubtypeMachine.electricfurnace.tag(),
	    () -> new TileEntityType<>(TileElectricFurnace::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.electricfurnace),
		    SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.electricfurnacerunning)), null));
    public static final RegistryObject<TileEntityType<TileWireMill>> TILE_WIREMILL = TILES.register(SubtypeMachine.wiremill.tag(),
	    () -> new TileEntityType<>(TileWireMill::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.wiremill)), null));
    public static final RegistryObject<TileEntityType<TileMineralGrinder>> TILE_MINERALGRINDER = TILES.register(SubtypeMachine.mineralgrinder.tag(),
	    () -> new TileEntityType<>(TileMineralGrinder::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.mineralgrinder)), null));
    public static final RegistryObject<TileEntityType<TileMineralCrusher>> TILE_MINERALCRUSHER = TILES.register(SubtypeMachine.mineralcrusher.tag(),
	    () -> new TileEntityType<>(TileMineralCrusher::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.mineralcrusher)), null));
    public static final RegistryObject<TileEntityType<TileBatteryBox>> TILE_BATTERYBOX = TILES.register(SubtypeMachine.batterybox.tag(),
	    () -> new TileEntityType<>(TileBatteryBox::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.batterybox)), null));
    public static final RegistryObject<TileEntityType<TileTransformer>> TILE_TRANSFORMER = TILES.register("transformer",
	    () -> new TileEntityType<>(TileTransformer::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.downgradetransformer),
		    SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.upgradetransformer)), null));
    public static final RegistryObject<TileEntityType<TileOxidationFurnace>> TILE_OXIDATIONFURNACE = TILES.register(
	    SubtypeMachine.oxidationfurnace.tag(),
	    () -> new TileEntityType<>(TileOxidationFurnace::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.oxidationfurnace),
		    SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.oxidationfurnacerunning)), null));
    public static final RegistryObject<TileEntityType<TileElectricPump>> TILE_ELECTRICPUMP = TILES.register(SubtypeMachine.electricpump.tag(),
	    () -> new TileEntityType<>(TileElectricPump::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.electricpump)), null));
    public static final RegistryObject<TileEntityType<TileThermoelectricGenerator>> TILE_THERMOELECTRICGENERATOR = TILES
	    .register(SubtypeMachine.thermoelectricgenerator.tag(), () -> new TileEntityType<>(TileThermoelectricGenerator::new,
		    Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.thermoelectricgenerator)), null));
    public static final RegistryObject<TileEntityType<TileHydroelectricGenerator>> TILE_HYDROELECTRICGENERATOR = TILES
	    .register(SubtypeMachine.hydroelectricgenerator.tag(), () -> new TileEntityType<>(TileHydroelectricGenerator::new,
		    Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.hydroelectricgenerator)), null));
    public static final RegistryObject<TileEntityType<TileWindmill>> TILE_WINDMILL = TILES.register(SubtypeMachine.windmill.tag(),
	    () -> new TileEntityType<>(TileWindmill::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.windmill)), null));
    public static final RegistryObject<TileEntityType<TileFermentationPlant>> TILE_FERMENTATIONPLANT = TILES
	    .register(SubtypeMachine.fermentationplant.tag(), () -> new TileEntityType<>(TileFermentationPlant::new,
		    Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.fermentationplant)), null));
    public static final RegistryObject<TileEntityType<TileCombustionChamber>> TILE_COMBUSTIONCHAMBER = TILES
	    .register(SubtypeMachine.combustionchamber.tag(), () -> new TileEntityType<>(TileCombustionChamber::new,
		    Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.combustionchamber)), null));
    public static final RegistryObject<TileEntityType<TileMineralWasher>> TILE_MINERALWASHER = TILES.register(SubtypeMachine.mineralwasher.tag(),
	    () -> new TileEntityType<>(TileMineralWasher::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.mineralwasher)), null));
    public static final RegistryObject<TileEntityType<TileChemicalMixer>> TILE_CHEMICALMIXER = TILES.register(SubtypeMachine.chemicalmixer.tag(),
	    () -> new TileEntityType<>(TileChemicalMixer::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.chemicalmixer)), null));
    public static final RegistryObject<TileEntityType<TileChemicalCrystallizer>> TILE_CHEMICALCRYSTALLIZER = TILES
	    .register(SubtypeMachine.chemicalcrystallizer.tag(), () -> new TileEntityType<>(TileChemicalCrystallizer::new,
		    Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.chemicalcrystallizer)), null));
    public static final RegistryObject<TileEntityType<TileCircuitBreaker>> TILE_CIRCUITBREAKER = TILES.register(SubtypeMachine.circuitbreaker.tag(),
	    () -> new TileEntityType<>(TileCircuitBreaker::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.circuitbreaker)), null));
    public static final RegistryObject<TileEntityType<TileTeleporter>> TILE_TELEPORTER = TILES.register(SubtypeMachine.teleporter.tag(),
	    () -> new TileEntityType<>(TileTeleporter::new, Sets.newHashSet(SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.teleporter)), null));

    public static final RegistryObject<TileEntityType<TileMultiSubnode>> TILE_MULTI = TILES.register("multisubnode",
	    () -> new TileEntityType<>(TileMultiSubnode::new, Sets.newHashSet(multi), null));

    public static final RegistryObject<TileEntityType<TileWire>> TILE_WIRE = TILES.register("wiregenerictile",
	    () -> new TileEntityType<>(TileWire::new, BlockWire.WIRESET, null));
    public static final RegistryObject<TileEntityType<TileLogisticalWire>> TILE_LOGISTICALWIRE = TILES.register("wirelogisticaltile",
	    () -> new TileEntityType<>(TileLogisticalWire::new, BlockWire.WIRESET, null));
    public static final RegistryObject<TileEntityType<TilePipe>> TILE_PIPE = TILES.register("pipegenerictile",
	    () -> new TileEntityType<>(TilePipe::new, BlockPipe.PIPESET, null));
    public static final RegistryObject<ContainerType<ContainerCoalGenerator>> CONTAINER_COALGENERATOR = CONTAINERS
	    .register(SubtypeMachine.coalgenerator.tag(), () -> new ContainerType<>(ContainerCoalGenerator::new));
    public static final RegistryObject<ContainerType<ContainerElectricFurnace>> CONTAINER_ELECTRICFURNACE = CONTAINERS
	    .register(SubtypeMachine.electricfurnace.tag(), () -> new ContainerType<>(ContainerElectricFurnace::new));
    public static final RegistryObject<ContainerType<ContainerO2OProcessor>> CONTAINER_O2OPROCESSOR = CONTAINERS.register("o2oprocessor",
	    () -> new ContainerType<>(ContainerO2OProcessor::new));
    public static final RegistryObject<ContainerType<ContainerDO2OProcessor>> CONTAINER_DO2OPROCESSOR = CONTAINERS.register("do2oprocessor",
	    () -> new ContainerType<>(ContainerDO2OProcessor::new));
    public static final RegistryObject<ContainerType<ContainerBatteryBox>> CONTAINER_BATTERYBOX = CONTAINERS.register(SubtypeMachine.batterybox.tag(),
	    () -> new ContainerType<>(ContainerBatteryBox::new));
    public static final RegistryObject<ContainerType<ContainerFermentationPlant>> CONTAINER_FERMENTATIONPLANT = CONTAINERS
	    .register(SubtypeMachine.fermentationplant.tag(), () -> new ContainerType<>(ContainerFermentationPlant::new));
    public static final RegistryObject<ContainerType<ContainerMineralWasher>> CONTAINER_MINERALWASHER = CONTAINERS
	    .register(SubtypeMachine.mineralwasher.tag(), () -> new ContainerType<>(ContainerMineralWasher::new));
    public static final RegistryObject<ContainerType<ContainerChemicalMixer>> CONTAINER_CHEMICALMIXER = CONTAINERS
	    .register(SubtypeMachine.chemicalmixer.tag(), () -> new ContainerType<>(ContainerChemicalMixer::new));
    public static final RegistryObject<ContainerType<ContainerChemicalCrystallizer>> CONTAINER_CHEMICALCRYSTALLIZER = CONTAINERS
	    .register(SubtypeMachine.chemicalcrystallizer.tag(), () -> new ContainerType<>(ContainerChemicalCrystallizer::new));

    private static <T extends IForgeRegistryEntry<T>> Supplier<? extends T> supplier(T entry) {
	return () -> entry;
    }

    private static <T extends IForgeRegistryEntry<T>> Supplier<? extends T> supplier(T entry, ISubtype en) {
	if (entry instanceof Block) {
	    SUBTYPEBLOCK_MAPPINGS.put(en, (Block) entry);
	} else if (entry instanceof Item) {
	    SUBTYPEITEM_MAPPINGS.put(en, (Item) entry);
	    ITEMSUBTYPE_MAPPINGS.put((Item) entry, en);
	}
	return supplier(entry);
    }
}
