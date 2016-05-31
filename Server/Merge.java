package Server;

import java.io.*;
import java.io.FileInputStream;
import java.util.ArrayList;
 
public class Merge extends Thread {

    Merge(){
        start();
    }

    private static void mergeFile(String[] filearray){
        try{
            File f[];
             int lengthofArray=filearray.length;
            f = new File[lengthofArray];
            for(int i = 0; i < lengthofArray+1; i++){
                 f[i] = new File("FileSplit/"+filearray[i]);
            }
           
            File fMerged = new File("MergedFile/mergedFile.mp4");
            InputStream in[];
            in = new InputStream[lengthofArray];
            for(int i = 0; i < lengthofArray+1; i++){
                in[i] = new FileInputStream(f[i]);
            }
 
            OutputStream out = new FileOutputStream(fMerged,true);
 //to do merging stuff here
            byte[] buf = new byte[1024];
            int len;

            for(int i = 0; i < lengthofArray+1; i++){
                while ((len = in[i].read(buf)) > 0){
                out.write(buf, 0, len);
                }
                in[i].close();
            }

            out.close();
            System.out.println("File merged.");
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage() + " in the specified directory.");
        }
  }

    public void run() {
    	try
    	{
    	ArrayList<String>fileList=null;
        String [] fileArray = null;
        ArrayList<String>chunkSizeList=null;
        String [] chunkSizeArray = null;
        //readChunkName readFileParts = new readChunkName();
        fileList = readChunkName.readFileName("fileparts.txt");
        fileArray=fileList.toArray(fileArray);
        chunkSizeList=readChunkName.readFileName("filesize.txt");
        chunkSizeArray=chunkSizeList.toArray(chunkSizeArray);
        int counter = 0;
        int flag = -1;
        while(true){
            counter = 0;
            for(int i = 0; i < fileList.size(); i++){
                //System.out.println("b"+i);
                File f=new File("FileSplit/"+fileArray[i]);
                //System.out.println("e"+i);
                if(f.exists() ){
                    if(f.length() == chunkSizeArray[0].length() || f.length() == chunkSizeArray[1].length()){
                        counter++;
                    }
                    
                }
                if(counter == fileList.size()	)
                {
                    flag = 1;
                }

            }
            if(flag == 1){
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
         mergeFile(fileArray);
         System.out.println("The file has been transfered successfully");
       }
    	catch(Exception ex)
    	{
    		ex.getMessage();
    	}
    }
    
}
 