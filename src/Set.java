import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Set {
	public int idex; 
	public int j;
	public static ArrayList<Integer> lru;
	public static ArrayList<Block> Blocks;
	public Set(int cacheSize, int numWays, int blockSize, HashMap<String, ArrayList<String>> memory){
		int j = 0;
		lru = new ArrayList<Integer>();
		Blocks = new ArrayList<Block>();
		for(int i = 0;i<numWays;i++){
			Blocks.add(new Block(blockSize, memory));
		}
		for(int i =0;i<numWays;i++){
			lru.add(0);
		}
	}
	
	public void storeTo(int blockSize, String tag, String value, int accessSize, String offset, String addr, String address){
		Block blk = findValidWay(blockSize,tag);
		blk.storeTo(value, tag, accessSize, offset, addr, address, blockSize, idex);
	}
	public void loadFrom(int blockSize, String tag, int accessSize, String offset, String addr, String address){
		Block blk = findValidWay(blockSize,tag);
		blk.loadFrom(tag, accessSize, offset, addr, address, blockSize, idex);
	}
	public int LRU(){
		int minIndex = lru.indexOf(Collections.min(lru));
		lru.set(minIndex,j);
		j++;
		return minIndex;
		}
	public Block findValidWay(int blockSize, String tag){
		Block retval = null;
		for(Block blk : Blocks){
			if(blk.isValid()==true && blk.tag().equals(tag)){
				retval = blk;
				int index = Blocks.indexOf(retval);
				idex = index;
				lru.set(index,j);
				j++;
				break;
			}
		}
		if(retval==null){
			int index = LRU();
			idex = index;
			//retval = new Block(blockSize);
			retval = Blocks.get(index);
		}
		return retval;
	}
	
	
}
