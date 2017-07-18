
import java.nio.file.Files;
import java.util.*;

import static java.lang.Character.isUpperCase;
import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.toList;

public class ClassFinder {

    public static void main(String[] args) throws Exception {
        List<String> inputLines = Files.readAllLines(get(args[0]));
        List<String> lines = prepareLines(inputLines);
        Map<String, List<String>> resMap = new TreeMap<>(divideClassNames(lines));
        List<String> resList = findClassList(resMap.keySet(), args[1]);

        resList.forEach(className -> {
            List<String> classNames = resMap.get(className);
            classNames.forEach(System.out::println);
        });
    }


    static Map<String, List<String>> divideClassNames(List<String> lines) {
        Map<String, List<String>> resMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        lines.forEach(
                fullClassName -> {
                    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                    List<String> fullClassNameList = getOrCreateClassList(resMap, className);
                    fullClassNameList.add(fullClassName);
                    resMap.put(className, fullClassNameList);
                });
        return resMap;
    }

    private static List<String> getOrCreateClassList(Map<String, List<String>> map, String key) {
        List<String> classList = map.get(key);
        if (classList == null || classList.size() < 1) {
            classList = new ArrayList<>();
        }
        return classList;
    }

    private static List<String> prepareLines(List<String> inputLines) {
        return inputLines.stream().filter(s -> !s.contains("`")).collect(toList());
    }

    static List<String> findClassList(Collection<String> classNames, String template) {
        List<String> resList = new ArrayList<>();
        boolean lowerCaseRule = isLowerCase(template);
        boolean spaceRule = isEndWithSpace(template);
        String lastWord = null;
        if (spaceRule) {
            lastWord = fetchLastWordInPattern(template.trim());
        }

        for (String className : classNames) {
            if (spaceRule && (!className.endsWith(lastWord))) {
                continue;
            }
            if (lowerCaseRule) {
                className = className.toLowerCase();
                template = template.toLowerCase();
            }
            if (patternMatching(className, template.trim()))
                resList.add(className);
        }
        Collections.sort(resList);
        return resList;
    }

    static String fetchLastWordInPattern(String template) {
        StringBuilder sb = new StringBuilder(template.trim());
        String reverseStr = sb.reverse().toString();
        for (char ch : reverseStr.toCharArray()) {
            if (isUpperCase(ch)) {
                return new StringBuilder(reverseStr.substring(0, reverseStr.indexOf(ch) + 1))
                        .reverse().toString();
            }
        }
        return template;
    }

    private static boolean patternMatching(String className, String template) {
        boolean res = true;
        for (Character templateCh : template.toCharArray()) {
            if (templateCh.compareTo('*') == 0) {
                className = className.substring(1);//
                continue;
            }
            int position = className.indexOf(templateCh);
            if (position > -1) {
                className = className.substring(position);
            } else {
                res = false;
                break;
            }
        }
        return res;
    }

    private static boolean isEndWithSpace(String template) {
        return template.endsWith(" ");
    }

    private static boolean isLowerCase(String template) {
        for (char ch : template.toCharArray()) {
            if (!Character.isLowerCase(ch)) {
                return false;
            }
        }
        return true;
    }

}
