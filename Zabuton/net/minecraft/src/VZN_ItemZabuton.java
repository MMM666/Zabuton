package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class VZN_ItemZabuton extends Item {

	public static final String colorNamesJP[] = {
		"çï", "ê‘", "óŒ", "íÉ",
		"ê¬", "éá", "ãÛ", "ã‚",
		"äD", "ïèéq", "ÍS", "â©",
		"íWê¬", "çgéá", "ûÚ", "îí" };

	public static final int colorValues[] = {
		0x1e1b1b, 0xb3312c, 0x3b511a, 0x51301a,
		0x253192, 0x7b2fbe, 0x287697, 0xa0a0af,
		0x434343, 0xd88198, 0x41cd34, 0xdecf2a,
		0x6689d3, 0xc354cd, 0xeb8844, 0xf0f0f0 };


	public VZN_ItemZabuton(int i) {
		super(i);
		maxStackSize = 8;
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabTransport);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		float f = 1.0F;
		float f1 = entityplayer.prevRotationPitch
				+ (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
		float f2 = entityplayer.prevRotationYaw
				+ (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
		double d = entityplayer.prevPosX
				+ (entityplayer.posX - entityplayer.prevPosX) * (double) f;
		double d1 = (entityplayer.prevPosY
				+ (entityplayer.posY - entityplayer.prevPosY) * (double) f + 1.6200000000000001D)
				- (double) entityplayer.yOffset;
		double d2 = entityplayer.prevPosZ
				+ (entityplayer.posZ - entityplayer.prevPosZ) * (double) f;
		Vec3 vec3d = world.getWorldVec3Pool().getVecFromPool(d, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double d3 = 5D;
		Vec3 vec3d1 = vec3d.addVector((double) f7 * d3, (double) f8 * d3,
				(double) f9 * d3);
		MovingObjectPosition movingobjectposition = world.clip(vec3d, vec3d1, true);
		if (movingobjectposition == null) {
			return itemstack;
		}
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			int i = movingobjectposition.blockX;
			int j = movingobjectposition.blockY;
			int k = movingobjectposition.blockZ;
			if (world.getBlockMaterial(i, j + 1, k) == Material.air) {
				if (!world.isRemote) {
					// if(world.getBlockId(i, j, k) == Block.snow.blockID)
					// {
					// j--;
					// }
					try {
						Constructor<VZN_EntityZabuton> lconstructor = mod_VZN_zabuton.classZabuton
								.getConstructor(World.class, double.class,
										double.class, double.class, byte.class);
						VZN_EntityZabuton ez = lconstructor.newInstance(world,
								(float) i + 0.5F, (float) j + 1.0F,
								(float) k + 0.5F,
								(byte) (itemstack.getItemDamage() & 0x0f));
						
						// ï˚å¸Ç¨ÇﬂÇÕÇ±Ç±Ç…ì¸ÇÍÇÈ
						ez.rotationYaw = (MathHelper
								.floor_double((double) ((entityplayer.rotationYaw * 4F) / 360F) + 2.50D) & 3) * 90;
						world.spawnEntityInWorld(ez);
					} catch (Exception e) {
					}
					
				}
				if (!entityplayer.capabilities.isCreativeMode) {
					itemstack.stackSize--;
				}
			}
		}
		return itemstack;
	}

	public int getColorFromDamage(int pdamage, int pindex) {
		int li = colorValues[pdamage];
		return li;
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return getColorFromDamage(par1ItemStack.getItemDamage(), par2);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return (new StringBuilder()).append(super.getUnlocalizedName()).append(".")
				.append(ItemDye.dyeColorNames[par1ItemStack.getItemDamage()]).toString();
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int li = 0; li < 16; li++) {
			par3List.add(new ItemStack(par1, 1, li));
		}
	}

}
