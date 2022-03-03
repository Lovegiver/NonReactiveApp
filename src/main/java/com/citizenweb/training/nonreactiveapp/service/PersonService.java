package com.citizenweb.training.nonreactiveapp.service;

import java.util.List;

public interface PersonService {
    String buildName();
    int computeAge();
    long countCharOccurrencesInWord(String word, char c);
    double computeMean(List<String> words, char c);
}
