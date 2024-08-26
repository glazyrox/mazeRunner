package maze;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuKeeper {
    FileHandler fileHandler = new FileHandler();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FULL_MENU_TEXT = """
            === Menu ===
            1. Generate a new maze.
            2. Load a maze.
            3. Save the maze.
            4. Display the maze.
            5. Find the escape.
            0. Exit.
            """;
    private static final String INIT_MENU_TEXT = """
            === Menu ===
            1. Generate a new maze
            2. Load a maze
            0. Exit
            """;
    private Maze maze;

    public MenuKeeper(Maze maze) {
        this.maze = maze;
    }

    public void showMenu() {;
        if (maze.isMazeAvailable) {
            System.out.println(FULL_MENU_TEXT);
        } else {
            System.out.println(INIT_MENU_TEXT);
        }

        try {
            this.resolveUserAnswer(scanner.nextInt());
        } catch (InputMismatchException e) {
            this.resolveUserAnswer(404);
        }
    }

    private void generateMaze(boolean isRepeat) {
        if (!isRepeat) {
            System.out.println("Enter the size of a new maze");
        }

        String userInput = scanner.next();
        int size = 0;

        try {
            size = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            System.out.println("Incorrect option. Please try again;");
            generateMaze(true);
            return;
        }

        maze.initMaze(size, size);
        maze.showMaze(maze.mazeMap);
    }

    private void saveMaze() {
        scanner.nextLine();
        String filename = scanner.nextLine();
        fileHandler.saveMazeFile(filename, maze);
    }

    private void loadMaze() {
        scanner.nextLine();
        String filename = scanner.nextLine();
        fileHandler.loadMazeFile(filename, maze);
    }

    private void resolveUserAnswer(int number) {
        switch (number) {
            case 1: {
                this.generateMaze(false);
                this.showMenu();
                break;
            }
            case 2: {
                this.loadMaze();
                this.showMenu();
                break;
            }
            case 3: {
                this.saveMaze();
                this.showMenu();
                break;
            }
            case 4: {
                maze.showMaze(maze.mazeMap);
                this.showMenu();
                break;
            }
            case 5: {
                maze.findEscape();
                this.showMenu();
                break;
            }
            case 0: {
                return;
            }
            case 404:
            default: {
                System.out.println("Incorrect option");
                this.showMenu();
            }
        }
    }
}
