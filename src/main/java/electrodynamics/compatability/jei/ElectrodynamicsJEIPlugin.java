package electrodynamics.compatability.jei;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;
import electrodynamics.common.recipe.MachineRecipes;
import electrodynamics.compatability.jei.BlastCraftJEIPlugin;
import electrodynamics.compatability.jei.recipeCategories.psuedoRecipes.Psuedo5XRecipe;
import electrodynamics.compatability.jei.recipeCategories.psuedoRecipes.PsuedoRecipes;
import electrodynamics.compatability.jei.recipeCategories.psuedoRecipes.PsuedoSolAndLiqToLiquidRecipe;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.blastcraft.BlastCompressorRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.ChemicalMixerRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.ElectricFurnaceRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.FermentationPlantRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.MineralCrusherRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.MineralGrinderRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.OxidationFurnaceRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.WireMillRecipeCategory;
import electrodynamics.compatability.jei.recipeCategories.specificMachines.electrodynamics.X5OreProcessingRecipeCategory;

import electrodynamics.prefab.tile.processing.DO2OProcessingRecipe;
import electrodynamics.prefab.tile.processing.O2OProcessingRecipe;
import electrodynamics.client.screen.ScreenChemicalCrystallizer;
import electrodynamics.client.screen.ScreenChemicalMixer;
import electrodynamics.client.screen.ScreenDO2OProcessor;
import electrodynamics.client.screen.ScreenElectricFurnace;
import electrodynamics.client.screen.ScreenFermentationPlant;
import electrodynamics.client.screen.ScreenMineralWasher;
import electrodynamics.client.screen.ScreenO2OProcessor;
import electrodynamics.common.block.subtype.SubtypeMachine;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

@JeiPlugin
public class ElectrodynamicsJEIPlugin implements IModPlugin{
	
