package vn.globits.demo.dto;

import vn.globits.demo.domain.Task;

import java.util.Date;

public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer priority;
    private Integer status;
    private Long projectId;
    private Long personId;

    public TaskDTO() {}

    public TaskDTO(Task t) {
        if (t != null) {
            this.id = t.getId();
            this.name = t.getName();
            this.description = t.getDescription();
            this.startTime = t.getStartTime();
            this.endTime = t.getEndTime();
            this.priority = t.getPriority();
            this.status = t.getStatus();
            this.projectId = t.getProject() != null ? t.getProject().getId() : null;
            this.personId = t.getPerson() != null ? t.getPerson().getId() : null;
        }
    }

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
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getPersonId() { return personId; }
    public void setPersonId(Long personId) { this.personId = personId; }
}
