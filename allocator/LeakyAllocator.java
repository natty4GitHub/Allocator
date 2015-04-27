package allocator;

import java.util.LinkedList;
import java.util.List;

public class LeakyAllocator implements Allocator {
    private class Leak {
        byte[] ba = new byte[64];
    }
    
    private List<Leak> list = new LinkedList<Leak>();
    @Override
    public void tick(long tick) {
        list.add(new Leak());
    }
}
