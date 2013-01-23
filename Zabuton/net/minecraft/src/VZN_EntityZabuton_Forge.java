package net.minecraft.src;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.*;

public class VZN_EntityZabuton_Forge extends VZN_EntityZabuton implements IEntityAdditionalSpawnData {

	public VZN_EntityZabuton_Forge(World world) {
		super(world);
	}

	public VZN_EntityZabuton_Forge(World world, byte pColor) {
		super(world, pColor);
	}
    
	public VZN_EntityZabuton_Forge(World world, ItemStack itemstack) {
		super(world, itemstack);
	}

	public VZN_EntityZabuton_Forge(World world, double x, double y, double z, byte pColor) {
		super(world, x, y, z, pColor);
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeByte(color);
		data.writeFloat(rotationYaw);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		color = data.readByte();
		setRotation(data.readFloat(), 0.0F);
	}

}
