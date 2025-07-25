package com.birblett.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.Random;

public class VectorUtils {

    private static final Random RANDOM = new Random();

    private static final Vec3d AXIS_X = new Vec3d(1, 0, 0);
    private static final Vec3d AXIS_Y = new Vec3d(0, 1, 0);

    public static Vec3d rotateAbout(Vec3d base, Vec3d axis, double angle) {
        double cos = Math.cos(angle);
        return base.multiply(cos).add(axis.crossProduct(base).multiply(Math.sin(angle))).add(axis.multiply(axis.dotProduct(base))
                .multiply(1 - cos));
    }

    public static Vec3d applyDivergence(Vec3d base, double maxAngle) {
        base = base.normalize();
        return rotateAbout(rotateAbout(base, (base.y == 1 || base.y == -1 ? AXIS_X : AXIS_Y).crossProduct(base).normalize(),
                RANDOM.nextDouble() * maxAngle), base, RANDOM.nextDouble() * Math.PI * 2).normalize();
    }

    public static Vec3d rotateTowards(Vec3d base, Vec3d target, double angle) {
        if (base.lengthSquared() == 0 || target.lengthSquared() == 0) {
            return base;
        }
        base = base.normalize();
        target = target.normalize();
        angle = Math.min(Math.acos(Math.clamp(base.dotProduct(target), -1.0, 1.0)), angle);
        Vec3d axis = base.crossProduct(target).normalize();
        if (axis.equals(Vec3d.ZERO)) {
            axis = base.crossProduct(AXIS_Y.equals(base) ? AXIS_X : AXIS_Y).normalize();
        }
        angle = Math.sin(angle / 2);
        return new Vec3d(base.toVector3f().rotate(new Quaternionf(axis.x * angle, axis.y * angle, axis.z * angle, Math.cos(angle / 2))));
    }

    public static void rotateEntity(Entity e, Vec3d rotation) {
        rotation = rotation.normalize();
        e.rotate((float) Math.toDegrees(Math.atan2(rotation.x, rotation.z)), (float) Math.toDegrees(Math.asin(rotation.y)));
    }

}
