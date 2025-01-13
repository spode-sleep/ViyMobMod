package net.spodesleep.viy.mobs.overworld;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.entity.EntityType;

public class SnowballProjectile extends DragonFireballEntity {

    public SnowballProjectile(EntityType<? extends DragonFireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public SnowballProjectile(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(world, owner, velocityX, velocityY, velocityZ);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            BlockPos center = this.getBlockPos();

            if (this.getWorld().getBlockState(center).isOf(Blocks.WATER)) {
                BlockPos surfacePos = center;
                while (this.getWorld().getBlockState(surfacePos.up()).isOf(Blocks.WATER)) {
                    surfacePos = surfacePos.up();
                }

                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos icePos = surfacePos.add(x, 0, z);
                        if (this.getWorld().getBlockState(icePos).isOf(Blocks.WATER)) {
                            this.getWorld().setBlockState(icePos, Blocks.ICE.getDefaultState());
                        }
                    }
                }

                this.getWorld().createExplosion(
                        null,
                        this.getX(),
                        surfacePos.getY(),
                        this.getZ(),
                        2.0F,
                        false,
                        World.ExplosionSourceType.NONE
                );

                this.discard();
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.getWorld().isClient && !this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.WATER)) {
            this.getWorld().createExplosion(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    2.0F,
                    false,
                    World.ExplosionSourceType.MOB);

            int radius = 3;
            BlockPos center = this.getBlockPos();
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance <= radius) {
                        BlockPos currentPos = center.add(x, -1, z);

                        for (int y = 1; y >= -2; y--) {
                            BlockPos checkPos = currentPos.down(y);
                            BlockPos snowPos = checkPos.up();

                            if (this.getWorld().getBlockState(checkPos).isSolid() &&
                                    this.getWorld().getBlockState(snowPos).isAir()) {
                                this.getWorld().setBlockState(snowPos, Blocks.SNOW.getDefaultState());
                                break;
                            }
                        }
                    }
                }
            }

            List<LivingEntity> nearbyEntities = this.getWorld().getEntitiesByClass(
                    LivingEntity.class,
                    new Box(this.getX() - radius, this.getY() - radius, this.getZ() - radius,
                            this.getX() + radius, this.getY() + radius, this.getZ() + radius),
                    entity -> true);

            for (LivingEntity entity : nearbyEntities) {
                entity.damage(this.getWorld().getDamageSources().freeze(), 4.0F);
            }

            this.discard();
        }
    }

}