package electrodynamics.api.electricity;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityElectrodynamic {
    public static final double DEFAULT_VOLTAGE = 120.0;
    @CapabilityInject(IElectrodynamic.class)
    public static Capability<IElectrodynamic> ELECTRODYNAMIC = null;

    public static void register() {
	CapabilityManager.INSTANCE.register(IElectrodynamic.class, new IStorage<IElectrodynamic>() {
	    @Override
	    public INBT writeNBT(Capability<IElectrodynamic> capability, IElectrodynamic instance, Direction side) {
		return DoubleNBT.valueOf(instance.getJoulesStored());
	    }

	    @Override
	    @Deprecated
	    public void readNBT(Capability<IElectrodynamic> capability, IElectrodynamic instance, Direction side, INBT nbt) {
		instance.setJoulesStored(((DoubleNBT) nbt).getDouble());
	    }
	}, () -> new ElectrodynamicStorage(1000, 0));
    }
}
