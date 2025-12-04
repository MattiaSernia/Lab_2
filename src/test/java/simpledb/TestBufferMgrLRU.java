package simpledb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import java.util.logging.Logger;
import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.BlockId;
import simpledb.file.FileMgr;
import simpledb.log.LogMgr;

public class TestBufferMgrLRU {
    private static final Logger log = Logger.getLogger(TestBufferMgrFIFO.class.getName());
	private FileMgr fm;
    private LogMgr lm;
    private BufferMgr bm;

    private static final int NUM_BUFFS = 3;
    private static final String TEST_DB_DIR = "lrutestdb";
    private static final int BLOCK_SIZE = 400;

    @BeforeEach
    public void setUp() {
        File dbDir = new File(TEST_DB_DIR);
        dbDir.mkdirs();
        //Set system property to use LRU replacement
        System.setProperty("simpledb.replacement", "LRU");
        fm = new FileMgr(dbDir, BLOCK_SIZE);
        lm = new LogMgr(fm, "fifotest.log");
        //Create BufferMgr, which will look into system property to choose the replacement policy
        bm = new BufferMgr(fm, lm, NUM_BUFFS);

        log.info("Completed setup.");
    }

    @Test
    public void testLRU() {
        log.info()
        BlockId blk0 = new BlockId("testfile", 0);
        BlockId blk1 = new BlockId("testfile", 1);
        BlockId blk2 = new BlockId("testfile", 2);
        BlockId blk3 = new BlockId("testfile", 3);

        log.info("Creation of four different test blocks.");

        Buffer b0 = bm.pin(blk0);
        log.info("Pin block 0");
        Buffer b1 = bm.pin(blk1);
        log.info("Pin block 1");
        Buffer b2 = bm.pin(blk2);   
        log.info("Pin block 2");


        bm.unpin(b0);
        bm.unpin(b1);
        bm.unpin(b2);

        log.info("Unpin all the blocks in memory, which are now ready to be evicted.");

        Buffer b0_2 = bm.pin(blk0); // We pin again blk0 which now is the most recently used block.

        log.info("Pin again the block 0, which now cannot be evicted.");

        assertSame(b0, b0_2);
        log.info("Verify that blk0 was not evicted and the same buffer frame is returned, as required by LRU.");


        bm.unpin(b0_2);
        log.info("Unpin again block 0, which now is ready to be evicted but it is now the most recently used.");
        // At this point b1 is the LRU.

        Buffer b3 = bm.pin(blk3);
        log.info("Pin block 3. The buffer is full, so our Buffer Manager has to choose one block in memory to evict.");

        log.info("Verify if the new block has replaced the most recently used block, b0.");
        assertNotSame(b0, b3, "LRU strategy has removed b0 which was the most recently used."); 
        log.info("Test PASSED: block 0 has not been evicted since it was the most recently used block.");

        log.info("Verify if block 1 is evicted when block 3 is pinned and brought into memory.");
        assertSame(b1, b3, "LRU has not removed the least recently used block, which was b1.");
        log.info("Test PASSED: block 1 is correcy evicted and block 3 takes its place");

    }

}
