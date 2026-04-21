import java.util.*;

public class FullSortingBenchmark {

    static int n = 1000;
    static String[] testTypes = {
            "Random", "Sorted", "Reverse",
            "Almost Sorted", "Half Sorted", "Few Unique"
    };

    public static void main(String[] args) {

        // Map: algorithm -> list of times
        Map<String, List<Long>> results = new LinkedHashMap<>();

        String[] algorithms = {
                "Bubble", "Insertion", "Selection",
                "Cocktail", "Gnome", "Shell",
                "Merge", "Quick", "Heap",
                "Counting", "Radix", "Bucket"
        };

        for (String algo : algorithms) {
            System.out.println(algo + " Sort is being tested...");
            results.put(algo, new ArrayList<>());

            for (String test : testTypes) {

                int[] arr = generateArray(n, test);
                int[] copy = Arrays.copyOf(arr, arr.length);

                long start = System.nanoTime();

                // CALL CORRECT SORT
                switch (algo) {
                    case "Bubble": bubbleSort(copy); break;
                    case "Insertion": insertionSort(copy); break;
                    case "Selection": selectionSort(copy); break;
                    case "Cocktail": cocktailSort(copy); break;
                    case "Gnome": gnomeSort(copy); break;
                    case "Shell": shellSort(copy); break;
                    case "Merge": mergeSort(copy, 0, copy.length - 1); break;
                    case "Quick": quickSort(copy, 0, copy.length - 1); break;
                    case "Heap": heapSort(copy); break;
                    case "Counting": countingSort(copy); break;
                    case "Radix": radixSort(copy); break;
                    case "Bucket": bucketSort(copy); break;
                }

                long end = System.nanoTime();
                long time = end - start;

                results.get(algo).add(time);

                System.out.println("  Test type: " + test);
                System.out.printf("    n=%d: %.6f sec\n", n, time / 1e9);
            }
            System.out.println("----------------------------------------");
        }

        // ================= ANALYSIS =================
        System.out.println("\n=== ANALYSIS ===");

        for (String algo : results.keySet()) {
            List<Long> times = results.get(algo);

            long sum = 0, min = Long.MAX_VALUE, max = Long.MIN_VALUE;

            for (long t : times) {
                sum += t;
                min = Math.min(min, t);
                max = Math.max(max, t);
            }

            double avg = sum / (double) times.size();

            System.out.println(algo + " Sort:");
            System.out.printf("  Avg time: %.6f sec\n", avg / 1e9);
            System.out.printf("  Best: %.6f sec\n", min / 1e9);
            System.out.printf("  Worst: %.6f sec\n\n", max / 1e9);
        }
    }

    // ================= ARRAY GENERATOR =================
    static int[] generateArray(int n, String type) {
        Random r = new Random();
        int[] arr = new int[n];

        switch (type) {
            case "Random":
                for (int i = 0; i < n; i++) arr[i] = r.nextInt(100);
                break;

            case "Sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                break;

            case "Reverse":
                for (int i = 0; i < n; i++) arr[i] = n - i;
                break;

            case "Almost Sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                int temp = arr[0]; arr[0] = arr[1]; arr[1] = temp;
                break;

            case "Half Sorted":
                for (int i = 0; i < n/2; i++) arr[i] = i;
                for (int i = n/2; i < n; i++) arr[i] = r.nextInt(100);
                break;

            case "Few Unique":
                for (int i = 0; i < n; i++) arr[i] = r.nextInt(3);
                break;
        }
        return arr;
    }

    // ================= SIMPLE SORTS =================
    static void bubbleSort(int[] a) {
        for (int i = 0; i < a.length-1; i++)
            for (int j = 0; j < a.length-i-1; j++)
                if (a[j] > a[j+1]) swap(a, j, j+1);
    }

