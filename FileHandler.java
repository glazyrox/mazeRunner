package maze;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileHandler {

    public void saveMazeFile(String fileName, Maze maze) {
        this.createDirectory();
        File file = new File("output/" + fileName);

        try (PrintWriter printWriter = new PrintWriter(file)) {

            for (int i = 0; i < maze.mazeMap.length; i++) {
                printWriter.println(Arrays.toString(
                        maze.mazeMap[i])
                        .replace("[", "")
                        .replace("]", "")
                        + ";");
            }
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }


    }

    private void createDirectory() {
        File directory = new File("output");

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public void loadMazeFile(String fileName, Maze maze) {
        String pathToHelloWorldJava = "output/" + fileName;

        try {
            String file = readFileAsString(pathToHelloWorldJava);
            String[] lines = file.split(";");
            int[][] mazeMap = new int[lines.length - 1][lines.length - 1];

            for (int i = 0; i < mazeMap.length; i++) {
                String[] line = lines[i]
                        .replace("\n", "")
                        .replace(" ", "")
                        .split(",");
                mazeMap[i] = new int[line.length];

                for (int j = 0; j < line.length; j++) {
                    mazeMap[i][j] = Integer.parseInt(line[j]);
                }
            }

            maze.setLoadedMaze(mazeMap);
        } catch (IOException e) {
            System.out.println("The file " + e.getMessage() + " does not exist");
        }
    }
}
