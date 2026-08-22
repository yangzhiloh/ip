import java.util.Scanner;

/**
 * Handles interactions between Esther and the user.
 */
public class Ui {
    private static final String DIVIDER =
            "___________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return Command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows Esther's welcome message.
     */
    public void showWelcome() {
        showDivider();
        System.out.println("Heyyyy! I'm your favourite assistant Esther.");
        System.out.println("What can I do for you today?");
        showDivider();
    }

    /**
     * Shows Esther's goodbye message.
     */
    public void showGoodbye() {
        showDivider();
        System.out.println("Byeee! Hope to see you soooon!");
        showDivider();
    }

    /**
     * Shows an error encountered while loading saved tasks.
     */
    public void showLoadingError() {
        showDivider();
        System.out.println(
                "I couldn't load your saved tasks. Starting empty.");
        showDivider();
    }

    /**
     * Shows all stored tasks and the number that remain incomplete.
     *
     * @param tasks Tasks to show.
     */
    public void showTaskList(TaskList tasks) {
        int tasksLeft = tasks.countNotDone();

        showDivider();

        if (tasksLeft == 0) {
            System.out.println("WOW! You have no tasks left, good work!");
        } else if (tasksLeft <= 5) {
            String taskWord = getTaskWord(tasksLeft);
            System.out.println(String.format(
                    "Only %d %s left! Should be a piece of cake for you!",
                    tasksLeft,
                    taskWord
            ));
        } else {
            System.out.println(String.format(
                    "%d tasks left?? What have you been doing this whole "
                            + "time?? You better focus up!",
                    tasksLeft
            ));
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format(
                    "%d.%s",
                    i + 1,
                    tasks.get(i)
            ));
        }

        showDivider();
    }

    /**
     * Shows a task that has been marked as done.
     *
     * @param task Task that was marked.
     */
    public void showTaskMarked(Task task) {
        showDivider();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(" " + task);
        showDivider();
    }

    /**
     * Shows a task that has been marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        showDivider();
        System.out.println("Okay! I've marked this task as not done.");
        System.out.println(" " + task);
        showDivider();
    }

    /**
     * Shows a task that has been deleted.
     *
     * @param task Task that was deleted.
     * @param taskCount Number of tasks remaining.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showDivider();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " "
                        + getTaskWord(taskCount) + " in the list.");
        showDivider();
    }

    /**
     * Shows a task that has been added.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks stored.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " "
                        + getTaskWord(taskCount) + " in the list.");
        showDivider();
    }

    /**
     * Shows a command-processing error.
     *
     * @param message Error message to show.
     */
    public void showError(String message) {
        showDivider();
        System.out.println(message);
        showDivider();
    }

    /**
     * Shows an error encountered while saving tasks.
     */
    public void showSaveError() {
        showDivider();
        System.out.println(
                "I couldn't save your tasks. Please try again.");
        showDivider();
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Shows the divider used between responses.
     */
    private void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Returns the singular or plural form of task for a count.
     *
     * @param taskCount Number of tasks.
     * @return Singular or plural task label.
     */
    private String getTaskWord(int taskCount) {
        return taskCount == 1 ? "task" : "tasks";
    }
}
