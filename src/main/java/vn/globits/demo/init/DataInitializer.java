package vn.globits.demo.init;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.globits.demo.domain.Company;
import vn.globits.demo.domain.Person;
import vn.globits.demo.domain.Role;
import vn.globits.demo.domain.User;
import vn.globits.demo.domain.Department;
import vn.globits.demo.domain.Project;
import vn.globits.demo.domain.Task;
import vn.globits.demo.repository.CompanyRepository;
import vn.globits.demo.repository.PersonRepository;
import vn.globits.demo.repository.RoleRepository;
import vn.globits.demo.repository.UserRepository;
import vn.globits.demo.repository.DepartmentRepository;
import vn.globits.demo.repository.ProjectRepository;
import vn.globits.demo.repository.TaskRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DataInitializer(PersonRepository personRepository,
                           UserRepository userRepository,
                           CompanyRepository companyRepository,
                           RoleRepository roleRepository,
                           DepartmentRepository departmentRepository,
                           ProjectRepository projectRepository,
                           TaskRepository taskRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("vi"));
        Random rnd = new Random();

        // --- SAFETY: chỉ tạo nếu DB trống (để tránh duplicate) ---
        if (roleRepository.count() > 0 || companyRepository.count() > 0
                || personRepository.count() > 0 || userRepository.count() > 0) {
            System.out.println("⚠️  DB already has data -> skip seeding.");
            return;
        }

        // Nếu muốn reset DB mỗi lần khởi động, xóa dòng if ở trên và uncomment phần dưới:
        userRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        departmentRepository.deleteAll();
        personRepository.deleteAll();
        companyRepository.deleteAll();
        roleRepository.deleteAll();

        // === 1. TẠO ROLES với Faker ===
        List<Role> roles = new ArrayList<>();
        String[] roleNames = {"ADMIN", "MANAGER", "USER", "GUEST", "SUPERVISOR", "DEVELOPER"};

        for (String roleName : roleNames) {
            Role role = new Role();
            role.setRole(roleName);
            role.setDescription(faker.job().title() + " - " + faker.company().bs());
            roles.add(roleRepository.save(role));
        }

        System.out.println("✅ Seeded " + roles.size() + " Role records.");

        // === 2. TẠO COMPANIES với Faker ===
        List<Company> companies = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Company company = new Company();
            company.setName(faker.company().name());
            company.setCode("COMP" + String.format("%03d", i + 1));
            company.setAddress(faker.address().fullAddress());
            companies.add(companyRepository.save(company));
        }

        System.out.println("✅ Seeded " + companies.size() + " Company records.");

        // === 3. TẠO DEPARTMENTS cho mỗi Company ===
        List<Department> allDepartments = new ArrayList<>();
        for (Company c : companies) {
            // Tạo 3 phòng ban top-level
            List<Department> tops = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Department d = new Department();
                d.setCode(c.getCode() + "-DEPT" + (i + 1));
                d.setName("Phòng " + faker.job().position());
                d.setCompany(c);
                tops.add(departmentRepository.save(d));
                allDepartments.add(d);
            }
            // Mỗi phòng tạo 1-2 phòng ban con
            for (Department parent : tops) {
                int childCount = rnd.nextInt(2) + 1;
                for (int j = 0; j < childCount; j++) {
                    Department child = new Department();
                    child.setCode(parent.getCode() + "-" + (j + 1));
                    child.setName("Bộ phận " + faker.job().keySkills());
                    child.setCompany(c);
                    child.setParent(parent);
                    allDepartments.add(departmentRepository.save(child));
                }
            }
        }

        System.out.println("✅ Seeded " + allDepartments.size() + " Department records.");

        // === 4. TẠO PERSONS với Faker và liên kết Company ===
        List<Person> savedPersons = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Person p = new Person();
            p.setFullName(faker.name().fullName());
            p.setGender(rnd.nextBoolean() ? "Male" : "Female");

            // faker.date().birthday trả về java.util.Date -> convert sang LocalDate
            Date d = faker.date().birthday(18, 65);
            LocalDate ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            p.setBirthDate(ld);

            p.setPhoneNumber(faker.phoneNumber().cellPhone());
            p.setAddress(faker.address().fullAddress());

            // Gán ngẫu nhiên một company cho person
            Company randomCompany = companies.get(rnd.nextInt(companies.size()));
            p.setCompany(randomCompany);

            savedPersons.add(personRepository.save(p));
        }

        System.out.println("✅ Seeded " + savedPersons.size() + " Person records.");

        // === 5. TẠO USERS với Faker và liên kết Person + Roles ===
        for (int i = 0; i < 100; i++) {
            User u = new User();
            // Tạo email ngẫu nhiên từ faker - đảm bảo unique
            String username = faker.name().username();
            String domain = faker.internet().domainName();
            u.setEmail(username + "@" + domain);

            // Tạo password ngẫu nhiên từ faker
            u.setPassword(faker.internet().password(8, 16)); // password 8-16 ký tự
            u.setIsActive(rnd.nextBoolean()); // Random active status

            // Gán person tương ứng (không bắt buộc nếu person_id có thể NULL)
            u.setPerson(savedPersons.get(i));

            // Gán ngẫu nhiên 1-3 roles cho mỗi user
            Set<Role> userRoles = new HashSet<>();
            int numRoles = rnd.nextInt(3) + 1; // 1 đến 3 roles

            for (int j = 0; j < numRoles; j++) {
                Role randomRole = roles.get(rnd.nextInt(roles.size()));
                userRoles.add(randomRole);
            }

            u.setRoles(userRoles);
            userRepository.save(u);
        }

        System.out.println("✅ Seeded 100 User records with roles.");

        // === 6. TẠO PROJECTS theo mỗi Company, chọn Persons trong Company ===
        List<Project> projects = new ArrayList<>();
        for (Company c : companies) {
            // Lấy persons thuộc company
            List<Person> personsOfCompany = savedPersons.stream()
                    .filter(pp -> pp.getCompany() != null && pp.getCompany().getId().equals(c.getId()))
                    .toList();
            int projectCount = rnd.nextInt(3) + 2; // 2-4 dự án mỗi company
            for (int i = 0; i < projectCount; i++) {
                Project pr = new Project();
                pr.setCode(c.getCode() + "-PRJ" + (i + 1));
                pr.setName("Dự án " + faker.app().name());
                pr.setDescription(faker.company().bs());
                pr.setCompany(c);
                // Chọn 3-8 người tham gia
                Set<Person> members = new HashSet<>();
                int memberCount = Math.min(personsOfCompany.size(), rnd.nextInt(6) + 3);
                for (int j = 0; j < memberCount; j++) {
                    members.add(personsOfCompany.get(rnd.nextInt(personsOfCompany.size())));
                }
                pr.setPersons(members);
                projects.add(projectRepository.save(pr));
            }
        }
        System.out.println("✅ Seeded " + projects.size() + " Project records.");

        // === 7. TẠO TASKS cho mỗi Project, gán Person trong Project ===
        int totalTasks = 0;
        String[] priorities = {"1", "2", "3"}; // 1 Cao, 2 Trung bình, 3 Thấp
        String[] statuses = {"1", "2", "3", "4"}; // 1 Mới tạo, 2 Đang làm, 3 Hoàn thành, 4 Tạm hoãn
        for (Project pr : projects) {
            List<Person> prPersons = new ArrayList<>(pr.getPersons());
            int taskCount = rnd.nextInt(6) + 10; // 10-15 task mỗi project
            for (int t = 0; t < taskCount; t++) {
                Task task = new Task();
                task.setProject(pr);
                task.setPerson(prPersons.isEmpty() ? null : prPersons.get(rnd.nextInt(prPersons.size())));
                task.setName("Task " + faker.hacker().verb() + " " + faker.hacker().noun());
                task.setDescription(faker.lorem().sentence());
                // Thời gian giả lập
                Date start = faker.date().past(60, java.util.concurrent.TimeUnit.DAYS);
                Date end = faker.date().future(60, java.util.concurrent.TimeUnit.DAYS);
                task.setStartTime(start);
                task.setEndTime(end);
                task.setPriority(Integer.parseInt(priorities[rnd.nextInt(priorities.length)]));
                task.setStatus(Integer.parseInt(statuses[rnd.nextInt(statuses.length)]));
                taskRepository.save(task);
                totalTasks++;
            }
        }
        System.out.println("✅ Seeded " + totalTasks + " Task records.");
        System.out.println("========================================");
        System.out.println("✅ Database seeding completed successfully!");
        System.out.println("   - Roles: " + roles.size());
        System.out.println("   - Companies: " + companies.size());
        System.out.println("   - Departments: " + allDepartments.size());
        System.out.println("   - Persons: " + savedPersons.size());
        System.out.println("   - Users: 100");
        System.out.println("   - Projects: " + projects.size());
        System.out.println("   - Tasks: " + totalTasks);
        System.out.println("========================================");
    }
}
