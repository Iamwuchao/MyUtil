package NIOUtil;

import java.io.File;
import java.io.FileNotFoundException;

public class CopyDirectory {
	private int bufferSize=8;//8M
	private CopyDirectory(final File sourceFile,final File targetFile,int bufferSize) throws Exception{
		if(sourceFile == null || targetFile == null){
			throw new NullPointerException();
		}
		if(!sourceFile.exists()){
			throw new FileNotFoundException("sourceFile not found");
		}
		if(!targetFile.exists()){
			if(!targetFile.mkdirs())
				throw new FileNotFoundException("create targetFile fail");
		}
		if(bufferSize<=0||bufferSize>128)
		{
			throw new IllegalArgumentException("bufferSize incorrect");
		}
		this.bufferSize = bufferSize;
	}
	
	private void DFSCopyDirectory(File sourceDirFile,File targetFile) {
		String targetCurrentDir = targetFile.getAbsolutePath().replace('\\','/');
		File[] files = sourceDirFile.listFiles();
		for(File temFile:files){
			File target = new File(targetCurrentDir+"/"+temFile.getName());
			if(temFile.isDirectory()){
				target.mkdirs();
				DFSCopyDirectory(temFile,target);
			}
			else if(temFile.isFile()){
				try{
					CopyFile.copyFile(temFile,target,bufferSize);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void copyDirectory(final File sourceDir,final File targetDir,int buffersize) throws Exception{
		CopyDirectory cd = new CopyDirectory(sourceDir,targetDir,buffersize);
		cd.DFSCopyDirectory(sourceDir, targetDir);
	}
	
}
