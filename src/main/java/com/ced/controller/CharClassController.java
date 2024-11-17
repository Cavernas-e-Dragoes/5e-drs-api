package com.ced.controller;

import com.ced.dto.APIReferenceDTO;
import com.ced.dto.SimpleResponse;
import com.ced.model.CharClass;
import com.ced.repository.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/classes")
public class CharClassController {

    @Autowired
    private ClassesRepository classesRepository;


    @GetMapping
    public ResponseEntity<SimpleResponse> getAllClasses() {
        List<CharClass> classes = classesRepository.findAll();

        List<APIReferenceDTO> summaries = classes.stream()
                .map(cls -> new APIReferenceDTO(
                        cls.index(),
                        cls.name(),
                        "/api/classes/" + cls.index()
                ))
                .collect(Collectors.toList());

        SimpleResponse response = new SimpleResponse(summaries.size(), summaries);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{index}")
    public ResponseEntity<CharClass> getClassByIndex(@PathVariable String index) {
        Optional<CharClass> charClass = classesRepository.findByIndex(index);
        return charClass.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
