package nishio.lazuli_lib.internals;

import net.minecraft.client.gl.Uniform;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;

public enum LazuliUniformType {

    INVALID(Object.class, "invalid", 0) {
        @Override
        public void apply(Uniform u, Object v) {
            LazuliLog.Shaders.warn("Invalid uniform type!");
        }

        @Override
        public float[] toFloatArray(Object v) {
            return new float[0];
        }
    },

    FLOAT_ARRAY(float[].class, "float[]", -1) {
        @Override
        public void apply(Uniform u, Object v) {
            u.set((float[]) v);
        }

        @Override
        public float[] toFloatArray(Object v) {
            return (float[]) v;
        }
    },

    INT_ARRAY(int[].class, "int[]", -1) {
        @Override
        public void apply(Uniform u, Object v) {
            int[] src = (int[]) v;
            float[] dst = new float[src.length];
            for (int i = 0; i < src.length; i++) {
                dst[i] = src[i];
            }
            u.set(dst);
        }

        @Override
        public float[] toFloatArray(Object v) {
            int[] src = (int[]) v;
            float[] dst = new float[src.length];
            for (int i = 0; i < src.length; i++) {
                dst[i] = src[i];
            }
            return dst;
        }
    },

    VEC3D_ARRAY(Vec3d[].class, "float[]", -1) {
        @Override
        public void apply(Uniform u, Object v) {
            Vec3d[] arr = (Vec3d[]) v;
            float[] f = new float[arr.length * 3];

            for (int i = 0; i < arr.length; i++) {
                int o = i * 3;
                f[o]     = (float) arr[i].x;
                f[o + 1] = (float) arr[i].y;
                f[o + 2] = (float) arr[i].z;
            }

            u.set(f);
        }

        @Override
        public float[] toFloatArray(Object v) {
            Vec3d[] arr = (Vec3d[]) v;
            float[] f = new float[arr.length * 3];

            for (int i = 0; i < arr.length; i++) {
                int o = i * 3;
                f[o]     = (float) arr[i].x;
                f[o + 1] = (float) arr[i].y;
                f[o + 2] = (float) arr[i].z;
            }

            return f;
        }
    },


    FLOAT(Float.class, "float", 1) {
        @Override
        public void apply(Uniform u, Object v) {
            u.set((Float) v);
        }

        @Override
        public float[] toFloatArray(Object v) {
            return new float[]{ (Float) v };
        }
    },

    INT(Integer.class, "int", 1) {
        @Override
        public void apply(Uniform u, Object v) {
            u.set((Integer) v);
        }

        @Override
        public float[] toFloatArray(Object v) {
            return new float[]{ ((Integer) v).floatValue() };
        }
    },

    BOOL(Boolean.class, "bool", 1) {
        @Override
        public void apply(Uniform u, Object v) {
            u.set((Boolean) v ? 1 : 0);
        }

        @Override
        public float[] toFloatArray(Object v) {
            return new float[]{ (Boolean) v ? 1f : 0f };
        }
    },

    VEC2F(Vec2f.class, "float", 2) {
        @Override
        public void apply(Uniform u, Object v) {
            Vec2f t = (Vec2f) v;
            u.set(t.x, t.y);
        }

        @Override
        public float[] toFloatArray(Object v) {
            Vec2f t = (Vec2f) v;
            return new float[]{ t.x, t.y };
        }
    },

    VEC2FJ(Vector2f.class, "float", 2) {
        @Override
        public void apply(Uniform u, Object v) {
            Vector2f t = (Vector2f) v;
            u.set(t.x, t.y);
        }

        @Override
        public float[] toFloatArray(Object v) {
            Vector2f t = (Vector2f) v;
            return new float[]{ t.x, t.y };
        }
    },

    VEC3D(Vec3d.class, "float", 3) {
        @Override
        public void apply(Uniform u, Object v) {
            Vec3d t = (Vec3d) v;
            u.set((float) t.getX(), (float) t.getY(), (float) t.getZ());
        }

        @Override
        public float[] toFloatArray(Object v) {
            Vec3d t = (Vec3d) v;
            return new float[]{ (float) t.x, (float) t.y, (float) t.z };
        }
    },

    COLOR(Color.class, "float", 4) {
        @Override
        public void apply(Uniform u, Object v) {
            Color c = (Color) v;
            u.set(
                    c.getRed() / 255f,
                    c.getGreen() / 255f,
                    c.getBlue() / 255f,
                    c.getAlpha() / 255f
            );
        }

        @Override
        public float[] toFloatArray(Object v) {
            Color c = (Color) v;
            return new float[]{
                    c.getRed() / 255f,
                    c.getGreen() / 255f,
                    c.getBlue() / 255f,
                    c.getAlpha() / 255f
            };
        }
    },

    MAT2F(Matrix2f.class, "matrix2x2", 4) {
        @Override
        public void apply(Uniform u, Object v) {
            float[] f = new float[4];
            ((Matrix2f) v).get(f);
            u.set(f);
        }

        @Override
        public float[] toFloatArray(Object v) {
            float[] f = new float[4];
            ((Matrix2f) v).get(f);
            return f;
        }
    },

    MAT3F(Matrix3f.class, "matrix3x3", 9) {
        @Override
        public void apply(Uniform u, Object v) {
            float[] f = new float[9];
            ((Matrix3f) v).get(f);
            u.set(f);
        }

        @Override
        public float[] toFloatArray(Object v) {
            float[] f = new float[9];
            ((Matrix3f) v).get(f);
            return f;
        }
    },

    MAT4F(Matrix4f.class, "matrix4x4", 16) {
        @Override
        public void apply(Uniform u, Object v) {
            float[] f = new float[16];
            ((Matrix4f) v).get(f);
            u.set(f);
        }

        @Override
        public float[] toFloatArray(Object v) {
            float[] f = new float[16];
            ((Matrix4f) v).get(f);
            return f;
        }
    };



    public final Class<?> javaType;
    public final String jsonType;
    public final int count;

    LazuliUniformType(Class<?> javaType, String jsonType, int count) {
        this.javaType = javaType;
        this.jsonType = jsonType;
        this.count = count;
    }

    public abstract void apply(Uniform u, Object value);
    public abstract float[] toFloatArray(Object value);
}
