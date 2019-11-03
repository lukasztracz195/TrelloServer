package pl.trello.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.trello.enums.InvitationState;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "memberId")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "recipient", referencedColumnName = "memberId")
    private Member recipient;

    @ManyToOne
    @JoinColumn(name = "boardId", referencedColumnName = "boardId")
    private Board board;

    @Enumerated(EnumType.STRING)
    private InvitationState state;
}
