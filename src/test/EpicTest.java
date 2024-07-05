package test;

import static org.junit.jupiter.api.Assertions.*;

import model.Epic;
import model.Status;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    public void EpicsWithEqualIdShouldBeEqual() {
        Epic epic1 = new Epic(10, "Сделать ремонт", "Уложиться в 2 миллиона", Status.NEW);
        Epic epic2 = new Epic(10, "Подготовиться к собеседованию", "1 июля в 11:00", Status.NEW);
        assertEquals(epic1, epic2,
                "Ошибка, наследники класса Task должны быть равны друг другу, если у них одинаковые id;");
    }
}