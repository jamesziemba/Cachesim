import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Block {
	boolean validity;
	String tag;
	ArrayList<String> data;
	boolean dirty;
	HashMap<String,ArrayList<String>> RAM;
	public Block(int blockSize, HashMap<String, ArrayList<String>> memory){
		tag = "";
		validity=false;
		data = new ArrayList<String>();
		dirty = false;
		RAM = memory;
	}
	public boolean isValid(){
		return validity;
	}

	public String tag() {
		return tag;
	}
	public void loadFrom(String tag1, int accessSize, String offset, String addr, String address, int blockSize, int idex){
		int where = Integer.parseInt(offset, 2);
		int counter =0;
		StringBuilder zeroes = new StringBuilder();
		for(int i = 0;i<blockSize;i++){
			zeroes.append("0");
		}
		if(tag.equals(tag1) && validity==true){
			ArrayList<String> temp = data;
			StringBuilder str = new StringBuilder();
			for(int i = where;i<accessSize+where;i++){
				str.append(temp.get(i));
			}
			System.out.println("load "+ address + " "+"hit "+ str.toString());
		}
		else if(validity==true && dirty == true && !tag.equals(tag1)){
			writeBack(idex, data);
			ArrayList<String> temp = RAM.get(addr);
			StringBuilder str = new StringBuilder();
			for(int i = where;i<accessSize+where;i++){
				str.append(temp.get(i));
			}
			System.out.println("load "+ address + " "+"miss "+ str.toString());
			tag = tag1;
		}
		else{
			if(validity ==true && dirty ==true){
				writeBack(idex,data);
			}
			ArrayList<String> temp = RAM.get(addr);
			if(temp == null){
				temp = new ArrayList<String>();
				for(int i = 0; i<blockSize;i++){
					temp.add("00");
				}
			}
			StringBuilder readVal = new StringBuilder();
			for(int i = where;i<accessSize+where;i++){
				readVal.append(temp.get(i));
			}
			data = temp;
			if(validity ==false){
				System.out.println("load "+ address + " "+"miss "+ readVal.toString());
				tag = tag1;
				validity = true;
			}
		}
	}

	public void storeTo(String value, String tag1, int accessSize, String offset, String addr, String address, int blockSize, int idex){
		int where = Integer.parseInt(offset, 2);
		int counter =0;
		if(validity == true && tag.equals(tag1)){
			for(int i = where;i<accessSize;i++){
				if(where!=accessSize-1){
					data.set(i,value.substring(counter, counter+2));
					counter=counter+2;
				}
				else{
					data.set(i,value.substring(counter));
				}
			}
			dirty = true;
			System.out.println("store "+ address + " hit" );
		}
		else{
			if(validity ==true && dirty ==true){
				writeBack(idex,data);
			}
			ArrayList<String> temp = new ArrayList<String>();
			ArrayList<String> listed = new ArrayList<String>(Arrays.asList(value.split("")));
			for(int i = 0;i<listed.size();i=i+2){
				StringBuilder tString = new StringBuilder();
				tString.append(listed.get(i));
				tString.append(listed.get(i+1));
				temp.add(tString.toString());
			}
			writeAllocate(addr,temp,offset, blockSize);
			ArrayList<String> dataBlock = getDataFromRAM(addr);
			data = dataBlock;
			if(validity ==false){
				System.out.println("store "+ address + " miss" );
			}
		}
		validity = true;
		tag = tag1;
	}
	public void writeAllocate(String addr,ArrayList<String> dataBlock, String offset, int blockSize){
		int where = Integer.parseInt(offset, 2);
		ArrayList<String> temp = RAM.get(addr);
		if(temp ==null){
			temp = new ArrayList<String>();
			for(int i = 0; i<blockSize;i++){
				temp.add("00");
			}
		}
		int dbCounter = 0;	
		for(int i = where;i<dataBlock.size()+where;i++){
			temp.set(i,dataBlock.get(dbCounter));
			dbCounter++;
		}
		RAM.put(addr, temp);
	}
	public void writeBack(int idex, ArrayList<String> dataBlock){
		StringBuilder newAddr = new StringBuilder();
		newAddr.append(tag);
		newAddr.append(Integer.toString(idex));
		RAM.put(newAddr.toString(), dataBlock);
	}
	public ArrayList<String> getDataFromRAM(String addr){
		return RAM.get(addr);
	}
}
