package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TagServiceImplTest {

    @Mock
    private TagDAOImpl tagDAOImpl;

    @Mock
    private GiftCertificateDAO giftCertificateDAO;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService = new TagServiceImpl();

    private static GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
    private static GiftCertificate giftCertificate = new GiftCertificate();
    private static Tag tag = new Tag();
    private static TagDto tagDto = new TagDto();
    private static Tag tag2 = new Tag();
    private static TagDto tagDto2 = new TagDto();
    private static List<GiftCertificate> giftCertificateList = new ArrayList<>();
    private static List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
    private static List<Tag> tagList = new ArrayList<>();
    private static List<TagDto> tagDtoList = new ArrayList<>();

    @BeforeAll
    public static void init() {
        tag.setName("test tag");
        tag.setId(1);
        tagDto.setName(tag.getName());
        tagDto.setId(tag.getId());

        tag2.setName("test tag 2");
        tag2.setId(2);
        tagDto2.setName(tag2.getName());
        tagDto2.setId(tag2.getId());

        tagDtoList.add(tagDto);
        tagDtoList.add(tagDto2);
        tagList.add(tag);
        tagList.add(tag2);

        giftCertificateDto.setId(1);
        giftCertificateDto.setName("Test name");
        giftCertificateDto.setDescription("Test description");
        giftCertificateDto.setDuration(10);
        giftCertificateDto.setPrice(20);

        giftCertificate.setId(giftCertificateDto.getId());
        giftCertificate.setName(giftCertificateDto.getName());
        giftCertificate.setDescription(giftCertificateDto.getDescription());
        giftCertificate.setDuration(giftCertificateDto.getDuration());
        giftCertificate.setPrice(giftCertificateDto.getPrice());
        giftCertificateList.add(giftCertificate);

        tagDto.setCertificates(giftCertificateDtoList);

    }

    @DisplayName("should be returned created Tag")
    @Test
    public void createTag() throws DuplicateEntryServiceException {
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagDAOImpl.create(tag)).thenReturn(tag);
        assertEquals(tagDto, tagService.create(tagDto));
    }

    @DisplayName("should be returned created Tag not null")
    @Test
    public void createTagNotNull() throws DuplicateEntryServiceException {
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagDAOImpl.create(tag)).thenReturn(tag);
        assertNotNull(tagService.create(tagDto));
    }

    @DisplayName("should be thrown DuplicateEntryServiceException")
    @Test
    public void createTagDuplicateKeyException() throws DuplicateEntryServiceException {
        when(modelMapper.map(tagDto, Tag.class)).thenThrow(DuplicateKeyException.class);
        assertThrows(DuplicateEntryServiceException.class, () -> {
            tagService.create(tagDto);
        });
    }

    @DisplayName("should be returned tagDto by id")
    @Test
    public void readTagById() throws IdNotExistServiceException {
        Tag tag = tagList.get(0);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagDAOImpl.read(5)).thenReturn(tag);
        assertEquals(tagDto, tagService.read(5));
    }

    @DisplayName("should be thrown idNotExistServiceException")
    @Test
    public void readTagByNotExistId() {
        when(tagDAOImpl.read(1)).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(IdNotExistServiceException.class, () -> {
            tagService.read(1);
        });
    }

    @DisplayName("should be returned list of TagDto")
    @Test
    public void findAllTags() {
        Tag testTag = new Tag();
        TagDto testTagDto = new TagDto();
        testTag.setId(1);
        testTag.setName("test tag");
        testTagDto.setId(testTag.getId());
        testTagDto.setName(testTag.getName());
        List<Tag> tags = new ArrayList<>();
        tags.add(testTag);
        when(modelMapper.map(testTag, TagDto.class)).thenReturn(testTagDto);
        when(giftCertificateDAO.getGiftCertificatesByTagId(anyLong())).thenReturn(giftCertificateList);
        when(tagDAOImpl.findAll()).thenReturn(tags);
        assertEquals(1, tagService.findAll().size());
    }

    @DisplayName("should be called method delete from DAO")
    @Test
    public void deleteTagById() throws IdNotExistServiceException {
        when(tagDAOImpl.delete(5)).thenReturn(1);
        tagService.delete(5);
        verify(tagDAOImpl).delete(5);
    }

    @DisplayName("should be called method delete from DAO")
    @Test
    public void deleteTagByNotExistId() throws IdNotExistServiceException {
        when(tagDAOImpl.delete(6)).thenReturn(0);
        assertThrows(IdNotExistServiceException.class, () -> {
            tagService.delete(6);
        });
    }

}
