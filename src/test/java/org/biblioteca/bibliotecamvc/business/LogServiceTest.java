/*package org.biblioteca.bibliotecamvc.business;

import org.biblioteca.bibliotecamvc.business.dto.LogDTO;
import org.biblioteca.bibliotecamvc.business.mapper.LogMapper;
import org.biblioteca.bibliotecamvc.business.service.LogService;
import org.biblioteca.bibliotecamvc.persistence.entities.LogEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.LogJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {
    @Mock
    private LogJPARepository logRepository;

    @InjectMocks
    private LogService logService;

    private LogMapper logMapper = new LogMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TODO
    @Test
    void getAllLogsByUserIdTest(){
        //Arrange

        var logs = new ArrayList<LogEntity>();
        logs.add(new LogEntity());

        var logsDTO = new ArrayList<LogDTO>();
        logsDTO.add(new LogDTO());

        int userId = 1;

        when(logRepository.getAllByUsers_Id(any(Integer.class))).thenReturn(logs);

        //Act
        var resp = logService.getAllLogsByUserId(userId);

        //Assert
        assertThat(resp).isEqualTo(logsDTO);
        verify(logRepository).getAllByUsers_Id(any(Integer.class));
    }
}*/
