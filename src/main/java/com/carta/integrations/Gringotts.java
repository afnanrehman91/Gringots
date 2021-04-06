package com.carta.integrations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Gringotts {
    private static final String USD = "USD";

    public List<String> process(String file) {
        return findMaxAverages(readGringottsData(file));
    }

    private Map<String, Double> readGringottsData(String fileName) {
        List<String> list = new ArrayList<>();
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            list = stream
                    .filter(line -> {
                        if(line.length() == 36 && line.contains("-")){
                            return false;
                        }
                        if(!line.startsWith("GTTB")){
                            return true;
                        }
                        return false;
                    })
                    .map(x -> x.contains(USD)? x.replace(USD,""):x)
                    .collect(Collectors.toList());
      for(int i = 1 ; i <= list.size() ; i=i+2){
          list.remove(i);
      }
      for(int i = 1 ; i <= list.size() ; i++){
              if (i % 2 != 0) {
                  if(!map.containsKey(list.get(i - 1))){
                      map.put(list.get(i - 1), new ArrayList<>());
                  }
              } else {
                  map.get(list.get(i - 2)).add(new Integer(list.get(i - 1)));
              }
      }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calculateAverage(map);
    }

    private List<String> findMaxAverages(Map<String, Double> mapGroup) {
        if(mapGroup.isEmpty())
            return Collections.emptyList();
        Double max = mapGroup.values().stream().max(Comparator.naturalOrder()).get();
        return mapGroup.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<String, Double> calculateAverage(Map<String, List<Integer>> map){
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e-> e.getValue()
                                .stream()
                                .mapToDouble(Integer::doubleValue)
                                .average()
                                .getAsDouble()));
    }
}