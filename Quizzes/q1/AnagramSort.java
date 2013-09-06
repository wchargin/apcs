import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnagramSort {
    
    public static String[] anagramSort(String[] input) {
        Map<String, List<String>> index = new HashMap<>();
        
        for (String word : input) {
            boolean found = false;
            for (String key : index.keySet()) {
                if (isAnagram(key, word)) {
                    index.get(key).add(word);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Create new key
                index.put(word, new ArrayList<String>(Arrays.asList(word)));
            }
        }
        
        String[] result = new String[input.length];
        int i = 0;
        for (List<String> list : index.values()) {
            for (String word : list) {
                result[i++] = word;
            }
        }
        return result;
    }
    
    private static boolean isAnagram(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        List<Character> c1 = toCharacterList(s1),
                        c2 = toCharacterList(s2);
                        
        Collections.sort(c1);
        Collections.sort(c2);
        
        return c1.equals(c2);
    }
    
    private static List<Character> toCharacterList(String s) {
        List<Character> l = new ArrayList<>(s.length());
        for (char c : s.toCharArray()) {
            l.add(c);
        }
        return l;
    }
    
    /**
     * Try this:
     * 
     * java AnagramSort dog tars pots stop god rats tops
     */
    public static void main(String[] args) {
        System.out.println(Arrays.toString(anagramSort(args)));
    }
    
}