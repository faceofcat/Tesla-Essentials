package com.trials.modsquad.Entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

import java.util.List;

/**
 * Created by Tjeltigre on 9/9/2016.
 */
public class Dragon2dot0 extends EntityDragon {
    private final PhaseManager phaseManager;
    private final DragonFightManager fightManager;
    private int growlTime = 200;

    public Dragon2dot0(World worldIn){
        super(worldIn);
        this.growlTime = 100;
        if (!worldIn.isRemote && worldIn.provider instanceof WorldProviderEnd)
        {
            this.fightManager = ((WorldProviderEnd)worldIn.provider).getDragonFightManager();
        }
        else
        {
            this.fightManager = null;
        }
        this.phaseManager = new PhaseManager((EntityDragon)this);
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.worldObj.isRemote)
        {
            this.setHealth(this.getHealth());

            if (!this.isSilent())
            {
                float f = MathHelper.cos(this.animTime * ((float)Math.PI * 2F));
                float f1 = MathHelper.cos(this.prevAnimTime * ((float)Math.PI * 2F));

                if (f1 <= -0.3F && f >= -0.3F)
                {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, this.getSoundCategory(), 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
                }

                if (!this.phaseManager.getCurrentPhase().getIsStationary() && --this.growlTime < 0)
                {
                    this.worldObj.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, this.getSoundCategory(), 2.5F, 0.8F + this.rand.nextFloat() * 0.3F, false);
                    this.growlTime = 200 + this.rand.nextInt(200);
                }
            }
        }

        this.prevAnimTime = this.animTime;

        if (this.getHealth() <= 0.0F)
        {
            float f13 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f15 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f17 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f13, this.posY + 2.0D + (double)f15, this.posZ + (double)f17, 0.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            this.updateDragonEnderCrystal();
            float f12 = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
            f12 = f12 * (float)Math.pow(2.0D, this.motionY);

            if (this.phaseManager.getCurrentPhase().getIsStationary())
            {
                this.animTime += 0.1F;
            }
            else if (this.slowed)
            {
                this.animTime += f12 * 0.5F;
            }
            else
            {
                this.animTime += f12;
            }

            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);

            if (this.isAIDisabled())
            {
                this.animTime = 0.5F;
            }
            else
            {
                if (this.ringBufferIndex < 0)
                {
                    for (int i = 0; i < this.ringBuffer.length; ++i)
                    {
                        this.ringBuffer[i][0] = (double)this.rotationYaw;
                        this.ringBuffer[i][1] = this.posY;
                    }
                }

                if (++this.ringBufferIndex == this.ringBuffer.length)
                {
                    this.ringBufferIndex = 0;
                }

                this.ringBuffer[this.ringBufferIndex][0] = (double)this.rotationYaw;
                this.ringBuffer[this.ringBufferIndex][1] = this.posY;

                if (this.worldObj.isRemote)
                {
                    if (this.newPosRotationIncrements > 0)
                    {
                        double d5 = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
                        double d0 = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
                        double d1 = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
                        double d2 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
                        this.rotationYaw = (float)((double)this.rotationYaw + d2 / (double)this.newPosRotationIncrements);
                        this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d5, d0, d1);
                        this.setRotation(this.rotationYaw, this.rotationPitch);
                    }

                    this.phaseManager.getCurrentPhase().doClientRenderEffects();
                }
                else
                {
                    IPhase iphase = this.phaseManager.getCurrentPhase();
                    iphase.doLocalUpdate();

                    if (this.phaseManager.getCurrentPhase() != iphase)
                    {
                        iphase = this.phaseManager.getCurrentPhase();
                        iphase.doLocalUpdate();
                    }

                    Vec3d vec3d = iphase.getTargetLocation();

                    if (vec3d != null)
                    {
                        double d6 = vec3d.xCoord - this.posX;
                        double d7 = vec3d.yCoord - this.posY;
                        double d8 = vec3d.zCoord - this.posZ;
                        double d3 = d6 * d6 + d7 * d7 + d8 * d8;
                        float f6 = iphase.getMaxRiseOrFall();
                        d7 = MathHelper.clamp_double(d7 / (double)MathHelper.sqrt_double(d6 * d6 + d8 * d8), (double)(-f6), (double)f6);
                        this.motionY += d7 * 0.10000000149011612D;
                        this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
                        double d4 = MathHelper.clamp_double(MathHelper.wrapDegrees(180.0D - MathHelper.atan2(d6, d8) * (180D / Math.PI) - (double)this.rotationYaw), -50.0D, 50.0D);
                        Vec3d vec3d1 = (new Vec3d(vec3d.xCoord - this.posX, vec3d.yCoord - this.posY, vec3d.zCoord - this.posZ)).normalize();
                        Vec3d vec3d2 = (new Vec3d((double)MathHelper.sin(this.rotationYaw * 0.017453292F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)))).normalize();
                        float f8 = Math.max(((float)vec3d2.dotProduct(vec3d1) + 0.5F) / 1.5F, 0.0F);
                        this.randomYawVelocity *= 0.8F;
                        this.randomYawVelocity = (float)((double)this.randomYawVelocity + d4 * (double)iphase.getYawFactor());
                        this.rotationYaw += this.randomYawVelocity * 0.1F;
                        float f9 = (float)(2.0D / (d3 + 1.0D));
                        float f10 = 0.06F;
                        this.moveRelative(0.0F, -1.0F, 0.06F * (f8 * f9 + (1.0F - f9)));

                        if (this.slowed)
                        {
                            this.moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
                        }
                        else
                        {
                            this.moveEntity(this.motionX, this.motionY, this.motionZ);
                        }

                        Vec3d vec3d3 = (new Vec3d(this.motionX, this.motionY, this.motionZ)).normalize();
                        float f11 = ((float)vec3d3.dotProduct(vec3d2) + 1.0F) / 2.0F;
                        f11 = 0.8F + 0.15F * f11;
                        this.motionX *= (double)f11;
                        this.motionZ *= (double)f11;
                        this.motionY *= 0.9100000262260437D;
                    }
                }

                this.renderYawOffset = this.rotationYaw;
                this.dragonPartHead.width = 1.0F;
                this.dragonPartHead.height = 1.0F;
                this.dragonPartNeck.width = 3.0F;
                this.dragonPartNeck.height = 3.0F;
                this.dragonPartTail1.width = 2.0F;
                this.dragonPartTail1.height = 2.0F;
                this.dragonPartTail2.width = 2.0F;
                this.dragonPartTail2.height = 2.0F;
                this.dragonPartTail3.width = 2.0F;
                this.dragonPartTail3.height = 2.0F;
                this.dragonPartBody.height = 3.0F;
                this.dragonPartBody.width = 5.0F;
                this.dragonPartWing1.height = 2.0F;
                this.dragonPartWing1.width = 4.0F;
                this.dragonPartWing2.height = 3.0F;
                this.dragonPartWing2.width = 4.0F;
                float f14 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * 0.017453292F;
                float f16 = MathHelper.cos(f14);
                float f18 = MathHelper.sin(f14);
                float f2 = this.rotationYaw * 0.017453292F;
                float f19 = MathHelper.sin(f2);
                float f3 = MathHelper.cos(f2);
                this.dragonPartBody.onUpdate();
                this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f19 * 0.5F), this.posY, this.posZ - (double)(f3 * 0.5F), 0.0F, 0.0F);
                this.dragonPartWing1.onUpdate();
                this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(f3 * 4.5F), this.posY + 2.0D, this.posZ + (double)(f19 * 4.5F), 0.0F, 0.0F);
                this.dragonPartWing2.onUpdate();
                this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(f3 * 4.5F), this.posY + 2.0D, this.posZ - (double)(f19 * 4.5F), 0.0F, 0.0F);

                if (!this.worldObj.isRemote && this.hurtTime == 0)
                {
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expandXyz(1.0D)));
                    this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getEntityBoundingBox().expandXyz(1.0D)));
                }

                double[] adouble = this.getMovementOffsets(5, 1.0F);
                float f4 = MathHelper.sin(this.rotationYaw * 0.017453292F - this.randomYawVelocity * 0.01F);
                float f20 = MathHelper.cos(this.rotationYaw * 0.017453292F - this.randomYawVelocity * 0.01F);
                this.dragonPartHead.onUpdate();
                this.dragonPartNeck.onUpdate();
                float f5 = this.getHeadYOffset(1.0F);
                this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f4 * 6.5F * f16), this.posY + (double)f5 + (double)(f18 * 6.5F), this.posZ - (double)(f20 * 6.5F * f16), 0.0F, 0.0F);
                this.dragonPartNeck.setLocationAndAngles(this.posX + (double)(f4 * 5.5F * f16), this.posY + (double)f5 + (double)(f18 * 5.5F), this.posZ - (double)(f20 * 5.5F * f16), 0.0F, 0.0F);

                for (int j = 0; j < 3; ++j)
                {
                    EntityDragonPart entitydragonpart = null;

                    if (j == 0)
                    {
                        entitydragonpart = this.dragonPartTail1;
                    }

                    if (j == 1)
                    {
                        entitydragonpart = this.dragonPartTail2;
                    }

                    if (j == 2)
                    {
                        entitydragonpart = this.dragonPartTail3;
                    }

                    double[] adouble1 = this.getMovementOffsets(12 + j * 2, 1.0F);
                    float f21 = this.rotationYaw * 0.017453292F + this.simplifyAngle(adouble1[0] - adouble[0]) * 0.017453292F;
                    float f22 = MathHelper.sin(f21);
                    float f7 = MathHelper.cos(f21);
                    float f23 = 1.5F;
                    float f24 = (float)(j + 1) * 2.0F;
                    entitydragonpart.onUpdate();
                    entitydragonpart.setLocationAndAngles(this.posX - (double)((f19 * 1.5F + f22 * f24) * f16), this.posY + (adouble1[1] - adouble[1]) - (double)((f24 + 1.5F) * f18) + 1.5D, this.posZ + (double)((f3 * 1.5F + f7 * f24) * f16), 0.0F, 0.0F);
                }

                if (!this.worldObj.isRemote)
                {
                    this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartNeck.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());

                    if (this.fightManager != null)
                    {
                        this.fightManager.dragonUpdate(this);
                    }
                }
            }
        }
    }

    private void updateDragonEnderCrystal()
    {
        if (this.healingEnderCrystal != null)
        {
            if (this.healingEnderCrystal.isDead)
            {
                this.healingEnderCrystal = null;
            }
            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth())
            {
                this.setHealth(this.getHealth() + 1.0F);
            }
        }

        if (this.rand.nextInt(10) == 0)
        {
            List<EntityEnderCrystal> list = this.worldObj.<EntityEnderCrystal>getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expandXyz(32.0D));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;

            for (EntityEnderCrystal entityendercrystal1 : list)
            {
                double d1 = entityendercrystal1.getDistanceSqToEntity(this);

                if (d1 < d0)
                {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.healingEnderCrystal = entityendercrystal;
        }
    }

    private void collideWithEntities(List<Entity> p_70970_1_)
    {
        double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
        double d1 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;

        for (Entity entity : p_70970_1_)
        {
            if (entity instanceof EntityLivingBase)
            {
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;
                entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);

                if (!this.phaseManager.getCurrentPhase().getIsStationary() && ((EntityLivingBase)entity).getRevengeTimer() < entity.ticksExisted - 2)
                {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
                    this.applyEnchantments(this, entity);
                }
            }
        }
    }

    private void attackEntitiesInList(List<Entity> p_70971_1_)
    {
        for (int i = 0; i < p_70971_1_.size(); ++i)
        {
            Entity entity = (Entity)p_70971_1_.get(i);

            if (entity instanceof EntityLivingBase)
            {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
                this.applyEnchantments(this, entity);
            }
        }
    }

    private float getHeadYOffset(float p_184662_1_)
    {
        double d0;

        if (this.phaseManager.getCurrentPhase().getIsStationary())
        {
            d0 = -1.0D;
        }
        else
        {
            double[] adouble = this.getMovementOffsets(5, 1.0F);
            double[] adouble1 = this.getMovementOffsets(0, 1.0F);
            d0 = adouble[1] - adouble1[0];
        }

        return (float)d0;
    }

    private float simplifyAngle(double p_70973_1_)
    {
        return (float)MathHelper.wrapDegrees(p_70973_1_);
    }

    private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
    {
        int i = MathHelper.floor_double(p_70972_1_.minX);
        int j = MathHelper.floor_double(p_70972_1_.minY);
        int k = MathHelper.floor_double(p_70972_1_.minZ);
        int l = MathHelper.floor_double(p_70972_1_.maxX);
        int i1 = MathHelper.floor_double(p_70972_1_.maxY);
        int j1 = MathHelper.floor_double(p_70972_1_.maxZ);
        boolean flag = false;
        boolean flag1 = false;

        for (int k1 = i; k1 <= l; ++k1)
        {
            for (int l1 = j; l1 <= i1; ++l1)
            {
                for (int i2 = k; i2 <= j1; ++i2)
                {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (!block.isAir(iblockstate, this.worldObj, blockpos) && iblockstate.getMaterial() != Material.FIRE)
                    {
                        if (!this.worldObj.getGameRules().getBoolean("mobGriefing"))
                        {
                            flag = true;
                        }
                        else if (block.canEntityDestroy(iblockstate, this.worldObj, blockpos, this))
                        {
                            if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY)
                            {
                                flag1 = this.worldObj.setBlockToAir(blockpos) || flag1;
                            }
                            else
                            {
                                flag = true;
                            }
                        }
                        else
                        {
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag1)
        {
            double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)this.rand.nextFloat();
            double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)this.rand.nextFloat();
            double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)this.rand.nextFloat();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        return flag;
    }
}
