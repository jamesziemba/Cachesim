import java.util.ArrayList;
import java.util.HashMap;


public class Cache {
	static ArrayList<Set> Sets;
	static int numSets;
	public Cache(int cacheSize, int numWays, int blockSize, int blockBits, HashMap<String, ArrayList<String>> memory){
		Sets = new ArrayList<Set>();
		numSets = (cacheSize*1024)/(numWays*blockSize);
		//System.out.println(numSets);
		for(int i = 0;i<numSets;i++){
			Sets.add(new Set(cacheSize,blockSize,numWays, memory));
		}
		
			Sets.add(new Set(cacheSize,blockSize,numWays, memory));
		
	}
	public static void storeTo(String address, int cacheSize, int numWays, int blockSize, int blockBits, String value, int accessSize){
		//System.out.println("HERE");
		int setNum = getSetNum(address, cacheSize, numWays, blockSize, blockBits);
		//System.out.println(setNum);
		if(Sets ==null){
			
		}
		
		Set num = Sets.get(setNum);
		String tag = getTag(address, cacheSize, numWays, blockSize, blockBits);
		String offset = getOffset(address, cacheSize, numWays, blockSize, blockBits);
		String addr = getAddr(address, cacheSize, numWays, blockSize, blockBits);
		num.storeTo(blockSize,tag, value, accessSize, offset, addr, address);
	}
	public static void loadFrom(String address, int cacheSize, int numWays, int blockSize, int blockBits, int accessSize){
		//System.out.println("HERE");
		int setNum = getSetNum(address, cacheSize, numWays, blockSize, blockBits);
		//System.out.println(setNum);
		Set num = Sets.get(setNum);
		String tag = getTag(address, cacheSize, numWays, blockSize, blockBits);
		String offset = getOffset(address, cacheSize, numWays, blockSize, blockBits);
		String addr = getAddr(address, cacheSize, numWays, blockSize, blockBits);
		num.loadFrom(blockSize,tag, accessSize, offset, addr, address);
	}
	public static int getSetNum(String address, int cacheSize, int numWays, int blockSize, int blockBits){
		int decimal = Integer.parseInt(address.substring(2),16);
		String binary = Integer.toBinaryString(decimal);
		if(binary.length()<24){
			int missing = 24-binary.length();
			StringBuilder changed = new StringBuilder();
			for(int i = 0;i<missing;i++){
				changed.append("0");
			}
			changed.append(binary);
			binary = changed.toString();
		}
		String offset = binary.substring(24-blockBits);
		int indexEnd = 24-blockBits;
		int numSets = cacheSize/(blockSize*numWays);
		int bitsForIndex = (int) (Math.log(numSets)/Math.log(2));
		int indexBeg = indexEnd - bitsForIndex;
		String index = binary.substring(indexBeg, indexEnd);
		int retval =0;
		if(index.equals("")){
			retval =0;
		}
		else{
			retval = Integer.parseInt(index, 2);
		}
		
		return retval;

	}

	public static String getTag(String address, int cacheSize, int numWays, int blockSize, int blockBits){
		int decimal = Integer.parseInt(address.substring(2),16);
		String binary = Integer.toBinaryString(decimal);
		if(binary.length()<24){
			int missing = 24-binary.length();
			StringBuilder changed = new StringBuilder();
			for(int i = 0;i<missing;i++){
				changed.append("0");
			}
			changed.append(binary);
			binary = changed.toString();
		}
		int indexEnd = 24-blockBits;
		int numSets = cacheSize/(blockSize*numWays);
		int bitsForIndex = (int) (Math.log(numSets)/Math.log(2));
		//if(bitsForIndex == 0){
		//	bitsForIndex=1;
		//}
		int indexBeg = indexEnd - bitsForIndex;
		String tag = binary.substring(0, indexBeg+1);
		return tag;
	}

	public static String getOffset(String address, int cacheSize, int numWays, int blockSize, int blockBits){
		int decimal = Integer.parseInt(address.substring(2),16);
		String binary = Integer.toBinaryString(decimal);
		if(binary.length()<24){
			int missing = 24-binary.length();
			StringBuilder changed = new StringBuilder();
			for(int i = 0;i<missing;i++){
				changed.append("0");
			}
			changed.append(binary);
			binary = changed.toString();
		}
		int indexEnd = 24-blockBits;
		int numSets = cacheSize/(blockSize*numWays);
		int bitsForIndex = (int) (Math.log(numSets)/Math.log(2));
		if(bitsForIndex == 0){
			bitsForIndex=1;
		}
		int indexBeg = indexEnd - bitsForIndex;
		String offset = binary.substring(indexEnd);
		return offset;
	}

	public static String getAddr(String address, int cacheSize, int numWays, int blockSize, int blockBits){
		int decimal = Integer.parseInt(address.substring(2),16);
		String binary = Integer.toBinaryString(decimal);
		if(binary.length()<24){
			int missing = 24-binary.length();
			StringBuilder changed = new StringBuilder();
			for(int i = 0;i<missing;i++){
				changed.append("0");
			}
			changed.append(binary);
			binary = changed.toString();
		}
		String offset = binary.substring(24-blockBits);
		int indexEnd = 24-blockBits;
		int numSets = cacheSize/(blockSize*numWays);
		int bitsForIndex = (int) (Math.log(numSets)/Math.log(2));
		if(bitsForIndex == 0){
			bitsForIndex=1;
		}
		int indexBeg = indexEnd - bitsForIndex;
		String addr = binary.substring(0, indexEnd+1);
		return addr;

	}
	//public static void main(String[] args){
	//	storeTo("0xff0200",1024,16, 32, 4, "a2bc", 5);
	//}


}
