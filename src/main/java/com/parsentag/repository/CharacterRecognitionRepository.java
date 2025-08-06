package com.parsentag.repository;

//todo: Переименовать в CharacterRecognitionService
public interface CharacterRecognitionRepository {
    String extractTextFromImageByKey(String key);
}
