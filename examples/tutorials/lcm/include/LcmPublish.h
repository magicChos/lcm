#pragma once

#include <lcm/lcm-cpp.hpp>
#include <iostream>
#include <unistd.h>
#include "picInfo/recordInfo.hpp"

class LcmPublisher
{
public:
    LcmPublisher(const std::string topic_name);
    ~LcmPublisher();
    int publish(picInfo::recordInfo &recordInfo);

private:
    std::string topic_name_;
    lcm::LCM lcm_;
};




