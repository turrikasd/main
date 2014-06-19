package com.thedespite.noshelter;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.block.BlockSnow;

public class ZombieBlock extends Block {

	@Override
	public void onNeighborBlockChange(World w, int x,
			int y, int z, Block b) {
		// TODO Auto-generated method stub
		
		if (w.isDaytime() && !w.isRaining() && !w.isThundering())
			w.setBlockToAir(x, y, z);
		
		w.scheduleBlockUpdate(x, y, z, self, 100);
	}

	@Override
	public void updateTick(World w, int x, int y,
			int z, Random p_149674_5_) {
		
		if (w.isDaytime() && !w.isRaining() && !w.isThundering())
			w.setBlockToAir(x, y, z);
		
		w.scheduleBlockUpdate(x, y, z, self, 100);
	}

	@Override
	public int tickRate(World p_149738_1_) {
		// TODO Auto-generated method stub
		
		return super.tickRate(p_149738_1_);
	}

	public static Block self;
	
	public static void InitSelf()
	{
    	self = new ZombieBlock(Material.ground)
        .setHardness(0.2F).setStepSound(Block.soundTypeGravel)
        .setBlockName("ZombieBlock").setCreativeTab(CreativeTabs.tabBlock)
        .setBlockTextureName("wool_colored")
        .setTickRandomly(true);
    	
    	GameRegistry.registerBlock(self, "ZombieBlock");
    	
    	System.out.println("ID: " + Block.getIdFromBlock(self));
	}
	
	protected ZombieBlock(Material mat) {
		super(mat);
		
		this.setTickRandomly(true);
		// TODO Auto-generated constructor stub
	}

}
