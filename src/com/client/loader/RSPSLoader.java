package com.client.loader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Cody Reichenbach
 * 
 */
public final class RSPSLoader {

	private ClassLoader classLoader;
	private final Map<String, Class<?>> classMap;

	public RSPSLoader() throws Exception {
		classMap = new HashMap<>();
		String ss = "./osps.jar";
		final URL url = new File(ss).toURI().toURL();

		classLoader = new URLClassLoader(new URL[] { url });
	}

	public Class<?> getClass(final String name) {
		if (classMap.containsKey(name))
			return classMap.get(name);
		try {
			final Class<?> clazz = classLoader.loadClass(name);
			classMap.put(name, clazz);
			return clazz;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
