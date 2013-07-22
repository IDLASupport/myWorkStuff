package stringReplacer;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 
 * @author Nicholas McNew
 * @version 1.0
 */
public class addFiles extends SimpleFileVisitor<Path> {
	ArrayList<Path> myFilePaths = new ArrayList<Path>();
	ArrayList<Path> myCssAndPicturePaths = new ArrayList<Path>();
	/**
	 * Creates a file visitor for tree walker and adds files to ArrayList to
	 * find and fix later
	 * 
	 * @param file
	 *            current file being visited by tree walker
	 * @param attr
	 *            File attributes to help determine type of file
	 * @return a continue to say to the filewalker to restart
	 */
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		if (attr.isRegularFile()) {
			HashMap<String, Boolean> myMap = ControlsPanel.optionsMap;
			if (file.toString().contains(".txt") & myMap.get("txt"))
				myFilePaths.add(file);
			if (file.toString().contains(".html") & myMap.get("html"))
				myFilePaths.add(file);

			if (file.toString().substring(file.toString().length()-4).contains(".xml") & myMap.get("xml"))
				myFilePaths.add(file);
			if((file.toString().contains("extra-img")| file.toString().contains("footer_custom")) & (file.toString().contains(".png") | file.toString().contains(".jpg") | file.toString().contains(".gif")) & myMap.get("convertCss")){
				myCssAndPicturePaths.add(file);
			}
			if(file.toString().contains(".css") & myMap.get("convertCss")){
				myCssAndPicturePaths.add(file);
			}

		}

		return CONTINUE;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Path> getFilePaths() {
		return myFilePaths;
	}
	public ArrayList<Path> getCssAndPicturePath(){
		return myCssAndPicturePaths;
	}
	public void setCssAndPicturePath(ArrayList<Path> newPath){
		myCssAndPicturePaths = newPath;
	}
	// If there is some error accessing
	// the file, let the user know.
	// If you don't override this method
	// and an error occurs, an IOException
	// is thrown.
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return CONTINUE;
	}
}