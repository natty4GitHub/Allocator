package allocator;

public interface Allocator {
    class Block {
        long expires;
        byte [] data;

        public Block(long expires, byte[] data) {
            this.expires = expires;
            this.data = data;
        }
    }

    public void tick(long tick);
}
