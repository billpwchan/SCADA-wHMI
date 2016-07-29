package com.thalesgroup.scadagen.wrapper.wrapper.client.dpc;

public interface DCP_i {
	  /**@name Typedefs / defines*/
	  //@{
	  /**
	      \par Description:
	     enum corresponding to the possible values for the new working status to be given to an equipment (see <em>changeEqpStatus()</em> function).
	      \code
	      enum WorkingStatus { 
	         OUT_OF_SERVICE = 0,
	         CONTROL_INHIBITED,
	         IN_SERVICE,
	         ALARM_INHIBIT_EQP,
	         NO_ALARM_INHIBIT_EQP
	      }; \endcode
	      \par Values:
	      \li \c OUT_OF_SERVICE :  the working status of the equipment is &quot;<em>out of service</em>&quot; (full inhibited). All the monitoring and control functions are inhibited.
	      \li \c CONTROL_INHIBITED :  the working status of the equipment is &quot;<em>control inhibited</em>&quot;. Control functions are inhibited, but monitoring functions remains unaffected.
	      \li \c IN_SERVICE :  the working status of the equipment is &quot;<em>in service</em>&quot;. All the monitoring and control functions are normal.
	      \li \c ALARM_INHIBIT_EQP :  the working status of the equipment is &quot;<em>valid - alarm inhibit</em>&quot;. An alarm inhibition is set on the equipment. The monitoring and control functions are not changed.
	      \li \c NO_ALARM_INHIBIT_EQP :  the alarm inhibition is removed on the equipment. The monitoring and control functions are not changed.
	  */
	  enum WorkingStatus { 
	    OUT_OF_SERVICE(0),
	    CONTROL_INHIBITED(1),
	    IN_SERVICE(2),
	    ALARM_INHIBIT_EQP(3),
	    NO_ALARM_INHIBIT_EQP(4);
		  
		  private int value;
		  private WorkingStatus(int value) {
			  this.value = value;
		  }
		  public int getValue() { return this.value; }
	  }

	  /**
	      \par Description:
	     enum corresponding to the possible values for the new tagging status to be given to an equipment (see <em>changeEqpTag()</em> function).
	      \code
	      enum TaggingStatus { 
	         ALL_TAGGING = 0,
	         TAGGING_1,
	         TAGGING_2,
	         NO_TAGGING
	      }; \endcode
	      \par Values:
	      \li \c ALL_TAGGING :  the tagging status of the equipment is &quot;<em>Tag 1 and tag 2</em>&quot;.
	      \li \c TAGGING_1 :  the tagging status of the equipment is &quot;<em>Tag 1</em>&quot;.
	      \li \c TAGGING_2 :  the tagging status of the equipment is &quot;<em>Tag 2</em>&quot;.
	      \li \c NO_TAGGING :  the tagging status of the equipment is &quot;<em>Normal - no tagging</em>&quot;.
	  */
	  enum TaggingStatus { 
	    ALL_TAGGING(0),
	    TAGGING_1(1),
	    TAGGING_2(2),
	    NO_TAGGING(3);
		 
		  private int value;
		  private TaggingStatus(int value) {
			  this.value = value;
		  }
		  public int getValue() { return this.value; }
	  }

	  /**
	      \par Description:
	     enum corresponding to the possible values for the new validity status to be given to an internal input or output variable (see <em>changeVarStatus()</em> function).
	      \code
	      enum ValidityStatus { 
	         OPERATOR_INHIBIT = 0,
	         VALID,
	         ALARM_INHIBIT_VAR,
	         NO_ALARM_INHIBIT_VAR
	      }; \endcode
	      \par Values:
	      \li \c OPERATOR_INHIBIT :  the variable is invalided with state &quot;<em>Invalid - Variable inhibited</em>&quot;,
	      \li \c VALID :  this value suppress the states of invalidity &quot;<em>Invalid - Variable inhibited</em>&quot;.
	      \li \c ALARM_INHIBIT_VAR :  an alarm inhibition is set on the variable (the operator invalidation state is not changed),
	      \li \c NO_ALARM_INHIBIT_VAR :  the alarm inhibition is removed (the operator invalidation state is not changed).
	  */
	  enum ValidityStatus { 
	    OPERATOR_INHIBIT(0),
	    VALID(1),
	    ALARM_INHIBIT_VAR(2),
	    NO_ALARM_INHIBIT_VAR(3);

		  private int value;
		  private ValidityStatus(int value) {
			  this.value = value;
		  }
		  public int getValue() { return this.value; }
		  
//		  private static final Map<Integer, ValidityStatus> lookup = new HashMap<Integer, ValidityStatus>();
//		  static {
//			  for ( ValidityStatus v : EnumSet.allOf(ValidityStatus.class) ) 
//				  lookup.put(v.getValue(), v);
//		  }
//		  public static ValidityStatus get(int value) {
//			  return lookup.get(value);
//		  }
	  }

	  /**
	      \par Description:
	     enum corresponding to the possible values for the new forced status to be given to an internal input variable (see <em>changeVarForce()</em> functions).
	      \code
	      enum ForcedStatus { 
	         NOT_FORCED = 0,
	         FORCED
	      }; \endcode
	      \par Values:
	      \li \c NOT_FORCED :  the value of the internal input variable takes back the field value and the state &quot;<em>Valid - Forced value</em>&quot; is annulated for the status,
	      \li \c FORCED :  the value of the internal input variable is forced and the status is changed to &quot;<em>Valid - Forced value</em>&quot;, only if the variable is valid.
	  */
	  enum ForcedStatus { 
	    NOT_FORCED(0),
	    FORCED(1);
		  
		  private int value;
		  private ForcedStatus(int value) {
			  this.value = value;
		  }
		  public int getValue() { return this.value; }
	  }
	  //@}
}
