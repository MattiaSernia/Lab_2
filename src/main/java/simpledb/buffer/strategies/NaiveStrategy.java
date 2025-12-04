package simpledb.buffer.strategies;

import simpledb.buffer.Buffer;
import simpledb.file.BlockId;

public class NaiveStrategy implements BufferStrategy
{
    private Buffer[] pool;
    public NaiveStrategy(Buffer[] buffPool)
    {
        pool = buffPool;
    }

    @Override
    public Buffer chooseUnpinnedBuffer(){
        for (Buffer buff : pool){
            if (!buff.isPinned())
            return buff;
        }
        return null;
    }
}
