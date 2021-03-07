package core;

import java.util.List;

abstract public class Iterator<T> {

    protected int current = 0;

    protected final List<T> iterables;

    public Iterator(List<T> iterables) {
        this.iterables = iterables;
    }


    public T getCurrentItem() {
        return this.iterables.get(this.current);
    }

    T peek() {
       return this.iterables.get(this.current);
    }

    T peekNext() {
        if ( notAtEnd() ) {
            return this.iterables.get(this.current + 1);
        }
        else {
            return null;
        }
    }


    public void advance() {
        this.current += 1;
    }

    abstract boolean notAtEnd();

    public T prev() {
        return this.iterables.get(this.current - 1);
    }

    public void moveBack() {
        this.current -= 1;
    }

}
