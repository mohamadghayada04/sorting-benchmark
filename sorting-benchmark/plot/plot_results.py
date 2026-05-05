"""
plot_results.py

Reads results/timings.csv and generates performance_log.png,
the log-scale figure used in the paper.

Usage:
    pip install matplotlib pandas
    python plot_results.py

The output file is saved as performance_log.png in the same directory.
"""

import pandas as pd
import matplotlib.pyplot as plt
import os

# Load data
csv_path = os.path.join(os.path.dirname(__file__), "../results/timings.csv")
df = pd.read_csv(csv_path)

# Average across all input types for each (algorithm, size) pair
avg = df.groupby(["algorithm", "size"])["avgTimeSec"].mean().reset_index()

# Only include algorithms that have data at all three sizes
# (slow ones were skipped at n=10000)
sizes = [10, 1000, 10000]
algo_counts = avg.groupby("algorithm")["size"].count()
full_algos = algo_counts[algo_counts == 3].index.tolist()
partial_algos = algo_counts[algo_counts < 3].index.tolist()

fig, ax = plt.subplots(figsize=(12, 6))

colors = plt.cm.tab10.colors
markers = ["o", "s", "D", "^", "v", "P", "X", "*", "h", "+", "x", "|"]

all_algos = sorted(avg["algorithm"].unique())

for i, algo in enumerate(all_algos):
    subset = avg[avg["algorithm"] == algo].sort_values("size")
    linestyle = "-" if algo in full_algos else "--"
    ax.plot(
        subset["size"],
        subset["avgTimeSec"],
        marker=markers[i % len(markers)],
        label=algo,
        color=colors[i % len(colors)],
        linestyle=linestyle,
        linewidth=1.8,
        markersize=7,
    )

ax.set_xscale("log")
ax.set_yscale("log")
ax.set_xticks(sizes)
ax.get_xaxis().set_major_formatter(plt.ScalarFormatter())
ax.set_xlabel("Input size (n)", fontsize=12)
ax.set_ylabel("Average execution time (seconds)", fontsize=12)
ax.set_title("Sorting Algorithm Performance Comparison", fontsize=14)
ax.legend(loc="upper left", fontsize=9, ncol=2)
ax.grid(True, which="both", linestyle="--", linewidth=0.5, alpha=0.7)

note = "Dashed lines = algorithms excluded at n=10000 (O(n²) too slow)"
ax.annotate(note, xy=(0.01, 0.01), xycoords="axes fraction", fontsize=8, color="gray")

plt.tight_layout()
out_path = os.path.join(os.path.dirname(__file__), "performance_log.png")
plt.savefig(out_path, dpi=150)
print(f"Figure saved to {out_path}")
plt.show()
