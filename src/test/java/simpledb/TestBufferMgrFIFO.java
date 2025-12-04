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

public class TestBufferMgrFIFO {
    private static final Logger log = Logger.getLogger(TestBufferMgrFIFO.class.getName());
	private FileMgr fm;
    private LogMgr lm;
    private BufferMgr bm;

    private static final int NUM_BUFFS = 3;
    private static final String TEST_DB_DIR = "fifotestdb";
    private static final int BLOCK_SIZE = 400;

    @BeforeEach
    public void setUp() {
        File dbDir = new File(TEST_DB_DIR);
        dbDir.mkdirs();
        //Set system property to use FIFO replacement
        System.setProperty("simpledb.replacement", "FIFO");
        fm = new FileMgr(dbDir, BLOCK_SIZE);
        lm = new LogMgr(fm, "fifotest.log");
        //Create BufferMgr, which will look into system property to choose the replacement policy
        bm = new BufferMgr(fm, lm, NUM_BUFFS);
    }

    @Test
    public void testFIFO() {
        log.info("Creation of five blocks.")
        BlockId blk0 = new BlockId("testfile", 0);
        BlockId blk1 = new BlockId("testfile", 1);
        BlockId blk2 = new BlockId("testfile", 2);
        BlockId blk3 = new BlockId("testfile", 3);
        BlockId blk4 = new BlockId("testfile", 4);

        log.info("Pin block 0");
        Buffer b0 = bm.pin(blk0);
        log.info("Pin block 1");
        Buffer b1 = bm.pin(blk1);
        log.info("Pin block 2");
        Buffer b2 = bm.pin(blk2);

        assertEquals(0, bm.available(), "All buffers should be pinned initially.");

        // we unpin b0 since it was the oldest - fifo strategy

        bm.unpin(b0);
        Buffer b3 = bm.pin(blk3);
        log.info("We unpin block 0 that becomes now can be evicted.");
        
        assertSame(b0,b3, "B3 has not correctly substituted b0");

        log.info("Test PASSED: block 3 has correctly replaced block 0.")



        // now b1 is the oldest - the first in

        log.info("We unpin all the blocks in the memory.")
        bm.unpin(b1);
        bm.unpin(b2);
        bm.unpin(b3);
        log.info("Now the last block in memory is block 1, since block 0 has already been evicted.")

        Buffer b4 = bm.pin(blk4);
        log.info("We pin a new block. According to the FIFO strategy, the first block in memory should be evicted, so block 1.")

        // b4 should have taken the place of the oldest block - b1, and not of the most recent block, b3.
        assertNotSame(b4,b3, "b4 should not take place of b3");
        log.info("Test PASSED: block 4 has correcly not replaced block 3.")
        assertSame(b1,b4, "b4 should take place of b1");
        log.info("Test PASSED: block 4 has correcly replaced block 1.")

    }

}