	private static final Logger logger = LogManager.getLogger(electrodynamics.api.References.ID);
	
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(electrodynamics.api.References.ID, "elecdyn_jei_plugin");
	}
	
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		
		//Electric Furnace
		registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.electricfurnace)), 
				ElectricFurnaceRecipeCategory.UID);
		
		//Wire Mill
		registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.wiremill)), 
				WireMillRecipeCategory.UID);
		
		//Mineral Crusher
		registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.mineralcrusher)), 
				MineralCrusherRecipeCategory.UID);
		
		//Mineral Grinder
		registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.mineralgrinder)), 
				MineralGrinderRecipeCategory.UID);
		
		//Oxidation Furnace
		registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.oxidationfurnace)), 
				OxidationFurnaceRecipeCategory.UID);
		
		//5x Ore Processing
		registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.chemicalcrystallizer)), 
				X5OreProcessingRecipeCategory.UID);
		
		//Chemical Mixer
		registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.chemicalmixer)), 
				ChemicalMixerRecipeCategory.UID);
		
		//Fermentation Chamber
				registration.addRecipeCatalyst(new ItemStack(electrodynamics.DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeMachine.fermentationplant)), 
						FermentationPlantRecipeCategory.UID);
		
	}
	
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) 
	{
		PsuedoRecipes.addElectrodynamicsRecipes();
		Minecraft mc = Minecraft.getInstance();
		ClientWorld world = Objects.requireNonNull(mc.world);
		
		/*Electrodynamics*/
		
		//Electric Furnace
		//This is broken for the 3.0 gradle mappings and there isn't really a fix for it. It works on the 4.0
		//version, so if we ever update then it will work!
		@SuppressWarnings("unchecked")
		Set<FurnaceRecipe> electricFurnaceRecipes = 
				ImmutableSet.copyOf(world.getRecipeManager().getRecipesForType((IRecipeType<FurnaceRecipe>) Registry.RECIPE_TYPE.getOrDefault(VanillaRecipeCategoryUid.FURNACE)));
				
		
		registration.addRecipes(electricFurnaceRecipes, ElectricFurnaceRecipeCategory.UID);
		
		//Wire Mill
		Set<O2OProcessingRecipe> wireMillRecipes = 
				MachineRecipes.o2orecipemap.get(electrodynamics.DeferredRegisters.TILE_WIREMILL.get());
		
		registration.addRecipes(wireMillRecipes, WireMillRecipeCategory.UID);
		
		//Mineral Crusher
		Set<O2OProcessingRecipe> mineralCrusherRecipes = 
				MachineRecipes.o2orecipemap.get(electrodynamics.DeferredRegisters.TILE_MINERALCRUSHER.get());
		
		registration.addRecipes(mineralCrusherRecipes, MineralCrusherRecipeCategory.UID);
		
		//Mineral Grinder
		Set<O2OProcessingRecipe> mineralGrinderRecipes = 
				MachineRecipes.o2orecipemap.get(electrodynamics.DeferredRegisters.TILE_MINERALGRINDER.get());
		
		registration.addRecipes(mineralGrinderRecipes, MineralGrinderRecipeCategory.UID);
		
		//Oxidation Furnace
		Set<DO2OProcessingRecipe> oxidationFurnaceRecipes = 
				MachineRecipes.do2orecipemap.get(electrodynamics.DeferredRegisters.TILE_OXIDATIONFURNACE.get());
		
		registration.addRecipes(oxidationFurnaceRecipes, OxidationFurnaceRecipeCategory.UID);
		
		//5x Ore Processing
		Set<Psuedo5XRecipe> x5Recipes = new HashSet<>(PsuedoRecipes.X5_ORE_RECIPES);
		
		registration.addRecipes(x5Recipes, X5OreProcessingRecipeCategory.UID);
		
		//Chemical Mixer
		Set<PsuedoSolAndLiqToLiquidRecipe> chemicalMixerRecipes = new HashSet<>(PsuedoRecipes.CHEMICAL_MIXER_RECIPES);
		
		registration.addRecipes(chemicalMixerRecipes, ChemicalMixerRecipeCategory.UID);
		
		//Fermentation Chamber
		Set<PsuedoSolAndLiqToLiquidRecipe> fermenterRecipes = new HashSet<>(PsuedoRecipes.FERMENTATION_CHAMBER_RECIPES);
				
		registration.addRecipes(fermenterRecipes, FermentationPlantRecipeCategory.UID);
		
		electrodynamicsInfoTabs(registration);
		
	}
	
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) 
	{
		//Electric Furnace
		registration.addRecipeCategories(new ElectricFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		
		//Wire Mill
		registration.addRecipeCategories(new WireMillRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		
		//Mineral Grinder
		registration.addRecipeCategories(new MineralGrinderRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		
		//Mineral Crusher
		registration.addRecipeCategories(new MineralCrusherRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		
		//Oxidation Furnace
		registration.addRecipeCategories(new OxidationFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		
		//5x Ore Processing
		registration.addRecipeCategories(new X5OreProcessingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		
		//Chemical Mixer
		registration.addRecipeCategories(new ChemicalMixerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		
		//Fermentation Chamber
		registration.addRecipeCategories(new FermentationPlantRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	
	}
	
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		int[] O2OArrowLocation = {80,35,22,15};
		
		//Each click area needs to be tied to a unique machine Screen class. Otherwise you will get multiple machines
		//popping up as with the O2O recipes for example
		
		//Wire Mill, Mineral Grinder, Mineral Crusher, Blast Compressor
		registry.addRecipeClickArea(ScreenO2OProcessor.class, O2OArrowLocation[0], O2OArrowLocation[1], O2OArrowLocation[2], O2OArrowLocation[3],
				getO2OGuiScreens()
		);
		
		//Oxidation Furnace
		registry.addRecipeClickArea(ScreenDO2OProcessor.class, O2OArrowLocation[0], O2OArrowLocation[1], O2OArrowLocation[2], O2OArrowLocation[3], 
				OxidationFurnaceRecipeCategory.UID
		);
		
		//Electric Furnace Click Area
		registry.addRecipeClickArea(ScreenElectricFurnace.class, O2OArrowLocation[0], O2OArrowLocation[1], 
				O2OArrowLocation[2], O2OArrowLocation[3], ElectricFurnaceRecipeCategory.UID);
		
		//Chemical Mixer 
		registry.addRecipeClickArea(ScreenChemicalMixer.class,97, 31, O2OArrowLocation[2], O2OArrowLocation[3], 
				ChemicalMixerRecipeCategory.UID);
		
		//Fermentation Plant
		registry.addRecipeClickArea(ScreenFermentationPlant.class, 97, 31, O2OArrowLocation[2], O2OArrowLocation[3],
				FermentationPlantRecipeCategory.UID);
		
		//Mineral Washer
		registry.addRecipeClickArea(ScreenMineralWasher.class,45, 35,  O2OArrowLocation[2], O2OArrowLocation[3],
				X5OreProcessingRecipeCategory.UID);
		
		//Chemical Crystalizer
		registry.addRecipeClickArea(ScreenChemicalCrystallizer.class,45, 35,  O2OArrowLocation[2], O2OArrowLocation[3],
				X5OreProcessingRecipeCategory.UID);
	}
	
	
	private ResourceLocation[] getO2OGuiScreens(){
		
		ArrayList<ResourceLocation> locations = new ArrayList<ResourceLocation>();
		
		locations.add(WireMillRecipeCategory.UID);
		locations.add(MineralGrinderRecipeCategory.UID);
		locations.add(MineralCrusherRecipeCategory.UID);
		
		//there's really nothing I can do about this unless the Blast Compressor and all the other O2O
		//machines get their own Screen class
		if(BlastCraftJEIPlugin.isBlastCraftLoaded) {
			locations.add(BlastCompressorRecipeCategory.UID);
		}
		
		ResourceLocation[] totalLocations = new ResourceLocation[locations.size()];
		
		for(int i = 0; i < locations.size();i++) {
			totalLocations[i] = locations.get(i);
		}
		
		return totalLocations;
		
	}
	
	
	private void electrodynamicsInfoTabs(IRecipeRegistration registration) {
		
		/* Machines currently with tabs:

		Coal Generator
		Upgrade Transformer
		Downgrade Transformer
		Solar Panel
		Advanced Solar Panel
		Thermoelectric Generator
		Combustion Chamber
		Hydroelectric Generator
		Wind Generator
		Mineral Washer
		Chemical Mixer
		Chemical Crystalizer
		
		*/
		ArrayList<ItemStack> edMachines = PsuedoRecipes.ELECTRODYNAMICS_MACHINES;
		String temp;
		
		for(ItemStack itemStack: edMachines) {
			temp = itemStack.getItem().toString();
			registration.addIngredientInfo(itemStack, VanillaTypes.ITEM, "info.jei.block." + temp);
		}

	}
	
}
