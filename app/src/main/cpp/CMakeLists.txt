cmake_minimum_required(VERSION 3.4.1)

add_library(
        fusion
        SHARED
        fusion.cpp)
find_library(
        fusion
        log)
target_link_libraries(
        fusion
        ${log-lib})


add_library(
        movingAverageFilter
        SHARED
        movingAverageFilter.cpp)
find_library(
        movingAverageFilter
        log)
target_link_libraries(
        movingAverageFilter
        ${log-lib})

add_library(
        trilateration
        SHARED
        trilateration.cpp)
find_library(
        trilateration
        log)
target_link_libraries(
        trilateration
        ${log-lib})
