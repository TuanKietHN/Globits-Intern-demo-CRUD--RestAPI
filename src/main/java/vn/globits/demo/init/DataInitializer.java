package vn.globits.demo.init;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.globits.demo.domain.Company;
import vn.globits.demo.domain.Person;
import vn.globits.demo.domain.Role;
import vn.globits.demo.domain.User;
import vn.globits.demo.repository.CompanyRepository;
import vn.globits.demo.repository.PersonRepository;
import vn.globits.demo.repository.RoleRepository;
import vn.globits.demo.repository.UserRepository;

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

    public DataInitializer(PersonRepository personRepository,
                           UserRepository userRepository,
                           CompanyRepository companyRepository,
                           RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("en"));
        Random rnd = new Random();

        // --- SAFETY: chỉ tạo nếu DB trống (để tránh duplicate) ---
        if (roleRepository.count() > 0 || companyRepository.count() > 0
                || personRepository.count() > 0 || userRepository.count() > 0) {
            System.out.println("⚠️  DB already has data -> skip seeding.");
            return;
        }

        // Nếu muốn reset DB mỗi lần khởi động, xóa dòng if ở trên và uncomment phần dưới:
        userRepository.deleteAll();
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

        // === 3. TẠO PERSONS với Faker và liên kết Company ===
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

        // === 4. TẠO USERS với Faker và liên kết Person + Roles ===
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
        System.out.println("========================================");
        System.out.println("✅ Database seeding completed successfully!");
        System.out.println("   - Roles: " + roles.size());
        System.out.println("   - Companies: " + companies.size());
        System.out.println("   - Persons: " + savedPersons.size());
        System.out.println("   - Users: 100");
        System.out.println("========================================");
    }
}