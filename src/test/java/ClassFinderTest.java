import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClassFinderTest {

    private static List<String> classList = new ArrayList<>();
    private static String template;

    static {
        classList.addAll(new ArrayList<>(Arrays.asList(
                "a.b.FooBar", "c.d.BarFoo", "a.a.FBarBar",
                "a.b.AbcCba", "c.q.Cccbbb", "a.a.CabdAd",
                "c.h.Bba", "s.d.Dac", "b.a.Cac",
                "s.h.CDaA", "g.d.DbaBB", "a.a.Cad",
                "a.k.Bbb", "h.d.DabA", "a.a.CbaCca",
                "r.y.AaabBb", "c.j.DadaD", "a.a.CbbaB",
                "f.h.Bba", "e.d.Dac", "w.a.Cac")));
    }

    @Test
    public void endWithSpacesTest() throws Exception {
        template = "ba    ";
        List<String> expectedList = Arrays.asList("a.b.AbcCba", "c.h.Bba", "f.h.Bba");
        List<String> resList = findClasses();
        assertEquals(expectedList, resList);
    }

    @Test
    public void divideClassNamesTest() throws Exception {
        List<String> classList = new ArrayList<>(Arrays.asList("a.b.Ab", "b.c.Cd", "b.a.Ab"));
        Map<String, List<String>> resMap = ClassFinder.divideClassNames(classList);
        assertTrue(resMap.get("Ab").size() == 2);
        assertTrue(resMap.get("Cd").size() == 1);
    }


    @Test
    public void wildCardTest() throws Exception {
        template = "B*a";
        List<String> expectedList = Arrays.asList("a.a.FBarBar", "a.b.FooBar", "c.d.BarFoo", "c.h.Bba", "f.h.Bba");

        List<String> resList = ClassFinder.findClassList(ClassFinderTest.classList, template);
        assertEquals(expectedList, resList);
    }

    @Test
    public void lowerCaseTest() throws Exception {
        template = "cb";
        List<String> expectedList = Arrays.asList("a.b.AbcCba", "a.a.CabdAd", "a.a.CbaCca", "a.a.CbbaB", "c.q.Cccbbb");
        List<String> resList = findClasses();
        assertEquals(expectedList, resList);
    }

    @Test
    public void fetchLastWordInPatternTest() {
        assertEquals("T", ClassFinder.fetchLastWordInPattern("AsTT"));
        assertEquals("Asret", ClassFinder.fetchLastWordInPattern("Asret"));
        assertEquals("Rdvuu", ClassFinder.fetchLastWordInPattern("asdTRdvuu"));
        assertEquals("Trove", ClassFinder.fetchLastWordInPattern("Trove"));
        assertEquals("U", ClassFinder.fetchLastWordInPattern("TrovU"));
        assertEquals("U", ClassFinder.fetchLastWordInPattern("rovU"));
    }

    private List<String> findClasses() {
        Map<String, List<String>> stringListMap = ClassFinder.divideClassNames(ClassFinderTest.classList);
        List<String> classList = ClassFinder.findClassList(stringListMap.keySet(), template);

        List<String> resList = new ArrayList<>();

        classList.forEach(className -> {
            List<String> classNames = stringListMap.get(className);
            resList.addAll(classNames);
        });
        return resList;
    }

}