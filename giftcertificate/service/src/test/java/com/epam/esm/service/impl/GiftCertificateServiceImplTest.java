package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.GiftCertificateDAOImpl;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GiftCertificateServiceImplTest {

    @Mock
    private TagDAOImpl tagDAOImpl;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private GiftCertificateDAOImpl giftCertificateDAOImpl;

    @InjectMocks
    private GiftSertificateServiceImpl giftCertificateService = new GiftSertificateServiceImpl();

    private static GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1, "Test name", "Test description", 10, 20, null, null, new ArrayList<>());
    private static GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto(2, "Test name 2", "Test description 2", 10, 20, null, null, null);
    private static GiftCertificateDto giftCertificateDto3 = new GiftCertificateDto(3, "Test name 3", "Test description 3", 10, 20, null, null, null);
    private static GiftCertificate giftCertificate = new GiftCertificate(1, "Test name", "Test description", 10, 20, null, null, null,null);
    private static GiftCertificate giftCertificate2 = new GiftCertificate(2, "Test name 2", "Test description 2", 10, 20, null, null, null, null);
    private static GiftCertificate giftCertificate3 = new GiftCertificate(3, "Test name 3", "Test description 3", 10, 20, null, null, null, null);
    private static Tag tag = new Tag(1, "test tag",null);
    private static TagDto tagDto = new TagDto((long) 1, "test tag");
    private static TagDto tagDto1 = new TagDto((long) 2, "Space");
    private static TagDto tagDto2 = new TagDto((long) 3, "Aircraft");
    private static List<GiftCertificate> giftCertificateList = new ArrayList<>();
    private static List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
    private static List<TagDto> tagDtoList = new ArrayList<>();

    @BeforeAll
    public static void init() {
        giftCertificateList.add(giftCertificate);
        giftCertificateDtoList.add(giftCertificateDto);
    }

    @DisplayName("should be returned created gift certificate")
    @Test
    public void createGiftCertificate() throws DuplicateEntryServiceException, TagNotExistServiceException, SQLIntegrityConstraintViolationException {
        when(giftCertificateDAOImpl.create(giftCertificate)).thenReturn((long) 100);
        when(giftCertificateDAOImpl.read(100)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        assertEquals(giftCertificateDto, giftCertificateService.create(giftCertificateDto));
    }

    @DisplayName("should be thrown duplicateEntryServiceException")
    @Test
    public void createGiftCertificateDuplicateEntryException() throws SQLIntegrityConstraintViolationException {
        when(giftCertificateDAOImpl.create(giftCertificate)).thenThrow(DuplicateKeyException.class);
        when(modelMapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        assertThrows(DuplicateEntryServiceException.class, () -> {
            giftCertificateService.create(giftCertificateDto);
        });
    }


    @DisplayName("should be renurned giftCertificateDto")
    @Test
    public void readGiftCertificateById() throws IdNotExistServiceException {
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        when(giftCertificateDAOImpl.read(2)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        assertEquals(giftCertificateDto, giftCertificateService.read(2));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void readGiftCertificateByNotExistId() {
        when(giftCertificateDAOImpl.read(10)).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(IdNotExistServiceException.class, () -> {
            giftCertificateService.read(10);
        });
    }

    @DisplayName("should be returned updated giftCertificateDto")
    @Test
    public void updateGiftCertificate() throws IdNotExistServiceException, UpdateServiceException {
        when(modelMapper.map(giftCertificateDto3, GiftCertificate.class)).thenReturn(giftCertificate3);
        when(giftCertificateDAOImpl.read(3)).thenReturn(giftCertificate3);
        when(modelMapper.map(giftCertificate3, GiftCertificateDto.class)).thenReturn(giftCertificateDto3);
    //    when(giftCertificateDAOImpl.update(giftCertificate3)).thenReturn(1);
     //   assertEquals(giftCertificateDto3, giftCertificateService.update(giftCertificateDto3));
    }

    @DisplayName("should be thrown UpdateServiceException")
    @Test
    public void updateGiftCertificateIdNotExist() {
        when(modelMapper.map(giftCertificateDto2, GiftCertificate.class)).thenReturn(giftCertificate2);
    //    when(giftCertificateDAOImpl.update(giftCertificate2)).thenReturn(0);
        assertThrows(UpdateServiceException.class, () -> {
            giftCertificateService.update(giftCertificateDto2);
        });
    }


    @DisplayName("should be returned list of giftCertificateDto")
    @Test
    public void findAllGiftCertificates() throws RequestParamServiceException, SQLSyntaxErrorException {
        when(giftCertificateDAOImpl.findAll("name", "DESC")).thenReturn(giftCertificateList);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        assertEquals(giftCertificateDtoList, giftCertificateService.findAll(null, null, "name",
                "DESC"));
    }

    @DisplayName("should be thrown RequestParamServiceException")
    @Test
    public void findAllGiftCertificatesRequestParamsAreNotValid() throws SQLSyntaxErrorException {
        when(giftCertificateDAOImpl.findAll("badParam", "asc")).thenThrow(BadSqlGrammarException.class);
        assertThrows(RequestParamServiceException.class, () -> {
            giftCertificateService.findAll(null, null, "badParam", null);
        });
    }

    @DisplayName("should be returned list of certificates by tad's name")
    @Test
    public void findAllGiftCertificatesByTagName() throws SQLSyntaxErrorException, RequestParamServiceException {
        List<Tag> tags = new ArrayList<>();
        when(giftCertificateDAOImpl.findAllCertificatesByTagName("спорт", "id", "asc"))
                .thenReturn(giftCertificateList);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        when(tagDAOImpl.getTagsByGiftCertificateId(anyLong())).thenReturn(tags);
        assertEquals(giftCertificateDtoList,
                giftCertificateService.findAll("tag", "спорт", "id", "asc"));
    }

    @DisplayName("should be returned list of certificates by name or description")
    @Test
    public void findAllGiftCertificatesByNameOrDescription() throws RequestParamServiceException {
        List<Tag> tags = new ArrayList<>();
        when(giftCertificateDAOImpl.findAllCertificatesByNameOrDescription("спорт", "id",
                "asc")).thenReturn(giftCertificateList);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        when(tagDAOImpl.getTagsByGiftCertificateId(anyLong())).thenReturn(tags);
        assertEquals(giftCertificateDtoList,
                giftCertificateService.findAll("name", "спорт", "id", "asc"));
    }

/*
    @DisplayName("should be called delete method from DAO")
    @Test
    public void deleteGiftCertificateById() throws IdNotExistServiceException {
        when(giftCertificateDAOImpl.delete(1)).thenReturn(1);
        assertEquals(1, giftCertificateDAOImpl.delete(1));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void deleteGiftCertificateByNotExistId() throws IdNotExistServiceException {
        when(giftCertificateDAOImpl.delete(9)).thenReturn(0);
        assertThrows(IdNotExistServiceException.class, () -> {
            giftCertificateService.delete(9);
        });
    }

 */
}
