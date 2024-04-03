package org.example.controller;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import org.example.enums.Confirmation;
import org.example.enums.FoodType;
import org.example.enums.Gender;
import org.example.exceptions.InvalidFormatException;
import org.example.exceptions.NullValueException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Profile;
import org.example.model.Question;
import org.example.model.RequestChart;
import org.example.service.serviceImpl.MatrimonialSiteImpl;
import org.example.validator.Validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MatrimonialSiteImpl matrimonialSite = new MatrimonialSiteImpl();
        System.out.println("**** WELCOME IN OUR Matrimonial Site ****");
        int choice = 0;
        do {
            System.out.println("Choose an option:");
            System.out.println("1. Add user details");
            System.out.println("2. Print profile details");
            System.out.println("3. Search matches with your profile");
            System.out.println("4. Checking the match score");
            System.out.println("5. Add your mandatory questions");
            System.out.println("6. Search matches for according to your mandatory questions");
            System.out.println("7. Checking Match score for mandatory questions");
            System.out.println("8. Descending sorted profiles according to matching score");
            choice = sc.nextInt();
            try {
                switch (choice) {
                    case 1:
                        System.out.println("Enter the details for the new profile:");
                        sc.nextLine();
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Age: ");
                        int age = sc.nextInt();
                        if (age <= 0) {
                            throw new InvalidFormatException("Please enter age in valid format");
                        }
                        sc.nextLine();
                        System.out.print("Gender: ");
                        Gender gender = Gender.valueOf(sc.next().toUpperCase());
                        sc.nextLine();
                        System.out.print("Profession: ");
                        String profession = sc.next();
                        if (profession.length() > 15) {
                            throw new InvalidFormatException("Please enter valid profession");
                        }
                        sc.nextLine();
                        System.out.print("Hobby: ");
                        String hobby = sc.next();
                        if (hobby.length() > 10) {
                            throw new InvalidFormatException("Please enter valid hobby");
                        }
                        sc.nextLine();
                        System.out.print("Favorite Colour: ");
                        String favoriteColour = sc.next();
                        if (favoriteColour.length() > 15) {
                            throw new InvalidFormatException("Please enter valid colour");
                        }
                        sc.nextLine();
                        System.out.print("Food Type: ");
                        FoodType foodType = FoodType.valueOf(sc.next().toUpperCase());
                        sc.nextLine();
                        System.out.print("Favorite Pet: ");
                        String favoritePet = sc.next();
                        if (favoritePet.length() > 15) {
                            throw new InvalidFormatException("Please enter valid pet type");
                        }
                        sc.nextLine();
                        System.out.print("Are you want to live in join family: ");
                        Confirmation confirmation = Confirmation.valueOf(sc.next().toUpperCase());
                        sc.nextLine();
                        Profile profile = new Profile(name, age, gender, profession, favoriteColour, foodType, favoritePet, confirmation, hobby);
                        Validations.nullCheck(profile);
                        String status = matrimonialSite.createProfile(profile);
                        System.out.println(status);
                        break;

                    case 2:
                        System.out.println("Showing profile....");
                        System.out.print("Id: ");
                        int userId = sc.nextInt();
                        try {
                            Profile shownProfile = matrimonialSite.showProfile(userId);
                            System.out.println(shownProfile);
                        } catch (UserNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.println("Search matches for you...");
                        System.out.println("Enter your id :");
                        int userId1 = sc.nextInt();
                        List<Profile> profiles = matrimonialSite.searchMatches(userId1);
                        if (profiles.isEmpty()) {
                            System.out.println("No matching profiles found.");
                        } else {
                            System.out.println("These are some matching profiles:");
                            for (Profile p : profiles) {
                                System.out.println(p);
                            }
                        }
                        sc.nextLine();
                        System.out.println("Do you want to follow any profile?");
                        String answer = sc.nextLine().toLowerCase();
                        if (answer.equals("yes")) {
                            System.out.println("Enter the number of profiles you want to follow:");
                            int no = sc.nextInt();
                            sc.nextLine();
                            int[] arr = new int[no];
                            System.out.println("Enter the IDs you want to follow:");
                            for (int i = 0; i < arr.length; i++) {
                                arr[i] = sc.nextInt();
                                matrimonialSite.addRequests(userId1, arr[i]);
                            }
                            System.out.println("You have successfully follow that profiles...");
                        } else {
                            System.out.println("You chose not to follow any profile.");
                        }

                        break;

                    case 4:
                        System.out.println("Checking Match score....");
                        System.out.println("Enter your id :");
                        int yourId = sc.nextInt();
                        System.out.println("Enter id of the profile you want to check :");
                        int otherId = sc.nextInt();
                        try {
                            Profile myProfile = matrimonialSite.showProfile(yourId);
                            Profile otherProfile = matrimonialSite.showProfile(otherId);
                            double matchingScore = matrimonialSite.calculateMatchingScore(myProfile, otherProfile);
                            System.out.println(matchingScore + "% is your matching score");
                        } catch (UserNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.println("Add your mandatory questions....");
                        System.out.println("Enter your profile ID:");
                        int profileId = sc.nextInt();
                        sc.nextLine();
                        try {
                            Profile showProfile = matrimonialSite.showProfile(profileId);
                            System.out.println("Enter the number of questions you want to add:");
                            int numQuestions = sc.nextInt();
                            sc.nextLine();
                            List<Question> questions = new ArrayList<>();
                            for (int i = 0; i < numQuestions; i++) {
                                System.out.println("Enter question " + (i + 1) + ":");
                                String questionText = sc.nextLine();
                                Question question = new Question();
                                question.setQuestionText(questionText);
                                questions.add(question);
                            }
                            List<Question> questionList = matrimonialSite.addQuestions(showProfile, questions);
                            System.out.println(questionList);
                        } catch (UserNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 6:
                        System.out.println("Search matches for you...");
                        System.out.println("Enter your profile ID:");
                        int id = sc.nextInt();
                        sc.nextLine();
                        List<Profile> partialMatch = matrimonialSite.getPartialMatch(id);
                        if (partialMatch.isEmpty()) {
                            System.out.println("No matching profiles found.");
                        } else {
                            System.out.println("These are some matching profiles:");
                            for (Profile p : partialMatch) {
                                System.out.println(p);
                            }
                        }
                        sc.nextLine();
                        System.out.println("Do you want to follow any profile?");
                        String ans = sc.nextLine().toLowerCase();
                        if (ans.equals("yes")) {
                            System.out.println("Enter the number of profiles you want to follow:");
                            int no = sc.nextInt();
                            if (no <= 0) {
                                throw new InvalidFormatException("Please enter value in valid format");
                            }
                            sc.nextLine();
                            int[] arr = new int[no];
                            System.out.println("Enter the IDs you want to follow:");
                            for (int i = 0; i < arr.length; i++) {
                                arr[i] = sc.nextInt();
                                matrimonialSite.addRequests(id, arr[i]);
                                System.out.println("You have successfully follow that profiles...");
                            }
                        } else {
                            System.out.println("You chose not to follow any profile.");
                        }
                        break;

                    case 7:
                        System.out.println("Checking Match score....");
                        System.out.println("Enter your id :");
                        int uId = sc.nextInt();
                        System.out.println("Enter id of the profile you want to check :");
                        int oId = sc.nextInt();
                        Profile myProfile = matrimonialSite.showProfile(uId);
                        Profile otherProfile = matrimonialSite.showProfile(oId);
                        Map<String, String[]> partialMatchScore = matrimonialSite.calculatePartialMatchScore(myProfile.getQuestions(), otherProfile);
                        System.out.println("Mismatched Fields:");
                        boolean foundMismatch = false;
                        for (Map.Entry<String, String[]> entry : partialMatchScore.entrySet()) {
                            String fieldName = entry.getKey();
                            String[] values = entry.getValue();
                            if (values.length <= 3) { // Ensure the array has at least three elements
                                if (fieldName.equalsIgnoreCase("matching_percentage")) {
                                    double percentage = Double.parseDouble(values[0]);
                                    System.out.println("Percentage Match: " + percentage + "%"); // Print percentage value
                                }else {
                                    String expectedValue = values[0];
                                    String actualValue = values[1];


                                    if (!fieldName.equals("matching_percentage")) {
                                        foundMismatch = true;
                                        System.out.println("Field: " + fieldName);
                                        System.out.println("Expected Value: " + expectedValue);
                                        System.out.println("Actual Value: " + actualValue);

                                    }
                                }
                            } else {
                                System.out.println("Invalid format for mismatched field: " + fieldName);
                            }
                        }

                        if (!foundMismatch) {
                            System.out.println("No mismatched fields found.");
                        }
                        System.out.println();
                        break;

                    case 8:
                        System.out.println("Print all the matching profiles in descending order of matching score");
                        System.out.println("Enter your id :");
                        int pId = sc.nextInt();
                        Profile profile1 = matrimonialSite.showProfile(pId);
                        List<Profile> profileList = matrimonialSite.searchMatches(pId);
                        Map<Profile, Double> sortedByDescendingOrder = matrimonialSite.sortByDescendingOrder(profile1, profileList);
                        for (Map.Entry<Profile, Double> entry : sortedByDescendingOrder.entrySet()) {
                            Profile pro = entry.getKey();
                            double score = entry.getValue();
                            System.out.println("Profile: " + pro + ", Matching Score: " + score);
                        }
                        break;

                    case 9:
                        System.out.println("Showing all followed profiles....");
                        System.out.println("Enter your id :");
                        int upId = sc.nextInt();
                        List<Profile> profileList1 = matrimonialSite.showAllProfiles(upId);
                        for (Profile requestChart : profileList1) {
                            System.out.println(requestChart +" ");
                        }
                        break;

                    case 10:
                        System.out.println("Enter your id :");
                        int up1Id = sc.nextInt();
                        List<Profile> list = matrimonialSite.fetchOppositeGenderProfiles(up1Id);
                        List<Integer> profileLists =  new ArrayList<>();
                        for (Profile p: list) {
                            System.out.println(p+" ");
                        }
                        sc.nextLine();
                        System.out.println("Do you want to follow any profile?");
                        String an = sc.nextLine().toLowerCase();
                        if (an.equals("yes")) {
                            System.out.println("Enter the number of profiles you want to follow:");
                            int no = sc.nextInt();
                            sc.nextLine();
                            int[] arr = new int[no];
                            System.out.println("Enter the IDs you want to follow:");
                            for (int i = 0; i < arr.length; i++) {
                                arr[i] = sc.nextInt();
                                matrimonialSite.addRequests(up1Id, arr[i]);
                                profileLists.add(arr[i]);
                            }
                            System.out.println("You have successfully follow that profiles...");
                        } else {
                            System.out.println("You chose not to follow any profile.");
                        }
                        break;

//                    case 11:
//
//                        System.out.println("Enter your id :");
//                        int up2Id = sc.nextInt();
//                        List<Profile> unrequested = matrimonialSite.fetchAllProfilesExceptOwnAndUnrequested(up2Id);
//                        for (Profile p: unrequested) {
//                            System.out.println(p+" ");
//                        }

                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");

                }
            } catch (NullValueException | InvalidFormatException | UserNotFoundException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 0);
    }
}
