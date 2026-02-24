package nishio.lazuli_lib.core.shaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.Uniform;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.stuff.LazuliUniformType;

import java.util.HashMap;
import java.util.Map;


public class LazuliUniform<T> {

    private static final Map<Class<?>, LazuliUniformType> UNIFORM_TYPE_FROM_CLASS = new HashMap<>();

    static {
        for (LazuliUniformType t: LazuliUniformType.values()){
            UNIFORM_TYPE_FROM_CLASS.put(t.javaType, t);
        }
    } //Populates the UNIFORM_TYPE_FROM_CLASS map

    public T value;
    public final LazuliUniformType type;
    public final String name;
    public String freeType = null;
    public int freeCount = -1;
    public boolean dirty = true;

    public LazuliUniform(String name, T initialValue){
        this.name = name;
        this.value = initialValue;
        this.type = UNIFORM_TYPE_FROM_CLASS.getOrDefault(value.getClass(), LazuliUniformType.INVALID);
    }

    public LazuliUniform(String name, String type, int count, T initialValue){
        this.name = name;
        this.value = initialValue;
        this.type = LazuliUniformType.FREE;
        this.freeType = type;
        this.freeCount = count;
    }

    public void setShaderUniform(ShaderProgram shader){
        if (!dirty){ return; }
        dirty = false;
        Uniform uniform = shader.getUniform(name);

        if (uniform == null) {
            LazuliLog.Shaders.warn("Uniform '{}' not found in shader {}", name, shader.getName());
            return;
        }

        type.apply(uniform, value);
    }

    public void setShaderUniform(ShaderProgram shader, T value){
        setValue(value);
        setShaderUniform(shader);
    }

    public void setShaderUniformGeneric(ShaderProgram shader, Object value){
        setValue((T) value);
        setShaderUniform(shader);
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);

        if (type == LazuliUniformType.FREE) {
            String type;
            switch (freeType){
                case "float", "vec2", "vec3", "Vec4":
                    type = "float";
                    break;
                case "mat4":
                    type = "matrix4x4";
                    break;
                default:
                    type = freeType;
                    break;
            }

            jsonObject.addProperty("type", type);
            jsonObject.addProperty("count", freeCount);
        } else {
            jsonObject.addProperty("type", type.jsonType);
            jsonObject.addProperty("count", type.toFloatArray(value).length);
        }

            JsonArray valuesArray = new JsonArray();
        for (float value : type.toFloatArray(value)) {
            valuesArray.add(value);
        }
        jsonObject.add("values", valuesArray);

        return jsonObject;
    }

    public void setValue(T v){
        if (value == v) { return; }
        dirty = true;
        value = v;
    }

    public void setValueGeneric(Object v){
        if (value == v) { return; }
        dirty = true;
        value = (T) v;
    }
}


