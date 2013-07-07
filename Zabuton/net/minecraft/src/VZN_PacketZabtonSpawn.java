package net.minecraft.src;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class VZN_PacketZabtonSpawn extends Packet23VehicleSpawn {

	public byte color;
	public float rotationYaw;

	public VZN_PacketZabtonSpawn(VZN_EntityZabuton pEntity) {
		super();

		this.entityId = pEntity.entityId;
		this.color = pEntity.color;
		this.xPosition = MathHelper.floor_double(pEntity.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(pEntity.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(pEntity.posZ * 32.0D);
		this.rotationYaw = pEntity.rotationYaw;
	}

	@Override
	public void readPacketData(DataInput par1DataInput) throws IOException {
		this.entityId = par1DataInput.readInt();
		this.color = par1DataInput.readByte();
		this.xPosition = par1DataInput.readInt();
		this.yPosition = par1DataInput.readInt();
		this.zPosition = par1DataInput.readInt();
		this.rotationYaw = par1DataInput.readFloat();
	}

	@Override
	public void writePacketData(DataOutput par1DataOutput) throws IOException {
		par1DataOutput.writeInt(this.entityId);
		par1DataOutput.writeByte(this.color);
		par1DataOutput.writeInt(this.xPosition);
		par1DataOutput.writeInt(this.yPosition);
		par1DataOutput.writeInt(this.zPosition);
		par1DataOutput.writeFloat(this.rotationYaw);
	}

	@Override
	public void processPacket(NetHandler var1) {
		if (var1 instanceof NetClientHandler) {
			WorldClient lworld = MMM_Helper.mc.theWorld;
			double lx = (double) this.xPosition / 32.0D;
			double ly = (double) this.yPosition / 32.0D;
			double lz = (double) this.zPosition / 32.0D;
			
			VZN_EntityZabuton lentity =
					new VZN_EntityZabuton(lworld, lx, ly, lz, this.color);
			
			lentity.serverPosX = this.xPosition;
			lentity.serverPosY = this.yPosition;
			lentity.serverPosZ = this.zPosition;
			lentity.setRotation(rotationYaw, 0.0F);
			lentity.entityId = this.entityId;
			lworld.addEntityToWorld(this.entityId, lentity);
		}
	}

	@Override
	public int getPacketSize() {
		return 21;
	}

}
