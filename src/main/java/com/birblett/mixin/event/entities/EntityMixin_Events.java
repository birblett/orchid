package com.birblett.mixin.event.entities;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.interfaces.entity.EnchantmentFlags;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin_Events implements EnchantmentFlags {

    @Unique long lastProcessedTick = 0;

    @Override
    public boolean orchid_isTickProcessed() {
        long time = ((Entity) (Object) this).getWorld().getTime();
        long lastTime = this.lastProcessedTick;
        this.lastProcessedTick = time;
        return time == lastTime;
    }

    @Override
    public void orchid_processTick(CallbackInfo c) {
        if (!this.orchid_isTickProcessed()) {
            Entity e = (Entity) (Object) this;
            if (EnchantmentUtils.entityIterator(e, (enchant, level) -> enchant.onEntityTick(e, e.getWorld(), level))) {
                c.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @WrapOperation(method = "applyGravity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getFinalGravity()D"))
    private double applyGravityMods(Entity instance, Operation<Double> original) {
        MutableDouble gravity = new MutableDouble(original.call(instance));
        if (instance instanceof ProjectileEntity p) {
            EnchantmentUtils.entityIterator(p, (enchant, level) -> {
                gravity.setValue(enchant.projectileGravityModifier(p, gravity.getValue(), level));
                return OrchidEnchantWrapper.ControlFlow.CONTINUE;
            });
        }
        return gravity.doubleValue();
    }

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"))
    private void onBlockLand(Block instance, BlockView world, Entity entity, Operation<Void> original) {
        if (entity instanceof LivingEntity e) {
            Block tmp = instance;
            Block out = EnchantmentUtils.equipIteratorGeneric(e, (enchant, level) ->
                    enchant.onBlockLand(e, entity.getWorld(), tmp, level));
            if (out != null) {
                instance = out;
            }
        }
        original.call(instance, world, entity);
    }

    @ModifyVariable(method = "stepOnBlock", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;isSwimming()Z", shift = At.Shift.BY, by = 2), argsOnly = true)
    private BlockState onBlockStep(BlockState state) {
        return EnchantmentUtils.getEntityBlockFeedbackState((Entity) (Object) this, state);
    }

    @WrapOperation(method = "spawnSprintingParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private BlockState onSprintParticleSpawn(World instance, BlockPos pos, Operation<BlockState> original) {
        BlockState state = original.call(instance, pos);
        if (state.getRenderType() != BlockRenderType.INVISIBLE) {
            state = EnchantmentUtils.getEntityBlockFeedbackState((Entity) (Object) this, state);
        }
        return state;
    }

    @ModifyVariable(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getStepHeight()F", ordinal = 0), ordinal = 3)
    private boolean canStepUp(boolean b) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity e) {
            Boolean temp = EnchantmentUtils.equipIteratorGeneric(e, (enchant, level) ->
                            enchant.modifyCanStep(e, e.getWorld(), level));
            if (temp != null) {
                b = temp;
            }
        }
        return b;
    }

    @WrapOperation(method = "setSwimming", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setFlag(IZ)V"))
    private void modifyCanSwim(Entity instance, int index, boolean value, Operation<Void> original) {
        if (instance instanceof LivingEntity e) {
            Boolean b = EnchantmentUtils.equipIteratorGeneric(e, (enchant, level) ->
                    enchant.modifyCanSwim(e, e.getWorld(), level));
            if (b != null) {
                value = b;
            }
        }
        original.call(instance, index, value);
    }

}
