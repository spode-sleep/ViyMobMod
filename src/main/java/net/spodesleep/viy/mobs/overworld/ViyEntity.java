package net.spodesleep.viy.mobs.overworld;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class ViyEntity extends GhastEntity {

    private Vec3d targetPosition;

    public ViyEntity(EntityType<? extends GhastEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints += 3;
    }

    protected void initGoals() {
        this.goalSelector.add(5, new FlyRandomlyGoal(this));
        this.goalSelector.add(7, new LookAtTargetGoal(this));
        this.goalSelector.add(7, new ViyEntity.ShootFireballGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(
            this,
            PlayerEntity.class,
            10,
            false,
            false,
            livingEntity -> livingEntity.getVehicle() != null
        ));
    }

    public static DefaultAttributeContainer.Builder createNightmareAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0);
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    private void findClosestTarget() {
        double closestDistance = Double.MAX_VALUE;
        Vec3d closestPosition = null;

        LivingEntity livingEntity = this.getTarget();

        if (livingEntity != null) {
            Entity ship = livingEntity.getVehicle();

            if(ship != null) {
                double distance = this.squaredDistanceTo(ship);
                if(distance < closestDistance) {
                    closestPosition = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5), livingEntity.getZ());
                }
            }
        }

        this.targetPosition = closestPosition;
    }

    protected void shootFireballAtTarget(Vec3d targetPos) {
        Vec3d vec3d = this.getRotationVec(1.0F);
        double deltaX = targetPos.x - this.getX();
        double deltaY = targetPos.y - (0.5 + this.getBodyY(0.5));
        double deltaZ = targetPos.z - this.getZ();
        
        SnowballProjectile snowball = new SnowballProjectile(this.getWorld(), this, deltaX, deltaY, deltaZ);
        snowball.setPosition(this.getX() + vec3d.x * 4.0, this.getBodyY(0.5) + 0.5, this.getZ() + vec3d.z * 4.0);
        this.getWorld().spawnEntity(snowball);
    }

    @Override
    public boolean canSee(Entity entity) {
        return (entity != null);
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return target != null && this.distanceTo(target) <= 64.0D;
    }

    private static class FlyRandomlyGoal extends Goal {
        private final ViyEntity ghast;
        private static final double ABSOLUTE_MAX_HEIGHT = 255.0;
        private static final double TARGET_HEIGHT_OFFSET = 25.0;
        private static final double WATER_AVOIDANCE_HEIGHT = 5.0;

        public FlyRandomlyGoal(ViyEntity ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        private double getMaxHeight() {
            if (ghast.targetPosition != null) {
                return Math.min(ghast.targetPosition.y + TARGET_HEIGHT_OFFSET, ABSOLUTE_MAX_HEIGHT);
            }
            return ABSOLUTE_MAX_HEIGHT;
        }

        private double getMinHeight(double x, double z) {
            double highestWater = -Double.MAX_VALUE;
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    mutable.set(x + dx, ghast.getY(), z + dz);
                    while (mutable.getY() > -64) {
                        if (ghast.getWorld().getBlockState(mutable).isOf(Blocks.WATER)) {
                            highestWater = Math.max(highestWater, mutable.getY());
                            break;
                        }
                        mutable.move(0, -1, 0);
                    }
                }
            }

            return highestWater == -Double.MAX_VALUE ? -64 : highestWater + WATER_AVOIDANCE_HEIGHT;
        }

        public boolean canStart() {
            MoveControl moveControl = this.ghast.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            } else {
                double d = moveControl.getTargetX() - this.ghast.getX();
                double e = moveControl.getTargetY() - this.ghast.getY();
                double f = moveControl.getTargetZ() - this.ghast.getZ();
                double g = d * d + e * e + f * f;
                return g < 1.0 || g > 3600.0;
            }
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            Random random = this.ghast.getRandom();
            double targetX = this.ghast.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double targetZ = this.ghast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;

            double minHeight = getMinHeight(targetX, targetZ);

            double currentY = this.ghast.getY();
            double randomY = currentY + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double targetY = MathHelper.clamp(randomY, minHeight, getMaxHeight());

            this.ghast.getMoveControl().moveTo(targetX, targetY, targetZ, 1.0);
        }
    }

    static class LookAtTargetGoal extends Goal {
        private final ViyEntity ghast;

        public LookAtTargetGoal(ViyEntity ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            return true;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            if (this.ghast.targetPosition != null) {
                double e = this.ghast.targetPosition.x - this.ghast.getX();
                double f = this.ghast.targetPosition.z - this.ghast.getZ();
                this.ghast.setYaw(-((float) MathHelper.atan2(e, f)) * 57.295776F);
                this.ghast.bodyYaw = this.ghast.getYaw();
            } else {
                Vec3d vec3d = this.ghast.getVelocity();
                this.ghast.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776F);
                this.ghast.bodyYaw = this.ghast.getYaw();
            }
        }
    }

    private static class ShootFireballGoal extends Goal {
        private final ViyEntity ghast;
        public int cooldown = 0;
        public int searchCooldown = 100;
    
        public ShootFireballGoal(ViyEntity ghast) {
            this.ghast = ghast;
        }
    
        public boolean canStart() {
            if (searchCooldown <= 0) {
                this.ghast.findClosestTarget();
                searchCooldown = 100;
            } else {
                searchCooldown--;
            }
            return this.ghast.targetPosition != null;
        }
    
        public void tick() {
            if (this.ghast.targetPosition != null) {
                World world = this.ghast.getWorld();
                ++this.cooldown;
    
                if (this.cooldown == 20) {
                    if (!this.ghast.isSilent()) {
                        world.syncWorldEvent(null, 1016, this.ghast.getBlockPos(), 0);
                    }
    
                    this.ghast.shootFireballAtTarget(this.ghast.targetPosition);
                    
                    this.cooldown = -40;
                }
    
                this.ghast.setShooting(this.cooldown > 10);
            }
        }
    }

    @Override
    public void dropInventory() {
        super.dropInventory();
        
        Random random = this.getRandom();
        int amount = random.nextInt(3) + 1;
        
        for(int i = 0; i < amount; i++) {
            this.dropItem(Items.FIRE_CHARGE);
        }
    }
}