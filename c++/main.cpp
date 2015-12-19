#include <iostream>
#include "splitVertex.h"
#include <iterator>
using namespace std;

int main() {
  SplitVertex split_vertex;
  cin >> split_vertex;
  split_vertex.calculate();

  cout << "size split vertex: " << split_vertex.size_split_vertex();
  cout << "\nsplit vertex: ";

  const auto &vec = split_vertex.get_split_vertex();
  std::copy(begin(vec), end(vec), ostream_iterator<int>(cout, ", "));
  cout << "\b\b \n";
}