    static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i], j = i-1;
            while (j >= 0 && a[j] > key) {
                a[j+1] = a[j];
                j--;
            }
            a[j+1] = key;
        }
    }

    static void selectionSort(int[] a) {
        for (int i = 0; i < a.length-1; i++) {
            int min = i;
            for (int j = i+1; j < a.length; j++)
                if (a[j] < a[min]) min = j;
            swap(a, i, min);
        }
    }

    static void cocktailSort(int[] a) {
        boolean swapped = true;
        int start = 0, end = a.length - 1;

        while (swapped) {
            swapped = false;
            for (int i = start; i < end; i++)
                if (a[i] > a[i+1]) { swap(a, i, i+1); swapped = true; }

            if (!swapped) break;

            swapped = false;
            end--;

            for (int i = end; i > start; i--)
                if (a[i] < a[i-1]) { swap(a, i, i-1); swapped = true; }

            start++;
        }
    }

    static void gnomeSort(int[] a) {
        int i = 0;
        while (i < a.length) {
            if (i == 0 || a[i] >= a[i-1]) i++;
            else { swap(a, i, i-1); i--; }
        }
    }

    static void shellSort(int[] a) {
        for (int gap = a.length/2; gap > 0; gap /= 2)
            for (int i = gap; i < a.length; i++) {
                int temp = a[i], j;
                for (j = i; j >= gap && a[j-gap] > temp; j -= gap)
                    a[j] = a[j-gap];
                a[j] = temp;
            }
    }

    // ================= ADVANCED =================
    static void mergeSort(int[] a, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(a, l, m);
            mergeSort(a, m+1, r);
            merge(a, l, m, r);
        }
    }

    static void merge(int[] a, int l, int m, int r) {
        int[] left = Arrays.copyOfRange(a, l, m+1);
        int[] right = Arrays.copyOfRange(a, m+1, r+1);

        int i=0, j=0, k=l;

        while (i<left.length && j<right.length)
            a[k++] = (left[i] <= right[j]) ? left[i++] : right[j++];

        while (i<left.length) a[k++] = left[i++];
        while (j<right.length) a[k++] = right[j++];
    }

    static void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int pi = partition(a, low, high);
            quickSort(a, low, pi-1);
            quickSort(a, pi+1, high);
        }
    }

    static int partition(int[] a, int low, int high) {
        int pivot = a[high], i = low-1;
        for (int j = low; j < high; j++)
            if (a[j] < pivot) swap(a, ++i, j);
        swap(a, i+1, high);
        return i+1;
    }

    static void heapSort(int[] a) {
        int n = a.length;
        for (int i = n/2-1; i>=0; i--) heapify(a,n,i);
        for (int i = n-1; i>0; i--) {
            swap(a,0,i);
            heapify(a,i,0);
        }
    }

    static void heapify(int[] a, int n, int i) {
        int largest = i, l = 2*i+1, r = 2*i+2;
        if (l<n && a[l]>a[largest]) largest = l;
        if (r<n && a[r]>a[largest]) largest = r;
        if (largest != i) {
            swap(a,i,largest);
            heapify(a,n,largest);
        }
    }

    static void countingSort(int[] a) {
        int max = Arrays.stream(a).max().getAsInt();
        int[] count = new int[max+1];

        for (int num : a) count[num]++;
        int index = 0;

        for (int i = 0; i < count.length; i++)
            while (count[i]-- > 0) a[index++] = i;
    }

    static void radixSort(int[] a) {
        int max = Arrays.stream(a).max().getAsInt();
        for (int exp = 1; max/exp > 0; exp *= 10)
            countingSortRadix(a, exp);
    }

    static void countingSortRadix(int[] a, int exp) {
        int[] output = new int[a.length];
        int[] count = new int[10];

        for (int num : a)
            count[(num/exp)%10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i-1];

        for (int i = a.length-1; i>=0; i--) {
            int digit = (a[i]/exp)%10;
            output[count[digit]-1] = a[i];
            count[digit]--;
        }

        System.arraycopy(output, 0, a, 0, a.length);
    }

    static void bucketSort(int[] a) {
        int n = a.length;
        List<List<Integer>> buckets = new ArrayList<>();

        for (int i = 0; i < n; i++)
            buckets.add(new ArrayList<>());

        int max = Arrays.stream(a).max().getAsInt();

        for (int num : a)
            buckets.get((num * n) / (max+1)).add(num);

        for (List<Integer> bucket : buckets)
            Collections.sort(bucket);

        int index = 0;
        for (List<Integer> bucket : buckets)
            for (int num : bucket)
                a[index++] = num;
    }

    // ================= HELPER =================
    static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
