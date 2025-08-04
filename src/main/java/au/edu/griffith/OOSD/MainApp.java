package au.edu.griffith.OOSD;

// 21.
// Think of 'import' as telling our file which toolboxes we need from Java's vast workshop.
// By importing 'Button', for example, we can just write 'Button' instead of the long, full
// name 'javafx.scene.control.Button' every time we need one.
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.InputStream;

// 1.
// Here we're creating the main blueprint for our application.
// 'public' is like a "Public Access" sign; it means this class can be seen and used by other parts of our project.
// 'class MainApp' is us giving our blueprint a name.
// 'extends Application' is the magic part. It means our class gets all the powers and tools from
// JavaFX's built-in 'Application' class, which knows all about creating windows and handling events.
public class MainApp extends Application {

    // 3.
    // This is a special method we must have because we extended 'Application'.
    // The '@Override' note tells Java, "I know 'Application' has a 'start' method, and I'm providing my own custom version here."
    // 'public void start(Stage initialStage)' breaks down like this:
    //    - 'public': Anyone can call this method.
    //    - 'void': This method does a job, but it doesn't return any value when it's done.
    //    - 'start': The name of the job.
    //    - '(Stage initialStage)': To do its job, this method needs one tool: a 'Stage', which we'll call 'initialStage'.
    @Override
    public void start(Stage initialStage) {
        // 19.
        // We are creating a new 'Stage' object. The 'new' keyword tells Java to build us a fresh object from a blueprint.
        // When we create it, we give it an instruction right away: 'StageStyle.UNDECORATED'. Think of this
        // as ordering a window but telling the builder, "No borders or title bar, please."
        Stage splashStage = new Stage(StageStyle.UNDECORATED);

        // 20.
        // Here we call our own 'showSplashScreen' method. We give it two things:
        // 1. The 'splashStage' we just created.
        // 2. A tiny, unnamed function: '() -> showMainWindow(initialStage)'. This is a lambda expression, a compact
        //    way of saying, "When you're done with the splash screen, here's the exact piece of code I want you to run next."
        showSplashScreen(splashStage, () -> showMainWindow(initialStage));
    }

    // 11.
    // This is our own custom method for the splash screen logic.
    // It needs two things to work: the 'splashStage' window, and a 'Runnable' to-do item, which we call 'onFinished'.
    // A 'Runnable' is simply any piece of code that can be "run", just like our lambda.
    private void showSplashScreen(Stage splashStage, Runnable onFinished) {
        // 12.
        // This 'if' statement is like asking the computer a yes/no question.
        // The question is: "When we tried to load the splash image, did we get nothing back (null)?"
        // If the answer is yes, the code inside the curly braces runs, which prints an error and skips the splash.
        // 'return;' tells the method to stop right here and not execute any more code.
        InputStream imageStream = getClass().getResourceAsStream("/assets/splash.png");
        if (imageStream == null) {
            System.err.println("Splash screen image not found! Skipping splash.");
            onFinished.run();
            return;
        }
        Image splashImage = new Image(imageStream);
        ImageView splashView = new ImageView(splashImage);

        // 13.
        // We build the visual parts. We put our image inside a simple 'BorderPane' layout,
        // and then we put that layout into a 'Scene'. A Scene is like the actual drawing or content
        // that appears inside a window (the Stage).
        BorderPane splashLayout = new BorderPane(splashView);
        Scene splashScene = new Scene(splashLayout);
        splashStage.setScene(splashScene);

        // 14.
        // Time to animate! A 'FadeTransition' is a ready-made tool from JavaFX that handles fading.
        // We tell it what to fade ('splashLayout') and for how long ('Duration.seconds(2)').
        // Then we give it the start ('setFromValue(0)' for invisible) and end ('setToValue(1)' for visible) points.
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), splashLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // 15.
        // We create a second animation for the fade-out, doing the reverse.
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), splashLayout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        // 16.
        // This is where we connect our animations. 'setOnFinished' is how we say, "When you finish this task...".
        // So, this line reads: "When the 'fadeIn' animation finishes, run this little piece of code: 'fadeOut.play()'".
        // The 'e ->' part is the lambda syntax; 'e' holds data about the event, which we don't need, so we ignore it.
        fadeIn.setOnFinished(e -> fadeOut.play());

        // 17.
        // We do the same for the fade-out. "When the 'fadeOut' animation finishes, do these two things:
        // first, close the splash window, and second, run the 'onFinished' code that we were given."
        // We use curly braces '{}' because we have more than one instruction to execute.
        fadeOut.setOnFinished(e -> {
            splashStage.close();
            onFinished.run();
        });

        // 18.
        // This is the starting gun. We make the window visible ('show()') and start the first animation ('play()').
        // Our chain of events will take care of the rest automatically.
        splashStage.show();
        fadeIn.play();
    }

    // 4.
    // Our custom method for building the main menu window.
    private void showMainWindow(Stage stage) {
        // We directly use the 'stage' object that was passed into this method.
        stage.setTitle("Trench Run Tetris");

        // 5.
        // A 'VBox' is a simple layout tool that stacks things vertically. We're telling it to leave 20 pixels of space between items.
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: black;");

        // 6.
        // We create our three button objects from the Button blueprint.
        Button playButton = new Button("Play");
        Button highScoresButton = new Button("High Scores");
        Button exitButton = new Button("Exit");

        // 7.
        // A 'String' is a variable type that holds text. Here, we're storing a block of CSS
        // (web styling code) to make our buttons look consistent without repeating ourselves.
        String buttonStyle = "-fx-font-size: 20px; -fx-background-color: #555; -fx-text-fill: yellow;";
        playButton.setStyle(buttonStyle);
        highScoresButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        // 8.
        // --- Add Button Actions ---
        // Now we make the buttons do something. The '.setOnAction()' method attaches an action
        // to the "on click" event of a button. We use another lambda here to define that action.
        playButton.setOnAction(e -> System.out.println("Play button clicked! Game screen coming in Session 2..."));

        highScoresButton.setOnAction(e -> System.out.println("High Scores button clicked! This will be implemented in a later session."));

        exitButton.setOnAction(e -> javafx.application.Platform.exit());


        // 9.
        // Every layout has a list of its contents, which we can get with 'getChildren()'.
        // The '.addAll(...)' method lets us add all our buttons to that list in one go.
        menuLayout.getChildren().addAll(playButton, highScoresButton, exitButton);

        // 10.
        // We create our main 'Scene', putting our VBox layout inside it and setting the window size.
        // Then we set this scene to be the one our main window displays.
        Scene mainScene = new Scene(menuLayout, 800, 600);
        stage.setScene(mainScene);
        stage.show();
    }

    // 2.
    // This is the universal starting point for any Java program.
    // 'static' means this method belongs to the 'MainApp' class blueprint itself, not to any one object.
    // This is crucial because it lets Java run this method to kick things off *before* any objects are even created.
    // '(String[] args)' is a placeholder for command-line arguments, which we aren't using but must include.
    public static void main(String[] args) {
        // 'launch(args)' is the official "start" button for a JavaFX application. It's a 'static' method
        // from the 'Application' class we inherited. It does all the background setup and then calls our 'start' method.
        launch(args);
    }
}