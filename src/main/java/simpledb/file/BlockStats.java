package simpledb.file;
import java.util.Map;
import simpledb.file.Stats;
import java.util.HashMap;
import simpledb.file.BlockId;
public class BlockStats {
	HashMap<String, Stats> fileMap= new HashMap<>();
	// --- METHODS --- //
	public String toString(){
		String out = "";
		for (String file : fileMap.keySet())
		{
			out += "-" + file + ":\n";
			out += "-- Read: " + this.getReadCount(file) + " | Written: " + this.getWriteCount(file) + "\n"; 
		}

		return out;
	}
	public void logReadBlock(BlockId block){
		if (!fileMap.containsKey(block.fileName()))
			fileMap.put(block.fileName(), new Stats());
		
		fileMap.get(block.fileName()).updateReads();
	}
	public void logWrittenBlock(BlockId block){
		if (!fileMap.containsKey(block.fileName()))
			fileMap.put(block.fileName(), new Stats());
		
		fileMap.get(block.fileName()).updateWrites();
	}
	public void reset(){
		fileMap = new HashMap<String, Stats>();
	}
	public int getReadCount(String fileName){
		int reads = 0;
		if (fileMap.containsKey(fileName))
			reads = fileMap.get(fileName).getReads();
		return reads;
	}
	public int getWriteCount(String fileName){
		int writes = 0;
		if (fileMap.containsKey(fileName))
			writes = fileMap.get(fileName).getWrites();
		return writes;
	}
	public int getTotalReads() {
		int total = 0;
		for (String filename: fileMap.keySet()){
			total+= getReadCount(filename);
		}
		return total;
	}
	public int getTotalWrites(){
		int total = 0;
		for (String filename: fileMap.keySet()){
			total+= getWriteCount(filename);
		}
		return total;
	}
	
}
