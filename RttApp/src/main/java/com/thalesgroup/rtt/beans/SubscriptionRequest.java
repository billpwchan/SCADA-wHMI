package com.thalesgroup.rtt.beans;


public class SubscriptionRequest implements Comparable<SubscriptionRequest>{
	
	private String callerId;
	
	private int order;
	
	private String xaxisLabel;
	
	private String yaxisLabel1;
	
	private String yaxisLabel2;
	
	private String topic;
	
	private String topicExpression;
	
	private String type;
	
	private String hypervisorId;
	
	private String field;
	
	private String env;
	
	private String subscriptionId;
	
	private Long terminationTime;
	
	private String lastUpdateValue;
	
	private String lastUpdateTime;
	
	private String chartWidth;
	
	private String chartHeight;
	
	
	public SubscriptionRequest() {};
	
	public SubscriptionRequest(String callerId, String topic, String topicExpression, String type, String field, String hypervisorId, String env) 
	{
		this.callerId = callerId;
		this.topic = topic;
		this.topicExpression = topicExpression;
		this.type = type;
		this.field = field;
		this.hypervisorId = hypervisorId;
		this.env = env;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTopicExpression() {
		return topicExpression;
	}

	public void setTopicExpression(String topicExpression) {
		this.topicExpression = topicExpression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getHypervisorId() {
		return hypervisorId;
	}

	public void setHypervisorId(String hypervisorId) {
		this.hypervisorId = hypervisorId;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	
	public String getQueryKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(env);
		sb.append("+");
		sb.append(hypervisorId);
		sb.append("+");
		sb.append(field);
		return sb.toString();
	}
	
	public String getLink() {
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(env);
		sb.append("/");
		sb.append(subscriptionId);
		return sb.toString();
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Long getTerminationTime() {
		return terminationTime;
	}

	public void setTerminationTime(Long terminationTime) {
		this.terminationTime = terminationTime;
	}

	public String getXaxisLabel() {
		return xaxisLabel;
	}

	public void setXaxisLabel(String xaxisLabel) {
		this.xaxisLabel = xaxisLabel;
	}

	public String getYaxisLabel1() {
		return yaxisLabel1;
	}

	public void setYaxisLabel1(String yaxisLabel1) {
		this.yaxisLabel1 = yaxisLabel1;
	}

	public String getYaxisLabel2() {
		return yaxisLabel2;
	}

	public void setYaxisLabel2(String yaxisLabel2) {
		this.yaxisLabel2 = yaxisLabel2;
	}

	public String getLastUpdateValue() {
		return lastUpdateValue;
	}

	public void setLastUpdateValue(String lastUpdateValue) {
		this.lastUpdateValue = lastUpdateValue;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(SubscriptionRequest subReq) {
		return Integer.compare(this.getOrder(), subReq.getOrder());
	}

	public String getChartWidth() {
		return chartWidth;
	}

	public void setChartWidth(String chartWidth) {
		this.chartWidth = chartWidth;
	}

	public String getChartHeight() {
		return chartHeight;
	}

	public void setChartHeight(String chartHeight) {
		this.chartHeight = chartHeight;
	}
	
	
}