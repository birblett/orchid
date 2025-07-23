package com.birblett.enchantment.impl.curse;

import com.birblett.damage_types.OrchidDamageTypes;
import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.entity.ProjectileFlags;
import com.birblett.mixin.accessor.PersistentProjectileAccessor;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.EntityUtils;
import com.birblett.util.WorldUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.joml.Quaternionf;

import java.util.Optional;

public class InfernalEnchantment extends OrchidEnchantWrapper {

    private static final ExplosionBehavior INFERNAL_EXPLOSION = new AdvancedExplosionBehavior(false, true,
            Optional.of(0.0f), Optional.empty());

    public InfernalEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                               Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        int remainingUseTicks = shooter.getItemUseTimeLeft();
        if (remainingUseTicks < 71961 && flag == Flag.DIRECT) {
            if (remainingUseTicks < 71801) {
                EnchantmentUtils.setTracked(entity, OrchidEnchantments.INFERNAL, 3);
            } else if (remainingUseTicks < 71881) {
                EnchantmentUtils.setTracked(entity, OrchidEnchantments.INFERNAL, 2);
            } else {
                EnchantmentUtils.setTracked(entity, OrchidEnchantments.INFERNAL, 1);
            }
            if (entity instanceof PersistentProjectileEntity p) {
                p.setDamage(((PersistentProjectileAccessor) p).orchid_damage() + 2 + Math.max(0, Math.min((remainingUseTicks - 71960) / -22.0, 7)));
                p.setOnFireFor(1000);
            }
            ProjectileFlags.setIgnoreIFrames(entity, true);
        } else {
            EnchantmentUtils.removeTracked(entity, OrchidEnchantments.INFERNAL);
        }
        return Flow.CONTINUE;
    }

    @Override
    public Flow useTick(LivingEntity user, ItemStack stack, int remainingUseTicks, int level) {
        if (remainingUseTicks <= 71961) {
            if (user.getWorld().isClient) {
                ParticleEffect part = remainingUseTicks > 71880 ? ParticleTypes.SMALL_FLAME : ParticleTypes.SOUL_FIRE_FLAME;
                double mult = 0.1 + 0.5 * (1 - Math.max(0, remainingUseTicks - 71800) / 200.0);
                if (remainingUseTicks < 71881) {
                    mult += 0.4;
                }
                int particles = 10 - (remainingUseTicks - 71800) / 20;
                for (int i = 0; i < particles; ++i) {
                    double angle = (i & 1) * 0.523599 + 1.309 + Math.random() * 0.1 - 0.05;
                    Quaternionf f = new Quaternionf(0, Math.sin(angle), 0, Math.cos(angle));
                    Vec3d rot = user.getRotationVector(0, user.getYaw());
                    Vec3d v = new Vec3d(f.transformUnit(rot.toVector3f())).add(Math.random() * 0.05 - 0.025, Math.random() * 0.3,
                            Math.random() * 0.05 - 0.025).multiply(mult);
                    Vec3d p = user.getPos().add(0, user.getScale() * (Math.random() * 0.1 + 0.1), 0).
                            add(v.normalize().multiply(0.4)).add(rot.multiply(0.4));
                    if (i % 3 == 0) {
                        Vec3d p2 = user.getPos().add(user.getWidth() * (Math.random() - 0.5), user.getHeight() * Math.random(),
                                user.getWidth() * (Math.random() - 0.5));
                        Vec3d v2 = v.multiply(0.04);
                        this.addParticlesVParticles(user, part, p2, v2, remainingUseTicks);
                    }
                    this.addParticlesVParticles(user, part, p, v, remainingUseTicks);
                }
            } else if (user.getWorld() instanceof ServerWorld world) {
                if (remainingUseTicks == 71961) {
                    WorldUtils.playSound(null, user.getWorld(), user, SoundEvents.ENTITY_BLAZE_SHOOT, 1.0f, 2.0f);
                } else if (remainingUseTicks == 71881) {
                    WorldUtils.playSound(null, user.getWorld(), user, SoundEvents.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
                } else if (remainingUseTicks == 71801) {
                    WorldUtils.playSound(null, user.getWorld(), user, SoundEvents.ENTITY_BLAZE_SHOOT, 1.0f, 0.1f);
                }
                int penalty = 9;
                user.setOnFireForTicks(50);
                if (remainingUseTicks < 71801 && remainingUseTicks % 10 == 0) {
                    user.damage(world, user.getDamageSources().create(OrchidDamageTypes.IMMOLATION), 4);
                    penalty = 29;
                } else if (remainingUseTicks < 71881 && remainingUseTicks % 20 == 0) {
                    user.damage(world, user.getDamageSources().create(OrchidDamageTypes.IMMOLATION), 4);
                    penalty = 19;
                } else if (remainingUseTicks % 20 == 0) {
                    user.damage(world, user.getDamageSources().create(OrchidDamageTypes.IMMOLATION), 2);
                }
                if (user.getStatusEffect(StatusEffects.FIRE_RESISTANCE) instanceof StatusEffectInstance i) {
                    user.setStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, i.getDuration() - penalty,
                            i.getAmplifier(), i.isAmbient(), i.shouldShowParticles(), i.shouldShowIcon()), user);
                }
            }
        }
        return Flow.CONTINUE;
    }

    @Override
    public double projectileDragModifier(ProjectileEntity entity, double drag, int level) {
        double dragDiff = (1.0 - drag) / 4.0 * level;
        return drag + dragDiff;
    }

    @Override
    public Flow onProjectileTick(ProjectileEntity entity, World world, int level) {
        if (entity.getY() > 1000) {
            EnchantmentUtils.removeTracked(entity, OrchidEnchantments.INFERNAL);
            return Flow.CONTINUE;
        }
        if (level == 3 && entity.getOwner() instanceof Entity e) {
            Vec3d v = entity.getPos().subtract(entity.getVelocity().multiply(0.6));
            world.createExplosion(e, e.getDamageSources().explosion(e, entity), INFERNAL_EXPLOSION, v, 1, true,
                    World.ExplosionSourceType.MOB);
        }
        return Flow.CONTINUE;
    }

    @Override
    public ParticleEffect projectileParticleModifier(ProjectileEntity entity, int level) {
        return switch (level) {
            case 1 -> ParticleTypes.SMALL_FLAME;
            case 2, 3 -> ParticleTypes.SOUL_FIRE_FLAME;
            default -> null;
        };
    }

    @Override
    public Flow onProjectileHit(ProjectileEntity entity, HitResult result, int level) {
        if (level == 3 && entity.getOwner() instanceof Entity e) {
            Vec3d v = result instanceof EntityHitResult entityHitResult ? entityHitResult.getEntity().getPos().add(0,
                    entityHitResult.getEntity().getHeight() / 3, 0) : entity.getPos();
            e.getWorld().createExplosion(e, v.x, v.y, v.z, 2, true, World.ExplosionSourceType.MOB);
            if (result instanceof EntityHitResult entityHitResult) {
                EntityUtils.resetIframes(entityHitResult.getEntity());
            }
            entity.discard();
        }
        EnchantmentUtils.removeTracked(entity, OrchidEnchantments.INFERNAL);
        return Flow.CONTINUE;
    }

    private void addParticlesVParticles(LivingEntity user, ParticleEffect part, Vec3d p, Vec3d v, int remainingUseTicks) {
        user.getWorld().addImportantParticleClient(part, true, p.x, p.y, p.z, v.x, v.y, v.z);
        if (remainingUseTicks < 71801) {
            v = v.add(Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1)
                    .multiply(8);
            user.getWorld().addImportantParticleClient(ParticleTypes.ELECTRIC_SPARK, true, p.x, p.y, p.z, v.x, v.y, v.z);
        }
    }
}
