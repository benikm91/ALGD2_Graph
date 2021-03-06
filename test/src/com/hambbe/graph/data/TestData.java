package com.hambbe.graph.data;

import com.hambbe.graph.IntGraph;
import com.hambbe.graph.Vertex;

import java.util.function.Function;

public class TestData {

    private static Vertex generateNext(IntGraph<String> graph, String current, int remaining) {
        if (remaining == -1) return null;
        Vertex parent = null;
        if (!"".equals(current)) parent = graph.addVertex(current);
        for (char c = 'A'; c <= 'Z'; c++) {
            Vertex child = generateNext(graph, current + c, remaining - 1);
            if (parent != null && child != null) {
                graph.connect(parent, child, 1);
            }
        }
        return parent;
    }

    /**
     * Get an ABC Graph
     *
     * ABC Graph has following connections:
     * wn -> wnm
     *
     * where
     *
     * w : word (Element of alphabet*)
     * n : letter (Element of alphabet)
     * m : letter (Element of alphabet)
     * alphabet : { A, B, C ...}
     *
     * e.g.
     * ADF -> { ADFA, ADFB, ADFC, ... }
     *
     * where
     *
     * w : AD
     * n : F
     * m : A or B or C
     *
     * Therefore an ABCGraph has 26^maxWordLength vertexes and 26^maxWordLength edges.
     *
     * @param maxWordLength Generate all nodes so that |w| <= maxWordLength.
     * @return ABCIntGraph with feature described above.
     */
    public static IntGraph<String> ABCintGraph(final int maxWordLength) {
        final IntGraph<String> graph = new IntGraph<>();
        generateNext(graph, "", maxWordLength);
        return graph;
    }

    public static Function<String, Double> ABCGraphOptimalHeuristic(final String toValue) {
        return (s) -> (toValue.charAt(s.length() - 1) == s.charAt(s.length() - 1)) ? 1d : 0d;
    }


}
