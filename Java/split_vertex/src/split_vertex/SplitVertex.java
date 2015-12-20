package split_vertex;

import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

class SplitVertex {

    ArrayList<ArrayList<Integer>> vertex_neighbor;
    ArrayList<Integer> split_vertex;
    int subGraph;

    public void readData(InputStream in) {
        Scanner sc = new Scanner(in);

        int vertex = sc.nextInt();
        vertex_neighbor = new ArrayList<>(vertex);
        for (int i = 0; i < vertex; i++) {
            vertex_neighbor.add(new ArrayList<>());
        }

        int edge = sc.nextInt();
        for (; edge > 0; edge--) {
            int v1 = sc.nextInt(),
                    v2 = sc.nextInt();
            vertex_neighbor.get(v1).add(v2);
            vertex_neighbor.get(v2).add(v1);
        }

        ArrayList<Boolean> visited = new ArrayList<>(vertex);
        vertex_neighbor.stream().forEach((_item) -> {
            visited.add(false);
        });
        subGraph = check_split(visited);
    }

    public int size_split_vertex() {
        return split_vertex.size();
    }

    public List<Integer> get_split_vertex() {
        return Collections.unmodifiableList(split_vertex);
    }

    public void calculate() {
        int vertex_job_size = Math.max(vertex_neighbor.size() / 8, 8);
        ArrayList<Integer> local_split_vertex = new ArrayList<>();
        Semaphore change_split_vertex = new Semaphore(1);

        ArrayList<Thread> jobs = new ArrayList<>();
        for (int position = 0; position < vertex_neighbor.size();) {
            int to_job_preper = position + vertex_job_size;
            if (to_job_preper > vertex_neighbor.size()) {
                to_job_preper = vertex_neighbor.size();
            }
            final int to_job = to_job_preper;
            final int from = position;
            Runnable worker = () -> {
                for (int i = from; i < to_job; i++) {
                    ArrayList<Boolean> visited = new ArrayList<>(vertex_neighbor.size());
                    vertex_neighbor.stream().forEach((_item) -> {
                        visited.add(false);
                    });
                    visited.set(i, true);
                    if (check_split(visited) > subGraph) {
                        try {
                            change_split_vertex.acquire();
                            local_split_vertex.add(i);
                            change_split_vertex.release();
                        } catch (InterruptedException exeption) {
                            System.err.print(
                                    "SplitVertex::calculate() change_split_vertex.acquire()"
                                    + " InterruptedException: "
                                    + exeption.getLocalizedMessage());
                        }
                    }
                }
            };
            jobs.add(new Thread(worker));
            position = to_job;
        }

        jobs.forEach((job) -> {
            job.start();
        });

        try {
            for (Thread job : jobs) {
                job.join();
            }
        } catch (InterruptedException exeption) {
            System.err.print(
                    "SplitVertex::calculate() join InterruptedException: "
                    + exeption.getLocalizedMessage());
        }
        split_vertex = local_split_vertex;
    }

    void dfs(int i, List<Boolean> visited) {
        if (visited.get(i)) {
            return;
        }
        visited.set(i, true);
        vertex_neighbor.get(i).stream().forEach((v) -> {
            dfs(v, visited);
        });
    }

    int check_split(List<Boolean> visited) {
        int count = 0;
        for (int i = 0; i < vertex_neighbor.size(); i++) {
            if (!visited.get(i)) {
                count++;
                dfs(i, visited);
            }
        }
        return count;
    }
}