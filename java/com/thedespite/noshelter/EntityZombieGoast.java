package com.thedespite.noshelter;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityZombieGoast extends EntityZombie {
	
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
		
		//Minecraft.getMinecraft().thePlayer.sendChatMessage("Living Update");
		//System.out.println("Living Update");
		
		
		if (Minecraft.getMinecraft().isIntegratedServerRunning() == true)
		{
			if (Minecraft.getMinecraft() != null)
				if (Minecraft.getMinecraft().thePlayer != null)
				{
					MinecraftServer ms = FMLCommonHandler.instance().getMinecraftServerInstance();
					World w = ms.worldServers[0];
				
					EntityPlayer closestP = null;
					
					for	(int i = 0; i < w.playerEntities.size(); i++)
					{
						if (closestP == null || this.getDistanceToEntity((Entity) w.playerEntities.get(i)) < this.getDistanceToEntity(closestP))
							closestP = (EntityPlayer) w.playerEntities.get(i);
					}
					
					int yOff = 0;
					if (closestP.posY < this.posY - 2)
						yOff = -1;
					else if (closestP.posY > this.posY + 1)
						yOff = 1;
					
					int x = (int)(this.posX + this.getLookVec().xCoord);
					int y = (int)(this.posY + this.getLookVec().yCoord);
					int z = (int)(this.posZ + this.getLookVec().zCoord);
					
					if (yOff == -1 && this.posY - closestP.posY > closestP.getDistanceToEntity(this) / 2)
					{
						w.setBlockToAir(x, y, z);
						w.setBlockToAir(x, y - 1, z);
					}
					
					else if (yOff == 1)
					{
						w.setBlockToAir(x, y + 2, z);
						w.setBlockToAir(x, y + 3, z);
					}
					
					else
					{
						w.setBlockToAir(x, y + 1, z);
						w.setBlockToAir(x, y + 2, z); 
					}
					
					if (yOff == 1 && closestP.posY - this.posY > closestP.getDistanceToEntity(this) / 2)
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
								w.setBlock((int)fwd.xCoord, (int)fwd.yCoord - 1, (int)fwd.zCoord, Blocks.dirt);
							
							Vec3 fwd2 = pos.addVector(dir.xCoord * 2, dir.yCoord, dir.zCoord * 2);
							
							if (w.getBlock((int)fwd2.xCoord, (int)fwd2.yCoord, (int)fwd2.zCoord) == Blocks.air)
								w.setBlock((int)fwd2.xCoord, (int)fwd2.yCoord, (int)fwd2.zCoord, Blocks.dirt);
						}
						
						else if (false)
						{
							if (w.getBlock(x, y - 1, z) == Blocks.air)
								w.setBlock(x, y - 1, z, Blocks.dirt);
							
							int xx = (int)(this.posX + 2 * this.getLookVec().xCoord);
							int zz = (int)(this.posZ + 2 * this.getLookVec().zCoord);
							
							if (w.getBlock(xx, y, zz) == Blocks.air)
								w.setBlock(xx, y, zz, Blocks.dirt);
						}
					}
					
					if (this.entityToAttack == null)
					{
						this.setAttackTarget(closestP);
					}
				}
		}
		
		super.onLivingUpdate();
	}
}
