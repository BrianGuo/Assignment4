package util;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Ringbuffer<T> implements BlockingQueue<T>{
	
	private T[] queue;
	private int head;
	private int tail;
	private int remainingCapacity;
	
	private class RingIterator<T> implements Iterator<T> {
		
		private int head;
		private int tail;
		private T[] queue;
		
		public RingIterator(int head, int tail, T[] queue){
			this.head = head;
			this.tail = tail;
			this.queue = queue;
		}
		
		@Override
		public boolean hasNext() {
			return head == tail - 1;
		}

		@Override
		public T next() {
			if (hasNext())
				return queue[head%queue.length];
			else
				throw new NoSuchElementException();
		}
		
	}
	@Override
	public T remove() throws NoSuchElementException{
		T result = element();
		head ++;
		remainingCapacity++;
		return result;
	}
	@Override
	public T poll() {
		try{
			return remove();
		}
		catch(NoSuchElementException e) {
			return null;
		}
	}
	@Override
	public T element() throws NoSuchElementException{
		if (isEmpty())
			throw new NoSuchElementException();
		else
			return queue[head%queue.length];
	}
	@Override
	public T peek(){
		try{
			return element();
		}
		catch(NoSuchElementException e) {
			return null;
		}
	}
	@Override
	public int size() {
		return queue.length - remainingCapacity;
	}
	@Override
	public boolean isEmpty() {
		return remainingCapacity == queue.length;
	}
	@Override
	public Iterator<T> iterator() {
		return new RingIterator<T>(head,tail,queue);
	}
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		for (int i = head; i < tail; i++ ) {
			if (!(c.contains(queue[i%queue.length])))
				return false;
		}
		return true;
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean add(T e) throws IllegalStateException {
		if (remainingCapacity == 0) {
			throw new IllegalStateException();
		}
		else {
			queue[(tail-1)%queue.length] = e;
			tail ++;
			remainingCapacity--;
			return true;
		}
	}
	@Override
	public boolean offer(T e) {
		try{
			return add(e);
		}
		catch(IllegalStateException ex){
			return false;
		}
	}
	@Override
	public void put(T e) throws InterruptedException {
		while(remainingCapacity == 0){
			wait();
		}
		add(e);
		
	}
	@Override
	public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public T take() throws InterruptedException {
		while(remainingCapacity == queue.length){
			wait();
		}
		return remove();
	}
	@Override
	public T poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int remainingCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean remove(Object o) {
		return false;
	}
	@Override
	public boolean contains(Object o) {
		for (int i = head; i< tail; i++ ){
			if (queue[i%queue.length].equals(o))
				return true;
		}
		return false;
	}
	@Override
	public int drainTo(Collection<? super T> c) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int drainTo(Collection<? super T> c, int maxElements) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean equals(Object o){
		if (!(o instanceof BlockingQueue))
			return false;
		else{
			BlockingQueue second = (BlockingQueue) o;
			if (!(second.containsAll(this)) || !(second.size() == this.size()))
				return false;
			return true;
		}
	}
}
	
	