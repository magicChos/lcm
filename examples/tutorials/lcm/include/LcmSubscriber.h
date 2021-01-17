#pragma once

#include <lcm/lcm-cpp.hpp>
#include <iostream>
#include "picInfo/recordInfo.hpp"

class LcmSubscriber
{
public:
    LcmSubscriber() = default;
    ~LcmSubscriber();

    void handleMessage(const lcm::ReceiveBuffer *rbuf, const std::string &chan, const picInfo::recordInfo *msg)
    {
        std::cout << chan << ": " << msg->full_image_name << std::endl;
    }

public:
    void subscribe(const std::string topic_name);

private:
    lcm::LCM lcm_;
    std::string full_name = "";
};
