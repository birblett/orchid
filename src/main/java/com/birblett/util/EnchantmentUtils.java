package com.birblett.util;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.network.AttachedDataRegistry;
import com.birblett.network.attached_data.EnchantmentDataAttachment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

import java.util.*;
import java.util.function.BiFunction;

@SuppressWarnings("UnstableApiUsage")
public class EnchantmentUtils {

    public static RegistryEntry<Enchantment> getEntry(World world, RegistryKey<Enchantment> e) {
        Optional<Registry<Enchantment>> reg = world.getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
        Optional<RegistryEntry.Reference<Enchantment>> optional = Optional.empty();
        if (reg.isPresent()) {
            optional = reg.get().getEntry(e.getValue());
        }
        return optional.orElse(null);
    }

    public static boolean hasEnchant(ItemStack stack, RegistryKey<Enchantment> key, World world) {
        RegistryEntry<Enchantment> e = getEntry(world, key);
        return e != null && EnchantmentHelper.getLevel(e, stack) > 0;
    }

    public static boolean stackIterator(ItemStack stack, BiFunction<OrchidEnchantWrapper, Integer, OrchidEnchantWrapper.ControlFlow> execute) {
        if (stack.isEmpty()) {
            return false;
        }
        return applyEnchants(execute, getSortedItemEnchants(stack));
    }

    @Nullable
    public static <T> T stackIteratorGeneric(ItemStack stack, BiFunction<OrchidEnchantWrapper, Integer, T> execute) {
        return getFirstMatch(execute, getSortedItemEnchants(stack));
    }

