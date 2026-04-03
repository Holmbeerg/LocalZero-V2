package com.localzero.service;

import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.exception.AlreadyInitiativeMemberException;
import com.localzero.exception.InitiativeNotFoundException;
import com.localzero.model.Initiative;
import com.localzero.model.InitiativeMember;
import com.localzero.model.User;
import com.localzero.model.enums.Neighborhood;
import com.localzero.model.enums.NotificationType;
import com.localzero.repository.InitiativeMemberRepository;
import com.localzero.repository.InitiativeRepository;
import com.localzero.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InitiativeServiceTest {

    @Mock
    private InitiativeRepository initiativeRepository;
    @Mock
    private InitiativeMemberRepository initiativeMemberRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;

    @InjectMocks
    private InitiativeService initiativeService;

    private User creator;
    private User joiner;
    private Initiative initiative;
    private CreateInitiativeRequest request;

    @BeforeEach
    void setUp() {
        creator = TestDataFactory.buildDefaultUser("creator@example.com", Neighborhood.CENTRUM);
        joiner = TestDataFactory.buildDefaultUser("joiner@example.com", Neighborhood.CENTRUM);
        initiative = TestDataFactory.buildPrivateInitiative(creator);
        request = TestDataFactory.buildCreateInitiativeRequest();
    }

    private void mockInitiativeFound() {
        when(initiativeRepository.findAccessibleById(1L, joiner, joiner.getLocation()))
                .thenReturn(Optional.of(initiative));
    }

    private void mockInitiativeNotFound() {
        when(initiativeRepository.findAccessibleById(1L, joiner, joiner.getLocation()))
                .thenReturn(Optional.empty());
    }

    @Test
    void getInitiativeByIdIfAccessible_returnsInitiativeWhenFound() {
        mockInitiativeFound();

        Initiative result = initiativeService.getInitiativeByIdIfAccessible(1L, joiner);

        assertThat(result).isEqualTo(initiative);
    }

    @Test
    void getInitiativeByIdIfAccessible_throwsWhenNotFound() {
        mockInitiativeNotFound();

        assertThatThrownBy(() -> initiativeService.getInitiativeByIdIfAccessible(1L, joiner))
                .isInstanceOf(InitiativeNotFoundException.class);
    }

    @Test
    void joinInitiative_throwsWhenInitiativeNotFound() {
        mockInitiativeNotFound();

        assertThatThrownBy(() -> initiativeService.joinInitiative(1L, joiner))
                .isInstanceOf(InitiativeNotFoundException.class);
    }

    @Test
    void joinInitiative_throwsWhenAlreadyMember() {
        mockInitiativeFound();
        when(initiativeRepository.isMember(1L, joiner)).thenReturn(true);

        assertThatThrownBy(() -> initiativeService.joinInitiative(1L, joiner))
                .isInstanceOf(AlreadyInitiativeMemberException.class);
    }

    @Test
    void joinInitiative_savesMemberWhenValid() {
        mockInitiativeFound();
        when(initiativeRepository.isMember(1L, joiner)).thenReturn(false);

        initiativeService.joinInitiative(1L, joiner);

        verify(initiativeMemberRepository).save(any(InitiativeMember.class));
    }

    @Test
    void joinInitiative_sendsNotificationWhenValid() {
        mockInitiativeFound();
        when(initiativeRepository.isMember(1L, joiner)).thenReturn(false);

        initiativeService.joinInitiative(1L, joiner);

        verify(notificationService).createAndAssignNotification(
                eq(NotificationType.JOIN_INITIATIVE),
                eq(creator),
                eq(joiner),
                argThat(map ->
                        map.get("initiative").equals(initiative.getTitle()) &&
                                map.get("joinedBy").equals(joiner.getName())
                )
        );
    }

    @Test
    void createInitiative_savesInitiativeAndAddsMember() {
        when(initiativeRepository.save(any(Initiative.class))).thenReturn(initiative);
        when(userService.findByLocation(any())).thenReturn(List.of());

        Initiative result = initiativeService.createInitiative(request, creator);

        verify(initiativeRepository).save(any(Initiative.class));
        verify(initiativeMemberRepository).save(any(InitiativeMember.class));

        assertThat(result).isEqualTo(initiative);
    }

    @Test
    void createInitiative_doesNotNotifyCreator() {
        User neighbor = TestDataFactory.buildDefaultUser("neighbor@example.com", Neighborhood.CENTRUM);

        when(initiativeRepository.save(any(Initiative.class))).thenReturn(initiative);
        when(userService.findByLocation(any())).thenReturn(List.of(creator, neighbor));

        initiativeService.createInitiative(request, creator);

        verify(notificationService).createAndAssignNotification(
                eq(NotificationType.NEW_INITIATIVE),
                eq(List.of(neighbor)),
                eq(creator),
                any()
        );
    }
}
