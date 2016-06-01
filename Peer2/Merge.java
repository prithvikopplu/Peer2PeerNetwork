package Peer2;

import java.io.*;
import java.util.ArrayList;

//import Peer2.Client; 
public class Merge extends Thread {

    Merge(){
        start();
    }

    private static void mergeFile(String[] filearray){
        try{
            File f[];
             int lengthofArray=filearray.length;
            f = new File[lengthofArray];
            for(int i = 0; i < lengthofArray; i++){
                 f[i] = new File("Peer2FileSplit/"+filearray[i]);
            }
           
            File fMerged = new File("Peer2MergedFile/mergedFile");
            File parentDir = fMerged.getParentFile();
			  if(! parentDir.exists()) 
			      parentDir.mkdirs();
            InputStream in[];
            in = new InputStream[lengthofArray];
            for(int i = 0; i < lengthofArray; i++){
                in[i] = new FileInputStream(f[i]);
            }
 
            OutputStream out = new FileOutputStream(fMerged,true);
 //to do merging stuff here
            byte[] buf = new byte[1024];
            int len;

            for(int i = 0; i < lengthofArray; i++){
                while ((len = in[i].read(buf)) > 0){
                out.write(buf, 0, len);
                }
                in[i].close();
            }

            out.close();
            System.out.println("File is merged and available for Peer2.");
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
  }

    public void run() {
    	try
    	{
    	ArrayList<String>fileList=null;
        ArrayList<String>chunkSizeList=null;
        //readChunkName readFileParts = new readChunkName();
        fileList = readChunkName.readFileName("src/Peer2/fileparts.txt");
        String [] fileArray = new String[fileList.size()];
        fileArray=fileList.toArray(fileArray);
        chunkSizeList=readChunkName.readFileName("src/Peer2/filesize.txt");
        String [] chunkSizeArray = new String[chunkSizeList.size()];
        chunkSizeArray=chunkSizeList.toArray(chunkSizeArray);
        int counter = 0;
        int flag = -1;
        while(true){
            counter = 0;
            for(int i = 0; i < fileList.size(); i++){
                File f=new File("Peer2FileSplit/"+fileArray[i]);
                if(!f.exists())
                {
               	 Thread.sleep(2000);
                }
//                int countToGetFromServer=0;                 
//                if(!f.exists())
//                {
//                	while(countToGetFromServer<20)
//                    {
//                	Thread.sleep(20000);
//                	countToGetFromServer++;
//                    }
//                	
//                }
//                
//                if(countToGetFromServer==20)
//                {
//                	new Client("127.0.0.1",8000,fileArray[i]);
//                	//String msgFromServer=Peer3.Client.this.dIn.readUTF();
//                	 f=new File("Peer3FileSplit/"+fileArray[i]);
//                	 if(!f.exists())
//                	 {
//                		 Thread.sleep(5000);
//                	 }
//                	 else
//                	 {
//                		 if(f.exists())
//                			 System.out.println("Taking file"+fileArray[i]+"from server because I has already waited for 40seconds");
//                    	 //counter++;
//                	 }
//                	 
//                }
               
                
                if(f.exists() ){
                	if((int)f.length() == Integer.parseInt(chunkSizeArray[0]) ||(int) f.length() == Integer.parseInt(chunkSizeArray[1])){
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
         System.out.println("The file has been transfered successfully to Peer2");
       }
    	catch(Exception ex)
    	{
    		ex.getMessage();
    	}
    }
    
}
 