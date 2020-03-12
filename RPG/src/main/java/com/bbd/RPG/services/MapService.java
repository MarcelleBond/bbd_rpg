package com.bbd.RPG.services;

import com.bbd.RPG.models.*;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class MapService {
    private final Character empty = '0';
    private final Character wall = '1';
    private final Character door = '$';
    private final Character player = '@';
    private final Random rand = new Random();
    private Character[][] getEmptyMazeWithWalls(int width, int height) {
        int mulWidth = width * 2 + 1;
        int mulHeight = height * 2 + 1;

        Character[][] map = new Character[mulHeight][mulWidth];
        for (int y = 0; y < mulHeight; y++) {
            for (int x = 0; x < mulWidth; x++) {
                if (y % 2 == 0 || x % 2 == 0)
                    map[y][x] = wall;
                else
                    map[y][x] = empty;
            }
        }
        return map;
    }

    private Character[][] getEmptyMaze(int width, int height) {
        Character[][] map = new Character[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
                    map[y][x] = wall;
                else
                    map[y][x] = empty;
            }
        }
        return map;
    }

    public Pair<List<Item>, List<Enemy>> getItemsAndEnemies(Character[][] map){
        List<Enemy> enemies = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        int avaliableSpaces = 0;
        for (Character[] characters : map) {
            for (Character chars : characters) {
                if (chars == '0')
                    avaliableSpaces++;
            }
        }

        // find avaliable space and add enemies to the map
        int enemyAmount = (avaliableSpaces > 10) ? new Random().nextInt((int)(Math.ceil(avaliableSpaces / 10) + 1)) : 1; //new Random().nextInt((int)(Math.ceil(map.length / 3) + 1)) ;
        avaliableSpaces -= enemyAmount;
        System.out.println(enemyAmount);
        while (enemyAmount >= 0) {
            int xPosition = new Random().nextInt(map.length) ;
            int yPosition = new Random().nextInt(map.length) ;

            if (map[xPosition][yPosition] == '0')
            {
                Position position = new Position(xPosition, yPosition);
                Enemy enemy = EnemyFactory.newEnemy(position);
                if (enemy != null)
                    enemies.add(enemy);
                enemyAmount--;
            }
        }

        // find avaliable space add items to the map
        int itemAmount = (avaliableSpaces > 10) ? new Random().nextInt((int)(Math.ceil(avaliableSpaces / 10) + 1)) : 1; //new Random().nextInt((int)(Math.ceil(map.length / 3) + 1)) ;
        while (itemAmount >= 0) {
            int xPosition = new Random().nextInt(map.length) ;
            int yPosition = new Random().nextInt(map.length) ;

            if (map[xPosition][yPosition] == '0')
            {
                Position position = new Position(xPosition, yPosition);
                Item item = ItemFactory.newItem(position);
                if (item != null)
                    items.add(item);
                itemAmount--;
            }
        }
        return Pair.with(items, enemies);
    }

    public Character[][] generateMaze(int width, int height) {
        int mulWidth = width * 2 + 1;
        int mulHeight = height * 2 + 1;

        Character[][] map = getEmptyMazeWithWalls(width, height);
        Position startPos = new Position(rand.nextInt(width) * 2 + 1, 0);
        Position exitPos = new Position(rand.nextInt(width) * 2 + 1, mulHeight - 1);
        map[startPos.y][startPos.x] = door;
        map[exitPos.y][exitPos.x] = door;

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
                map[midPoint.y][midPoint.x] = empty;
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

    public Character[][] generateDivisionMaze(int width, int height) {
        Character[][] map = getEmptyMaze(width, height);
        divide(map, new Position(0, 0), new Position(width - 1, height - 1));
        return map;
    }

    public void divide(Character[][] map, Position low, Position high) {
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
            if (rand.nextInt(2) == 1)
                divideVertically(map, low, high);
            else
                divideHorizontally(map, low, high);
        }
    }

    private int getHolePosition(int low, int high, Position mid, boolean isVert, Character[][] map) {
        int hole;
        boolean noNeighbouringWalls = false;
        do {
            hole = rand.ints(low + 1, high).findFirst().getAsInt();
            if (isVert) {
                noNeighbouringWalls = map[hole][mid.x - 1] == empty && map[hole][mid.x + 1] == empty;
            } else {
                noNeighbouringWalls = map[mid.y - 1][hole] == empty && map[mid.y + 1][hole] == empty;
            }
        } while (!noNeighbouringWalls);
        return hole;
    }

    private Position getWallPosition(Position low, Position high, boolean isVert, Character[][] map) {
        Position mid = null;
        if (isVert){
            mid = new Position((high.x + low.x) / 2, low.y);
            if (map[low.y][mid.x] == empty || map[high.y][mid.x] == empty){
                mid.x += 1;
            }
        }
        else {
            mid = new Position(low.x, (low.y + high.y) / 2);
            if (map[mid.y][low.x] == empty || map[mid.y][high.x] == empty)
                mid.y += 1;
        }
        return mid;
    }

    private void divideVertically(Character[][] map, Position low, Position high) {
        Position mid = getWallPosition(low, high, true, map);
        int hole = getHolePosition(low.y, high.y, mid, true, map);
        for (int y = low.y + 1; y < high.y; y++) {
            if (y != hole)
                map[y][mid.x] = wall;
        }
        divide(map, low, new Position(mid.x, high.y));
        divide(map, new Position(mid.x, low.y), high);
    }

    private void divideHorizontally(Character[][] map, Position low, Position high) {
        Position mid = getWallPosition(low, high, false, map);
        int hole = getHolePosition(low.x, high.x, mid, false, map);
        for (int x = low.x + 1; x < high.x; x++) {
            if (x != hole)
                map[mid.y][x] =wall;
        }
        divide(map, low, new Position(high.x, mid.y));
        divide(map, new Position(low.x, mid.y), high);
    }

    public Position getMazeStartingPosition(Character[][] map) {
        for (int x = 0; x < map[0].length; x++){
            if (map[0][x] == door){
                return new Position(x, 1);
            }
        }
        return new Position(-1, -1);
    }
}
