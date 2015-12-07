import static org.junit.Assert.*;

import org.junit.Test;

public class RingbufferTest {

	@Test
	public void test() {
		Ringbuffer<Integer> buffer = new Ringbuffer<Integer>( 5);
		System.out.println(buffer.isEmpty());
		buffer.add(new Integer(2));
		System.out.println(buffer.isEmpty());
		System.out.println(buffer.size());
		System.out.println(buffer.contains(new Integer(2)));
		System.out.println(buffer.peek());
		System.out.println(buffer.poll());
		buffer.add(new Integer(5));
		buffer.remove();
		System.out.println(buffer.size());
		System.out.println(buffer.peek());
		System.out.println(buffer.contains(new Integer(2)));
		
	}

}
