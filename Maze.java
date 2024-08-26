package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Maze {
    private static final Random random = new Random();
    private static final String WALL = "\u2588\u2588";
    private static final String PASSAGE = "  ";
    private static final String WAY_OUT = "//";
    private static final int OPEN = 0;
    private static final int CLOSE = 1;
    private static final int[] DX = {0, 2, 0, -2};
    private static final int[] DY = {-2, 0, 2, 0};

    int[][] mazeMap;
    int[][] resolvedMaze;
    int width = 0;
    int height = 0;

    int[] enterXY = {};
    int[] exitXY = {};

    public boolean isMazeAvailable = false;

    public void initMaze(int height, int width) {
        this.fillWallMaze(height, width);
        this.generateMaze();
        this.isMazeAvailable = true;
    }

    private void fillWallMaze(int height, int width) {
        this.mazeMap = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                mazeMap[i][j] = CLOSE;
            }
        }

        this.width = width;
        this.height = height;

//      System.out.println(Arrays.deepToString(mazeMap));
    }

    public void showMaze(int[][] mazeMap) {
        for (int i = 0; i < mazeMap.length; i++) {
            for (int j = 0; j < this.mazeMap[i].length; j++) {
                if (mazeMap[i][j] == 1) {
//                    System.out.print(j);
                    System.out.print(WALL);
                } else if (mazeMap[i][j] == 7) {
                    System.out.print(WAY_OUT);
                } else if (mazeMap[i][j] == 0) {
//                    System.out.print(j);
                    System.out.print(PASSAGE);
                }
            }
            System.out.println();
        }
    }

    public void generateMaze() {
        int startX = generateRandomOddNumberInRange(1, width - 1);
        int startY = generateRandomOddNumberInRange(1, height - 1);

        mazeMap[startY][startX] = OPEN;
        List<int[]> walls = new ArrayList<>();

        addWalls(mazeMap, walls, startX, startY);

        while (!walls.isEmpty()) {
            int[] wall = walls.remove(random.nextInt(walls.size()));

            int wx = wall[1];
            int wy = wall[0];

            mazeMap[wy][wx] = OPEN;

            connectWithOpenCell(mazeMap, wx, wy);
            addWalls(mazeMap, walls, wx, wy);
//            showMaze();
//            System.out.println();

//            System.out.println("wx " + wx);
//            System.out.println("wy " + wy);
        }
        this.createEnter(mazeMap);
        this.createExit(mazeMap);
    }

    private static void addWalls(int[][] mazeMap, List<int[]> walls, int x, int y) {

        for (int i = 0; i < 4; i++) {
            int wx = x + DX[i];
            int wy = y + DY[i];

            boolean isPositive = wx > 0 && wy > 0;
            boolean isInMaze = wx < mazeMap[0].length - 1 && wy < mazeMap.length - 1;

            if (isPositive && isInMaze) {
                boolean isWall = mazeMap[wy][wx] == CLOSE;

                if (isWall) {
                    walls.add(new int[]{wy, wx});
                }
            }
        }
    }

    private void connectWithOpenCell(int[][] maze, int x, int y) {
        for (int i = 0; i < 4; i++) {
            int nx = x + DX[i];
            int ny = y + DY[i];

//            System.out.println();
//            System.out.println("NX " + nx);
//            System.out.println("NY " + ny);

            boolean isPositive = nx >= 0 && ny >= 0;
            boolean isInMaze = nx < maze[0].length && ny < maze.length;

            if (isPositive && isInMaze && maze[ny][nx] == OPEN) {

                maze[y + DY[i] / 2][x + DX[i] / 2] = OPEN;
                break;
            }
        }
    }

    private void createEnter(int[][] mazeMap) {
        mazeMap[1][0] = OPEN;
        mazeMap[1][1] = OPEN;
        this.enterXY = new int[]{1, 0};

        System.out.println("1 " + Arrays.toString(this.enterXY));
    }

    private void createExit(int[][] mazeMap) {
        mazeMap[mazeMap.length - 2][mazeMap.length - 1] = OPEN;
        this.exitXY = new int[]{mazeMap.length - 2, mazeMap.length - 1};
        System.out.println("2 " + Arrays.toString(this.exitXY));

    }

    public static int generateRandomOddNumberInRange(int lowerBound, int upperBound) {
        Random random = new Random();

        // Корректируем границы, чтобы они были нечетными
        if (lowerBound % 2 == 0) {
            lowerBound++;
        }
        if (upperBound % 2 == 0) {
            upperBound--;
        }

        // Генерируем случайное число в диапазоне от скорректированных lowerBound до upperBound (включительно)

        return lowerBound + 2 * random.nextInt((upperBound - lowerBound) / 2 + 1);
    }

    public void findEscape() {
        int startX = this.enterXY[0], startY = this.enterXY[1];
        int endX = this.exitXY[0], endY = this.exitXY[1];

        List<int[]> path = MazeResolver.bfs(mazeMap, startX, startY, endX, endY);

//        // Вывод результата
//        if (!path.isEmpty()) {
//            System.out.println("Path found:");
//            for (int[] p : path) {
//                System.out.println(Arrays.toString(p));
//            }
//        } else {
//            System.out.println("No path found.");
//        }

        resolvedMaze = new int[height][width];

        for (int i = 0; i < mazeMap.length; i++) {
            for (int j = 0; j < mazeMap[i].length; j++) {
                resolvedMaze[i][j] = mazeMap[i][j];
            }
        }

        for (int[] p : path) {
            resolvedMaze[p[0]][p[1]] = 7;
        }

        this.showMaze(resolvedMaze);
    }

    public void setLoadedMaze(int[][] mazeMap) {
        this.isMazeAvailable = true;
        this.mazeMap = mazeMap;
        this.height = this.mazeMap.length;
        this.width = this.mazeMap.length;

        for (int i = 0; i < mazeMap.length; i++) {
            if (mazeMap[i][0] == OPEN) {
                this.enterXY = new int[]{i, 0};
                break;
            }
        }

        for (int i = 0; i < mazeMap.length; i++) {
            if (mazeMap[i][mazeMap.length - 1] == OPEN) {
                this.exitXY = new int[]{i, mazeMap.length - 1};
                break;
            }
        }
    }
}
