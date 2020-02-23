package com.bbd.RPG.services;

import com.bbd.RPG.models.Position;
import com.bbd.RPG.models.Vector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MapService {

    public List<List<Character>> generateTestMaze(int width, int height) {
        List<List<Character>> map = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            List<Character> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    row.add('1');
                } else {
                    row.add('0');
                }
            }
            map.add(row);
        }
        return map;
    }

    private List<List<Character>> getEmptyMazeWithWalls(int width, int height) {
        int mulWidth = width * 2 + 1;
        int mulHeight = height * 2 + 1;
        List<List<Character>> map = new ArrayList<>();
        for (int y = 0; y < mulHeight; y++) {
            List<Character> row = new ArrayList<>();
            for (int x = 0; x < mulWidth; x++) {
                if (y % 2 == 0 || x % 2 == 0)
                    row.add('1');
                else
                    row.add('0');
            }
            map.add(row);
        }
        return map;
    }

    private List<List<Character>> getEmptyMaze(int width, int height) {
        List<List<Character>> map = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            List<Character> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
                    row.add('1');
                else
                    row.add('0');
            }
            map.add(row);
        }
        return map;
    }

    public List<List<Character>> generateMaze(int width, int height) {
        int mulWidth = width * 2 + 1;
        int mulHeight = height * 2 + 1;
        List<List<Character>> map = getEmptyMazeWithWalls(width, height);
        Random rand = new Random();
        Position startPos = new Position(rand.nextInt(width) * 2 + 1, 0);
        Position exitPos = new Position(rand.nextInt(width) * 2 + 1, mulHeight - 1);
        map.get(startPos.y).set(startPos.x, '$');
        map.get(exitPos.y).set(exitPos.x, '$');

        Vector[] dirs = new Vector[]{new Vector(0, -2), new Vector(0, 2), new Vector(-2, 0), new Vector(2, 0)};
        List<Position> visited = new ArrayList<>();
        visited.add(startPos);
        List<Position> stack = new ArrayList<>();
        Position currPos = new Position(rand.nextInt(width) * 2 + 1, rand.nextInt(height) * 2 + 1);
        while (true) {
            List<Position> neighbours = new ArrayList<>();
            for (Vector dir : dirs) {
                Position newPos = new Position(currPos.x + dir.x, currPos.y + dir.y);
                if (newPos.x >= 0 && newPos.x < mulWidth &&
                        newPos.y >= 0 && newPos.y < mulHeight &&
                        !isVisited(visited, newPos)) {
                    neighbours.add(newPos);
                }
            }
            System.out.println(neighbours);
            if (neighbours.isEmpty()) {
                if (stack.isEmpty()) {
                    return map;
                }
                currPos = stack.remove(stack.size() - 1);
            } else {
                Position tempPos = neighbours.get(rand.nextInt(neighbours.size()));
                Position midPoint = new Position(currPos.x, currPos.y);
                midPoint.x += Integer.compare(tempPos.x, currPos.x);
                midPoint.y += Integer.compare(tempPos.y, currPos.y);
                map.get(midPoint.y).set(midPoint.x, '0');
                currPos = tempPos;
                visited.add(currPos);
                stack.add(currPos);
            }
        }
    }

    private boolean isVisited(List<Position> visited, Position position) {
        for (Position visitedPos : visited) {
            if (visitedPos.x == position.x && visitedPos.y == position.y)
                return true;
        }
        return false;
    }

    public List<List<Character>> generateDivisionMaze(int width, int height) {
        List<List<Character>> map = getEmptyMaze(width, height);
        divide(map, new Position(0, 0), new Position(width - 1, height - 1));
        return map;
    }

    public void divide(List<List<Character>> map, Position low, Position high) {
        System.out.println(low);
        System.out.println(high);
        System.out.println("==========");
        int yDiff = high.y - low.y;
        int xDiff = high.x - low.x;
        if (yDiff <= 4 || xDiff <= 4 || yDiff * xDiff < 150)
            return;
        else if (yDiff <= 5 && xDiff > 5)
            divideVertically(map, low, high);
        else if (xDiff <= 5 && yDiff > 5)
            divideHorizontally(map, low, high);
        else if (xDiff <= 5 && yDiff <= 5)
            return;
        else {
            Random rand = new Random();
            if (rand.nextInt(2) == 1)
                divideVertically(map, low, high);
            else
                divideHorizontally(map, low, high);
        }
    }

    private void divideVertically(List<List<Character>> map, Position low, Position high) {
        Random r = new Random();
        int hole = r.ints(low.y + 1, high.y).findFirst().getAsInt();
        Position mid = new Position((high.x + low.x) / 2, low.y);
        for (int y = low.y + 1; y < high.y; y++) {
            if (y != hole)
                map.get(y).set(mid.x, '1');
        }
        divide(map, low, new Position((high.x + low.x) / 2, high.y));
        divide(map, new Position((high.x + low.x) / 2, low.y), high);
    }

    private void divideHorizontally(List<List<Character>> map, Position low, Position high) {
        Random r = new Random();
        int hole = r.ints(low.x + 1, high.x).findFirst().getAsInt();
        Position mid = new Position(low.x, (low.y + high.y) / 2);
        for (int x = low.x + 1 ; x < high.x; x++) {
            if (x != hole)
                map.get(mid.y).set(x, '1');
        }
        divide(map, low, new Position(high.x, (low.y + high.y) / 2));
        divide(map, new Position(low.x, (low.y + high.y) / 2), high);
    }
}
