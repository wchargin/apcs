public class SplatSearch {
    
    public static void main(String[] args) 
    {
        int[] arr = {1,2,3,4,5,6,7,8,9};
        int[] arr1 = {7,8,9,1,2,3,4,5,6};
        int[] arr2 = {9,1,2,3,4,5,6,7,8};
        
        System.out.println(findMin(arr));
        System.out.println(findMin(arr1));
        System.out.println(findMin(arr2));
    }
    
    public static int findMin(int[] input)
    {
        int low = 0, high = input.length/2;
        
        if(input[low] < input[high])
        {
            low = high;
            high = input.length - 1;
        }
        
        while(low != high)
        {
            int middle = low + (high-low) >> 1;
            if(input[low] > input[middle])
            {
                high = middle;
                continue;
            }
            else
            {
                low = middle;
                continue;
            }
        }
        return high;
    }
    
}