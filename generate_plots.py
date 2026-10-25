import csv
import math
import os
import matplotlib.pyplot as plt

def load_data(filepath):
    data = []
    with open(filepath, 'r') as f:
        reader = csv.DictReader(f)
        for row in reader:
            data.append({
                'algorithm': row['algorithm'],
                'graph': row['graph'],
                'n': int(row['n']),
                'm': int(row['m']),
                'time_ms': float(row['time_ms']),
                'dsu_ops': int(row['dsu_ops']),
                'edge_scans': int(row['edge_scans']),
                'comparisons': int(row['comparisons']),
                'max_depth': int(row['max_depth']),
                'result': int(row['result'])
            })
    return data

def fit_slope(x_vals, y_vals):
    # Линейная регрессия на log-log данных: log(y) = slope * log(x) + intercept
    log_x = [math.log(x) for x in x_vals]
    log_y = [math.log(y) for y in y_vals]
    n = len(log_x)
    mean_x = sum(log_x) / n
    mean_y = sum(log_y) / n
    num = sum((log_x[i] - mean_x) * (log_y[i] - mean_y) for i in range(n))
    den = sum((log_x[i] - mean_x) ** 2 for i in range(n))
    return num / den

def main():
    os.makedirs('results/plots', exist_ok=True)
    data = load_data('results/results.csv')

    algorithms = ['Kruskal', 'Prim', 'Kosaraju', 'Kahn', 'DagShortestPath']
    densities = ['sparse', 'dense']

    # --- Plot 1: Operations vs n (Log-Log) ---
    plt.figure(figsize=(9, 6))
    for algo in algorithms:
        for dens in densities:
            pts = [d for d in data if d['algorithm'] == algo and d['graph'] == dens]
            pts.sort(key=lambda x: x['n'])
            x = [p['n'] for p in pts]
            # Kruskal: comparisons + dsu_ops + edge_scans, Prim: comparisons + edge_scans, others: edge_scans
            if algo == 'Kruskal':
                y = [p['comparisons'] + p['dsu_ops'] + p['edge_scans'] for p in pts]
            elif algo == 'Prim':
                y = [p['comparisons'] + p['edge_scans'] for p in pts]
            else:
                y = [p['edge_scans'] for p in pts]

            style = '-' if dens == 'sparse' else '--'
            plt.loglog(x, y, label=f"{algo} ({dens})", linestyle=style, marker='o')

    plt.title("Plot 1: Operations vs Number of Vertices (Log-Log)")
    plt.xlabel("Number of Vertices (n)")
    plt.ylabel("Total Measured Operations")
    plt.grid(True, which="both", ls=":", alpha=0.6)
    plt.legend(bbox_to_anchor=(1.04, 1), loc="upper left")
    plt.tight_layout()
    plt.savefig('results/plots/plot1_operations_vs_n.png', dpi=300)
    plt.close()
    print("[+] Plot 1 saved to results/plots/plot1_operations_vs_n.png")

    # --- Plot 2: Scans per Edge (edgeScans / m vs n) ---
    plt.figure(figsize=(9, 5))
    for algo in ['Kosaraju', 'Kahn', 'DagShortestPath']:
        for dens in densities:
            pts = [d for d in data if d['algorithm'] == algo and d['graph'] == dens]
            pts.sort(key=lambda x: x['n'])
            x = [p['n'] for p in pts]
            ratio = [p['edge_scans'] / p['m'] for p in pts]
            style = '-' if dens == 'sparse' else '--'
            plt.plot(x, ratio, label=f"{algo} ({dens})", linestyle=style, marker='s')

    plt.title("Plot 2: Scans per Edge (edgeScans / m vs n)")
    plt.xlabel("Number of Vertices (n)")
    plt.ylabel("edgeScans / m")
    plt.ylim(0, 2.5)
    plt.grid(True, ls=":", alpha=0.6)
    plt.legend()
    plt.tight_layout()
    plt.savefig('results/plots/plot2_scans_per_edge.png', dpi=300)
    plt.close()
    print("[+] Plot 2 saved to results/plots/plot2_scans_per_edge.png")

    # --- Plot 3: Share of edges Kruskal examines (edgeScans / m) ---
    plt.figure(figsize=(9, 5))
    for dens in densities:
        pts = [d for d in data if d['algorithm'] == 'Kruskal' and d['graph'] == dens]
        pts.sort(key=lambda x: x['n'])
        x = [p['n'] for p in pts]
        share = [p['edge_scans'] / p['m'] for p in pts]
        plt.plot(x, share, label=f"Kruskal ({dens})", marker='^', linewidth=2)

    plt.title("Plot 3: Share of Edges Kruskal Examines (edgeScans / m vs n)")
    plt.xlabel("Number of Vertices (n)")
    plt.ylabel("Share of Examined Edges")
    plt.ylim(0, 1.1)
    plt.grid(True, ls=":", alpha=0.6)
    plt.legend()
    plt.tight_layout()
    plt.savefig('results/plots/plot3_kruskal_share.png', dpi=300)
    plt.close()
    print("[+] Plot 3 saved to results/plots/plot3_kruskal_share.png")

    # --- Slope Calculation ---
    print("\n--- Slope Fit Results (log-log slope) ---")
    for algo in ['Kosaraju', 'Kahn', 'DagShortestPath']:
        for dens in densities:
            pts = [d for d in data if d['algorithm'] == algo and d['graph'] == dens]
            pts.sort(key=lambda x: x['n'])
            slope = fit_slope([p['n'] for p in pts], [p['edge_scans'] for p in pts])
            print(f"Slope of edgeScans for {algo} ({dens}): {slope:.2f}")

    for dens in densities:
        pts = [d for d in data if d['algorithm'] == 'Kruskal' and d['graph'] == dens]
        pts.sort(key=lambda x: x['n'])
        # Total operations for Kruskal (comparisons + edgeScans)
        ops = [p['comparisons'] + p['edge_scans'] for p in pts]
        slope = fit_slope([p['n'] for p in pts], ops)
        print(f"Slope of total operations for Kruskal ({dens}): {slope:.2f}")

if __name__ == '__main__':
    main()