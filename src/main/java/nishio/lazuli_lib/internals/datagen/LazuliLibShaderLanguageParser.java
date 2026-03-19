package nishio.lazuli_lib.internals.datagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.stuff.LazuliRegexPatterns;
import org.apache.commons.io.FileUtils;

import static java.lang.Math.max;

public class LazuliLibShaderLanguageParser {
    private static Map<Identifier, String> cachedImports = new HashMap<>();
    public static String parseToGLSL(String LLSL){
        StringBuilder GLSLsb = new StringBuilder(LLSL);

        List<Identifier> imports = new ArrayList<>();
        List<String> settings = new ArrayList<>();

        // parse Imports
        Matcher importMatcher = LazuliRegexPatterns.LLSL_IMPORT_REGEX.matcher(GLSLsb.toString());
        while (importMatcher.find()) {
            String moduleName = importMatcher.group(1).replace(".", ":");
            imports.add(Identifier.tryParse(moduleName));
            GLSLsb.replace(importMatcher.start(), importMatcher.end(), "\n// import ".concat(moduleName).concat("\n"));
            importMatcher = LazuliRegexPatterns.LLSL_IMPORT_REGEX.matcher(GLSLsb.toString());
        }

        // parse settings
        Matcher settingMatcher = LazuliRegexPatterns.LLSL_ENABLE_REGEX.matcher(GLSLsb.toString());
        while (settingMatcher.find()) {
            String settingName = settingMatcher.group(1);
            settings.add(settingName);
            GLSLsb.replace(settingMatcher.start(), settingMatcher.end(), "\n// ".concat(settingName).concat("\n"));
            settingMatcher = LazuliRegexPatterns.LLSL_ENABLE_REGEX.matcher(GLSLsb.toString());
        }

        //region === parse autoSemiColom ===
        if (settings.contains("autoSemicolon")){
            int counter = 0;
            int parenthesisLevel = 0;
            boolean skipLine = false;
            while (counter < GLSLsb.length() - 2){

                if (GLSLsb.charAt(counter) == '#') {
                    skipLine = true;
                }

                if (GLSLsb.charAt(counter) == '{') {
                    skipLine = true;
                }

                if (GLSLsb.charAt(counter) == '}') {
                    skipLine = true;
                }

                if (GLSLsb.charAt(counter) == '/' && GLSLsb.charAt(counter + 1) == '/') {
                    skipLine = true;
                }

                if(!skipLine) {
                    if (GLSLsb.charAt(counter) == '(') {
                        parenthesisLevel++;
                    }
                    if (GLSLsb.charAt(counter) == ')') {
                        parenthesisLevel--;
                    }

                    if (
                            GLSLsb.charAt(counter + 1) == '\n' &&
                            GLSLsb.charAt(counter) != ';' &&
                            GLSLsb.charAt(counter) != '\n' &&
                            GLSLsb.charAt(counter) != '\t'
                    ) {
                        GLSLsb.insert(counter + 1, ";");
                        counter++;
                    }

                } else {
                    if (GLSLsb.charAt(counter) == '\n') {
                        skipLine = false;
                    }
                }

                counter++;
            }
        }
        //endregion

        //region === parse SugarSugars! ===
        if(settings.contains("SugarSugar")){
            settings.add("samplerSugar");
            settings.add("uvComponents");
            settings.add("fFloat");
        }

        int counter = 0;
        int parenthesisLevel = 0;
        int parenthesisLevelMem = 0;
        String mem1 = "";
        String mem2 = "";
        String mem3 = "";
        boolean skipLine = false;
        boolean isNumber = false;

        while (counter < GLSLsb.length() - 2){
            if (GLSLsb.charAt(counter) == '#') {skipLine = true;}
            if (GLSLsb.charAt(counter) == '{') {skipLine = true;}
            if (GLSLsb.charAt(counter) == '}') {skipLine = true;}
            if (GLSLsb.charAt(counter) == '/' && GLSLsb.charAt(counter + 1) == '/') {skipLine = true;}

            if(!skipLine) {
                char character = GLSLsb.charAt(counter);
                char nextCharacter = GLSLsb.charAt(counter + 1);
                boolean isNextSeparator = (nextCharacter == ' ' || nextCharacter == ';' || nextCharacter == ')' || nextCharacter == '+' || nextCharacter == '-' || nextCharacter == '*' || nextCharacter == '/' || nextCharacter == ',' || nextCharacter == '.');

                switch (character) {
                    case '(' : parenthesisLevel++; mem1 = ""; break;
                    case ')' : parenthesisLevel--; mem1 = ""; break;
                    case ' ', ',' : mem1 = "";
                    case '.' : mem2 = ""; mem3 = mem1; break;
                    default: mem1 = mem1.concat(String.valueOf(character)); mem2 = mem2.concat(String.valueOf(character)); break;
                }

                switch (mem2) {
                    case "u" :
                        if (settings.contains("uvComponents") && isNextSeparator){
                            GLSLsb.replace(counter, counter + 1, "x");
                        }
                        mem2 = "";
                        break;
                    case "v" :
                        if (settings.contains("uvComponents") && isNextSeparator){
                            GLSLsb.replace(counter, counter + 1, "y");
                        }
                        mem2 = "";
                        break;
                    case "uv" :
                        if (settings.contains("uvComponents") && isNextSeparator){
                            GLSLsb.replace(counter - 1, counter + 1, "xy");
                        }
                        mem2 = "";
                        break;
                    case "vu" :
                        if (settings.contains("uvComponents") && isNextSeparator){
                            GLSLsb.replace(counter - 1, counter + 1, "yx");
                        }
                        mem2 = "";
                        break;
                    case "tex" :
                        if(nextCharacter == '(') {
                            if (settings.contains("samplerSugar")) {
                                GLSLsb.replace(counter - 3 - mem3.length(), counter + 2, "texture(".concat(mem3).concat(", "));
                            }
                            mem2 = "";
                            break;
                        }
                    case "texture" :
                        if(nextCharacter == '(') {
                            if (settings.contains("samplerSugar")) {
                                GLSLsb.replace(counter - 7 - mem3.length(), counter + 2, "texture(".concat(mem3).concat(", "));
                            }
                            mem2 = "";
                            break;
                        }

                }

            } else {
                if (GLSLsb.charAt(counter) == '\n') {
                    skipLine = false;
                    mem1 = "";
                }
            }

            counter++;
        }

        //endregion

        // add imports
        for (Identifier importId : imports){
            String importString = null;
            if (cachedImports.containsKey(importId)){
                importString = cachedImports.get(importId);
            } else {
                java.io.InputStream stream = LazuliEasyFileAcess.getDirectOrPackagePath(importId, ".glsl", "/shaders/include/");

                try (var reader = new BufferedReader(new InputStreamReader(stream))) {
                    importString = reader.lines().collect(Collectors.joining("\n"));
                    importString = importString.replace("#version 150", "").replace("#version 100", "");
                    importString = parseToGLSL(importString);
                    cachedImports.put(importId, importString);

                } catch (IOException e) {
                    LazuliLog.Warp.error("Failed to add include %s".formatted(importId.toString()));
                    e.printStackTrace();
                }
            }

            int insertLine = max(max(GLSLsb.lastIndexOf("uniform "), GLSLsb.lastIndexOf("\nin ")), GLSLsb.lastIndexOf("\nout "));
            counter = insertLine;

            while (GLSLsb.charAt(counter) != '\n') {
                counter++;
            }
            GLSLsb.insert(counter, "\n\n".concat(importString));
        }

        return GLSLsb.toString();
    }

    public static void clearCache() {
        cachedImports.clear();
    }
}
