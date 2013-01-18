package net.minecraft.src;

import java.io.DataInputStream;
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
	public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.color = var1.readByte();
        this.xPosition = var1.readInt();
        this.yPosition = var1.readInt();
        this.zPosition = var1.readInt();
        this.rotationYaw = var1.readFloat();
	}

	@Override
	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeByte(this.color);
		var1.writeInt(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.writeFloat(this.rotationYaw);
	}

	@Override
	public void processPacket(NetHandler var1) {
		if (var1 instanceof NetClientHandler) {
			WorldClient lworld = mod_VZN_zabuton.mc.theWorld;
	        double lx = (double)this.xPosition / 32.0D;
	        double ly = (double)this.yPosition / 32.0D;
	        double lz = (double)this.zPosition / 32.0D;
	        
	        VZN_EntityZabuton lentity = new VZN_EntityZabuton(lworld, lx, ly, lz, this.color);

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
