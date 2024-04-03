package org.example.service.serviceImpl;

import org.example.exceptions.InvalidFormatException;
import org.example.exceptions.NullValueException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Profile;
import org.example.model.Question;
import org.example.model.RequestChart;
import org.example.service.MatrimonialSite;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.*;
import java.util.stream.Collectors;

public class MatrimonialSiteImpl implements MatrimonialSite {
    private final SessionFactory sessionFactory;

    public MatrimonialSiteImpl() {
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    @Override
    public String createProfile(Profile profile) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(profile);
                transaction.commit();
                return "Profile added successfully!";
            } catch (NullValueException e) {
                e.printStackTrace();
                transaction.rollback();
                throw e;
            } catch (InvalidFormatException e) {
                e.printStackTrace();
                transaction.rollback();
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Profile showProfile(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Profile profile = session.get(Profile.class, id);
            if (profile == null) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }
            return profile;
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public double calculateMatchingScore(Profile profile, Profile otherProfile) {
        int totalScore = 0;
        int maxPossibleScore = 7;
        if (profile.getGender() != otherProfile.getGender()) {
            int ageDifference = Math.abs(profile.getAge() - otherProfile.getAge());
            if (ageDifference <= 5) {
                totalScore += 2;
            }
            if (profile.getProfession().equalsIgnoreCase(otherProfile.getProfession())) {
                totalScore++;
            }
            if (profile.getHobby().equalsIgnoreCase(otherProfile.getHobby())) {
                totalScore++;
            }
            if (profile.getFavoriteColour().equalsIgnoreCase(otherProfile.getFavoriteColour())) {
                totalScore++;
            }
            if (profile.getFoodType() == otherProfile.getFoodType()) {
                totalScore++;
            }
            if (profile.getFavoritePet().equalsIgnoreCase(otherProfile.getFavoritePet())) {
                totalScore++;
            }
            if (profile.getThoughts() == otherProfile.getThoughts()) {
                totalScore++;
            }
        }
        double percentage = ((double) totalScore / maxPossibleScore) * 100;

        String formattedPercentage = String.format("%.2f", percentage);

        return Double.parseDouble(formattedPercentage);
    }





    @Override
    public List<Profile> searchMatches(int id) {
        try (Session session = sessionFactory.openSession()) {
            Profile profile1 = session.get(Profile.class, id);
            if (profile1 == null) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }

            List<Profile> matches = session.createQuery(
                            "SELECT p FROM Profile p WHERE p.gender != :gender", Profile.class)
                    .setParameter("gender", profile1.getGender())
                    .getResultList()
                    .stream()
                    .filter(profile -> calculateMatchingScore(profile1, profile) >= 60)
                    .collect(Collectors.toList());

            return matches;
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    @Override
//    public List<Profile> getPartialMatch(int id) {
//        try (Session session = sessionFactory.openSession()) {
//            Profile profile1 = session.get(Profile.class, id);
//            List<Profile> profileList = new ArrayList<>();
//            if (profile1 == null) {
//                throw new UserNotFoundException("User not found with ID: " + id);
//            }
//            List<Question> questions = session.createQuery("SELECT q FROM Question q WHERE q.profile.id = :id", Question.class)
//                    .setParameter("id", profile1.getId())
//                    .getResultList();
//            List<Profile> matches = session.createQuery(
//                            "SELECT p FROM Profile p WHERE p.gender != :gender", Profile.class)
//                    .setParameter("gender", profile1.getGender())
//                    .getResultList();
//
//            for (Profile profile : matches) {
//                double score = calculatePartialMatchScore(questions, profile);
//                if (score >= 60) {
//                    profileList.add(profile);
//                }
//            }
//            return profileList;
//        } catch (UserNotFoundException e) {
//            e.printStackTrace();
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    @Override
    public List<Profile> getPartialMatch(int id) {
        try (Session session = sessionFactory.openSession()) {
            Profile profile1 = session.get(Profile.class, id);
            List<Profile> profileList = new ArrayList<>();
            if (profile1 == null) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }
            List<Question> questions = session.createQuery("SELECT q FROM Question q WHERE q.profile.id = :id", Question.class)
                    .setParameter("id", profile1.getId())
                    .getResultList();
            List<Profile> matches = session.createQuery(
                            "SELECT p FROM Profile p WHERE p.gender != :gender", Profile.class)
                    .setParameter("gender", profile1.getGender())
                    .getResultList();

            for (Profile profile : matches) {
                Map<String, String[]> partialMatchScore = calculatePartialMatchScore(questions, profile);
                double percentage = Double.parseDouble(partialMatchScore.get("matching_percentage")[0]); // Percentage value
                if (percentage >= 60) {
                    profileList.add(profile);
                }
            }
            return profileList;
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Map<String, String[]> calculatePartialMatchScore(List<Question> questions, Profile profile) {
        Map<String, String[]> mismatchedFields = new HashMap<>();

        int totalScore = 0;
        int maxScore = questions.size();

        for (Question question : questions) {
            String questionText = question.getQuestionText().toLowerCase();
            String expectedAnswer;
            String actualAnswer = question.getAnswer().toLowerCase();

            switch (questionText) {
                case "age":
                    expectedAnswer = String.valueOf(profile.getAge());
                    break;
                case "profession":
                    expectedAnswer = profile.getProfession().toLowerCase();
                    break;
                case "favoritecolour":
                    expectedAnswer = profile.getFavoriteColour().toLowerCase();
                    break;
                case "foodtype":
                    expectedAnswer = String.valueOf(profile.getFoodType());
                    break;
                case "favoritepet":
                    expectedAnswer = profile.getFavoritePet().toLowerCase();
                    break;
                case "thoughts":
                    expectedAnswer = String.valueOf(profile.getThoughts());
                    break;
                case "hobby":
                    expectedAnswer = profile.getHobby().toLowerCase();
                    break;
                default:
                    continue;
            }

            boolean match = actualAnswer.equalsIgnoreCase(expectedAnswer);

            if (match) {
                totalScore++;
            } else {
                mismatchedFields.put(questionText, new String[]{expectedAnswer, actualAnswer});
            }
        }
        double percentage = ((double) totalScore / maxScore) * 100;
        mismatchedFields.put("matching_percentage", new String[]{String.valueOf(percentage)});

        return mismatchedFields;
    }

    @Override
    public List<Question> addQuestions(Profile profile, List<Question> questions) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                for (Question question : questions) {
                    question.setProfile(profile);
                    if (question.getQuestionText().equalsIgnoreCase("age")) {
                        question.setAnswer(String.valueOf(profile.getAge()));
                        session.save(question);
                    }
                    if (question.getQuestionText().equalsIgnoreCase("profession")) {
                        question.setAnswer(profile.getProfession());
                        session.save(question);
                    }
                    if (question.getQuestionText().equalsIgnoreCase("favoriteColour")) {
                        question.setAnswer(profile.getFavoriteColour());
                        session.save(question);
                    }
                    if (question.getQuestionText().equalsIgnoreCase("foodType")) {
                        question.setAnswer(String.valueOf(profile.getFoodType()));
                        session.save(question);
                    }
                    if (question.getQuestionText().equalsIgnoreCase("favoritePet")) {
                        question.setAnswer(profile.getFavoritePet());
                        session.save(question);
                    }
                    if (question.getQuestionText().equalsIgnoreCase("thoughts")) {
                        question.setAnswer(String.valueOf(profile.getThoughts()));
                        session.save(question);
                    }
                    if (question.getQuestionText().equalsIgnoreCase("hobby")) {
                        question.setAnswer(profile.getHobby());
                        session.save(question);
                    }
                }
                profile.setQuestions(questions);
                session.saveOrUpdate(profile);
                transaction.commit();
                return questions;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public void addRequests(int id, int anotherId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Profile profile = showProfile(id);
            if (profile == null) {
                throw new UserNotFoundException("This user is not present....");
            }
            Profile otherProfile = showProfile(anotherId);
            if (otherProfile == null) {
                throw new UserNotFoundException("This profile is not exist in our database");
            }
            Long existingRequestCount = session.createQuery(
                            "SELECT COUNT(rc) FROM RequestChart rc " +
                                    "WHERE rc.myProfile.id = :profileId AND rc.otherProfile.id = :otherProfileId", Long.class)
                    .setParameter("profileId", id)
                    .setParameter("otherProfileId", anotherId)
                    .getSingleResult();

            if (existingRequestCount > 0) {
                throw new IllegalArgumentException("You have already followed this profile....");
            }

            RequestChart requestChart = new RequestChart();
            requestChart.setMyProfile(profile);
            requestChart.setOtherProfile(otherProfile);
            session.save(requestChart);
            transaction.commit();
        } catch (UserNotFoundException e) {
            throw e;
        }catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public Map<Profile, Double> sortByDescendingOrder(Profile mainProfile, List<Profile> profileList) {
        Map<Profile, Double> profileScores = new HashMap<>();
        for (Profile profile : profileList) {
            double score = calculateMatchingScore(mainProfile, profile);
            profileScores.put(profile, score);
        }
        Map<Profile, Double> sortedProfileScores = profileScores.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedProfileScores;
    }

    public List<Profile> showAllProfiles(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Profile profile = showProfile(id);
            Query<RequestChart> query = session.createQuery(
                    "SELECT rc FROM RequestChart rc WHERE rc.myProfile = :profile",
                    RequestChart.class
            );
            query.setParameter("profile", profile);
            List<RequestChart> requestCharts = query.getResultList();
            List<Profile> profiles = new ArrayList<>();
            for (RequestChart requestChart : requestCharts) {
                profiles.add(requestChart.getOtherProfile());
            }

            transaction.commit();
            return profiles;
        }
    }

    public List<Profile> fetchOppositeGenderProfiles(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Profile profile = showProfile(id);
            Query<Profile> query = session.createQuery(
                    "SELECT p FROM Profile p WHERE p.gender != :gender",
                    Profile.class
            );
            query.setParameter("gender", profile.getGender());
            List<Profile> profiles = query.getResultList();
            transaction.commit();
            return profiles;
        }
    }
//    public List<Profile> fetchAllProfilesExceptOwnAndUnrequested(int userId) {
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            // Retrieve the user's own profile
//            Profile userProfile = showProfile(userId);
//
//            // Construct HQL query to fetch profiles from Profile table
//            Query<Profile> query = session.createQuery(
//                    "SELECT p FROM Profile p WHERE p.id != :userId AND " +
//                            "p.id NOT IN (SELECT rc.otherProfiles.id FROM RequestChart rc WHERE rc.myProfile.id = :userProfileId)",
//                    Profile.class
//            );
//            query.setParameter("userId", userId);
//            query.setParameter("userProfileId", userProfile.getId());
//
//            // Execute query to get profiles except the user's own and those not present in RequestChart table
//            List<Profile> profiles = query.getResultList();
//
//            transaction.commit();
//            return profiles;
//        }
//    }







}

