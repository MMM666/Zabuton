package net.minecraft.src;

import java.lang.reflect.Constructor;

import net.minecraft.server.MinecraftServer;

public class VZN_BehaviorZabutonDispense extends BehaviorProjectileDispense {

	final MinecraftServer mcServer;
	protected ItemStack fitemstack;


	public VZN_BehaviorZabutonDispense(MinecraftServer par1MinecraftServer) {
		this.mcServer = par1MinecraftServer;
	}

	@Override
	public ItemStack dispenseStack(IBlockSource par1iBlockSource, ItemStack par2ItemStack) {
		fitemstack = par2ItemStack;
		return super.dispenseStack(par1iBlockSource, par2ItemStack);
	}

	@Override
	protected IProjectile getProjectileEntity(World var1, IPosition var2) {
		try {
			Constructor<VZN_EntityZabuton> lconstructor = mod_VZN_zabuton.classZabuton.getConstructor(World.class, double.class, double.class, double.class, byte.class);
			return lconstructor.newInstance(var1, var2.getX(), var2.getY(), var2.getZ(), (byte)fitemstack.getItemDamage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
