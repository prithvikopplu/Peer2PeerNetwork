package Peer4;
import java.net.*;
import java.io.*;
import java.util.*;

import Peer1.ReadConfig;

public class Server
{
    public static void main(String args[]) throws Exception
    {
    	try{
        /*Upload thread where this peer connects to the server to get the initial chunks
    	from the initialchunks.txt
    	*/
       
        ArrayList<String>uploadFileList=null;
        uploadFileList = readChunkName.readFileName("src/Peer4/initialchunks.txt");
        String [] initialUploadChunks = new String[uploadFileList.size()];
        initialUploadChunks=uploadFileList.toArray(initialUploadChunks);
        String[] ports = ReadConfig.readport();
        int serverPort=Integer.parseInt(ports[0]);
        for(int i = 0; i < uploadFileList.size(); i++)
        {
        	/*Connecting on port 8000 which is assigned to the server(File Owner)
        	and asking for chunks as specified in initialchunks.txt*/
            new Client("127.0.0.1",serverPort,initialUploadChunks[i]);
        }


         //Download thread from which its peer downloads the file which it requires.
        ArrayList<String>chunkFileList=null;        
        chunkFileList = readChunkName.readFileName("src/Peer4/fileparts.txt");
        String [] chunkNames = new String[chunkFileList.size()];
        chunkNames=chunkFileList.toArray(chunkNames);
        String [] portsForPeer = new String[2];
//        String[] ports = ReadConfig.readport();
        portsForPeer=ports[4].split("\\s+");
        int peerPort = Integer.parseInt(portsForPeer[0]);
        int downloadPeerPort = Integer.parseInt(portsForPeer[1]);

        for(int i = 0; i < chunkFileList.size(); i++){
            new Client("127.0.0.1",downloadPeerPort,chunkNames[i]);
        }
        //Merge thread to merge all the file chunks received.
        new Merge();
        //Server socket listening        
        ServerSocket soc=new ServerSocket(peerPort);
        try{
        System.out.println("I am a peer acting as a server on " + peerPort+" and I am receiving files from peer with port at "+ downloadPeerPort);
        while(true)
        {
            new Handler(soc.accept()); 
        }
    }
        finally
    	{
		soc.close();
	}
    }
    	catch(Exception ex)
    	{
    		ex.getMessage();
    	}
  }
    
}

class Handler extends Thread
{
	 Socket clientConnection;
	 DataInputStream  dataIn;	//stream read from the socket
	 DataOutputStream  dataOut;    //stream write to the socket
    
    Handler(Socket soc)
    {
        try
        {
        	clientConnection=soc;                        
            dataIn=new DataInputStream(clientConnection.getInputStream());
            dataOut=new DataOutputStream(clientConnection.getOutputStream());
            //System.out.println("FTP Client Connected ...");
            start();
            
        }
        catch(Exception ex)
        {
        	ex.getMessage();
        }  
        //start();      
    }
    void transferFileToPeer() throws Exception
    {    
    	int val;
        String fileName=dataIn.readUTF();
        File f=new File(fileName);
        ArrayList<String>chunkSizeList=null;
        chunkSizeList=readChunkName.readFileName("src/Peer4/filesize.txt");
        String [] chunkSizeArray = new String[chunkSizeList.size()];
        chunkSizeArray=chunkSizeList.toArray(chunkSizeArray);
        while(true){
            f = new File(fileName);
            if(!f.exists())
            {
               //keeps checking for the file every 2 seconds.
                Thread.sleep(2000);
                System.out.println("The requested file"+fileName+" is not available at the peer yet.Waiting...");
            }

            if(f.exists()&&((int)f.length()==(Integer.parseInt(chunkSizeArray[0]))||((int)f.length()==Integer.parseInt(chunkSizeArray[1])))){
               
            	File file = new File("Peer4FileSplit/");
            	System.out.println("Downloading file:"+fileName);
            	System.out.print("Chunks available at the peer 5:");
            	for(String fileNames : file.list()) 
        		{
        			System.out.print(fileNames+",");
        		}
            	System.out.println("\n");break;
             }
        }
        dataOut.writeUTF("AllSet");
		   FileInputStream fileIn=new FileInputStream(f);
		   do
		   {
		   val=fileIn.read();
		   dataOut.writeUTF(String.valueOf(val));
		   }
		   while(val!=-1);
		   fileIn.close();
		   dataOut.writeUTF("File Sent");
    }
    

    public void run()
    {
       
            try
            {
            	transferFileToPeer();
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }
        //}
    }
}