package hackathon;

public class Position {
    int row;
    int col;

    Direction lightDirection;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(int row, int col, Direction lightDirection) {
        this.row = row;
        this.col = col;
        this.lightDirection = lightDirection;
    }
}
