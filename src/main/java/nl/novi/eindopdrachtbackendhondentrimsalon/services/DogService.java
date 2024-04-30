package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DogService {
    private final DogRepository dogRepository;

    @Autowired
    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Dog updateDogCharacteristics (Long dogId, String name, String breed, int age) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new RecordNotFoundException("Dog not found with id: " + dogId));

        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);

        return dogRepository.save(dog);
    }
}
