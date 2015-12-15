import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import container.Treap;
import NIOUtil.CopyDirectory;
import NIOUtil.CopyOption;
import NIOUtil.WalkFileTree;


public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File sourceFile= new File("F:/Test");
		Queue<File> queue = WalkFileTree.visitFileTree(sourceFile,true);
		for(File file : queue)
		{
			System.out.println(file.getName());
		}
	}

}
