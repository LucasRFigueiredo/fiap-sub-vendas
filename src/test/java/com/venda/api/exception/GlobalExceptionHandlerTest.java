package com.venda.api.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException notFoundException = new NotFoundException("Resource not found");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNotFoundException(notFoundException);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().containsKey("timestamp"));
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("Resource not found", response.getBody().get("message"));
    }

    @Test
    void testHandleBusinessException() {
        BusinessException businessException = new BusinessException("Business rule violation");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleBusinessException(businessException);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("timestamp"));
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Business rule violation", response.getBody().get("message"));
    }

    @Test
    void testHandleGenericException() {
        Exception genericException = new Exception("Unexpected error");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(genericException);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().containsKey("timestamp"));
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals("Ocorreu um erro inesperado", response.getBody().get("message"));
    }
}
