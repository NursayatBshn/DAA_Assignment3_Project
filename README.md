# DAA Assignment 3: Graphs (DSU, MST, SCC, and Shortest Paths)

## Overview
This project provides a custom Java implementation of fundamental graph algorithms using Adjacency Lists. It includes:
* **Disjoint Set Union (DSU)** with union by rank and path compression.
* **Minimum Spanning Tree (MST):** Kruskal's and Prim's algorithms.
* **Strongly Connected Components (SCC):** Kosaraju's and Tarjan's algorithms (Iterative, Stack-safe up to 100,000 vertices).
* **Topological Sort:** Kahn's algorithm with cycle detection.
* **Shortest Paths:** O(V+E) relaxation in Directed Acyclic Graphs (DAGs) supporting negative weights.
* **Bonus:** Condensation Graph generation and Critical Path evaluation.

## Build and Test Instructions

Ensure you have **Java 17+** and **Maven 3.8+** installed.

### 1. Run Unit Tests (JUnit 5)
Executes all correctness tests, including cross-checks with Brute Force and Bellman-Ford, stack-safety limits, and 100 random graph agreements.
```bash
mvn test

```

### 2. Run the Benchmark

Generates deterministic graphs (using seed 42) and runs performance counting metrics across 5 sizes (up to 100,000 vertices) and 2 densities. Results are appended to `results/results.csv`.

```bash
mvn compile
java -Xmx4g -cp target/classes benchmark.BenchmarkRunner

```

### 3. Generate Plots

If Python 3 and `matplotlib` are installed, generate the complexity plots:

```bash
python3 generate_plots.py

```
