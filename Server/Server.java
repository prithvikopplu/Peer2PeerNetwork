package Server;


/**
 * 
 */

/**
 * @author Prithvi
 *
 */
import java.net.*;
import java.io.*;
//import java.nio.*;
//import java.nio.channels.*;
//import java.util.*;
public class Server 
{
	private static final int serverPort = 8000;//The port on which the server is running.
	public static void main(String[] args) throws Exception
	{
		//Splitting the file using fileSplit function.
		fileSplit();
		System.out.println("The listening port is active."); 
    	ServerSocket listener = new ServerSocket(serverPort);
	//int clientNum = 1;
    	try {
        		while(true) {
        			//A thread is created for each client here.
            		//new Handler(listener.accept(),clientNum).start();
			//System.out.println("Client "  + clientNum + " is connected!");
        			new Handler(listener.accept());
			//clientNum++;
        			}
    	} finally {
        		listener.close();
    	} 
	}
	/**
 	One handler class for each client
 	*/
	private static class Handler extends Thread {
    	public Socket clientConnection;
    	public DataInputStream  dataIn;	//stream read from the socket
    	public DataOutputStream  dataOut;    //stream write to the socket
	//private int no;		//The index number of the client

    	public Handler(Socket connection) 
    	{
    		try{
            this.clientConnection = connection;
    		//this.no = no;
    		this.dataIn=new DataInputStream(clientConnection.getInputStream());
    		this.dataOut=new DataOutputStream(clientConnection.getOutputStream());
    		//Starts the thread 
    		start();
    		//System.out.println("Client "  + this.no + " is connected!");
    	}
    	catch(Exception ex)
    		{
    		System.out.println(ex.getMessage());
    		}
    	}	
    	public void transferFileToPeer()throws Exception
    	{
    		int val;
    		//reading the filename using readUTF
    		String fileName=dataIn.readUTF();
    		File f=new File(fileName);
    		// Checking if the file exists.
    		while (true)
    		{
    		if(!f.exists())
            {
                System.out.println("Searching for"+fileName+".Please wait...");
                Thread.sleep(2000);
            }

             if(f.exists()  ){
                break;
             }
    		}    		
    		   dataOut.writeUTF("AllSet");
//    		   int sizeBuffer=50*1024;
//    		   BufferedInputStream fileIn=new BufferedInputStream(new FileInputStream(f));
//    		   byte[] a=new byte[sizeBuffer];
    		   FileInputStream fileIn=new FileInputStream(f);
    		   do
    		   {
//    		   val=fileIn.read(a,0,sizeBuffer);
    			   val=fileIn.read();
    		   dataOut.writeUTF(String.valueOf(val));
    		   }
    		   while(val!=-1);
    		   fileIn.close();
    		   dataOut.writeUTF("File"+fileName+ "Received"); 		
    		
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
    	}
}
	//A method to split the file which in turn calls the class FileSplit.java.
	public  static void fileSplit() throws Exception
	{
		try
		{
		String fileToSplit;
		//prompting the user to enter the filename.
		File file = new File(".");
		for(String fileNames : file.list()) System.out.println(fileNames);
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Please enter the file name :");
		fileToSplit=br.readLine();
		//FileSplit f1=new FileSplit();
		//FileToTransfer is the location where the file is present.
		SplitFile.fileSplitter(new File("FileToTransfer/"+fileToSplit));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
}
