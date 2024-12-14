package org.example.test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.users.SubscriptionsItem;
import org.example.vkRepo.VkApiRepo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class VkApiRepoTest {

    private final VkApiRepo vkApiRepo;

    {
        try {
            vkApiRepo = new VkApiRepo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Тест для метода isNameContainsIt
    @Test
    void testIsNameContainsItWithNull() {
        assertFalse(vkApiRepo.isNameContainsIt(null), "Should return false for null input");
    }

    @Test
    void testIsNameContainsItWithEmptyString() {
        assertFalse(vkApiRepo.isNameContainsIt(""), "Should return false for empty input");
    }

    @Test
    void testIsNameContainsItWithMatchingKeyword() {
        assertTrue(vkApiRepo.isNameContainsIt("Java development"), "Should return true if name contains 'Java'");
    }


    @Test
    void testIsNameContainsItWithMixedCaseKeyword() {
        assertTrue(vkApiRepo.isNameContainsIt("Frontend development"), "Should return true if name contains 'frontend' (case insensitive)");
    }

    @Test
    void testIsNameContainsItWithNoKeyword() {
        assertFalse(vkApiRepo.isNameContainsIt("Design studio"), "Should return false if no keyword is present");
    }

    // Тест для метода countGroup
    @Test
    void testCountGroupWithMatchingNames() {
        // Мокаем объекты SubscriptionsItem
        SubscriptionsItem item1 = mock(SubscriptionsItem.class);
        SubscriptionsItem item2 = mock(SubscriptionsItem.class);

        // Мокаем поведение getUsersSubscriptionsItemAsGroupFull для каждого элемента
        GroupFull group1 = mock(GroupFull.class);
        GroupFull group2 = mock(GroupFull.class);

        when(item1.getUsersSubscriptionsItemAsGroupFull()).thenReturn(group1);
        when(item2.getUsersSubscriptionsItemAsGroupFull()).thenReturn(group2);

        // Подготовка данных
        when(group1.getName()).thenReturn("Java programming");
        when(group2.getName()).thenReturn("Frontend development");

        // Список с двумя группами, обе из которых должны пройти проверку
        List<SubscriptionsItem> itemList = Arrays.asList(item1, item2);

        // Проверяем, что оба элемента будут посчитаны
        assertEquals(2, vkApiRepo.countGroup(itemList), "Should count both items that match 'IT' keywords");
    }
}
