package EvolutionGame.simulation;

import EvolutionGame.data.MapDirection;
import EvolutionGame.data.Vector2d;
import EvolutionGame.map.IWorldMap;
import EvolutionGame.map.WorldMap;
import EvolutionGame.map.visualisation.IMapVisualiserObserver;
import EvolutionGame.map.visualisation.MapVisualiser;
import EvolutionGame.mapElement.animal.Animal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.*;

class SimulationEngine {

    private IWorldMap world;
    private int year;
    MapVisualiser mapVisualiser;
    private int saveTime;
    private File file;

    SimulationEngine(Integer width, Integer height, Integer jungleWidth, Integer jungleHeight, Integer startEnergy, Integer moveEnergy, Integer plantEnergy, int plantSpawnRatio, int numberOfStartingAnimals, int shift, int saveTime) {
        Vector2d mapBounds = new Vector2d((width / 2), (height / 2));
        Vector2d jungleBounds = new Vector2d((jungleWidth / 2), (jungleHeight / 2));
        mapVisualiser = new MapVisualiser(mapBounds, jungleBounds, shift);
        this.world = new WorldMap(mapBounds, jungleBounds, plantEnergy, startEnergy, plantSpawnRatio, moveEnergy, mapVisualiser);
        mapVisualiser.addIWorldMap(this.world);
        Random random = new Random();
        List<Integer> genes = new ArrayList<>();
        this.year = 0;
        this.saveTime = saveTime;
        for (int i = 0; i < 8; i++)
            genes.add(i);
        for (int i = 8; i < 32; i++)
            genes.add(random.nextInt(8));
        for (int i = 0; i < numberOfStartingAnimals; i++)
            new Animal(world, new Vector2d(random.nextInt(width) - width / 2, random.nextInt(height) - height / 2), MapDirection.NORTH, genes, startEnergy);
        try {
            file = new File("simulation.txt");
            if (file.delete())
                System.out.println("file deleted");
            if (file.createNewFile())
                System.out.println("file created");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    void simulateAYear() {
        if (this.year % this.saveTime == 0)
            writeToFileInfo();
        world.spentYear();
        this.year++;
    }

    void clearVisualiser() {
        this.mapVisualiser.clearMap();
    }

    String getInfo() {
        return "Years: " + this.year +
                ",\n Animals: " + this.world.getCurrentNumberOfAnimals() +
                ",\n Plants: " + this.world.getCurrentNumberOfPlants() +
                ",\n Average Energy: " + (int) this.world.getAverageEnergy() +
                ",\n Average Life Span: " + (int) this.world.getAverageYears() +
                ",\n Average Children: " + (int) this.world.getAverageOffspringsNumber() +
                "\n\n";
    }

    String getInfoAboutChosenAnimal() {
        return this.mapVisualiser.getInfoAboutChosenAnimal();
    }

    private void writeToFileInfo() {
        try {
            Files.write(Paths.get("simulation.txt"), getInfo().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

}