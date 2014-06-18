package com.thedespite.noshelter;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

public class EventHandlerClass {

	
	World w;
	long next = 0;
	private final Lock lock = new ReentrantLock();
	
	
	@SubscribeEvent
	public void livingEvent(LivingEvent event)
	{
		if (!event.entityLiving.isDead && Minecraft.getMinecraft().isIntegratedServerRunning() == true && !(event.entity instanceof EntityPlayer) && Minecraft.getSystemTime() > next)
		{
			MinecraftServer ms = FMLCommonHandler.instance().getMinecraftServerInstance();
			w = ms.worldServers[0];
			
			if ((!w.isDaytime() || w.isRaining() || w.isThundering() || w.getBlockLightValue((int)event.entity.posX, (int)event.entity.posY + 1, (int)event.entity.posZ) < 7) && lock.tryLock())
			{
				boolean closeToPlayer = false;
				
				for (int i = 0; i < w.playerEntities.size(); i++)
				{
					EntityPlayer p = (EntityPlayer) w.playerEntities.get(i); 
					if (!p.isDead && event.entityLiving.getDistanceToEntity(p) < 30.0d)
						closeToPlayer = true;
				}
				
				if (closeToPlayer)
				{
					for (int i = 0; i < 1; i++)
					{
						int x = (int)event.entity.posX;
						int y = (int)event.entity.posY;
						int z = (int)event.entity.posZ;
						
						y += 1 + i;
						
						if (new Random().nextInt(4) == 0)
							SpawnCD(x, y, z);
						else
							SpawnZG(x, y, z);
					}
					
					next = Minecraft.getSystemTime() + 1000;
					lock.unlock();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void blockEvent(BlockEvent event)
	{
		/*MinecraftServer ms = FMLCommonHandler.instance().getMinecraftServerInstance();
		w = ms.worldServers[0];
		
		SpawnZG(event.x, event.y + 1, event.z);*/
	}
	
	private void SpawnZG(double x, double y, double z)
	{
		EntityZombieGoast ezg = new EntityZombieGoast(w);
		ezg.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
		
		w.spawnEntityInWorld(ezg);
	}	
	
	private void SpawnCD(double x, double y, double z)
	{
		EntityCreeperDang ecd = new EntityCreeperDang(w);
		ecd.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
		
		w.spawnEntityInWorld(ecd);
	}
}
