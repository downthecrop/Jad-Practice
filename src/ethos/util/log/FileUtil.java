package ethos.util.log;

import java.io.File;

/**
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class FileUtil {

	/**
	 * Normalizes the specified {@code path} to be accessible outside of a
	 * system confined scope (i.e an archive)
	 * 
	 * @param path The {@code String} representation of the path to be
	 *            normalized.
	 * @return The normalized path, as a {@link File}.
	 */
	public static File normalize(String path) {
		return new File(new File("."), path);
	}

	private FileUtil() {
		throw new UnsupportedOperationException();
	}

}