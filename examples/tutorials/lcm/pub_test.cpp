#include <iostream>
#include "LcmPublish.h"
#include <lcm/lcm-cpp.hpp>
#include "picInfo/recordInfo.hpp"
#include <memory>

int main(int argc, char **argv)
{
    std::shared_ptr<LcmPublisher> publish_obj = std::make_shared<LcmPublisher>("Image");
    picInfo::recordInfo record;
    for(int i = 0 ; i < 10 ; ++i)
    {
        record.full_image_name = std::to_string(i) + ".jpg";
        publish_obj->publish(record);
    }

    return 1;
}
