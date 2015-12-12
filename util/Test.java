import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import container.Treap;
import NIOUtil.CopyDirectory;
import NIOUtil.CopyOption;


public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File sourceFile= new File("F:/Test/test.rmvb");
	//	File distinationFile1 = new File("F:/T1");
	//	File distinationFile2 = new File("F:/T2");
		/*long startTime1 = System.currentTimeMillis();
		try {
			CopyDirectory.copyDirectory(sourceFile, distinationFile1,CopyOption.REPLACE_EXISTING);
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
		 System.out.println(t2/1000);*/
		/*FileOutputStream out = new FileOutputStream(sourceFile,true); 
		FileInputStream inputstream = new FileInputStream(sourceFile);
		byte[] buffer = new byte[100];
		ByteBuffer bb = ByteBuffer.allocate(1024);
		FileChannel channel = inputstream.getChannel();
		channel.close();
		System.out.println("channel "+channel.position());
		int read=inputstream.read(buffer);
		System.out.println("channel "+channel.position()+" read "+read);*/
	//	int read1=channel.read(bb);
		//System.out.println("inputstream "+channel.position()+" read "+read1);
		 Treap<Integer> treap = new Treap<Integer>();
		 int[] test = {2,0,1};
		 for(int i=test.length-1;i>=0;i--){
			 treap.add(test[i]);
			 System.out.println(treap.contains(test[i]));
		 }
		
	}

}
