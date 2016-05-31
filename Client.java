package Peer1;
import java.net.*;
//import java.util.ArrayList;
import java.io.*;
public class Client extends Thread
{

    Socket clientSoc;
    DataInputStream dIn;
    DataOutputStream dOut;
    BufferedReader br;
    String fName = "";
    String ip = "";
    int port = 0;
    Socket sock = null;


    Client(String ipAddress, int portNumber,  String fileName )
    {


        ip = ipAddress;
        port = portNumber;
        fName = fileName;
        //calls the run method of specific client and hence assigns a new thread.
        start();      
    }


    public void run()
    {         
            boolean b1 = true;
            while(b1)
            {

            try
            {
            //try to look for a connection every 2 seconds.	
            Thread.sleep(2000);
            sock = new Socket(ip, port);
            b1 = false;
            System.out.println("Received connection from port: " + port +" file"+fName);
            }
            catch(Exception e)
            {
            	//trying once again to make a connection.
            	try {
                    sock = new Socket(ip,port);
                } 
                catch(Exception e1)
            	{
                	b1=true;
                	System.out.println("Trying to establish a connection to port: "+port+" for file"+fName);
            	}
            	 //e.printStackTrace();
                  b1 = true;
                  System.out.println("Trying to establish a connection to port: "+port+" for file"+fName);
            }                     
            }

        // Found a socket to connect to.
        try
        {
            clientSoc=sock;
            dIn=new DataInputStream(clientSoc.getInputStream());
            dOut=new DataOutputStream(clientSoc.getOutputStream());
            br=new BufferedReader(new InputStreamReader(System.in));
            ReceiveFile(fName);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }  
    }

    void ReceiveFile(String fileName) throws Exception
    {
        try{
        String fName = fileName;
//        ArrayList<String>chunkSizeList=null;
////        chunkSizeList=readChunkName.readFileName("src/Peer1/filesize.txt");
////        String [] chunkSizeArray = new String[chunkSizeList.size()];
////        chunkSizeArray=chunkSizeList.toArray(chunkSizeArray);
        if(port==8000)
        	{
            dOut.writeUTF("FileSplit/"+fName);
        	}
        	else
        	{
        		dOut.writeUTF("Peer2FileSplit/"+fName);	
        	}
            String msgFromServer=dIn.readUTF();
            //System.out.println(msgFromServer);
            if(msgFromServer.compareTo("AllSet")==0)
            {
                System.out.println("Receiving file "+fileName);
                File f=new File("Peer1FileSplit/"+fName);
                File parentDir = f.getParentFile();
  			  if(! parentDir.exists()) 
  			      parentDir.mkdirs();
                if(f.exists())
                {
                     dOut.flush();
                     System.out.println("Peer requested for file "+fName+" from DOwnloadPeer but it is already available.Rewrite Aborted.");
                    return;                      
                }
//                int sizeBuffer=50*1024;
//                byte[] a=new byte[sizeBuffer];
//                BufferedOutputStream fOut=new BufferedOutputStream(new FileOutputStream(f));
                FileOutputStream fOut=new FileOutputStream(f);
                int val;
                String temp;
                do
                {
                    temp=dIn.readUTF();
                    val=Integer.parseInt(temp);
                    if(val!=-1)
                    {
                        fOut.write(val);                    
                    }
                }while(val!=-1);
                fOut.close();
                int fromPeer = port - 8000;
                if(fromPeer != 0)
                {
                	System.out.println(fName+" received from Download Peer at port "+ fromPeer +".");
                }
                else
                {
                	//This is a file owner a.k.a server port because the server port is assigned to 8000.
                    System.out.println(fName+"  received from File Owner.");
                }                
        }
        }
        catch(Exception ex)
    	{
    		ex.getMessage();
    	}

    }

	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}

}
