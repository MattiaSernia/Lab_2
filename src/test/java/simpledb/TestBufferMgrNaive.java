package simpledb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import java.util.logging.Logger;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.BlockId;
import simpledb.file.FileMgr;
import simpledb.log.LogMgr;

public class TestBufferMgrNaive {
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
        //Set system property to use NAIVE replacement
        System.setProperty("simpledb.replacement", "NAIVE");
        fm = new FileMgr(dbDir, BLOCK_SIZE);
        lm = new LogMgr(fm, "lrutest.log");
        //Create BufferMgr, which will look into system property to choose the replacement policy
        bm = new BufferMgr(fm, lm, NUM_BUFFS);
    }

    @Test
    public void testEvictsFirstAvailableFirst() {
        BlockId blk0 = new BlockId("testfile", 0);
        BlockId blk1 = new BlockId("testfile", 1);
        BlockId blk2 = new BlockId("testfile", 2);
        BlockId blk3 = new BlockId("testfile", 3);

        log.info("Pin three blocks")
        Buffer b0 = bm.pin(blk0);
        Buffer b1 = bm.pin(blk1);
        Buffer b2 = bm.pin(blk2);

        assertEquals(0, bm.available(), "All buffers should be pinned initially.");
        log.info("Test PASSED: all buffers are correctly pinned.")

        // Make all candidates: unpin all in some order.
        bm.unpin(b1);
        bm.unpin(b2);
        bm.unpin(b0);
        log.info("Unpin all the blocks in the memory.")

        assertEquals(3, bm.available(), "All buffers should now be available.");
        log,info("Test PASSED: all the buffers are available since we have unpinned them.")

        log.info("We pin a new block.")
        Buffer b3 = bm.pin(blk3);

        assertSame(b0, b3,
                "NAIVE: expected blk3 to reuse first avaialble buffer buffer");
        
        log.info("Test PASSED: block 3 replaces first available buffer buffer, so block 0 gets correctly evicted.")
        assertEquals(blk3, b3.block());
        log.info("Test PASSED: we verified that the recycled buffer is now correctly mapped to block 3.")
    }

}
