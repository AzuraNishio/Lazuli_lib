package nishio.lazuli_lib.internals.datagen;

import me.jellysquid.mods.sodium.client.gl.shader.GlProgram;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.compat.LazuliSodiumResourcePackCompat;

import java.util.*;

public class LazuliTrueWarp {
    static class Injection{
        String content;
        String target;
        String type;

        public Injection(String content, String target, String type){
            this.content = content;
            this.target = target;
            this.type = type;
        }
    }

    List<Injection> injections;
    List<Identifier> targets;
    List<String> samplers;
    List<LazuliUniform<?>> uniform;
    Map<String, String> fragments = new HashMap<>();
    Map<String, String> vertexes = new HashMap<>();
    Identifier id;

    public LazuliTrueWarp(Identifier warpId){
        id = warpId;
        this.injections = new ArrayList<>();
        this.samplers = new ArrayList<>();
        this.uniform = new ArrayList<>();
        this.targets = new ArrayList<>();
    }

    public LazuliTrueWarp register(){
        LazuliWarpManager.registerWarp(this);
        return this;
    }

    public LazuliTrueWarp addTarget(Identifier target){
        this.targets.add(target);
        return this;
    }

    LazuliTrueWarp addInjection(String content, String target, String type){
        this.injections.add(new Injection(content, target, type));
        return this;
    }

    LazuliTrueWarp addSampler(String sampler){
        this.samplers.add(sampler);
        return this;
    }

    LazuliTrueWarp addUniform(String uniform, String type){
        switch (type){
            case "float":
                this.uniform.add(new LazuliUniform<>(uniform, type, 1, 0));
                break;
            case "vec2":
                this.uniform.add(new LazuliUniform<>(uniform, type, 2, new float[]{0, 0}));
                break;
            case "vec3":
                this.uniform.add(new LazuliUniform<>(uniform, type, 3, new float[]{0, 0, 0}));
                break;
            case "vec4":
                this.uniform.add(new LazuliUniform<>(uniform, type, 4, new float[]{0, 0, 0, 0}));
                break;
            case "mat4":
                this.uniform.add(new LazuliUniform<>(uniform, type, 16, new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
                break;
            default:
                this.uniform.add(new LazuliUniform<>(uniform, type, 1, 0));
                break;
        }




        return this;
    }

    public void print() {
        String message = ".\n==============================================[ Lazuli Warp Dump ]==============================================";
        if (!this.uniform.isEmpty()) {
            message = message.concat("\n-----------------[ Uniforms ]-----------------");
            for (LazuliUniform<?> u : this.uniform) {
                message = message.concat("\nName: %s, type: %s, count: %s".formatted(u.name, u.type.jsonType, u.type.count));
            }
        }

        if (!this.samplers.isEmpty()) {
            message = message.concat("\n-----------------[ Samplers ]-----------------");
            for (String sampler : this.samplers) {
                message = message.concat("\nName: %s".formatted(sampler));
            }
        }

        message = message.concat("\n-----------------[ Injections ]-----------------\n");
        for (Injection i : injections) {
            message = message.concat("""
                     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
                     Injection type: %s Target: %s
                     %s
                    """.formatted(i.type, i.target, i.content));
        }
        message = message.concat("\n=================================================================================================================");

        LazuliLog.Warp.info(message);
    }


    private String addUniforms(String oContent){
        String content = oContent;
        String uTarget = content.substring(0, content.indexOf('\n'));

        for (LazuliUniform uni : uniform) {
            String replacement = "%s\nuniform ".formatted(uTarget);

            replacement = replacement.concat(uni.freeType);
            replacement = replacement.concat(" ");
            replacement = replacement.concat(uni.name);
            replacement = replacement.concat(";");


            content = content.replace(uTarget, replacement);
        }
        return content;
    }

    private String processContent(String content, Injection inj){
        String target = inj.target;
        String replacement;

        boolean REPLACE = target.contains("<REPLACE>");
        boolean NOT_WRAP = target.contains("<NOT_WRAP>");
        boolean SODIUM = target.contains("<SODIUM>");

        target = target.replace("<REPLACE>", "");
        target = target.replace("<NOT_WRAP>", "");
        target = target.replace("<SODIUM>", "");

        if (SODIUM && LazuliSodiumResourcePackCompat.isIsSodiumLoaded()){
            for (LazuliUniform u : this.uniform){
                LazuliSodiumResourcePackCompat.sodiumLazuliUniforms.put(u.name, u);
            }
        }

        if(!SODIUM ^ LazuliSodiumResourcePackCompat.isIsSodiumLoaded()) {
            if (NOT_WRAP) {
                if (REPLACE) {
                    replacement = content;
                } else {
                    replacement = target.replace("X", inj.content);
                }
            } else {
                if (REPLACE) {
                    replacement = "\n".concat(inj.content).concat("\n");
                } else {
                    replacement = target.replace("X", "\n".concat(inj.content).concat("\n"));
                }
            }


            return content.replace(target.replace("X", ""), replacement);
        }
        return content;
    }

    public Map<String, String> generateFiles(){
        Map<String, String> files = new HashMap<>();
        for (String id : fragments.keySet()){
            String content = fragments.get(id);
            String name = new Identifier(id).getPath();
            LazuliLog.Shaders.info(id);

            content = addUniforms(content);
            for (Injection inj : injections) {
                if(Objects.equals(inj.type, "FRAGMENT")) {
                    content = processContent(content, inj);
                }
            }

            files.put(id, content);
        }
        GlProgram<ChunkShaderInterface> activeProgram;

        for (String id : vertexes.keySet()){
            String content = vertexes.get(id);
            String name = new Identifier(id).getPath();

            content = addUniforms(content);
            for (Injection inj : injections) {
                if(Objects.equals(inj.type, "VERTEX")) {
                    content = processContent(content, inj);
                }
            }
            files.put(id, content);
        }

        return files;
    }
}
