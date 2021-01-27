package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.GiftCertificateJDBCTemplate;
import com.epam.esm.dao.impl.TagJDBCTemplate;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntryDAOException;
import com.epam.esm.exception.IdNotExistDAOException;
import com.epam.esm.exception.RequestParamDAOException;
import com.epam.esm.exception.UpdateDAOException;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.*;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GiftCertificateServiceImplTest {

    @Mock
    private TagJDBCTemplate tagJDBCTemplate;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private GiftCertificateJDBCTemplate giftCertificateJDBCTemplate;

    @InjectMocks
    private GiftSertificateServiceImpl giftCertificateService = new GiftSertificateServiceImpl();

    private static GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
    private static GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto();
    private static GiftCertificateDto giftCertificateDto3 = new GiftCertificateDto();
    private static GiftCertificate giftCertificate = new GiftCertificate();
    private static GiftCertificate giftCertificate2 = new GiftCertificate();
    private static GiftCertificate giftCertificate3 = new GiftCertificate();
    private static Tag tag = new Tag();
    private static TagDto tagDto = new TagDto();
    private static List<GiftCertificate> giftCertificateList = new ArrayList<>();
    private static List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();

    @BeforeEach
    public void init() {

        tag.setName("test tag");
        List<Tag> tags = new ArrayList<>();
        tagDto.setName(tag.getName());
        List<TagDto> tagDtoList = new ArrayList<>();

        TagDto tagDto1 = new TagDto();
        tagDto1.setName("Space");
        TagDto tagDto2 = new TagDto();
        tagDto2.setName("Aircraft");

        List<TagDto> tagsDto = new ArrayList<TagDto>();
        tagsDto.add(tagDto1);
        tagsDto.add(tagDto2);

        giftCertificateDto.setId(1);
        giftCertificateDto.setName("Test name");
        giftCertificateDto.setDescription("Test description");
        giftCertificateDto.setDuration(10);
        giftCertificateDto.setPrice(20);
        giftCertificateDto.setTags(tagsDto);
        giftCertificateDtoList.add(giftCertificateDto);

        giftCertificateDto.setId(2);
        giftCertificateDto.setName("Test name 2");
        giftCertificateDto.setDescription("Test description 2");
        giftCertificateDto.setDuration(10);
        giftCertificateDto.setPrice(20);
        giftCertificateDto.setTags(tagsDto);

        giftCertificateDto2.setId(9);
        giftCertificateDto2.setName("Test name 2");
        giftCertificateDto2.setDescription("Test description 2");
        giftCertificateDto2.setDuration(10);
        giftCertificateDto2.setPrice(20);

        giftCertificateDto3.setId(8);
        giftCertificateDto3.setName("Test name 2");
        giftCertificateDto3.setDescription("Test description 2");
        giftCertificateDto3.setDuration(10);
        giftCertificateDto3.setPrice(20);


        giftCertificate.setId(giftCertificateDto.getId());
        giftCertificate.setName(giftCertificateDto.getName());
        giftCertificate.setDescription(giftCertificateDto.getDescription());
        giftCertificate.setDuration(giftCertificateDto.getDuration());
        giftCertificate.setPrice(giftCertificateDto.getPrice());
        giftCertificateList.add(giftCertificate);

        giftCertificate2.setId(giftCertificateDto2.getId());
        giftCertificate2.setName(giftCertificateDto2.getName());
        giftCertificate2.setDescription(giftCertificateDto2.getDescription());
        giftCertificate2.setDuration(giftCertificateDto2.getDuration());
        giftCertificate2.setPrice(giftCertificateDto2.getPrice());

        giftCertificate3.setId(giftCertificateDto3.getId());
        giftCertificate3.setName(giftCertificateDto3.getName());
        giftCertificate3.setDescription(giftCertificateDto3.getDescription());
        giftCertificate3.setDuration(giftCertificateDto3.getDuration());
        giftCertificate3.setPrice(giftCertificateDto3.getPrice());

        tagDto.setCertificates(giftCertificateDtoList);
        tag.setCertificates(giftCertificateList);
        giftCertificateDto.setTags(tagDtoList);
        giftCertificate.setTags(tags);
    }

    @DisplayName("should be returned created gift certificate")
    @Test
    public void createGiftCertificate() throws DuplicateEntryDAOException, TagNameNotExistServiceException, DuplicateEntryServiceException {
        when(giftCertificateJDBCTemplate.create(giftCertificate)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        assertEquals(giftCertificateDto, giftCertificateService.create(giftCertificateDto));
    }

    @DisplayName("should be thrown duplicateEntryServiceException")
    @Test
    public void createGiftCertificateDuplicateEntryException() throws DuplicateEntryDAOException {
        when(giftCertificateJDBCTemplate.create(giftCertificate)).thenThrow(DuplicateEntryDAOException.class);
        when(modelMapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        assertThrows(DuplicateEntryServiceException.class, () -> {
            giftCertificateService.create(giftCertificateDto);
        });
    }

    @DisplayName("should be renurned giftCertificateDto")
    @Test
    public void readGiftCertificateById() throws IdNotExistDAOException, IdNotExistServiceException {
        GiftCertificate giftCertificate = giftCertificateList.get(1);
        when(giftCertificateJDBCTemplate.read(2)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        assertEquals(giftCertificateDto, giftCertificateService.read(2));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void readGiftCertificateByNotExistId() throws IdNotExistDAOException {
        when(giftCertificateJDBCTemplate.read(10)).thenThrow(IdNotExistDAOException.class);
        assertThrows(IdNotExistServiceException.class, () -> {
            giftCertificateService.read(10);
        });
    }

    @DisplayName("should be returned updated giftCertificateDto")
    @Test
    public void updateGiftCertificate() throws IdNotExistServiceException, IdNotExistDAOException, UpdateDAOException, UpdateServiceException {
        GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto();
        giftCertificateDto2.setName("giftCertificateDto 2");
        giftCertificateDto2.setDescription("description 2 giftCertificateDto");
        giftCertificateDto2.setPrice(10);
        giftCertificateDto2.setDuration(30);
        giftCertificateDto2.setId(4);
        when(modelMapper.map(giftCertificateDto2, GiftCertificate.class)).thenReturn(giftCertificate);
        GiftCertificate giftCertificate = giftCertificateList.get(1);
        when(giftCertificateJDBCTemplate.read(4)).thenReturn(giftCertificate);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto2);
        when(modelMapper.map(giftCertificateDto2, GiftCertificate.class)).thenReturn(giftCertificate);
        when(giftCertificateJDBCTemplate.update(giftCertificate)).thenReturn(giftCertificate);
        assertEquals(giftCertificateDto2, giftCertificateService.update(giftCertificateDto2));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void updateGiftCertificateIdNotExist() throws IdNotExistDAOException {
        when(modelMapper.map(giftCertificateDto2, GiftCertificate.class)).thenReturn(giftCertificate2);
        when(giftCertificateJDBCTemplate.read(9)).thenThrow(IdNotExistDAOException.class);
        assertThrows(IdNotExistServiceException.class, () -> {
            giftCertificateService.update(giftCertificateDto2);
        });
    }


    @DisplayName("should be returned list of giftCertificateDto")
    @Test
    public void findAllGiftCertificates() throws RequestParamDAOException, RequestParamServiceException {
        when(giftCertificateJDBCTemplate.findAll("name", "DESC")).thenReturn(giftCertificateList);
        when(modelMapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        assertEquals(giftCertificateDtoList, giftCertificateService.findAll("name", "DESC"));
    }

    @DisplayName("should be thrown RequestParamServiceException")
    @Test
    public void findAllGiftCertificatesRequestParamsAreNotValid() throws RequestParamDAOException {
        when(giftCertificateJDBCTemplate.findAll(null, null)).thenThrow(RequestParamDAOException.class);
        assertThrows(RequestParamServiceException.class, () -> {
            giftCertificateService.findAll(null, null);
        });
    }

    @DisplayName("should be called delete method from DAO")
    @Test
    public void deleteGiftCertificateById() throws IdNotExistServiceException, IdNotExistDAOException {
        doNothing().when(giftCertificateJDBCTemplate).delete(5);
        GiftCertificate giftCertificate = giftCertificateList.get(0);
        giftCertificateService.delete(5);
        verify(giftCertificateJDBCTemplate, times(1)).delete(5);
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void deleteGiftCertificateByNotExistId() throws IdNotExistDAOException, IdNotExistServiceException {
        doNothing().when(giftCertificateJDBCTemplate).delete(7);
        giftCertificateService.delete(7);
        verify(giftCertificateJDBCTemplate, times(1)).delete(7);

    }
}
