package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dogs")
public class DogController {

    private final DogService dogService;

    @Autowired
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PutMapping("/{dogId}/characteristics")
    public ResponseEntity<Dog> updateDogCharacteristics(@PathVariable Long dogId, @RequestParam String name, @RequestParam String breed, @RequestParam int age) {
        Dog updatedDog = dogService.updateDogCharacteristics(dogId, name, breed, age);
        return ResponseEntity.ok(updatedDog);
    }


}
