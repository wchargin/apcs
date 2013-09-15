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
        
        // +1 for each in s1, -1 for each in s2
        int[] netFrequency = new int[128 /* assuming in ASCII */];
        
        for (char c : s1.toCharArray()) {
            netFrequency[(int) c]++;
        }
        for (char c : s2.toCharArray()) {
            netFrequency[(int) c]--;
        }
        // If equal all elements of netFrequency are 0
        for (int i : netFrequency) {
            if (i != 0) {
                return false;
            }
        }
        return true;
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