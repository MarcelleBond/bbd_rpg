package com.bbd.RPG.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {
    public List<List<Character>> generateMaze(int width, int height){
        List<List<Character>> map = new ArrayList<>();
        for (int y = 0; y < height; y++){
            List<Character> row = new ArrayList<>();
            for (int x = 0; x < width; x++){
                if (x == 0 || x == width-1 || y == 0 || y == height - 1){
                    row.add('1');
                } else {
                    row.add('0');
                }
            }
            map.add(row);
        }
        return map;
    }

}
