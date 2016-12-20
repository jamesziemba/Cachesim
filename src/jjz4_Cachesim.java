import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class jjz4_Cachesim {
	public static HashMap<String,ArrayList<String>> memory;
	public static Cache cache;
	

	public static void main(String[] args) throws IOException {
		memory = new HashMap<String,ArrayList<String>>();
		String tracefile = args[0];
		int cacheSize = Integer.parseInt(args[1]);
		int numWays = Integer.parseInt(args[2]);
		//System.out.println(numWays);
		int blockSize = Integer.parseInt(args[3]);
		int blockBits = (int) (Math.log(blockSize)/Math.log(2));
		start(cacheSize,numWays,blockSize,blockBits,tracefile);
		
	}
	public static void start(int cacheSize,int numWays, int blockSize,int blockBits, String tracefile) throws IOException{
		cache = new Cache(cacheSize, numWays,blockSize, blockBits, memory);
		BufferedReader inBuffer = new BufferedReader(new FileReader(tracefile));
		String line = inBuffer.readLine();
		while(line!=null){
			//System.out.println(line);
			processLine(line.split(" "), cacheSize, numWays, blockSize, blockBits);
			line = inBuffer.readLine();
		}
		inBuffer.close();
	}
	@SuppressWarnings("static-access")
	public static void processLine(String[] instruct, int cacheSize, int numWays, int blockSize, int blockBits){
		String address = instruct[1];
		int accessSize = Integer.parseInt(instruct[2]);
		String command = instruct[0];
		//System.out.println(instruct[0]);
		if(command.equals("store")){
			String value = instruct[3];
			cache.storeTo(address, cacheSize*1024,numWays, blockSize, blockBits, value, accessSize);
		}
		if(command.equals("load")){
			cache.loadFrom(address, cacheSize*1024,numWays, blockSize, blockBits, accessSize);
		}
		
	}
	

}
