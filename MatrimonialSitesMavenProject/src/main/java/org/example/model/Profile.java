package org.example.model;

import org.example.enums.Confirmation;
import org.example.enums.FoodType;
import org.example.enums.Gender;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    private int age;
    private Gender gender;
    private String profession;
    private String favoriteColour;
    private FoodType foodType;
    private String favoritePet;
    private Confirmation thoughts;
    private String hobby;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    public Profile() {
    }

    public Profile(String fullName, int age, Gender gender, String profession, String favoriteColour, FoodType foodType, String favoritePet, Confirmation thoughts, String hobby) {
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.profession = profession;
        this.favoriteColour = favoriteColour;
        this.foodType = foodType;
        this.favoritePet = favoritePet;
        this.thoughts = thoughts;
        this.hobby = hobby;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getFavoriteColour() {
        return favoriteColour;
    }

    public void setFavoriteColour(String favoriteColour) {
        this.favoriteColour = favoriteColour;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public String getFavoritePet() {
        return favoritePet;
    }

    public void setFavoritePet(String favoritePet) {
        this.favoritePet = favoritePet;
    }

    public Confirmation getThoughts() {
        return thoughts;
    }

    public void setThoughts(Confirmation thoughts) {
        this.thoughts = thoughts;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", profession='" + profession + '\'' +
                ", favoriteColour='" + favoriteColour + '\'' +
                ", foodType=" + foodType +
                ", favoritePet='" + favoritePet + '\'' +
                ", thoughts=" + thoughts +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
