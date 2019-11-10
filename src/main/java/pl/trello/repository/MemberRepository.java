package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.trello.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberId(long memberId);

}