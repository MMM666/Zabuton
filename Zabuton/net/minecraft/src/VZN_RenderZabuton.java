package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class VZN_RenderZabuton extends Render {

	protected ModelBase baseZabuton;


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
		
		String s = entityzabuton.getTexture();
		if (s.isEmpty()) {
			// 色をつける
			int k = ((VZN_ItemZabuton)mod_VZN_zabuton.zabuton).getColorFromDamage(entityzabuton.color, 0);
			float f15 = (float)(k >> 16 & 0xff) / 255F;
			float f17 = (float)(k >> 8 & 0xff) / 255F;
			float f19 = (float)(k & 0xff) / 255F;
			float f21 = entityzabuton.getBrightness(f1);
			GL11.glColor4f(f15 * f21, f17 * f21, f19 * f21, 1.0F);
			s = "/item/zabuton.png";
		}
		loadTexture(s);
		GL11.glScalef(-1F, -1F, 1.0F);
		baseZabuton.render(entityzabuton, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		doRenderZabuton((VZN_EntityZabuton)entity, d, d1, d2, f, f1);
	}

}