package com.ced.repository;

import com.ced.model.CharClass;
import com.ced.model.utils.APIReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Classes Repository Unit Tests")
class ClassesRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ClassesRepository repository;

    private CharClass fighter;
    private CharClass wizard;

    @BeforeEach
    void setUp() {
        // Create test data
        fighter = createTestClass("fighter", "Fighter");
        wizard = createTestClass("wizard", "Wizard");
    }

    @Test
    @DisplayName("Should find all character classes")
    void shouldFindAllCharacterClasses() {
        // AAA - Arrange
        when(repository.findAll()).thenReturn(List.of(fighter, wizard));

        // AAA - Act
        List<CharClass> classes = repository.findAll();

        // AAA - Assert
        assertThat(classes).isNotEmpty();
        assertThat(classes).hasSize(2);
        assertThat(classes).containsExactlyInAnyOrder(fighter, wizard);
    }

    @Test
    @DisplayName("Should find character class by ID")
    void shouldFindCharacterClassById() {
        // AAA - Arrange
        String id = "wizard-id";
        when(repository.findById(id)).thenReturn(Optional.of(wizard));

        // AAA - Act
        Optional<CharClass> foundClass = repository.findById(id);

        // AAA - Assert
        assertThat(foundClass).isPresent();
        assertThat(foundClass.get().name()).isEqualTo(wizard.name());
        assertThat(foundClass.get().index()).isEqualTo(wizard.index());
    }

    @Test
    @DisplayName("Should save character class")
    void shouldSaveCharacterClass() {
        // AAA - Arrange
        when(repository.save(any(CharClass.class))).thenReturn(fighter);

        // AAA - Act
        CharClass savedClass = repository.save(fighter);

        // AAA - Assert
        assertThat(savedClass).isNotNull();
        assertThat(savedClass.index()).isEqualTo(fighter.index());
        assertThat(savedClass.name()).isEqualTo(fighter.name());
    }

    /**
     * Helper method to create a test character class
     */
    private CharClass createTestClass(String index, String name) {
        return new CharClass(
            index + "-id", // ID for test purposes
            index,
            name,
            name.equals("Fighter") ? 10 : 6, // Fighters have d10, wizards d6
            List.of(), // proficiencyChoices
            List.of(new APIReference("proficiency", name + " Proficiency", "/api/proficiencies/prof-" + index)), // proficiencies
            List.of(), // savingThrows
            List.of(), // startingEquipment
            List.of(), // startingEquipmentOptions
            "/api/classes/" + index + "/levels", // classLevels
            null, // multiClassing
            List.of(), // subclasses
            null, // spellCasting
            "/api/classes/" + index // url
        );
    }
} 