import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;

import NIOUtil.CopyDirectory;


public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File sourceFile= new File("F:/Test");
		File distinationFile1 = new File("F:/T1");
		File distinationFile2 = new File("F:/T2");
		long startTime1 = System.currentTimeMillis();
		try {
			CopyDirectory.copyDirectory(sourceFile, distinationFile1,32);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime1 = System.currentTimeMillis();
		
		long startTime2 = System.currentTimeMillis();
		Files.copy(sourceFile.toPath(),distinationFile2.toPath(), StandardCopyOption.REPLACE_EXISTING);
		long endTime2 = System.currentTimeMillis();
		 long t1 = endTime1 - startTime1;
		 long t2 = endTime2 - startTime2;
		 System.out.println(t1/1000);
		 System.out.println(t2/1000);
	}

}
