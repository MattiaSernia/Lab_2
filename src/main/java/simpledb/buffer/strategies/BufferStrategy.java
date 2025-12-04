package simpledb.buffer.strategies;

import simpledb.buffer.Buffer;
import simpledb.file.BlockId;

public interface BufferStrategy {
    public default void pin(Buffer buff) {}
    public Buffer chooseUnpinnedBuffer();
}