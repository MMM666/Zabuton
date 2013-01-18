package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class VZN_EntityZabuton extends Entity implements IProjectile{

	private double zabutonX;
	private double zabutonY;
	private double zabutonZ;
	private double zabutonYaw;
	private double zabutonPitch;
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	private int health;
	public boolean isDispensed;
	public byte color;

	private int boatPosRotationIncrements;



	// Method
	public VZN_EntityZabuton(World world) {
		super(world);
		preventEntitySpawning = true;
		setSize(0.81F, 0.2F);
		yOffset = 0F;
		health = 20;
		isDispensed = false;
		color = 15;
	}

	public VZN_EntityZabuton(World world, byte pColor) {
		this(world);
		color = pColor;
	}

	public VZN_EntityZabuton(World world, ItemStack itemstack) {
		this(world, (byte)(itemstack.getItemDamage() & 0x0f));
	}

	public VZN_EntityZabuton(World world, double x, double y, double z, byte pColor) {
		this(world, pColor);
		setPosition(x, y + (double)yOffset, z);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
	}

	@Override
	public void setThrowableHeading(double px, double py, double pz, float f, float f1) {
		// ディスペンサー用
		float f2 = MathHelper.sqrt_double(px * px + py * py + pz * pz);
		px /= f2;
		py /= f2;
		pz /= f2;
		px += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
		py += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
		pz += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
		px *= f;
		py *= f;
		pz *= f;
		motionX = px;
		motionY = py;
		motionZ = pz;
		float f3 = MathHelper.sqrt_double(px * px + pz * pz);
		prevRotationYaw = rotationYaw = (float)((Math.atan2(px, pz) * 180D) / 3.1415927410125732D);
		prevRotationPitch = rotationPitch = (float)((Math.atan2(py, f3) * 180D) / 3.1415927410125732D);
//        ticksInGround = 0;
		setDispensed(true);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(17, new Byte((byte)(isDispensed ? 0x01 : 0x00)));
		dataWatcher.addObject(18, Integer.valueOf(0));
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return par1Entity.boundingBox;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		color = nbttagcompound.getByte("Color");
		health = nbttagcompound.getShort("Health");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setByte("Color", (byte)(color & 0x0f));
		nbttagcompound.setShort("Health", (byte)health);
	}

	@Override
	public double getMountedYOffset() {
		if (riddenByEntity instanceof EntityPlayer) {
			return (double)height * 0.0D + 0.1D;
		}
		if (riddenByEntity instanceof EntitySpider) {
			return (double)height * 0.0D - 0.1D;
		}
		if (riddenByEntity instanceof EntitySkeleton ||
				riddenByEntity instanceof EntityZombie ||
				riddenByEntity instanceof EntityEnderman) {
			return (double)height * 0.0D - 0.4D;
		}
		// 特殊機対応
		if (riddenByEntity.getClass().getSimpleName().compareTo("Melo_e") == 0) {
			return (double)height * 0.0D - 0.4D;
		}
		
		return (double)height * 0.0D + 0.1D;
	}

	@Override
	public String getTexture() {
		return mod_VZN_zabuton.textureNames[color];
	}

	@Override
	public boolean handleWaterMovement() {
		// 独自の水没判定
		int var4 = MathHelper.floor_double(boundingBox.minX);
		int var5 = MathHelper.floor_double(boundingBox.maxX + 1.0D);
		int var6 = MathHelper.floor_double(boundingBox.minY);
		int var7 = MathHelper.floor_double(boundingBox.maxY + 1.0D);
		int var8 = MathHelper.floor_double(boundingBox.minZ);
		int var9 = MathHelper.floor_double(boundingBox.maxZ + 1.0D);
		
		if (!worldObj.checkChunksExist(var4, var6, var8, var5, var7, var9))
		{
			return false;
		}
		else
		{
			boolean var10 = false;
			
			for (int var12 = var4; var12 < var5; ++var12)
			{
				for (int var13 = var6; var13 < var7; ++var13)
				{
					for (int var14 = var8; var14 < var9; ++var14)
					{
						Block var15 = Block.blocksList[worldObj.getBlockId(var12, var13, var14)];
						
						if (var15 != null && var15.blockMaterial == Material.water)
						{
							double var16 = (double)((float)(var13 + 1) - BlockFluid.getFluidHeightPercent(worldObj.getBlockMetadata(var12, var13, var14)));
							
							if ((double)var7 >= var16)
							{
								var10 = true;
							}
						}
					}
				}
			}
			return var10;
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		Entity entity = damagesource.getEntity();
		if(worldObj.isRemote || isDead) {
			return true;
		}
		setBeenAttacked();
		if (entity instanceof EntityPlayer) {
			entityDropItem(new ItemStack(mod_VZN_zabuton.zabuton, 1, color), 0.0F);
			setDead();
		} else {
			health -= i;
			if(health <= 0) {
				setDead();
			}
		}
		if (isDead && riddenByEntity != null) {
			riddenByEntity.mountEntity(null);
			setRiddenByEntityID(riddenByEntity);
		}
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public void setPositionAndRotation2(double px, double py, double pz, float f, float f1, int i) {
//        this.setPosition(px, py, pz);
//        this.setRotation(f, f1);
		this.boatPosRotationIncrements = i + 5;
		
		
		this.zabutonX = px;
		this.zabutonY = py;
		this.zabutonZ = pz;
		this.zabutonYaw = (double)f;
		this.zabutonPitch = (double)f1;

//        motionX = velocityX;
//        motionY = velocityY;
//        motionZ = velocityZ;
	}

	@Override
	public void setVelocity(double d, double d1, double d2) {
		velocityX = motionX = d;
		velocityY = motionY = d1;
		velocityZ = motionZ = d2;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		// ボートの判定のコピー
		// ボートは直接サーバーと位置情報を同期させているわけではなく、予測位置計算系に値を渡している。
		// 因みにボートの座標同期間隔は結構長めなので動きが変。
		
		
		double var6;
		double var8;
		double var12;
		double var26;
		double var24 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		
		if (this.worldObj.isRemote) {
			// Client
			if (this.boatPosRotationIncrements > 0)
			{
				var6 = this.posX + (this.zabutonX - this.posX) / (double)this.boatPosRotationIncrements;
				var8 = this.posY + (this.zabutonY - this.posY) / (double)this.boatPosRotationIncrements;
				var26 = this.posZ + (this.zabutonZ - this.posZ) / (double)this.boatPosRotationIncrements;
				var12 = MathHelper.wrapAngleTo180_double(this.zabutonYaw - (double)this.rotationYaw);
				this.rotationYaw = (float)((double)this.rotationYaw + var12 / (double)this.boatPosRotationIncrements);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.zabutonPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(var6, var8, var26);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			}
			else
			{
				motionY -= 0.08D;
				if (this.onGround)
				{
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
					setDispensed(false);
					
				}
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
			
			// TODO:特殊処理
			if (this.riddenByEntity instanceof EntityLiving) { 
				EntityLiving lel = (EntityLiving)riddenByEntity;
				// 座ってる間は消滅させない
				lel.entityAge = 0;
			}
			
		}
		else
		{
			// Server
			// 落下
			motionY -= 0.08D;
			
			// 搭乗者によるベクトル操作
			if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer)
			{
				this.motionX += this.riddenByEntity.motionX * 0.2D;
				this.motionZ += this.riddenByEntity.motionZ * 0.2D;
			}
			
			// 最高速度判定
			Double lmaxspeed = isDispensed() ? 10.0D : 0.35D;
			var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			if (var6 > lmaxspeed)
			{
				var8 = lmaxspeed / var6;
				this.motionX *= var8;
				this.motionZ *= var8;
				var6 = lmaxspeed;
			}
			
			
			if (this.onGround)
			{
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
				setDispensed(false);
				// setVelocityの呼ばれる回数が少なくて変な動きをするので対策
//                this.velocityChanged = true;
			}
			
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			
			this.motionX *= 0.9900000095367432D;
			this.motionY *= 0.949999988079071D;
			this.motionZ *= 0.9900000095367432D;
			
			// ヘッディング
			this.rotationPitch = 0.0F;
			var8 = (double)this.rotationYaw;
			var26 = this.prevPosX - this.posX;
			var12 = this.prevPosZ - this.posZ;
			
			if (var26 * var26 + var12 * var12 > 0.001D)
			{
				var8 = (double)((float)(Math.atan2(var12, var26) * 180.0D / Math.PI));
			}
			
			double var14 = MathHelper.wrapAngleTo180_double(var8 - (double)this.rotationYaw);
			
			if (var14 > 20.0D)
			{
				var14 = 20.0D;
			}
			
			if (var14 < -20.0D)
			{
				var14 = -20.0D;
			}
			
			this.rotationYaw = (float)((double)this.rotationYaw + var14);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			
//            if (!this.worldObj.isRemote)
			{
				// 当たり判定
				List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.17D, 0.0D, 0.17D));
				
				if (var16 != null && !var16.isEmpty())
				{
					Iterator var28 = var16.iterator();
					
					while (var28.hasNext())
					{
						Entity var18 = (Entity)var28.next();
						
						if (var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof VZN_EntityZabuton)
						{
							var18.applyEntityCollision(this);
						}
					}
				}
				
			}
		}
		if (this.riddenByEntity != null) {
			if (this.riddenByEntity instanceof EntityLiving) { 
				// 座ってる間は消滅させない
				((EntityLiving)riddenByEntity).entityAge = 0;
			}
			if (riddenByEntity.isDead) {
				// 着座対象が死んだら無人化
				riddenByEntity = null;
				setRiddenByEntityID(riddenByEntity);
			} 
			else if (inWater) {
				// ぬれた座布団はひゃぁってなる
				riddenByEntity.mountEntity(null);
				setRiddenByEntityID(riddenByEntity);
			}
		}
	}

	@Override
    public void applyEntityCollision(Entity entity) {
		// 吸着判定
    	if (worldObj.isRemote) {
            return;
        }
        if (entity == riddenByEntity) {
            return;
        }
        if ((entity instanceof EntityLiving) && !(entity instanceof EntityPlayer) && riddenByEntity == null && entity.ridingEntity == null) {
        	entity.mountEntity(this);
        	setRiddenByEntityID(riddenByEntity);
        }
        super.applyEntityCollision(entity);
    }
    
	@Override
    public boolean interact(EntityPlayer entityplayer) {
		// ラーイド・オン！
        if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer) {
            return true;
        }
        if (!worldObj.isRemote) {
//        	if (riddenByEntity != null && !(riddenByEntity instanceof EntityPlayer)) {
//            	riddenByEntity.mountEntity(null);
//        	}
            entityplayer.mountEntity(this);
//            setRiddenByEntityID(riddenByEntity);
        }
        return true;
    }

	// 射出判定
	public boolean isDispensed() {
		return dataWatcher.getWatchableObjectByte(17) > 0x00;
	}
	
	public void setDispensed(boolean isDispensed) {
		dataWatcher.updateObject(17, (byte)(isDispensed ? 0x01 : 0x00));
	}
	
	// クライアント側補正用
	public int getRiddenByEntityID() {
		int li = dataWatcher.getWatchableObjectInt(18);
		return li;
	}
	
	public Entity getRiddenByEntity() {
		return ((WorldClient)worldObj).getEntityByID(getRiddenByEntityID());
	}
	
	public void setRiddenByEntityID(Entity pentity) {
		dataWatcher.updateObject(18, Integer.valueOf(pentity == null ? 0 : pentity.entityId));
	}

}
