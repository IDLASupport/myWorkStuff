package stringReplacer;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * 
 * @author Nicholas McNew
 * @version 0.3
 */
public class FindAndReplaceString {
	private String destinationFilePath;
	private ArrayList<String> noTouchList = new ArrayList<String>();
	private String myStyleLoc;
	private HashMap<String, Boolean> myOptionsMap = new HashMap<String, Boolean>();

	/**
	 * Constructor to build a new FindAndReplacString Class runs different
	 * processes that extract, redo files, and recompress
	 * 
	 * @param file
	 *            the location of the zip that is to be fixed
	 * @param replacerString
	 *            string that needs to be replaced
	 * @param replacementString
	 *            string that replace the replacerString
	 */
	public FindAndReplaceString(String file, String replacerString,
			String replacementString, String ignoreListLoc, String zipCopyStyle)
			throws Exception {

		File f = new File(file);
		System.out.println("Unzipping: " + f);
		destinationFilePath = unzipFile(f);
		if (ControlsPanel.optionsMap.get("Ignore File List")) {
			noTouchList = importNoTouch(new File(ignoreListLoc));
			noTouchList.add(replacementString);
		}

		File newZipDestination = new File(destinationFilePath.substring(0,
				destinationFilePath.length()) + "_New.zip");
		System.out.println("Fixing files in " + f);
		if (ControlsPanel.optionsMap.get("copyStyle")) {
			myStyleLoc = unzipFile(new File(zipCopyStyle));
			fixCss();
			System.out.println("Deleting files from " + myStyleLoc);

			FileUtils.cleanDirectory(new File(myStyleLoc));
			FileUtils.deleteDirectory(new File(myStyleLoc));
		}
		findAndReplace(replacerString, replacementString);
		System.out.println("Zipping file to " + newZipDestination);
		zipFile(destinationFilePath, newZipDestination);

		System.out.println("Deleting files from " + destinationFilePath);
		FileUtils.cleanDirectory(new File(destinationFilePath));
		FileUtils.deleteDirectory(new File(destinationFilePath));
	}

