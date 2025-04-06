package com.ced.cache;

import com.ced.model.Identifiable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericCacheTest {

    private GenericCache<TestEntity> cache;

    @Mock
    private MongoRepository<TestEntity, String> repository;

    @Mock
    private Logger logger;

    private final String ENTITY_NAME = "testEntity";

    @BeforeEach
    void setUp() {
        cache = new GenericCache<>(repository, logger, ENTITY_NAME);
    }

    @Nested
    @DisplayName("Data loading tests")
    class LoadDataTests {

        @Test
        @DisplayName("Should load data successfully when repository returns items")
        void shouldLoadDataSuccessfullyWhenRepositoryReturnsItems() {
            // AAA - Arrange
            TestEntity entity = new TestEntity("1", "test-index", "Test Name");
            when(repository.findAll()).thenReturn(List.of(entity));

            // AAA - Act
            cache.loadData();

            // AAA - Assert
            verify(repository).findAll();
            verify(logger).info(contains("Carregando dados de {} em memória..."), eq(ENTITY_NAME));
            verify(logger).info(contains("Dados de {} carregados com sucesso: {} registros."), 
                eq(ENTITY_NAME), eq(1));

            assertThat(cache.getAll())
                .isNotEmpty()
                .hasSize(1)
                .contains(entity);
        }

        @Test
        @DisplayName("Should log warning when repository returns an empty list")
        void shouldLogWarningWhenRepositoryReturnsEmptyList() {
            // AAA - Arrange
            when(repository.findAll()).thenReturn(Collections.emptyList());

            // AAA - Act
            cache.loadData();

            // AAA - Assert
            verify(repository).findAll();
            verify(logger).warn(contains("Nenhum(a) {} encontrado(a) no repositório."), eq(ENTITY_NAME));
            
            assertThat(cache.getAll()).isEmpty();
        }

        @Test
        @DisplayName("Should log error when exception occurs during data loading")
        void shouldLogErrorWhenExceptionOccurs() {
            // AAA - Arrange
            RuntimeException exception = new RuntimeException("Test error");
            when(repository.findAll()).thenThrow(exception);

            // AAA - Act
            cache.loadData();

            // AAA - Assert
            verify(repository).findAll();
            
            ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Object> entityNameCaptor = ArgumentCaptor.forClass(Object.class);
            ArgumentCaptor<Object> exceptionMsgCaptor = ArgumentCaptor.forClass(Object.class);
            ArgumentCaptor<Exception> exceptionCaptor = ArgumentCaptor.forClass(Exception.class);
            
            verify(logger).error(messageCaptor.capture(), entityNameCaptor.capture(), 
                exceptionMsgCaptor.capture(), exceptionCaptor.capture());
            
            assertThat(messageCaptor.getValue()).contains("Falha ao carregar dados de {}");
            assertThat(entityNameCaptor.getValue()).isEqualTo(ENTITY_NAME);
            assertThat(exceptionMsgCaptor.getValue()).isEqualTo(exception.getMessage());
            assertThat(exceptionCaptor.getValue()).isEqualTo(exception);
            
            assertThat(cache.getAll()).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("Data query tests")
    class QueryDataTests {
        
        @Test
        @DisplayName("Should return all cached items")
        void shouldReturnAllCachedItems() {
            // AAA - Arrange
            TestEntity entity1 = new TestEntity("1", "test-index-1", "Test Name 1");
            TestEntity entity2 = new TestEntity("2", "test-index-2", "Test Name 2");
            when(repository.findAll()).thenReturn(List.of(entity1, entity2));
            cache.loadData();

            // AAA - Act
            List<TestEntity> result = cache.getAll();

            // AAA - Assert
            assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(entity1, entity2);
        }
        
        @Test
        @DisplayName("Should return item by index when it exists")
        void shouldReturnItemByIndexWhenExists() {
            // AAA - Arrange
            TestEntity entity1 = new TestEntity("1", "test-index-1", "Test Name 1");
            TestEntity entity2 = new TestEntity("2", "test-index-2", "Test Name 2");
            when(repository.findAll()).thenReturn(List.of(entity1, entity2));
            cache.loadData();

            // AAA - Act
            Optional<TestEntity> result = cache.getByIndex("test-index-2");

            // AAA - Assert
            assertThat(result)
                .isPresent()
                .contains(entity2);
        }
        
        @Test
        @DisplayName("Should return empty Optional when index does not exist")
        void shouldReturnEmptyOptionalWhenIndexDoesNotExist() {
            // AAA - Arrange
            TestEntity entity = new TestEntity("1", "test-index", "Test Name");
            when(repository.findAll()).thenReturn(List.of(entity));
            cache.loadData();

            // AAA - Act
            Optional<TestEntity> result = cache.getByIndex("non-existent-index");

            // AAA - Assert
            assertThat(result).isEmpty();
        }
        
        @Test
        @DisplayName("Should return empty Optional when index is null")
        void shouldReturnEmptyOptionalWhenIndexIsNull() {
            // AAA - Arrange
            TestEntity entity = new TestEntity("1", "test-index", "Test Name");
            when(repository.findAll()).thenReturn(List.of(entity));
            cache.loadData();

            // AAA - Act
            Optional<TestEntity> result = cache.getByIndex(null);

            // AAA - Assert
            assertThat(result).isEmpty();
        }
    }
    
    /**
     * Test entity class
     */
    record TestEntity(String id, String index, String name) implements Identifiable {
    }
} 