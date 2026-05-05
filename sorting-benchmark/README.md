# Sorting Algorithms Benchmark

## What This Is

This repository contains the full code and results for the paper:

> **When Do Simple Sorting Algorithms Outperform Advanced Ones?**  
> An Experimental Study Across Different Input Types and Sizes  
> Mohammad Gayada — West University of Timișoara

The paper compares twelve sorting algorithms across six input types and three input sizes, and asks a simple question: when does the theoretical advantage of an efficient algorithm actually start to matter in practice?

The short answer: at n = 10 it doesn't matter at all. At n = 1000 it starts to. At n = 10000 the gap is very obvious.

---

## Repository Structure

```
sorting-benchmark/
│
├── SortingExperiment/
│   └── FullSortingBenchmark.java   # All 12 algorithms + benchmarking harness
│
├── results/
│   └── timings.csv                 # Raw timing data used in the paper
│
├── plot/
│   └── plot_results.py             # Python script to regenerate the figure
│
├── SortingPaper.pdf                # The full paper
└── README.md
```

---

## Algorithms Included

| Category | Algorithms |
|---|---|
| Simple O(n²) | Bubble Sort, Insertion Sort, Selection Sort, Gnome Sort, Cocktail Sort |
| Efficient O(n log n) | Shell Sort, Merge Sort, Quick Sort, Heap Sort |
| Linear-time | Counting Sort, Radix Sort, Bucket Sort |

---

## Input Types Tested

1. **Random** — integers drawn uniformly from [0, 10n]
2. **Sorted** — ascending order
3. **Reverse sorted** — descending order
4. **Almost sorted** — sorted with ⌊n/10⌋ random adjacent swaps
5. **Half sorted** — first half sorted, second half random
6. **Few unique elements** — values drawn from {0, 1, ..., 9}

---

## How to Run

### Requirements
- Java 17 or later
- Python 3 + matplotlib + pandas (for the plot only)

### Compile and run

```bash
cd SortingExperiment
javac FullSortingBenchmark.java
java FullSortingBenchmark
```

Results are printed to the console and written to `results/timings.csv`.

### Regenerate the figure

```bash
cd plot
pip install matplotlib pandas
python plot_results.py
```

This reads `results/timings.csv` and saves `performance_log.png`.

---

## Key Results

| Algorithm | Avg Time at n=1000 (s) |
|---|---|
| Counting Sort | 0.000113 |
| Shell Sort | 0.000248 |
| Heap Sort | 0.000277 |
| Quick Sort | 0.000292 |
| Merge Sort | 0.000374 |
| Radix Sort | 0.000179 |
| Insertion Sort | 0.000875 |
| Bucket Sort | 0.001163 |
| Selection Sort | 0.001442 |
| Gnome Sort | 0.001557 |
| Cocktail Sort | 0.001895 |
| Bubble Sort | 0.002228 |

At n = 10000, Insertion Sort is ~20× slower than Merge Sort on random input. Bubble Sort, Selection Sort, Gnome Sort, and Cocktail Sort were excluded at this size because they took several seconds per trial.

---

## Experimental Setup

- Language: Java 17 (OpenJDK 17.0.9)
- Hardware: Intel Core i5-1135G7, 8 GB RAM, Windows 11
- Timing: `System.nanoTime()` before and after each sort call
- Warm-up: 5 discarded runs per algorithm before measurement
- Repetitions: 20 runs per (algorithm, input type, size) combination; arithmetic mean recorded

---

## Author

Mohammad Gayada  
West University of Timișoara  
mohamad.ghayada04@e-uvt.ro
