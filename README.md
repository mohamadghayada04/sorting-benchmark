# Sorting Algorithms Benchmark (Java)

## 📌 Overview
This project presents an experimental comparison of multiple sorting algorithms implemented in Java.

The goal is to analyze when simple algorithms (like Insertion Sort) outperform more advanced ones (like Quick Sort or Merge Sort), especially on small or structured datasets.

---

## ⚙️ Algorithms Implemented

### Comparison-based:
- Bubble Sort
- Insertion Sort
- Selection Sort
- Cocktail Sort
- Gnome Sort
- Shell Sort
- Merge Sort
- Quick Sort
- Heap Sort

### Non-comparison:
- Counting Sort
- Radix Sort
- Bucket Sort

---

## 🧪 Test Types

Each algorithm was tested on:

- Random arrays
- Sorted arrays
- Reverse arrays
- Almost sorted arrays
- Half sorted arrays
- Few unique values

---

## 📊 Key Findings

- Insertion Sort performed best overall on small datasets
- Gnome Sort was extremely efficient on already sorted data
- Selection Sort showed consistent behavior across all tests
- Advanced algorithms introduced overhead for small input sizes

---

## 🚀 How to Run

Compile and run:

```bash
javac FullSortingBenchmark.java
java FullSortingBenchmark
