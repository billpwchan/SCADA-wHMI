package com.thalesgroup.rtt.tools;

import java.util.ArrayList;
import java.util.Collection;

import com.thalesgroup.hv.core.tools.communication.notification.QueryTools;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.FromClauseType;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.QueryList;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.QueryType;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.SelectClauseType;
import com.thalesgroup.hv.ws.dialect.entity_content_v1.WhereClauseType;

public class QueryBuilder {
	
	public static QueryBuilder queryBuilder = null;
	
	private static EntityDataHelper dataHelper = new EntityDataHelper();
	
	private QueryBuilder() {};
	
	public static QueryBuilder getInstance() {
		if (queryBuilder == null) {
			queryBuilder = new QueryBuilder();
			return queryBuilder;
		} else {
			return queryBuilder;
		}
	}
	
	
	public QueryList buildQuery(String selectFields, String fromTopic, String type, String identifier) 
	throws Exception 
	{
		QueryList queryList = new QueryList();
		Collection<String> fieldList = new ArrayList<String>();
		SelectClauseType selectClause = null;
		WhereClauseType whereClause = null;
		FromClauseType fromClause = null;
		Collection<QueryType> queryTypeList = new ArrayList<QueryType>();
		
		if (!isValidEquipmentType(type)) {
			throw new Exception("Invalid equipment type option:" + type);
		} else {
		
		}
		
		fromClause = QueryTools.createFromClause(fromTopic);
		
		if (selectFields != null && !selectFields.isEmpty()) {
			fieldList = RttUtil.splitStringToList(selectFields, ",");
			if (isValidSelectFields(fieldList, type)) {
				selectClause = QueryTools.createSelectClause(fieldList);
			} else {
				throw new Exception("Invalid Select field(s)");
			}
		} else {
			selectClause = getAllSelectFields(type);
		}
		
		whereClause = createWhereEntityTypeAndIdCond(type, identifier);
		
		queryTypeList.add(QueryTools.createQuery(selectClause, fromClause, whereClause));
		
		queryList = QueryTools.createQueryList(queryTypeList);
		
		return queryList;
	}
	
	public SelectClauseType getSelectFields(String fields)
	{
		Collection<String> fieldList = RttUtil.splitStringToList(fields, ",");
		if (!fieldList.isEmpty() && fieldList != null ) {
			return QueryTools.createSelectClause(fieldList);
		}
		return null;
	}
	
	public SelectClauseType getAllSelectFields(String fromClause) throws EntityManipulationException
	{
		SelectClauseType selectClause = new SelectClauseType();
		selectClause = QueryTools.createSelectClause(dataHelper.getDeclaredStatusNames(fromClause));
		return selectClause;
	}
	
	
	public boolean isValidEquipmentType(String type) 
	{
		return dataHelper.getBeanEditor().getClassFromName(type) != null;
	}
	
	
	
	public boolean isValidSelectFields(Collection<String> fieldSet, String fromClause) throws EntityManipulationException
	{
		Collection<String> declaredFields = dataHelper.getDeclaredStatusNames(fromClause);
		for (String field : fieldSet) {
			if (!declaredFields.contains(field)) {
				return false;
			}
		}
		return true;
	}
	
	public WhereClauseType createWhereEntityTypeAndIdCond(String type, String identifier) {
		return QueryTools.createWhereClause(QueryTools.createAndOperator(QueryTools.createEqualOperator("type", type), 
				 QueryTools.createEqualOperator("id", identifier)));
	}
	
	
		
}