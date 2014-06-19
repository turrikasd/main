package com.thedespite.noshelter;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

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
    	FMLCommonHandler.instance().bus().register(new EventHandlerClass());
    	
    	EntityRegistry.registerModEntity(EntityZombieGoast.class, "ZombieGoast", 0, this, 120, 1, true);
    	EntityRegistry.registerModEntity(EntityCreeperDang.class, "SmartCreeper", 1, this, 120, 1, true);
    	
    	for	(int i = 0; i < 40; i++)
    	{
    		EntityRegistry.addSpawn(EntityZombieGoast.class, 200, 0, 1, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    		EntityRegistry.addSpawn(EntityCreeperDang.class, 200, 0, 1, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    		
    		EntityRegistry.removeSpawn(EntityCreeper.class, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    		EntityRegistry.removeSpawn(EntityZombie.class, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    		EntityRegistry.removeSpawn(EntitySkeleton.class, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    		EntityRegistry.removeSpawn(EntitySpider.class, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
    	}
    	
    	ZombieBlock.InitSelf();
    }
}
