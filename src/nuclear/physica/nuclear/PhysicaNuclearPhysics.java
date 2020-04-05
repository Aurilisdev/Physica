package physica.nuclear;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import physica.CoreReferences;
import physica.api.core.abstraction.recipe.IRecipeRegister;
import physica.api.core.load.ContentLoader;
import physica.api.core.load.LoadPhase;
import physica.nuclear.client.NuclearClientRegister;
import physica.nuclear.common.NuclearBlockRegister;
import physica.nuclear.common.NuclearEntityRegister;
import physica.nuclear.common.NuclearFluidRegister;
import physica.nuclear.common.NuclearItemRegister;
import physica.nuclear.common.NuclearRecipeRegister;
import physica.nuclear.common.NuclearTabRegister;
import physica.nuclear.common.NuclearWorldGenRegister;
import physica.nuclear.common.configuration.ConfigNuclearPhysics;
import physica.nuclear.common.effect.potion.PotionRadiation;
import physica.nuclear.common.radiation.RoentgenOverlay;
import physica.proxy.CommonProxy;

@Mod(modid = NuclearReferences.DOMAIN, name = NuclearReferences.NAME, version = CoreReferences.VERSION, dependencies = "required-after:" + CoreReferences.DOMAIN)
public class PhysicaNuclearPhysics {

	@SidedProxy(clientSide = "physica.proxy.ClientProxy", serverSide = "physica.proxy.ServerProxy")
	public static CommonProxy			sidedProxy;
	public static ContentLoader			proxyLoader	= new ContentLoader();

	@Instance(NuclearReferences.NAME)
	public static PhysicaNuclearPhysics	INSTANCE;
	@Metadata(NuclearReferences.DOMAIN)
	public static ModMetadata			metadata;

	public static File					configFolder;
	public static ConfigNuclearPhysics	config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		INSTANCE = this;
		configFolder = new File(event.getModConfigurationDirectory(), "/" + NuclearReferences.DOMAIN);
		proxyLoader.addContent(sidedProxy);
		proxyLoader.addContent(config = new ConfigNuclearPhysics());
		proxyLoader.addContent(new NuclearTabRegister());

		proxyLoader.addContent(new NuclearFluidRegister());
		proxyLoader.addContent(new NuclearBlockRegister());
		proxyLoader.addContent(new NuclearItemRegister());
		proxyLoader.addContent(new NuclearEntityRegister());

		if (event.getSide() == Side.CLIENT)
		{
			proxyLoader.addContent(new NuclearClientRegister());
			RoentgenOverlay overlay = new RoentgenOverlay();
			MinecraftForge.EVENT_BUS.register(overlay);
			FMLCommonHandler.instance().bus().register(overlay);
		}

		proxyLoader.addContent(new NuclearRecipeRegister());
		proxyLoader.addContent(new NuclearWorldGenRegister());
		metadata.authorList = CoreReferences.Metadata.AUTHORS;
		metadata.autogenerated = false;
		metadata.credits = CoreReferences.Metadata.CREDITS;
		metadata.description = CoreReferences.Metadata.DESCRIPTION.replace("Physica", NuclearReferences.NAME);
		metadata.modId = NuclearReferences.DOMAIN;
		metadata.name = NuclearReferences.NAME;
		metadata.parent = CoreReferences.DOMAIN;
		metadata.updateUrl = CoreReferences.Metadata.UPDATE_URL;
		metadata.url = CoreReferences.Metadata.URL;
		metadata.version = CoreReferences.VERSION;
		proxyLoader.callRegister(LoadPhase.CreativeTabRegister);
		proxyLoader.callRegister(LoadPhase.ConfigRegister);
		proxyLoader.callRegister(LoadPhase.RegisterObjects);
		proxyLoader.callRegister(LoadPhase.PreInitialize);
		proxyLoader.callRegister(LoadPhase.ClientRegister);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxyLoader.callRegister(LoadPhase.Initialize);
		proxyLoader.callRegister(LoadPhase.EntityRegister);
		proxyLoader.callRegister(LoadPhase.FluidRegister);
		proxyLoader.callRegister(LoadPhase.WorldRegister);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxyLoader.callRegister(LoadPhase.PostInitialize);
		System.out.println("Radiation potion id: " + PotionRadiation.INSTANCE.id); // To initialize the potion...
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		proxyLoader.callRegister(LoadPhase.OnStartup);
		IRecipeRegister.callRegister("Nuclear");
	}
}