#include "splitVertex.h"
#include <assert.h>
#include <thread>
#include <mutex>
#include <algorithm>

using namespace std;

istream &operator>>(istream &input, SplitVertex &value) {
  int vertex;
  input >> vertex;
  value.vertex_neighbor.resize(vertex);

  int edge;
  input >> edge;
  for (;edge;--edge) {
    int v1, v2;
    input >> v1 >> v2;
    assert(v1 < value.vertex_neighbor.size() &&
           v2 < value.vertex_neighbor.size());
    value.vertex_neighbor[v1].emplace_back(v2);
    value.vertex_neighbor[v2].emplace_back(v1);
  }

  vector<bool> visited(vertex, false);
  value.subGraph = value.check_split(visited);
  return input;
}

int SplitVertex::size_split_vertex() { return split_vertex.size(); }

const vector<int> &SplitVertex::get_split_vertex() { return split_vertex; }

void SplitVertex::calculate() {
  int size = vertex_neighbor.size();
  int vertex_job_size = size / thread::hardware_concurrency();
  vertex_job_size = max(vertex_job_size, 8);

  decltype(split_vertex) local_split_vertex;
  mutex change_split_vertex;

  vector<thread> jobs;
  for (int from = 0; from < size;) {
    int to_job = from + vertex_job_size;
    if (to_job > size)
      to_job = size;
    jobs.emplace_back([&, from = from, to_job = to_job ]() {
      for (int i = from; i < to_job; i++) {
        vector<bool> visited(vertex_neighbor.size(), false);
        visited[i] = true;
        if (check_split(visited) > subGraph) {
          lock_guard<mutex> lock(change_split_vertex);
          local_split_vertex.emplace_back(i);
        }
      }
    });
    from = to_job;
  }
  for (auto &job : jobs)
    job.join();
  std::sort(begin(local_split_vertex),end(local_split_vertex));
  swap(split_vertex, local_split_vertex);
}
void SplitVertex::dfs(int i, vector<bool> &visited) {
  if (visited[i])
    return;

  visited[i] = true;
  for (const auto &v : vertex_neighbor[i])
    dfs(v, visited);
}

int SplitVertex::check_split(vector<bool> &visited) {
  int size = vertex_neighbor.size(), count{0};
  for (int i = 0; i < size; i++)
    if (!visited[i]) {
      count++;
      dfs(i, visited);
    }
  return count;
}
