import java.util.Scanner;

public class Esther {
    public static void main(String[] args) {
        String line = "___________________________________________________________";

        Scanner scanner = new Scanner(System.in);

        String[] tasks = new String[100];
        int taskCount = 0;

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
            } else if (command.equals("list")) {
                System.out.println(line);
                if (taskCount == 0) {System.out.println("WOW! You have no tasks left, good work!");}
                if (1 <= taskCount && taskCount <= 5) {
                    String taskWord = taskCount == 1 ? "task" : "tasks";
                    System.out.println(String.format("Only %d %s left! Should be a piece of cake for you!", taskCount, taskWord));
                }
                if (taskCount > 5) {System.out.println(String.format("%d tasks left?? What have you been doing this whole time?? You better focus up!", taskCount));}
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(String.format("%d. %s", i + 1, tasks[i]));
                }
                System.out.println(line);
            } else {
                tasks[taskCount] = command;
                taskCount++;
                String taskWord = taskCount == 1 ? "task" : "tasks";

                System.out.println(line);
                System.out.println(String.format("Added: %s", command));
                System.out.println(String.format("Now you have %d %s outstanding", taskCount, taskWord));
                System.out.println(line);
            }

        }

        scanner.close();
        
    }
}
