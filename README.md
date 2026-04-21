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
- Gnome Sort was very efficient on sorted data
- Selection Sort showed stable performance
- Advanced algorithms had more overhead for small inputs

---

## 🚀 How to Run

Compile and run:

javac FullSortingBenchmark.java  
java FullSortingBenchmark

---

## 🔗 Repository Purpose

This repository supports a research paper analyzing the practical performance of sorting algorithms under different conditions.

All experiments are reproducible using the provided code.

---

## 👤 Author

Mohammad Ghayada  
West University of Timișoara
