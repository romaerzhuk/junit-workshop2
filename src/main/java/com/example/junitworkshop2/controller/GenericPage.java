package com.example.junitworkshop2.controller;

import java.util.List;

/**
 * Страница запроса.
 *
 * @param data  страница запроса
 * @param total итоговое количество записей
 * @param <T>   тип DTO
 * @author Roman_Erzhukov
 */
public record GenericPage<T>(List<T> data, long total) {
}
