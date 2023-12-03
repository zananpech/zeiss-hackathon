package hackathon;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {

		Maze maze = new Maze(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
		Map<String, Integer> reflectionCost = getReflectionCost();
		Map<String, Position> newPositionalDirectionDueToFixedMirror = getPositionalDirectionDueToFixedMirror();
		List<Position> fourDirectionalPositions = getFourDirectionalPositions();

		Position startPos = maze.getStartPos();
		PriorityQueue<Node> pq = new PriorityQueue<>();
		pq.add(new Node(startPos, 1, maze.getMatrix()));


		Set<String> visited = new HashSet<>();
		visited.add(Integer.toString(startPos.row) + startPos.col);

		while (!pq.isEmpty()) {
			Node currNode = pq.poll();

			if (reachExit(maze.getMatrix(), currNode.pos.row, currNode.pos.col)) {
				String output = createStringRepresentationFromMatrix(currNode.modiMatrix);
				System.out.println(output);
			}

			int currNodeValue = maze.getMatrix()[currNode.pos.row][currNode.pos.col];

			if (currNodeValue == 6 || currNodeValue == 7) {
				Position newPosDirection = newPositionalDirectionDueToFixedMirror.get(Integer.toString(currNodeValue)
						+ currNode.pos.lightDirection);
				Position newPos = calculateNewPositionDueToFixedMirror(currNode.pos, newPosDirection);
				if (validMove(newPos.row, newPos.col, maze.getMatrix(), visited)) {
					visited.add(Integer.toString(newPos.row) + newPos.col);
					Node newNode = new Node(newPos, currNode.cost + 1, currNode.modiMatrix);
					pq.add(newNode);
				}
			} else {
				for (Position fourDirectionalPos: fourDirectionalPositions) {
					Position newPos = calculateNewPositionDueToFixedMirror(currNode.pos, fourDirectionalPos);
					if (validMove(newPos.row, newPos.col, maze.getMatrix(), visited)) {
						int newCost = currNode.cost +
								reflectionCost.get(currNode.pos.lightDirection.name()
												+ newPos.lightDirection.name());

						int[][] cloneMatrix = createCloneMatrix(currNode.modiMatrix);
						if(currNodeValue != 2) {
							cloneMatrix[currNode.pos.row][currNode.pos.col] =
									reflectionCost.get(currNode.pos.lightDirection.name()
									+ newPos.lightDirection.name()) - 1;
						}

						visited.add(Integer.toString(newPos.row) + newPos.col);
						Node newNode = new Node(newPos, newCost, cloneMatrix);
						pq.add(newNode);
					}
				}
			}
		}
	}

	public static boolean validMove(int row, int col, int[][] matrix, Set<String> visited) {
		return row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length && matrix[row][col] != 1
				&& !visited.contains(Integer.toString(row) + col);
	}

	public static boolean reachExit(int[][] matrix, int row, int col) {
		return matrix[row][col] == 3;
	}
	public static Map<String, Integer> getReflectionCost() {
		Map<String, Integer> reflectionMap = new HashMap<>();
		reflectionMap.put("LEFTTOP", 6);
		reflectionMap.put("RIGHTTOP", 5);
		reflectionMap.put("LEFTBOTTOM", 5);
		reflectionMap.put("RIGHTBOTTOM", 6);
		reflectionMap.put("TOPRIGHT", 5);
		reflectionMap.put("TOPLEFT", 6);
		reflectionMap.put("BOTTOMRIGHT", 6);
		reflectionMap.put("BOTTOMLEFT", 5);
		reflectionMap.put("LEFTLEFT", 1);
		reflectionMap.put("RIGHTRIGHT", 1);
		reflectionMap.put("TOPTOP", 1);
		reflectionMap.put("BOTTOMBOTTOM", 1);
		return reflectionMap;
	}

	public static Map<String, Position> getPositionalDirectionDueToFixedMirror() {
		Map<String, Position> map = new HashMap<>();
		map.put("6RIGHT", new Position(-1, 0, Direction.TOP));
		map.put("6BOTTOM", new Position(0, -1, Direction.LEFT));
		map.put("6LEFT", new Position(1, 0, Direction.BOTTOM));
		map.put("6TOP", new Position(0, 1, Direction.RIGHT));
		map.put("7RIGHT", new Position(1, 0, Direction.BOTTOM));
		map.put("7BOTTOM", new Position(0, 1, Direction.RIGHT));
		map.put("7LEFT", new Position(-1, 0, Direction.TOP));
		map.put("7TOP", new Position(0, -1, Direction.LEFT));
		return map;
	}

	public static Position calculateNewPositionDueToFixedMirror(Position currPos, Position newPosDirection) {
        return new Position(currPos.row + newPosDirection.row, currPos.col + newPosDirection.col,
				newPosDirection.lightDirection);
	}

	public static List<Position> getFourDirectionalPositions() {
		List<Position> positions = new ArrayList<>();
		positions.add(new Position(1, 0, Direction.BOTTOM));
		positions.add(new Position(-1, 0, Direction.TOP));
		positions.add(new Position(0, 1, Direction.RIGHT));
		positions.add(new Position(0, -1, Direction.LEFT));
		return positions;
	}

	public static int[][] createCloneMatrix(int[][] matrix) {
		int[][] clone = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) clone[i] =
				matrix[i].clone();
		return clone;
	}

	public static String createStringRepresentationFromMatrix(int[][] matrix) {
		return Arrays.stream(matrix)
				.map(ints -> IntStream.of(ints).mapToObj(String::valueOf)
						.collect(Collectors.joining("")))
				.collect(Collectors.joining(""));
	}
}