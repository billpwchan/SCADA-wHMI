package com.thalesgroup.rtt.tasks;

import java.text.ParseException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.thalesgroup.hv.ws.notification_v1.ResourceUnknownFault;
import com.thalesgroup.hv.ws.notification_v1.UnableToDestroySubscriptionFault;
import com.thalesgroup.hv.ws.notification_v1.UnacceptableTerminationTimeFault;
import com.thalesgroup.hv.ws.notification_v1.xsd.RenewResponse;
import com.thalesgroup.hv.ws.notification_v1.xsd.UnsubscribeResponse;
import com.thalesgroup.rtt.registry.SubscriptionTracker;
import com.thalesgroup.rtt.tools.RttUtil;
import com.thalesgroup.rtt.ws.producers.WebServiceProducerConnector;

@Component
public class ListeningFutureTask {
	private static final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
	
	@Autowired
	private SubscriptionTracker tracker;
	
	@Autowired
	private WebServiceProducerConnector connector;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/*
	public void unsubscribe(final String subscriptionId) {
		ListenableFuture<UnsubscribeResponse> unsubscribe = executorService.submit(new Callable<UnsubscribeResponse>() {
			public UnsubscribeResponse call() throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
				return connector.unsubscribe(subscriptionId);
			}
		});
		
		Futures.addCallback(unsubscribe, new FutureCallback<UnsubscribeResponse>() {

			@Override
			public void onSuccess(UnsubscribeResponse response) {
				if (response != null) {
					//tracker.removeSubscription(subscriptionId);
					log.info("Subscription: " + subscriptionId + " removed from internal cache");
				}
			}
			@Override
			public void onFailure(Throwable t) {
				if (t instanceof ResourceUnknownFault) {
					log.error("Subscription id: " + subscriptionId + " no longer exist in connector");
					//tracker.removeSubscription(subscriptionId);
				}
			} 
			
		});
				
	}
	
	public void renew(final String subscriptionId) {
		ListenableFuture<RenewResponse> renew = executorService.submit(new Callable<RenewResponse>() {
			public RenewResponse call() throws UnacceptableTerminationTimeFault, ResourceUnknownFault, DatatypeConfigurationException, ParseException {
				return connector.renew(subscriptionId);
			}
		});
		
		Futures.addCallback(renew, new FutureCallback<RenewResponse>() {

			@Override
			public void onSuccess(RenewResponse response) {
				//tracker.addOrReplaceSubscription(subscriptionId, RttUtil.toUnixTime(response.getTerminationTime()));
				log.info("Subscription: " + subscriptionId + " renewed with termination time: "+ response.getTerminationTime());
			}

			@Override
			public void onFailure(Throwable t) {
				if (t instanceof UnacceptableTerminationTimeFault) {
					log.error("Unacceptable renewal termination time: " + t.getMessage());
				} else if (t instanceof ResourceUnknownFault) {
					log.error("Subscription no longer exists with connector: " + t.getMessage());
				} else if (t instanceof DatatypeConfigurationException) {
					log.error("Datatype error encountered with subsription renewal: " + t.getMessage());
				} else if (t instanceof ParseException) {
					log.error("Parse error encountered with subsription renewal: " + t.getMessage());
				}
				//tracker.removeSubscription(subscriptionId);
			}
			
		});
	}
	*/
}