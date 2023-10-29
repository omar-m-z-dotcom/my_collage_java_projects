package gateways;

public class GateWayFactory {
	/**
	 * dummy private constructor to hide the implicit public one
	 */
	private GateWayFactory() {
		// TODO Auto-generated constructor stub
	}

	public static GateWay generateGateWay(GateWayTypes type) {
		if (type == GateWayTypes.EMAIL_GATEWAY) {
			return new EmailGateWay();
		}
		if (type == GateWayTypes.SMS_GATEWAY) {
			return new SMSGateWay();
		}
		return null;
	}
}