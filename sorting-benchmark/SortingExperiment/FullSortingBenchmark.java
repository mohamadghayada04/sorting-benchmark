import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FullSortingBenchmark.java
 *
 * Benchmarks 12 sorting algorithms across 6 input types and 3 input sizes.
 * Results are printed to console and written to ../results/timings.csv.
 *
 * Compile:  javac FullSortingBenchmark.java
 * Run:      java FullSortingBenchmark
 *
 * Author: Mohammad Gayada
 * West University of Timisoara
 */
public class FullSortingBenchmark {

    static final int WARMUP_RUNS = 5;
    static final int MEASURE_RUNS = 20;
    static final int[] SIZES = {10, 1000, 10000};

    // Algorithms excluded at n=10000 due to excessive runtime
    static final String[] SLOW_ALGORITHMS = {"Bubble", "Selection", "Gnome", "Cocktail"};

    // =========================================================
    //  SORTING ALGORITHMS
    // =========================================================

    static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) minIdx = j;
            }
            int tmp = arr[minIdx]; arr[minIdx] = arr[i]; arr[i] = tmp;
        }
    }

    static void gnomeSort(int[] arr) {
        int n = arr.length;
        int i = 0;
        while (i < n) {
            if (i == 0 || arr[i] >= arr[i - 1]) {
                i++;
            } else {
                int tmp = arr[i]; arr[i] = arr[i - 1]; arr[i - 1] = tmp;
                i--;
            }
        }
    }

    static void cocktailSort(int[] arr) {
        int n = arr.length;
        boolean swapped = true;
        int start = 0, end = n - 1;
        while (swapped) {
            swapped = false;
            for (int i = start; i < end; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i]; arr[i] = arr[i + 1]; arr[i + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break;
            swapped = false;
            end--;
            for (int i = end - 1; i >= start; i--) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i]; arr[i] = arr[i + 1]; arr[i + 1] = tmp;
                    swapped = true;
                }
            }
            start++;
        }
    }

    static void shellSort(int[] arr) {
        int n = arr.length;
        int gap = 1;
        while (gap < n / 3) gap = 3 * gap + 1;  // Knuth's sequence
        while (gap >= 1) {
            for (int i = gap; i < n; i++) {
                int key = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > key) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = key;
            }
            gap /= 3;
        }
    }

    static void mergeSort(int[] arr) {
        if (arr.length <= 1) return;
        mergeSortHelper(arr, 0, arr.length - 1);
    }

    static void mergeSortHelper(int[] arr, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSortHelper(arr, left, mid);
        mergeSortHelper(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1, n2 = right - mid;
        int[] L = Arrays.copyOfRange(arr, left, mid + 1);
        int[] R = Arrays.copyOfRange(arr, mid + 1, right + 1);
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) arr[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    static void quickSort(int[] arr) {
        if (arr.length <= 1) return;
        quickSortHelper(arr, 0, arr.length - 1);
    }

    static void quickSortHelper(int[] arr, int low, int high) {
        if (low >= high) return;
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        int tmp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = tmp;
        int pi = i + 1;
        quickSortHelper(arr, low, pi - 1);
        quickSortHelper(arr, pi + 1, high);
    }

    static void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            int tmp = arr[0]; arr[0] = arr[i]; arr[i] = tmp;
            heapify(arr, i, 0);
        }
    }

    static void heapify(int[] arr, int n, int i) {
        int largest = i, left = 2 * i + 1, right = 2 * i + 2;
        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;
        if (largest != i) {
            int tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
            heapify(arr, n, largest);
        }
    }

    static void countingSort(int[] arr) {
        if (arr.length == 0) return;
        int max = arr[0], min = arr[0];
        for (int x : arr) { if (x > max) max = x; if (x < min) min = x; }
        int range = max - min + 1;
        int[] count = new int[range];
        for (int x : arr) count[x - min]++;
        int idx = 0;
        for (int i = 0; i < range; i++)
            while (count[i]-- > 0) arr[idx++] = i + min;
    }

    static void radixSort(int[] arr) {
        if (arr.length == 0) return;
        int max = arr[0];
        for (int x : arr) if (x > max) max = x;
        for (int exp = 1; max / exp > 0; exp *= 10)
            countingSortByDigit(arr, exp);
    }

    static void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        for (int x : arr) count[(x / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[--count[digit]] = arr[i];
        }
        System.arraycopy(output, 0, arr, 0, n);
    }

    static void bucketSort(int[] arr) {
        if (arr.length == 0) return;
        int max = arr[0], min = arr[0];
        for (int x : arr) { if (x > max) max = x; if (x < min) min = x; }
        int n = arr.length;
        @SuppressWarnings("unchecked")
        java.util.List<Integer>[] buckets = new java.util.ArrayList[n];
        for (int i = 0; i < n; i++) buckets[i] = new java.util.ArrayList<>();
        double range = max - min + 1.0;
        for (int x : arr) {
            int idx = (int) ((x - min) / range * (n - 1));
            buckets[idx].add(x);
        }
        int pos = 0;
        for (java.util.List<Integer> bucket : buckets) {
            int[] b = bucket.stream().mapToInt(Integer::intValue).toArray();
            insertionSort(b);  // small buckets sorted with Insertion Sort
            for (int x : b) arr[pos++] = x;
        }
    }

    // =========================================================
    //  INPUT GENERATORS
    // =========================================================

    static int[] randomArray(int n, Random rng) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(10 * n);
        return arr;
    }

    static int[] sortedArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        return arr;
    }

    static int[] reverseSortedArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = n - i;
        return arr;
    }

    static int[] almostSortedArray(int n, Random rng) {
        int[] arr = sortedArray(n);
        int swaps = n / 10;
        for (int i = 0; i < swaps; i++) {
            int j = rng.nextInt(n - 1);
            int tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
        }
        return arr;
    }

    static int[] halfSortedArray(int n, Random rng) {
        int[] arr = new int[n];
        for (int i = 0; i < n / 2; i++) arr[i] = i;
        for (int i = n / 2; i < n; i++) arr[i] = rng.nextInt(10 * n);
        return arr;
    }

    static int[] fewUniqueArray(int n, Random rng) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(10);
        return arr;
    }

    // =========================================================
    //  BENCHMARKING HARNESS
    // =========================================================

    @FunctionalInterface
    interface SortFunction { void sort(int[] arr); }

    static double benchmark(SortFunction fn, int[] input) {
        // Warm-up: run WARMUP_RUNS times, discard results
        for (int i = 0; i < WARMUP_RUNS; i++) {
            int[] copy = Arrays.copyOf(input, input.length);
            fn.sort(copy);
        }
        // Measure: run MEASURE_RUNS times, take arithmetic mean
        long total = 0;
        for (int i = 0; i < MEASURE_RUNS; i++) {
            int[] copy = Arrays.copyOf(input, input.length);
            long start = System.nanoTime();
            fn.sort(copy);
            total += System.nanoTime() - start;
        }
        return (double) total / MEASURE_RUNS / 1_000_000_000.0; // convert to seconds
    }

    static boolean isSlowAlgorithm(String name) {
        for (String slow : SLOW_ALGORITHMS)
            if (name.equals(slow)) return true;
        return false;
    }

    // =========================================================
    //  MAIN
    // =========================================================

    public static void main(String[] args) throws IOException {
        Random rng = new Random(42); // fixed seed for reproducibility

        String[] algoNames = {
            "Bubble", "Insertion", "Selection", "Gnome", "Cocktail",
            "Shell", "Merge", "Quick", "Heap", "Counting", "Radix", "Bucket"
        };

        SortFunction[] algos = {
            FullSortingBenchmark::bubbleSort,
            FullSortingBenchmark::insertionSort,
            FullSortingBenchmark::selectionSort,
            FullSortingBenchmark::gnomeSort,
            FullSortingBenchmark::cocktailSort,
            FullSortingBenchmark::shellSort,
            FullSortingBenchmark::mergeSort,
            FullSortingBenchmark::quickSort,
            FullSortingBenchmark::heapSort,
            FullSortingBenchmark::countingSort,
            FullSortingBenchmark::radixSort,
            FullSortingBenchmark::bucketSort
        };

        String[] inputTypes = {
            "Random", "Sorted", "ReverseSorted", "AlmostSorted", "HalfSorted", "FewUnique"
        };

        StringBuilder csv = new StringBuilder();
        csv.append("algorithm,inputType,size,avgTimeSec\n");

        System.out.printf("%-15s %-15s %-8s %-15s%n", "Algorithm", "InputType", "Size", "AvgTime(s)");
        System.out.println("-".repeat(58));

        for (int size : SIZES) {
            for (int a = 0; a < algoNames.length; a++) {
                String name = algoNames[a];
                SortFunction fn = algos[a];

                // Skip slow algorithms at large size
                if (size == 10000 && isSlowAlgorithm(name)) {
                    System.out.printf("%-15s %-15s %-8d %s%n", name, "ALL", size, "SKIPPED (O(n^2) too slow)");
                    continue;
                }

                double totalAvg = 0;
                int count = 0;

                for (String inputType : inputTypes) {
                    int[] input = switch (inputType) {
                        case "Random"        -> randomArray(size, rng);
                        case "Sorted"        -> sortedArray(size);
                        case "ReverseSorted" -> reverseSortedArray(size);
                        case "AlmostSorted"  -> almostSortedArray(size, rng);
                        case "HalfSorted"    -> halfSortedArray(size, rng);
                        case "FewUnique"     -> fewUniqueArray(size, rng);
                        default -> throw new IllegalArgumentException("Unknown type: " + inputType);
                    };

                    double avg = benchmark(fn, input);
                    totalAvg += avg;
                    count++;

                    csv.append(String.format("%s,%s,%d,%.9f%n", name, inputType, size, avg));
                    System.out.printf("%-15s %-15s %-8d %.9f%n", name, inputType, size, avg);
                }

                double overallAvg = totalAvg / count;
                System.out.printf("  --> %-12s overall avg at n=%-6d : %.9f s%n%n", name, size, overallAvg);
            }
        }

        // Write CSV
        try (FileWriter fw = new FileWriter("../results/timings.csv")) {
            fw.write(csv.toString());
            System.out.println("\nResults written to ../results/timings.csv");
        } catch (IOException e) {
            System.out.println("Could not write CSV: " + e.getMessage());
        }
    }
}
