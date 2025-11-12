package vn.globits.demo.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    private int priority; // 1: Cao, 2: Trung bình, 3: Thấp
    private int status;   // 1: Mới tạo, 2: Đang làm, 3: Hoàn thành, 4: Tạm hoãn

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project; // Quan hệ ManyToOne với Project

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person; // Quan hệ ManyToOne với Person (ai làm công việc này)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
}

