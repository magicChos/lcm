#include "LcmSubscriber.h"

LcmSubscriber::~LcmSubscriber()
{
}


void LcmSubscriber::subscribe(const std::string topic_name)
{
    if (!lcm_.good())
    {
        return;
    }

    lcm_.subscribe(topic_name , &LcmSubscriber::handleMessage , this);
    while (0 == lcm_.handle())
    {
        continue;
    }
}
