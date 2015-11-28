package NIOUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;

public class CopyDirectory {
	private FileChannel sourceFileChannel;
	private FileChannel distinationFileChannel;
	private File sourceFile;
	private File distinationFile;
	private CopyDirectory(final File sourceFile,final File distinationFile) throws Exception{
		if(sourceFile == null || distinationFile == null){
			throw new NullPointerException();
		}
		if(distinationFile.exists()){
			throw new Exception("distinationFile is exists");
		}
		if(!sourceFile.exists()){
			throw new FileNotFoundException("sourceFile not found");
		}
		this.sourceFile = sourceFile;
		this.distinationFile = distinationFile;
	}
	
	public static void copyDirectory(final File sourceFile,final File distinationFile) throws Exception{
		CopyDirectory cd = new CopyDirectory(sourceFile,distinationFile);
		
	}
	
}
