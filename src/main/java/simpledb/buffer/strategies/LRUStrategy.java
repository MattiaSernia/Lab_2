package simpledb.buffer.strategies;

import simpledb.buffer.Buffer;
import simpledb.file.BlockId;

import java.util.ArrayDeque;

public class LRUStrategy implements BufferStrategy
{

    private ArrayDeque<Buffer> LRUqueue;

    public LRUStrategy(Buffer[] buffPool)
    {
        LRUqueue = new ArrayDeque<>();
        for (Buffer buff : buffPool)
            LRUqueue.add(buff);
    }

    @Override
    public void pin(Buffer buff)
    {
        if (LRUqueue.contains(buff))
            LRUqueue.remove(buff);
        LRUqueue.add(buff); 
    }

    @Override
    public Buffer chooseUnpinnedBuffer()
    {
            for (Buffer buff : LRUqueue)
            {
                if (!buff.isPinned())
                {
                    LRUqueue.remove(buff);
                    return buff;
                }
            }
        return null;
    }
    
}