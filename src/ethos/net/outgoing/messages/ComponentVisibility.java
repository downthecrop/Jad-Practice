package ethos.net.outgoing.messages;

import ethos.net.outgoing.PacketMessage;

/**
 * @author Jason MacKeigan
 * @date Nov 15, 2014, 3:14:01 AM
 */
public class ComponentVisibility implements PacketMessage<ComponentVisibility> {

	private int state;

	private int componentId;

	public ComponentVisibility(int state, int componentId) {
		this.state = state;
		this.componentId = componentId;
	}

	public int getState() {
		return state;
	}

	public int getComponentId() {
		return componentId;
	}

	@Override
	public boolean matches(ComponentVisibility message) {
		return message.componentId == componentId;
	}

	@Override
	public boolean requiresUpdate(ComponentVisibility message) {
		return message.state != state;
	}
}
