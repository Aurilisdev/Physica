package electrodynamics.api.screen.component;

import java.util.List;

import net.minecraft.util.text.ITextProperties;

@FunctionalInterface
public interface TextPropertySupplier {
    List<? extends ITextProperties> getInfo();
}