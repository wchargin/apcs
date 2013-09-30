package graphs.wordladder;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Stack;

public class BasicQueue<E> extends AbstractQueue<E> {

	private Stack<E> in = new Stack<>(), out = new Stack<>();

	@Override
	public boolean offer(E e) {
		in.push(e);
		return true;
	}

	private void setupOut() {
		if (out.isEmpty()) {
			// push reverse of in onto out
			// O(in.size())
			while (!in.isEmpty()) {
				out.push(in.pop());
			}
		}
	}

	@Override
	public E peek() {
		setupOut();
		return out.peek();
	}

	@Override
	public E poll() {
		setupOut();
		return out.isEmpty() ? null : out.pop();
	}

	private class QueueIterator implements Iterator<E> {

		/**
		 * This is cloned so that we can destructively edit the queue without
		 * affecting the enclosing instance.
		 */
		private BasicQueue<E> cloned = new BasicQueue<>();
		{
			// AbstractQueue's `addAll' results in stack overflow
			// must clone manually
			cloned.in = new Stack<E>();
			cloned.out = new Stack<E>();
			cloned.in.addAll(in);
			cloned.out.addAll(out);
		}

		/**
		 * The index of the current item in the list [out + reverse(in)]. Also
		 * equivalent to one less than the number of times {@link #next()} has
		 * been called.
		 */
		private int index = -1;

		@Override
		public boolean hasNext() {
			return !(cloned.out.isEmpty() && cloned.in.isEmpty());
		}

		@Override
		public E next() {
			index++;
			cloned.setupOut();
			return cloned.out.pop();
		}

		@Override
		public void remove() {
			if (index < out.size()) {
				out.remove(index);
			} else {
				in.remove(index - out.size());
			}
		}

	}

	@Override
	public Iterator<E> iterator() {
		return new QueueIterator();
	}

	@Override
	public int size() {
		return in.size() + out.size();
	}

}
