import java.util.Scanner;

public class Esther {
    public static void main(String[] args) {
        String line = "___________________________________________________________";

        Scanner scanner = new Scanner(System.in);

        System.out.println(line);
        System.out.println("Heyyyy! I'm your favourite assistant Esther.");
        System.out.println("What can I do for you today?");
        System.out.println(line);

        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println(line);
                System.out.println("Byeee! Hope to see you soooon!");
                System.out.println(line);
                break;
            }

            System.out.println(line);
            System.out.println(command);
            System.out.println(line);
        }

        scanner.close();
        
    }
}
