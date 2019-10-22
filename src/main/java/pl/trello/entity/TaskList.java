package pl.trello.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tasklists")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskListId;

    @ManyToOne
    @JoinColumn(name = "boardId", referencedColumnName = "boardId")
    private Board board;

    @OneToMany( mappedBy = "taskList", fetch = FetchType.EAGER)
    private List<Task> tasks = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer position;
}
