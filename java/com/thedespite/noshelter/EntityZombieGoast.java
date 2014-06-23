package com.thedespite.noshelter;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityZombieGoast extends EntityZombie {
	
	World w;
	Minecraft mc;
	MinecraftServer ms;
	Random rnd;
	
	
	public EntityZombieGoast(World w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.monster.EntityZombie#onLivingUpdate()
	 */
	@Override
	public void onLivingUpdate() {
		// TODO Auto-generated method stub
		
		if (mc == null)
			mc = Minecraft.getMinecraft();
		
		if (mc.isIntegratedServerRunning() == true)
		{
			if (mc != null)
				if (mc.thePlayer != null)
				{
					if (ms == null)
						ms = FMLCommonHandler.instance().getMinecraftServerInstance();
					
					if (w == null)
						w = ms.worldServers[0];
				
					EntityPlayerMP closestP = null;
					
					for	(int i = 0; i < w.playerEntities.size(); i++)
					{
						if (closestP == null || this.getDistanceToEntity((Entity) w.playerEntities.get(i)) < this.getDistanceToEntity(closestP))
							closestP = (EntityPlayerMP) w.playerEntities.get(i);
					}
					
					int yOff = 0;
					if (closestP.posY < this.posY - 2)
						yOff = -1;
					else if (closestP.posY > this.posY + 1)
						yOff = 1;
					
					int x = (int)(this.posX + this.getLookVec().xCoord);
					int y = (int)(this.posY + this.getLookVec().yCoord);
					int z = (int)(this.posZ + this.getLookVec().zCoord);
					
					int d = closestP.getEntityId();
					
					if (yOff == -1 && this.posY - closestP.posY > closestP.getDistanceToEntity(this) / 2)
					{
						//w.setBlockToAir(x, y, z);
						//w.setBlockToAir(x, y - 1, z);
						
						//w.destroyBlockInWorldPartially(d, x, y, z, 100);
						//w.destroyBlockInWorldPartially(d, x, y - 1, z, 100);
						
						RemBlock(x, y, z, closestP);
						RemBlock(x, y - 1, z, closestP);
					}
					
					else if (yOff == 1)
					{
						//w.setBlockToAir(x, y + 2, z);
						//w.setBlockToAir(x, y + 3, z);
						//w.destroyBlockInWorldPartially(d, x, y + 2, z, 100);
						//w.destroyBlockInWorldPartially(d, x, y + 3, z, 100);
						
						RemBlock(x, y + 2, z, closestP);
						RemBlock(x, y + 3, z, closestP);
					}
					
					else
					{
						//w.setBlockToAir(x, y + 1, z);
						//w.setBlockToAir(x, y + 2, z); 
						//w.destroyBlockInWorldPartially(d, x, y + 1, z, 100);
						//w.destroyBlockInWorldPartially(d, x, y + 2, z, 100);
						
						RemBlock(x, y + 1, z, closestP);
						RemBlock(x, y + 2, z, closestP);
					}
					
					if ((!w.isDaytime() || w.isRaining() || w.isThundering()) && yOff == 1 && this.onGround && !this.isInWater() && closestP.posY - this.posY > closestP.getDistanceToEntity(this) / 2)
					{
						if (closestP != null)
						{
							Vec3 pos = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
							Vec3 tar = Vec3.createVectorHelper(closestP.posX, closestP.posY, closestP.posZ);
							
							Vec3 dir = pos.subtract(tar).normalize();
							//Vec3 dir = tar.subtract(pos).normalize();
							
							//System.out.println("" + dir.xCoord + " : " + dir.yCoord + " : " + dir.zCoord);
							
							Vec3 fwd = pos.addVector(dir.xCoord, dir.yCoord, dir.zCoord);
							
							if (w.getBlock((int)fwd.xCoord, (int)fwd.yCoord - 1, (int)fwd.zCoord) == Blocks.air)
								w.setBlock((int)fwd.xCoord, (int)fwd.yCoord - 1, (int)fwd.zCoord, ZombieBlock.self);
							
							Vec3 fwd2 = pos.addVector(dir.xCoord * 2, dir.yCoord, dir.zCoord * 2);
							
							if (w.getBlock((int)fwd2.xCoord, (int)fwd2.yCoord, (int)fwd2.zCoord) == Blocks.air)
								w.setBlock((int)fwd2.xCoord, (int)fwd2.yCoord, (int)fwd2.zCoord, ZombieBlock.self);
						}
					}
					
					if (this.entityToAttack == null)
					{
						this.setAttackTarget(closestP);
						//this.entityToAttack = closestP;
						//this.setRevengeTarget(closestP);
					}
				}
		}
		
		super.onLivingUpdate();
	}
	
	protected void RemBlock(int x, int y, int z, EntityPlayerMP p)
	{
		if (rnd == null)
			rnd = new Random();
		
		if (rnd.nextInt(32) == 0)
		{
			Block b = w.getBlock(x, y, z);
			//b.onBlockExploded(ww, x, y, z, null);
			
			if (b.isNormalCube())
			{
				this.playSound("mob.zombie.metal", 1.0f, rnd.nextFloat());
				
				// TODO
				//b.dropBlockAsItemWithChance(w, x, y, z, w.getBlockMetadata(x, y, z), 1.0F, 0);
				
				w.setBlockToAir(x, y, z);
			}
		}
			//ww.setBlockToAir(x, y, z);
	}
}
