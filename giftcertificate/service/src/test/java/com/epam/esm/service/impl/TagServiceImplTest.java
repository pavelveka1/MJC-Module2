package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.TagJDBCTemplate;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntryDAOException;
import com.epam.esm.exception.IdNotExistDAOException;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TagServiceImplTest {

    @Mock
    private TagJDBCTemplate tagJDBCTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService = new TagServiceImpl();
    /*
        private static TagJDBCTemplate tagJDBCTemplate = mock(TagJDBCTemplate.class);
        private static ModelMapper modelMapper = mock(ModelMapper.class);

    */
    private static GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
    private static GiftCertificate giftCertificate = new GiftCertificate();
    private static Tag tag = new Tag();
    private static TagDto tagDto = new TagDto();
    private static List<GiftCertificate> giftCertificateList = new ArrayList<>();
    private static List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
    private static List<Tag> tagList = new ArrayList<>();
    private static List<TagDto> tagDtoList = new ArrayList<>();

    @BeforeAll
    public static void init() {
        tag.setName("test tag");
        tagDto.setName(tag.getName());

        tagDtoList.add(tagDto);
        tagList.add(tag);

        giftCertificateDto.setId(1);
        giftCertificateDto.setName("Test name");
        giftCertificateDto.setDescription("Test description");
        giftCertificateDto.setDuration(10);
        giftCertificateDto.setPrice(20);
        giftCertificateDto.setTags(tagDtoList);
        giftCertificateDtoList.add(giftCertificateDto);

        giftCertificate.setId(giftCertificateDto.getId());
        giftCertificate.setName(giftCertificateDto.getName());
        giftCertificate.setDescription(giftCertificateDto.getDescription());
        giftCertificate.setDuration(giftCertificateDto.getDuration());
        giftCertificate.setPrice(giftCertificateDto.getPrice());
        giftCertificateList.add(giftCertificate);

        tagDto.setCertificates(giftCertificateDtoList);
        tag.setCertificates(giftCertificateList);
        giftCertificateDto.setTags(tagDtoList);
        giftCertificate.setTags(tagList);
    }

    @DisplayName("should be returned created Tag")
    @Test
    public void createTag() throws DuplicateEntryDAOException, DuplicateEntryServiceException {
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagJDBCTemplate.create(tag)).thenReturn(tag);
        assertEquals(tagDto, tagService.create(tagDto));
    }

    @DisplayName("should be returned not null")
    @Test
    public void createTagNotNull() throws DuplicateEntryDAOException, DuplicateEntryServiceException {
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagJDBCTemplate.create(tag)).thenReturn(tag);
        assertNotNull(tagService.create(tagDto));
    }

    @DisplayName("should be thrown DuplicateEntryServiceException")
    @Test
    public void createTagDuplicateEntryServiceException() throws DuplicateEntryDAOException {
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(tagJDBCTemplate.create(tag)).thenThrow(DuplicateEntryDAOException.class);
        assertThrows(DuplicateEntryServiceException.class, () -> {
            tagService.create(tagDto);
        });
    }


    @DisplayName("should be returned tagDto by id")
    @Test
    public void readTagById() throws IdNotExistDAOException, IdNotExistServiceException {
        Tag tag = tagList.get(0);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagJDBCTemplate.read(5)).thenReturn(tag);
        assertEquals(tagDto, tagService.read(5));
    }

    @DisplayName("should be thrown idNotExistServiceException")
    @Test
    public void readTagByNotExistId() throws IdNotExistDAOException {
        Optional<Tag> tagOptional = tagList.stream().findFirst();
        when(tagJDBCTemplate.read(1)).thenThrow(IdNotExistDAOException.class);
        assertThrows(IdNotExistServiceException.class, () -> {
            tagService.read(1);
        });
    }


    @DisplayName("should be returned list of TagDto")
    @Test
    public void findAllTags() {
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagJDBCTemplate.findAll()).thenReturn(tagList);
        assertEquals(tagDtoList, tagService.findAll());
    }

    @DisplayName("should be returned not null")
    @Test
    public void findAllTagsResultNotNull() {
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagJDBCTemplate.findAll()).thenReturn(tagList);
        assertNotNull(tagService.findAll());
    }

    @DisplayName("should be called method delete from DAO")
    @Test
    public void deleteTagById() throws IdNotExistServiceException, IdNotExistDAOException {
        doNothing().when(tagJDBCTemplate).delete(5);
        tagService.delete(5);
        verify(tagJDBCTemplate, times(1)).delete(5);
    }

}