	/**
	 * 
	 * @param txtFile
	 * @return
	 */
	public ArrayList<String> importNoTouch(File txtFile) {
		ArrayList<String> noTouchList = new ArrayList<String>();
		try {
			@SuppressWarnings("resource")
			Scanner in = new Scanner(txtFile);
			while (in.hasNextLine()) {
				noTouchList.add(in.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noTouchList;

	}

	public void fixCss() {
		ArrayList<Path> mySrcPath = new ArrayList<Path>();
		ArrayList<Path> myDestPath = new ArrayList<Path>();
		ArrayList<Path> blankPaths = new ArrayList<Path>();
		Path myPath = Paths.get(myStyleLoc);
		addFiles af = new addFiles();
		try {
			Files.walkFileTree(myPath, af);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mySrcPath = af.getCssAndPicturePath();
		af.setCssAndPicturePath(blankPaths);
		myPath = Paths.get(destinationFilePath);
		try {
			Files.walkFileTree(myPath, af);
		} catch (IOException e) {
			e.printStackTrace();
		}
		myDestPath = af.getCssAndPicturePath();
		System.out.println(mySrcPath);
		System.out.println(myDestPath);
		try {
			findAndReplaceFile("_print.css", mySrcPath, myDestPath);
			mySrcPath = walkCssFileTree(myStyleLoc);
			myDestPath = walkCssFileTree(destinationFilePath);

			findAndReplaceFile("mobile-style.css", mySrcPath, myDestPath);

			mySrcPath = walkCssFileTree(myStyleLoc);
			myDestPath = walkCssFileTree(destinationFilePath);
			findAndReplaceNamedCss(mySrcPath, myDestPath);

			mySrcPath = walkCssFileTree(myStyleLoc);
			myDestPath = walkCssFileTree(destinationFilePath);
			addAndReplaceImgs(mySrcPath, myDestPath);

			mySrcPath = walkCssFileTree(myStyleLoc);
			myDestPath = walkCssFileTree(destinationFilePath);

			replaceBannerCustom(af.getHtmlPaths(), myDestPath);

			mySrcPath = walkCssFileTree(myStyleLoc);
			myDestPath = walkCssFileTree(destinationFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Path> walkCssFileTree(String myLoc) {
		Path myPath = Paths.get(myLoc);
		addFiles af = new addFiles();
		try {
			Files.walkFileTree(myPath, af);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return af.getCssAndPicturePath();
	}

	public void findAndReplaceNamedCss(ArrayList<Path> mySrcPath,
			ArrayList<Path> myDestPath) throws IOException {
		File styleFile = new File(myStyleLoc);
		File srcCss = null;
		for (int i = 0; i < mySrcPath.size(); i++) {
			if (mySrcPath.get(i).toString()
					.contains(styleFile.getName().toString() + ".css")) {
				srcCss = (mySrcPath.get(i).toFile());
				System.out.println(srcCss);
			}
		}
		File destFile = new File(destinationFilePath);
		File destCss = null;
		for (int i = 0; i < myDestPath.size(); i++) {

			if (myDestPath.get(i).toString()
					.contains(destFile.getName().toString() + ".css")) {
				destCss = (myDestPath.get(i).toFile());
				System.out.println(destCss);
			}
		}
		if (srcCss != null && (srcCss.isFile() & destCss.isFile())) {
			FileUtils.copyFile(srcCss, destCss);
			replaceOldFile(destCss, srcCss);
		}
	}

	public void replaceBannerCustom(ArrayList<Path> htmlFiles,
			ArrayList<Path> myDestPath) {
		ArrayList<File> destCssHtml = new ArrayList<File>();
		for (Path k : htmlFiles) {
			destCssHtml.add(k.toFile());
		}
		File destCss = null;
		for (int i = 0; i < myDestPath.size(); i++) {
			if (myDestPath.get(i).toString().contains(".css")
					| myDestPath.get(i).toString().contains(".html")) {
				destCssHtml.add(myDestPath.get(i).toFile());
			}
		}
		System.out.println(destCssHtml);
		for (int i = 0; i < destCssHtml.size(); i++) {

			destCss = destCssHtml.get(i);
			File newFile = new File(removeFileFormat(destCssHtml.get(i)
					.toString()));
			BufferedReader br = null;
			try {

				String sCurrentLine;
				if (!newFile.exists()) {
					newFile.createNewFile();
				}
				br = new BufferedReader(new FileReader(destCss));
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(newFile)));
				while ((sCurrentLine = br.readLine()) != null) {
					String dumbyString = replaceString("banner_custom.gif",
							"banner_custom.png", sCurrentLine);
					dumbyString = replaceString("banner_custom.jpg",
							"banner_custom.png", dumbyString);					
					dumbyString += "\r\n";
					if(dumbyString.contains("role=\"banner\"") && br.readLine().contains("</div>")){
						dumbyString += "<img src=\"banner_custom.png\" alt=\"\" border=\"0\" class=\"banner\">" + "\r\n" +  "</div>"+ "\r\n";
					}
					out.write(dumbyString);
				}
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			replaceOldFile(destCss, newFile);
		}

	}

	public void addAndReplaceImgs(ArrayList<Path> mySrcPath,
			ArrayList<Path> myDestPath) throws IOException {
		File destFile = new File(destinationFilePath);
		File destPath = new File(destinationFilePath + "\\"
				+ destFile.getName().toString()  + "\\"
						+ destFile.getName().toString());

		for (int i = 0; i < mySrcPath.size(); i++) {
			if (!myDestPath.contains(mySrcPath.get(i).getFileName())
					& (mySrcPath.get(i).toString().contains(".png")
							| mySrcPath.get(i).toString().contains(".jpg") | mySrcPath
							.get(i).toString().contains(".gif"))) {
				FileUtils.copyFileToDirectory(mySrcPath.get(i).toFile(),
						destPath);

			}
		}
	}

	public void findAndReplaceFile(String finder, ArrayList<Path> mySrcPath,
			ArrayList<Path> myDestPath) throws IOException {
		File srcFile = new File("");
		File destFile = new File("");
		for (int i = 0; i < mySrcPath.size(); i++) {
			if (mySrcPath.get(i).toString().contains(finder)) {
				srcFile = mySrcPath.get(i).toFile();
			}
		}
		for (int i = 0; i < myDestPath.size(); i++) {
			if (myDestPath.get(i).toString().contains(finder)) {
				destFile = myDestPath.get(i).toFile();
			}
		}
		FileUtils.copyFile(srcFile, destFile);
	}

	/**
	 * Grabs each line in document and sends to replaceString after completed
	 * sends file to be replaced by replaceOldFile()
	 * 
	 * @param findThis
	 *            String that is found in document
	 * @param replaceWithThis
	 *            String that replaces the findThis String
	 * @throws IOException
	 *             when file is not found
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public void findAndReplace(String findThis, String replaceWithThis)
			throws IOException, ParserConfigurationException, SAXException,
			TransformerException {
		ArrayList<Path> myPaths = new ArrayList<Path>();
		Path myPath = Paths.get(destinationFilePath);

		addFiles af = new addFiles();
		try {
			Files.walkFileTree(myPath, af);
		} catch (IOException e) {
			e.printStackTrace();
		}
		myPaths = af.getFilePaths();
		BufferedReader br = null;
		PrintWriter out = null;
		for (int i = 0; i < myPaths.size(); i++) {
			if ((myPaths.get(i).toString()
					.substring(myPaths.get(i).toString().length() - 4)
					.equals(".xml"))) {

				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();

				Document doc = dBuilder.parse(myPaths.get(i).toFile());
				if (doc.hasChildNodes()) {

					xmlFinder(doc.getChildNodes(), findThis, replaceWithThis);

				}
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(myPaths.get(i)
						.toString());
				transformer.transform(source, result);
			} else {
				File currentPath = new File(myPaths.get(i).toString());
				File newFile = new File(removeFileFormat(currentPath.getPath()));
				try {

					String sCurrentLine;
					if (!newFile.exists()) {
						newFile.createNewFile();
					}
					br = new BufferedReader(new FileReader(currentPath));
					out = new PrintWriter(new BufferedWriter(new FileWriter(
							newFile)));
					while ((sCurrentLine = br.readLine()) != null) {
						String dumbyString = replaceString(findThis,
								replaceWithThis, sCurrentLine) + "\r\n";
						out.write(dumbyString);
					}
					out.close();

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (br != null) {
							br.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				replaceOldFile(currentPath, newFile);
			}
		}
	}

	/**
	 * Finds children of current node and sends data to be fixed by modifyXml
	 * 
	 * @param nodes
	 *            list of nodes that are children of the current node
	 * @param findThis
	 *            string to find and replace
	 * @param replaceWithThis
	 *            string to be put in place
	 */
	public void xmlFinder(NodeList nodes, String findThis,
			String replaceWithThis) {

		for (int count = 0; count < nodes.getLength(); count++) {
			Node tempNode = nodes.item(count);
			if (tempNode.getNodeType() == Node.ELEMENT_NODE
					& !tempNode.getTextContent().equals(""))
				tempNode.setNodeValue(replaceContent(tempNode.getTextContent(),
						findThis, replaceWithThis));
			if (tempNode.hasAttributes()) {

				// get attributes names and values
				NamedNodeMap nodeMap = tempNode.getAttributes();

				for (int i = 0; i < nodeMap.getLength(); i++) {

					Node node = nodeMap.item(i);
					node.setNodeValue(replaceContent(node.getTextContent(),
							findThis, replaceWithThis));

				}

			}
			if (tempNode.hasChildNodes()) {

				xmlFinder(tempNode.getChildNodes(), findThis, replaceWithThis);

			}

		}
	}

	/**
	 * Unzips a zip archive from the specified file
	 * 
	 * @param f
	 *            a file to unzip
	 * @return
	 */
	public String unzipFile(File f) {
		String myDestinationFilepath = "";
		try {
			String sourceFilePath = f.toString();
			myDestinationFilepath = sourceFilePath.substring(0,
					sourceFilePath.length() - 4);
			File temp = new File(myDestinationFilepath);
			temp.mkdir();

			ZipFile zipFile = new ZipFile(f);
			Enumeration<?> e = zipFile.entries();

			while (e.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) e.nextElement();
				File entryFilePath = new File(myDestinationFilepath,
						entry.getName());

				entryFilePath.getParentFile().mkdirs();

				// If the entry is directory, leave it. Otherwise extract
				// it.
				if (!entry.toString().contains("__MACOSX")) {
					if (entry.isDirectory()) {
						entryFilePath.mkdirs();
						continue;
					} else {

						BufferedInputStream bis = new BufferedInputStream(
								zipFile.getInputStream(entry));
						int b;
						byte buffer[] = new byte[1024];

						FileOutputStream fos = new FileOutputStream(
								entryFilePath);
						BufferedOutputStream bos = new BufferedOutputStream(
								fos, 1024);

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
			}
		} catch (IOException ioException) {
			System.out.println("Input Output Exception: " + ioException);
		}
		return myDestinationFilepath;
	}

	/**
	 * Zips file with same structure as original Directory
	 * 
	 * @param oldFilePath
	 *            original file directory
	 * @param zipFile
	 *            directory that points to new file
	 * @throws Exception
	 *             when new file cannot be found or incorrect permissons are
	 *             used
	 */
	@SuppressWarnings("resource")
	public void zipFile(String oldFilePath, File zipFile) throws Exception {
		File directory = new File(oldFilePath);
		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<File>();
		queue.push(directory);
		OutputStream out = new FileOutputStream(zipFile);
		Closeable res = out;
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (!kid.getName().equals("__MACOSX")) {
						if (kid.isDirectory()) {
							queue.push(kid);
							name = name.endsWith("/") ? name : name + "/";
							zout.putNextEntry(new ZipEntry(name));
						} else {
							zout.putNextEntry(new ZipEntry(name));
							copy(kid, zout);
							zout.closeEntry();
						}
					}
				}
			}
		} finally {
			res.close();
		}
	}

	/**
	 * Copies current streams
	 * 
	 * @param in
	 *            The current InputStream
	 * @param out
	 *            Current Output Stream
	 * @throws IOException
	 *             if can't access file
	 */
	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

	/**
	 * Copies files from directory into new zip, used in zipFile()
	 * 
	 * @param file
	 *            original file to be copied into zip
	 * @param out
	 *            Current output stream
	 * @throws IOException
	 */
	private static void copy(File file, OutputStream out) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			copy(in, out);
		} finally {
			in.close();
		}
	}

	/**
	 * 
	 * @param oldFile
	 *            Original file location to be deleted
	 * @param newFile
	 *            file to be renamed to oldfile for zip copy
	 */
	private void replaceOldFile(File oldFile, File newFile) {
		// Once everything is complete, delete old file..
		oldFile.delete();

		// And rename tmp file's name to old file name
		newFile.renameTo(oldFile);
	}

	/**
	 * Creates a string that will be used to write to new document
	 * 
	 * @param replacer
	 *            String to be split on
	 * @param replacement
	 *            String that is put in it's place
	 * @param currentString
	 *            Current line of the document
	 * @return String of current line to be written to new document
	 */
	public String replaceString(String replacer, String replacement,
			String currentString) {
		if (currentString.contains(replacer)) {
			return replaceContent(currentString, replacer, replacement);
		}
		return currentString;
	}

	public ArrayList<Integer> stringHasStuff(String currentString) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (String j : noTouchList) {
			int index = currentString.indexOf(j);
			while (index >= 0) {
				indices.add(index);
				index = currentString.indexOf(j, index + 1);
			}
		}
		return indices;
	}

	/**
	 * replaces the string of xml
	 * 
	 * @param toSplit
	 *            string to separate
	 * @param toSplitOn
	 *            string to separate toSplit
	 * @param toAvoid
	 *            string to replace and avoid in toSplit
	 * @return the final string
	 */
	private String replaceContent(String toSplit, String toSplitOn,
			String toAvoid) {
		ArrayList<String> newString = new ArrayList<String>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		if (noTouchList.isEmpty()) {
			int i = 0;
			int j = 0;
			while (i < toSplit.length() & i != -1) {
				i = toSplit.indexOf(toSplitOn, i);
				j = toSplit.indexOf(toAvoid, j);
				if (i >= 0) {
					if (i != j) {
						indices.add(i);
						i += toSplitOn.length();
					} else if (i == j) {
						i = toSplit.indexOf(toSplitOn, toAvoid.length() + i);
					}
				}
			}
		} else {

			int i = 0;
			ArrayList<Integer> j = stringHasStuff(toSplit);
			while (i < toSplit.length() & i != -1) {
				i = toSplit.indexOf(toSplitOn, i);
				if (i >= 0) {
					if (!j.contains(i)) {
						indices.add(i);
						i += toSplitOn.length();
					} else {
						i = toSplit.indexOf(toSplitOn, i + 1);
					}
				}
			}
		}
		if (indices.size() == 0) {
			newString.add(toSplit);
		}
		for (int k = 0; k < indices.size(); k++) {

			if (indices.size() == 1) {
				if (indices.get(k) == 0) {
					newString.add("");
					newString.add(toSplit.substring(toSplitOn.length()));
				} else if (toSplit.substring(indices.get(k),
						indices.get(k) + toSplitOn.length())
						.equals(toSplit.substring(toSplit.length()
								- toSplitOn.length()))) {
					newString.add(toSplit.substring(0, indices.get(k)));
					newString.add("");
				} else {
					newString.add(toSplit.substring(0, indices.get(k)));
					newString.add("");
					newString.add(toSplit.substring(indices.get(k)
							+ toSplitOn.length()));

				}
			} else if (k == 0) {
				if (indices.get(k) == 0)
					newString.add("");
				else {
					newString.add(toSplit.substring(0, indices.get(k)));
					newString.add("");
				}
			} else if (k == indices.size() - 1) {
				newString.add(toSplit.substring(indices.get(k) - 1));
			} else {
				newString
						.add(toSplit.substring(
								indices.get(k - 1) + toSplitOn.length(),
								indices.get(k)));
				newString.add("");
			}
		}
		String[] arrayStrings = new String[newString.size()];
		for (int k = 0; k < newString.size(); k++)
			arrayStrings[k] = newString.get(k);
		String newLine = "";
		for (int k = 0; k < arrayStrings.length; k++) {
			if (arrayStrings[k].equals(""))
				newLine += toAvoid;
			else
				newLine += arrayStrings[k];
		}
		return newLine;
	}

	/**
	 * Removes file extension and places a new ending on file so old document
	 * can be copied
	 * 
	 * @param fileName
	 *            File name to change for copying purposes
	 * @return filename of new document
	 */
	public String removeFileFormat(String fileName) {
		int periodLoc = fileName.lastIndexOf('.');
		String extension = fileName.substring(periodLoc);
		fileName = fileName.substring(0, periodLoc);
		return fileName + "_newFile" + extension;
	}

}
