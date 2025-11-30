package simpledb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simpledb.file.BlockStats;
import simpledb.file.BlockId;

//TODO: test toString method
public class TestBlockStats {
    private BlockStats bs;
    @Test
    public void testBlockStatsInitialization() {
        BlockStats bs = new BlockStats();
        if (!(bs instanceof BlockStats)){
            assert(false);
        }
    }
    
    @Test
    public void testNullReadLog(){
        try{
            bs = new BlockStats();
            assert(bs.getReadCount("nonexistentfile") == 0);
        }catch(Exception e){
            assert(false);
        }
    }

    @Test
    public void testNullWriteLog(){
        try{
            bs = new BlockStats();
            assert(bs.getWriteCount("nonexistentfile") == 0);
        }
        catch(Exception e)
        {
            assert(false);
        }
    }
    @Test 
    public void testBlockStatsReadLog(){
        try{
            BlockStats bs = new BlockStats();
            bs.logReadBlock(new BlockId("testfile", 0));
            assert(bs.getReadCount("testfile") == 1);
        }
        
        catch(Exception e)
        {
            assert(false);
        }
    }
    @Test 
    public void testMultipleBlockStatsReadLog(){
        try{
            BlockStats bs = new BlockStats();
            for (int i=0; i<30; i++){
                bs.logReadBlock(new BlockId("testfile", 0));
            }
            assert(bs.getReadCount("testfile") == 30);
        }
        catch(Exception e)
        {
            assert(false);
        }
    }
    @Test 
    public void testMultipleBlocksStatsReadLog(){
        try{
            BlockStats bs = new BlockStats();
            for (int i=0; i<30; i++){
                bs.logReadBlock(new BlockId("testfile", i));
            }
            assert(bs.getReadCount("testfile") == 30);
        }
        catch(Exception e)
        {
            assert(false);
        }
    }

    @Test 
    public void testBlockStatsWriteLog(){
        try{
            BlockStats bs = new BlockStats();
            bs.logWrittenBlock(new BlockId("testfile", 0));
            assert(bs.getWriteCount("testfile") == 1);
        }
        
        catch(Exception e)
        {
            assert(false);
        }
    }
    @Test 
    public void testMultipleBlockStatsWriteLog(){
        try{
            BlockStats bs = new BlockStats();
            for (int i=0; i<30; i++){
                bs.logWrittenBlock(new BlockId("testfile", 0));
            }
            assert(bs.getWriteCount("testfile") == 30);
        }
        catch(Exception e)
        {
            assert(false);
        }
    }
    @Test 
    public void testMultipleBlocksStatsWriteLog(){
        try{
            BlockStats bs = new BlockStats();
            for (int i=0; i<30; i++){
                bs.logWrittenBlock(new BlockId("testfile", i));
            }
            assert(bs.getWriteCount("testfile") == 30);
        }
        catch(Exception e)
        {
            assert(false);
        }
    }
    @Test
    public void testBlockStatsReset(){
        try{
            BlockStats bs = new BlockStats();
            bs.logReadBlock(new BlockId("testfile", 0));
            bs.reset();
            assert(bs.getReadCount("testfile") == 0);
        }
        catch(Exception e)
        {
            assert(false);
        }
    }


    @Test 
    public void testBlockStatsTotalRead(){
        try{
            BlockStats bs = new BlockStats();
            bs.logReadBlock(new BlockId("testfile", 0));
            bs.logReadBlock(new BlockId("testfile2", 0));

            bs.logReadBlock(new BlockId("testfile", 0));
            bs.logReadBlock(new BlockId("testfile2", 0));
            assert(bs.getTotalReads() == 4);
        }
        
        catch(Exception e)
        {
            assert(false);
        }
    }

    @Test 
    public void testBlockStatsTotalWrites(){
        try{
            BlockStats bs = new BlockStats();
            bs.logWrittenBlock(new BlockId("testfile", 0));
            bs.logWrittenBlock(new BlockId("testfile2", 0));

            bs.logWrittenBlock(new BlockId("testfile", 0));
            bs.logWrittenBlock(new BlockId("testfile2", 0));

            assert(bs.getTotalWrites() == 4);
        }
        
        catch(Exception e)
        {
            assert(false);
        }
    }
    
            
}
