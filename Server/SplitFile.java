package Server;
import java.io.*;
public class SplitFile {
	public  static void fileSplitter(File f)throws FileNotFoundException, IOException
	{
		double noOfChunks;
		 int noOfChunksEachPeer;
		try
		{
		String fileName=f.getName();
		long lengthOfFile=f.length();
		int chunkSize=100*1024;	 
         //Each peer gets this #of chunks.
		noOfChunks=Math.ceil((double)(lengthOfFile)/(double)(chunkSize));
		if(noOfChunks%5!=0)
		{
			noOfChunksEachPeer=(int)(noOfChunks/5)+1;
		}
		else
		{
			noOfChunksEachPeer=(int)noOfChunks/5;
		}
        
		//Reads the data within the file in the form of bytes 
		BufferedInputStream in=new BufferedInputStream(new FileInputStream(f));
		int partNumber;
		for(partNumber=0; partNumber< Math.ceil((double)(lengthOfFile)/(double)(chunkSize))-1; partNumber++)
		{
			if(partNumber<noOfChunks)
			{
			//creates a file for each chunk
			String path="FileSplit"+"/"+fileName+"."+ String.format("%03d", partNumber) ;
			File newFile=new File(path);
			File parentDir = newFile.getParentFile();
			  if(! parentDir.exists()) 
			      parentDir.mkdirs(); 
            newFile.createNewFile();
			BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(newFile));
			//writing 100kb to each file
			for (int currentByte=0; currentByte <chunkSize; currentByte++)
			{
			out.write(in.read());	
			}
			out.close();
		}
		}	
		//for the last chunk which can be lesser than chunk size
		if(lengthOfFile!=chunkSize*(partNumber-1))
		{
			String path="FileSplit"+"/"+fileName+"."+ String.format("%03d", partNumber) ;
			File newFile=new File(path);
			File parentDir = newFile.getParentFile();
			  if(! parentDir.exists()) 
			      parentDir.mkdirs(); 
            newFile.createNewFile();
			BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(newFile));
			int bytesLeftOver;
			while((bytesLeftOver=in.read()) !=-1)
				out.write(bytesLeftOver);
			out.close();
		}
		//closing the input stream
		in.close();
	 //creating a text file 'FileSize.txt' with the size of the file,chunk size and number of chunks.
	 String pathFileSize="";
	 for(int i = 1; i < 6; i++ )
	 {
		 pathFileSize="src/Peer"+i+"/filesize.txt";
		 File fileSize=new File(pathFileSize);
		 File parentDir3 = fileSize.getParentFile();
		  if(! parentDir3.exists()) 
		      parentDir3.mkdirs();
		 if(fileSize.isFile()) {
			 fileSize.delete();}
//		 System.out.println("absolute path for " + fileSize + " is " +fileSize.getAbsolutePath());
//		 System.out.println("path for " + fileSize + " is " +fileSize.getPath());
//		 System.out.println("parent file for " + fileSize + " is " +fileSize.getParentFile());
//		 System.out.println("to path file for " + fileSize + " is " +fileSize.toPath());
		 fileSize.createNewFile();
         FileWriter fw = new FileWriter(fileSize, true);
         BufferedWriter bw = new BufferedWriter(fw);
         bw.write(chunkSize+"\n");
         bw.flush();
         bw.write((int)(lengthOfFile-((Math.floor((lengthOfFile/chunkSize)))*chunkSize))+"\n");
         bw.flush();
         bw.close();
         fw.close();
	 }
	 //creating a text file with all the chunks 'fileparts.txt' and initial chunk list 'initialchunks.txt' 
	 for(int i = 1; i < 6; i ++){
         File fileParts=new File("src/Peer"+i+"/fileparts.txt");
         File parentDir = fileParts.getParentFile();
		  if(! parentDir.exists()) 
		      parentDir.mkdirs();
         if(fileParts.isFile()) {
        	 fileParts.delete();}
         fileParts.createNewFile();
          
//         File networkconnection=new File("Peer"+i+"/neighbor.txt");
//         File parentDir1 = fileParts.getParentFile();
//         if(! parentDir1.exists()) 
//		      parentDir1.mkdirs();
//         if(networkconnection.isFile()) {
//        	 networkconnection.delete();}
//         networkconnection.createNewFile();
         
          File initialChunks=new File("src/Peer"+i+"/initialchunks.txt");
          File parentDir2 = fileParts.getParentFile();
          if(! parentDir2.exists()) 
		      parentDir2.mkdirs();
          if(initialChunks.isFile()) {
        	  initialChunks.delete();}
          initialChunks.createNewFile();

           FileWriter fw2 = new FileWriter(fileParts, true);
           BufferedWriter bw2 = new BufferedWriter(fw2);

         //inserting the contents into the created text files.
           for(int j = 0; j <Math.ceil((double)(lengthOfFile)/(double)(chunkSize)); j++)
           {
               String splitChunkName = fileName+"."+String.format("%03d", j);               
              bw2.write(splitChunkName+"\n");
              bw2.flush();
           }
           
               FileWriter fw3 = new FileWriter(initialChunks, true);
               BufferedWriter bw3 = new BufferedWriter(fw3);
             for(int j = (i - 1)*noOfChunksEachPeer; j < (i-1)*noOfChunksEachPeer+noOfChunksEachPeer; j++)
             {
            	 if(j<noOfChunks){
              String initialChunkName = fileName+"."+String.format("%03d", j);               
              bw3.write(initialChunkName+"\n");
              bw3.flush();
             }else{
            	 bw3.flush();
            	 }
             }
//             FileWriter fw4 = new FileWriter(networkconnection, true);
//             BufferedWriter bw4 = new BufferedWriter(fw4);
             File configFile=new File("src/Peer"+i+"/config.txt");
             File parentDir3 = fileParts.getParentFile();
             if(! parentDir3.exists()) 
   		      parentDir3.mkdirs();
             if(configFile.isFile()) {
            	 configFile.delete();}
             configFile.createNewFile();
             FileWriter fw5 = new FileWriter(configFile, true);
             BufferedWriter bw5 = new BufferedWriter(fw5);
             for(int j=0;j<6;j++)
             {
            	 if(j==0)
            	 {
            		 bw5.write("800"+j+"\n");
            		 bw5.flush();
            	 }
            	 else{	 
            		 if(j!=5)
            		 {
            	 String portNo1="800"+j;
            	 String portNo2="800"+(j+1);
            	 bw5.write(portNo1+"  "+portNo2+"\n");
            	 bw5.flush();
            	 }
            		 else
            		 {
            			 String portNo1="800"+j;
                    	 String portNo2="800"+(j-4);
                    	 bw5.write(portNo1+"  "+portNo2+"\n");
                    	 bw5.flush();
            		 }
            	 }
             }
             //assigning the neighbor for each peer in the text file neighbor.txt
//             for(int j = i; j <i+2; j ++)
//             {
//            	 if(j<=5)
//            	 {
//            	String portNo="800"+j;
//            	bw4.write(portNo+"\n");
//            	bw4.flush();
//            	 }
//            	 if(j>5)
//            	 {            		 
//            		 bw4.write("8001"+"\n");
//            		 bw4.flush();
//            	 }
//             }
               bw2.close();
               fw2.close();
               bw3.close();
               fw3.close();
//               bw4.close();
//               fw4.close();
               bw5.close();
               fw5.close();
       }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	 }

/*
    public static void main(String[] args) throws IOException {
    	fileSplitter(new File("FileToTransfer/fileToSplit.pdf"));
   }

*/

 }


