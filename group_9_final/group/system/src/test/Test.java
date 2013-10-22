package test;

import gov.nasa.jpf.annotation.Ensures;
import gov.nasa.jpf.annotation.Invariant;
import gov.nasa.jpf.annotation.Requires;

@Invariant({ "d within 40 +- 5", "a > 0" })
class Test {
	double d = 0;
	int a = 0;
	Object test = null;

	@Requires("c > 0")
	@Ensures("Result >= 0")
	public int computeSomething(int c) {
		return c - 3;
	}

	public static void main(String[] args) {
		Test t = new Test();
		t.computeSomething(5);
	}
}