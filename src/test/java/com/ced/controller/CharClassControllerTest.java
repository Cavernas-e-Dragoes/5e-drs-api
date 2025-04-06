package com.ced.controller;

import com.ced.dto.APIReferenceDTO;
import com.ced.dto.Response;
import com.ced.model.CharClass;
import com.ced.model.utils.APIReference;
import com.ced.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ced.util.ResponseBuilder.buildResponse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharClassController Tests")
class CharClassControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClassService classService;

    @InjectMocks
    private CharClassController controller;

    private static final String CLASSES_ENDPOINT = "/api/classes";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Nested
    @DisplayName("Get all classes tests")
    class GetAllClassesTests {

        @Test
        @DisplayName("Should return all classes with pagination")
        void shouldReturnAllClassesWithPagination() throws Exception {
            // AAA - Arrange
            List<CharClass> classes = List.of(
                    createMockClass("barbarian", "Barbarian"),
                    createMockClass("wizard", "Wizard")
            );
            Page<CharClass> page = new PageImpl<>(classes, PageRequest.of(0, 10), classes.size());

            when(classService.getAll(any(Pageable.class))).thenReturn(page);

            // Create the expected response 
            List<APIReferenceDTO> apiReferences = classes.stream()
                    .map(c -> new APIReferenceDTO(c.index(), c.name(), CLASSES_ENDPOINT + "/" + c.index()))
                    .collect(Collectors.toList());

            Response mockResponse = new Response(
                    classes.size(),
                    apiReferences,
                    1, // totalPages
                    0  // currentPage
            );

            try (MockedStatic<com.ced.util.ResponseBuilder> mockedStatic = mockStatic(com.ced.util.ResponseBuilder.class)) {
                mockedStatic.when(() -> buildResponse(any(), anyString())).thenReturn(mockResponse);

                // AAA - Act & Assert
                mockMvc.perform(get(CLASSES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.count", equalTo(classes.size())))
                        .andExpect(jsonPath("$.results", hasSize(classes.size())))
                        .andExpect(jsonPath("$.results[0].index", equalTo("barbarian")))
                        .andExpect(jsonPath("$.results[1].index", equalTo("wizard")));
            }
        }
    }

    @Nested
    @DisplayName("Get class by index tests")
    class GetClassByIndexTests {

        @Test
        @DisplayName("Should return class when it exists")
        void shouldReturnClassWhenItExists() throws Exception {
            // AAA - Arrange
            String classIndex = "wizard";
            CharClass wizardClass = createMockClass(classIndex, "Wizard");

            when(classService.getByIndex(classIndex)).thenReturn(Optional.of(wizardClass));

            // AAA - Act & Assert
            mockMvc.perform(get(CLASSES_ENDPOINT + "/{classe}", classIndex)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.index", equalTo(classIndex)))
                    .andExpect(jsonPath("$.name", equalTo("Wizard")));
        }

        @Test
        @DisplayName("Should return 404 when class does not exist")
        void shouldReturn404WhenClassDoesNotExist() throws Exception {
            // AAA - Arrange
            String nonExistentClass = "non-existent";
            when(classService.getByIndex(nonExistentClass)).thenReturn(Optional.empty());

            // AAA - Act & Assert
            mockMvc.perform(get(CLASSES_ENDPOINT + "/{classe}", nonExistentClass)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    /**
     * Helper method to create a mock CharClass
     */
    private CharClass createMockClass(String index, String name) {
        return new CharClass(
                "id-" + index,
                index,
                name,
                10,  // hitDie
                List.of(),  // proficiencyChoices
                List.of(new APIReference("proficiency", "Proficiency Name", "/api/proficiencies/prof")),  // proficiencies
                List.of(),  // savingThrows
                List.of(),  // startingEquipment
                List.of(),  // startingEquipmentOptions
                "/api/classes/" + index + "/levels",  // classLevels
                null,  // multiClassing
                List.of(new APIReference("subclass", "Subclass Name", "/api/subclasses/sub")),  // subclasses
                null,  // spellCasting
                "/api/classes/" + index  // url
        );
    }
} 