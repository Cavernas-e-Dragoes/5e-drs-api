package com.ced.controller;

import com.ced.dto.APIReferenceDTO;
import com.ced.dto.SimpleResponse;
import com.ced.model.Race;
import com.ced.repository.RacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/racas")
public class RaceController {

    @Autowired
    private RacesRepository racesRepository;

    @GetMapping
    public ResponseEntity<SimpleResponse> getAllRaces() {
        List<Race> races = racesRepository.findAll();

        List<APIReferenceDTO> summaries = races.stream()
                .map(race -> new APIReferenceDTO(
                        race.index(),
                        race.name(),
                        "/api/racas/" + race.index()
                ))
                .collect(Collectors.toList());

        SimpleResponse response = new SimpleResponse(summaries.size(), summaries);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{index}")
    public ResponseEntity<Race> getRaceByIndex(@PathVariable String index) {
        Optional<Race> race = racesRepository.findByIndex(index);
        return race.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
