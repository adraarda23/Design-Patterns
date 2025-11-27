package BehavioralPatterns.Iterator;

/**
 * BookIterator - Kitap koleksiyonunda baştan sona dolaşan iterator
 */
public class BookIterator implements Iterator<Book> {
    private BookCollection collection;
    private int currentPosition;

    public BookIterator(BookCollection collection) {
        this.collection = collection;
        this.currentPosition = 0;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < collection.getSize();
    }

    @Override
    public Book next() {
        if (this.hasNext()) {
            Book book = collection.getBookAt(currentPosition);
            currentPosition++;
            return book;
        }
        return null;
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }
}
