package maze;

public class Main {
    public static void main(String[] args) {
        MenuKeeper menuKeeper = new MenuKeeper(new Maze());
        menuKeeper.showMenu();;
    }
}
