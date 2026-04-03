package com.localzero.repository;

import com.localzero.config.TestcontainersConfig;
import com.localzero.model.Initiative;
import com.localzero.model.User;
import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test") // We don't want seeded data
class InitiativeRepositoryTest {

    @Autowired private InitiativeRepository initiativeRepository;
    @Autowired private UserRepository userRepository;

    private User centrumCreator;
    private User centrumNeighbor;
    private User hyllieOutsider;

    @BeforeEach
    void setUp() {
        centrumCreator = userRepository.save(User.builder()
                .email("creator@example.com")
                .passwordHash("hashed_password")
                .name("Centrum Creator")
                .location(Neighborhood.CENTRUM)
                .build());

        centrumNeighbor = userRepository.save(User.builder()
                .email("neighbor@example.com")
                .passwordHash("hashed_password")
                .name("Centrum Neighbor")
                .location(Neighborhood.CENTRUM)
                .build());

        hyllieOutsider = userRepository.save(User.builder()
                .email("outsider@example.com")
                .passwordHash("hashed_password")
                .name("Hyllie Outsider")
                .location(Neighborhood.HYLLIE)
                .build());
    }

    private Initiative buildInitiative(String title, boolean isPublic, User creator) {
        return Initiative.builder()
                .title(title)
                .description("A description")
                .location(creator.getLocation())
                .category(InitiativeCategory.COMMUNITY_GARDENING)
                .creator(creator)
                .startDate(LocalDate.now())
                .publicFlag(isPublic)
                .build();
    }

    @Test
    void save_persistsInitiative() {
        Initiative saved = initiativeRepository.save(
                buildInitiative("Clean up the park", true, centrumCreator));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Clean up the park");
    }

    @Test
    void findAllAccessibleByUser_returnsPublicInitiativesFromAnyNeighborhood() {
        initiativeRepository.save(buildInitiative("Public Centrum Event", true, centrumCreator));

        List<Initiative> result = initiativeRepository
                .findAllAccessibleByUser(hyllieOutsider, hyllieOutsider.getLocation());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Public Centrum Event");
    }

    @Test
    void findAllAccessibleByUser_returnsPrivateInitiativesInSameNeighborhood() {
        initiativeRepository.save(buildInitiative("Private Centrum Event", false, centrumCreator));

        List<Initiative> result = initiativeRepository
                .findAllAccessibleByUser(centrumNeighbor, centrumNeighbor.getLocation());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Private Centrum Event");
    }

    @Test
    void findAllAccessibleByUser_doesNotReturnPrivateInitiativesFromOtherNeighborhoods() {
        initiativeRepository.save(buildInitiative("Private Centrum Event", false, centrumCreator));

        List<Initiative> result = initiativeRepository
                .findAllAccessibleByUser(hyllieOutsider, hyllieOutsider.getLocation());

        assertThat(result).isEmpty();
    }

    @Test
    void findAllAccessibleByUser_returnsCreatorsOwnPrivateInitiatives() {
        initiativeRepository.save(buildInitiative("My private event", false, centrumCreator));

        List<Initiative> result = initiativeRepository
                .findAllAccessibleByUser(centrumCreator, centrumCreator.getLocation());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("My private event");
    }

    @Test
    void findAccessibleById_returnsEmptyForPrivateEventInDifferentNeighborhood() {
        Initiative saved = initiativeRepository.save(
                buildInitiative("Private Centrum Event", false, centrumCreator));

        Optional<Initiative> result = initiativeRepository
                .findAccessibleById(saved.getId(), hyllieOutsider, hyllieOutsider.getLocation());

        assertThat(result).isEmpty();
    }

    @Test
    void findAccessibleById_returnsInitiativeForPrivateEventInSameNeighborhood() {
        Initiative saved = initiativeRepository.save(
                buildInitiative("Private Centrum Event", false, centrumCreator));

        Optional<Initiative> result = initiativeRepository
                .findAccessibleById(saved.getId(), centrumNeighbor, centrumNeighbor.getLocation());

        assertThat(result).isPresent();
    }

    @Test
    void isMember_returnsFalseWhenUserIsNotMember() {
        Initiative saved = initiativeRepository.save(
                buildInitiative("Some event", true, centrumCreator));

        boolean result = initiativeRepository.isMember(saved.getId(), centrumNeighbor);

        assertThat(result).isFalse();
    }
}