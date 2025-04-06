package com.ced.service;

import com.ced.model.Identifiable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseServiceTest {

    @Mock
    private TestDataLoader dataLoader;

    private TestBaseService service;

    private static final List<TestEntity> TEST_ENTITIES = List.of(
            new TestEntity("1", "entity-1", "Entity 1"),
            new TestEntity("2", "entity-2", "Entity 2"),
            new TestEntity("3", "entity-3", "Entity 3"),
            new TestEntity("4", "entity-4", "Entity 4"),
            new TestEntity("5", "entity-5", "Entity 5")
    );

    @BeforeEach
    void setUp() {
        service = new TestBaseService(dataLoader);
    }

    @Nested
    @DisplayName("Tests for getAll method")
    class GetAllTests {

        @Test
        @DisplayName("Should return page with correct data when pagination is applied")
        void shouldReturnCorrectPagedData() {
            // AAA - Arrange
            when(dataLoader.getAllEntities()).thenReturn(TEST_ENTITIES);
            Pageable pageable = PageRequest.of(0, 2, Sort.by("index").ascending());

            // AAA - Act
            Page<TestEntity> result = service.getAll(pageable);

            // AAA - Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(TEST_ENTITIES.size());
            assertThat(result.getTotalPages()).isEqualTo(3);
            assertThat(result.getNumber()).isZero();
            assertThat(result.getContent()).containsExactly(
                    TEST_ENTITIES.get(0),
                    TEST_ENTITIES.get(1)
            );
        }

        @ParameterizedTest(name = "Page {0}, Size {1}")
        @MethodSource("paginationCombinations")
        @DisplayName("Should return correct page for different pagination combinations")
        void shouldReturnCorrectPageForDifferentPaginationCombinations(int page, int size, int expectedElements) {
            // AAA - Arrange
            when(dataLoader.getAllEntities()).thenReturn(TEST_ENTITIES);
            Pageable pageable = PageRequest.of(page, size);

            // AAA - Act
            Page<TestEntity> result = service.getAll(pageable);

            // AAA - Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(expectedElements);
            assertThat(result.getNumber()).isEqualTo(page);
        }

        static Stream<Arguments> paginationCombinations() {
            return Stream.of(
                    Arguments.of(0, 2, 2),   // first page, 2 elements
                    Arguments.of(1, 2, 2),   // second page, 2 elements
                    Arguments.of(2, 2, 1),   // third page, 1 element
                    Arguments.of(0, 5, 5),   // first page, all elements
                    Arguments.of(0, 10, 5),  // first page, more than total
                    Arguments.of(1, 5, 0)    // second page, no elements
            );
        }

        @Test
        @DisplayName("Should return empty page when requested page is out of bounds")
        void shouldReturnEmptyPageWhenRequestedPageIsOutOfBounds() {
            // AAA - Arrange
            when(dataLoader.getAllEntities()).thenReturn(TEST_ENTITIES);
            Pageable pageable = PageRequest.of(3, 2); // Beyond the last page

            // AAA - Act
            Page<TestEntity> result = service.getAll(pageable);

            // AAA - Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).as("Total elements should be preserved even for out-of-bounds pages")
                    .isEqualTo(TEST_ENTITIES.size());
            assertThat(result.getNumber()).isEqualTo(pageable.getPageNumber());
            assertThat(result.getTotalPages()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Tests for getByIndex method")
    class GetByIndexTests {

        @Test
        @DisplayName("Should return entity when index exists")
        void shouldReturnEntityWhenIndexExists() {
            // AAA - Arrange
            String existingIndex = "entity-3";
            TestEntity expectedEntity = TEST_ENTITIES.get(2);
            when(dataLoader.getEntityByIndex(existingIndex)).thenReturn(Optional.of(expectedEntity));

            // AAA - Act
            Optional<TestEntity> result = service.getByIndex(existingIndex);

            // AAA - Assert
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedEntity);
        }

        @Test
        @DisplayName("Should return empty Optional when index does not exist")
        void shouldReturnEmptyOptionalWhenIndexDoesNotExist() {
            // AAA - Arrange
            String nonExistingIndex = "non-existing";
            when(dataLoader.getEntityByIndex(nonExistingIndex)).thenReturn(Optional.empty());

            // AAA - Act
            Optional<TestEntity> result = service.getByIndex(nonExistingIndex);

            // AAA - Assert
            assertThat(result).isEmpty();
        }
    }

    /**
     * Interface for test data loader
     */
    interface TestDataLoader {
        List<TestEntity> getAllEntities();

        Optional<TestEntity> getEntityByIndex(String index);
    }

    /**
     * Concrete service class for tests
     */
    static class TestBaseService extends BaseService<TestEntity> {

        private final TestDataLoader testDataLoader;

        public TestBaseService(TestDataLoader testDataLoader) {
            super(null);  // We're not using the actual DataLoader
            this.testDataLoader = testDataLoader;
        }

        @Override
        protected List<TestEntity> getAllItems() {
            return testDataLoader.getAllEntities();
        }

        @Override
        protected Optional<TestEntity> getItemByIndex(String index) {
            return testDataLoader.getEntityByIndex(index);
        }
    }

    /**
     * Test entity
     */
    record TestEntity(String id, String index, String name) implements Identifiable {
    }
} 