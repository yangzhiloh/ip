package esther.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import esther.command.CommandProcessor;
import esther.command.CommandResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for Esther's main JavaFX window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private CommandProcessor commandProcessor;
    private Image userImage;
    private Image estherImage;

    /**
     * Initializes scrolling for the conversation area.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        sendButton.setDefaultButton(true);
        userImage = loadImage("/images/user.png");
        estherImage = loadImage("/images/esther.png");
    }

    /**
     * Injects the shared command processor.
     *
     * @param commandProcessor Processor used to execute Esther commands.
     */
    public void setCommandProcessor(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    /**
     * Adds Esther messages to the conversation.
     *
     * @param messages Messages to display in order.
     */
    public void showEstherMessages(List<String> messages) {
        for (String message : messages) {
            dialogContainer.getChildren().add(
                    DialogBox.createEstherDialog(message, estherImage));
        }
    }

    /**
     * Processes the current input and displays the response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        dialogContainer.getChildren().add(
                DialogBox.createUserDialog(input, userImage));
        CommandResult result = commandProcessor.process(input);
        showEstherMessages(result.messages());
        userInput.clear();

        if (result.shouldExit() && userInput.getScene() != null) {
            userInput.getScene().getWindow().hide();
        }
    }

    private Image loadImage(String resourcePath) {
        try (InputStream imageStream = MainWindow.class.getResourceAsStream(
                resourcePath)) {
            if (imageStream == null) {
                throw new IllegalStateException(
                        "Missing required GUI image: " + resourcePath);
            }
            return new Image(imageStream);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load GUI image: " + resourcePath, exception);
        }
    }
}
