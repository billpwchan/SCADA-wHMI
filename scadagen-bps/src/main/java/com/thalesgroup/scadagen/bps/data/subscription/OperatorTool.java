package com.thalesgroup.scadagen.bps.data.subscription;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.notification.tools.operator.IOperator;
import com.thalesgroup.scadagen.bps.conf.common.And;
import com.thalesgroup.scadagen.bps.conf.common.Equals;
import com.thalesgroup.scadagen.bps.conf.common.In;
import com.thalesgroup.scadagen.bps.conf.common.Operator;


public final class OperatorTool {
	private static final Logger LOGGER = LoggerFactory.getLogger(OperatorTool.class);

	private static IOperator getOperator(Operator operator) {
		IOperator op;

		if ((operator instanceof Equals)) {
			Equals equals = (Equals) operator;
			op = new com.thalesgroup.hv.sdk.connector.notification.tools.operator.Equals(equals.getStatus(), equals.getValue());
			
		} else {
			if ((operator instanceof In)) {
				In in = (In) operator;
				op = new com.thalesgroup.hv.sdk.connector.notification.tools.operator.In(in.getStatus(), new HashSet<String>(in.getValue()));
			} else {
				if ((operator instanceof And)) {
					And and = (And) operator;

					op = new com.thalesgroup.hv.sdk.connector.notification.tools.operator.And(
							getOperator(and.getFirstOperand()), getOperator(and.getSecondOperand()));

				} else {
					LOGGER.warn("Cannot convert the criteria of type [{}].", operator.getClass().getName());
					op = null;
				}
			}
		}
		return op;
	}


	public static Set<IOperator> convert(Operator confCriteria)
			throws HypervisorException {
		Set<IOperator> toReturn = new HashSet<IOperator>();

		toReturn.add(getOperator(confCriteria));
		
		return toReturn;
		
	}
}
