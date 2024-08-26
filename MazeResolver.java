package maze;

import java.util.*;

public class MazeResolver {
    // Направления движения: вверх, вниз, влево, вправо
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

    // Метод для поиска выхода из лабиринта с использованием BFS
    public static List<int[]> bfs(int[][] maze, int startX, int startY, int endX, int endY) {
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        int[][] parent = new int[rows * cols][2]; // Хранение координат родителей

        // Инициализация массива родителей значением [-1, -1]
        for (int[] p : parent) {
            Arrays.fill(p, -1);
        }

        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            // Если достигли выхода
            if (x == endX && y == endY) {
                return reconstructPath(parent, startX, startY, endX, endY, cols);
            }

            // Проходим по всем соседним клеткам
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (nx >= 0 && ny >= 0 && nx < rows && ny < cols && maze[nx][ny] == 0 && !visited[nx][ny]) {
                    queue.add(new int[]{nx, ny});
                    visited[nx][ny] = true;
                    parent[nx * cols + ny] = new int[]{x, y}; // Запоминаем родителя
                }
            }
        }

        // Если путь не найден, возвращаем пустой список
        return Collections.emptyList();
    }

    // Метод для восстановления пути от конца до начала
    private static List<int[]> reconstructPath(int[][] parent, int startX, int startY, int endX, int endY, int cols) {
        List<int[]> path = new ArrayList<>();
        int[] step = {endX, endY};

        while (!(step[0] == startX && step[1] == startY)) {
            path.add(step);
            int[] prev = parent[step[0] * cols + step[1]];
            step = prev;
        }
        path.add(new int[]{startX, startY}); // Добавляем стартовую точку
        Collections.reverse(path); // Переворачиваем путь для получения правильного порядка
        return path;
    }
}
