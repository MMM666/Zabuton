package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class VZN_RenderZabuton extends Render {

	protected ModelBase baseZabuton;
	protected ResourceLocation[] textures = new ResourceLocation[] {
			new ResourceLocation("textures/entity/zabuton_f.png"),
			new ResourceLocation("textures/entity/zabuton_e.png"),
			new ResourceLocation("textures/entity/zabuton_d.png"),
			new ResourceLocation("textures/entity/zabuton_c.png"),
			new ResourceLocation("textures/entity/zabuton_b.png"),
			new ResourceLocation("textures/entity/zabuton_a.png"),
			new ResourceLocation("textures/entity/zabuton_9.png"),
			new ResourceLocation("textures/entity/zabuton_8.png"),
			new ResourceLocation("textures/entity/zabuton_7.png"),
			new ResourceLocation("textures/entity/zabuton_6.png"),
			new ResourceLocation("textures/entity/zabuton_5.png"),
			new ResourceLocation("textures/entity/zabuton_4.png"),
			new ResourceLocation("textures/entity/zabuton_3.png"),
			new ResourceLocation("textures/entity/zabuton_2.png"),
			new ResourceLocation("textures/entity/zabuton_1.png"),
			new ResourceLocation("textures/entity/zabuton_0.png")
	};

	public VZN_RenderZabuton() {
		shadowSize = 0.5F;
		baseZabuton = new VZN_ModelZabuton();
	}

	public void doRenderZabuton(VZN_EntityZabuton entityzabuton, double d, double d1, double d2, float f, float f1) {
		// レンダリング実装
		// レンダリング
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		
		func_110777_b(entityzabuton);
		GL11.glScalef(-1F, -1F, 1.0F);
		baseZabuton.render(entityzabuton, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		doRenderZabuton((VZN_EntityZabuton)entity, d, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation func_110775_a(Entity var1) {
		return textures[((VZN_EntityZabuton)var1).color];
	}

}