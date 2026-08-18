import java.util.Scanner;

public class Esther {
    public static void main(String[] args) {
        String line = "___________________________________________________________";

        Scanner scanner = new Scanner(System.in);

        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];
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
                int tasksLeft = 0;

                for (int i = 0; i < taskCount; i++) {
                    if (!isDone[i]) {
                        tasksLeft++;
                    }
                }
                System.out.println(line);

                if (tasksLeft == 0) {System.out.println("WOW! You have no tasks left, good work!");}

                if (1 <= tasksLeft && tasksLeft <= 5) {
                    String taskWord = tasksLeft == 1 ? "task" : "tasks";
                    System.out.println(String.format("Only %d %s left! Should be a piece of cake for you!", tasksLeft, taskWord));
                }

                if (tasksLeft > 5) {System.out.println(String.format("%d tasks left?? What have you been doing this whole time?? You better focus up!", tasksLeft));}

                for (int i = 0; i < taskCount; i++) {
                    String statusIcon = isDone[i] ? "X" : " ";
                    System.out.println(String.format("%d.[%s] %s", i + 1, statusIcon, tasks[i]));
                }

                System.out.println(line);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5).trim());
                int taskIndex = taskNumber - 1;

                isDone[taskIndex] = true;

                System.out.println(line);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  [X] " + tasks[taskIndex]);
                System.out.println(line);
            } else {
                tasks[taskCount] = command;
                taskCount++;
                String taskWord = taskCount == 1 ? "task" : "tasks";

                System.out.println(line);
                System.out.println(String.format("Added: %s", command));
                System.out.println(String.format("Now you have %d %s in the list", taskCount, taskWord));
                System.out.println(line);
            }

        }

        scanner.close();

    }
}
