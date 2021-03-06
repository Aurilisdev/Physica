package electrodynamics.prefab.screen.component;

import java.awt.Rectangle;
import java.util.function.DoubleSupplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import electrodynamics.api.References;
import electrodynamics.api.screen.IScreenWrapper;
import electrodynamics.prefab.utilities.UtilitiesRendering;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenComponentProgress extends ScreenComponent {
    public static final int WIDTHARROW = 22;
    private static final int HEIGHTARROW = 16;
    private static final int POSXARROW = 0;
    private static final int POSYARROW = 0;
    private static final int WIDTHFLAME = 14;
    private static final int HEIGHTFLAME = 14;
    private static final int POSXFLAME = 0;
    private static final int POSYFLAME = 19;
    private boolean left = false;
    private boolean isFlame = false;

    private final DoubleSupplier progressInfoHandler;

    public ScreenComponentProgress(final DoubleSupplier progressInfoHandler, final IScreenWrapper gui, final int x, final int y) {
	super(new ResourceLocation(References.ID + ":textures/screen/component/progress.png"), gui, x, y);
	this.progressInfoHandler = progressInfoHandler;
    }

    public ScreenComponentProgress flame() {
	isFlame = true;
	return this;
    }

    public ScreenComponentProgress left() {
	left = true;
	// TODO: Finish left side render
	return this;
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
	return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, isFlame ? WIDTHFLAME : WIDTHARROW, isFlame ? HEIGHTFLAME : HEIGHTARROW);
    }

    @Override
    public void renderBackground(MatrixStack stack, final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
	UtilitiesRendering.bindTexture(resource);
	if (left) {
	    gui.drawTexturedRect(stack, guiWidth + xLocation, guiHeight + yLocation, POSXARROW + WIDTHARROW * 3, POSYARROW, WIDTHARROW, HEIGHTARROW);
	} else {
	    gui.drawTexturedRect(stack, guiWidth + xLocation, guiHeight + yLocation, isFlame ? POSXFLAME : POSXARROW,
		    isFlame ? POSYFLAME + HEIGHTFLAME : POSYARROW, isFlame ? WIDTHFLAME : WIDTHARROW, isFlame ? HEIGHTFLAME : HEIGHTARROW);
	}
	if (isFlame) {
	    int scale = (int) (progressInfoHandler.getAsDouble() * HEIGHTFLAME);
	    gui.drawTexturedRect(stack, guiWidth + xLocation, guiHeight + yLocation + HEIGHTFLAME - scale, POSXFLAME, POSYFLAME + HEIGHTFLAME - scale,
		    WIDTHFLAME, scale);
	} else {
	    if (!left) {
		gui.drawTexturedRect(stack, guiWidth + xLocation, guiHeight + yLocation, POSXARROW + WIDTHARROW, POSYARROW,
			(int) (progressInfoHandler.getAsDouble() * WIDTHARROW), HEIGHTARROW);
	    } else {

	    }
	}
    }

}