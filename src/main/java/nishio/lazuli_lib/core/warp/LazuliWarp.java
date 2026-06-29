package nishio.lazuli_lib.core.warp;

import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.compat.LazuliSodiumResourcePackCompat;
import nishio.lazuli_lib.internals.datagen.LazuliTrueWarp;

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
        LazuliSodiumResourcePackCompat.checkForSodium();
        trueWarp.addTarget(LazuliSodiumResourcePackCompat.filterSodiumTargets(target));
        return this;
    }

    public LazuliWarp addTargets(List<Identifier> targets){
        for(Identifier target: targets) {
            this.addTarget(target);
        }
        return this;
    }

    public LazuliWarp addTargets(Set<Identifier> targets){
        for(Identifier target: targets) {
            this.addTarget(target);
        }
        return this;
    }
}
