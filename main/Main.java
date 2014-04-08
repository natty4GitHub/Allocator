package main;

import allocator.Allocator;
import allocator.SimpleAllocator;

public class Main {

    public static void main(String[] args) throws Throwable {
        final Allocator[] allocators = {
            // create young gen noise 
            SimpleAllocator.builder().build(),
            // Long-lived allocations -- 6 minutes to stable, about 1GB stable data
//            SimpleAllocator.builder().rate(50).count(200).lambda(5).lifetime(600000).sizeEach(1024).build()
        };

        final long startTick = System.currentTimeMillis();
        while (true) {
            for (Allocator a : allocators) {
                a.tick(System.currentTimeMillis() - startTick);
                Thread.sleep(2);
            }
        }
    }
}
