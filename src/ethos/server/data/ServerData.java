package ethos.server.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.io.FilenameUtils;

import ethos.model.content.BonusActivation;
import ethos.model.wogw.WellOfGoodWill;

/**
 * <p>
 * This class should be used to store data which may not be lost when the server is stopped.
 * </p>
 * 
 * <b>Expanding the class</b>
 * <p>
 * Loading and saving variables is done dynamically, meaning there is no need to hard-code read and write methods. All you have to do is add a class level variable and add a get
 * method to retrieve the Object from the Class and a set method (optional).
 * </p>
 * 
 * <b>Saving non-primitive data types</b>
 * <p>
 * Be careful when saving non-primitive data types. Saving and reading is done with Serialization so whenever you change a data type the serialized file may no longer be compatible
 * with the actual Object. If you change a data type it is advised to delete all .var files of that type and recreate them during runtime. Besides, make sure the data type is
 * serializeable, otherwise it will <u>not</u> be saved.
 * </p>
 * 
 * @author Snappie
 *
 */
public class ServerData{

	/**
	 * A list of String values that represents Internet protocol address that have received starters on
	 */
	private ArrayList<String> starters;

	/**
	 * The shortest time it took to complete zulrah
	 * 
	 * @param <String> the name of the player
	 * @param <Long> the time it took to complete
	 */
	private SerializablePair<String, Long> zulrahTime;

	private WellOfGoodWill wellOfGoodWill;

	private BonusActivation bonusActivation;

	/**
	 * Collection of all variables which need to be saved.
	 */
	private PriorityQueue<Field> updateQueue = new PriorityQueue<>();

	/**
	 * Create an instance of ServerData with all variables deserialized from .var files.
	 */
	public ServerData() {
		loadServerData();
	}

	/**
	 * Reads all .var files and deserializes them if possible.
	 */
	private void loadServerData() {
		initializeVariables();
		File[] fileList = new File("./Data/server/").listFiles();
		for (File varFile : fileList) {
			if (varFile.getName().endsWith(".var")) {
				try {
					setVariable(varFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Deserialize the specified file and attempt to set the class level variable with the file name to it.
	 * 
	 * @param varFile The serialized variable file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private <T> void setVariable(File varFile) throws ClassNotFoundException, IOException, IllegalArgumentException, IllegalAccessException {
		try {
			String varName = FilenameUtils.removeExtension(varFile.getName());
			Field field = ServerData.class.getDeclaredField(varName);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(varFile));
			T variable = (T) in.readObject();

			field.set(this, variable);
			in.close();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Create an instance of all variables to avoid Null Pointer Exceptions when trying to access variables which aren't loaded from a file.
	 */
	private void initializeVariables() {
		Field[] fields = ServerData.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				if (field.get(this) == null) {
					field.set(this, field.getType().newInstance());
				}
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calls the save method for all class level variables.
	 */
	public synchronized void saveServerData() {
		Field[] fields = ServerData.class.getDeclaredFields();
		for (Field field : fields) {
			saveField(field);
		}
	}

	/**
	 * Serialize and save a variable.
	 * 
	 * @param field The variable as a field.
	 */
	public synchronized void saveField(Field field) {
		if (field.getName().equals("updateQueue")) {
			return;
		}
		File tempFile = new File("./Data/server/" + field.getName() + ".tmp");
		tempFile.delete();
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));
			out.writeObject(field.get(this));
			out.close();
			new File("./Data/server/" + field.getName() + ".var").delete();
			tempFile.renameTo(new File("./Data/server/" + field.getName() + ".var"));
		} catch (NotSerializableException e) {
			e.printStackTrace();
			return;
		} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		tempFile.delete();
	}

	/**
	 * Issue a request to save the class level variable. The variable will be updated once {@link ServerData#saveQueue} is called.
	 * 
	 * @param varName The name of the variable.
	 */
	private void requestUpdate(String varName) {
		try {
			Field field = ServerData.class.getDeclaredField(varName);
			updateQueue.add(field);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves all variables which are stored in the {@link #updateQueue} and empties the queue.
	 */
	public void processQueue() {
		Field field;
		while ((field = updateQueue.poll()) != null) {
			saveField(field);
		}
	}

	/**
	 * Retrieves the best time it took a player to complete zulrah
	 * 
	 * @return the best time
	 */
	public SerializablePair<String, Long> getZulrahTime() {
		return zulrahTime;
	}

	/**
	 * Modifies the best time it took to beat zulrah
	 * 
	 * @param zulrahTime the new best time
	 */
	public void setSerializablePair(SerializablePair<String, Long> zulrahTime) {
		this.zulrahTime = zulrahTime;
		requestUpdate("zulrahTime");
	}

	public WellOfGoodWill getWellOfGoodWill() {
		return wellOfGoodWill;
	}

	public void setWellOfGoodWill(WellOfGoodWill wellOfGoodWill) {
		this.wellOfGoodWill = wellOfGoodWill;
		requestUpdate("wellOfGoodWill");
	}
	
	public BonusActivation getBonusActivation() {
		return bonusActivation;
	}

	public void setBonusActivation(BonusActivation bonusActivation) {
		this.bonusActivation = bonusActivation;
		requestUpdate("bonusActivation");
	}

	public List<String> getStarters() {
		return starters;
	}

	public void addStarter(String ip) {
		starters.add(ip);
		requestUpdate("starters");
	}

}
