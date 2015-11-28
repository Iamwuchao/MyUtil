package NIOUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;

/*
 * 文件复制
 * 将原文件复制到目的文件
 */
public class CopyFile {
	private FileChannel sourceFileChannel;
	private FileChannel distinationFileChannel;
	private RandomAccessFile fileInputStream;
	private RandomAccessFile fileOutputStream;
	private long fileLength;
	private int BUFFERSIZE=8;
	private static int MAX_BUFFER_SIZE = 20;//1M
	private static int BUFFER_SIZE = 20;//1M
	private static int ISBIGFILE = 128;//30M 为大文件
	
	/*
	 * sourceFile 源文件
	 * distinationFile 目标文件
	 * bufferSize 文件复制时缓存的大小 默认值为8M 
	 */
	private CopyFile(final File sourceFile,final File distinationFile,int bufferSize) throws Exception
	{
		if(bufferSize<=0||bufferSize>128)
		{
			throw new IllegalArgumentException("bufferSize incorrect");
		}
		if(sourceFile==null||distinationFile==null)
		{
			throw new NullPointerException();
		}
		if(sourceFile.isDirectory())
		{
			throw new Exception("file is a directory");
		}
		if(!sourceFile.exists())
		{
			throw new FileNotFoundException("source file not found");
		}
		if(distinationFile.exists())
		{
			throw new FileNotFoundException("distination file exists");
		}
		fileInputStream = new RandomAccessFile(sourceFile,"rws");
		sourceFileChannel = fileInputStream.getChannel();
		fileOutputStream = new RandomAccessFile(distinationFile,"rws");
		distinationFileChannel = fileOutputStream.getChannel();
		this.BUFFERSIZE=bufferSize;
	}
	
	public static void copyFile(final File sourceFile,final File distinationFile,int bufferSize) throws Exception
	{
		CopyFile cbc = new CopyFile(sourceFile,distinationFile,bufferSize);
		cbc.fileLength = sourceFile.length();
		long times=cbc.fileLength>>BUFFER_SIZE;
		FileLock lock = cbc.sourceFileChannel.lock();
		try{
			if(times<ISBIGFILE)
				cbc.copyNormalFile();
			else
				cbc.copyBigFile();
		}catch(IOException e){
				throw new IOException("copy file IO error");
		}finally{
			if(lock!=null)lock.release();
			if(cbc.sourceFileChannel!=null) cbc.sourceFileChannel.close();
			if(cbc.distinationFileChannel!=null) cbc.distinationFileChannel.close();
			if(cbc.fileInputStream!=null) cbc.fileInputStream.close();
			if(cbc.fileOutputStream!=null) cbc.fileInputStream.close();
			System.out.println("end lock release");
		}
	}
	
	public static void copyFile(final File sourceFile,final File distinationFile) throws Exception
	{
		copyFile(sourceFile,distinationFile,8);
	}
	
	private void copyBigFile() throws IOException
	{
		long position=0;
		int dataChunk = (1<<MAX_BUFFER_SIZE)*BUFFERSIZE;
		byte[] data = new byte[dataChunk];
		while((position+dataChunk)<this.fileLength)
		{
			MappedByteBuffer sourceMbb = this.sourceFileChannel.map(MapMode.READ_ONLY,position,dataChunk);
			MappedByteBuffer distinationMbb = this.distinationFileChannel.map(MapMode.READ_WRITE, position,dataChunk);
			sourceMbb.get(data);
			distinationMbb.put(data);
			position+=dataChunk;
			distinationMbb.force();
		}
		if(position<this.fileLength)
		{
			int size = (int) (this.fileLength-position);
			byte[] buffer = new byte[size];
			MappedByteBuffer sourceMbb = this.sourceFileChannel.map(MapMode.READ_ONLY,position,size);
			MappedByteBuffer distinationMbb = this.distinationFileChannel.map(MapMode.READ_WRITE, position,size);
			sourceMbb.get(buffer);
			distinationMbb.put(buffer);
			distinationMbb.force();
		}
	}
	
	private void copyNormalFile() throws IOException{
		this.distinationFileChannel.transferFrom(sourceFileChannel, 0, this.fileLength);
	}
	
}
