package stringReplacer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 * @author Nicholas McNew
 * @version 1.0
 */
public class UnzipFile {
	private String myDestinationFilePath;

	/**
	 * Unzips file into a directory of the same name
	 * 
	 * @param f
	 *            Zip file to be extract
	 */
	public UnzipFile(File f) {
		try {
			String sourceFilePath = f.toString();
			myDestinationFilePath = sourceFilePath.substring(0,
					sourceFilePath.length() - 4) ;
			File temp = new File(myDestinationFilePath);
			temp.mkdir();

			ZipFile zipFile = new ZipFile(f);
			Enumeration<?> e = zipFile.entries();

			while (e.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) e.nextElement();
				File entryFilePath = new File(myDestinationFilePath,
						entry.getName());

				entryFilePath.getParentFile().mkdirs();

				// If the entry is directory, leave it. Otherwise extract
				// it.
				if (entry.isDirectory()) {
					entryFilePath.mkdirs();
					continue;
				} else {

					BufferedInputStream bis = new BufferedInputStream(
							zipFile.getInputStream(entry));
					int b;
					byte buffer[] = new byte[1024];

					FileOutputStream fos = new FileOutputStream(entryFilePath);
					BufferedOutputStream bos = new BufferedOutputStream(fos,
							1024);

					while ((b = bis.read(buffer, 0, 1024)) != -1) {
						bos.write(buffer, 0, b);
					}

					// Flush the output stream and close it.
					bos.flush();
					bos.close();

					// Close the input stream.
					bis.close();

				}
			}
		} catch (IOException ioException) {
			System.out.println("Input Output Exception: " + ioException);
		}
	}

	/**
	 * Grabs Current main directory
	 * 
	 * @return current main directory to work with
	 */
	public String getDestination() {
		return myDestinationFilePath;
	}

}
