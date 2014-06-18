package com.thedespite.noshelter;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = NoShelterMain.MODID, version = NoShelterMain.VERSION)
public class NoShelterMain
{
    public static final String MODID = "NoShelter";
    public static final String VERSION = "1.7";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        //System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    	
    	MinecraftForge.EVENT_BUS.register(new EventHandlerClass());
    	EntityRegistry.registerModEntity(EntityZombieGoast.class, "ZombieGoast", 0, this, 120, 1, true);
    	EntityRegistry.registerModEntity(EntityCreeperDang.class, "SmartCreeper", 1, this, 120, 1, true);
    	
    	for	(int i = 0; i < 40; i++)
    	{
    		EntityRegistry.addSpawn(EntityZombieGoast.class, 2, 0, 1, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    		EntityRegistry.addSpawn(EntityCreeperDang.class, 2, 0, 1, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    	}
    }
}
