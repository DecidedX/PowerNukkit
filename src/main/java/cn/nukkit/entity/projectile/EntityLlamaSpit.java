package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.CriticalParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.entity.EntityUtils;

public class EntityLlamaSpit extends EntityProjectile {

    public static final int NETWORK_ID = 102;

    protected boolean critical = false;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.3125f;
    }

    @Override
    public float getHeight() {
        return 0.3125f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    protected double getDamage() {
        return 4;
    }

    public EntityLlamaSpit(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityLlamaSpit(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);

        this.critical = critical;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        boolean hasUpdate = super.onUpdate(currentTick);

        if (!this.hadCollision && this.critical) {
            this.level.addParticle(new CriticalParticle(this.add(this.getWidth() / 2 + EntityUtils.rand(-100, 100) / 500, this.getHeight() / 2 + EntityUtils.rand(-100, 100) / 500, this.getWidth() / 2 + EntityUtils.rand(-100, 100) / 500)));
        } else if (this.onGround) {
            this.critical = false;
        }

        if (this.age > 1200 || this.isCollided) {
            this.kill();
            hasUpdate = true;
        }

        this.timing.stopTiming();

        return hasUpdate;
    }
}
