package allocator;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import util.Poisson;

public class SimpleAllocator implements Allocator {
    private final List<Block> blocks = new LinkedList<Block>();
    
    private final int rate; // milliseconds between allocations
    private final int sizeEach; // bytes in each block's array
    private final int count; // number of blocks per allocation
    private final int lifetime; // max milliseconds lifetime of allocations
    private final int lambda; // lambda value for lifetime (poisson) distribution

    private final Poisson p = new Poisson();
    
    private long nextAllocateTime = 0;
    
    private SimpleAllocator(int rate, int sizeEach, int count, int lifetime, int lambda) {
        this.rate = rate;
        this.sizeEach = sizeEach;
        this.count = count;
        this.lifetime = lifetime;
        this.lambda = lambda;
    }

    public static class Builder {
        // defaults should account for 78MB of young space
        private int rate = 1, sizeEach = 64, count = 4096, lifetime = 20000, lambda = 1;
        private Builder() {}
        public Builder rate(int r) { rate = r; return this; }
        public Builder sizeEach(int s) { sizeEach = s; return this; }
        public Builder count(int c) { count = c; return this; }
        public Builder lifetime(int l) { lifetime = l; return this; }
        public Builder lambda(int l) { lambda = l; return this; }
        public SimpleAllocator build() { 
            return new SimpleAllocator(rate, sizeEach, count, lifetime, lambda); 
        }
    }
    public static Builder builder() { return new Builder(); }
    
    @Override
    public void tick(long tick) {
        // allocate phase
        if (tick >= nextAllocateTime) {
            nextAllocateTime += rate;
            for (int i = 0; i < count; i++) {
                blocks.add(
                    new Block(
                        tick + (int)((lifetime * p.poisson(lambda, 10)) / 10),
                        new byte[sizeEach])
                );
            }
        }
        // deallocate phase
        ListIterator<Block> lib = blocks.listIterator();
        while (lib.hasNext()) {
            Block b = lib.next();
            if (b.expires <= tick) {
                lib.remove();
            }
        }
    }
}
