package stringReplacer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Nicholas McNew
 * @version 0.3
 */
public class FindAndReplaceString {
	private String destinationFilePath;
	private ArrayList<String> noTouchList = new ArrayList<String>();
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
			String replacementString) throws Exception {
		File f = new File(file);
		System.out.println("Unzipping: " + f);
		UnzipFile unzipped = new UnzipFile(f);

		destinationFilePath = unzipped.getDestination();
		File newZipDestination = new File(destinationFilePath.substring(0,
				destinationFilePath.length()) + "_New.zip");
		System.out.println("Fixing files in " + f);
		findAndReplace(replacerString, replacementString);
		System.out.println("Zipping file to " + newZipDestination);
		zipFile(destinationFilePath, newZipDestination);
		System.out.println("Deleting files from " + destinationFilePath);
		FileUtils.cleanDirectory(new File(destinationFilePath));
		FileUtils.deleteDirectory(new File(destinationFilePath));
	}

	public FindAndReplaceString(String file, String replacerString,
			String replacementString, String ignoreListLoc) throws Exception {
		File f = new File(file);
		System.out.println("Unzipping: " + f);
		UnzipFile unzipped = new UnzipFile(f);
		noTouchList = importNoTouch(new File(ignoreListLoc));
		noTouchList.add(replacementString);
		destinationFilePath = unzipped.getDestination();
		File newZipDestination = new File(destinationFilePath.substring(0,
				destinationFilePath.length()) + "_New.zip");
		System.out.println("Fixing files in " + f);
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
	 */
	public void findAndReplace(String findThis, String replaceWithThis)
			throws IOException {
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
			File currentPath = new File(myPaths.get(i).toString());
			File newFile = new File(removeFileFormat(currentPath.getPath()));
			try {

				String sCurrentLine;
				if (!newFile.exists()) {
					newFile.createNewFile();
				}
				br = new BufferedReader(new FileReader(currentPath));
				out = new PrintWriter(new BufferedWriter(
						new FileWriter(newFile)));
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
		String newString = "";
		if (currentString.contains(replacer)) {
			String[] myString = (noTouchList.isEmpty()) ? splitString(currentString, replacer,
					replacement) : splitStringAdv(currentString, replacer);
			newString = reformString(currentString, myString, replacer,
					replacement);
			return newString;
		}
		return currentString;
	}

	/**
	 * Remakes the string with the replacement string
	 * 
	 * @param string
	 *            Original string
	 * @param mySegs
	 *            The string segmented on the split word
	 * @param wordToSplitOn
	 *            string the original string was split on, used for testing
	 *            first and last words
	 * @param wordToReplace
	 *            string to be put in between segments
	 * @return a complete string with the words in the line
	 */
	public String reformString(String string, String[] mySegs,
			String wordToSplitOn, String wordToReplace) {
		String newLine = "";
		for(int i = 0; i < mySegs.length; i++){
			if(mySegs[i].equals(""))
				newLine += wordToReplace;
			else
				newLine += mySegs[i];
		}
		return newLine;
	}

	/**
	 * Splits strings intelligently, by avoiding the replacement string
	 * 
	 * @param toSplit
	 *            The String to be split into an array
	 * @param toSplitOn
	 *            The string that is being searched for
	 * @param toAvoid
	 *            the string that we don't want to replace used in cases when
	 *            there are items like "cat" and "cats"
	 * @return a string array split on the toSplitOn string
	 */
	public String[] splitString(String toSplit, String toSplitOn,
			String toAvoid) {
		ArrayList<String> newString = new ArrayList<String>();
			
				int i = 0;
				int j = 0;
				ArrayList<Integer> indices = new ArrayList<Integer>();
				while(i < toSplit.length() & i != -1){
					i = toSplit.indexOf(toSplitOn, i);
					j = toSplit.indexOf(toAvoid, j);
					if(i >= 0){
						if(i != j ){
							indices.add(i);
							i += toSplitOn.length();
						}
						else if(i == j){
							i = toSplit.indexOf(toSplitOn, toAvoid.length() + i);
						}
						}
				}
				if(indices.size() == 0){
					newString.add(toSplit);
				}
				for(int k = 0; k < indices.size(); k++){
					
					if(indices.size() == 1){
						if(indices.get(k) == 0){
							newString.add("");
							newString.add(toSplit.substring(toSplitOn.length()));
						}
						else if(toSplit.substring(indices.get(k), indices.get(k) + toSplitOn.length()).equals(toSplit.substring(toSplit.length()-toSplitOn.length()))){
							newString.add(toSplit.substring(0,indices.get(k)));
							newString.add("");
						}
						else
						{
							newString.add(toSplit.substring(0,indices.get(k)));
							newString.add("");
							newString.add(toSplit.substring(indices.get(k) + toSplitOn.length()));
							
						}
					}
					else if(k == 0){
						if(indices.get(k) == 0)
							newString.add("");
						else{
						newString.add(toSplit.substring(0,indices.get(k)));
						newString.add("");
							}
						}
					else if(k == indices.size()-1){
						newString.add(toSplit.substring(indices.get(k)-1));
					}
					else{
						newString.add(toSplit.substring(indices.get(k-1)+toSplitOn.length(),indices.get(k)));
						newString.add("");
					}
				}
			
		
		String[] arrayStrings = new String[newString.size()];
		for (int k = 0; k < newString.size(); k++)
			arrayStrings[k] = newString.get(k);
		return arrayStrings;

	}
	
	public ArrayList<Integer> stringHasStuff(String currentString){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(String j : noTouchList){
			int index = currentString.indexOf(j);
			while (index >= 0) {
				indices.add(index);
				index = currentString.indexOf(j, index + 1);
			}
		}
		return indices;
	}
	public String[] splitStringAdv(String toSplit, String toSplitOn){
		ArrayList<String> newString = new ArrayList<String>();
			int i = 0;
			ArrayList<Integer> j = stringHasStuff(toSplit);
			ArrayList<Integer> indices = new ArrayList<Integer>();
			while(i < toSplit.length() & i != -1){
				i = toSplit.indexOf(toSplitOn, i);
				if(i >= 0){
					if(!j.contains(i)){
						indices.add(i);
						i += toSplitOn.length();
					}
					else {
						i = toSplit.indexOf(toSplitOn, i + 1);
					}
					}
			}
			System.out.println(indices);
			if(indices.size() == 0){
				newString.add(toSplit);
			}
			for(int k = 0; k < indices.size(); k++){
				
				if(indices.size() == 1){
					if(indices.get(k) == 0){
						newString.add("");
						newString.add(toSplit.substring(toSplitOn.length()));
					}
					else if(toSplit.substring(indices.get(k), indices.get(k) + toSplitOn.length()).equals(toSplit.substring(toSplit.length()-toSplitOn.length()))){
						newString.add(toSplit.substring(0,indices.get(k)));
						newString.add("");
					}
					else
					{
						newString.add(toSplit.substring(0,indices.get(k)));
						newString.add("");
						newString.add(toSplit.substring(indices.get(k) + toSplitOn.length()));
						
					}
				}
				else if(k == 0){
					if(indices.get(k) == 0)
						newString.add("");
					else{
					newString.add(toSplit.substring(0,indices.get(k)));
					newString.add("");
						}
					}
				else if(k == indices.size()-1){
					newString.add(toSplit.substring(indices.get(k)-1));
				}
				else{
					newString.add(toSplit.substring(indices.get(k-1)+toSplitOn.length(),indices.get(k)));
					newString.add("");
				}
			}
		
			String[] arrayStrings = new String[newString.size()];
			for (int k = 0; k < newString.size(); k++)
				arrayStrings[k] = newString.get(k);
			return arrayStrings;
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
