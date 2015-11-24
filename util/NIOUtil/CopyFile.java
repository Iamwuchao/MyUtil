package NIOUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;


public class CopyFile {
	private FileChannel sourceFileChannel;
	private FileChannel distinationFileChannel;
	private RandomAccessFile fileInputStream;
	private RandomAccessFile fileOutputStream;
	private long fileLength;
	private int BUFFERSIZE=8;
	private static int MAX_BUFFER_SIZE = 20;//1M
	private static int BUFFER_SIZE = 10;//1K
	private static int ISBIGFILE = 128;
	private CopyFile(File sourceFile,File distinationFile,int bufferSize) throws FileNotFoundException
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
			throw new FileNotFoundException("file is directory");
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
	}
	
	public static void copyFile(File sourceFile,File distinationFile,int bufferSize) throws IOException
	{
		CopyFile cbc = new CopyFile(sourceFile,distinationFile,bufferSize);
		cbc.fileLength = sourceFile.length();
		long times=cbc.fileLength>>BUFFER_SIZE;
		FileLock lock = cbc.sourceFileChannel.lock();
		try{
			if(times>ISBIGFILE)
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
	
	public static void copyFile(File sourceFile,File distinationFile) throws IOException
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
	/*	int read=0;
		ByteBuffer byteBuffer = ByteBuffer.allocate((1<<BUFFER_SIZE)*BUFFERSIZE);
		while(read>-1)
		{
			read = sourceFileChannel.read(byteBuffer);
			byteBuffer.flip();
			distinationFileChannel.write(byteBuffer);
			byteBuffer.flip();
		}
		distinationFileChannel.force(metaData);*/
		this.distinationFileChannel.transferFrom(sourceFileChannel, 0, this.fileLength);
	}
}
