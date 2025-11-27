package BehavioralPatterns.Iterator;

/**
 * ReverseBookIterator - Kitap koleksiyonunda sondan başa dolaşan iterator
 * Bu, aynı koleksiyonda farklı dolaşma stratejisi örneğidir
 */
public class ReverseBookIterator implements Iterator<Book> {
    private BookCollection collection;
    private int currentPosition;

    public ReverseBookIterator(BookCollection collection) {
        this.collection = collection;
        this.currentPosition = collection.getSize() - 1;
    }

    @Override
    public boolean hasNext() {
        return currentPosition >= 0;
    }

    @Override
    public Book next() {
        if (this.hasNext()) {
            Book book = collection.getBookAt(currentPosition);
            currentPosition--;
            return book;
        }
        return null;
    }

    @Override
    public void reset() {
        currentPosition = collection.getSize() - 1;
    }
}
