package nishio.lazuli_lib.core.shaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.Uniform;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.LazuliUniformType;

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
    public boolean dirty = true;

    public LazuliUniform(String name, T initialValue){
        this.name = name;
        this.value = initialValue;
        this.type = UNIFORM_TYPE_FROM_CLASS.getOrDefault(value.getClass(), LazuliUniformType.INVALID);
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
        jsonObject.addProperty("type", type.jsonType);
        jsonObject.addProperty("count", type.count);

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


