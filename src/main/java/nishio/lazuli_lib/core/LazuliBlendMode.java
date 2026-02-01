package nishio.lazuli_lib.core;

public class LazuliBlendMode {
    private final String func;
    private final String src;
    private final String dst;

    public static final LazuliBlendMode DEFAULT =
            new LazuliBlendMode("add", "srcalpha", "1-srcalpha");

    public static final LazuliBlendMode ADDITIVE =
            new LazuliBlendMode("add", "srcalpha", "one");

    public static final LazuliBlendMode ADDITIVE_FULL =
            new LazuliBlendMode("add", "one", "one");

    public static final LazuliBlendMode OPAQUE =
            new LazuliBlendMode("add", "one", "zero");

    public static final LazuliBlendMode PREMULTIPLIED_ALPHA =
            new LazuliBlendMode("add", "one", "1-srcalpha");

    public static final LazuliBlendMode MULTIPLY =
            new LazuliBlendMode("add", "dstcolor", "zero");

    public static final LazuliBlendMode MULTIPLY_ALPHA =
            new LazuliBlendMode("add", "dstcolor", "1-srcalpha");

    public static final LazuliBlendMode SCREEN =
            new LazuliBlendMode("add", "one", "1-srccolor");

    public static final LazuliBlendMode SCREEN_ALPHA =
            new LazuliBlendMode("add", "one", "1-srcalpha");

    public static final LazuliBlendMode SUBTRACT =
            new LazuliBlendMode("sub", "srcalpha", "one");

    public static final LazuliBlendMode REVERSE_SUBTRACT =
            new LazuliBlendMode("revsub", "srcalpha", "one");

    public static final LazuliBlendMode DIFFERENCE =
            new LazuliBlendMode("sub", "one", "one");

    public static final LazuliBlendMode MIN =
            new LazuliBlendMode("min", "one", "one");

    public static final LazuliBlendMode MAX =
            new LazuliBlendMode("max", "one", "one");

    public static final LazuliBlendMode INVERT =
            new LazuliBlendMode("add", "1-dstcolor", "zero");

    public static final LazuliBlendMode DARKEN =
            new LazuliBlendMode("min", "one", "one");

    public static final LazuliBlendMode LIGHTEN =
            new LazuliBlendMode("max", "one", "one");

    private LazuliBlendMode(String func, String src, String dst) {
        this.func = func;
        this.src = src;
        this.dst = dst;
    }

    public String equation() { return func; }
    public String src() { return src; }
    public String dst() { return dst; }

    public LazuliBlendMode setFuncAdd() {
        return new LazuliBlendMode("add", src, dst);
    }

    public LazuliBlendMode setFuncSub() {
        return new LazuliBlendMode("sub", src, dst);
    }

    public LazuliBlendMode setFuncReverseSub() {
        return new LazuliBlendMode("revsub", src, dst);
    }

    public LazuliBlendMode setFuncMin() {
        return new LazuliBlendMode("min", src, dst);
    }

    public LazuliBlendMode setFuncMax() {
        return new LazuliBlendMode("max", src, dst);
    }

    public LazuliBlendMode setSrc(String newSrc) {
        return new LazuliBlendMode(func, newSrc, dst);
    }

    public LazuliBlendMode setDst(String newDst) {
        return new LazuliBlendMode(func, src, newDst);
    }
}

