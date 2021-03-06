/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.client.render.effects;


import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import appeng.api.util.AEPartLocation;
import appeng.client.texture.ExtraBlockTextures;


@SideOnly( Side.CLIENT )
public class CraftingFx extends ParticleBreaking
{

	private final TextureAtlasSprite particleTextureIndex;

	private final int startBlkX;
	private final int startBlkY;
	private final int startBlkZ;

	public CraftingFx( final World par1World, final double par2, final double par4, final double par6, final Item par8Item )
	{
		super( par1World, par2, par4, par6, par8Item );
		this.particleGravity = 0;
		this.particleBlue = 1;
		this.particleGreen = 0.9f;
		this.particleRed = 1;
		this.particleAlpha = 1.3f;
		this.particleScale = 1.5f;
		this.particleTextureIndex = ExtraBlockTextures.BlockEnergyParticle.getIcon().getAtlas();
		this.particleMaxAge /= 1.2;

		this.startBlkX = MathHelper.floor_double( this.posX );
		this.startBlkY = MathHelper.floor_double( this.posY );
		this.startBlkZ = MathHelper.floor_double( this.posZ );
	}

	@Override
	public int getFXLayer()
	{
		return 1;
	}

	@Override
	public void renderParticle( final VertexBuffer par1Tessellator, final Entity p_180434_2_, final float partialTick, final float x, final float y, final float z, final float rx, final float rz )
	{
		if( partialTick < 0 || partialTick > 1 )
		{
			return;
		}

		final float f6 = this.particleTextureIndex.getMinU();
		final float f7 = this.particleTextureIndex.getMaxU();
		final float f8 = this.particleTextureIndex.getMinV();
		final float f9 = this.particleTextureIndex.getMaxV();
		final float scale = 0.1F * this.particleScale;

		float offX = (float) ( this.prevPosX + ( this.posX - this.prevPosX ) * partialTick );
		float offY = (float) ( this.prevPosY + ( this.posY - this.prevPosY ) * partialTick );
		float offZ = (float) ( this.prevPosZ + ( this.posZ - this.prevPosZ ) * partialTick );

		final int blkX = MathHelper.floor_double( offX );
		final int blkY = MathHelper.floor_double( offY );
		final int blkZ = MathHelper.floor_double( offZ );
		if( blkX == this.startBlkX && blkY == this.startBlkY && blkZ == this.startBlkZ )
		{
			offX -= interpPosX;
			offY -= interpPosY;
			offZ -= interpPosZ;

			// AELog.info( "" + partialTick );
			final float f14 = 1.0F;
			par1Tessellator.color( this.particleRed * f14, this.particleGreen * f14, this.particleBlue * f14, this.particleAlpha ).pos( offX - x * scale - rx * scale, offY - y * scale, offZ - z * scale - rz * scale ).tex( f7, f9 ).endVertex();
			par1Tessellator.color( this.particleRed * f14, this.particleGreen * f14, this.particleBlue * f14, this.particleAlpha ).pos( offX - x * scale + rx * scale, offY + y * scale, offZ - z * scale + rz * scale ).tex( f7, f8 ).endVertex();
			par1Tessellator.color( this.particleRed * f14, this.particleGreen * f14, this.particleBlue * f14, this.particleAlpha ).pos( offX + x * scale + rx * scale, offY + y * scale, offZ + z * scale + rz * scale ).tex( f6, f8 ).endVertex();
			par1Tessellator.color( this.particleRed * f14, this.particleGreen * f14, this.particleBlue * f14, this.particleAlpha ).pos( offX + x * scale - rx * scale, offY - y * scale, offZ + z * scale - rz * scale ).tex( f6, f9 ).endVertex();
		}
	}

	public void fromItem( final AEPartLocation d )
	{
		this.posX += 0.2 * d.xOffset;
		this.posY += 0.2 * d.yOffset;
		this.posZ += 0.2 * d.zOffset;
		this.particleScale *= 0.8f;
	}

	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.motionY -= 0.04D * (double)this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
		this.particleScale *= 0.51f;
		this.particleAlpha *= 0.51f;
	}

	public void setMotionX( float motionX )
	{
		this.motionX = motionX;
	}

	public void setMotionY( float motionY )
	{
		this.motionY = motionY;
	}

	public void setMotionZ( float motionZ )
	{
		this.motionZ = motionZ;
	}
}
