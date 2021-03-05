package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntryServiceException;
import com.epam.esm.service.exception.IdNotExistServiceException;
import com.epam.esm.service.exception.PaginationException;
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
import org.springframework.data.domain.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TagServiceImplTest {

    @Mock
    private TagDAO tagDAOImpl;

    @Mock
    private GiftCertificateDAO giftCertificateDAO;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService = new TagServiceImpl();

    private static GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1, "Test name", "Test description", 10, 20, null, null, null);
    private static GiftCertificate giftCertificate = new GiftCertificate(1, "Test name", "Test description", 10, 20, null, null, null, null, false);
    private static Tag tag = new Tag(1, "test tag", null);
    private static Tag tag2 = new Tag(2, "test tag 2", null);
    private static TagDto tagDto = new TagDto((long) 1, "test tag");
    private static TagDto tagDto2 = new TagDto((long) 2, "test tag 2");
    private static List<GiftCertificate> giftCertificateList = new ArrayList<>();
    private static List<Tag> tagList = new ArrayList<>();
    private static List<TagDto> tagDtoList = new ArrayList<>();
    private static final Long ONE = (long) 1;

    @BeforeAll
    public static void init() {
        tagDtoList.add(tagDto);
        tagDtoList.add(tagDto2);
        tagList.add(tag);
        tagList.add(tag2);
        giftCertificateList.add(giftCertificate);
    }

    @DisplayName("should be returned created Tag")
    @Test
    public void testCreateTag() throws DuplicateEntryServiceException, SQLIntegrityConstraintViolationException {
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagDAOImpl.save(tag)).thenReturn(tag);
        assertEquals(tagDto, tagService.create(tagDto));
    }

    @DisplayName("should be returned created Tag not null")
    @Test
    public void testCreateTagNotNull() throws DuplicateEntryServiceException, SQLIntegrityConstraintViolationException {
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagDAOImpl.save(tag)).thenReturn(tag);
        assertNotNull(tagService.create(tagDto));
    }


    @DisplayName("should be returned tagDto by id")
    @Test
    public void testReadTagById() throws IdNotExistServiceException {
        Tag tag = tagList.get(0);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagDAOImpl.findById(new Long(5))).thenReturn(Optional.of(tag));
        assertEquals(tagDto, tagService.read(5));
    }


    @DisplayName("should be thrown idNotExistServiceException")
    @Test
    public void testReadTagByNotExistId() {
        when(tagDAOImpl.findById(new Long(1))).thenThrow(IllegalArgumentException.class);
        assertThrows(IdNotExistServiceException.class, () -> {
            tagService.read(1);
        });
    }


    @DisplayName("should be returned list of TagDto")
    @Test
    public void testFindAllTags() throws PaginationException {
        Tag testTag = new Tag();
        TagDto testTagDto = new TagDto();
        testTag.setId(1);
        testTag.setName("test tag");
        testTagDto.setId(testTag.getId());
        testTagDto.setName(testTag.getName());
        List<Tag> tags = new ArrayList<>();
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(testTagDto);
        tags.add(testTag);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(modelMapper.map(testTag, TagDto.class)).thenReturn(testTagDto);
        Page<Tag> page = new PageImpl<>(tags, pageable, tags.size());
        when(tagDAOImpl.findAll(pageable)).thenReturn(page);
        assertEquals(tagDtoList, tagService.findAll(1, 10));
    }

    @DisplayName("should be thrown PaginationException")
    @Test
    public void testFindAllTagsPaginationException() throws PaginationException {
        assertThrows(PaginationException.class, () -> {
            tagService.findAll(0, 1).size();
        });
    }


    @DisplayName("should be called method delete from DAO")
    @Test
    public void testDeleteTagById() throws IdNotExistServiceException {
        when(tagDAOImpl.findById(new Long(6))).thenReturn(Optional.ofNullable(tag));
        tagService.delete(6);
        verify(tagDAOImpl).delete(tag);
    }

    @DisplayName("should be called method delete from DAO")
    @Test
    public void testDeleteTagByNotExistId() throws IdNotExistServiceException {
        when(tagDAOImpl.findById(new Long(3))).thenReturn(Optional.ofNullable(null));
        assertThrows(IdNotExistServiceException.class, () -> {
            tagService.delete(3);
        });
    }

    @DisplayName("should be returned widely used tag")
    @Test
    public void testGetWidelyUsedByUserTagWithHighestCost() throws IdNotExistServiceException {
        when(tagDAOImpl.getIdWidelyUsedByUserTagWithHighestCost(1)).thenReturn(ONE);
        when(tagDAOImpl.findById(new Long(1))).thenReturn(Optional.ofNullable(tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        assertEquals(tagDto, tagService.getWidelyUsedByUserTagWithHighestCost(1));
    }

    @DisplayName("should be thrown IdNotExistServiceException")
    @Test
    public void testGetWidelyUsedByUserTagWithHighestCostUserIdNotExist() throws IdNotExistServiceException {
        when(tagDAOImpl.getIdWidelyUsedByUserTagWithHighestCost(2)).thenThrow(NullPointerException.class);
        assertThrows(IdNotExistServiceException.class, () -> {
            tagService.getWidelyUsedByUserTagWithHighestCost(2);
        });
    }

}