    private static final EquipmentSlot[] ITERABLE_SLOT_ORDER = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.BODY, EquipmentSlot.SADDLE};

    public static boolean equipIterator(LivingEntity e, BiFunction<OrchidEnchantWrapper, Integer, OrchidEnchantWrapper.ControlFlow> execute) {
        boolean exit = false;
        for (EquipmentSlot slot : ITERABLE_SLOT_ORDER) {
            if (!e.getEquippedStack(slot).isEmpty() && EnchantmentHelper.hasEnchantments(e.getEquippedStack(slot))) {
                exit |= applyEnchants(execute, getEquipMatchingEnchants(e.getEquippedStack(slot), slot));
            }
        }
        return exit;
    }

    private static ArrayList<Triplet<Integer, Integer, RegistryKey<Enchantment>>> getSortedItemEnchants(ItemStack stack) {
        ArrayList<Triplet<Integer, Integer, RegistryKey<Enchantment>>> enchantments = new ArrayList<>();
        for (RegistryEntry<Enchantment> e : EnchantmentHelper.getEnchantments(stack).getEnchantments()) {
            if (e.getKey().isPresent() && OrchidEnchantments.PROCESS_PRIORITY.get(e.getKey().get()) instanceof Integer i) {
                addListTriplet(enchantments, e.getKey().get(), i,  EnchantmentHelper.getLevel(e, stack));
            }
        }
        return enchantments;
    }

    private static ArrayList<Triplet<Integer, Integer, RegistryKey<Enchantment>>> getEquipMatchingEnchants(ItemStack stack, EquipmentSlot slot) {
        ArrayList<Triplet<Integer, Integer, RegistryKey<Enchantment>>> enchantments = new ArrayList<>();
        for (RegistryEntry<Enchantment> e : EnchantmentHelper.getEnchantments(stack).getEnchantments()) {
            if (e.getKey().isPresent() && OrchidEnchantments.PROCESS_PRIORITY.get(e.getKey().get()) instanceof Integer i) {
                if (Arrays.stream(OrchidEnchantments.ORCHID_ENCHANTMENTS.get(i).get(e.getKey().get()).slots)
                        .anyMatch(s -> s.matches(slot))) {
                    addListTriplet(enchantments, e.getKey().get(), i,  EnchantmentHelper.getLevel(e, stack));
                }
            }
        }
        return enchantments;
    }

    private static void addListTriplet(ArrayList<Triplet<Integer, Integer, RegistryKey<Enchantment>>> enchantments, RegistryKey<Enchantment> e, Integer i, int level) {
        // triplet order is priority, level, enchantment
        Triplet<Integer, Integer, RegistryKey<Enchantment>> t = new Triplet<>(i, level, e);
        int idx = Collections.binarySearch(enchantments, t, Comparator.comparingInt(Triplet::getA));
        if (idx < 0) {
            enchantments.add(-idx - 1, t);
        } else {
            enchantments.add(idx, t);
        }
    }

    public static boolean entityIterator(Entity e, BiFunction<OrchidEnchantWrapper, Integer, OrchidEnchantWrapper.ControlFlow> execute) {
        return applyEnchants(execute, getEntityEnchantFlags(e));
    }

    @Nullable
    public static <T> T entityIteratorGeneric(ProjectileEntity p, BiFunction<OrchidEnchantWrapper, Integer, T> execute) {
        return getFirstMatch(execute, getEntityEnchantFlags(p));
    }

    private static ArrayList<Triplet<Integer, Integer, RegistryKey<Enchantment>>> getEntityEnchantFlags(Entity entity) {
        // triplet order is priority, level, enchantment
        ArrayList<Triplet<Integer, Integer, RegistryKey<Enchantment>>> enchantments = new ArrayList<>();
        for (Map.Entry<RegistryKey<Enchantment>, Integer> entry : EnchantmentUtils.getTrackedMap(entity).entrySet()) {
            if (OrchidEnchantments.PROCESS_PRIORITY.get(entry.getKey()) instanceof Integer i) {
                Triplet<Integer, Integer, RegistryKey<Enchantment>> t = new Triplet<>(i, entry.getValue(), entry.getKey());
                int idx = Collections.binarySearch(enchantments, t, Comparator.comparingInt(Triplet::getA));
                if (idx < 0) {
                    enchantments.add(-idx - 1, t);
                } else {
                    enchantments.add(idx, t);
                }
            }
        }
        return enchantments;
    }

    private static boolean applyEnchants(BiFunction<OrchidEnchantWrapper, Integer, OrchidEnchantWrapper.ControlFlow> execute, List<Triplet<Integer, Integer, RegistryKey<Enchantment>>> enchantments) {
        OrchidEnchantWrapper.ControlFlow exit = OrchidEnchantWrapper.ControlFlow.CONTINUE;
        int lastPrio = 0;
        for (Triplet<Integer, Integer, RegistryKey<Enchantment>> t : enchantments) {
            if (exit != OrchidEnchantWrapper.ControlFlow.CONTINUE && exit != OrchidEnchantWrapper.ControlFlow.CANCEL_AFTER && t.getA() != lastPrio) {
                break;
            }
            if (exit == OrchidEnchantWrapper.ControlFlow.CONTINUE || exit == OrchidEnchantWrapper.ControlFlow.CANCEL_AFTER) {
                OrchidEnchantWrapper.ControlFlow temp = execute.apply(OrchidEnchantments.ORCHID_ENCHANTMENTS.get(t.getA()).get(t.getC()), t.getB());
                if (exit == OrchidEnchantWrapper.ControlFlow.CANCEL_AFTER) {
                    if (temp == OrchidEnchantWrapper.ControlFlow.CANCEL_BREAK || temp == OrchidEnchantWrapper.ControlFlow.BREAK) {
                        exit = OrchidEnchantWrapper.ControlFlow.CANCEL_BREAK;
                    }
                } else {
                    exit = temp;
                }
            }
            lastPrio = t.getA();
        }
        return exit == OrchidEnchantWrapper.ControlFlow.CANCEL_BREAK || exit == OrchidEnchantWrapper.ControlFlow.CANCEL_AFTER;
    }

    @Nullable
    private static <T> T getFirstMatch(BiFunction<OrchidEnchantWrapper, Integer, T> execute, List<Triplet<Integer, Integer, RegistryKey<Enchantment>>> enchantments) {
        T value = null;
        for (Triplet<Integer, Integer, RegistryKey<Enchantment>> t : enchantments) {
           if ((value = execute.apply(OrchidEnchantments.ORCHID_ENCHANTMENTS.get(t.getA()).get(t.getC()), t.getB())) != null) {
               break;
           }
        }
        return value;
    }

    public static boolean onProjectileFired(ItemStack stack, ItemStack projectileStack, ProjectileEntity entity, LivingEntity shooter, ServerWorld world, boolean critical, OrchidEnchantWrapper.Flag f) {
        HashMap<RegistryKey<Enchantment>, Integer> map = new HashMap<>();
        EnchantmentUtils.stackIterator(stack, (enchant, level) -> {
            map.put(enchant.getOrCreateKey(), level);
            return OrchidEnchantWrapper.ControlFlow.CONTINUE;
        });
        EnchantmentUtils.setTrackedFromMap(entity, map);
        return EnchantmentUtils.stackIterator(stack, (enchant, level) ->
                enchant.onProjectileFired(shooter, entity, stack, projectileStack, world, critical, level, f));
    }

    public static double projectileDragModifier(ProjectileEntity p, double value) {
        MutableDouble drag = new MutableDouble(value);
        EnchantmentUtils.entityIterator(p, (enchant, level) -> {
            drag.setValue(enchant.projectileDragModifier(p, drag.getValue(), level));
            return OrchidEnchantWrapper.ControlFlow.CONTINUE;
        });
        return drag.getValue();
    }

    public static Map<RegistryKey<Enchantment>, Integer> getTrackedMap(AttachmentTarget target) {
        return target.getAttachedOrCreate(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT).getAll();
    }

    public static void setTrackedFromMap(AttachmentTarget target, Map<RegistryKey<Enchantment>, Integer> data) {
        target.setAttached(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT, new EnchantmentDataAttachment(data));
    }

    public static void setTracked(AttachmentTarget target, RegistryKey<Enchantment> enchantment, int value) {
        EnchantmentDataAttachment tmp = target.getAttachedOrCreate(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT);
        target.setAttached(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT, tmp.setEntry(enchantment, value));
    }

    public static int getTrackedLevel(AttachmentTarget target, RegistryKey<Enchantment> enchantment) {
        EnchantmentDataAttachment tmp;
        if ((tmp = target.getAttached(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT)) != null) {
            return tmp.getOrDefault(enchantment, 0);
        }
        return 0;
    }

    public static void removeTracked(AttachmentTarget target, RegistryKey<Enchantment> enchantment) {
        EnchantmentDataAttachment tmp;
        if ((tmp = target.getAttached(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT)) != null) {
            target.setAttached(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT, tmp.removeEntry(enchantment));
        }
    }

    public static void addToTracked(AttachmentTarget target, RegistryKey<Enchantment> enchantment, int value) {
        EnchantmentDataAttachment tmp = target.getAttachedOrCreate(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT);
        int v = tmp.getOrDefault(enchantment, value);
        target.setAttached(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT, tmp.setEntry(enchantment, v + value));
    }

    public static boolean trackedContains(AttachmentTarget target, RegistryKey<Enchantment> enchantment) {
        EnchantmentDataAttachment tmp;
        if ((tmp = target.getAttached(AttachedDataRegistry.ENCHANTMENT_DATA_ATTACHMENT)) != null) {
            return tmp.contains(enchantment);
        }
        return false;
    }

}
