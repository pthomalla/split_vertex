#ifndef SPLIT_VERTEX_H
#define SPLIT_VERTEX_H

#include <vector>
#include <iostream>

class SplitVertex {
  std::vector<std::vector<int>> vertex_neighbor;
  std::vector<int> split_vertex;
  int subGraph;

  friend std::istream &operator>>(std::istream &input, SplitVertex &value);
  int check_split(std::vector<bool> &visited);
  void dfs(int i, std::vector<bool> &visited);

public:
  void calculate();
  int size_split_vertex();
  const std::vector<int> &get_split_vertex();
};

std::istream &operator>>(std::istream &input, SplitVertex &value);

#endif
