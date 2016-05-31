package Peer3;
import java.io.*;
import java.util.*;
public class readChunkName 
{
	public static ArrayList<String> readFileName(String file)
	{
		ArrayList<String>files=new ArrayList<String>();	
		
	try (BufferedReader br = new BufferedReader(new FileReader(file)))
	{

		String CurrentLine;

		while ((CurrentLine = br.readLine()) != null)
		{
			files.add(CurrentLine);
		}
	}
	catch (IOException e)
	{
		e.printStackTrace();
	} 
		
		return files;
}

}
