package com.thedespite.noshelter;

import java.lang.reflect.Method;

import scala.reflect.internal.Trees.This;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class EntityCreeperDang extends EntityCreeper {

	private double lastDistToPlayer = 10000.0d;
	private int timesNoProgress = 0;
	private int explosionRadius = 3;
	
	public EntityCreeperDang(World w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.monster.EntityMob#onLivingUpdate()
	 */
	@Override
	public void onLivingUpdate() {
		// TODO Auto-generated method stub
		
		if (Minecraft.getMinecraft().isIntegratedServerRunning())
		{
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
				
				double newDist = this.getDistanceToEntity(closestP);
				if (newDist >= lastDistToPlayer - 5.0d)
				{
					timesNoProgress++;
				}
				
				else
				{
					timesNoProgress = 0;
					lastDistToPlayer = newDist;
				}
				
				if (timesNoProgress >= 180 && newDist < 8.0d)
				{
					this.setCreeperState(1);
			        func_146077_cc();
				}
				
				if (this.entityToAttack == null)
				{					
					this.setAttackTarget(closestP);
				}
			}
		}
		
		super.onLivingUpdate();
	}
	
	// copied from superclass
    private void func_146077_cc()
    {
        if (!this.worldObj.isRemote)
        {
            boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");

            if (this.getPowered())
            {
                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(this.explosionRadius * 2), flag);
            }
            else
            {
                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, flag);
            }

            this.setDead();
        }
    }
}
