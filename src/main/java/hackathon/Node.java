package hackathon;
class Node implements Comparable<Node> {
    public Position pos;
    public int cost;

    public int[][] modiMatrix;

    public Node(Position pos, int cost, int[][] modiMatrix)
    {
        this.pos = pos;
        this.cost = cost;
        this.modiMatrix = modiMatrix;
    }

    @Override
    public int compareTo(Node n) {
        return Integer.compare(this.cost, n.cost);
        }

}