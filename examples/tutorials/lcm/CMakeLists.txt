cmake_minimum_required(VERSION 3.5.1)

project(lcm_test)
set(CMAKE_CXX_STANDARD 11)

include_directories( include . )

set(SRC 
        src/LcmPublish.cpp
        src/LcmSubscriber.cpp
    )

add_executable(pub_test pub_test.cpp ${SRC})
target_link_libraries(pub_test lcm)
add_executable(sub_test sub_test.cpp ${SRC})
target_link_libraries(sub_test lcm)
