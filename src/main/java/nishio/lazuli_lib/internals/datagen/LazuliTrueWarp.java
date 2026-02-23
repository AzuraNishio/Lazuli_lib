package nishio.lazuli_lib.internals.datagen;

import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.LazuliLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public Map<String, String> generateFiles(){
        Map<String, String> files = new HashMap<>();
        for (String name : fragments.keySet()){
            String content = fragments.get(name);

            for (LazuliUniform uni : uniform) {
                String replacement = "#version 150\nuniform ";
                String target = "#version 150";

                replacement = replacement.concat(uni.freeType);
                replacement = replacement.concat(" ");
                replacement = replacement.concat(uni.name);
                replacement = replacement.concat(";");


                content = content.replace(target, replacement);
            }

            for (Injection inj : injections) {
                if(inj.type == "FRAGMENT") {
                    String replacement = inj.target.replace("X", "\n".concat(inj.content).concat("\n"));
                    String target = inj.target.replace("X", "");


                    content = content.replace(target, replacement);
                }
            }

            files.put(name, content);
        }

        for (String name : vertexes.keySet()){
            String content = vertexes.get(name);

            for (LazuliUniform uni : uniform) {
                String replacement = "#version 150\nuniform ";
                String target = "#version 150";

                replacement = replacement.concat(uni.freeType);
                replacement = replacement.concat(" ");
                replacement = replacement.concat(uni.name);
                replacement = replacement.concat(";");


                content = content.replace(target, replacement);
            }

            for (Injection inj : injections) {
                if(inj.type == "VERTEX") {
                    String replacement = inj.target.replace("X", "\n".concat(inj.content).concat("\n"));
                    String target = inj.target.replace("X", "");


                    content = content.replace(target, replacement);
                }
            }
            files.put(name, content);
        }

        return files;
    }
}
