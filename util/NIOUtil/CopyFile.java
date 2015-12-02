package NIOUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;
import java.nio.file.FileAlreadyExistsException;

/*
 * 文件复制
 * 将原文件复制到目的文件
 */
public class CopyFile {
	private FileChannel sourceFileChannel;
	private FileChannel targetFileChannel;
	private RandomAccessFile fileInputStream;
	private RandomAccessFile fileOutputStream;
	private long fileLength;
	private int BUFFERSIZE=8;
	private static int MAX_BUFFER_SIZE = 20;//1M
	private static int BUFFER_SIZE = 20;//1M
	private static int ISBIGFILE = 128;//128M 为大文件
	
	/*
	 * sourceFile 源文件
	 * targetFile 目标文件
	 * bufferSize 文件复制时缓存的大小 默认值为8M 
	 */
	private CopyFile(final File sourceFile,final File targetFile,int bufferSize) throws Exception
	{
		if(bufferSize<=0||bufferSize>128)
		{
			throw new IllegalArgumentException("bufferSize incorrect");
		}
		if(sourceFile==null||targetFile==null)
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
		if(targetFile.exists())
		{
			System.out.println(targetFile.getAbsolutePath());
			throw new FileAlreadyExistsException("target file exists");
		}
		fileInputStream = new RandomAccessFile(sourceFile,"rws");
		sourceFileChannel = fileInputStream.getChannel();
		fileOutputStream = new RandomAccessFile(targetFile,"rws");
		targetFileChannel = fileOutputStream.getChannel();
		this.BUFFERSIZE=bufferSize;
	}
	
	public static void copyFile(final File sourceFile,final File targetFile,int bufferSize) throws Exception
	{
		CopyFile cbc = new CopyFile(sourceFile,targetFile,bufferSize);
		cbc.fileLength = sourceFile.length();
		long times=cbc.fileLength>>BUFFER_SIZE;
		FileLock lock = cbc.sourceFileChannel.lock();
		try{
			if(times<ISBIGFILE)
				cbc.copyNormalFile();
			else
				cbc.copyNormalFile();
		}catch(IOException e){
				throw new IOException("copy file IO error");
		}finally{
			if(lock!=null)lock.release();
			if(cbc.sourceFileChannel!=null) cbc.sourceFileChannel.close();
			if(cbc.targetFileChannel!=null) cbc.targetFileChannel.close();
			if(cbc.fileInputStream!=null) cbc.fileInputStream.close();
			if(cbc.fileOutputStream!=null) cbc.fileInputStream.close();
			System.out.println("end lock release");
		}
	}
	
	public static void copyFile(final File sourceFile,final File targetFile) throws Exception
	{
		copyFile(sourceFile,targetFile,8);
	}
	
	private void copyBigFile() throws IOException
	{
		long position=0;
		int dataChunk = (1<<MAX_BUFFER_SIZE)*BUFFERSIZE;
		byte[] data = new byte[dataChunk];
		while((position+dataChunk)<this.fileLength)
		{
			MappedByteBuffer sourceMbb = this.sourceFileChannel.map(MapMode.READ_ONLY,position,dataChunk);
			MappedByteBuffer targetMbb = this.targetFileChannel.map(MapMode.READ_WRITE, position,dataChunk);
			sourceMbb.get(data);
			targetMbb.put(data);
			position+=dataChunk;
			targetMbb.force();
		}
		if(position<this.fileLength)
		{
			int size = (int) (this.fileLength-position);
			byte[] buffer = new byte[size];
			MappedByteBuffer sourceMbb = this.sourceFileChannel.map(MapMode.READ_ONLY,position,size);
			MappedByteBuffer targetMbb = this.targetFileChannel.map(MapMode.READ_WRITE, position,size);
			sourceMbb.get(buffer);
			targetMbb.put(buffer);
			targetMbb.force();
		}
	}
	
	private void copyNormalFile() throws IOException{
		this.targetFileChannel.transferFrom(sourceFileChannel, 0, this.fileLength);
	}
	
}
