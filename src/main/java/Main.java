import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		for(int j = 1; j<=20; j++){
			for(int i = 0; i<10; i++){
				Tester.testNetwork(j, 0.1, i);
			}
			for(int i = 0; i<10; i++){
				Tester.testNetwork(j, 0.3, i);
			}
			for(int i = 0; i<10; i++){
				Tester.testNetwork(j, 0.5, i);
			}
			for(int i = 0; i<10; i++){
				Tester.testNetwork(j, 0.7, i);
			}
			for(int i = 0; i<10; i++){
				Tester.testNetwork(j, 0.9, i);
			}
		}
		System.out.println("Done");
	}

}
