#include "LcmPublish.h"

LcmPublisher::LcmPublisher(const std::string topic_name) : topic_name_(topic_name)
{
}
LcmPublisher::~LcmPublisher()
{
}

int LcmPublisher::publish(picInfo::recordInfo &recordInfo)
{
    if (!lcm_.good())
    {
        return 0;
    }
    int status = lcm_.publish(topic_name_, &recordInfo);

    return status;
}