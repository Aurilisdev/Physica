package electrodynamics.prefab.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class GenericCustomScreen<T extends Container> extends ContainerScreen<T> {
    protected GenericCustomScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
	super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
	this.renderBackground(matrixStack);
	super.render(matrixStack, mouseX, mouseY, partialTicks);
	renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
	RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	minecraft.getTextureManager().bindTexture(getScreenBackground());
	blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    public abstract ResourceLocation getScreenBackground();
}
