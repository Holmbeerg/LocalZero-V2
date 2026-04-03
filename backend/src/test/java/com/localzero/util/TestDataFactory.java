package com.localzero.util;

import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.model.Initiative;
import com.localzero.model.User;
import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;

import java.time.LocalDate;

// Test Fixture
public class TestDataFactory {

    public static User buildDefaultUser(String email, Neighborhood neighborhood) {
        return User.builder()
                .email(email)
                .passwordHash("hashed_password")
                .name("Default User")
                .location(neighborhood)
                .build();
    }

    public static Initiative buildInitiative(String title, boolean isPublic, User creator) {
        return Initiative.builder()
                .title(title)
                .description("A detailed description")
                .location(creator.getLocation())
                .category(InitiativeCategory.COMMUNITY_GARDENING)
                .creator(creator)
                .startDate(LocalDate.now())
                .publicFlag(isPublic)
                .build();
    }

    public static Initiative buildPrivateInitiative(User creator) {
        return buildInitiative("Private Initiative", false, creator);
    }

    public static Initiative buildPublicInitiative(User creator) {
        return buildInitiative("Public Initiative", true, creator);
    }

    public static CreateInitiativeRequest buildCreateInitiativeRequest() {
        return new CreateInitiativeRequest(
                "Test Initiative",
                "A description",
                InitiativeCategory.COMMUNITY_GARDENING,
                true,
                LocalDate.now(),
                null
        );
    }
}