package java.alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <a href="http://dynamicallyinvokable.blogspot.com/2016/10/dynamic-programming-in-5-easy-steps.html">Text Justification</a>
 */
public class TextJustification {

    public List<String> fullJustify(String[] words, int maxWidth) {
        return buildLines(words, maxWidth);
    }

    static class Justify {
        final String[] words;
        final int[] mem;
        final int[] parentPointers;

        Justify(String[] _words) {
            words = _words;
            mem = new int[ _words.length ];
            parentPointers = new int[ _words.length ];
            Arrays.fill(mem, -1);
        }

        @Override
        public String toString() {
            return "Justify{" +
                    "mem=" + Arrays.toString(mem) +
                    ", parentPointers=" + Arrays.toString(parentPointers) +
                    '}';
        }
    }

    private List<String> buildLines(String[] words, int maxWidth) {
        Justify j = new Justify(words);
        calcJustify(j, maxWidth, 0);

        List<String> lines = new ArrayList<>();
        int from = 0;

        while(from < words.length) {
            int next = j.parentPointers[from];

            String line = Arrays.stream(words)
                    .skip(from)
                    .limit(next - from)
                    .collect(Collectors.joining(" "));

            lines.add(line);
            from = next;
        }

        return lines;
    }

    private static int calcJustify(Justify justify, int maxWidth, int initStep) {
        if (initStep == justify.words.length) {
            return 0;
        }

        if (justify.mem[initStep] != -1) {
            return justify.mem[initStep];
        }

        int minBadness = Integer.MAX_VALUE;
        for (int nextStep = initStep + 1; nextStep <= justify.words.length; nextStep++) {

            int badness = badness(justify.words, initStep, nextStep, maxWidth);
            int nextMinBadness = calcJustify(justify, maxWidth, nextStep);
            int nextBadness = badness + nextMinBadness;
            if (minBadness > nextBadness) {
                //save best choice
                System.out.println("minBadness=" + minBadness + ", initStep=" + initStep + ", nextStep=" + nextStep);
                justify.parentPointers[initStep] = nextStep;
                minBadness = nextBadness;
            }

        }

        // save solution
        justify.mem[initStep] = minBadness;
        return minBadness;
    }

    /**
     * badness of a line is equal to:
     *   ( lengthOfLine - numverOfWords_WithSpacesBetweenWords ) ^ 3
     */
    private static int badness(String[] words, int from, int to, int width) {
        int spaces = to - from - 1;

        if (to == words.length) //don't care about last line
            return 0;


        int wordsLen = Arrays.stream(words)
                .skip(from)
                .limit(to - from)
                .mapToInt(String::length)
                .sum();

        if (wordsLen + spaces > width) {
            return 10*1000*1000;
        }

        return (int) Math.pow(width - wordsLen - spaces, 3);
    }


    public static void main(String[] args) {
        List<String> strings = new TextJustification().fullJustify(
                new String[]{"This", "is", "an", "example", "of", "text", "justification."},
                16
        );

        System.out.println(strings);
    }
}
