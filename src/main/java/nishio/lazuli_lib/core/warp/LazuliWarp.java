package nishio.lazuli_lib.core.warp;

import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.datagen.LazuliTrueWarp;
import nishio.lazuli_lib.internals.datagen.LazuliWarpManager;

import java.util.List;
import java.util.Set;

public class LazuliWarp {
    public LazuliTrueWarp trueWarp;
    public LazuliWarp(Identifier path){
        trueWarp = new LazuliTrueWarp(path);
    }

    public LazuliWarp register(){
        trueWarp.register();
        return this;
    }
    public LazuliWarp addTarget(Identifier target){
        trueWarp.addTarget(target);
        return this;
    }

    public LazuliWarp addTargets(List<Identifier> targets){
        for(Identifier target: targets) {
            trueWarp.addTarget(target);
        }
        return this;
    }

    public LazuliWarp addTargets(Set<Identifier> targets){
        for(Identifier target: targets) {
            trueWarp.addTarget(target);
        }
        return this;
    }
}
