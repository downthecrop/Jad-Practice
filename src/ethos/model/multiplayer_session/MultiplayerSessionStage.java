package ethos.model.multiplayer_session;

import java.util.Objects;

public class MultiplayerSessionStage {
	/**
	 * Represents the start of a session where a player
	 */
	public static final int REQUEST = 1;
	/**
	 * Represents the stage that players offer items to the screen for trade
	 */
	public static final int OFFER_ITEMS = 2;

	/**
	 * Represents the stage that players must both confirm or decline the trade
	 */
	public static final int CONFIRM_DECISION = 3;

	/**
	 * Represents the stage where some interaction may take place in the session before finalization
	 */
	public static final int FURTHER_INTERATION = 4;

	/**
	 * Represents the final stage of the trade which lasts for only a short period of time.
	 */
	public static final int FINALIZE = 5;

	/**
	 * The stage of the trade
	 */
	private int stage;

	/**
	 * The attachment to the trade stage, this will more than likely be a player object that will be attatched when they have confirmed a certain stage.
	 */
	private Object attachment;

	/**
	 * Allows use to create a new trade stage with a default stage id.
	 * 
	 * @param stage the stage id
	 */
	public MultiplayerSessionStage(int stage) {
		this.stage = stage;
	}

	/**
	 * Allows use to create a new trade stage, with a default stage id, and an object as an attachment to the trade stage.
	 * 
	 * @param stage
	 * @param attachment
	 */
	public MultiplayerSessionStage(int stage, Object attachment) {
		this(stage);
		this.attachment = attachment;
	}

	/**
	 * Assigns an attachment to this stage object
	 * 
	 * @param attachment the attachment to be assigned
	 */
	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	/**
	 * Retrieves the attachment object to this class
	 * 
	 * @return the attachment
	 */
	public Object getAttachment() {
		return attachment;
	}

	/**
	 * Determines if the trade stage has an attachment
	 * 
	 * @return
	 */
	public boolean hasAttachment() {
		return Objects.nonNull(attachment);
	}

	/**
	 * Retrieves the stage of the trade in the form of an Integer
	 * 
	 * @return
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * Assigns the stage of the trade in the form of an Integer
	 * 
	 * @param stage the stage
	 */
	public void setStage(int stage) {
		this.stage = stage;
	}

}
