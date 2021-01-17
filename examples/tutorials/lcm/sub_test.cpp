#include <lcm/lcm-cpp.hpp>
#include "picInfo/recordInfo.hpp"
#include <iostream>
#include <memory>
#include "LcmSubscriber.h"

int main(int argc, char **argv)
{
    std::shared_ptr<LcmSubscriber> subscriber_obj = std::make_shared<LcmSubscriber>();

    subscriber_obj->subscribe("Image");
    return 1;
}