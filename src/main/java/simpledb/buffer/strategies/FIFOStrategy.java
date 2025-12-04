package simpledb.buffer.strategies;
import simpledb.buffer.Buffer;
import simpledb.file.BlockId;
import java.util.ArrayDeque;
public class FIFOStrategy implements BufferStrategy {
    private ArrayDeque<Buffer> bufferQueue;
    public FIFOStrategy(Buffer[] buffPool) { 
        bufferQueue = new ArrayDeque<>();
        for (Buffer buff : buffPool) {
            bufferQueue.add(buff);
        }
    }

    @Override
    public void pin(Buffer buff) {
        if (!bufferQueue.contains(buff)) {
            bufferQueue.addLast(buff);
        }
    }
    @Override
    public Buffer chooseUnpinnedBuffer() {
        for (Buffer buff : bufferQueue) {
            if (!buff.isPinned()) {
                bufferQueue.remove(buff);
                return buff;
            }
        }
        return null;
    }
    
}
