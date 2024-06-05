package m4;
import java.util.*;

public class m4 {
    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static class Graph {
        private final Map<Integer, List<Edge>> adjList = new HashMap<>();

        void addEdge(int from, int to, int weight) {
            adjList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, weight));
            adjList.computeIfAbsent(to, k -> new ArrayList<>());  // Ensure all nodes are in the adjList
        }

        List<Edge> getEdges(int node) {
            return adjList.getOrDefault(node, Collections.emptyList());
        }
    }

    public static List<Integer> dijkstra(Graph graph, int start, int end) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        for (int vertex : graph.adjList.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        pq.add(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentVertex = current[0];
            int currentDistance = current[1];

            if (currentDistance > distances.get(currentVertex)) continue;

            for (Edge edge : graph.getEdges(currentVertex)) {
                int newDist = currentDistance + edge.weight;
                if (newDist < distances.get(edge.to)) {
                    distances.put(edge.to, newDist);
                    previous.put(edge.to, currentVertex);
                    pq.add(new int[]{edge.to, newDist});
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        for (Integer at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (path.size() == 1 && !path.contains(start)) return Collections.emptyList();
        return path;
    }

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);

        Graph graph = new Graph();

        System.out.println("Введите количество ребер:");
        int edgeCount = scanner.nextInt();

        System.out.println("Введите ребра (формат: from to weight):");
        for (int i = 0; i < edgeCount; i++) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            int weight = scanner.nextInt();
            graph.addEdge(from, to, weight);
        }

        System.out.println("Введите начальную вершину:");
        int start = scanner.nextInt();

        System.out.println("Введите конечную вершину:");
        int end = scanner.nextInt();

        List<Integer> shortestPath = dijkstra(graph, start, end);

        if (shortestPath.isEmpty()) {
            System.out.println("Путь не найден.");
        } else {
            System.out.print("Кратчайший путь: ");
            for (int vertex : shortestPath) {
                System.out.print(vertex + " ");
            }
            System.out.println();

            int totalCost = 0;
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                int from = shortestPath.get(i);
                int to = shortestPath.get(i + 1);
                for (Edge edge : graph.getEdges(from)) {
                    if (edge.to == to) {
                        totalCost += edge.weight;
                        break;
                    }
                }
            }
            System.out.println("Суммарная стоимость пути: " + totalCost);
        }

        scanner.close();
    }
}
