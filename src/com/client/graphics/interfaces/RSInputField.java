package com.client.graphics.interfaces;

public class RSInputField extends RSInterface {
	
	/**
	 * Determines if the input field is editable by the owner
	 */
	private final boolean editable;
	
	/**
	 * Creates a new input field
	 * @param identity
	 * 				the id of the input field
	 * @param characterLimit		
	 * 				the character limit, if any
	 * @param color					
	 * 				the color of the text
	 * @param text
	 * 				the default text in the field
	 * @param width					
	 * 				the default width of the field
	 * @param height				
	 * 				the height of the field
	 * @param asterisks				
	 * 				should be displayed in asterisks
	 * @param updatesEveryInput		
	 * 				should this update to the server ever input
	 * @param regex					
	 * 				does the field only take in certain information
	 * @param editable				
	 * 				should the field be editable
	 */
	public RSInputField(int identity, int characterLimit, int color, String text, int width, int height, boolean asterisks, boolean updatesEveryInput, String regex, boolean editable) {
		RSInterface.interfaceCache[identity] = this;
		super.id = identity;
		super.type = 16;
		super.atActionType = 8;
		super.message = text;
		super.width = width;
		super.height = height;
		super.characterLimit = characterLimit;
		super.textColor = color;
		super.displayAsterisks = asterisks;
		super.tooltips = new String[] {"Clear", "Edit"};
		super.defaultInputFieldText = text;
		super.updatesEveryInput = updatesEveryInput;
		super.inputRegex = regex;
		this.editable = editable;
	}
	
	/**
	 * Determines if the input field is editable
	 * @return	{@code true} if it can be edited
	 */
	public boolean isEditable() {
		return editable;
	}

}
