project(split_vertex)
cmake_minimum_required(VERSION 2.8)

file(GLOB SRC_LIST
    "*.cpp"
)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++1z  -pthread -pedantic-errors")

if(CMAKE_BUILD_TYPE MATCHES Debug)
  add_compile_options(-g)
  message("Debug build.")
elseif(CMAKE_BUILD_TYPE MATCHES Release)
  add_compile_options(-O3)
  message("Release build.")
else()
  message("Some other build type.")
endif()

if(NOT EXISTS "/usr/bin/clang++")
   message( FATAL_ERROR "not find clang++ compiler")
endif()

set(CMAKE_CXX_COMPILER "/usr/bin/clang++")

add_executable(${PROJECT_NAME} ${SRC_LIST} )
