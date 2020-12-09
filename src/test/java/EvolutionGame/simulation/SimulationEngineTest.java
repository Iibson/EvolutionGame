package EvolutionGame.simulation;

import EvolutionGame.data.MapDirection;
import EvolutionGame.data.Vector2d;
import EvolutionGame.map.IWorldMap;
import EvolutionGame.map.WorldMap;
import EvolutionGame.mapElement.animal.Animal;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationEngineTest {

    private List<IWorldMap> worlds = new ArrayList<>();
    private Random random = new Random();

    @Test
    public void testRun() {
        IWorldMap map = new WorldMap(10, 10, 6, 6, 10, 20, 2);
        List<Integer> genes = new ArrayList<>();
        int[] check = {0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 32; i++) {
            genes.add(random.nextInt(8));
            check[genes.get(i)]++;
        }
        for (int i = 0; i < 8; i++) {
            if (check[i] == 0) {
                genes.add(i);
                check[i]++;
            }
        }
        while (genes.size() > 32) {
            int rand = random.nextInt(8);
            if (check[rand] > 1)
                genes.remove((Integer) rand);
        }
        for (int i = 0; i < 10; i++)
            new Animal(map, new Vector2d(0, 0), MapDirection.NORTH, genes, 20);
        worlds.add(map);
        new SimulationEngine(worlds, 50000).run();
    }
}
