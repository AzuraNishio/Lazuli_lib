package nishio.lazuli_lib.internals;

import nishio.lazuli_lib.core.shaders.LazuliUniform;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LazulidefaultUniforms {
//  "uniforms": [
//    { "name": "ProjMat", "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
//    { "name": "InSize", "type": "float", "count": 2, "values": [ 1.0, 1.0 ] },
//    { "name": "OutSize", "type": "float", "count": 2, "values": [ 1.0, 1.0 ] },
//    { "name": "Time", "type": "float", "count": 1, "values": [1.0]},
//  ]


    public static final Set<LazuliUniform<?>> defaultUniforms = new HashSet<>();
    public static final Set<LazuliUniform<?>> defaultFramebufferUniforms = new HashSet<>();

    static {
        defaultUniforms.add(new LazuliUniform<Matrix4f>("ModelViewMat", new Matrix4f()));
        defaultUniforms.add(new LazuliUniform<Matrix4f>("ProjMat", new Matrix4f()));
        defaultUniforms.add(new LazuliUniform<Color>("ColorModulator", Color.white));

        defaultFramebufferUniforms.add(new LazuliUniform<Matrix4f>("ProjMat", new Matrix4f()));
        defaultFramebufferUniforms.add(new LazuliUniform<Vector2f>("InSize", new Vector2f()));
        defaultFramebufferUniforms.add(new LazuliUniform<Vector2f>("OutSize", new Vector2f()));
        defaultFramebufferUniforms.add(new LazuliUniform<Float>("Time", 0f));
    }
}


