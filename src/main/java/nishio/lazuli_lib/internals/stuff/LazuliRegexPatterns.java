package nishio.lazuli_lib.internals.stuff;

import java.util.regex.Pattern;

public class LazuliRegexPatterns {
    public static Pattern WARP_FRAGMENT_REGEX = Pattern.compile(
            "(?<=(//WARP_FRAGMENT))\\(([^)]*)\\)\\n([\\s\\S]*?\\{)([\\s\\S]*?)(?=(\\}//BREAK))",
            Pattern.MULTILINE
    );
    public static Pattern WARP_VERTEX_REGEX = Pattern.compile(
            "(?<=(//WARP_VERTEX))\\(([^)]*)\\)\\n([\\s\\S]*?\\{)([\\s\\S]*?)(?=(\\}//BREAK))",
            Pattern.MULTILINE
    );

    public static Pattern WARP_UNIFORM_REGEX = Pattern.compile(
            "(?<=uniform )(?!sampler2D)(\\w+) (\\w+)",
            Pattern.MULTILINE);

    public static Pattern WARP_SAMPLER_REGEX = Pattern.compile(
            "(?<=uniform sampler2D )(\\w+)",
            Pattern.MULTILINE
    );
}
