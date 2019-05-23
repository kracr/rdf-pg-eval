package neo4j;

public class Timer {
	public static long startTime = 0;
	public static long endTime = 0;

	public static void init() {

		startTime = System.currentTimeMillis();
	}

	public static long duration() {
		endTime = System.currentTimeMillis();
		return endTime - startTime;
	}

}
