package ethos;

import ethos.model.content.wogw.Wogw;

/**
 * A thread which will be started when the server is being shut down. Although in most cases the Thread will be started, it cannot be guaranteed.
 * 
 * @author Emiel
 *
 */
public class ShutdownHook extends Thread {

	public void run() {
		System.out.println("Successfully executed ShutdownHook");
		Wogw.save();
	}
}
