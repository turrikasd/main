package com.thedespite.noshelter;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
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
import net.minecraftforge.event.world.WorldEvent.PotentialSpawns;

public class EventHandlerClass {

	private int spawnTime = 11000;
	private boolean isD = false;
	World w;
	long next = 0;
	private final Lock lock = new ReentrantLock();
	Random rnd;
	Minecraft mc;
	MinecraftServer ms;
	
	@SubscribeEvent
	public void tickEvent(TickEvent event)
	{
		if (mc == null)
			mc = Minecraft.getMinecraft();
		
		if (true && mc.isIntegratedServerRunning() == true && event.type == TickEvent.Type.SERVER && Minecraft.getSystemTime() > next)
		{
			if (ms == null)
				ms = FMLCommonHandler.instance().getMinecraftServerInstance();
			
			if (w == null)
				w = ms.worldServers[0];
		
			long date = w.getWorldTime() / 24000L;
			long thclk = w.getWorldTime() / 12000L;
			
			boolean isDay = false;
			if (thclk % 2L == 0)
				isDay = true;
					
			if (!isD && date != 0 && date % 2L == 0 && !w.isDaytime())
			{
				isD = true;
				spawnTime -= 1000;
				
				mc.thePlayer.sendChatMessage("It's time to kill zombie goast");
			}
			
			else if (isDay && isD)
			{
				isD = false;
				mc.thePlayer.sendChatMessage("Is nolonger time to kill zombie goast");
			}
			
			if (isD && mc.thePlayer != null && lock.tryLock())
			{
				if (rnd == null)
					rnd = new Random();
				
				EntityPlayer p = (EntityPlayer) w.playerEntities.get(rnd.nextInt(w.playerEntities.size()));

				int x = (int) (p.posX + rnd.nextInt(32) - 16);
				int z = (int) (p.posZ + rnd.nextInt(32) - 16);
				
				int y;
				
				if (w.isDaytime())
					y = (int) (p.posY - 48);
				else // night
					y = (int) (p.posY - 24);
				
				if (y < 0)
					y = 0;
				
				for (; y < 256; y++)
				{
					if (w.isAirBlock(x, y, z))
						break;
				}
				
				//mc.thePlayer.sendChatMessage("" + x + ":" + y + ":" + z);
				
				if (y != 256 && !w.isDaytime() || w.isBlockNormalCubeDefault((int)x, (int)y, (int)z, false) || w.isThundering() || w.getBlockLightValue((int)x, (int)y + 1, (int)z) < 7)
				{
					for (int i = 0; i < 1; i++)
					{
						//Minecraft.getMinecraft().thePlayer.sendChatMessage("Spawning ZombieGoast or creepa");
						
						int xx = (int)x;
						int yy = (int)y;
						int zz = (int)z;
						//int xx = (int) (p.posX + 5);
						//int yy = (int) p.posY;
						//int zz = (int) (p.posZ + 5);
						
						yy += 2 + i;
						
						if (rnd.nextInt(8) == 0)
							SpawnCD(xx, yy, zz);
						else
							SpawnZG(xx, yy, zz);
					}
					
					next = Minecraft.getSystemTime() + spawnTime;
					
					
				}
				
				lock.unlock();
			}
		}
	}
	
	//@SubscribeEvent
	public void livingEvent(LivingEvent event)
	{
		if (false && !event.entityLiving.isDead && Minecraft.getMinecraft().isIntegratedServerRunning() == true && !(event.entity instanceof EntityPlayer) && Minecraft.getSystemTime() > next)
		{
			MinecraftServer ms = FMLCommonHandler.instance().getMinecraftServerInstance();
			w = ms.worldServers[0];
			
			if ((!w.isDaytime() || w.isBlockNormalCubeDefault((int)event.entity.posX, (int)event.entity.posY, (int)event.entity.posZ, false) || w.isThundering() || w.getBlockLightValue((int)event.entity.posX, (int)event.entity.posY + 1, (int)event.entity.posZ) < 7) && lock.tryLock())
			{
				boolean closeToPlayer = false;
				
				for (int i = 0; i < w.playerEntities.size(); i++)
				{
					EntityPlayer p = (EntityPlayer) w.playerEntities.get(i); 
					if (!p.isDead && event.entityLiving.getDistanceToEntity(p) < 64.0d && !p.isDead && event.entityLiving.getDistanceToEntity(p) > 16.0d)
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
					
					next = Minecraft.getSystemTime() + 5000;
					lock.unlock();
				}
			}
		}
	}
	
	//@SubscribeEvent
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
