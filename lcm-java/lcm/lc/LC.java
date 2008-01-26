package lcm.lc;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.*;

/** Lightweight Communications implementation **/
public class LC
{
    static class SubscriptionRecord
    {
	String  regex;
	Pattern pat;
	LCSubscriber lcsub;
    }

    ArrayList<SubscriptionRecord> subscriptions = new ArrayList<SubscriptionRecord>();
    ArrayList<Provider> providers = new ArrayList<Provider>();

    HashMap<String,ArrayList<SubscriptionRecord>> subscriptionsMap = new HashMap<String,ArrayList<SubscriptionRecord>>();

    static LC singleton;

    public LC(Object... urls) throws IOException
    {
	if (urls.length==0)
	    urls = new String[] {"udpm://"};

	for (Object o : urls) {
	    String url = (String) o;

	    URLParser up = new URLParser(url);
	    String protocol = up.get("protocol");
	    
	    if (protocol.equals("udpm"))
		providers.add(new UDPMulticastProvider(this, url));
	    else
		System.out.println("LC: Unknown URL protocol: "+protocol);
	}
    }

    public static LC getSingleton()
    {
	if (singleton == null) {
	    try {
		singleton = new LC("udpm://");
	    } catch (Exception ex) {
		System.out.println("LC singleton fail: "+ex);
		System.exit(0);
		return null;
	    }
	}

	return singleton;
    }

    public void publish(String channel, String s) throws IOException
    {
	s=s+"\0";
	byte[] b = s.getBytes();
	publish(channel, b, 0, b.length);
    }

    public void publish(String channel, LCEncodable e)
    {
	try {
	    ByteArrayOutputStream bouts = new ByteArrayOutputStream(256);
	    DataOutputStream outs = new DataOutputStream(bouts);
	    
	    e.encode(outs);
	    
	    byte[] b = bouts.toByteArray();
	    
	    publish(channel, b, 0, b.length);
	} catch (IOException ex) {
	    System.out.println("LC publish fail: "+ex);
	}
    }

    public synchronized void publish(String channel, byte[] data, int offset, int length) 
	throws IOException
    {
	for (Provider p : providers) 
	    p.publish(channel, data, offset, length);
    }

    public void subscribe(String regex, LCSubscriber sub)
    {
	SubscriptionRecord srec = new SubscriptionRecord();
	srec.regex = regex;
	srec.pat = Pattern.compile(regex);
	srec.lcsub = sub;

	synchronized(subscriptions) {
	    subscriptions.add(srec);
	    
	    for (String channel : subscriptionsMap.keySet()) {
		if (srec.pat.matcher(channel).matches()) {
		    ArrayList<SubscriptionRecord> subs = subscriptionsMap.get(channel);
		    subs.add(srec);
		}
	    }
	}
    }

    public void receiveMessage(String channel, byte data[], int offset, int length)
    {
	synchronized (subscriptions) {
	    ArrayList<SubscriptionRecord> srecs = subscriptionsMap.get(channel);
	    
	    if (srecs == null) {
		// must build this list!
		srecs = new ArrayList<SubscriptionRecord>();
		subscriptionsMap.put(channel, srecs);
		
		for (SubscriptionRecord srec : subscriptions) {
		    if (srec.pat.matcher(channel).matches())
			srecs.add(srec);
		}
	    }
	    
	    for (SubscriptionRecord srec : srecs) {
		srec.lcsub.messageReceived(channel, 
					   new DataInputStream(new ByteArrayInputStream(data, 
											offset, 
											length)));
	    }
	}
    }
    
    public synchronized void subscribeAll(LCSubscriber sub)
    {
	subscribe(".*", sub);
    }

    ////////////////////////////////////////////////////////////////

    public static void main(String args[])
    {
	LC lc;

	try {
	    lc = new LC();
	} catch (IOException ex) {
	    System.out.println("ex: "+ex);
	    return;
	}

	lc.subscribeAll(new SimpleSubscriber());

	while (true) {				
	    try {
		Thread.sleep(1000);
		lc.publish("TEST", "foobar");
	    } catch (Exception ex) {
		System.out.println("ex: "+ex);
	    }
	}
    }

    static class SimpleSubscriber implements LCSubscriber
    {
	public void messageReceived(String channel, DataInputStream dins)
	{
	    System.out.println("RECV: "+channel);
	}
    }
}