package hackathon;

public class Maze {
    private String origMaze;

    private int[][] matrix;

    private Position startPos;
    public Maze(int width, int height, String maze) {
        this.origMaze = maze;
        this.matrix = new int[height][width];
        int start = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.matrix[i][j] = Integer.parseInt(String.valueOf(maze.charAt(start)));
                if (this.matrix[i][j] == 2) {
                    this.startPos = new Position(i, j);
                    if (i == 0) {
                        this.startPos.lightDirection = Direction.BOTTOM;
                    } else if (i == height - 1) {
                        this.startPos.lightDirection = Direction.TOP;
                    } else if (j == 0) {
                        this.startPos.lightDirection = Direction.RIGHT;
                    } else if (j == width - 1) {
                        this.startPos.lightDirection = Direction.LEFT;
                    }
                }
                start ++;
            }
        }
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public Position getStartPos() {
        return startPos;
    }

    public String getOrigMaze() {
        return origMaze;
    }
}
