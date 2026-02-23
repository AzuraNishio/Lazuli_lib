package nishio.lazuli_lib.internals.datagen;

import nishio.lazuli_lib.internals.stuff.LazuliRegexPatterns;

import java.util.regex.Matcher;

public class LazuliWarpParser {
    public static LazuliTrueWarp parse(LazuliTrueWarp warp, String warpString){
        Matcher fragmentMatcher = LazuliRegexPatterns.WARP_FRAGMENT_REGEX.matcher(warpString);

        while (fragmentMatcher.find()){
            String target = fragmentMatcher.group(2);
            String content = fragmentMatcher.group(4);
            warp.addInjection(content, target, "FRAGMENT");
        }

        Matcher vertexMatcher = LazuliRegexPatterns.WARP_VERTEX_REGEX.matcher(warpString);

        while (vertexMatcher.find()){
            String target = vertexMatcher.group(2);
            String content = vertexMatcher.group(4);
            warp.addInjection(content, target, "VERTEX");
        }

        Matcher uniformMatcher = LazuliRegexPatterns.WARP_UNIFORM_REGEX.matcher(warpString);

        while (uniformMatcher.find()){
            String type = uniformMatcher.group(1);
            String name = uniformMatcher.group(2);
            warp.addUniform(name, type);
        }

        Matcher samplerMatcher = LazuliRegexPatterns.WARP_SAMPLER_REGEX.matcher(warpString);

        while (samplerMatcher.find()){
            String name = samplerMatcher.group(1);
            warp.addSampler(name);
        }

        return warp;
    }
}
