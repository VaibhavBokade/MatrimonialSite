package org.example.service;

import org.example.model.Profile;
import org.example.model.Question;

import java.util.List;
import java.util.Map;

public interface MatrimonialSite {
    String createProfile(Profile profile);
    List<Profile> searchMatches(int id);
    Profile showProfile(Integer id);
    double calculateMatchingScore(Profile profile,Profile otherProfile);
    List<Profile> getPartialMatch(int id);
    //double calculatePartialMatchScore(List<Question> questions, Profile profile);
    Map<String, String[]> calculatePartialMatchScore(List<Question> questions, Profile profile);
    List<Question> addQuestions(Profile profile, List<Question> questions);
    Map<Profile, Double> sortByDescendingOrder(Profile mainProfile, List<Profile> profileList);
    void addRequests(int id, int anotherId);
}
