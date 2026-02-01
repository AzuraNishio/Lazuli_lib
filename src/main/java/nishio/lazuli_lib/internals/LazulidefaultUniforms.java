package nishio.lazuli_lib.internals;

import nishio.lazuli_lib.core.shaders.LazuliUniform;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LazulidefaultUniforms {
    //    { "name": "ModelViewMat", "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
    //        { "name": "ProjMat", "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
    //        { "name": "ColorModulator", "type": "float", "count": 4, "values": [ 1.0, 1.0, 1.0, 1.0 ] }

    private static Map<String, LazuliUniform<?>> defaultUniforms = new HashMap<>();

    static {
        defaultUniforms.put("ModelViewMat", new LazuliUniform<Matrix4f>("ModelViewMat", new Matrix4f()));
        defaultUniforms.put("ProjMat", new LazuliUniform<Matrix4f>("ProjMat", new Matrix4f()));
        defaultUniforms.put("ColorModulator", new LazuliUniform<Color>("ColorModulator", Color.white));
    }

    public static Map<String, LazuliUniform<?>> getDefaultUniforms() {
        return new HashMap<>(defaultUniforms);
    }

    public static Map<String, LazuliUniform<?>> addDefaultUniforms(Map<String, LazuliUniform<?>> l) {
        l.putAll(new HashMap<>(defaultUniforms));
        return l;
    }
}
