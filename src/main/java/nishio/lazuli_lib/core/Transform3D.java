package nishio.lazuli_lib.core;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform3D {
    public Vec3d position;
    public Quaternionf rotation;

    public static final Transform3D ZERO = new Transform3D(
            Vec3d.ZERO, new Quaternionf()
    );

    public static Transform3D fromPosition(Vec3d pos) {
        return new Transform3D(pos, new Quaternionf());
    }

    public static Transform3D fromRotation(Quaternionf rot) {
        return new Transform3D(Vec3d.ZERO, new Quaternionf(rot));
    }

    public Transform3D(Vec3d position, Quaternionf rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Transform3D() {
        this.position = Vec3d.ZERO;
        this.rotation = new Quaternionf(); // Identity
    }

    public Transform3D copy() {
        return new Transform3D(position, new Quaternionf(rotation));
    }

    // 🌸 Rotation helpers

    public Transform3D rotateAroundX(float degrees) {
        rotation.rotateX((float) Math.toRadians(degrees));
        return this;
    }

    public Transform3D rotateAroundY(float degrees) {
        rotation.rotateY((float) Math.toRadians(degrees));
        return this;
    }

    public Transform3D rotateAroundZ(float degrees) {
        rotation.rotateZ((float) Math.toRadians(degrees));
        return this;
    }

    public Transform3D rotateAround(Vec3d axis, float degrees) {
        Vector3f axisVec = new Vector3f((float) axis.x, (float) axis.y, (float) axis.z).normalize();
        rotation.rotateAxis((float) Math.toRadians(degrees), axisVec.x, axisVec.y, axisVec.z);
        return this;
    }

    public Transform3D apply(Transform3D other) {
        // Rotate other's position by our rotation, then add our position
        Vector3f offset = new Vector3f((float) other.position.x, (float) other.position.y, (float) other.position.z);
        offset.rotate(this.rotation);

        Vec3d newPos = this.position.add(offset.x, offset.y, offset.z);
        Quaternionf newRot = new Quaternionf(this.rotation).mul(other.rotation);

        return new Transform3D(newPos, newRot);
    }

    public Vec3d transformPoint(Vec3d point) {
        Vector3f p = new Vector3f((float) point.x, (float) point.y, (float) point.z);
        p.rotate(rotation);
        return position.add(p.x, p.y, p.z);
    }

}
