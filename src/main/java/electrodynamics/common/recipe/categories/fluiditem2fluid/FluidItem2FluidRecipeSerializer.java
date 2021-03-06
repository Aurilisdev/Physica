package electrodynamics.common.recipe.categories.fluiditem2fluid;

import java.lang.reflect.Constructor;

import com.google.gson.JsonObject;

import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.ElectrodynamicsRecipeSerializer;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class FluidItem2FluidRecipeSerializer<T extends FluidItem2FluidRecipe> extends ElectrodynamicsRecipeSerializer<T> {

    public FluidItem2FluidRecipeSerializer(Class<T> recipeClass) {
	super(recipeClass);
    }

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {

	CountableIngredient itemInput = CountableIngredient.deserialize(JSONUtils.getJsonObject(json, "item_input"));
	FluidIngredient fluidInput = FluidIngredient.deserialize(JSONUtils.getJsonObject(json, "fluid_input"));
	FluidStack fluidOutput = FluidIngredient.deserialize(JSONUtils.getJsonObject(json, "fluid_output")).getFluidStack();

	try {
	    Constructor<T> recipeConstructor = getRecipeClass().getDeclaredConstructor(
		    new Class[] { ResourceLocation.class, CountableIngredient.class, FluidIngredient.class, FluidStack.class });
	    return recipeConstructor.newInstance(new Object[] { recipeId, itemInput, fluidInput, fluidOutput });
	} catch (Exception e) {
	    ElectrodynamicsRecipe.LOGGER.info("Recipe generation has failed!");
	    return null;
	}

    }

    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
	CountableIngredient itemInput = CountableIngredient.read(buffer);
	FluidIngredient fluidInput = FluidIngredient.read(buffer);
	FluidStack fluidOutput = FluidIngredient.read(buffer).getFluidStack();

	try {
	    Constructor<T> recipeConstructor = getRecipeClass().getDeclaredConstructor(
		    new Class[] { ResourceLocation.class, CountableIngredient.class, FluidIngredient.class, FluidStack.class });
	    return recipeConstructor.newInstance(new Object[] { recipeId, itemInput, fluidInput, fluidOutput });
	} catch (Exception e) {
	    ElectrodynamicsRecipe.LOGGER.info("Recipe generation has failed!");
	    return null;
	}
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
	CountableIngredient itemInput = (CountableIngredient) recipe.getIngredients().get(0);
	FluidIngredient fluidInput = (FluidIngredient) recipe.getIngredients().get(1);
	FluidIngredient fluidOutput = new FluidIngredient(recipe.getFluidRecipeOutput());
	itemInput.writeStack(buffer);
	fluidInput.writeStack(buffer);
	fluidOutput.writeStack(buffer);
    }

}
