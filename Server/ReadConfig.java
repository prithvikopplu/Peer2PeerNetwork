package Server;

import java.io.*;
public class ReadConfig {
 
	public static String[] readport() {
 			String [] ports=new String[2] ;
 			int i = 0;
		try (BufferedReader br = new BufferedReader(new FileReader("Peer4/config.txt")))
		{ 
			String currentLine;
 			while ((currentLine = br.readLine()) != null) {
				ports[i] = (currentLine);
				i++;
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		} 		
 		return ports;
	}
}
